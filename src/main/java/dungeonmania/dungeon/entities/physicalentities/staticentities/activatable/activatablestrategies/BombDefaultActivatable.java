package dungeonmania.dungeon.entities.physicalentities.staticentities.activatable.activatablestrategies;

import java.util.Map;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.dungeon.entities.physicalentities.staticentities.activatable.FloorSwitch;
import dungeonmania.util.Position;

public class BombDefaultActivatable implements ActivatableStrategy {
    @Override
    public boolean isActive(Dungeon dungeon, Position pos, Map<Position, Boolean> visited) {
        return pos.getCardinallyAdjacentPositions().stream().anyMatch(p ->
            dungeon.getEntitiesAtPosition(p).stream()
                   .filter(e -> e instanceof FloorSwitch)
                   .map(e -> (FloorSwitch) e)
                   .anyMatch(s -> s.isActive() && s.getActivatableStrategy() instanceof SwitchDefaultActivatable)
        );
    }
}
