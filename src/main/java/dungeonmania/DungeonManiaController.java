package dungeonmania;

import java.util.List;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.exceptions.InvalidMovementException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;

public class DungeonManiaController {
    private Dungeon dungeon;
    private SaveGameManager saveGameManager = new SaveGameManager("/saves");

    public String getSkin() {
        return "default";
    }

    public String getLocalisation() {
        return "en_US";
    }

    /**
     * /dungeons
     */
    public static List<String> dungeons() {
        return FileLoader.listFileNamesInResourceDirectory("dungeons");
    }

    /**
     * /configs
     */
    public static List<String> configs() {
        return FileLoader.listFileNamesInResourceDirectory("configs");
    }

    /**
     * /game/new
     */
    public DungeonResponse newGame(String dungeonName, String configName) throws IllegalArgumentException {
        dungeon = new Dungeon(dungeonName, configName);
        return getDungeonResponseModel();
    }

    /**
     * /game/dungeonResponseModel
     */
    public DungeonResponse getDungeonResponseModel() {
        return dungeon.getResponse();
    }

    /**
     * /game/tick/item
     */
    public DungeonResponse tick(String itemUsedId) throws IllegalArgumentException, InvalidActionException {
        dungeon.useItem(itemUsedId);
        dungeon.tick();
        return getDungeonResponseModel();
    }

    /**
     * /game/tick/movement
     */
    public DungeonResponse tick(Direction movementDirection) {
        // Before trying anything, take a save
        Dungeon dungeonCopy = new Dungeon(dungeon);
        try {
            dungeon.movePlayer(movementDirection);
        } catch (InvalidMovementException e) {
            // There was some invalid movement rip
            dungeon = dungeonCopy;
        }

        dungeon.tick();
        return getDungeonResponseModel();
    }

    /**
     * /game/build
     */
    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {
        dungeon.build(buildable);
        return getDungeonResponseModel();
    }

    /**
     * /game/interact
     */
    public DungeonResponse interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        dungeon.interactWith(entityId);
        return getDungeonResponseModel();
    }

    /**
     * /game/save
     */
    public DungeonResponse saveGame(String name) throws IllegalArgumentException {
        saveGameManager.save(dungeon, name);
        return getDungeonResponseModel();
    }

    /**
     * /game/load
     */
    public DungeonResponse loadGame(String name) throws IllegalArgumentException {
        dungeon = saveGameManager.load(name);

        return getDungeonResponseModel();
    }

    /**
     * /games/all
     */
    public List<String> allGames() {
        return saveGameManager.getSaves();
    }

    /**
     * /game/new/generate
     */
    public DungeonResponse generateDungeon(int xStart, int yStart, int xEnd, int yEnd, String configName) {
        dungeon = new Dungeon(configName, new Position(xStart, yStart), new Position(xEnd, yEnd));
        return dungeon.getResponse();
    }

}
