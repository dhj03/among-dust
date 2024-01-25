package dungeonmania.dungeon.player;

import dungeonmania.dungeon.entities.physicalentities.Physical;
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
import dungeonmania.exceptions.InvalidMovementException;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public interface Visitor extends Physical {
    public void move(Position position);

    public void visit(CollectableEntity collectable, Direction moveDirection) throws InvalidMovementException;
    public void visit(Boulder boulder, Direction moveDirection) throws InvalidMovementException;
    public void visit(Door door, Direction moveDirection) throws InvalidMovementException;
    public void visit(Exit exit, Direction moveDirection) throws InvalidMovementException;
    public void visit(Activatable activatable, Direction moveDirection) throws InvalidMovementException;
    public void visit(Portal portal, Direction moveDirection) throws InvalidMovementException;
    public void visit(Wall wall, Direction moveDirection) throws InvalidMovementException;
    public void visit(ZombieToastSpawner spawner, Direction moveDirection) throws InvalidMovementException;
    public void visit(Spider spider, Direction moveDirection) throws InvalidMovementException;
    public void visit(ZombieToast zombieToast, Direction moveDirection) throws InvalidMovementException;
    public void visit(Ally ally, Direction moveDirection) throws InvalidMovementException;
    public void visit(SwampTile swampTile, Direction moveDirection) throws InvalidMovementException;
}
