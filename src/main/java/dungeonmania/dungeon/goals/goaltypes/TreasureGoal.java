package dungeonmania.dungeon.goals.goaltypes;

import org.json.JSONObject;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.dungeon.entities.physicalentities.collectableentities.SunStone;
import dungeonmania.dungeon.entities.physicalentities.collectableentities.Treasure;

public class TreasureGoal extends ToggleGoal {
    public static final String TYPE = "treasure";
    private static int treasureGoal;

    public static void configure(JSONObject config) {
        treasureGoal = config.getInt("enemy_goal");
    }

    @Override
    public String getResponse(Dungeon dungeon) {
        return ":" + TYPE;
    }

    @Override
    public boolean currentlyCompleted(Dungeon dungeon) {
        if (!dungeon.isPlayerAlive()) {
            return false;
        }

        int numTreasure = dungeon.getNumInInventory(Treasure.TYPE);
        int numSunStone = dungeon.getNumInInventory(SunStone.TYPE);
        return numTreasure + numSunStone >= treasureGoal;
    }
}
