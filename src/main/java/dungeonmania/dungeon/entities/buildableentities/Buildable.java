package dungeonmania.dungeon.entities.buildableentities;

import java.util.List;
import java.util.Map;

import dungeonmania.exceptions.InvalidActionException;

public interface Buildable {
    public List<Map<String, ItemQuantifier>> getRecipes() throws InvalidActionException;
}
