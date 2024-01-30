package amongdust.dungeon.entities.physicalentities.collectableentities;

import org.json.JSONObject;

import amongdust.dungeon.Dungeon;
import amongdust.dungeon.battle.BattleModifier;
import amongdust.dungeon.entities.physicalentities.PhysicalEntity;
import amongdust.dungeon.player.Equipment;
import amongdust.util.Position;

public class Sword extends CollectableEntity implements Equipment {
    private int durability;
    private static int attack;
    private static int maxDurability;
    public static final String TYPE = "sword";

    /**
     * Constructor for new entity where you want to generate new Id and
     * increment the number of entities. Use this when creating brand new entity.
     * @param type
     * @param isInteractable
     * @param position
     */
    public Sword(Dungeon dungeon, Position position) {
        super(dungeon, TYPE, position);
        this.durability = maxDurability;
    }

    /**
     * Copy constructor for Sword.
     */
    private Sword(Dungeon newDungeon, Sword old) {
        super(newDungeon, old);
        this.durability = old.durability;
    }

    public static void configure(JSONObject config) {
        attack = config.getInt("sword_attack");
        maxDurability = config.getInt("sword_durability");
    }

    @Override
    public PhysicalEntity getCopy(Dungeon newDungeon) {
        return new Sword(newDungeon, this);
    }

    @Override
    public void addModifications(BattleModifier modifier) {
        modifier.addAttackAdditive(attack);
        durability--;
    }

    @Override
    public boolean isBroken() {
        return durability <= 0;
    }
}
