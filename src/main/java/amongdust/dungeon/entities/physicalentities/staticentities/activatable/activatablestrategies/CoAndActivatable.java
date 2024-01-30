package amongdust.dungeon.entities.physicalentities.staticentities.activatable.activatablestrategies;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import amongdust.dungeon.Dungeon;
import amongdust.dungeon.entities.physicalentities.staticentities.activatable.Activatable;
import amongdust.util.Position;

public class CoAndActivatable implements ActivatableStrategy {
    public static final String TYPE = "co_and";

    @Override
    public boolean isActive(Dungeon dungeon, Position pos, Map<Position, Boolean> visited) {
        // Condition: At least two cardinally adjacent entities that
        // were activated on the same tick
        List<Integer> activationTimes = pos.getCardinallyAdjacentPositions().stream().map(p -> {
            Activatable a = dungeon.getEntitiesAtPosition(p).stream()
                                   .filter(e -> e instanceof Activatable)
                                   .map(e -> (Activatable) e)
                                   .findFirst().orElse(null);
            if (a == null) {
                return null;
            }
            return a.getTimeSinceActivation();
        }).filter(t -> t != null && t != -1).collect(Collectors.toList());

        // Check for duplicates
        return activationTimes.size() != new HashSet<>(activationTimes).size();
    }
}
