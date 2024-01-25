package dungeonmania.dungeon.entities.physicalentities.staticentities.activatable.activatablestrategies;

import java.util.Map;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.util.Position;

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
