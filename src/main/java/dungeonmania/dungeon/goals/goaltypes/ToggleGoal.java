package dungeonmania.dungeon.goals.goaltypes;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.dungeon.goals.Goals;

public abstract class ToggleGoal implements Goals {
    private boolean completed = false;

    protected abstract boolean currentlyCompleted(Dungeon dungeon);

    @Override
    public boolean hasCompleted(Dungeon dungeon) {
        completed = completed || currentlyCompleted(dungeon);
        return completed;
    }
}
