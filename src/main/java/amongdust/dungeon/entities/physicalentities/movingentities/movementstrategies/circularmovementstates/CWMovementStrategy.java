package amongdust.dungeon.entities.physicalentities.movingentities.movementstrategies.circularmovementstates;

import java.util.Map;

import amongdust.dungeon.Dungeon;
import amongdust.dungeon.entities.physicalentities.movingentities.MovingEntity;
import amongdust.dungeon.entities.physicalentities.movingentities.movementstrategies.MovementStrategy;
import amongdust.exceptions.InvalidMovementException;
import amongdust.util.Direction;
import amongdust.util.Position;

public class CWMovementStrategy implements MovementStrategy {
    private final Position center;
    private int changeDirectionCount;

    public CWMovementStrategy(Position center) {
        this(center, 0);
    }

    public CWMovementStrategy(Position center, int changeDirectionCount) {
        this.center = center;
        this.changeDirectionCount = changeDirectionCount;
    }

    @Override
    public void move(Dungeon dungeon, MovingEntity movingEntity) {
        if (changeDirectionCount == 2) {
            changeDirectionCount = 0;
            return;
        }
        Direction direction = getDirectionMap(center).get(movingEntity.getPosition());
        try {
            dungeon.moveVisitor(movingEntity, direction);
            changeDirectionCount = 0;
        } catch (InvalidMovementException e) {
            setNextState(movingEntity);
            movingEntity.getMovementStrategy().move(dungeon, movingEntity);
        }
    }

    public void setNextState(MovingEntity movingEntity) {
        movingEntity.setMovementStrategy(new CCWMovementStrategy(getCenter(), updateChangeDirectionCount()));
    }

    public Position getCenter() {
        return center;
    }

    public int updateChangeDirectionCount() {
        return changeDirectionCount++;
    }

    public Map<Position, Direction> getDirectionMap(Position position) {
        return Map.of(
            new Position(position.getX(),     position.getY()    ), Direction.UP,
            new Position(position.getX(),     position.getY() - 1), Direction.RIGHT,
            new Position(position.getX() + 1, position.getY() - 1), Direction.DOWN,
            new Position(position.getX() + 1, position.getY()    ), Direction.DOWN,
            new Position(position.getX() + 1, position.getY() + 1), Direction.LEFT,
            new Position(position.getX(),     position.getY() + 1), Direction.LEFT,
            new Position(position.getX() - 1, position.getY() + 1), Direction.UP,
            new Position(position.getX() - 1, position.getY()    ), Direction.UP,
            new Position(position.getX() - 1, position.getY() - 1), Direction.RIGHT
        );
    }
}
