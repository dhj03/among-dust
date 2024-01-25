package dungeonmania.dungeon.entities.buildableentities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.dungeon.battle.BattleModifier;
import dungeonmania.dungeon.entities.Entity;
import dungeonmania.dungeon.entities.physicalentities.collectableentities.Arrow;
import dungeonmania.dungeon.entities.physicalentities.collectableentities.Wood;
import dungeonmania.dungeon.player.Equipment;

public class Bow extends Entity implements Buildable, Equipment {

    private int durability;
    private static int maxDurability;
    public static final String TYPE = "bow";
    private static final List<Map<String, ItemQuantifier>> RECIPES = new ArrayList<>(
        Arrays.asList(
            Map.of(
                Wood.TYPE, new ItemQuantifier(1, true),
                Arrow.TYPE, new ItemQuantifier(3, true)
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
    public Bow(Dungeon dungeon) {
        super(dungeon, TYPE);
        this.durability = maxDurability;
    }

    public static void configure(JSONObject config) {
        maxDurability = config.getInt("bow_durability");
    }

    @Override
    public List<Map<String, ItemQuantifier>> getRecipes() {
        return RECIPES;
    }

    @Override
    public void addModifications(BattleModifier modifier) {
        modifier.multiplyAttackMultiplier(2);
        durability--;
    }

    @Override
    public boolean isBroken() {
        return durability <= 0;
    }
}
