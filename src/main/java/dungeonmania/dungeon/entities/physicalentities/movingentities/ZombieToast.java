package dungeonmania.dungeon.entities.physicalentities.movingentities;

import org.json.JSONObject;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.dungeon.battle.Stats;
import dungeonmania.dungeon.entities.physicalentities.PhysicalEntity;
import dungeonmania.dungeon.entities.physicalentities.collisionstrategies.ZombieCollisionStrategy;
import dungeonmania.dungeon.entities.physicalentities.movingentities.movementstrategies.WanderMovementStrategy;
import dungeonmania.dungeon.player.Visitor;
import dungeonmania.dungeon.player.playerstate.observer.PlayerStateSubscriber;
import dungeonmania.dungeon.player.playerstate.states.PlayerState;
import dungeonmania.exceptions.InvalidMovementException;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class ZombieToast extends MovingEntity implements PlayerStateSubscriber {
    private static int attack;
    private static int health;

    public static final String TYPE = "zombie_toast";

    public ZombieToast(Dungeon dungeon, Position position) {
        this(dungeon, position, new Stats(health, attack));
    }

    protected ZombieToast(Dungeon dungeon, Position position, Stats stats) {
        super(dungeon, TYPE, false, position, new WanderMovementStrategy(), new ZombieCollisionStrategy(), stats);
    }

    /**
     * Copy constructor for ZombieToast.
     */
    protected ZombieToast(Dungeon newDungeon, ZombieToast old) {
        super(newDungeon, old);
    }

    public static void configure(JSONObject config) {
        attack = config.getInt("zombie_attack");
        health = config.getInt("zombie_health");
    }

    @Override
    public void accept(Visitor visitor, Direction moveDirection) throws InvalidMovementException {
        visitor.visit(this, moveDirection);
    }

    @Override
    public PhysicalEntity getCopy(Dungeon newDungeon) {
        return new ZombieToast(newDungeon, this);
    }

    @Override
    public void update(PlayerState state) {
        this.movementStrategy = state.getMovementStrategy(this);
    }
}
