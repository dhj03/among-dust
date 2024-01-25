package dungeonmania.dungeon.player.playerstate.states;

import java.io.Serializable;

import dungeonmania.dungeon.entities.physicalentities.movingentities.Assassin;
import dungeonmania.dungeon.entities.physicalentities.movingentities.Mercenary;
import dungeonmania.dungeon.entities.physicalentities.movingentities.ZombieToast;
import dungeonmania.dungeon.entities.physicalentities.movingentities.movementstrategies.MovementStrategy;

public interface PlayerState extends Serializable {
    public MovementStrategy getMovementStrategy(Assassin assassin);
    public MovementStrategy getMovementStrategy(Mercenary mercenary);
    public MovementStrategy getMovementStrategy(ZombieToast zombie);
}
