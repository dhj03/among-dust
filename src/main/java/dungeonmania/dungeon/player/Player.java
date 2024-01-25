package dungeonmania.dungeon.player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.json.JSONObject;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.dungeon.battle.BattleModifier;
import dungeonmania.dungeon.battle.Fighter;
import dungeonmania.dungeon.battle.Stats;
import dungeonmania.dungeon.entities.Entity;
import dungeonmania.dungeon.entities.EntityFactory;
import dungeonmania.dungeon.entities.buildableentities.Bow;
import dungeonmania.dungeon.entities.buildableentities.MidnightArmour;
import dungeonmania.dungeon.entities.buildableentities.Shield;
import dungeonmania.dungeon.entities.physicalentities.Tickable;
import dungeonmania.dungeon.entities.physicalentities.collectableentities.CollectableEntity;
import dungeonmania.dungeon.entities.physicalentities.collectableentities.SunStone;
import dungeonmania.dungeon.entities.physicalentities.collisionstrategies.CollisionStrategy;
import dungeonmania.dungeon.entities.physicalentities.collisionstrategies.PlayerCollisionStrategy;
import dungeonmania.dungeon.entities.physicalentities.movingentities.Ally;
import dungeonmania.dungeon.entities.physicalentities.movingentities.Mercenary;
import dungeonmania.dungeon.entities.physicalentities.movingentities.Spider;
import dungeonmania.dungeon.entities.physicalentities.movingentities.ZombieToast;
import dungeonmania.dungeon.entities.physicalentities.staticentities.Boulder;
import dungeonmania.dungeon.entities.physicalentities.staticentities.Door;
import dungeonmania.dungeon.entities.physicalentities.staticentities.Exit;
import dungeonmania.dungeon.entities.physicalentities.staticentities.Portal;
import dungeonmania.dungeon.entities.physicalentities.staticentities.SwampTile;
import dungeonmania.dungeon.entities.physicalentities.staticentities.SwitchDoor;
import dungeonmania.dungeon.entities.physicalentities.staticentities.Wall;
import dungeonmania.dungeon.entities.physicalentities.staticentities.ZombieToastSpawner;
import dungeonmania.dungeon.entities.physicalentities.staticentities.activatable.Activatable;
import dungeonmania.dungeon.player.playerstate.observer.PlayerStatePublisher;
import dungeonmania.dungeon.player.playerstate.observer.PlayerStateSubscriber;
import dungeonmania.dungeon.player.playerstate.states.InvinciblePlayerState;
import dungeonmania.dungeon.player.playerstate.states.NormalPlayerState;
import dungeonmania.dungeon.player.playerstate.states.PlayerState;
import dungeonmania.dungeon.player.playerstate.states.TemporaryPlayerState;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.exceptions.InvalidMovementException;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Player implements Visitor, PlayerStatePublisher, Tickable, Fighter, Serializable {
    private static int attack;
    private static int health;
    private Stats stats;
    private Queue<TemporaryPlayerState> stateQueue;
    private Position position;
    private Position oldPosition;
    private Inventory inventory;
    private int killCount;
    private CollisionStrategy collisionStrategy = new PlayerCollisionStrategy();
    private List<PlayerStateSubscriber> subscribers = new ArrayList<>();
    private Dungeon dungeon;
    public final static String TYPE = "player";
    private static final List<String> UNLOCKED_BUILDABLES = new ArrayList<>(Arrays.asList(Bow.TYPE, Shield.TYPE, MidnightArmour.TYPE));

    public Player(Dungeon dungeon, Position position) {
        this.stateQueue = new LinkedBlockingQueue<>();
        this.position = position;
        this.oldPosition = position;
        this.inventory = new Inventory();
        this.dungeon = dungeon;
        this.killCount = 0;
        this.stats = new Stats(health, attack);
    }

    public Player(Dungeon dungeon, JSONObject JSONEntity) {
        this(dungeon, new Position(JSONEntity.getInt("x"), JSONEntity.getInt("y")));
    }

    public Player(Dungeon newDungeon, Player old) {
        this.stateQueue = old.stateQueue; // Since state shouldn't change between movement
        this.position = new Position(old.getPosition().getX(), old.getPosition().getY());
        this.oldPosition = new Position(old.getOldPosition().getX(), old.getOldPosition().getY());
        this.inventory = new Inventory(old.inventory);
        this.dungeon = newDungeon;
        this.stats = old.stats;
        this.killCount = old.killCount;
    }

    public static void configure(JSONObject config) {
        attack = config.getInt("player_attack");
        health = config.getInt("player_health");
    }

    public int getKillCount() {
        return killCount;
    }

    public void incrementKillCount() {
        killCount++;
    }

    public int getNumInInventory(String type) {
        return inventory.countType(type);
    }

    public Position getPosition() {
        return position;
    }

    public Position getOldPosition() {
        return oldPosition;
    }

    public String getId() {
        return TYPE;
    }

    @Override
    public Stats getStats() {
        return stats;
    }

    public List<Equipment> getListEquipment() {
        return inventory.getListEquipment();
    }

    public EntityResponse getEntityResponse() {
        return new EntityResponse(
            getId(),
            TYPE,
            getPosition(),
            false
        );
    }

    public List<ItemResponse> getInventoryResponse() {
        return inventory.getResponse();
    }

    public ItemResponse getPotionResponse() {
        if (stateQueue.isEmpty()) {
            return null;
        }
        TemporaryPlayerState currentState = stateQueue.peek();
        return currentState.getItemResponse();
    }

    public void addToInventory(Entity entity) {
        inventory.add(entity);
    }

    public boolean openDoor(Door door) {
        if (inventory.countType(SunStone.TYPE) > 0 || inventory.removeKey(door.getKeyNum())) {
            door.unlock();
            return true;
        }
        return false;
    }

    public boolean openDoor(SwitchDoor switchDoor) {
        if (inventory.removeKey(switchDoor.getKeyNum())) {
            switchDoor.unlock();
            return true;
        }
        return false;
    }

    public void build(String buildable) throws InvalidActionException {
        inventory.build(EntityFactory.createBuildableFromType(dungeon, buildable));
    }

    public List<String> getAvailableBuildables() {
        List<String> buildables = new ArrayList<>();

        // Check all unlocked recipes (by default starts with bow and shield)
        for (String buildable : UNLOCKED_BUILDABLES) {
            try {
                // Get a copy of the inventory and try building the buildable
                Inventory copyInventory = new Inventory(inventory);
                copyInventory.build(EntityFactory.createBuildableFromType(dungeon, buildable));
                // Success, add
                buildables.add(buildable);
            } catch (InvalidActionException e) {
                // Can't build, continue to next unlocked recipe
                continue;
            }
        }
        return buildables;
    }

    public void enqueueState(TemporaryPlayerState state) {
        stateQueue.add(state);
    }

    public void useItem(String itemUsedId) throws InvalidActionException {
        inventory.useItem(itemUsedId, this);
    }

    public void consume(Entity entity) {
        inventory.removeFromId(entity.getId());
    }

    public void removeItemIfFound(String type, int count) throws InvalidActionException {
        if (inventory.countType(type) < count) {
            throw new InvalidActionException("Not enough " + type + " in inventory to remove");
        }

        inventory.removeFromType(type, count);
    }

    public void addAllyModifiers(BattleModifier modifier) {
        dungeon.getEntitiesOfType(Mercenary.TYPE).stream()
                        .map(e -> (Mercenary) e)
                        .forEach(m -> m.addModifications(modifier));
    }

    @Override
    public void move(Position position) {
        this.oldPosition = this.position;
        this.position = position;
    }

    @Override
    public void visit(CollectableEntity collectable, Direction moveDirection) throws InvalidMovementException {
        collisionStrategy.moveTo(this, dungeon, collectable, moveDirection);
    }

    @Override
    public void visit(Boulder boulder, Direction moveDirection) throws InvalidMovementException {
        collisionStrategy.moveTo(this, dungeon, boulder, moveDirection);
    }

    @Override
    public void visit(Door door, Direction moveDirection) throws InvalidMovementException {
        collisionStrategy.moveTo(this, dungeon, door, moveDirection);
    }

    public void visit(Exit exit, Direction moveDirection) throws InvalidMovementException {
        collisionStrategy.moveTo(this, dungeon, exit, moveDirection);
    }

    public void visit(Activatable activatable, Direction moveDirection) throws InvalidMovementException {
        collisionStrategy.moveTo(this, dungeon, activatable, moveDirection);
    }

    @Override
    public void visit(Portal portal, Direction moveDirection) throws InvalidMovementException {
        collisionStrategy.moveTo(this, dungeon, portal, moveDirection);
    }

    @Override
    public void visit(Wall wall, Direction moveDirection) throws InvalidMovementException {
        collisionStrategy.moveTo(this, dungeon, wall, moveDirection);
    }

    @Override
    public void visit(ZombieToastSpawner spawner, Direction moveDirection) throws InvalidMovementException {
        collisionStrategy.moveTo(this, dungeon, spawner, moveDirection);
    }

    @Override
    public void visit(Spider spider, Direction moveDirection) throws InvalidMovementException {
        collisionStrategy.moveTo(this, dungeon, spider, moveDirection);
    }

    @Override
    public void visit(ZombieToast zombieToast, Direction moveDirection) throws InvalidMovementException {
        collisionStrategy.moveTo(this, dungeon, zombieToast, moveDirection);
    }

    public void visit(Ally ally, Direction moveDirection) throws InvalidMovementException {
        collisionStrategy.moveTo(this, dungeon, ally, moveDirection);
    }

    @Override
    public void visit(SwampTile swampTile, Direction moveDirection) throws InvalidMovementException {
        collisionStrategy.moveTo(this, dungeon, swampTile, moveDirection);
    }

    @Override
    public void publish() {
        subscribers.stream()
                   .forEach(s -> s.update(stateQueue.isEmpty() ? new NormalPlayerState() : stateQueue.peek()));
    }

    @Override
    public void addSubscriber(PlayerStateSubscriber sub) {
        subscribers.add(sub);
    }

    @Override
    public void removeSubscriber(PlayerStateSubscriber sub) {
        subscribers.remove(sub);
    }

    @Override
    public void tick() {
        publish();

        if (!stateQueue.isEmpty()) {
            TemporaryPlayerState currentState = stateQueue.peek();
            currentState.tick();

            if (currentState.isFinished()) {
                stateQueue.remove();
            }
        }
    }

    @Override
    public void die() {
        dungeon.removePlayer();
    }

    public PlayerState getState() {
        return stateQueue.peek();
    }

    @Override
    public double getDeltaHealth(PlayerState state, Stats opponentStats, BattleModifier modifier) {
        return (
            getState() instanceof InvinciblePlayerState ?
                0 :
                (modifier.getDefenceAdditive() - opponentStats.getBaseAttack()) / 10
        );
    }
}
