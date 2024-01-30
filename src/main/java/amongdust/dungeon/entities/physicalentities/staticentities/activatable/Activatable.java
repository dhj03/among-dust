package amongdust.dungeon.entities.physicalentities.staticentities.activatable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import amongdust.dungeon.Dungeon;
import amongdust.dungeon.entities.physicalentities.PhysicalEntity;
import amongdust.dungeon.entities.physicalentities.Tickable;
import amongdust.dungeon.entities.physicalentities.staticentities.activatable.activatablestrategies.ActivatableStrategy;
import amongdust.dungeon.player.Visitor;
import amongdust.exceptions.InvalidMovementException;
import amongdust.util.Direction;
import amongdust.util.Position;

public abstract class Activatable extends PhysicalEntity implements Tickable {
    protected ActivatableStrategy activatableStrategy;
    protected int timeSinceActivation;

    /**
     * Constructor for new logic entity where you want to generate new Id and
     * increment the number of entities. Use this when creating brand new entity.
     * @param type
     * @param isInteractable
     * @param position
     */
    public Activatable(Dungeon dungeon, String type, boolean isInteractable, Position position, ActivatableStrategy activatableStrategy) {
        super(dungeon, type, isInteractable, position);
        this.activatableStrategy = activatableStrategy;
        this.timeSinceActivation = -1;
    }

    /**
     * Copy constructor for Activatable.
     */
    protected Activatable(Dungeon newDungeon, Activatable old) {
        super(newDungeon, old);
        this.activatableStrategy = old.activatableStrategy;
        this.timeSinceActivation = old.timeSinceActivation;
    }

    @Override
    public void accept(Visitor visitor, Direction moveDirection) throws InvalidMovementException {
        visitor.visit(this, moveDirection);
    }

    public boolean isActive() {
        return isActive(new HashMap<Position, Boolean>());
    }

    protected boolean isActive(Map<Position, Boolean> visited) {
        // Mark this position as visited
        visited.put(getPosition(), false);

        // Go deeper on adjacent
        List<PhysicalEntity> adjacentEntities = new ArrayList<>();
        getPosition().getCardinallyAdjacentPositions()
                     .stream()
                     .forEach(p -> adjacentEntities.addAll(dungeon.getEntitiesAtPosition(p)));

        // Figure out if adjacent that we haven't visited yet are active
        adjacentEntities.stream().filter(e -> e instanceof Activatable && !visited.containsKey(e.getPosition()))
                                 .map(e -> (Activatable) e)
                                 .forEach(a -> {
                                    boolean currentValue = visited.getOrDefault(a.getPosition(), false);
                                    visited.put(a.getPosition(), a.isActive(visited) || currentValue);
        });

        // Figure out if this is active
        return activatableStrategy.isActive(dungeon, getPosition(), visited);
    }

    public ActivatableStrategy getActivatableStrategy() {
        return activatableStrategy;
    }

    public int getTimeSinceActivation() {
        return timeSinceActivation;
    }

    @Override
    public void tick() {
        if (!isActive()) {
            timeSinceActivation = -1;
        } else {
            timeSinceActivation++;
        }
    }
}
