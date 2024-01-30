package amongdust.dungeon.goals.goaltypes;

import amongdust.dungeon.Dungeon;
import amongdust.dungeon.entities.physicalentities.staticentities.Boulder;
import amongdust.dungeon.entities.physicalentities.staticentities.activatable.FloorSwitch;
import amongdust.dungeon.goals.Goals;
import amongdust.util.Position;

public class BoulderGoal implements Goals {
    public static final String TYPE = "boulders";

    @Override
    public String getResponse(Dungeon dungeon) {
        return ":" + TYPE;
    }

    @Override
    public boolean hasCompleted(Dungeon dungeon) {
        if (!dungeon.isPlayerAlive()) {
            return false;
        }

        return dungeon.getPositionsOfEntitiesOfType(FloorSwitch.TYPE).stream()
                      .allMatch(p -> isBoulderAtPosition(dungeon, p));
    }

    private boolean isBoulderAtPosition(Dungeon dungeon, Position position) {
        return dungeon.getEntitiesAtPosition(position).stream()
                      .anyMatch(e -> e instanceof Boulder);
    }
}
