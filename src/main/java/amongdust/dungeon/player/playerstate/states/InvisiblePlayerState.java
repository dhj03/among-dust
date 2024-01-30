package amongdust.dungeon.player.playerstate.states;

import amongdust.dungeon.entities.Entity;
import amongdust.dungeon.entities.physicalentities.movingentities.Assassin;
import amongdust.dungeon.entities.physicalentities.movingentities.Mercenary;
import amongdust.dungeon.entities.physicalentities.movingentities.ZombieToast;
import amongdust.dungeon.entities.physicalentities.movingentities.movementstrategies.MovementStrategy;
import amongdust.dungeon.entities.physicalentities.movingentities.movementstrategies.WanderMovementStrategy;

public class InvisiblePlayerState extends TemporaryPlayerState {
    public InvisiblePlayerState(Entity item, int duration) {
        super(item, duration);
    }

    @Override
    public MovementStrategy getMovementStrategy(Mercenary mercenary) {
        return new WanderMovementStrategy();
    }

    @Override
    public MovementStrategy getMovementStrategy(ZombieToast zombie) {
        return new WanderMovementStrategy();
    }

    @Override
    public MovementStrategy getMovementStrategy(Assassin assassin) {
        if (assassin.playerInReconRange()) {
            NormalPlayerState normal = new NormalPlayerState();
            return normal.getMovementStrategy(assassin);
        }
        return new WanderMovementStrategy();
    }
}
