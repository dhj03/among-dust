package dungeonmania;

import static dungeonmania.TestUtils.compareDungeons;
import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getPlayerPosition;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class PersistenceTests {

    // Movement Paths for Testing
    public static String[] playerDungeons() {
        return new String[] {
            "d_basicPlayerTest00",
            "d_basicPlayerTest57",
            "d_doorTest",
            "d_basicPotionTest",
            "d_bombComplexTest"
        };
    }

    @ParameterizedTest
    @MethodSource(value = "playerDungeons")
    @DisplayName("Test Savegame Entity Number/Type/Position Persistence")
    public void testPersistenceEntity(String dungeonName) {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse drBefore = dmc.newGame(dungeonName, "c_movementTest_testMovementDown");

        int noSaves = dmc.allGames().size();

        // Save game
        dmc.saveGame(dungeonName);

        // Change the state of the dungeon a bit
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);

        // Load game
        DungeonResponse drAfter = dmc.loadGame(dungeonName);

        // Compare two dungeonresponses
        compareDungeons(drBefore, drAfter);

        // Test to see one more savegame has been completed
        assertEquals(dmc.allGames().size(), noSaves + 1);

        // Remove saves
        saveGameCleanup(dungeonName);
    }


    @Test
    @DisplayName("Test Savegame Mercenary Bribe Persistence")
    public void testPersistenceMercBribe() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse drInit = dmc.newGame("d_mercTest_PlayerDiagonal1", "c_mercTest_bribe0rad1");

        List<EntityResponse> mercenaries = getEntities(drInit, "mercenary");
        String mercId = mercenaries.get(0).getId();
        // Try to bribe
        assertDoesNotThrow(() -> dmc.interact(mercId));

        // Save game
        dmc.saveGame("mercDungeon");

        // Load game
        DungeonResponse dr = dmc.loadGame("mercDungeon");

        // Check whether the merc follows the player's old position
        for (int i = 0; i < 10; i++) {
            dr = dmc.tick(Direction.RIGHT);
        }
        mercenaries = getEntities(dr, "mercenary");
        Position finalMercPosition = mercenaries.get(0).getPosition();

        Position finalPlayerPosition = getPlayerPosition(dr);

        // Ensure the merc is now at most 3 blocks away from player, meaning it was led away
        assertTrue(finalMercPosition.getDistanceTo(finalPlayerPosition) <= 3);

        saveGameCleanup("mercDungeon");
    }

    @Test
    @DisplayName("Test Savegame Door Status Persistence")
    public void testPersistenceDoorStatus() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_doorTest", "c_movementTest_testMovementDown");

        // Pick up the correct key and open
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);

        DungeonResponse dr = dmc.getDungeonResponseModel();

        // Make sure the player is in the door
        Position playerPosition = TestUtils.getPlayerPosition(dr);
        assertEquals(new Position(-2, 0), playerPosition);


        dmc.saveGame("savegame");

        DungeonResponse drNew = dmc.loadGame("savegame");

        // Make sure that if I step back off, I can step back onto the door
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.LEFT);
        drNew = dmc.getDungeonResponseModel();
        playerPosition = TestUtils.getPlayerPosition(drNew);
        assertEquals(new Position(-2, 0), playerPosition);

        saveGameCleanup("savegame");
    }

    @Test
    @DisplayName("Test Savegame Portal Persistence - Multiple Portals")
    public void testPersistencePortalStatus() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_portalsAdvanced", "c_movementTest_testMovementDown");

        dmc.saveGame("portal_test");

        // Create new game to wipe dungeon
        dmc.newGame("d_doorTest", "c_movementTest_testMovementDown");

        dmc.loadGame("portal_test");

        // Step onto the red portal, expect to go through red and grey and be to the right of the other grey portal
        dmc.tick(Direction.RIGHT);

        DungeonResponse dr = dmc.getDungeonResponseModel();
        Position playerPosition = TestUtils.getPlayerPosition(dr);
        assertEquals(new Position(6, 1), playerPosition);

        saveGameCleanup("portal_test");
    }

    @Test
    @DisplayName("Test Savegame Player Inventory - Picking Up of Items")
    public void testPersistencePlayerInventory() {

        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_basicCollectiblePickupTest", "c_movementTest_testMovementDown");

        for (int i = 0; i < 6; i++) {
            dmc.tick(Direction.RIGHT);
        }

        dmc.saveGame("inventory_test");

        // Create new game to wipe dungeon
        dmc.newGame("d_doorTest", "c_movementTest_testMovementDown");

        dmc.loadGame("inventory_test");

        List<ItemResponse> ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "arrow");
        assertEquals(2, ir.size());
        ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "wood");
        assertEquals(1, ir.size());
        ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "wood");
        assertEquals(1, ir.size());

        saveGameCleanup("inventory_test");
    }

    @Test
    @DisplayName("Test Savegame Player Potion Persistence - Zombie Toast Running Away")
    public void testPersistencePlayerPotion() {

        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_potionTestZombie", "c_potionTestConfigLength50");

        // Pick up both potions
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        // Use invincibility potion
        List<ItemResponse> ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "invincibility_potion");
        String potionId = ir.get(0).getId();
        assertDoesNotThrow(() -> dmc.tick(potionId));


        dmc.saveGame("potion_test");

        // Create new game to wipe dungeon
        dmc.newGame("d_doorTest", "c_movementTest_testMovementDown");

        dmc.loadGame("potion_test");


        DungeonResponse dr = dmc.getDungeonResponseModel();
        Position oldPosition = TestUtils.getEntities(dr, "zombie_toast").get(0).getPosition();
        for (int i = 0; i < 49; i++) {
            dmc.tick(Direction.RIGHT);

            dr = dmc.getDungeonResponseModel();
            Position newPosition = TestUtils.getEntities(dr, "zombie_toast").get(0).getPosition();

            assertEquals(new Position(-1, 0), Position.calculatePositionBetween(oldPosition, newPosition));
            oldPosition = newPosition;
        }

        saveGameCleanup("potion_test");

    }

    @Test
    @DisplayName("Persistence testing a map with 4 conjunction goal")
    public void testPersistenceGoal() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_complexGoalsTest_orAll", "c_complexGoalsTest_andAll");

        // ":exit OR :treasure OR :boulders OR :enemies"
        // All the goals are active
        assertTrue(TestUtils.getGoals(res).contains(":exit"));
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));
        assertTrue(TestUtils.getGoals(res).contains(":boulders"));
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // pick up coin
        res = dmc.tick(Direction.DOWN);

        dmc.saveGame("goal_test");

        // Create new game to wipe dungeon
        dmc.newGame("d_doorTest", "c_movementTest_testMovementDown");

        dmc.loadGame("goal_test");
        res = dmc.getDungeonResponseModel();

        assertEquals("", TestUtils.getGoals(res));

        saveGameCleanup("goal_test");
    }


    @Test
    @DisplayName("Test complex logic switch - persistence")
    public void testPersistenceLogicORSwitchComplex() {
        // x represents wire
        /*                         light_bulb
         *                             x
         * Player Boulder switch x and_switch x or_switch x switch boulder
         *
         */
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("d_logicTest_orSwitch", "c_bombTest_placeBombRadius2");

        // Make sure bulb turned off
        assertTrue(TestUtils.countEntityOfType(dr, "light_bulb_off") == 1);

        // Push boulder onto switch
        dr = dmc.tick(Direction.RIGHT);

        dmc.saveGame("logic_test");
        // Create new game to wipe dungeon
        dmc.newGame("d_doorTest", "c_movementTest_testMovementDown");
        dr = dmc.loadGame("logic_test");

        // Make sure bulb turned off still
        assertTrue(TestUtils.countEntityOfType(dr, "light_bulb_off") == 1);

        // Push second boulder
        dr = dmc.tick(Direction.UP);
        for (int i = 0; i < 9; i++) {
            dr = dmc.tick(Direction.RIGHT);
        }

        dmc.saveGame("logic_test");
        // Create new game to wipe dungeon
        dmc.newGame("d_doorTest", "c_movementTest_testMovementDown");
        dmc.loadGame("logic_test");

        dr = dmc.tick(Direction.DOWN);
        dr = dmc.tick(Direction.LEFT);

        // Light bulb should turn on
        assertTrue(TestUtils.countEntityOfType(dr, "light_bulb_on") == 1);

        saveGameCleanup("logic_test");
    }

    @Test
    @DisplayName("Test Multi Savegame - Inventory Test & Portal Test")
    public void testPersistenceMultiSavegame() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_basicCollectiblePickupTest", "c_movementTest_testMovementDown");

        for (int i = 0; i < 6; i++) {
            dmc.tick(Direction.RIGHT);
        }

        dmc.saveGame("inventory_test");

        // Save Dungeon with new portals
        dmc.newGame("d_portalsAdvanced", "c_movementTest_testMovementDown");

        dmc.saveGame("portal_test");

        // Load back original dungeon
        dmc.loadGame("inventory_test");
        // Test Inventory of saveGame 1
        List<ItemResponse> ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "arrow");
        assertEquals(2, ir.size());
        ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "wood");
        assertEquals(1, ir.size());
        ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "wood");
        assertEquals(1, ir.size());

        // Test the portal dungeon
        dmc.loadGame("portal_test");

        // Step onto the red portal, expect to go through red and grey and be to the right of the other grey portal
        dmc.tick(Direction.RIGHT);

        DungeonResponse dr = dmc.getDungeonResponseModel();
        Position playerPosition = TestUtils.getPlayerPosition(dr);
        assertEquals(new Position(6, 1), playerPosition);

        saveGameCleanup("inventory_test");
        saveGameCleanup("portal_test");
    }

    private void saveGameCleanup(String savegame) {
        File save = new File(System.getProperty("user.dir") + "/saves/" + savegame);
        save.delete();
    }

}
