package amongdust.dungeon.entities.physicalentities.staticentities.activatable.activatablestrategies;

import java.util.Map;

import amongdust.dungeon.Dungeon;
import amongdust.dungeon.entities.physicalentities.staticentities.activatable.FloorSwitch;
import amongdust.util.Position;

public class AndActivatable implements ActivatableStrategy {
    public static final String TYPE = "and";

    @Override
    public boolean isActive(Dungeon dungeon, Position pos, Map<Position, Boolean> visited) {
        // First condition: Every cardinally adjacent switch must be active
        // Second condition: At least 2 cardinally adjacent active entities
        return (
            pos.getCardinallyAdjacentPositions().stream()
               .filter(p -> visited.getOrDefault(p, false))
               .count() >= 2
            &&
            pos.getCardinallyAdjacentPositions().stream().filter(p ->
                dungeon.getEntitiesAtPosition(p).stream().anyMatch(e -> e instanceof FloorSwitch)
            ).allMatch(p -> visited.getOrDefault(p, false))
        );
    }
}
