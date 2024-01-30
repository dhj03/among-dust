package amongdust.dungeon.entities.physicalentities;

import amongdust.dungeon.Dungeon;
import amongdust.dungeon.entities.Entity;
import amongdust.dungeon.player.Visitor;
import amongdust.exceptions.InvalidMovementException;
import amongdust.response.models.EntityResponse;
import amongdust.util.Direction;
import amongdust.util.Position;

public abstract class PhysicalEntity extends Entity implements Physical {
    protected boolean isInteractable;
    private Position position;

    /**
     * Constructor for new entity where you want to generate new Id and
     * increment the number of entities. Use this when creating brand new entity.
     * @param type
     * @param isInteractable
     * @param position
     */
    public PhysicalEntity(Dungeon dungeon, String type, boolean isInteractable, Position position) {
        super(dungeon, type);
        this.isInteractable = isInteractable;
        this.position = position;
    }

    /**
     * Copy constructor for PhysicalEntity
     */
    public PhysicalEntity(Dungeon newDungeon, PhysicalEntity old) {
        super(newDungeon, old);
        this.isInteractable = old.isInteractable;
        this.position = old.position;
    }

    public Position getPosition() {
        return position;
    }

    public void move(Position position) {
        this.position = position;
    }

    public boolean isInteractable() {
        return isInteractable;
    }

    public EntityResponse getResponse() {
        return new EntityResponse(
            getId(),
            getType(),
            getPosition(),
            isInteractable()
        );
    }

    // Abstract methods
    public abstract void accept(Visitor visitor, Direction moveDirection) throws InvalidMovementException;

    public abstract PhysicalEntity getCopy(Dungeon newDungeon);
}
