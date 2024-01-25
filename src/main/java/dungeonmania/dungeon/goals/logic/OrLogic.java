package dungeonmania.dungeon.goals.logic;

import org.json.JSONObject;

import dungeonmania.dungeon.Dungeon;

public class OrLogic extends LogicType {
    public static final String TYPE = "OR";

    public OrLogic(JSONObject goals) {
        super(goals);
    }

    @Override
    public boolean hasCompleted(Dungeon dungeon) {
        return getChildrenCompletionList(dungeon).stream()
                                                 .anyMatch(r -> r);
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
