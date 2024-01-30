package amongdust.dungeon.player.playerstate.states;

import amongdust.dungeon.entities.physicalentities.movingentities.Ally;
import amongdust.dungeon.entities.physicalentities.movingentities.Assassin;
import amongdust.dungeon.entities.physicalentities.movingentities.Mercenary;
import amongdust.dungeon.entities.physicalentities.movingentities.ZombieToast;
import amongdust.dungeon.entities.physicalentities.movingentities.movementstrategies.FollowPlayerMovementStrategy;
import amongdust.dungeon.entities.physicalentities.movingentities.movementstrategies.MovementStrategy;
import amongdust.dungeon.entities.physicalentities.movingentities.movementstrategies.TargetPlayerMovementStrategy;
import amongdust.dungeon.entities.physicalentities.movingentities.movementstrategies.WanderMovementStrategy;

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
