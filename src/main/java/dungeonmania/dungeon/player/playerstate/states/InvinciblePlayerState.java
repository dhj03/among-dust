package dungeonmania.dungeon.player.playerstate.states;

import dungeonmania.dungeon.entities.Entity;
import dungeonmania.dungeon.entities.physicalentities.movingentities.Ally;
import dungeonmania.dungeon.entities.physicalentities.movingentities.Assassin;
import dungeonmania.dungeon.entities.physicalentities.movingentities.Mercenary;
import dungeonmania.dungeon.entities.physicalentities.movingentities.ZombieToast;
import dungeonmania.dungeon.entities.physicalentities.movingentities.movementstrategies.FollowPlayerMovementStrategy;
import dungeonmania.dungeon.entities.physicalentities.movingentities.movementstrategies.MovementStrategy;
import dungeonmania.dungeon.entities.physicalentities.movingentities.movementstrategies.RunawayFromPlayerMovementStrategy;

public class InvinciblePlayerState extends TemporaryPlayerState {
    public InvinciblePlayerState(Entity item, int duration) {
        super(item, duration);
    }

    @Override
    public MovementStrategy getMovementStrategy(Mercenary mercenary) {
        return getBribedStrategy(mercenary);
    }

    @Override
    public MovementStrategy getMovementStrategy(ZombieToast zombie) {
        return new RunawayFromPlayerMovementStrategy();
    }

    @Override
    public MovementStrategy getMovementStrategy(Assassin assassin) {
        return getBribedStrategy(assassin);
    }

    private MovementStrategy getBribedStrategy(Ally ally) {
        return ally.isBribed() ? new FollowPlayerMovementStrategy() : new RunawayFromPlayerMovementStrategy();
    }
}
