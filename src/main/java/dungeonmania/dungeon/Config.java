package dungeonmania.dungeon;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import dungeonmania.dungeon.goals.goaltypes.EnemyGoal;
import dungeonmania.dungeon.goals.goaltypes.TreasureGoal;
import dungeonmania.dungeon.entities.buildableentities.Bow;
import dungeonmania.dungeon.entities.buildableentities.MidnightArmour;
import dungeonmania.dungeon.entities.buildableentities.Shield;
import dungeonmania.dungeon.entities.physicalentities.collectableentities.Bomb;
import dungeonmania.dungeon.entities.physicalentities.collectableentities.InvincibilityPotion;
import dungeonmania.dungeon.entities.physicalentities.collectableentities.InvisibilityPotion;
import dungeonmania.dungeon.entities.physicalentities.collectableentities.Sword;
import dungeonmania.dungeon.entities.physicalentities.movingentities.Ally;
import dungeonmania.dungeon.entities.physicalentities.movingentities.Assassin;
import dungeonmania.dungeon.entities.physicalentities.movingentities.Hydra;
import dungeonmania.dungeon.entities.physicalentities.movingentities.Mercenary;
import dungeonmania.dungeon.entities.physicalentities.movingentities.Spider;
import dungeonmania.dungeon.entities.physicalentities.movingentities.ZombieToast;
import dungeonmania.dungeon.entities.physicalentities.staticentities.ZombieToastSpawner;
import dungeonmania.dungeon.player.Player;
import dungeonmania.util.FileLoader;

public class Config {
    public static void configure(String configName) throws IllegalArgumentException {
        // Load the JSON object
        JSONObject config;
        try {
            // Get the dungeon config
            String fileContents = FileLoader.loadResourceFile("/configs/" + configName + ".json");
            config = new JSONObject(fileContents);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Dungeon file doesn't exist");
        }

        // Pass the config into each class that cares about it
        Ally.configure(config);
        Assassin.configure(config);
        Mercenary.configure(config);
        Bomb.configure(config);
        Bow.configure(config);
        Player.configure(config);
        Hydra.configure(config);
        InvincibilityPotion.configure(config);
        InvisibilityPotion.configure(config);
        MidnightArmour.configure(config);
        Shield.configure(config);
        Spider.configure(config);
        Sword.configure(config);
        TreasureGoal.configure(config);
        EnemyGoal.configure(config);
        ZombieToast.configure(config);
        ZombieToastSpawner.configure(config);
    }

    public static double getOptionalDouble(JSONObject config, String field) {
        try {
            return config.getDouble(field);
        } catch (JSONException e) {
            return 0; // Default value (as it wasn't found)
        }
    }

    public static int getOptionalInt(JSONObject config, String field) {
        return (int) getOptionalDouble(config, field);
    }
}
