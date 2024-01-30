package amongdust.dungeon.entities.physicalentities.staticentities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import amongdust.dungeon.Dungeon;
import amongdust.dungeon.entities.physicalentities.PhysicalEntity;
import amongdust.dungeon.player.Visitor;
import amongdust.exceptions.InvalidMovementException;
import amongdust.util.Direction;
import amongdust.util.Position;

public class Portal extends PhysicalEntity {
    public static final String TYPE = "portal";
    private Position partnerPosition;
    private final String colour;

    /**
     * Constructor for new entity where you want to generate new Id and
     * increment the number of entities. Use this when creating brand new entity.
     * @param type
     * @param isInteractable
     * @param position
     */
    public Portal(Dungeon dungeon, Position position, String colour) {
        super(dungeon, TYPE, false, position);
        this.colour = colour;
    }

    /**
     * Copy constructor for Portal.
     */
    private Portal(Dungeon newDungeon, Portal old) {
        super(newDungeon, old);
        this.colour = old.colour;
        this.partnerPosition = old.partnerPosition;
    }

    public String getColour() {
        return colour;
    }

    public Position getPartnerPosition() {
        return partnerPosition;
    }

    public void setPartnerPosition(Position partnerPosition) {
        this.partnerPosition = partnerPosition;
    }

    public void accept(Visitor visitor, Direction moveDirection) throws InvalidMovementException {
        visitor.visit(this, moveDirection);
    }

    @Override
    public PhysicalEntity getCopy(Dungeon newDungeon) {
        Portal copy = new Portal(newDungeon, this);
        return copy;
    }

    /**
     * The type for portals are different to other stuff since they
     * follow the pattern "portal_RED" for example.
     */
    @Override
    public String getType() {
        return super.getType() + "_" + colour;
    }

    /**
     * Note that this assumes that each portal has *exactly* 1 partner (grouped by colour).
     * This is outlined in the spec, and there is no error checking.
     * @param entities
     */
    public static void connectPartnerPortals(Map<String, PhysicalEntity> entities) {
        // Make a new map with has a list of portals.
        // Each key corresponds to a colour and each list correponds to the Portal of that colour
        Map<String, List<Portal>> portalPairs = new HashMap<>();
        entities.values().stream().filter(e -> e instanceof Portal).forEach(e -> {
            Portal p = (Portal) e;
            String colour = p.getColour();
            portalPairs.putIfAbsent(colour, new ArrayList<Portal>());
            portalPairs.get(colour).add(p);
        });

        // Should now have a map of pairs, connect them
        portalPairs.values().stream().forEach(pair -> {
            Portal p1 = pair.get(0);
            Portal p2 = pair.get(1);
            p1.setPartnerPosition(p2.getPosition());
            p2.setPartnerPosition(p1.getPosition());
        });
    }

}
