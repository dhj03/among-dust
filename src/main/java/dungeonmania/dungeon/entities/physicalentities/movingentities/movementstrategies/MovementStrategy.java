package dungeonmania.dungeon.entities.physicalentities.movingentities.movementstrategies;

import java.io.Serializable;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.dungeon.entities.physicalentities.movingentities.MovingEntity;

public interface MovementStrategy extends Serializable{
    public void move(Dungeon dungeon, MovingEntity movingEntity);
}
