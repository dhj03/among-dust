package amongdust.dungeon.entities.physicalentities.movingentities.movementstrategies;

import amongdust.dungeon.Dungeon;
import amongdust.dungeon.entities.physicalentities.movingentities.MovingEntity;
import amongdust.exceptions.InvalidMovementException;
import amongdust.util.Direction;

public class TargetPlayerMovementStrategy implements MovementStrategy {

    @Override
    public void move(Dungeon dungeon, MovingEntity movingEntity) {
        Direction moveDirection = DijkstraAlgorithm.calculateDirection(dungeon, movingEntity, dungeon.getPlayerPosition());

        // If there is no path towards the Player, move randomly.
        if (moveDirection == null) {
            MovementStrategy wanderStrategy = new WanderMovementStrategy();
            wanderStrategy.move(dungeon, movingEntity);
            return;
        }

        try {
            dungeon.moveVisitor(movingEntity, moveDirection);
        } catch (InvalidMovementException e) {
        }
    }
}