package amongdust.dungeon.player;

import amongdust.dungeon.entities.physicalentities.Physical;
import amongdust.dungeon.entities.physicalentities.collectableentities.CollectableEntity;
import amongdust.dungeon.entities.physicalentities.movingentities.Ally;
import amongdust.dungeon.entities.physicalentities.movingentities.Spider;
import amongdust.dungeon.entities.physicalentities.movingentities.ZombieToast;
import amongdust.dungeon.entities.physicalentities.staticentities.Boulder;
import amongdust.dungeon.entities.physicalentities.staticentities.Door;
import amongdust.dungeon.entities.physicalentities.staticentities.Exit;
import amongdust.dungeon.entities.physicalentities.staticentities.Portal;
import amongdust.dungeon.entities.physicalentities.staticentities.SwampTile;
import amongdust.dungeon.entities.physicalentities.staticentities.Wall;
import amongdust.dungeon.entities.physicalentities.staticentities.ZombieToastSpawner;
import amongdust.dungeon.entities.physicalentities.staticentities.activatable.Activatable;
import amongdust.exceptions.InvalidMovementException;
import amongdust.util.Direction;
import amongdust.util.Position;

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
