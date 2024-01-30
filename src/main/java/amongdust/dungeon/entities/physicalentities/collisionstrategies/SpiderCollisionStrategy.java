package amongdust.dungeon.entities.physicalentities.collisionstrategies;

import amongdust.dungeon.Dungeon;
import amongdust.dungeon.entities.physicalentities.staticentities.Boulder;
import amongdust.dungeon.player.Visitor;
import amongdust.exceptions.InvalidMovementException;
import amongdust.util.Direction;

public class SpiderCollisionStrategy extends CollisionStrategy {

    @Override
    public void moveTo(Visitor visitor, Dungeon dungeon, Boulder boulder, Direction moveDirection)
            throws InvalidMovementException {
        throw new InvalidMovementException("Spider can't move onto boulder");
    }
}
