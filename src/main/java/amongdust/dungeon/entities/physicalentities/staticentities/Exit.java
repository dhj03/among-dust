package amongdust.dungeon.entities.physicalentities.staticentities;

import amongdust.dungeon.Dungeon;
import amongdust.dungeon.entities.physicalentities.PhysicalEntity;
import amongdust.dungeon.player.Visitor;
import amongdust.exceptions.InvalidMovementException;
import amongdust.util.Direction;
import amongdust.util.Position;

public class Exit extends PhysicalEntity {
    public static final String TYPE = "exit";

    /**
     * Constructor for new entity where you want to generate new Id and
     * increment the number of entities. Use this when creating brand new entity.
     * @param type
     * @param isInteractable
     * @param position
     */
    public Exit(Dungeon dungeon, Position position) {
        super(dungeon, TYPE, false, position);
    }

    /**
     * Copy constructor for Exit.
     */
    private Exit(Dungeon newDungeon, Exit old) {
        super(newDungeon, old);
    }

    public void accept(Visitor visitor, Direction moveDirection) throws InvalidMovementException {
        visitor.visit(this, moveDirection);
    }

    @Override
    public PhysicalEntity getCopy(Dungeon newDungeon) {
        return new Exit(newDungeon, this);
    }
}
