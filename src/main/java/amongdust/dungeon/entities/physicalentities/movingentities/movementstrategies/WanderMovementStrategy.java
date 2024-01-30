package amongdust.dungeon.entities.physicalentities.movingentities.movementstrategies;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import amongdust.dungeon.Dungeon;
import amongdust.dungeon.entities.physicalentities.movingentities.MovingEntity;
import amongdust.exceptions.InvalidMovementException;
import amongdust.util.Direction;

public class WanderMovementStrategy implements MovementStrategy {
    private static final List<Direction> DIRECTIONS = Arrays.asList(
        Direction.UP,
        Direction.DOWN,
        Direction.LEFT,
        Direction.RIGHT
    );

    @Override
    public void move(Dungeon dungeon, MovingEntity movingEntity) {
        // Shuffle the directions
        Collections.shuffle(DIRECTIONS);

        // Move in the first availiable direction
        for (Direction direction : DIRECTIONS) {
            try {
                dungeon.moveVisitor(movingEntity, direction);
                break;
            } catch (InvalidMovementException e) {
            }
        }
    }
}
