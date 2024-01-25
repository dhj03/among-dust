package dungeonmania.dungeon.entities.physicalentities.collisionstrategies;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.dungeon.entities.physicalentities.staticentities.Boulder;
import dungeonmania.dungeon.player.Visitor;
import dungeonmania.exceptions.InvalidMovementException;
import dungeonmania.util.Direction;

public class SpiderCollisionStrategy extends CollisionStrategy {

    @Override
    public void moveTo(Visitor visitor, Dungeon dungeon, Boulder boulder, Direction moveDirection)
            throws InvalidMovementException {
        throw new InvalidMovementException("Spider can't move onto boulder");
    }
}
