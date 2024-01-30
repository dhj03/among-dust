package amongdust.dungeon.entities.physicalentities.staticentities;

import amongdust.dungeon.Dungeon;
import amongdust.dungeon.entities.physicalentities.PhysicalEntity;
import amongdust.dungeon.entities.physicalentities.collectableentities.CollectableEntity;
import amongdust.dungeon.entities.physicalentities.collisionstrategies.BoulderCollisionStrategy;
import amongdust.dungeon.entities.physicalentities.collisionstrategies.CollisionStrategy;
import amongdust.dungeon.entities.physicalentities.movingentities.Ally;
import amongdust.dungeon.entities.physicalentities.movingentities.Spider;
import amongdust.dungeon.entities.physicalentities.movingentities.ZombieToast;
import amongdust.dungeon.entities.physicalentities.staticentities.activatable.Activatable;
import amongdust.dungeon.player.Visitor;
import amongdust.exceptions.InvalidMovementException;
import amongdust.util.Direction;
import amongdust.util.Position;

public class Boulder extends PhysicalEntity implements Visitor {
    public static final String TYPE = "boulder";
    private CollisionStrategy collisionStrategy = new BoulderCollisionStrategy();

    /**
     * Constructor for new entity where you want to generate new Id and
     * increment the number of entities. Use this when creating brand new entity.
     * @param type
     * @param isInteractable
     * @param position
     */
    public Boulder(Dungeon dungeon, Position position) {
        super(dungeon, TYPE, false, position);
    }

    /**
     * Copy constructor for Boulder.
     */
    private Boulder(Dungeon newDungeon, Boulder old) {
        super(newDungeon, old);
    }

    public void accept(Visitor visitor, Direction moveDirection) throws InvalidMovementException {
        visitor.visit(this, moveDirection);
    }

    @Override
    public PhysicalEntity getCopy(Dungeon newDungeon) {
        return new Boulder(newDungeon, this);
    }

    @Override
    public void visit(CollectableEntity collectable, Direction moveDirection) throws InvalidMovementException {
        // Boulder and collectable can co-exist, so just move the boulder
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

    @Override
    public void visit(Exit exit, Direction moveDirection) throws InvalidMovementException {
        collisionStrategy.moveTo(this, dungeon, exit, moveDirection);
    }

    @Override
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

    @Override
    public void visit(Ally ally, Direction moveDirection) throws InvalidMovementException {
        collisionStrategy.moveTo(this, dungeon, ally, moveDirection);
    }
    @Override
    public void visit(SwampTile swampTile, Direction moveDirection) throws InvalidMovementException {
        collisionStrategy.moveTo(this, dungeon, swampTile, moveDirection);
    }
}
