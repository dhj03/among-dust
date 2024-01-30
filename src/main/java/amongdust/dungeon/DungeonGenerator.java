package amongdust.dungeon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import amongdust.dungeon.entities.physicalentities.staticentities.Wall;
import amongdust.util.Position;

public class DungeonGenerator {
    private Dungeon dungeon;
    private Position start;
    private Position end;

    public DungeonGenerator(Dungeon dungeon, Position start, Position end) {
        this.dungeon = dungeon;
        this.start = start;
        this.end = end;
    }

    /**
     * @param start the top-left start Position
     * @param end the bottom-right end Position
     * @return a randomly generated maze Dungeon
     */
    public void randomisedPrims() {
        fillWalls();
        carveMaze();
        fixExit();
    }

    private void fillWalls() {
        int width = end.getX() - start.getX() + 1;
        int height = end.getY() - start.getY() + 1;

        for (int dy = -1; dy <= height; dy++) {
            for (int dx = -1; dx <= width; dx++) {
                Position position = start.translateBy(dx, dy);
                if (!position.equals(start)) {
                    dungeon.addEntity(new Wall(dungeon, position));
                }
            }
        }
    }

    private void carveMaze() {
        List<Position> options = new ArrayList<>();
        options.addAll(getNeighboursOfType(start, 2, Wall.TYPE));
        while (!options.isEmpty()) {
            Position next = popRandomPosition(options);
            List<Position> neighbours = getNeighboursEmpty(next, 2);

            if (!neighbours.isEmpty()) {
                Position neighbour = popRandomPosition(neighbours);
                Position posVector = Position.calculatePositionBetween(next, neighbour);
                Position between = next.translateBy(posVector.getX() / 2, posVector.getY() / 2);

                removeTypeFromPos(next, Wall.TYPE);
                removeTypeFromPos(between, Wall.TYPE);
            }
            options.addAll(getNeighboursOfType(next, 2, Wall.TYPE));
        }
    }

    private void fixExit() {
        if (!dungeon.getEntitiesAtPosition(end).stream().anyMatch(e -> e.getType().equals(Wall.TYPE))) {
            return;
        }
        removeTypeFromPos(end, Wall.TYPE);
        List<Position> neighbours = getNeighboursOfType(end, 1, Wall.TYPE);
        if (neighbours.size() == 2) {
            removeTypeFromPos(popRandomPosition(neighbours), Wall.TYPE);
        }
    }

    private void removeTypeFromPos(Position pos, String type) {
        dungeon.getEntitiesAtPosition(pos).stream().filter(e -> e.getType().equals(type)).forEach(w -> dungeon.removeEntity(w));
    }

    private Position popRandomPosition(List<Position> positions) {
        Random rand = new Random();
        return positions.remove(rand.nextInt(positions.size()));
    }

    private List<Position> getNeighboursEmpty(Position pos, int dist) {
        return getNeighboursNotBoundary(pos, dist).stream()
                .filter(p -> dungeon.getEntitiesAtPosition(p).stream().noneMatch(e -> e.getType().equals(Wall.TYPE)))
                .collect(Collectors.toList());
    }

    private List<Position> getNeighboursOfType(Position pos, int dist, String type) {
        return getNeighboursNotBoundary(pos, dist).stream()
                .filter(p -> dungeon.getPositionsOfEntitiesOfType(type).contains(p))
                .collect(Collectors.toList());
    }

    private List<Position> getNeighboursNotBoundary(Position pos, int dist) {
        return getNeighbours(pos, dist).stream()
                .filter(p -> p.getX() >= start.getX() && p.getX() <= end.getX()
                        && p.getY() >= start.getY() && p.getY() <= end.getY())
                .collect(Collectors.toList());
    }

    private List<Position> getNeighbours(Position pos, int dist) {
        return Arrays.asList(pos.translateBy(dist, 0), pos.translateBy(-dist, 0), pos.translateBy(0, dist), pos.translateBy(0, -dist));
    }
}
