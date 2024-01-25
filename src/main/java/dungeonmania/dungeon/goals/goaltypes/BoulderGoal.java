package dungeonmania.dungeon.goals.goaltypes;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.dungeon.entities.physicalentities.staticentities.Boulder;
import dungeonmania.dungeon.entities.physicalentities.staticentities.activatable.FloorSwitch;
import dungeonmania.dungeon.goals.Goals;
import dungeonmania.util.Position;

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
