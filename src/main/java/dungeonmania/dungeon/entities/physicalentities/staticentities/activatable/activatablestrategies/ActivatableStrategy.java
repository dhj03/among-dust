package dungeonmania.dungeon.entities.physicalentities.staticentities.activatable.activatablestrategies;

import java.io.Serializable;
import java.util.Map;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.util.Position;

public interface ActivatableStrategy extends Serializable {
    public boolean isActive(Dungeon dungeon, Position pos, Map<Position, Boolean> visited);
}
