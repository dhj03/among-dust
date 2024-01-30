package amongdust.dungeon.entities;

import java.io.Serializable;

import amongdust.dungeon.Dungeon;

public abstract class Entity implements Serializable {
    protected Dungeon dungeon;
    private final String id;
    private final String type;
    private static int entityCount = 0;

    /**
     * Constructor for new entity where you want to generate new Id and
     * increment the number of entities. Use this when creating brand new entity.
     * @param type
     */
    public Entity(Dungeon dungeon, String type) {
        this.id = getNewId();
        this.type = type;
        this.dungeon = dungeon;
    }

    /**
     * Copy constructor for Entity
     */
    public Entity(Dungeon newDungeon, Entity old) {
        this.id = old.id;
        this.type = old.type;
        this.dungeon = newDungeon;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    // Static methods
    private static String getNewId() {
        return Integer.toString(entityCount++);
    }
}
