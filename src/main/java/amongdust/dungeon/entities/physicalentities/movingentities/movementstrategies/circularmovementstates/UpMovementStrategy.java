package amongdust.dungeon.entities.physicalentities.movingentities.movementstrategies.circularmovementstates;

import amongdust.dungeon.Dungeon;
import amongdust.dungeon.entities.physicalentities.movingentities.MovingEntity;
import amongdust.dungeon.entities.physicalentities.movingentities.movementstrategies.MovementStrategy;
import amongdust.exceptions.InvalidMovementException;
import amongdust.util.Direction;
import amongdust.util.Position;

public class UpMovementStrategy implements MovementStrategy {

    @Override
    public void move(Dungeon dungeon, MovingEntity movingEntity) {
        Position origin = movingEntity.getPosition();
        try {
            dungeon.moveVisitor(movingEntity, Direction.UP);
            movingEntity.setMovementStrategy(new CWMovementStrategy(origin));
        } catch (InvalidMovementException e) {
        }
    }
}
