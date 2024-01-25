package dungeonmania.dungeon.entities.physicalentities.collectableentities;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.dungeon.entities.physicalentities.PhysicalEntity;
import dungeonmania.dungeon.entities.physicalentities.Tickable;
import dungeonmania.dungeon.entities.physicalentities.staticentities.activatable.Activatable;
import dungeonmania.dungeon.entities.physicalentities.staticentities.activatable.activatablestrategies.ActivatableStrategy;
import dungeonmania.dungeon.player.Player;
import dungeonmania.util.Position;

public class Bomb extends CollectableEntity implements Usable, Tickable {
    public static final String TYPE = "bomb";
    private ActivatableStrategy explodeStrategy;
    private static int explosionRadius;

    /**
     * Constructor for new entity where you want to generate new Id and
     * increment the number of entities. Use this when creating brand new entity.
     * @param type
     * @param isInteractable
     * @param position
     */
    public Bomb(Dungeon dungeon, Position position, ActivatableStrategy explodeStrategy) {
        super(dungeon, TYPE, position);
        this.explodeStrategy = explodeStrategy;
    }

    /**
     * Copy constructor for Bomb.
     */
    private Bomb(Dungeon newDungeon, Bomb old) {
        super(newDungeon, old);
        this.explodeStrategy = old.explodeStrategy;
    }

    public static void configure(JSONObject config) {
        explosionRadius = config.getInt("bomb_radius");
    }

    @Override
    public PhysicalEntity getCopy(Dungeon newDungeon) {
        return new Bomb(newDungeon, this);
    }

    @Override
    public void use(Player player) {
        move(new Position(player.getPosition()));
        dungeon.addEntity(this);
        player.consume(this);
    }

    @Override
    public void tick() {
        Map<Position, Boolean> adjacent = new HashMap<>();
        getPosition().getCardinallyAdjacentPositions().forEach(p -> {
            dungeon.getEntitiesAtPosition(p).stream()
                   .filter(e -> e instanceof Activatable)
                   .map(e -> (Activatable) e)
                   .forEach(a -> adjacent.put(a.getPosition(), a.isActive()));
        });

        if (explodeStrategy.isActive(dungeon, getPosition(), adjacent)) {
            explode();
        }
    }

    private void explode() {
        dungeon.getEntitiesInRange(getPosition(), explosionRadius)
               .stream()
               .forEach(e -> dungeon.removeEntity(e));
    }
}
