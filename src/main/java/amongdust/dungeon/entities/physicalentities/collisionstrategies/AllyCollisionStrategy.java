package amongdust.dungeon.entities.physicalentities.collisionstrategies;

import amongdust.dungeon.Dungeon;
import amongdust.dungeon.entities.physicalentities.staticentities.Portal;
import amongdust.dungeon.player.Visitor;
import amongdust.exceptions.InvalidMovementException;
import amongdust.util.Direction;
import amongdust.util.Position;

public class AllyCollisionStrategy extends ZombieCollisionStrategy {

    @Override
    public void moveTo(Visitor visitor, Dungeon dungeon, Portal portal, Direction moveDirection) throws InvalidMovementException {
        Position partnerPosition = portal.getPartnerPosition();
        visitor.move(partnerPosition);

        // Try and move again
        dungeon.moveVisitor(visitor, moveDirection);
    }
}
