package amongdust.dungeon.entities.physicalentities.staticentities.activatable.activatablestrategies;

import java.util.Map;

import amongdust.dungeon.Dungeon;
import amongdust.util.Position;

public class LogicSwitchActivatable implements ActivatableStrategy {
    private ActivatableStrategy logicActivatableStrategy;
    private ActivatableStrategy defaultActivatableStrategy = new SwitchDefaultActivatable();
    public LogicSwitchActivatable(ActivatableStrategy logicActivatableStrategy) {
        this.logicActivatableStrategy = logicActivatableStrategy;
    }

    @Override
    public boolean isActive(Dungeon dungeon, Position pos, Map<Position, Boolean> visited) {
        // Condition: Default switch activation strategy + logic strategy
        return logicActivatableStrategy.isActive(dungeon, pos, visited) || 
               defaultActivatableStrategy.isActive(dungeon, pos, visited);
    }
}