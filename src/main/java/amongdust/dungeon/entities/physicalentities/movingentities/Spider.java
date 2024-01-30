package amongdust.dungeon.entities.physicalentities.movingentities;

import java.util.concurrent.ThreadLocalRandom;

import org.json.JSONObject;

import amongdust.dungeon.Dungeon;
import amongdust.dungeon.battle.Stats;
import amongdust.dungeon.entities.physicalentities.PhysicalEntity;
import amongdust.dungeon.entities.physicalentities.collisionstrategies.SpiderCollisionStrategy;
import amongdust.dungeon.entities.physicalentities.movingentities.movementstrategies.circularmovementstates.UpMovementStrategy;
import amongdust.dungeon.player.Visitor;
import amongdust.exceptions.InvalidMovementException;
import amongdust.util.Direction;
import amongdust.util.Position;

public class Spider extends MovingEntity {
    public static final String TYPE = "spider";
    private static final int MAX_SPAWN_RADIUS = 30;
    private static final int MIN_SPAWN_RADIUS = 5;
    private static int attack;
    private static int health;
    private static int timeSinceSpawn;
    private static int spawnRate;

    public Spider(Dungeon dungeon, Position position) {
        super(dungeon, TYPE, false, position, new UpMovementStrategy(), new SpiderCollisionStrategy(), new Stats(health, attack));
    }

    /**
     * Copy constructor for Spider.
     */
    private Spider(Dungeon newDungeon, Spider old) {
        super(newDungeon, old);
    }

    public static void configure(JSONObject config) {
        attack = config.getInt("spider_attack");
        health = config.getInt("spider_health");
        spawnRate = config.getInt("spider_spawn_rate");
        timeSinceSpawn = 0;
    }

    public static void tickSpawn(Dungeon dungeon) {
        if (spawnRate == 0) return;

        timeSinceSpawn++;
        if (timeSinceSpawn % spawnRate == 0) {
            Position playerPos = dungeon.getPlayerPosition();

            Position spawnPos = new Position(playerPos.getX() + generateDelta(), playerPos.getY() + generateDelta());

            Spider spider = new Spider(dungeon, spawnPos);
            dungeon.addEntity(spider);
            timeSinceSpawn = 0;
        }
    }

    @Override
    public void accept(Visitor visitor, Direction moveDirection) throws InvalidMovementException {
        visitor.visit(this, moveDirection);
    }

    @Override
    public PhysicalEntity getCopy(Dungeon newDungeon) {
        return new Spider(newDungeon, this);
    }

    private static int generateDelta() {
        int delta = ThreadLocalRandom.current().nextInt(MIN_SPAWN_RADIUS, MAX_SPAWN_RADIUS);
        if (ThreadLocalRandom.current().nextBoolean()) {
            delta = -delta;
        }
        return delta;
    }
}
