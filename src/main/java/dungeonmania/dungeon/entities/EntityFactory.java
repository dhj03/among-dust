package dungeonmania.dungeon.entities;

import org.json.JSONObject;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.dungeon.entities.buildableentities.Bow;
import dungeonmania.dungeon.entities.buildableentities.Buildable;
import dungeonmania.dungeon.entities.buildableentities.MidnightArmour;
import dungeonmania.dungeon.entities.buildableentities.Shield;
import dungeonmania.dungeon.entities.physicalentities.PhysicalEntity;
import dungeonmania.dungeon.entities.physicalentities.collectableentities.Arrow;
import dungeonmania.dungeon.entities.physicalentities.collectableentities.Bomb;
import dungeonmania.dungeon.entities.physicalentities.collectableentities.InvincibilityPotion;
import dungeonmania.dungeon.entities.physicalentities.collectableentities.InvisibilityPotion;
import dungeonmania.dungeon.entities.physicalentities.collectableentities.Key;
import dungeonmania.dungeon.entities.physicalentities.collectableentities.SunStone;
import dungeonmania.dungeon.entities.physicalentities.collectableentities.Sword;
import dungeonmania.dungeon.entities.physicalentities.collectableentities.Treasure;
import dungeonmania.dungeon.entities.physicalentities.collectableentities.Wood;
import dungeonmania.dungeon.entities.physicalentities.movingentities.Assassin;
import dungeonmania.dungeon.entities.physicalentities.movingentities.Hydra;
import dungeonmania.dungeon.entities.physicalentities.movingentities.Mercenary;
import dungeonmania.dungeon.entities.physicalentities.movingentities.Spider;
import dungeonmania.dungeon.entities.physicalentities.movingentities.ZombieToast;
import dungeonmania.dungeon.entities.physicalentities.staticentities.Boulder;
import dungeonmania.dungeon.entities.physicalentities.staticentities.Door;
import dungeonmania.dungeon.entities.physicalentities.staticentities.Exit;
import dungeonmania.dungeon.entities.physicalentities.staticentities.Portal;
import dungeonmania.dungeon.entities.physicalentities.staticentities.SwampTile;
import dungeonmania.dungeon.entities.physicalentities.staticentities.SwitchDoor;
import dungeonmania.dungeon.entities.physicalentities.staticentities.Wall;
import dungeonmania.dungeon.entities.physicalentities.staticentities.ZombieToastSpawner;
import dungeonmania.dungeon.entities.physicalentities.staticentities.activatable.FloorSwitch;
import dungeonmania.dungeon.entities.physicalentities.staticentities.activatable.LightBulb;
import dungeonmania.dungeon.entities.physicalentities.staticentities.activatable.Wire;
import dungeonmania.dungeon.entities.physicalentities.staticentities.activatable.activatablestrategies.ActivatableStrategy;
import dungeonmania.dungeon.entities.physicalentities.staticentities.activatable.activatablestrategies.LogicFactory;
import dungeonmania.dungeon.entities.physicalentities.staticentities.activatable.activatablestrategies.OrActivatable;
import dungeonmania.util.Position;

public class EntityFactory {
    public static PhysicalEntity createEntityFromJSON(Dungeon dungeon, JSONObject JSONEntity) {
        String type = JSONEntity.getString("type");
        Position pos = new Position(JSONEntity.getInt("x"), JSONEntity.getInt("y"));

        int keyNum;
        ActivatableStrategy logic;
        switch (type) {
            case Treasure.TYPE: // Collectable entities
                return new Treasure(dungeon, pos);
            case Key.TYPE:
                keyNum = JSONEntity.getInt("key");
                return new Key(dungeon, pos, keyNum);
            case SunStone.TYPE:
                return new SunStone(dungeon, pos);
            case InvincibilityPotion.TYPE:
                return new InvincibilityPotion(dungeon, pos);
            case InvisibilityPotion.TYPE:
                return new InvisibilityPotion(dungeon, pos);
            case Wood.TYPE:
                return new Wood(dungeon, pos);
            case Arrow.TYPE:
                return new Arrow(dungeon, pos);
            case Bomb.TYPE:
                logic = LogicFactory.createActivatableStrategy(JSONEntity);
                return new Bomb(dungeon, pos, logic);
            case Sword.TYPE:
                return new Sword(dungeon, pos);
            case Boulder.TYPE: // Static entities
                return new Boulder(dungeon, pos);
            case Door.TYPE:
                keyNum = JSONEntity.getInt("key");
                return new Door(dungeon, pos, keyNum);
            case SwitchDoor.TYPE:
                keyNum = JSONEntity.getInt("key");
                logic = LogicFactory.createActivatableStrategy(JSONEntity);
                return new SwitchDoor(dungeon, pos, keyNum, logic);
            case Exit.TYPE:
                return new Exit(dungeon, pos);
            case FloorSwitch.TYPE:
                logic = LogicFactory.createActivatableStrategy(JSONEntity);
                return new FloorSwitch(dungeon, pos, logic);
            case Wire.TYPE:
                return new Wire(dungeon, pos, new OrActivatable());
            case Portal.TYPE:
                String colour = JSONEntity.getString("colour");
                return new Portal(dungeon, pos, colour);
            case Wall.TYPE:
                return new Wall(dungeon, pos);
            case ZombieToastSpawner.TYPE:
                return new ZombieToastSpawner(dungeon, pos);
            case Spider.TYPE:
                return new Spider(dungeon, pos);
            case Mercenary.TYPE:
                return new Mercenary(dungeon, pos);
            case Assassin.TYPE:
                return new Assassin(dungeon, pos);
            case ZombieToast.TYPE:
                return new ZombieToast(dungeon, pos);
            case LightBulb.TYPE:
                logic = LogicFactory.createActivatableStrategy(JSONEntity);
                return new LightBulb(dungeon, pos, logic);
            case Hydra.TYPE:
                return new Hydra(dungeon, pos);
            case SwampTile.TYPE:
                int movementFactor = JSONEntity.getInt("movement_factor");
                return new SwampTile(dungeon, pos, movementFactor);
            default:
                throw new IllegalArgumentException("Tried to build PhysicalEntity with invalid type " + type);
        }
    }

    public static Buildable createBuildableFromType(Dungeon dungeon, String type) {
        switch (type) {
            case Shield.TYPE:
                return new Shield(dungeon);
            case Bow.TYPE:
                return new Bow(dungeon);
            case MidnightArmour.TYPE:
                return new MidnightArmour(dungeon);
            default:
                throw new IllegalArgumentException("Tried to build Buildable with invalid type " + type);
        }
    }
}
