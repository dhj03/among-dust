package amongdust.dungeon.entities.physicalentities.collectableentities;

import org.json.JSONObject;

import amongdust.dungeon.Dungeon;
import amongdust.dungeon.entities.physicalentities.PhysicalEntity;
import amongdust.dungeon.player.Player;
import amongdust.dungeon.player.playerstate.states.InvinciblePlayerState;
import amongdust.util.Position;

public class InvincibilityPotion extends CollectableEntity implements Usable {
    private static int duration;
    public static final String TYPE = "invincibility_potion";

    /**
     * Constructor for new entity where you want to generate new Id and
     * increment the number of entities. Use this when creating brand new entity.
     * @param type
     * @param isInteractable
     * @param position
     */
    public InvincibilityPotion(Dungeon dungeon, Position position) {
        super(dungeon, TYPE, position);
    }

    /**
     * Copy constructor for InvincibilityPotion.
     */
    private InvincibilityPotion(Dungeon newDungeon, InvincibilityPotion old) {
        super(newDungeon, old);
    }

    public static void configure(JSONObject config) {
        duration = config.getInt("invincibility_potion_duration");
    }

    @Override
    public PhysicalEntity getCopy(Dungeon newDungeon) {
        return new InvincibilityPotion(newDungeon, this);
    }

    @Override
    public void use(Player player) {
        player.consume(this);
        player.enqueueState(new InvinciblePlayerState(this, duration));
    }
}
