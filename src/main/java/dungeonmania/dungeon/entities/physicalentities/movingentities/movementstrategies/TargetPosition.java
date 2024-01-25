package dungeonmania.dungeon.entities.physicalentities.movingentities.movementstrategies;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.dungeon.entities.physicalentities.movingentities.MovingEntity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class TargetPosition {
    public static List<Direction> generate(MovingEntity entity, Position targetPosition) {
        Position entityPosition = entity.getPosition();

        Position posVector = Position.calculatePositionBetween(entityPosition, targetPosition);
        Direction dir = getDirectionFromVector(posVector);

        List<Direction> directions = new ArrayList<>();
        directions.add(dir);

        Direction dirCW = rotateClockwise(dir);
        Direction dirCCW = rotateCounterclockwise(dir);
        double d1 = targetPosition.getDistanceTo(entityPosition.translateBy(dirCW));
        double d2 = targetPosition.getDistanceTo(entityPosition.translateBy(dirCCW));

        // Add direction with least distance first.
        directions.add((d1 < d2) ? dirCW : dirCCW);
        directions.add((d1 >= d2) ? dirCW : dirCCW);

        return directions;
    }

    private static Direction getDirectionFromVector(Position posVector) {
        double angle = Math.atan2(posVector.getY(), posVector.getX());
        int sections = (int) (4.0 * angle / Math.PI);

        switch (sections) {
            case -2:
            case -1:
                return Direction.UP;
            case 0:
                return Direction.RIGHT;
            case 1:
            case 2:
                return Direction.DOWN;
            default:
                return Direction.LEFT;
        }
    }

    private static Direction rotateClockwise(Direction dir) {
        switch (dir) {
            case LEFT:
                return Direction.UP;
            case UP:
                return Direction.RIGHT;
            case RIGHT:
                return Direction.DOWN;
            case DOWN:
            default:
                return Direction.LEFT;
        }
    }

    private static Direction rotateCounterclockwise(Direction dir) {
        switch (dir) {
            case LEFT:
                return Direction.DOWN;
            case DOWN:
                return Direction.RIGHT;
            case RIGHT:
                return Direction.UP;
            case UP:
            default:
                return Direction.LEFT;
        }
    }

}
