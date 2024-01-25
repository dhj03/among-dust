package dungeonmania.dungeon.entities.physicalentities.staticentities.activatable.activatablestrategies;

import java.util.Map;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.util.Position;

public class OrActivatable implements ActivatableStrategy {
    public static final String TYPE = "or";

    @Override
    public boolean isActive(Dungeon dungeon, Position pos, Map<Position, Boolean> visited) {
        // Condition: At least one cardinally adjacent active entity
        return pos.getCardinallyAdjacentPositions().stream()
                  .anyMatch(p -> visited.getOrDefault(p, false));
    }
}
