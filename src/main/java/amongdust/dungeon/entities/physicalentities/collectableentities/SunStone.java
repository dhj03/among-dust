package amongdust.dungeon.entities.physicalentities.collectableentities;

import amongdust.dungeon.Dungeon;
import amongdust.dungeon.entities.physicalentities.PhysicalEntity;
import amongdust.util.Position;

public class SunStone extends CollectableEntity {
    public static final String TYPE = "sun_stone";
    /**
     * Constructor for new entity where you want to generate new Id and
     * increment the number of entities. Use this when creating brand new entity.
     * @param type
     * @param isInteractable
     * @param position
     */
    public SunStone(Dungeon dungeon, Position position) {
        super(dungeon, TYPE, position);
    }

    /**
     * Copy constructor for Wood.
     */
    private SunStone(Dungeon newDungeon, SunStone old) {
        super(newDungeon, old);
    }

    @Override
    public PhysicalEntity getCopy(Dungeon newDungeon) {
        return new SunStone(newDungeon, this);
    }
}
