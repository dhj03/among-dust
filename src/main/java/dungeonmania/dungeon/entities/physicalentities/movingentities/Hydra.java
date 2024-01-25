package dungeonmania.dungeon.entities.physicalentities.movingentities;

import java.util.Random;

import org.json.JSONObject;

import dungeonmania.dungeon.Config;
import dungeonmania.dungeon.Dungeon;
import dungeonmania.dungeon.battle.BattleModifier;
import dungeonmania.dungeon.battle.Stats;
import dungeonmania.dungeon.entities.physicalentities.PhysicalEntity;
import dungeonmania.dungeon.player.playerstate.states.PlayerState;
import dungeonmania.util.Position;

public class Hydra extends ZombieToast {
    private static int attack;
    private static int health;
    private static double regenRate;
    private static int regenAmount;
    private static Random random = new Random();

    public static final String TYPE = "hydra";

    public Hydra(Dungeon dungeon, Position position) {
        super(dungeon, position, new Stats(attack, health));
    }

    /**
     * Copy constructor for Hydra
     */
    private Hydra(Dungeon newDungeon, Hydra hydra) {
        super(newDungeon, hydra);
    }

    public static void configure(JSONObject config) {
        attack = Config.getOptionalInt(config, "hydra_attack");
        health = Config.getOptionalInt(config, "hydra_health");
        regenRate = Config.getOptionalDouble(config, "hydra_health_increase_rate");
        regenAmount = Config.getOptionalInt(config, "hydra_health_increase_amount");
    }

    @Override
    public PhysicalEntity getCopy(Dungeon newDungeon) {
        return new Hydra(dungeon, this);
    }

    @Override
    public double getDeltaHealth(PlayerState state, Stats opponentStats, BattleModifier modifier) {
        if (random.nextDouble() < regenRate) {
            return regenAmount;
        }

        return super.getDeltaHealth(state, opponentStats, modifier);
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
