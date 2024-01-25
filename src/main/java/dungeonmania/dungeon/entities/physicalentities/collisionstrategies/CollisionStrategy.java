package dungeonmania.dungeon.entities.physicalentities.collisionstrategies;

import java.io.Serializable;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.dungeon.entities.physicalentities.collectableentities.CollectableEntity;
import dungeonmania.dungeon.entities.physicalentities.movingentities.Ally;
import dungeonmania.dungeon.entities.physicalentities.movingentities.Spider;
import dungeonmania.dungeon.entities.physicalentities.movingentities.ZombieToast;
import dungeonmania.dungeon.entities.physicalentities.staticentities.Boulder;
import dungeonmania.dungeon.entities.physicalentities.staticentities.Door;
import dungeonmania.dungeon.entities.physicalentities.staticentities.Exit;
import dungeonmania.dungeon.entities.physicalentities.staticentities.Portal;
import dungeonmania.dungeon.entities.physicalentities.staticentities.SwampTile;
import dungeonmania.dungeon.entities.physicalentities.staticentities.Wall;
import dungeonmania.dungeon.entities.physicalentities.staticentities.ZombieToastSpawner;
import dungeonmania.dungeon.entities.physicalentities.staticentities.activatable.Activatable;
import dungeonmania.dungeon.player.Visitor;
import dungeonmania.exceptions.InvalidMovementException;
import dungeonmania.util.Direction;

public abstract class CollisionStrategy implements Serializable {
    public void moveTo(Visitor visitor, Dungeon dungeon, CollectableEntity collectable, Direction moveDirection) throws InvalidMovementException {
        visitor.move(collectable.getPosition());
    }
    public void moveTo(Visitor visitor, Dungeon dungeon, Boulder boulder, Direction moveDirection) throws InvalidMovementException {
        visitor.move(boulder.getPosition());
    }
    public void moveTo(Visitor visitor, Dungeon dungeon, Door door, Direction moveDirection) throws InvalidMovementException {
        visitor.move(door.getPosition());
    }
    public void moveTo(Visitor visitor, Dungeon dungeon, Exit exit, Direction moveDirection) throws InvalidMovementException {
        visitor.move(exit.getPosition());
    }
    public void moveTo(Visitor visitor, Dungeon dungeon, Activatable activatable, Direction moveDirection) throws InvalidMovementException {
        visitor.move(activatable.getPosition());
    }
    public void moveTo(Visitor visitor, Dungeon dungeon, Portal portal, Direction moveDirection) throws InvalidMovementException {
        visitor.move(portal.getPosition());
    }
    public void moveTo(Visitor visitor, Dungeon dungeon, Wall wall, Direction moveDirection) throws InvalidMovementException {
        visitor.move(wall.getPosition());
    }
    public void moveTo(Visitor visitor, Dungeon dungeon, ZombieToastSpawner spawner, Direction moveDirection) throws InvalidMovementException {
        visitor.move(spawner.getPosition());
    }
    public void moveTo(Visitor visitor, Dungeon dungeon, Spider spider, Direction moveDirection) throws InvalidMovementException {
        visitor.move(spider.getPosition());
    }
    public void moveTo(Visitor visitor, Dungeon dungeon, ZombieToast zombieToast, Direction moveDirection) throws InvalidMovementException {
        visitor.move(zombieToast.getPosition());
    }
    public void moveTo(Visitor visitor, Dungeon dungeon, Ally ally, Direction moveDirection) throws InvalidMovementException {
        visitor.move(ally.getPosition());
    }
    public void moveTo(Visitor visitor, Dungeon dungeon, SwampTile swampTile, Direction moveDirection) throws InvalidMovementException {
        visitor.move(swampTile.getPosition());
    }
}
