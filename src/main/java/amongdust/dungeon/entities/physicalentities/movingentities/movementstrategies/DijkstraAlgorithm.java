package amongdust.dungeon.entities.physicalentities.movingentities.movementstrategies;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import amongdust.dungeon.Dungeon;
import amongdust.dungeon.entities.physicalentities.PhysicalEntity;
import amongdust.dungeon.entities.physicalentities.movingentities.MovingEntity;
import amongdust.dungeon.entities.physicalentities.staticentities.SwampTile;
import amongdust.exceptions.InvalidMovementException;
import amongdust.util.Direction;
import amongdust.util.Position;

public class DijkstraAlgorithm {
    private static final int INFINITY = Integer.MAX_VALUE;
    private static final double MAX_SEARCH_RADIUS = 75;
    private static final Direction[] DIRECTIONS = {Direction.DOWN, Direction.LEFT, Direction.RIGHT, Direction.UP};

    private static Map<Position, Integer> dist;
    private static Map<Position, Position> prevPos;
    private static Map<Position, Direction> prevDir;
    private static Map<Position, Boolean> visited;

    /**
     * Determines a path from the MovingEntity to the target Position,
     * and returns the Direction to move in from the MovingEntity's current Position.
     * If a path cannot be found or exceeds the search radius, null is returned.
     * 
     * @param dungeon The current Dungeon
     * @param movingEntity The pathing Entity
     * @param target The target Position being pathed to
     * @return The Direction to move if a path is found, or null otherwise.
     */
    public static Direction calculateDirection(Dungeon dungeon, MovingEntity movingEntity, Position target) {
        dist = new HashMap<>();
        prevPos = new HashMap<>();
        prevDir = new HashMap<>();
        visited = new HashMap<>();

        Position originPos = movingEntity.getPosition();
        dist.put(originPos, 0);
        prevPos.put(originPos, null);
        prevDir.put(originPos, null);

        // A Queue which prioritises smaller distances over larger ones.
        Queue<Position> q = new PriorityQueue<>((p1, p2) -> dist.getOrDefault(p1, INFINITY) - dist.getOrDefault(p2, INFINITY));
        q.add(originPos);
        boolean targetLocated = false;

        while (!q.isEmpty() && !targetLocated) {
            Position u = q.poll();

            // Skip u if already visited, or if the distance to it is greater than MAX_SEARCH_RADIUS
            if (visited.getOrDefault(u, false) || originPos.getDistanceTo(u) > MAX_SEARCH_RADIUS) {
                continue;
            }
            visited.put(u, true);

            targetLocated = explorePosition(dungeon, movingEntity, u, target, q);
            //outputDijkstra(movingEntity.getPosition(), target, u, 10);
        }

        if (targetLocated) {
            Position newPosition = target;
            while (prevPos.get(newPosition) != originPos) {
                newPosition = prevPos.get(newPosition);
            }
            return prevDir.get(newPosition);
        }
        return null;
    }

    /**
     * Explores the cardinal Directions surrounding `u` searching for a target Position.
     * 
     * @param dungeon The current Dungeon
     * @param movingEntity The pathing Entity
     * @param u The Position currently being explored
     * @param target The target Position being pathed to
     * @param dist A map containing the distance to a given Position
     * @param prevPos A map containing the previous Position of a Position
     * @param prevDir A map containing the previous Direction of a Position
     * @param q A Queue of Positions to explore
     * @return true if the target was found, false otherwise
     */
    private static boolean explorePosition(Dungeon dungeon, MovingEntity movingEntity, Position u, Position target, Queue<Position> q) {
        for (Direction d : DIRECTIONS) {
            Position v = getPositionAfterMove(dungeon, movingEntity, u, d);
            if (v == null) {
                visited.put(u.translateBy(d), true);
                continue;
            }
            q.add(v);

            int cost = dist.get(u) + movementCost(dungeon, v);
            if (cost < dist.getOrDefault(v, INFINITY)) {
                dist.put(v, cost);
                prevPos.put(v, u);
                prevDir.put(v, d);
                // Re-add v to the queue with updated priority.
                q.remove(v);
                q.add(v);
            }

            if (v.equals(target)) {
                return true;
            }
        }
        return false;
    }

    /*
     * Gets where the movingEntity ends up after moving in a Direction.
     * Useful since Portals can change how an Entity moves.
     */
    private static Position getPositionAfterMove(Dungeon dungeon, MovingEntity movingEntity, Position origin, Direction direction) {
        // Before trying anything, take a save
        Dungeon dungeonCopy = new Dungeon(dungeon);
        MovingEntity entityCopy = (MovingEntity) movingEntity.getCopy(dungeonCopy);
        entityCopy.move(origin);
        try {
            dungeonCopy.moveVisitor(entityCopy, direction);
            return entityCopy.getPosition();
        } catch (InvalidMovementException e) {
            return null;
        }
    }


    private static int movementCost(Dungeon dungeon, Position position) {
        List<PhysicalEntity> entities = dungeon.getEntitiesAtPosition(position);
        for (PhysicalEntity entity : entities) {
            if (entity instanceof SwampTile) {
                return ((SwampTile) entity).getMovementFactor() + 1;
            }
        }
        return 1;
    }

    /**
     * A handy visualiser to see how the Dijkstra algorithm is working.
     * Outputs to dijkstra.txt at the root folder.
     * 
     * @param origin The Position of the Entity
     * @param target The target Position being searched for, shown as { }
     * @param focus The current Position being explored, shown as < >
     * @param dist A map containing the distance to a given Position, shown as an integer
     * @param visited A map determining if a Position has been explored, shown as [ ]
     * @param radius The radius of the output
     */
    // Commented out so it doesn't impact coverage
    // private static void outputDijkstra(Position origin, Position target, Position focus, int radius) {
    //     int left = origin.getX() - radius;
    //     int right = origin.getX() + radius;
    //     int top = origin.getY() - radius;
    //     int bottom = origin.getY() + radius;

    //     String output = "";

    //     for (int y = top; y <= bottom; y++) {
    //         for (int x = left; x <= right; x++) {
    //             Position u = new Position(x, y);
    //             String tile = dist.getOrDefault(u, INFINITY).toString();
    //             if (tile.equals(Integer.toString(INFINITY))) {
    //                 tile = "-";
    //             }
    //             if (u.equals(focus)) {
    //                 tile = "<" + tile + ">";
    //             } else if (u.equals(target)) {
    //                 tile = "{" + tile + "}";
    //             } else if (visited.getOrDefault(u, false)) {
    //                 tile = "[" + tile + "]";
    //             } else {
    //                 tile = " " + tile + " ";
    //             }
    //             output += tile + " ";
    //         }
    //         output += "\n";
    //     }
    //     output += "\n";

    //     try {
    //         FileWriter fw = new FileWriter("dijkstra.txt", true);
    //         fw.write(output);
    //         fw.close();
    //     } catch (IOException e) {
    //     }
    // }
}
