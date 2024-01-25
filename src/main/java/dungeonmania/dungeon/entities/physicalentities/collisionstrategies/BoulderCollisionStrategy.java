package dungeonmania.dungeon.entities.physicalentities.collisionstrategies;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.dungeon.entities.physicalentities.movingentities.Ally;
import dungeonmania.dungeon.entities.physicalentities.movingentities.Spider;
import dungeonmania.dungeon.entities.physicalentities.movingentities.ZombieToast;
import dungeonmania.dungeon.entities.physicalentities.staticentities.Boulder;
import dungeonmania.dungeon.entities.physicalentities.staticentities.Door;
import dungeonmania.dungeon.entities.physicalentities.staticentities.Exit;
import dungeonmania.dungeon.entities.physicalentities.staticentities.Portal;
import dungeonmania.dungeon.entities.physicalentities.staticentities.Wall;
import dungeonmania.dungeon.entities.physicalentities.staticentities.ZombieToastSpawner;
import dungeonmania.dungeon.player.Visitor;
import dungeonmania.exceptions.InvalidMovementException;
import dungeonmania.util.Direction;

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
