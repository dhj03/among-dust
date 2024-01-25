package dungeonmania.dungeon.entities.physicalentities.collisionstrategies;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.dungeon.entities.physicalentities.staticentities.Boulder;
import dungeonmania.dungeon.entities.physicalentities.staticentities.Door;
import dungeonmania.dungeon.entities.physicalentities.staticentities.Portal;
import dungeonmania.dungeon.entities.physicalentities.staticentities.Wall;
import dungeonmania.dungeon.player.Visitor;
import dungeonmania.exceptions.InvalidMovementException;
import dungeonmania.util.Direction;

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
