package amongdust.dungeon.entities.physicalentities.collectableentities;

import amongdust.dungeon.Dungeon;
import amongdust.dungeon.entities.physicalentities.PhysicalEntity;
import amongdust.util.Position;

public class Arrow extends CollectableEntity {
    public static final String TYPE = "arrow";

    /**
     * Constructor for new entity where you want to generate new Id and
     * increment the number of entities. Use this when creating brand new entity.
     * @param type
     * @param isInteractable
     * @param position
     */
    public Arrow(Dungeon dungeon, Position position) {
        super(dungeon, TYPE, position);
    }

    /**
     * Copy constructor for Arrow.
     */
    private Arrow(Dungeon newDungeon, Arrow old) {
        super(newDungeon, old);
    }

    @Override
    public PhysicalEntity getCopy(Dungeon newDungeon) {
        return new Arrow(newDungeon, this);
    }
}
