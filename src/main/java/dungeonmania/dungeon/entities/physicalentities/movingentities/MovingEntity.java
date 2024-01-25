package dungeonmania.dungeon.entities.physicalentities.movingentities;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.dungeon.battle.BattleModifier;
import dungeonmania.dungeon.battle.Fighter;
import dungeonmania.dungeon.battle.Stats;
import dungeonmania.dungeon.entities.physicalentities.PhysicalEntity;
import dungeonmania.dungeon.entities.physicalentities.Tickable;
import dungeonmania.dungeon.entities.physicalentities.collectableentities.CollectableEntity;
import dungeonmania.dungeon.entities.physicalentities.collisionstrategies.CollisionStrategy;
import dungeonmania.dungeon.entities.physicalentities.movingentities.movementstrategies.MovementStrategy;
import dungeonmania.dungeon.entities.physicalentities.staticentities.Boulder;
import dungeonmania.dungeon.entities.physicalentities.staticentities.Door;
import dungeonmania.dungeon.entities.physicalentities.staticentities.Exit;
import dungeonmania.dungeon.entities.physicalentities.staticentities.Portal;
import dungeonmania.dungeon.entities.physicalentities.staticentities.SwampTile;
import dungeonmania.dungeon.entities.physicalentities.staticentities.Wall;
import dungeonmania.dungeon.entities.physicalentities.staticentities.ZombieToastSpawner;
import dungeonmania.dungeon.entities.physicalentities.staticentities.activatable.Activatable;
import dungeonmania.dungeon.player.Visitor;
import dungeonmania.dungeon.player.playerstate.states.InvinciblePlayerState;
import dungeonmania.dungeon.player.playerstate.states.PlayerState;
import dungeonmania.exceptions.InvalidMovementException;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public abstract class MovingEntity extends PhysicalEntity implements Visitor, Tickable, Fighter {
    protected MovementStrategy movementStrategy;
    private CollisionStrategy collisionStrategy;
    private double stuckCounter = 0;

    private Stats stats;

    public MovingEntity(Dungeon dungeon, String type, boolean isInteractable, Position position, MovementStrategy movementStrategy, CollisionStrategy collisionStrategy, Stats stats) {
        super(dungeon, type, isInteractable, position);
        this.movementStrategy = movementStrategy;
        this.collisionStrategy = collisionStrategy;
        this.stats = stats;
    }

    /**
     * Copy constructor for MovingEntity.
     */
    public MovingEntity(Dungeon newDungeon, MovingEntity old) {
        super(newDungeon, old);
        this.movementStrategy = old.movementStrategy;
        this.collisionStrategy = old.collisionStrategy;
        this.stuckCounter = old.stuckCounter;
        this.stats = old.stats;
    }

    @Override
    public void tick() {
        if (stuckCounter <= 0) {
            movementStrategy.move(dungeon, this);
        } else {
            stuckCounter--;
        }
    }

    public void setMovementStrategy(MovementStrategy movementStrategy) {
        this.movementStrategy = movementStrategy;
    }

    public MovementStrategy getMovementStrategy() {
        return movementStrategy;
    }

    public void setCollisionStrategy(CollisionStrategy collisionStrategy) {
        this.collisionStrategy = collisionStrategy;
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
        stuckCounter = swampTile.getMovementFactor();
    }

    @Override
    public Stats getStats() {
        return stats;
    }

    @Override
    public void die() {
        dungeon.removeEntity(this);
    }

    @Override
    public double getDeltaHealth(PlayerState state, Stats opponentStats, BattleModifier modifier) {
        return (
            state instanceof InvinciblePlayerState ?
                -Double.MAX_VALUE :
                -modifier.getAttackMultiplier() * (opponentStats.getBaseAttack() + modifier.getAttackAdditive()) / 5
        );
    }
}
