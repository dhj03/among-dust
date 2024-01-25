package dungeonmania.dungeon.entities.physicalentities.staticentities.activatable;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.dungeon.entities.physicalentities.PhysicalEntity;
import dungeonmania.dungeon.entities.physicalentities.staticentities.activatable.activatablestrategies.ActivatableStrategy;
import dungeonmania.dungeon.player.Visitor;
import dungeonmania.exceptions.InvalidMovementException;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

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
