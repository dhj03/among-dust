package amongdust.dungeon.goals.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

import amongdust.dungeon.Dungeon;
import amongdust.dungeon.goals.Goals;

public abstract class LogicType implements Goals {
    private List<Goals> children = new ArrayList<>();

    public LogicType(JSONObject goals) {
        // Get the children and add them recursively
        JSONArray children = goals.getJSONArray("subgoals");
        for (Object childAsObject : children) {
            JSONObject childAsJSONObject = (JSONObject) childAsObject;
            addChild(Goals.makeGoals(childAsJSONObject));
        }
    }

    @Override
    public String getResponse(Dungeon dungeon) {
        if (hasCompleted(dungeon)) {
            return "";
        }

        List<String> filteredResponses = children.stream()
                                                 .filter(g -> !g.hasCompleted(dungeon))
                                                 .map(g -> g.getResponse(dungeon))
                                                 .collect(Collectors.toList());
        return String.join(" " + getType() + " ", filteredResponses);
    }

    public void addChild(Goals child) {
        children.add(child);
    }

    public void removeChild(Goals child) {
        children.remove(child);
    }

    public List<Boolean> getChildrenCompletionList(Dungeon dungeon) {
        return children.stream()
                       .map(c -> c.hasCompleted(dungeon))
                       .collect(Collectors.toList());
    }

    public abstract String getType();
}
