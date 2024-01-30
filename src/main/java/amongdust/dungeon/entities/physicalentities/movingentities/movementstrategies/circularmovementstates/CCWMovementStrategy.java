package amongdust.dungeon.entities.physicalentities.movingentities.movementstrategies.circularmovementstates;

import java.util.Map;

import amongdust.dungeon.entities.physicalentities.movingentities.MovingEntity;
import amongdust.util.Direction;
import amongdust.util.Position;

public class CCWMovementStrategy extends CWMovementStrategy {

    public CCWMovementStrategy(Position center, int changeDirectionCount) {
        super(center, changeDirectionCount);
    }

    @Override
    public void setNextState(MovingEntity movingEntity) {
        movingEntity.setMovementStrategy(new CWMovementStrategy(getCenter(), updateChangeDirectionCount()));
    }

    @Override
    public Map<Position, Direction> getDirectionMap(Position position) {
        return Map.of(
            new Position(position.getX(),     position.getY()    ), Direction.DOWN,
            new Position(position.getX(),     position.getY() - 1), Direction.LEFT,
            new Position(position.getX() + 1, position.getY() - 1), Direction.LEFT,
            new Position(position.getX() + 1, position.getY()    ), Direction.UP,
            new Position(position.getX() + 1, position.getY() + 1), Direction.UP,
            new Position(position.getX(),     position.getY() + 1), Direction.RIGHT,
            new Position(position.getX() - 1, position.getY() + 1), Direction.RIGHT,
            new Position(position.getX() - 1, position.getY()    ), Direction.DOWN,
            new Position(position.getX() - 1, position.getY() - 1), Direction.DOWN
        );
    }

}
