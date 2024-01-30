package amongdust.dungeon.entities.physicalentities.collisionstrategies;

import amongdust.dungeon.Dungeon;
import amongdust.dungeon.entities.physicalentities.movingentities.Ally;
import amongdust.dungeon.entities.physicalentities.movingentities.Spider;
import amongdust.dungeon.entities.physicalentities.movingentities.ZombieToast;
import amongdust.dungeon.entities.physicalentities.staticentities.Boulder;
import amongdust.dungeon.entities.physicalentities.staticentities.Door;
import amongdust.dungeon.entities.physicalentities.staticentities.Exit;
import amongdust.dungeon.entities.physicalentities.staticentities.Portal;
import amongdust.dungeon.entities.physicalentities.staticentities.Wall;
import amongdust.dungeon.entities.physicalentities.staticentities.ZombieToastSpawner;
import amongdust.dungeon.player.Visitor;
import amongdust.exceptions.InvalidMovementException;
import amongdust.util.Direction;

public class BoulderCollisionStrategy extends CollisionStrategy {

    @Override
    public void moveTo(Visitor visitor, Dungeon dungeon, Boulder boulder, Direction moveDirection)
            throws InvalidMovementException {
        throw new InvalidMovementException("Boulder can't push another boulder");
    }

    @Override
    public void moveTo(Visitor visitor, Dungeon dungeon, Door door, Direction moveDirection)
            throws InvalidMovementException {
        throw new InvalidMovementException("Undefined behaviour");
    }

    @Override
    public void moveTo(Visitor visitor, Dungeon dungeon, Exit exit, Direction moveDirection)
            throws InvalidMovementException {
        throw new InvalidMovementException("Undefined behaviour");
    }

    @Override
    public void moveTo(Visitor visitor, Dungeon dungeon, Portal portal, Direction moveDirection)
            throws InvalidMovementException {
        throw new InvalidMovementException("Undefined behaviour");
    }

    @Override
    public void moveTo(Visitor visitor, Dungeon dungeon, Wall wall, Direction moveDirection)
            throws InvalidMovementException {
        throw new InvalidMovementException("Boulder can't go onto wall");
    }

    @Override
    public void moveTo(Visitor visitor, Dungeon dungeon, ZombieToastSpawner spawner, Direction moveDirection)
            throws InvalidMovementException {
        throw new InvalidMovementException("Undefined behaviour");
    }

    @Override
    public void moveTo(Visitor visitor, Dungeon dungeon, Spider spider, Direction moveDirection)
            throws InvalidMovementException {
        throw new InvalidMovementException("Undefined behaviour");
    }

    @Override
    public void moveTo(Visitor visitor, Dungeon dungeon, ZombieToast zombieToast, Direction moveDirection)
            throws InvalidMovementException {
        throw new InvalidMovementException("Undefined behaviour");
    }

    @Override
    public void moveTo(Visitor visitor, Dungeon dungeon, Ally ally, Direction moveDirection)
            throws InvalidMovementException {
        throw new InvalidMovementException("Undefined behaviour");
    }
}
