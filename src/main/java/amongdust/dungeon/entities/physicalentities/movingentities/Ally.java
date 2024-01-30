package amongdust.dungeon.entities.physicalentities.movingentities;

import org.json.JSONObject;

import amongdust.dungeon.Dungeon;
import amongdust.dungeon.battle.BattleModifier;
import amongdust.dungeon.battle.Stats;
import amongdust.dungeon.entities.Interactable;
import amongdust.dungeon.entities.physicalentities.collectableentities.Treasure;
import amongdust.dungeon.entities.physicalentities.collisionstrategies.AllyCollisionStrategy;
import amongdust.dungeon.entities.physicalentities.movingentities.movementstrategies.MovementStrategy;
import amongdust.dungeon.player.Player;
import amongdust.exceptions.InvalidActionException;
import amongdust.util.Position;

public abstract class Ally extends MovingEntity implements Interactable {
    private static int bribeRadius;
    private static int allyAttack;
    private static int allyDefence;

    public Ally(Dungeon dungeon, String type, Position position, MovementStrategy movementStrategy, Stats stats) {
        super(dungeon, type, true, position, movementStrategy, new AllyCollisionStrategy(), stats);
    }

    public Ally(Dungeon newDungeon, Ally old) {
        super(newDungeon, old);
    }

    public static void configure(JSONObject config) {
        bribeRadius = config.getInt("bribe_radius");
        allyAttack = config.getInt("ally_attack");
        allyDefence = config.getInt("ally_defence");
    }

    // Adds this allies contribution to the battle modifiers
    public void addModifications(BattleModifier modifier) {
        modifier.addAttackAdditive(isBribed() ? allyAttack : 0);
        modifier.addDefenceAdditive(isBribed() ? allyDefence : 0);
    }

    public boolean isBribed() {
        return !super.isInteractable;
    }

    @Override
    public void interact(Player player) throws InvalidActionException {
        // Check to see whether the player is within bribe radius
        if (!dungeon.getEntitiesInRange(player.getPosition(), bribeRadius)
                    .stream()
                    .anyMatch(e -> e.equals(this))) {

            throw new InvalidActionException("Ally not in range of player");
        }

        if (player.getNumInInventory(Treasure.TYPE) < getBribeAmount()) {
            throw new InvalidActionException("Not enough treasure");
        }

        if (bribeSuccessful()) {
            // Check to see whether player has enough gold, then remove it
            player.removeItemIfFound(Treasure.TYPE, getBribeAmount());

            // Set ally to bribed
            super.isInteractable = false;
        }
    }

    public abstract int getBribeAmount();
    public abstract boolean bribeSuccessful();
}
