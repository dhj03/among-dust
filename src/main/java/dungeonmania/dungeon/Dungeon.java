package dungeonmania.dungeon;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONObject;

import dungeonmania.dungeon.battle.Battle;
import dungeonmania.dungeon.battle.Fighter;
import dungeonmania.dungeon.entities.EntityFactory;
import dungeonmania.dungeon.entities.Interactable;
import dungeonmania.dungeon.entities.physicalentities.PhysicalEntity;
import dungeonmania.dungeon.entities.physicalentities.Tickable;
import dungeonmania.dungeon.entities.physicalentities.movingentities.Mercenary;
import dungeonmania.dungeon.entities.physicalentities.movingentities.MovingEntity;
import dungeonmania.dungeon.entities.physicalentities.movingentities.Spider;
import dungeonmania.dungeon.entities.physicalentities.staticentities.Exit;
import dungeonmania.dungeon.entities.physicalentities.staticentities.Portal;
import dungeonmania.dungeon.goals.Goals;
import dungeonmania.dungeon.goals.goaltypes.ExitGoal;
import dungeonmania.dungeon.player.Player;
import dungeonmania.dungeon.player.Visitor;
import dungeonmania.dungeon.player.playerstate.observer.PlayerStateSubscriber;
import dungeonmania.dungeon.player.playerstate.states.InvisiblePlayerState;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.exceptions.InvalidMovementException;
import dungeonmania.response.models.AnimationQueue;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;

public class Dungeon implements Serializable {
    private static int dungeonCount = 0;
    private final String dungeonId;
    private final String dungeonName;
    private final String configName;

    private Goals goals;
    private Map<String, PhysicalEntity> entities = new HashMap<>();
    private Player player;
    private List<BattleResponse> battles = new ArrayList<>();
    List<AnimationQueue> animations = new ArrayList<>();

    public Dungeon(String dungeonName, String configName) {
        // Initialise name and Id
        this.dungeonId = getNewId();
        this.configName = configName;
        this.dungeonName = dungeonName;
        this.player = null;

        Config.configure(configName);

        loadDungeon();
    }

    /*
     * Constructor for randomly generated Dungeon.
     */
    public Dungeon(String configName, Position start, Position end) {
        // Initialise name and Id
        this.dungeonId = getNewId();
        this.configName = configName;
        this.dungeonName = "random";

        Config.configure(configName);

        this.player = new Player(this, start);
        this.goals = new ExitGoal();

        addEntity(new Exit(this, end));

        DungeonGenerator dg = new DungeonGenerator(this, start, end);
        dg.randomisedPrims();
    }

    public Dungeon(Dungeon old) {
        this.dungeonId = old.dungeonId;
        this.configName = old.configName;
        this.dungeonName = old.dungeonName;
        this.player = new Player(this, old.player);
        this.goals = old.goals;

        // Copy the Player and the Entities beforehand in case there are changes we need to roll back
        this.entities = new HashMap<>();
        old.entities.values().stream().forEach(e -> {
            addEntity(e.getCopy(this));
        });
    }

    public DungeonResponse getResponse() {
        List<ItemResponse> ir = (player == null) ? new ArrayList<>() : player.getInventoryResponse();
        List<String> buildables = (player == null) ? new ArrayList<>() : player.getAvailableBuildables();
        return new DungeonResponse(
            dungeonId,
            dungeonName,
            getEntityResponses(),
            ir,
            battles,
            buildables,
            goals.hasCompleted(this) ? "" : goals.getResponse(this),
            animations
        );
    }

    public String getConfigName() {
        return configName;
    }

    public void movePlayer(Direction direction) throws InvalidMovementException {
        moveVisitor(player, direction);
    }

    public Position getPlayerPosition() {
        return player.getPosition();
    }

    public Position getPlayerOldPosition() {
        return player.getOldPosition();
    }

    public void moveVisitor(Visitor visitor, Direction direction) throws InvalidMovementException {
        // Get the entities at the position the visitor wants to move to
        Position visitorPosition = visitor.getPosition();
        Position newPosition = visitorPosition.translateBy(direction);
        List<PhysicalEntity> clashingEntities = getEntitiesAtPosition(newPosition);

        if (clashingEntities.isEmpty()) {
            // There isn't anything there...
            // move the visitor
            visitor.move(newPosition);
        } else {
            // There is something here.
            // Let them decide where the visitor moves and if the visitor can move
            for (PhysicalEntity clashingEntity : clashingEntities) {
                clashingEntity.accept(visitor, direction);
            }
        }
    }

    public void tick() {
        // Battle after player movement and also after entity movement.
        player.tick();
        battle();

        // Separate list so that we can modify the original list in each tick
        // Tick moving entities
        List<Tickable> tickables = entities.values().stream()
                                           .filter(e -> e instanceof Tickable && e instanceof MovingEntity)
                                           .map(e -> (Tickable) e)
                                           .collect(Collectors.toList());

        tickables.forEach(e -> e.tick());

        // Tick not moving entities
        tickables = entities.values().stream()
                            .filter(e -> e instanceof Tickable && !(e instanceof MovingEntity))
                            .map(e -> (Tickable) e)
                            .collect(Collectors.toList());

        tickables.forEach(e -> e.tick());

        // Tick spawning for spider
        Spider.tickSpawn(this);
        battle();

    }

    private void battle() {
        if (player == null || player.getState() instanceof InvisiblePlayerState) {
            return;
        }


        List<Fighter> toBattle = getEntitiesAtPosition(player.getPosition()).stream()
                                .filter(e -> e instanceof Fighter && !((e instanceof Mercenary) && ((Mercenary) e).isBribed()))
                                .map(e -> (Fighter) e)
                                .collect(Collectors.toList());


        for (Fighter battling : toBattle) {
            if (player == null) {
                return;
            }

            battles.add(Battle.startBattle(player, battling));
        }
    }

    public int getNumInInventory(String type) {
        return player.getNumInInventory(type);
    }

    public int getKillCount() {
        return player.getKillCount();
    }

    public void removeEntity(PhysicalEntity entity) {
        if (entity instanceof PlayerStateSubscriber) {
            player.removeSubscriber((PlayerStateSubscriber) entity);
        }
        entities.remove(entity.getId());
    }

    public void addEntity(PhysicalEntity entity) {
        entities.put(entity.getId(), entity);

        if (entity instanceof PlayerStateSubscriber && player != null) {
            player.addSubscriber((PlayerStateSubscriber) entity);
        }
    }

    public void removePlayer() {
        player = null;
    }

    public boolean isPlayerAlive() {
        return player != null;
    }

    public void build(String buildable) throws InvalidActionException {
        player.build(buildable);
    }

    public void useItem(String itemUsedId) throws InvalidActionException {
        player.useItem(itemUsedId);
    }

    public List<PhysicalEntity> getEntitiesInRange(Position position, int radius) {
        return entities.values().stream().filter(e ->
            Position.inRange(e.getPosition(), position, radius)
        ).collect(Collectors.toList());
    }

    public List<PhysicalEntity> getEntitiesOfType(String type) {
        return entities.values().stream()
                       .filter(e -> e.getType().equals(type))
                       .collect(Collectors.toList());
    }

    public List<Position> getPositionsOfEntitiesOfType(String type) {
        return getEntitiesOfType(type).stream()
                       .map(e -> e.getPosition())
                       .collect(Collectors.toList());
    }

    public List<PhysicalEntity> getEntitiesAtPosition(Position position) {
        return getEntitiesInRange(position, 0);
    }

    public void interactWith(String id) throws InvalidActionException, IllegalArgumentException {
        PhysicalEntity target = entities.get(id);
        if (target == null) {
            throw new IllegalArgumentException("Entity ID invalid");
        }
        if (!target.isInteractable() && (target instanceof Interactable)) {
            return;
        }

        Interactable interactTarget = (Interactable) target;
        interactTarget.interact(this.player);
    }

    public void addAnimation(String entityId, List<String> actions, boolean loop, int duration) {
        animations.add(new AnimationQueue("PostTick", entityId, actions, loop, duration));
    }

    // Helpers
    private List<EntityResponse> getEntityResponses() {
        List<EntityResponse> entList = entities.values()
                                               .stream()
                                               .map(e -> e.getResponse())
                                               .collect(Collectors.toList());

        if (player != null) {
            entList.add(player.getEntityResponse());
        }
        return entList;
    }

    private void loadDungeon() throws IllegalArgumentException {
        JSONObject dungeonFile;
        try {
            // Get the dungeon config
            String fileContents = FileLoader.loadResourceFile("/dungeons/" + dungeonName + ".json");
            dungeonFile = new JSONObject(fileContents);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Dungeon file doesn't exist");
        }

        this.goals = Goals.makeGoals(dungeonFile.getJSONObject("goal-condition"));

        // Add all the elements
        for (Object o : dungeonFile.getJSONArray("entities")) {
            JSONObject element = (JSONObject) o;

            if (element.getString("type").equals(Player.TYPE)) {
                this.player = new Player(this, element);
            } else {
                PhysicalEntity entity = EntityFactory.createEntityFromJSON(this, element);
                addEntity(entity);
            }
        }

        // Connect each portal to it's partner (by colour)
        Portal.connectPartnerPortals(entities);
    }

    private String getNewId() {
        return Integer.toString(dungeonCount++);
    }
}
