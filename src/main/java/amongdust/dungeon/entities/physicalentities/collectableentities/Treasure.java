package amongdust.dungeon.entities.physicalentities.collectableentities;

import amongdust.dungeon.Dungeon;
import amongdust.dungeon.entities.physicalentities.PhysicalEntity;
import amongdust.util.Position;

public class Treasure extends CollectableEntity {
    public static final String TYPE = "treasure";

    /**
     * Constructor for new entity where you want to generate new Id and
     * increment the number of entities. Use this when creating brand new entity.
     * @param type
     * @param isInteractable
     * @param position
     */
    public Treasure(Dungeon dungeon, Position position) {
        super(dungeon, TYPE, position);
    }

    /**
     * Copy constructor for Treasure.
     */
    private Treasure(Dungeon newDungeon, Treasure old) {
        super(newDungeon, old);
    }

    @Override
    public PhysicalEntity getCopy(Dungeon newDungeon) {
        return new Treasure(newDungeon, this);
    }
}
