package dungeonmania.dungeon.entities.physicalentities.collectableentities;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.dungeon.entities.physicalentities.PhysicalEntity;
import dungeonmania.dungeon.player.Visitor;
import dungeonmania.exceptions.InvalidMovementException;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public abstract class CollectableEntity extends PhysicalEntity {
    /**
     * Constructor for new entity where you want to generate new Id and
     * increment the number of entities. Use this when creating brand new entity.
     * @param type
     * @param isInteractable
     * @param position
     */
    public CollectableEntity(Dungeon dungeon, String type, Position position) {
        super(dungeon, type, false, position);
    }

    /**
     * Copy constructor for CollectableEntity.
     */
    public CollectableEntity(Dungeon newDungeon, CollectableEntity old) {
        super(newDungeon, old);
    }

    public void accept(Visitor visitor, Direction moveDirection) throws InvalidMovementException {
        visitor.visit(this, moveDirection);
    }
}
