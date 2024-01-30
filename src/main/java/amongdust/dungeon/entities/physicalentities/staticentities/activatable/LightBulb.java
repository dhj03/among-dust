package amongdust.dungeon.entities.physicalentities.staticentities.activatable;

import amongdust.dungeon.Dungeon;
import amongdust.dungeon.entities.physicalentities.PhysicalEntity;
import amongdust.dungeon.entities.physicalentities.staticentities.activatable.activatablestrategies.ActivatableStrategy;
import amongdust.dungeon.player.Visitor;
import amongdust.exceptions.InvalidMovementException;
import amongdust.util.Direction;
import amongdust.util.Position;

public class LightBulb extends Activatable {
    private static final String BASE_TYPE = "light_bulb";
    private static final String OFF = "_off";
    private static final String ON = "_on";
    public static final String TYPE = BASE_TYPE + OFF;

    /**
     * Constructor for new logic entity where you want to generate new Id and
     * increment the number of entities. Use this when creating brand new entity.
     * @param dungeon
     * @param position
     * @param logicType
     */
    public LightBulb(Dungeon dungeon, Position position, ActivatableStrategy activatableStrategy) {
        super(dungeon, TYPE, false, position, activatableStrategy);
    }

    private LightBulb(Dungeon newDungeon, LightBulb old) {
        super(newDungeon, old);
        this.activatableStrategy = old.getActivatableStrategy();
    }

    @Override
    public void accept(Visitor visitor, Direction moveDirection) throws InvalidMovementException {
        visitor.visit(this, moveDirection);
    }

    @Override
    public PhysicalEntity getCopy(Dungeon newDungeon) {
        return new LightBulb(newDungeon, this);
    }

    @Override
    public void tick() {
        if (isActive()) {
            dungeon.removeEntity(this);
            dungeon.addEntity(new LightBulb(dungeon, getPosition(), activatableStrategy));
        }
    }

    @Override
    public String getType() {
        String extraType = isActive() ? ON : OFF;
        return BASE_TYPE + extraType;
    }
}
