package amongdust.dungeon.entities.physicalentities.movingentities;

import java.util.Random;

import org.json.JSONObject;

import amongdust.dungeon.Config;
import amongdust.dungeon.Dungeon;
import amongdust.dungeon.battle.Stats;
import amongdust.dungeon.entities.physicalentities.PhysicalEntity;
import amongdust.dungeon.entities.physicalentities.movingentities.movementstrategies.TargetPlayerMovementStrategy;
import amongdust.dungeon.player.Visitor;
import amongdust.dungeon.player.playerstate.observer.PlayerStateSubscriber;
import amongdust.dungeon.player.playerstate.states.PlayerState;
import amongdust.exceptions.InvalidMovementException;
import amongdust.util.Direction;
import amongdust.util.Position;

public class Assassin extends Ally implements PlayerStateSubscriber {
    private static double failRate;
    private static int bribeAmount;
    private static int attack;
    private static int health;
    private static int reconRadius;
    private static Random random = new Random(System.currentTimeMillis());
    public static final String TYPE = "assassin";

    public Assassin(Dungeon dungeon, Position position) {
        super(dungeon, TYPE, position, new TargetPlayerMovementStrategy(), new Stats(health, attack));
    }

    private Assassin(Dungeon newDungeon, Assassin old) {
        super(newDungeon, old);
    }

    public static void configure(JSONObject config) {
        failRate = Config.getOptionalDouble(config, "assassin_bribe_fail_rate");
        bribeAmount = Config.getOptionalInt(config, "assassin_bribe_amount");
        attack = Config.getOptionalInt(config, "assassin_attack");
        health = Config.getOptionalInt(config, "assassin_health");
        reconRadius = Config.getOptionalInt(config, "assassin_recon_radius");
    }

    public boolean playerInReconRange() {
        return Position.inRange(getPosition(), dungeon.getPlayerPosition(), reconRadius);
    }

    @Override
    public boolean bribeSuccessful() {
        double roll = random.nextDouble();
        return roll > failRate;
    }

    @Override
    public void accept(Visitor visitor, Direction moveDirection) throws InvalidMovementException {
        visitor.visit(this, moveDirection);
    }

    @Override
    public PhysicalEntity getCopy(Dungeon newDungeon) {
        return new Assassin(newDungeon, this);
    }

    @Override
    public int getBribeAmount() {
        return bribeAmount;
    }

    @Override
    public void update(PlayerState state) {
        this.movementStrategy = state.getMovementStrategy(this);
    }
}
