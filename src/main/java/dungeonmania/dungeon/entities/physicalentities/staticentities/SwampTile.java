package dungeonmania.dungeon.entities.physicalentities.staticentities;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.dungeon.entities.physicalentities.PhysicalEntity;
import dungeonmania.dungeon.player.Visitor;
import dungeonmania.exceptions.InvalidMovementException;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class SwampTile extends PhysicalEntity {
    public static final String TYPE = "swamp_tile";
    private final int movementFactor;

    public SwampTile(Dungeon dungeon, Position position, int movementFactor) {
        super(dungeon, TYPE, false, position);
        this.movementFactor = movementFactor;
    }

    private SwampTile(Dungeon newDungeon, SwampTile old) {
        super(newDungeon, old);
        this.movementFactor = old.movementFactor;
    }

    public int getMovementFactor() {
        return movementFactor;
    }

    @Override
    public void accept(Visitor visitor, Direction moveDirection) throws InvalidMovementException {
        visitor.visit(this, moveDirection);
    }

    @Override
    public PhysicalEntity getCopy(Dungeon newDungeon) {
        return new SwampTile(newDungeon, this);
    }
}
