package amongdust.dungeon.goals.goaltypes;

import amongdust.dungeon.Dungeon;
import amongdust.dungeon.entities.physicalentities.staticentities.Exit;
import amongdust.dungeon.goals.Goals;
import amongdust.util.Position;

public class ExitGoal implements Goals {
    public static final String TYPE = "exit";

    @Override
    public String getResponse(Dungeon dungeon) {
        return ":" + TYPE;
    }

    @Override
    public boolean hasCompleted(Dungeon dungeon) {
        if (!dungeon.isPlayerAlive()) {
            return false;
        }

        Position playerPosition = dungeon.getPlayerPosition();

        return dungeon.getEntitiesAtPosition(playerPosition).stream()
                                                            .anyMatch(e -> e instanceof Exit);
    }
}
