package dungeonmania;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dungeonmania.dungeon.Config;
import dungeonmania.dungeon.Dungeon;

public class SaveGameManager {
    private final String DIRECTORY;

    public SaveGameManager(String directory) {
        this.DIRECTORY =  System.getProperty("user.dir") + directory;
        // Creates directory if doesn't exist
        createDirectory(this.DIRECTORY);
    }


    public Dungeon load(String saveName) throws IllegalArgumentException {
        Dungeon savedDungeon;
        try {   
            FileInputStream file = new FileInputStream(DIRECTORY + '/' + saveName);
            ObjectInputStream in = new ObjectInputStream(file);
              
            // Deserialise the dungeon
            savedDungeon = (Dungeon) in.readObject();
              
            in.close();
            file.close();
    
        } catch (Exception e) {
            throw new IllegalArgumentException("Error loading savegame: " + e);
        }

        Config.configure(savedDungeon.getConfigName());
        return savedDungeon;
    }

    public void save(Dungeon dungeon, String saveName) {
        try {
            // Creates directory if doesn't exist
            createDirectory(DIRECTORY);

            // Creates new file at location, if doesn't already exist
            File saveFile = new File(DIRECTORY + '/' + saveName);
            saveFile.createNewFile();

            FileOutputStream file = new FileOutputStream(DIRECTORY + '/' + saveName);
            ObjectOutputStream out = new ObjectOutputStream(file);

            out.writeObject(dungeon);
            out.close();
            file.close();

        } catch(Exception e) {
            System.out.println(e);
        }
    }

    public List<String> getSaves() {
        File saveFolder = new File(DIRECTORY);

        // If directory is empty
        if (DIRECTORY.length() == 0) return new ArrayList<String>();
        
        return Arrays.asList(saveFolder.list());
    }

    private static void createDirectory(String directory) {
        try {
            // Creates directory if doesn't exist
            Path path = Paths.get(directory);
            if (!Files.exists(path)) Files.createDirectory(path);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
}
