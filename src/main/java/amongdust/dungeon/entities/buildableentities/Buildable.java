package amongdust.dungeon.entities.buildableentities;

import java.util.List;
import java.util.Map;

import amongdust.exceptions.InvalidActionException;

public interface Buildable {
    public List<Map<String, ItemQuantifier>> getRecipes() throws InvalidActionException;
}
