package amongdust.dungeon.entities.physicalentities.collectableentities;

import org.json.JSONObject;

import amongdust.dungeon.Dungeon;
import amongdust.dungeon.entities.physicalentities.PhysicalEntity;
import amongdust.dungeon.player.Player;
import amongdust.dungeon.player.playerstate.states.InvisiblePlayerState;
import amongdust.util.Position;

public class InvisibilityPotion extends CollectableEntity implements Usable {
    private static int duration;
    public static final String TYPE = "invisibility_potion";

    /**
     * Constructor for new entity where you want to generate new Id and
     * increment the number of entities. Use this when creating brand new entity.
     * @param type
     * @param isInteractable
     * @param position
     */
    public InvisibilityPotion(Dungeon dungeon, Position position) {
        super(dungeon, TYPE, position);
    }

    /**
     * Copy constructor for InvisibilityPotion.
     */
    private InvisibilityPotion(Dungeon newDungeon, InvisibilityPotion old) {
        super(newDungeon, old);
    }

    public static void configure(JSONObject config) {
        duration = config.getInt("invisibility_potion_duration");
    }

    @Override
    public PhysicalEntity getCopy(Dungeon newDungeon) {
        return new InvisibilityPotion(newDungeon, this);
    }

    @Override
    public void use(Player player) {
        player.consume(this);
        player.enqueueState(new InvisiblePlayerState(this, duration));
    }
}
