package amongdust.dungeon.entities.physicalentities.movingentities.movementstrategies;

import java.io.Serializable;

import amongdust.dungeon.Dungeon;
import amongdust.dungeon.entities.physicalentities.movingentities.MovingEntity;

public interface MovementStrategy extends Serializable{
    public void move(Dungeon dungeon, MovingEntity movingEntity);
}
