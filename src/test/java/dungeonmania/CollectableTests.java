package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;


public class CollectableTests {
    @Test
    @DisplayName("Test if collectable entities appear on the map")
    public void testCollectableSpawning() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("d_collectableTest", "c_movementTest_testMovementDown");

        assertEquals("d_collectableTest", dr.getDungeonName());
        assertEquals(":exit", dr.getGoals());
        assertEquals(0, dr.getBattles().size());
        assertEquals(0, dr.getBuildables().size());
        assertEquals(0, dr.getAnimations().size());
        assertEquals(0, dr.getInventory().size());

        assertEquals(9, dr.getEntities().size());

        assertTrue(dr.getEntities().stream().anyMatch(er ->
            er.getType().equals("treasure") && er.getPosition().equals(new Position(1, 1))));
        assertTrue(dr.getEntities().stream().anyMatch(er ->
            er.getType().equals("key") && er.getPosition().equals(new Position(1, 1))));
        assertTrue(dr.getEntities().stream().anyMatch(er ->
            er.getType().equals("invincibility_potion") && er.getPosition().equals(new Position(1, 1))));
        assertTrue(dr.getEntities().stream().anyMatch(er ->
            er.getType().equals("invisibility_potion") && er.getPosition().equals(new Position(1, 1))));
        assertTrue(dr.getEntities().stream().anyMatch(er ->
            er.getType().equals("wood") && er.getPosition().equals(new Position(3, 1))));
        assertTrue(dr.getEntities().stream().anyMatch(er ->
            er.getType().equals("arrow") && er.getPosition().equals(new Position(1, 3))));
        assertTrue(dr.getEntities().stream().anyMatch(er ->
            er.getType().equals("bomb") && er.getPosition().equals(new Position(2, 1))));
        assertTrue(dr.getEntities().stream().anyMatch(er ->
            er.getType().equals("sword") && er.getPosition().equals(new Position(6, 9))));
        assertTrue(dr.getEntities().stream().anyMatch(er ->
            er.getType().equals("sword") && er.getPosition().equals(new Position(4, 20))));
    }

    @Test
    @DisplayName("Test Player can pick up and place a bomb")
    public void testPlayerBombPlacement() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("d_bombPlacementTest", "c_movementTest_testMovementDown");
        dr = dmc.tick(Direction.RIGHT);
        dr = dmc.tick(Direction.DOWN);

        List<ItemResponse> ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "bomb");
        assertEquals(1, ir.size());
        dr = dmc.tick(Direction.RIGHT);

        // Place bomb one space to the right
        String bombId = ir.get(0).getId();
        assertDoesNotThrow(() -> dmc.tick(bombId));
        dr = dmc.getDungeonResponseModel();
        assertTrue(dr.getEntities().stream().anyMatch(er ->
            er.getType().equals("bomb") && er.getPosition().equals(new Position(4, 3))));
    }


    @Test
    @DisplayName("Test bomb doesn't blow up next to inactive switch")
    public void testBombAdjacentInactiveSwitch() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_bombSimpleTest", "c_movementTest_testMovementDown");
        // Move up and down to get some ticks done
        dmc.tick(Direction.UP);
        dmc.tick(Direction.DOWN);

        DungeonResponse dr = dmc.getDungeonResponseModel();
        assertTrue(dr.getEntities().stream().anyMatch(er ->
            er.getType().equals("bomb") && er.getPosition().equals(new Position(1, 0))));
        assertTrue(dr.getEntities().stream().anyMatch(er ->
            er.getType().equals("switch") && er.getPosition().equals(new Position(1, -1))));
    }

    @Test
    @DisplayName("Test bomb doesn't blow up next to boulder")
    public void testBombAdjacentBoulder() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_bombSimpleTest2", "c_movementTest_testMovementDown");

        // Move up and down to get some ticks done
        dmc.tick(Direction.UP);
        dmc.tick(Direction.DOWN);

        DungeonResponse dr = dmc.getDungeonResponseModel();
        assertTrue(dr.getEntities().stream().anyMatch(er ->
            er.getType().equals("bomb") && er.getPosition().equals(new Position(1, 0))));
        assertTrue(dr.getEntities().stream().anyMatch(er ->
            er.getType().equals("boulder") && er.getPosition().equals(new Position(1, -1))));
    }

    @Test
    @DisplayName("Test bomb blows up when switch activating")
    public void testBombAdjacentActivatingSwitch() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_bombComplexTest", "c_movementTest_testMovementDown"); // Bomb radius 1

        // Get ready to push boulder but dont pick up wood
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.RIGHT);

        DungeonResponse dr = dmc.getDungeonResponseModel();
        // Make sure nothing has blown up yet
        assertTrue(dr.getEntities().stream().anyMatch(er ->
            er.getType().equals("bomb") && er.getPosition().equals(new Position(1, 0))));
        assertTrue(dr.getEntities().stream().anyMatch(er ->
            er.getType().equals("treasure") && er.getPosition().equals(new Position(1, 1))));

        // Push boulder
        dmc.tick(Direction.RIGHT);
        dr = dmc.getDungeonResponseModel();
        // Make sure wood is the only thing that didn't get blown up
        assertTrue(dr.getEntities().stream().anyMatch(er ->
            er.getType().equals("wood") && er.getPosition().equals(new Position(-1, 0))));
            assertTrue(dr.getEntities().stream().anyMatch(er ->
            er.getType().equals("player") && er.getPosition().equals(new Position(0, -1))));
        // Assert only player and wood remain
        assertTrue(dr.getEntities().size() == 2);
    }

    @Test
    @DisplayName("Test bomb blows up when placed next to already active switch")
    public void testBombAdjacentActivatedSwitch() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_bombComplexTest", "c_movementTest_testMovementDown"); // Bomb radius 1

        // Pick up bomb
        dmc.tick(Direction.RIGHT);

        // Go around wood, push boulder onto switch
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        // Prepare to place bomb
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.RIGHT);

        List<ItemResponse> ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "bomb");
        String bombId = ir.get(0).getId();
        assertDoesNotThrow(() -> dmc.tick(bombId));

        DungeonResponse dr = dmc.getDungeonResponseModel();
        // Make sure wood is the only thing that didn't get blown up
        assertTrue(dr.getEntities().stream().anyMatch(er ->
            er.getType().equals("wood") && er.getPosition().equals(new Position(-1, 0))));
        assertTrue(dr.getEntities().stream().anyMatch(er ->
            er.getType().equals("player") && er.getPosition().equals(new Position(1, 0))));
        assertTrue(dr.getEntities().size() == 2);
    }

    @Test
    @DisplayName("Test bomb radius")
    public void testBombRadius() {
        // Same as last test, but radius is 2
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_bombComplexTest", "c_bombTest_placeBombRadius2"); // Bomb radius 2

        // Pick up bomb
        dmc.tick(Direction.RIGHT);

        // Go around wood, push boulder onto switch
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        // Prepare to place bomb
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.RIGHT);

        List<ItemResponse> ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "bomb");
        String bombId = ir.get(0).getId();
        assertDoesNotThrow(() -> dmc.tick(bombId));

        DungeonResponse dr = dmc.getDungeonResponseModel();
        // Make sure only player remains
        assertTrue(dr.getEntities().stream().anyMatch(er ->
            er.getType().equals("player") && er.getPosition().equals(new Position(1, 0))));
        assertTrue(dr.getEntities().size() == 1);
    }

    @Test
    @DisplayName("Test id invalid")
    public void testBombIdInvalid() {
        // Same as last test, but radius is 2
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_bombComplexTest", "c_bombTest_placeBombRadius2"); // Bomb radius 2

        // Pick up wood
        dmc.tick(Direction.LEFT);

        List<ItemResponse> ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "wood");
        String invalidBombId = ir.get(0).getId();
        assertThrows(IllegalArgumentException.class, () -> dmc.tick(invalidBombId));
    }

    @Test
    @DisplayName("Test bomb gets removed from inventory")
    public void testBombRemovalFromInventory() {
        // Same as last test, but radius is 2
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_bombComplexTest", "c_bombTest_placeBombRadius2"); // Bomb radius 2

        // Make sure no bomb in inventory
        List<ItemResponse> ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "bomb");
        assertTrue(ir.isEmpty());

        // Pick up bomb
        dmc.tick(Direction.RIGHT);

        // Make sure bomb in inventory
        ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "bomb");
        assertFalse(ir.isEmpty());

        // Move right and place bomb
        dmc.tick(Direction.RIGHT);
        ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "bomb");
        String bombId = ir.get(0).getId();
        assertDoesNotThrow(() -> dmc.tick(bombId));

        // Move right again
        dmc.tick(Direction.RIGHT);

        // Make sure no bomb in inventory again
        ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "bomb");
        assertTrue(ir.isEmpty());

        // Assert you can't place bomb again
        assertThrows(InvalidActionException.class, () -> dmc.tick(bombId));
    }

    @Test
    @DisplayName("Test bomb works with one active switch, one inactive")
    public void testBombTwoSwitchesOneInactive() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_bombMultipleSwitchesTest", "c_bombTest_placeBombRadius2"); // Bomb radius 2

        // Pick up bomb
        dmc.tick(Direction.RIGHT);

        // Go around wood, push boulder onto switch
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        // Prepare to place bomb
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.RIGHT);

        List<ItemResponse> ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "bomb");
        String bombId = ir.get(0).getId();
        assertDoesNotThrow(() -> dmc.tick(bombId));

        DungeonResponse dr = dmc.getDungeonResponseModel();
        // Make sure only player remains
        assertTrue(dr.getEntities().stream().anyMatch(er ->
            er.getType().equals("player") && er.getPosition().equals(new Position(1, 0))));
        assertTrue(dr.getEntities().size() == 1);
    }

    @Test
    @DisplayName("Test bomb works with two active switches")
    public void testBombTwoSwitchesBothInactive() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_bombMultipleSwitchesTest", "c_bombTest_placeBombRadius2"); // Bomb radius 2

        // Pick up bomb
        dmc.tick(Direction.RIGHT);

        // Go around wood, push boulder onto switch
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        // Push other boulder onto switch
        dmc.tick(Direction.UP);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.DOWN);

        // Prepare to place bomb
        dmc.tick(Direction.UP);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.RIGHT);

        // Place bomb, everything should blow up
        List<ItemResponse> ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "bomb");
        String bombId = ir.get(0).getId();
        assertDoesNotThrow(() -> dmc.tick(bombId));

        DungeonResponse dr = dmc.getDungeonResponseModel();
        // Make sure only player remains
        assertTrue(dr.getEntities().stream().anyMatch(er ->
            er.getType().equals("player") && er.getPosition().equals(new Position(1, 0))));
        assertTrue(dr.getEntities().size() == 1);
    }

    @Test
    @DisplayName("Test Sun Stone working completes treasure goal")
    public void testSunStoneCompletesTreasureGoal() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("d_testSunStone", "c_sunStoneTests_treasureGoal1");

        // Pick up sunstone
        dr = dmc.tick(Direction.RIGHT);

        // Goals should be complete now
        assertTrue(TestUtils.getGoals(dr).length() == 0);
    }

    @Test
    @DisplayName("Test sun stone opens locked door")
    public void testSunStoneUnlocksDoor() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_testSunStone", "c_sunStoneTests_treasureGoal2");

        // Pick up sun stone and walk into door
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.DOWN);

        DungeonResponse dr = dmc.getDungeonResponseModel();
        assertEquals(new Position(1, 1), TestUtils.getPlayer(dr).get().getPosition());

        // Step off and walk into the wall, then back onto the door
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.UP);

        // Check we're back on the door
        dr = dmc.getDungeonResponseModel();
        assertEquals(new Position(1, 1), TestUtils.getPlayer(dr).get().getPosition());
    }

    @Test
    @DisplayName("Test sun stone can't bribe assassin or mercenary")
    public void testSunStoneNoBribe() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_testSunStoneNotBribing", "c_sunStoneTests_treasureGoal2");

        // Pick up sun stone
        dmc.tick(Direction.RIGHT);
        DungeonResponse dr = dmc.getDungeonResponseModel();
        String mercId = TestUtils.getEntities(dr, "mercenary").get(0).getId();
        String assassinId = TestUtils.getEntities(dr, "assassin").get(0).getId();

        assertThrows(InvalidActionException.class, () -> dmc.interact(mercId));
        assertThrows(InvalidActionException.class, () -> dmc.interact(assassinId));
    }
}
