package amongdust.dungeon.entities.physicalentities.staticentities.activatable.activatablestrategies;

import java.util.Map;

import amongdust.dungeon.Dungeon;
import amongdust.util.Position;

public class XorActivatable implements ActivatableStrategy {
    public static final String TYPE = "xor";

    @Override
    public boolean isActive(Dungeon dungeon, Position pos, Map<Position, Boolean> visited) {
        // Condition: only one cardinally adjacent active entity
        return pos.getCardinallyAdjacentPositions().stream()
                  .filter(p -> visited.getOrDefault(p, false))
                  .count() == 1;
    }
}
