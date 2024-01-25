package dungeonmania.dungeon.entities.physicalentities.staticentities;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.dungeon.entities.physicalentities.PhysicalEntity;
import dungeonmania.dungeon.player.Visitor;
import dungeonmania.exceptions.InvalidMovementException;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Door extends PhysicalEntity {
    private final int keyNum;
    protected boolean isLocked;
    public static final String TYPE = "door";

    /**
     * Constructor for new entity where you want to generate new Id and
     * increment the number of entities. Use this when creating brand new entity.
     * @param type
     * @param isInteractable
     * @param position
     */
    public Door(Dungeon dungeon, Position position, int keyNum) {
        super(dungeon, TYPE, false, position);
        this.keyNum = keyNum;
        this.isLocked = true;
    }

    /**
     * Copy constructor for Door.
     */
    protected Door(Dungeon newDungeon, Door old) {
        super(newDungeon, old);
        this.keyNum = old.keyNum;
        this.isLocked = old.isLocked;
    }

    public void accept(Visitor visitor, Direction moveDirection) throws InvalidMovementException {
        visitor.visit(this, moveDirection);
    }

    public int getKeyNum() {
        return keyNum;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void unlock() {
        isLocked = false;
    }

    @Override
    public String getType() {
        String extraType = isLocked() ? "" : "_open";
        return super.getType() + extraType;
    }

    @Override
    public PhysicalEntity getCopy(Dungeon newDungeon) {
        return new Door(newDungeon, this);
    }
}
