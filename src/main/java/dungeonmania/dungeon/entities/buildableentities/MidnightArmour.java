package dungeonmania.dungeon.entities.buildableentities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import dungeonmania.dungeon.Config;
import dungeonmania.dungeon.Dungeon;
import dungeonmania.dungeon.battle.BattleModifier;
import dungeonmania.dungeon.entities.Entity;
import dungeonmania.dungeon.entities.physicalentities.collectableentities.Sword;
import dungeonmania.dungeon.entities.physicalentities.movingentities.ZombieToast;
import dungeonmania.dungeon.entities.physicalentities.collectableentities.SunStone;
import dungeonmania.dungeon.player.Equipment;
import dungeonmania.exceptions.InvalidActionException;

public class MidnightArmour extends Entity implements Buildable, Equipment {

    private static int attack;
    private static int defence;
    public static final String TYPE = "midnight_armour";
    private static final List<Map<String, ItemQuantifier>> RECIPES = new ArrayList<Map<String, ItemQuantifier>>(
        Arrays.asList(
            Map.of(
                Sword.TYPE, new ItemQuantifier(1, true),
                SunStone.TYPE, new ItemQuantifier(1, true)
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
    public MidnightArmour(Dungeon dungeon) {
        super(dungeon, TYPE);
    }

    public static void configure(JSONObject config) {
        attack = Config.getOptionalInt(config, "midnight_armour_attack");
        defence = Config.getOptionalInt(config, "midnight_armour_defence");
    }

    @Override
    public List<Map<String, ItemQuantifier>> getRecipes() throws InvalidActionException {
        if (!dungeon.getEntitiesOfType(ZombieToast.TYPE).isEmpty()) {
            throw new InvalidActionException("Cannot build due to zombies being present");
        } else {
            return RECIPES;
        }
    }

    @Override
    public void addModifications(BattleModifier modifier) {
        modifier.addAttackAdditive(attack);
        modifier.addDefenceAdditive(defence);
    }

    @Override
    public boolean isBroken() {
        return false;
    }
}
