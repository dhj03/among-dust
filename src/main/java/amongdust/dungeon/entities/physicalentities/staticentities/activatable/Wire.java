package amongdust.dungeon.entities.physicalentities.staticentities.activatable;

import amongdust.dungeon.Dungeon;
import amongdust.dungeon.entities.physicalentities.PhysicalEntity;
import amongdust.dungeon.entities.physicalentities.staticentities.activatable.activatablestrategies.ActivatableStrategy;
import amongdust.dungeon.player.Visitor;
import amongdust.exceptions.InvalidMovementException;
import amongdust.util.Direction;
import amongdust.util.Position;

public class Wire extends Activatable {
    public static final String TYPE = "wire";

    /**
     * Constructor for new logic entity where you want to generate new Id and
     * increment the number of entities. Use this when creating brand new entity.
     * @param dungeon
     * @param position
     * @param logicType
     */
    public Wire(Dungeon dungeon, Position position, ActivatableStrategy activatableStrategy) {
        super(dungeon, TYPE, false, position, activatableStrategy);
    }

    /**
     * Copy constructor for Wire.
     */
    private Wire(Dungeon newDungeon, Wire old) {
        super(newDungeon, old);
    }

    public void accept(Visitor visitor, Direction moveDirection) throws InvalidMovementException {
        visitor.visit(this, moveDirection);
    }

    @Override
    public PhysicalEntity getCopy(Dungeon newDungeon) {
        return new Wire(newDungeon, this);
    }
}
