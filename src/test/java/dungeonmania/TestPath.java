package dungeonmania;

import java.util.List;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class TestPath {
    private List<Direction> directions;
    private Position expectedRelPosition;
    
    public TestPath( List<Direction> directions, Position expectedRelPosition) {
        this.directions = directions;
        this.expectedRelPosition = expectedRelPosition;
    }

    public List<Direction> getDirections() {
        return this.directions;
    }

    public Position getExpectedRelPosition() {
        return this.expectedRelPosition;
    }
}