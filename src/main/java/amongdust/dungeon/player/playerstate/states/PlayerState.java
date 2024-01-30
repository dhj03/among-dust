package amongdust.dungeon.player.playerstate.states;

import java.io.Serializable;

import amongdust.dungeon.entities.physicalentities.movingentities.Assassin;
import amongdust.dungeon.entities.physicalentities.movingentities.Mercenary;
import amongdust.dungeon.entities.physicalentities.movingentities.ZombieToast;
import amongdust.dungeon.entities.physicalentities.movingentities.movementstrategies.MovementStrategy;

public interface PlayerState extends Serializable {
    public MovementStrategy getMovementStrategy(Assassin assassin);
    public MovementStrategy getMovementStrategy(Mercenary mercenary);
    public MovementStrategy getMovementStrategy(ZombieToast zombie);
}
