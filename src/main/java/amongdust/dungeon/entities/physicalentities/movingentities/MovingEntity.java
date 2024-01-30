package amongdust.dungeon.entities.physicalentities.movingentities;

import amongdust.dungeon.Dungeon;
import amongdust.dungeon.battle.BattleModifier;
import amongdust.dungeon.battle.Fighter;
import amongdust.dungeon.battle.Stats;
import amongdust.dungeon.entities.physicalentities.PhysicalEntity;
import amongdust.dungeon.entities.physicalentities.Tickable;
import amongdust.dungeon.entities.physicalentities.collectableentities.CollectableEntity;
import amongdust.dungeon.entities.physicalentities.collisionstrategies.CollisionStrategy;
import amongdust.dungeon.entities.physicalentities.movingentities.movementstrategies.MovementStrategy;
import amongdust.dungeon.entities.physicalentities.staticentities.Boulder;
import amongdust.dungeon.entities.physicalentities.staticentities.Door;
import amongdust.dungeon.entities.physicalentities.staticentities.Exit;
import amongdust.dungeon.entities.physicalentities.staticentities.Portal;
import amongdust.dungeon.entities.physicalentities.staticentities.SwampTile;
import amongdust.dungeon.entities.physicalentities.staticentities.Wall;
import amongdust.dungeon.entities.physicalentities.staticentities.ZombieToastSpawner;
import amongdust.dungeon.entities.physicalentities.staticentities.activatable.Activatable;
import amongdust.dungeon.player.Visitor;
import amongdust.dungeon.player.playerstate.states.InvinciblePlayerState;
import amongdust.dungeon.player.playerstate.states.PlayerState;
import amongdust.exceptions.InvalidMovementException;
import amongdust.util.Direction;
import amongdust.util.Position;

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
