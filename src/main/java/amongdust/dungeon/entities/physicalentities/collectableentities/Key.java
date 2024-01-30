package amongdust.dungeon.entities.physicalentities.collectableentities;

import amongdust.dungeon.Dungeon;
import amongdust.dungeon.entities.physicalentities.PhysicalEntity;
import amongdust.util.Position;

public class Key extends CollectableEntity {
    private final int keyNum;
    public static final String TYPE = "key";

    /**
     * Constructor for new entity where you want to generate new Id and
     * increment the number of entities. Use this when creating brand new entity.
     * @param type
     * @param isInteractable
     * @param position
     */
    public Key(Dungeon dungeon, Position position, int keyNum) {
        super(dungeon, TYPE, position);
        this.keyNum = keyNum;
    }

    /**
     * Copy constructor for Key.
     */
    private Key(Dungeon newDungeon, Key old) {
        super(newDungeon, old);
        this.keyNum = old.keyNum;
    }

    public boolean matchesDoor(int keyNum) {
        return this.keyNum == keyNum;
    }

    @Override
    public PhysicalEntity getCopy(Dungeon newDungeon) {
        return new Key(newDungeon, this);
    }
}
