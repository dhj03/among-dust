package amongdust;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import amongdust.exceptions.InvalidActionException;
import amongdust.response.models.DungeonResponse;
import amongdust.response.models.ItemResponse;
import amongdust.util.Direction;

public class BuildableTests {

    @Test
    @DisplayName("Test Player can't craft with no materials")
    public void testCraftingInventoryEmpty() {
        AmongDustController dmc = new AmongDustController();
        dmc.newGame("d_basicCraftableTest", "c_movementTest_testMovementDown");

        List<String> buildables = TestUtils.getBuildables(dmc.getDungeonResponseModel());
        assertEquals(0, buildables.size());

        assertThrows(InvalidActionException.class, () -> dmc.build("shield"));
    }

    @Test
    @DisplayName("Test Player can't craft invalid buildable")
    public void testCraftingInvalidBuildable() {
        AmongDustController dmc = new AmongDustController();
        dmc.newGame("d_basicCraftableTest", "c_movementTest_testMovementDown");

        // You cannot build a sussy baka
        assertThrows(IllegalArgumentException.class, () -> dmc.build("sussybaka"));
    }

    @Test
    @DisplayName("Test Player can build bow")
    public void testCanCraftBow() {
        AmongDustController dmc = new AmongDustController();
        dmc.newGame("d_basicCraftableTest", "c_movementTest_testMovementDown");

        for (int i = 0; i < 4; i++) {
            dmc.tick(Direction.RIGHT);
        }

        List<String> buildables = TestUtils.getBuildables(dmc.getDungeonResponseModel());
        assertEquals(1, buildables.size());
        assertTrue(buildables.get(0).equals("bow"));
        
        assertDoesNotThrow(() -> dmc.build("bow"));
        List<ItemResponse> ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "bow");
        assertEquals(1, ir.size());
    }

    @Test
    @DisplayName("Test Player can build shield with treasure")
    public void testCanCraftShieldWithTreasure() {
        AmongDustController dmc = new AmongDustController();
        dmc.newGame("d_basicCraftableTest", "c_movementTest_testMovementDown");

        for (int i = 0; i < 4; i++) {
            dmc.tick(Direction.RIGHT);
        }
        dmc.tick(Direction.DOWN);

        List<String> buildables = TestUtils.getBuildables(dmc.getDungeonResponseModel());
        assertEquals(2, buildables.size());

        assertEquals(1, buildables.stream()
                                  .filter(buildable -> buildable.equals("shield"))
                                  .collect(Collectors.toList()).size());

        assertDoesNotThrow(() -> dmc.build("shield"));
        List<ItemResponse> ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "shield");
        assertEquals(1, ir.size());
    }

    @Test
    @DisplayName("Test Player can build shield with key")
    public void testCanCraftShieldWithKey() {
        AmongDustController dmc = new AmongDustController();
        dmc.newGame("d_basicCraftableTest", "c_movementTest_testMovementDown");

        for (int i = 0; i < 4; i++) {
            dmc.tick(Direction.RIGHT);
        }
        dmc.tick(Direction.UP);

        List<String> buildables = TestUtils.getBuildables(dmc.getDungeonResponseModel());
        assertEquals(2, buildables.size());

        assertEquals(1, buildables.stream()
                                  .filter(buildable -> buildable.equals("shield"))
                                  .collect(Collectors.toList()).size());

        assertDoesNotThrow(() -> dmc.build("shield"));
        List<ItemResponse> ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "shield");
        assertEquals(1, ir.size());
    }

    @Test
    @DisplayName("Test sun stone building shield")
    public void testSunStoneBuildingShield() {
        AmongDustController dmc = new AmongDustController();
        dmc.newGame("d_testSunStone", "c_sunStoneTests_treasureGoal2");
        
        // Pick up sun stone and 2 wood
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        // Try and craft
        assertDoesNotThrow(() -> dmc.build("shield"));
        DungeonResponse dr = dmc.getDungeonResponseModel();
        assertTrue(TestUtils.getInventory(dr, "shield").size() == 1);
        assertTrue(TestUtils.getInventory(dr, "wood").size() == 0);
        assertTrue(TestUtils.getInventory(dr, "sun_stone").size() == 1);
    }

    @Test
    @DisplayName("Test Player can build midnight armour")
    public void testCanCraftMidnightArmour() {
        AmongDustController dmc = new AmongDustController();
        dmc.newGame("d_buildableTest_midnight_armour", "c_movementTest_testMovementDown");

        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        List<String> buildables = TestUtils.getBuildables(dmc.getDungeonResponseModel());
        assertEquals(1, buildables.size());

        assertEquals(1, buildables.stream()
                                  .filter(buildable -> buildable.equals("midnight_armour"))
                                  .collect(Collectors.toList()).size());

        assertDoesNotThrow(() -> dmc.build("midnight_armour"));
        List<ItemResponse> ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "midnight_armour");
        assertEquals(1, ir.size());
        ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "sword");
        assertEquals(0, ir.size());
        ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "sun_stone");
        assertEquals(0, ir.size());
    }

    @Test
    @DisplayName("Test Player cannot build midnight armour due to zombie")
    public void testCannotCraftMidnightArmourWithZombie() {
        AmongDustController dmc = new AmongDustController();
        dmc.newGame("d_buildableTest_midnight_armour_zombie", "c_movementTest_testMovementDown");

        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        List<String> buildables = TestUtils.getBuildables(dmc.getDungeonResponseModel());
        assertEquals(0, buildables.size());

        assertThrows(InvalidActionException.class, () -> dmc.build("midnight_armour"));
    }

    @Test
    @DisplayName("Test Player can't craft with insufficient materials")
    public void testCraftingInsufficientMaterials() {
        AmongDustController dmc = new AmongDustController();
        dmc.newGame("d_basicCraftableTest", "c_movementTest_testMovementDown");

        for (int i = 0; i < 3; i++) {
            dmc.tick(Direction.RIGHT);
        }

        List<String> buildables = TestUtils.getBuildables(dmc.getDungeonResponseModel());
        assertEquals(0, buildables.size());

        assertThrows(InvalidActionException.class, () -> dmc.build("bow"));
    }

    @Test
    @DisplayName("Test items depleting")
    public void testCraftingDepletesInventory() {
        AmongDustController dmc = new AmongDustController();
        dmc.newGame("d_basicCraftableTest", "c_movementTest_testMovementDown");

        for (int i = 0; i < 4; i++) {
            dmc.tick(Direction.RIGHT);
        }
        dmc.tick(Direction.UP);

        assertDoesNotThrow(() -> dmc.build("bow"));

        List<String> buildables = TestUtils.getBuildables(dmc.getDungeonResponseModel());
        assertEquals(1, buildables.size());

        assertEquals(0, TestUtils.getInventory(dmc.getDungeonResponseModel(), "arrow").size());
        assertEquals(2, TestUtils.getInventory(dmc.getDungeonResponseModel(), "wood").size());
        assertEquals(1, TestUtils.getInventory(dmc.getDungeonResponseModel(), "key").size());

        assertEquals(1, buildables.stream()
                                  .filter(buildable -> buildable.equals("shield"))
                                  .collect(Collectors.toList()).size());

        assertDoesNotThrow(() -> dmc.build("shield"));

        buildables = TestUtils.getBuildables(dmc.getDungeonResponseModel());
        assertEquals(0, buildables.size());

        List<ItemResponse> ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "bow");
        assertEquals(1, ir.size());

        ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "shield");
        assertEquals(1, ir.size());
    }
}
