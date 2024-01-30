package amongdust.dungeon.goals.goaltypes;

import org.json.JSONObject;

import amongdust.dungeon.Dungeon;
import amongdust.dungeon.entities.physicalentities.staticentities.ZombieToastSpawner;
import amongdust.dungeon.goals.Goals;

public class EnemyGoal implements Goals {
    public static final String TYPE = "enemies";
    private static int enemyGoal;

    public static void configure(JSONObject config) {
        enemyGoal = config.getInt("enemy_goal");
    }

    @Override
    public String getResponse(Dungeon dungeon) {
        return ":" + TYPE;
    }

    @Override
    public boolean hasCompleted(Dungeon dungeon) {
        if (!dungeon.isPlayerAlive()) {
            return false;
        }

        return (
            dungeon.getKillCount() >= enemyGoal &&
            dungeon.getPositionsOfEntitiesOfType(ZombieToastSpawner.TYPE).isEmpty()
        );
    }
}
