package dungeonmania.dungeon.entities.physicalentities.collisionstrategies;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.dungeon.entities.physicalentities.staticentities.Portal;
import dungeonmania.dungeon.player.Visitor;
import dungeonmania.exceptions.InvalidMovementException;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class AllyCollisionStrategy extends ZombieCollisionStrategy {

    @Override
    public void moveTo(Visitor visitor, Dungeon dungeon, Portal portal, Direction moveDirection) throws InvalidMovementException {
        Position partnerPosition = portal.getPartnerPosition();
        visitor.move(partnerPosition);

        // Try and move again
        dungeon.moveVisitor(visitor, moveDirection);
    }
}
