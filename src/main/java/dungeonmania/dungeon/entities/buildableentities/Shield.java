package dungeonmania.dungeon.entities.buildableentities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.dungeon.battle.BattleModifier;
import dungeonmania.dungeon.entities.Entity;
import dungeonmania.dungeon.entities.physicalentities.collectableentities.Key;
import dungeonmania.dungeon.entities.physicalentities.collectableentities.SunStone;
import dungeonmania.dungeon.entities.physicalentities.collectableentities.Treasure;
import dungeonmania.dungeon.entities.physicalentities.collectableentities.Wood;
import dungeonmania.dungeon.player.Equipment;

public class Shield extends Entity implements Buildable, Equipment {

    private int durability;
    private static int defence;
    private static int maxDurability;
    public static final String TYPE = "shield";
    private static final List<Map<String, ItemQuantifier>> RECIPES = new ArrayList<>(
        Arrays.asList(
            Map.of(
                Wood.TYPE, new ItemQuantifier(2, true),
                Treasure.TYPE, new ItemQuantifier(1, true)
            ),
            Map.of(
                Wood.TYPE, new ItemQuantifier(2, true),
                Key.TYPE, new ItemQuantifier(1, true)
            ),
            Map.of(
                Wood.TYPE, new ItemQuantifier(2, true),
                SunStone.TYPE, new ItemQuantifier(1, false)
            )
        )
    );

    /**
     * Constructor for new entity where you want to generate new Id and
     * increment the number of entities. Use this when creating brand new entity.
     * @param type
     * @param isInteractable
     * @param position
     */
    public Shield(Dungeon dungeon) {
        super(dungeon, TYPE);
        this.durability = maxDurability;
    }

    public static void configure(JSONObject config) {
        defence = config.getInt("shield_defence");
        maxDurability = config.getInt("shield_durability");
    }

    @Override
    public List<Map<String, ItemQuantifier>> getRecipes() {
        return RECIPES;
    }

    @Override
    public void addModifications(BattleModifier modifier) {
        modifier.addDefenceAdditive(defence);
        durability--;
    }

    @Override
    public boolean isBroken() {
        return durability <= 0;
    }
}
