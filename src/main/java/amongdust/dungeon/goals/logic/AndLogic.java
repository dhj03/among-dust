package amongdust.dungeon.goals.logic;

import org.json.JSONObject;

import amongdust.dungeon.Dungeon;

public class AndLogic extends LogicType {
    public static final String TYPE = "AND";

    public AndLogic(JSONObject goals) {
        super(goals);
    }

    @Override
    public boolean hasCompleted(Dungeon dungeon) {
        return getChildrenCompletionList(dungeon).stream()
                                                 .allMatch(r -> r);
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
