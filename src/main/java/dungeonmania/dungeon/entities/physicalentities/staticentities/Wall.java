package dungeonmania.dungeon.entities.physicalentities.staticentities;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.dungeon.entities.physicalentities.PhysicalEntity;
import dungeonmania.dungeon.player.Visitor;
import dungeonmania.exceptions.InvalidMovementException;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Wall extends PhysicalEntity {
    public static final String TYPE = "wall";

    /**
     * Constructor for new entity where you want to generate new Id and
     * increment the number of entities. Use this when creating brand new entity.
     * @param type
     * @param isInteractable
     * @param position
     */
    public Wall(Dungeon dungeon, Position position) {
        super(dungeon, TYPE, false, position);
    }

    /**
     * Copy constructor for Wall.
     */
    private Wall(Dungeon newDungeon, Wall old) {
        super(newDungeon, old);
    }

    public void accept(Visitor visitor, Direction moveDirection) throws InvalidMovementException {
        visitor.visit(this, moveDirection);
    }

    @Override
    public PhysicalEntity getCopy(Dungeon newDungeon) {
        return new Wall(newDungeon, this);
    }
}
