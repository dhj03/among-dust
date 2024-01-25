package dungeonmania.dungeon.entities.physicalentities.movingentities.movementstrategies.circularmovementstates;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.dungeon.entities.physicalentities.movingentities.MovingEntity;
import dungeonmania.dungeon.entities.physicalentities.movingentities.movementstrategies.MovementStrategy;
import dungeonmania.exceptions.InvalidMovementException;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

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
