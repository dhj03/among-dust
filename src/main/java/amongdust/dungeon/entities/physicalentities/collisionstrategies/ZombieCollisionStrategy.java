package amongdust.dungeon.entities.physicalentities.collisionstrategies;

import amongdust.dungeon.Dungeon;
import amongdust.dungeon.entities.physicalentities.staticentities.Boulder;
import amongdust.dungeon.entities.physicalentities.staticentities.Door;
import amongdust.dungeon.entities.physicalentities.staticentities.Portal;
import amongdust.dungeon.entities.physicalentities.staticentities.Wall;
import amongdust.dungeon.player.Visitor;
import amongdust.exceptions.InvalidMovementException;
import amongdust.util.Direction;

public class ZombieCollisionStrategy extends CollisionStrategy {

    @Override
    public void moveTo(Visitor visitor, Dungeon dungeon, Boulder boulder, Direction moveDirection) throws InvalidMovementException {
        // Move onto the boulder
        visitor.move(boulder.getPosition());

        // Move the boulder
        dungeon.moveVisitor(boulder, moveDirection);
    }

    @Override
    public void moveTo(Visitor visitor, Dungeon dungeon, Door door, Direction moveDirection) throws InvalidMovementException {
        if (!door.isLocked()) {
            visitor.move(door.getPosition());
        } else {
            throw new InvalidMovementException("Cannot open locked door");
        }
    }

    @Override
    public void moveTo(Visitor visitor, Dungeon dungeon, Wall wall, Direction moveDirection) throws InvalidMovementException {
        throw new InvalidMovementException("Cannot step onto wall");
    }

    @Override
    public void moveTo(Visitor visitor, Dungeon dungeon, Portal portal, Direction moveDirection) throws InvalidMovementException {
        throw new InvalidMovementException("Cannot use portal");
    }
}
