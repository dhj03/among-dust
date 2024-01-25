package dungeonmania.dungeon.entities.physicalentities.movingentities;

import org.json.JSONObject;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.dungeon.battle.Stats;
import dungeonmania.dungeon.entities.physicalentities.PhysicalEntity;
import dungeonmania.dungeon.entities.physicalentities.movingentities.movementstrategies.TargetPlayerMovementStrategy;
import dungeonmania.dungeon.player.Visitor;
import dungeonmania.dungeon.player.playerstate.observer.PlayerStateSubscriber;
import dungeonmania.dungeon.player.playerstate.states.PlayerState;
import dungeonmania.exceptions.InvalidMovementException;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Mercenary extends Ally implements PlayerStateSubscriber {
    private static int bribeAmount;
    private static int attack;
    private static int health;
    public static final String TYPE = "mercenary";

    public Mercenary(Dungeon dungeon, Position position) {
        super(dungeon, TYPE, position, new TargetPlayerMovementStrategy(), new Stats(health, attack));
    }

    /**
     * Copy constructor for Mercenary.
     */
    private Mercenary(Dungeon newDungeon, Mercenary old) {
        super(newDungeon, old);
    }

    public static void configure(JSONObject config) {
        bribeAmount = config.getInt("bribe_amount");
        attack = config.getInt("mercenary_attack");
        health = config.getInt("mercenary_health");
    }

    @Override
    public void accept(Visitor visitor, Direction moveDirection) throws InvalidMovementException {
        visitor.visit(this, moveDirection);
    }

    @Override
    public PhysicalEntity getCopy(Dungeon newDungeon) {
        return new Mercenary(newDungeon, this);
    }

    @Override
    public void update(PlayerState state) {
        this.movementStrategy = state.getMovementStrategy(this);
    }

    @Override
    public boolean bribeSuccessful() {
        return true;
    }

    @Override
    public int getBribeAmount() {
        return bribeAmount;
    }
}
