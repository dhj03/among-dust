package dungeonmania.dungeon.player.playerstate.states;

import dungeonmania.dungeon.entities.physicalentities.movingentities.Ally;
import dungeonmania.dungeon.entities.physicalentities.movingentities.Assassin;
import dungeonmania.dungeon.entities.physicalentities.movingentities.Mercenary;
import dungeonmania.dungeon.entities.physicalentities.movingentities.ZombieToast;
import dungeonmania.dungeon.entities.physicalentities.movingentities.movementstrategies.FollowPlayerMovementStrategy;
import dungeonmania.dungeon.entities.physicalentities.movingentities.movementstrategies.MovementStrategy;
import dungeonmania.dungeon.entities.physicalentities.movingentities.movementstrategies.TargetPlayerMovementStrategy;
import dungeonmania.dungeon.entities.physicalentities.movingentities.movementstrategies.WanderMovementStrategy;

public class NormalPlayerState implements PlayerState {
    @Override
    public MovementStrategy getMovementStrategy(Mercenary mercenary) {
        return getBribedStrategy(mercenary);
    }

    @Override
    public MovementStrategy getMovementStrategy(ZombieToast zombie) {
        return new WanderMovementStrategy();
    }

    @Override
    public MovementStrategy getMovementStrategy(Assassin assassin) {
        return getBribedStrategy(assassin);
    }

    private MovementStrategy getBribedStrategy(Ally ally) {
        return ally.isBribed() ? new FollowPlayerMovementStrategy() : new TargetPlayerMovementStrategy();
    }
}
