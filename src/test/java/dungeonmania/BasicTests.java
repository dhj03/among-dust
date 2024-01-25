package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;

public class BasicTests {
    @Test
    @DisplayName("Test that a completely blank config file gives an empty response")
    public void testCompleteBasics() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse actual = dmc.newGame("d_completelyEmpty", "c_movementTest_testMovementDown");

        assertEquals(0, actual.getEntities().size());
        assertEquals(0, actual.getBattles().size());
        assertEquals(0, actual.getBuildables().size());
        assertEquals(0, actual.getAnimations().size());
        assertEquals(":exit", actual.getGoals());
        assertEquals("d_completelyEmpty", actual.getDungeonName());
        assertEquals(0, actual.getInventory().size());
    }

    @Test
    @DisplayName("Test that an invalid dungeon name throws an IllegalArgumentException")
    public void testInvalidDungeonName() {
        DungeonManiaController dmc = new DungeonManiaController();
        assertThrows(IllegalArgumentException.class, () -> dmc.newGame("this is a made up file name", "c_movementTest_testMovementDown"));
    }

    @Test
    @DisplayName("Test that an invalid config name throws an IllegalArgumentException")
    public void testInvalidConfigName() {
        DungeonManiaController dmc = new DungeonManiaController();
        assertThrows(IllegalArgumentException.class, () -> dmc.newGame("d_completelyEmpty", "this config is nonsense ajksjkndajknsjknd"));
    }
}
