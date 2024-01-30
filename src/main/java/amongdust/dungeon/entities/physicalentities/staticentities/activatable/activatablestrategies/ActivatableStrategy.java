package amongdust.dungeon.entities.physicalentities.staticentities.activatable.activatablestrategies;

import java.io.Serializable;
import java.util.Map;

import amongdust.dungeon.Dungeon;
import amongdust.util.Position;

public interface ActivatableStrategy extends Serializable {
    public boolean isActive(Dungeon dungeon, Position pos, Map<Position, Boolean> visited);
}
