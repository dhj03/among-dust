package amongdust.dungeon.entities.physicalentities.staticentities;

import java.util.List;

import org.json.JSONObject;

import amongdust.dungeon.Dungeon;
import amongdust.dungeon.entities.Interactable;
import amongdust.dungeon.entities.physicalentities.PhysicalEntity;
import amongdust.dungeon.entities.physicalentities.Tickable;
import amongdust.dungeon.entities.physicalentities.collectableentities.Sword;
import amongdust.dungeon.entities.physicalentities.movingentities.ZombieToast;
import amongdust.dungeon.player.Player;
import amongdust.dungeon.player.Visitor;
import amongdust.exceptions.InvalidActionException;
import amongdust.exceptions.InvalidMovementException;
import amongdust.util.Direction;
import amongdust.util.Position;

public class ZombieToastSpawner extends PhysicalEntity implements Tickable, Interactable {
    private int timeSinceSpawn;
    private static int spawnRate;
    public static final String TYPE = "zombie_toast_spawner";

    /**
     * Constructor for new entity where you want to generate new Id and
     * increment the number of entities. Use this when creating brand new entity.
     * @param type
     * @param isInteractable
     * @param position
     */
    public ZombieToastSpawner(Dungeon dungeon, Position position) {
        super(dungeon, TYPE, true, position);
        this.timeSinceSpawn = 1;
    }

    /**
     * Constructor for PhysicalEntity, where you can set your own Id (which may or may not already be used).
     * Used by to copy entities, can't see any other reason to use this.
     * @param id
     * @param type
     * @param isInteractable
     * @param position
     */
    public ZombieToastSpawner(Dungeon newDungeon, ZombieToastSpawner old) {
        super(newDungeon, old);
        this.timeSinceSpawn = old.timeSinceSpawn;
    }

    public static void configure(JSONObject config) {
        spawnRate = config.getInt("zombie_spawn_rate");
    }

    public void accept(Visitor visitor, Direction moveDirection) throws InvalidMovementException {
        visitor.visit(this, moveDirection);
    }

    @Override
    public void tick() {
        if (spawnRate == 0) return;

        if (timeSinceSpawn % spawnRate == 0) {
            spawn();
            timeSinceSpawn = 0;
        }
        timeSinceSpawn++;
    }

    @Override
    public PhysicalEntity getCopy(Dungeon newDungeon) {
        return new ZombieToastSpawner(newDungeon, this);
    }

    private void spawn() {
        // Spawn Zombie Toast on top of the Spawner
        ZombieToast newZombieToast = new ZombieToast(this.dungeon, this.getPosition());
        dungeon.addEntity(newZombieToast);

        // Move (or at least try)
        newZombieToast.tick();

        // If we couldn't move, we should still be on the same spot.
        // Remove it.
        if (newZombieToast.getPosition().equals(this.getPosition())) {
            dungeon.removeEntity(newZombieToast);
        }
    }

    @Override
    public void interact(Player player) throws InvalidActionException {

        // Check to see whether the player is cardinally adjacent
        List<Position> validPositions = this.getPosition().getCardinallyAdjacentPositions();

        if (!validPositions.stream()
                .anyMatch(p -> p.equals(player.getPosition()))) {

            throw new InvalidActionException("Player not cardinally adjacent to spawner");
        }

        // Check to see whether player has a sword
        if (player.getNumInInventory(Sword.TYPE) < 1) {
            throw new InvalidActionException("Player does not have a weapon");
        }

        dungeon.removeEntity(this);
    }
}
