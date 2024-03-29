package amongdust.dungeon.entities.physicalentities.staticentities;

import java.util.HashMap;
import java.util.Map;

import amongdust.dungeon.Dungeon;
import amongdust.dungeon.entities.physicalentities.PhysicalEntity;
import amongdust.dungeon.entities.physicalentities.staticentities.activatable.Activatable;
import amongdust.dungeon.entities.physicalentities.staticentities.activatable.activatablestrategies.ActivatableStrategy;
import amongdust.dungeon.player.Visitor;
import amongdust.exceptions.InvalidMovementException;
import amongdust.util.Direction;
import amongdust.util.Position;

public class SwitchDoor extends Door {
    public static final String TYPE = "switch_door";
    private ActivatableStrategy activatableStrategy;

    public SwitchDoor(Dungeon dungeon, Position position, int keyNum, ActivatableStrategy activatableStrategy) {
        super(dungeon, position, keyNum);
        this.activatableStrategy = activatableStrategy;
    }

    /**
     * Copy constructor for SwitchDoor.
     */
    private SwitchDoor(Dungeon newDungeon, SwitchDoor old) {
        super(newDungeon, old);
        this.activatableStrategy = old.activatableStrategy;
    }

    @Override
    public PhysicalEntity getCopy(Dungeon newDungeon) {
        return new SwitchDoor(newDungeon, this);
    }

    @Override
    public void accept(Visitor visitor, Direction moveDirection) throws InvalidMovementException {
        visitor.visit(this, moveDirection);
    }

    @Override
    public boolean isLocked() {
        Map<Position, Boolean> adjacent = new HashMap<>();
        getPosition().getCardinallyAdjacentPositions().forEach(p -> {
            dungeon.getEntitiesAtPosition(p).stream()
                   .filter(e -> e instanceof Activatable)
                   .map(e -> (Activatable) e)
                   .forEach(a -> adjacent.put(a.getPosition(), a.isActive()));
        });
        return isLocked && !activatableStrategy.isActive(dungeon, getPosition(), adjacent);
    }

    @Override
    public String getType() {
        String extraType = isLocked() ? "" : "_open";
        return TYPE + extraType;
    }
}
