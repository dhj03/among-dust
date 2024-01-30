package amongdust.dungeon.entities.physicalentities.collisionstrategies;

import amongdust.dungeon.Dungeon;
import amongdust.dungeon.entities.physicalentities.collectableentities.CollectableEntity;
import amongdust.dungeon.entities.physicalentities.collectableentities.Key;
import amongdust.dungeon.entities.physicalentities.staticentities.Door;
import amongdust.dungeon.player.Player;
import amongdust.dungeon.player.Visitor;
import amongdust.exceptions.InvalidMovementException;
import amongdust.util.Direction;

public class PlayerCollisionStrategy extends AllyCollisionStrategy {
    @Override
    public void moveTo(Visitor visitor, Dungeon dungeon, CollectableEntity collectable, Direction moveDirection) throws InvalidMovementException {
        super.moveTo(visitor, dungeon, collectable, moveDirection);

        if (!(visitor instanceof Player)) {
            return;
        }
        Player player = (Player) visitor;

        // Remove the collectable from the map and add to the inventory, unless it's a key
        if (!(collectable instanceof Key) || player.getNumInInventory(Key.TYPE) <= 0) {
            // Pick it up
            dungeon.removeEntity(collectable);
            player.addToInventory(collectable);
        }
    }

    @Override
    public void moveTo(Visitor visitor, Dungeon dungeon, Door door, Direction moveDirection) throws InvalidMovementException {
        try {
            super.moveTo(visitor, dungeon, door, moveDirection);
            return;
        } catch (InvalidMovementException e) {
            // Door is locked.
        }

        if (!(visitor instanceof Player)) {
            return;
        }
        Player player = (Player) visitor;
        if (!player.openDoor(door)) {
            throw new InvalidMovementException("Player attempted to move onto locked door");
        }
        dungeon.moveVisitor(visitor, moveDirection);
    }
}
