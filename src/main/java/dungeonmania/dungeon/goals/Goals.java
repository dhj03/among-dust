package dungeonmania.dungeon.goals;

import java.io.Serializable;

import org.json.JSONObject;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.dungeon.goals.goaltypes.BoulderGoal;
import dungeonmania.dungeon.goals.goaltypes.EnemyGoal;
import dungeonmania.dungeon.goals.goaltypes.ExitGoal;
import dungeonmania.dungeon.goals.goaltypes.TreasureGoal;
import dungeonmania.dungeon.goals.logic.AndLogic;
import dungeonmania.dungeon.goals.logic.OrLogic;

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
