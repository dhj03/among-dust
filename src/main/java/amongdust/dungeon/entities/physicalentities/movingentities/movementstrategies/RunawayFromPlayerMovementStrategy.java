package amongdust.dungeon.entities.physicalentities.movingentities.movementstrategies;

import java.util.List;
import java.util.stream.Collectors;

import amongdust.dungeon.Dungeon;
import amongdust.dungeon.entities.physicalentities.movingentities.MovingEntity;
import amongdust.exceptions.InvalidMovementException;
import amongdust.util.Direction;
import amongdust.util.Position;

public class RunawayFromPlayerMovementStrategy implements MovementStrategy {
    @Override
    public void move(Dungeon dungeon, MovingEntity movingEntity) {
        Position targetPos = dungeon.getPlayerPosition();

        List<Direction> directions = TargetPosition.generate(movingEntity, targetPos).stream()
                                                   .map(d -> oppositeDirection(d))
                                                   .collect(Collectors.toList());

        for (Direction direction : directions) {
            try {
                dungeon.moveVisitor(movingEntity, direction);
                break;
            } catch (InvalidMovementException e) {
            }
        }
    }

    private Direction oppositeDirection(Direction direction) {
        switch (direction) {
            case UP:
                return Direction.DOWN;
            case DOWN:
                return Direction.UP;
            case LEFT:
                return Direction.RIGHT;
            case RIGHT:
                return Direction.LEFT;
            default:
                throw new IllegalArgumentException("Invalid direction given");
        }
    }
}
