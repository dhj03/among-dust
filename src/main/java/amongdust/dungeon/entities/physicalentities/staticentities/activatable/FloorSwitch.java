package amongdust.dungeon.entities.physicalentities.staticentities.activatable;

import amongdust.dungeon.Dungeon;
import amongdust.dungeon.entities.physicalentities.PhysicalEntity;
import amongdust.dungeon.entities.physicalentities.staticentities.activatable.activatablestrategies.ActivatableStrategy;
import amongdust.dungeon.player.Visitor;
import amongdust.exceptions.InvalidMovementException;
import amongdust.util.Direction;
import amongdust.util.Position;

public class FloorSwitch extends Activatable {
    public static final String TYPE = "switch";
    /**
     * Constructor for new logic entity where you want to generate new Id and
     * increment the number of entities. Use this when creating brand new entity.
     * @param dungeon
     * @param position
     * @param logicType
     */
    public FloorSwitch(Dungeon dungeon, Position position, ActivatableStrategy activatableStrategy) {
        super(dungeon, TYPE, false, position, activatableStrategy);
    }

    /**
     * Copy constructor for FloorSwitch.
     */
    private FloorSwitch(Dungeon newDungeon, FloorSwitch old) {
        super(newDungeon, old);
        this.activatableStrategy = old.getActivatableStrategy();
    }

    public void accept(Visitor visitor, Direction moveDirection) throws InvalidMovementException {
        visitor.visit(this, moveDirection);
    }

    @Override
    public PhysicalEntity getCopy(Dungeon newDungeon) {
        return new FloorSwitch(newDungeon, this);
    }
}
