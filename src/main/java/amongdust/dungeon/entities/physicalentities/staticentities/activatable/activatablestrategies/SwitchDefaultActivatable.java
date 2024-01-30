package amongdust.dungeon.entities.physicalentities.staticentities.activatable.activatablestrategies;

import java.util.Map;

import amongdust.dungeon.Dungeon;
import amongdust.dungeon.entities.physicalentities.staticentities.Boulder;
import amongdust.util.Position;

public class SwitchDefaultActivatable implements ActivatableStrategy {
    @Override
    public boolean isActive(Dungeon dungeon, Position pos, Map<Position, Boolean> visited) {
        return dungeon.getEntitiesAtPosition(pos)
                      .stream()
                      .anyMatch(e -> e instanceof Boulder);
    }
}
