package amongdust.dungeon.goals;

import java.io.Serializable;

import org.json.JSONObject;

import amongdust.dungeon.Dungeon;
import amongdust.dungeon.goals.goaltypes.BoulderGoal;
import amongdust.dungeon.goals.goaltypes.EnemyGoal;
import amongdust.dungeon.goals.goaltypes.ExitGoal;
import amongdust.dungeon.goals.goaltypes.TreasureGoal;
import amongdust.dungeon.goals.logic.AndLogic;
import amongdust.dungeon.goals.logic.OrLogic;

public interface Goals extends Serializable {
    public String getResponse(Dungeon dungeon);
    public boolean hasCompleted(Dungeon dungeon);

    public static Goals makeGoals(JSONObject goals) {
        String goalString = goals.getString("goal");
        switch (goalString) {
            case BoulderGoal.TYPE:
                return new BoulderGoal();
            case EnemyGoal.TYPE:
                return new EnemyGoal();
            case ExitGoal.TYPE:
                return new ExitGoal();
            case TreasureGoal.TYPE:
                return new TreasureGoal();
            case AndLogic.TYPE:
                return new AndLogic(goals);
            case OrLogic.TYPE:
                return new OrLogic(goals);
            default:
                throw new IllegalArgumentException();
        }
    }
}
