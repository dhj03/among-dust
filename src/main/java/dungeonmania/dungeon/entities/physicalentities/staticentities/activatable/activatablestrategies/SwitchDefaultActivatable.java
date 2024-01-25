package dungeonmania.dungeon.entities.physicalentities.staticentities.activatable.activatablestrategies;

import java.util.Map;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.dungeon.entities.physicalentities.staticentities.Boulder;
import dungeonmania.util.Position;

public class SwitchDefaultActivatable implements ActivatableStrategy {
    @Override
    public boolean isActive(Dungeon dungeon, Position pos, Map<Position, Boolean> visited) {
        return dungeon.getEntitiesAtPosition(pos)
                      .stream()
                      .anyMatch(e -> e instanceof Boulder);
    }
}
