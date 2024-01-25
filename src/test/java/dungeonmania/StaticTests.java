package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static dungeonmania.TestUtils.getEntities;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class StaticTests {
    @Test
    @DisplayName("Test if static entities appear on the map")
    public void testBasicStaticPlacement() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("d_basicStaticTest", "c_movementTest_testMovementDown");

        assertEquals("d_basicStaticTest", dr.getDungeonName());
        assertEquals(":exit", dr.getGoals());
        assertEquals(0, dr.getBattles().size());
        assertEquals(0, dr.getBuildables().size());
        assertEquals(0, dr.getAnimations().size());
        assertEquals(0, dr.getInventory().size());

        // 11 total entities - 2 exits, 2 walls, 2 portals, 1 boulder, door, switch, spawner and a key (for the door)
        assertEquals(11, dr.getEntities().size());

        assertEquals(2, TestUtils.countEntityOfType(dr, "exit"));
        assertEquals(2, TestUtils.countEntityOfType(dr, "wall"));
        assertEquals(2, TestUtils.countEntityOfType(dr, "portal"));
        assertEquals(1, TestUtils.countEntityOfType(dr, "boulder"));
        assertEquals(1, TestUtils.countEntityOfType(dr, "door"));
        assertEquals(1, TestUtils.countEntityOfType(dr, "switch"));
        assertEquals(1, TestUtils.countEntityOfType(dr, "zombie_toast_spawner"));
        assertEquals(1, TestUtils.countEntityOfType(dr, "key"));
    }

    @Test
    @DisplayName("Test that walking through a portal takes us to the correct location")
    public void testBasicPortal() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_portalsBasic", "c_movementTest_testMovementDown");

        // Step onto the blue portal, expect to be to the right of the other blue portal
        // (which is on 1, 2)
        dmc.tick(Direction.RIGHT);

        DungeonResponse dr = dmc.getDungeonResponseModel();
        Position playerPosition = TestUtils.getPlayerPosition(dr);
        assertEquals(new Position(2, 2), playerPosition);
    }

    @Test
    @DisplayName("Test that walking multiple portals takes us to the correct location")
    public void testComplexPortal() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_portalsAdvanced", "c_movementTest_testMovementDown");

        // Step onto the red portal, expect to go through red and grey and be to the right of the other grey portal
        dmc.tick(Direction.RIGHT);

        DungeonResponse dr = dmc.getDungeonResponseModel();
        Position playerPosition = TestUtils.getPlayerPosition(dr);
        assertEquals(new Position(6, 1), playerPosition);
    }

    @Test
    @DisplayName("Test that walking through blocked portal does nothing")
    public void testPortalIntoWall() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_portalIntoWall", "c_movementTest_testMovementDown");

        // Step into portal directly to the right.
        // Exit portal has wall in the way.
        // Expect move to abort and nothing to happen (i.e., stay at (0, 0))
        dmc.tick(Direction.RIGHT);

        DungeonResponse dr = dmc.getDungeonResponseModel();
        Position playerPosition = TestUtils.getPlayerPosition(dr);
        assertEquals(new Position(0, 0), playerPosition);
    }

    @Test
    @DisplayName("Test that walking into a random wall doesn't unlink portals")
    public void testInvalidMovementDoesntBreakPortal() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_portalIntoWall", "c_movementTest_testMovementDown");

        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.UP);

        DungeonResponse dr = dmc.getDungeonResponseModel();
        Position playerPosition = TestUtils.getPlayerPosition(dr);
        assertEquals(new Position(1, -1), playerPosition);
    }

    @Test
    @DisplayName("Test basic Boulder pushing")
    public void testBasicBoulder() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_boulderTest", "c_movementTest_testMovementDown");

        dmc.tick(Direction.RIGHT);

        DungeonResponse dr = dmc.getDungeonResponseModel();

        // Make sure the player moved correctly
        Position playerPosition = TestUtils.getPlayerPosition(dr);
        assertEquals(new Position(1, 0), playerPosition);

        // Make sure the boulder moved correctly
        boolean boulderAtCorrectPosition = TestUtils.getEntities(dr, "boulder").stream()
                                                    .anyMatch(b -> b.getPosition().equals(new Position(2, 0)));
        assertTrue(boulderAtCorrectPosition);
    }

    @Test
    @DisplayName("Test pushing boulder into a Wall")
    public void testBoulderBlockedByWall() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_boulderTest", "c_movementTest_testMovementDown");

        dmc.tick(Direction.LEFT);

        DungeonResponse dr = dmc.getDungeonResponseModel();

        // Make sure player didn't move
        Position playerPosition = TestUtils.getPlayerPosition(dr);
        assertEquals(new Position(0, 0), playerPosition);

        // Make sure the boulder didn't move
        boolean boulderAtCorrectPosition = TestUtils.getEntities(dr, "boulder").stream()
                                                    .anyMatch(b -> b.getPosition().equals(new Position(-1, 0)));
        assertTrue(boulderAtCorrectPosition);
    }

    @Test
    @DisplayName("Test pushing Boulder onto an item (treasure)")
    public void testBoulderBlockedByCollectable() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_boulderTest", "c_movementTest_testMovementDown");

        // Same as basic test, but push it twice so it's on top of treasure
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        DungeonResponse dr = dmc.getDungeonResponseModel();

        // Make sure the player moved correctly
        Position playerPosition = TestUtils.getPlayerPosition(dr);
        assertEquals(new Position(2, 0), playerPosition);

        // Make sure the boulder moved correctly and the treasure is still there
        boolean boulderAtCorrectPosition = TestUtils.getEntities(dr, "boulder").stream()
                                                    .anyMatch(b -> b.getPosition().equals(new Position(3, 0)));
        assertTrue(boulderAtCorrectPosition);
        boolean treasureAtCorrectPosition = TestUtils.getEntities(dr, "treasure").stream()
                                                     .anyMatch(b -> b.getPosition().equals(new Position(3, 0)));
        assertTrue(treasureAtCorrectPosition);

        // Push once more and expect player to pick up item, player to move and boulder to move
        dmc.tick(Direction.RIGHT);
        dr = dmc.getDungeonResponseModel();

        playerPosition = TestUtils.getPlayerPosition(dr);
        assertEquals(new Position(3, 0), playerPosition);

        boulderAtCorrectPosition = TestUtils.getEntities(dr, "boulder").stream()
                                            .anyMatch(b -> b.getPosition().equals(new Position(4, 0)));
        assertTrue(boulderAtCorrectPosition);

        boolean noTreasure = TestUtils.getEntities(dr, "treasure").isEmpty();
        assertTrue(noTreasure);

        List<ItemResponse> ir = TestUtils.getInventory(dr, "treasure");
        assertEquals(1, ir.size());
    }

    @Test
    @DisplayName("Test pushing Boulder onto a FloorSwitch")
    public void testBoulderBlockedByFloorSwitch() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_boulderTest", "c_movementTest_testMovementDown");

        // Go down and try to push boulder onto a floorswitch
        dmc.tick(Direction.UP);

        DungeonResponse dr = dmc.getDungeonResponseModel();

        // Make sure the player moved down
        Position playerPosition = TestUtils.getPlayerPosition(dr);
        assertEquals(new Position(0, -1), playerPosition);

        // Make sure the boulder moved correctly and the switch is still there
        boolean boulderAtCorrectPosition = TestUtils.getEntities(dr, "boulder").stream()
                                                    .anyMatch(b -> b.getPosition().equals(new Position(0, -2)));
        assertTrue(boulderAtCorrectPosition);

        boolean switchAtCorrectPosition = TestUtils.getEntities(dr, "switch").stream()
                                                   .anyMatch(b -> b.getPosition().equals(new Position(0, -2)));
        assertTrue(switchAtCorrectPosition);

        // Push once more
        dmc.tick(Direction.UP);

        dr = dmc.getDungeonResponseModel();

        playerPosition = TestUtils.getPlayerPosition(dr);
        assertEquals(new Position(0, -2), playerPosition);

        boulderAtCorrectPosition = TestUtils.getEntities(dr, "boulder").stream()
                                            .anyMatch(b -> b.getPosition().equals(new Position(0, -3)));
        assertTrue(boulderAtCorrectPosition);

        switchAtCorrectPosition = TestUtils.getEntities(dr, "switch").stream()
                                           .anyMatch(b -> b.getPosition().equals(new Position(0, -2)));
        assertTrue(switchAtCorrectPosition);
    }

    @Test
    @DisplayName("Test pushing 2 Boulders in a row")
    public void testBoulderBlockedByBoulder() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_boulderTest", "c_movementTest_testMovementDown");

        // Go up and try and push 2 boulders
        dmc.tick(Direction.DOWN);

        DungeonResponse dr = dmc.getDungeonResponseModel();

        // Make sure the nothing moved
        Position playerPosition = TestUtils.getPlayerPosition(dr);
        assertEquals(new Position(0, 0), playerPosition);

        // Make sure neither boulder moved
        boolean boulder1AtCorrectPosition = TestUtils.getEntities(dr, "boulder").stream()
                                                     .anyMatch(b -> b.getPosition().equals(new Position(0, 1)));
        assertTrue(boulder1AtCorrectPosition);

        boolean boulder2AtCorrectPosition = TestUtils.getEntities(dr, "boulder").stream()
                                                     .anyMatch(b -> b.getPosition().equals(new Position(0, 2)));
        assertTrue(boulder2AtCorrectPosition);
    }

    @Test
    @DisplayName("Test trying to open a door without any key")
    public void testNoKey() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_doorTest", "c_movementTest_testMovementDown");

        // Go around the key and try and to go through the door
        dmc.tick(Direction.UP);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.DOWN);

        DungeonResponse dr = dmc.getDungeonResponseModel();

        // Make sure the player didn't
        Position playerPosition = TestUtils.getPlayerPosition(dr);
        assertEquals(new Position(2, -1), playerPosition);
    }

    @Test
    @DisplayName("Test trying to open a door with the wrong key")
    public void testWrongKey() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_doorTest", "c_movementTest_testMovementDown");

        // Grab the wrong key, then go back to the other door
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.DOWN);

        DungeonResponse dr = dmc.getDungeonResponseModel();

        // Make sure the player didn't go in the door
        Position playerPosition = TestUtils.getPlayerPosition(dr);
        assertEquals(new Position(2, -1), playerPosition);
    }

    @Test
    @DisplayName("Test trying to open a door with the correct key")
    public void testCorrectKey1() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_doorTest", "c_movementTest_testMovementDown");

        // Pick up the correct key and open
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        DungeonResponse dr = dmc.getDungeonResponseModel();

        // Make sure the player is in the door
        Position playerPosition = TestUtils.getPlayerPosition(dr);
        assertEquals(new Position(2, 0), playerPosition);

        // Make sure that if I step back off, I can step back onto the door
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.LEFT);
        dr = dmc.getDungeonResponseModel();
        playerPosition = TestUtils.getPlayerPosition(dr);
        assertEquals(new Position(2, 0), playerPosition);
    }

    @Test
    @DisplayName("Test trying to open a door with the correct key")
    public void testCorrectKey2() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_doorTest", "c_movementTest_testMovementDown");

        // Pick up the correct key and open
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);

        DungeonResponse dr = dmc.getDungeonResponseModel();

        // Make sure the player is in the door
        Position playerPosition = TestUtils.getPlayerPosition(dr);
        assertEquals(new Position(-2, 0), playerPosition);

        // Make sure that if I step back off, I can step back onto the door
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.LEFT);
        dr = dmc.getDungeonResponseModel();
        playerPosition = TestUtils.getPlayerPosition(dr);
        assertEquals(new Position(-2, 0), playerPosition);
    }

    @Test
    @DisplayName("Test trying to open a door with wrong key. Also, can't pick up 2 keys")
    public void testTryingToPickUp2Keys() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_doorTest", "c_movementTest_testMovementDown");

        // Pick up both keys then try and open one door
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);

        DungeonResponse dr = dmc.getDungeonResponseModel();

        // Make sure the player can't open this door
        Position playerPosition = TestUtils.getPlayerPosition(dr);
        assertEquals(new Position(-1, 0), playerPosition);

        // Go and open the other door (which we actually do have the correct key for)
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dr = dmc.getDungeonResponseModel();
        playerPosition = TestUtils.getPlayerPosition(dr);
        assertEquals(new Position(2, 0), playerPosition);

        // Make sure that if I step back off, I can step back onto the door
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.LEFT);
        dr = dmc.getDungeonResponseModel();
        playerPosition = TestUtils.getPlayerPosition(dr);
        assertEquals(new Position(2, 0), playerPosition);
    }

    @Test
    @DisplayName("Test open a door, walk into a wall and check that the door is still open (a bug I found while wriing the implementation)")
    public void testInvalidMovementResetsDoor() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_doorTest", "c_movementTest_testMovementDown");

        // Grab key, unlock door, step off door, walk into wall then go back into the door
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.RIGHT);

        DungeonResponse dr = dmc.getDungeonResponseModel();

        // Make sure the player is in the door
        Position playerPosition = TestUtils.getPlayerPosition(dr);
        assertEquals(new Position(2, 0), playerPosition);
    }

    @Test
    @DisplayName("Test place Zombie Toast Spawner")
    public void testZombieToastSpawnerPlace() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_spawnerTest", "c_spawnerTest_spawnRateZero");

        DungeonResponse dr = dmc.getDungeonResponseModel();

        List<EntityResponse> spawners = TestUtils.getEntities(dr, "zombie_toast_spawner");

        assertEquals(new Position(100, 100), spawners.get(0).getPosition());
    }

    @Test
    @DisplayName("Test Zombie Toast Spawner No Spawning")
    public void testZombieToastSpawnerZero() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_spawnerTest", "c_spawnerTest_spawnRateZero");


        for (int i = 0; i < 45; i++) {
            dmc.tick(Direction.DOWN);
        }

        // Ensure no zombies created
        assertEntityNum(dmc, "zombie_toast", 0);
    }

    @Test
    @DisplayName("Test Zombie Toast Spawner Spawning Every Tick")
    public void testZombieToastSpawnerOne() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_spawnerTest", "c_spawnerTest_spawnRateOne");

        // 4 zombie toasts only, as they cannot spawn atop one another
        for (int i = 1; i < 4; i++) {
            dmc.tick(Direction.DOWN);

            // Ensure the right number of zombies are created
            assertEntityNum(dmc, "zombie_toast", i);

        }
    }

    @Test
    @DisplayName("Test Zombie Toast Spawner Spawning Every 10 Ticks")
    public void testZombieToastSpawnerTen() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_spawnerTest", "c_spawnerTest_spawnRateTen");

        for (int i = 1; i < 45; i++) {
            dmc.tick(Direction.DOWN);

            // Ensure the right number of zombies are created (once every 10 ticks)
            assertEntityNum(dmc, "zombie_toast", i / 10);
        }
    }

    @Test
    @DisplayName("Test Zombie Toast Spawner all sides blocked")
    public void testZombieToastSpawnerBlocked() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_spawnerTestBlocked", "c_spawnerTest_spawnRateOne");

        for (int i = 1; i < 45; i++) {
            dmc.tick(Direction.DOWN);

            // Ensure no zombie toasts have spawned
            assertEntityNum(dmc, "zombie_toast", 0);
        }
    }


    // Helper function to assert the number of entitites of a type found
    private void assertEntityNum(DungeonManiaController dmc, String type, int num) {
        DungeonResponse dr = dmc.getDungeonResponseModel();
        List<EntityResponse> entities = TestUtils.getEntities(dr, type);
        assertEquals(num, entities.size());
    }






    @Test
    @DisplayName("Test Destroying Zombie Toast Spawner from too far away")
    public void testZombieToastSpawnerDestroyTooFar() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("d_spawnerTestDestroy", "c_spawnerTest_spawnRateZero");

        // Move up and right to collect sword
        dr = dmc.tick(Direction.UP);
        dr = dmc.tick(Direction.RIGHT);

        // Get spawner
        List<EntityResponse> spawners = getEntities(dr, "zombie_toast_spawner");
        String spawner = spawners.get(0).getId();
        // Try destroying spawner from 2 blocks away
        assertThrows(InvalidActionException.class, () -> dmc.interact(spawner));
    }

    @Test
    @DisplayName("Test Destroying Zombie Toast Spawner from diagonally adjacent block")
    public void testZombieToastSpawnerDestroyDiagonal() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("d_spawnerTestDestroy", "c_spawnerTest_spawnRateZero");

        // Move up and right to collect sword
        dr = dmc.tick(Direction.UP);
        dr = dmc.tick(Direction.RIGHT);

        // Move to diagonal position
        dr = dmc.tick(Direction.RIGHT);

        // Get spawner
        List<EntityResponse> spawners = getEntities(dr, "zombie_toast_spawner");
        String spawner = spawners.get(0).getId();
        // Try destroying spawner from diagonal
        assertThrows(InvalidActionException.class, () -> dmc.interact(spawner));
    }

    @Test
    @DisplayName("Test Destroying Zombie Toast Spawner with no weapon")
    public void testZombieToastSpawnerDestroyNoWeapon() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("d_spawnerTestDestroy", "c_spawnerTest_spawnRateZero");

        // Move into position
        dr = dmc.tick(Direction.RIGHT);
        dr = dmc.tick(Direction.RIGHT);

        // Get spawner
        List<EntityResponse> spawners = getEntities(dr, "zombie_toast_spawner");
        String spawner = spawners.get(0).getId();
        // Try destroying spawner from 2 blocks away
        assertThrows(InvalidActionException.class, () -> dmc.interact(spawner));
    }

    @Test
    @DisplayName("Test Destroying Zombie Toast Spawner with sword")
    public void testZombieToastSpawnerDestroy() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("d_spawnerTestDestroy", "c_spawnerTest_spawnRateZero");

        // Move up and right to collect sword
        dr = dmc.tick(Direction.UP);
        dr = dmc.tick(Direction.RIGHT);

        // Move to the tile above the spawner
        dr = dmc.tick(Direction.RIGHT);
        dr = dmc.tick(Direction.RIGHT);

        // Get spawner
        List<EntityResponse> spawners = getEntities(dr, "zombie_toast_spawner");
        String spawner = spawners.get(0).getId();
        // Try destroying spawner
        assertDoesNotThrow(() -> dmc.interact(spawner));

        // Check none are left
        assertEntityNum(dmc, "zombie_toast_spawner", 0);
    }

    @Test
    @DisplayName("Test basic or logic light bulb mechanism")
    public void testLogicBasicSwitchBulb() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("d_logicTest_switchBulb", "c_movementTest_testMovementDown");

        // Make some random moves that shouldn't trigger anything
        dr = dmc.tick(Direction.UP);
        dr = dmc.tick(Direction.DOWN);

        // Make sure all bulbs are off
        assertTrue(TestUtils.countEntityOfType(dr, "light_bulb_off") == 2);
        assertTrue(TestUtils.countEntityOfType(dr, "light_bulb_on") == 0);

        // Push boulder onto switch
        dr = dmc.tick(Direction.RIGHT);

        // Make sure one off, one on
        assertTrue(TestUtils.countEntityOfType(dr, "light_bulb_on") == 1);
        assertTrue(TestUtils.countEntityOfType(dr, "light_bulb_off") == 1);
    }

    @Test
    @DisplayName("Test basic non-logic light bulb mechanism")
    public void testLogicSwitchActivationByWire() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("d_logicTest_switchActivationByWire", "c_movementTest_testMovementDown");

        // Make some random moves that shouldn't trigger anything
        dr = dmc.tick(Direction.UP);
        dr = dmc.tick(Direction.DOWN);

        // Make sure all bulbs are off
        assertTrue(TestUtils.countEntityOfType(dr, "light_bulb_off") == 1);
        assertTrue(TestUtils.countEntityOfType(dr, "light_bulb_on") == 0);

        // Push boulder onto switch
        dr = dmc.tick(Direction.RIGHT);

        // Make sure both light switches turned on
        assertTrue(TestUtils.countEntityOfType(dr, "light_bulb_on") == 1);
        assertTrue(TestUtils.countEntityOfType(dr, "light_bulb_off") == 0);

        // Push boulder off
        dr = dmc.tick(Direction.UP);
        dr = dmc.tick(Direction.RIGHT);
        dr = dmc.tick(Direction.DOWN);

        // Make sure all bulbs are off
        assertTrue(TestUtils.countEntityOfType(dr, "light_bulb_off") == 1);
        assertTrue(TestUtils.countEntityOfType(dr, "light_bulb_on") == 0);
    }

    @Test
    @DisplayName("Test basic wires blowing up a bomb")
    public void testLogicBombActivationByWire() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("d_logicTest_bombActivationByWire", "c_bombTest_placeBombRadius2");

        // Make some random moves that shouldn't trigger anything
        dr = dmc.tick(Direction.UP);
        dr = dmc.tick(Direction.DOWN);

        // Make sure sword still there
        assertTrue(TestUtils.countEntityOfType(dr, "sword") == 1);
        assertTrue(TestUtils.countEntityOfType(dr, "bomb") == 1);

        // Push boulder onto switch
        dr = dmc.tick(Direction.RIGHT);

        // Make sure bomb exploded
        assertTrue(TestUtils.countEntityOfType(dr, "sword") == 0);
        assertTrue(TestUtils.countEntityOfType(dr, "bomb") == 0);
        assertTrue(TestUtils.countEntityOfType(dr, "wire") == 0);
        assertTrue(TestUtils.countEntityOfType(dr, "switch") == 1);
        assertTrue(TestUtils.countEntityOfType(dr, "boulder") == 1);
    }

    @Test
    @DisplayName("Test switch door activation")
    public void testLogicSwitchDoorActivationByWire() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("d_logicTest_switchDoorActivationByWire", "c_bombTest_placeBombRadius2");

        // Make sure player can't open door
        dr = dmc.tick(Direction.LEFT);

        // Make sure player is still there
        assertEquals(TestUtils.getPlayerPosition(dr), new Position(0, 0, 0));

        // Push boulder onto switch
        dr = dmc.tick(Direction.RIGHT);

        // Go back to now opened door opened
        dr = dmc.tick(Direction.LEFT);
        dr = dmc.tick(Direction.LEFT);
        dr = dmc.tick(Direction.LEFT);
        assertTrue(TestUtils.getInventory(dr, "treasure").size() == 1);
        assertTrue(TestUtils.countEntityOfType(dr, "treasure") == 0);

        // Deactivate door by pushing boulder off
        for (int i = 0; i < 4; i++) {
            dr = dmc.tick(Direction.RIGHT);
        }
        for (int i = 0; i < 4; i++) {
            dr = dmc.tick(Direction.LEFT);
        }

        // Make sure door locked
        assertEquals(TestUtils.getPlayerPosition(dr), new Position(0, 0));

        // Pick up key
        dr = dmc.tick(Direction.UP);
        dr = dmc.tick(Direction.DOWN);

        // Move inside door
        dr = dmc.tick(Direction.LEFT);
        dr = dmc.tick(Direction.LEFT);

        // Make sure player inside room
        assertEquals(TestUtils.getPlayerPosition(dr), new Position(-2, 0));
    }

    @Test
    @DisplayName("Test player being unable to pick up bulb or wire")
    public void testLogicCantPickUpBulbOrWire() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("d_logicTest_basicPickupTest", "c_bombTest_placeBombRadius2");

        // Move right past all the entities
        dr = dmc.tick(Direction.RIGHT);
        dr = dmc.tick(Direction.RIGHT);

        // Make sure nothing in inventory
        assertTrue(TestUtils.countEntityOfType(dr, "wire") == 1);
        assertTrue(TestUtils.countEntityOfType(dr, "light_bulb_off") == 1);
        assertTrue(TestUtils.getInventory(dr, "wire").size() == 0);
        assertTrue(TestUtils.getInventory(dr, "light_bulb_off").size() == 0);
    }

    @Test
    @DisplayName("Test logical AND switch still works if boulder is stepped on it")
    public void testLogicANDSwitch1Input() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("d_logicTest_logicANDBulb1SwitchInput", "c_bombTest_placeBombRadius2");

        // Push boulder onto switch
        dr = dmc.tick(Direction.RIGHT);

        // Make sure bulb is now on
        assertTrue(TestUtils.countEntityOfType(dr, "light_bulb_off") == 0);
    }

    @Test
    @DisplayName("Test logical AND switch lighting with 4 switch inputs")
    public void testLogicANDSwitch4Inputs() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("d_logicTest_logicANDBulb4SwitchInput", "c_bombTest_placeBombRadius2");

        // Make some random moves that shouldn't trigger anything
        dr = dmc.tick(Direction.UP);
        dr = dmc.tick(Direction.DOWN);

        // Make sure bulb turned off
        assertTrue(TestUtils.countEntityOfType(dr, "light_bulb_off") == 1);

        // Push boulder onto switch
        dr = dmc.tick(Direction.RIGHT);

        // Make sure with 4 inputs, the bulb is on
        assertTrue(TestUtils.countEntityOfType(dr, "light_bulb_on") == 1);
        assertTrue(TestUtils.countEntityOfType(dr, "light_bulb_off") == 0);
    }

    @Test
    @DisplayName("Test logical AND switch with 1 switch and 2 wire inputs")
    public void testLogicAND1SwitchInput2WireInputs() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("d_logicTest_Bulb1SwitchInput2WireInputs", "c_bombTest_placeBombRadius2");

        // Make sure bulb turned off
        assertTrue(TestUtils.countEntityOfType(dr, "light_bulb_off") == 1);

        // Push boulder onto switch, bulb should turn on because of already active wire input
        dr = dmc.tick(Direction.RIGHT);

        // Make sure bulb turned on
        assertTrue(TestUtils.countEntityOfType(dr, "light_bulb_on") == 1);

        // Push second boulder, nothing should happen
        dr = dmc.tick(Direction.DOWN);
        dr = dmc.tick(Direction.DOWN);
        dr = dmc.tick(Direction.DOWN);
        dr = dmc.tick(Direction.RIGHT);
        assertTrue(TestUtils.countEntityOfType(dr, "light_bulb_on") == 1);

        // Push boulder off, and original boulder off
        for (int i = 0; i < 6; i++) {
            dr = dmc.tick(Direction.RIGHT);
        }
        for (int i = 0; i < 3; i++) {
            dr = dmc.tick(Direction.UP);
        }
        dr = dmc.tick(Direction.LEFT);

        // Make sure bulb is now off
        assertTrue(TestUtils.countEntityOfType(dr, "light_bulb_off") == 1);
    }

    @Test
    @DisplayName("Test complex case with or switch")
    public void testLogicORSwitchComplex() {
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

        // Make sure bulb turned off still
        assertTrue(TestUtils.countEntityOfType(dr, "light_bulb_off") == 1);

        // Push second boulder
        dr = dmc.tick(Direction.UP);
        for (int i = 0; i < 9; i++) {
            dr = dmc.tick(Direction.RIGHT);
        }
        dr = dmc.tick(Direction.DOWN);
        dr = dmc.tick(Direction.LEFT);

        // Light bulb should turn on
        assertTrue(TestUtils.countEntityOfType(dr, "light_bulb_on") == 1);
    }

    @Test
    @DisplayName("Test complex case with co_and switch not working")
    public void testLogicCOANDSwitchComplex() {
        // x represents wire
        /*                          light_bulb
         *                              x
         * Player Boulder switch x co_and_switch x or_switch x switch boulder
         *
         */
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("d_logicTest_coAndSwitch", "c_bombTest_placeBombRadius2");

        // Make sure bulb turned off
        assertTrue(TestUtils.countEntityOfType(dr, "light_bulb_off") == 1);

        // Push boulder onto switch
        dr = dmc.tick(Direction.RIGHT);

        // Make sure bulb turned off still
        assertTrue(TestUtils.countEntityOfType(dr, "light_bulb_off") == 1);

        // Push second boulder
        dr = dmc.tick(Direction.DOWN);
        for (int i = 0; i < 8; i++) {
            dr = dmc.tick(Direction.RIGHT);
        }
        dr = dmc.tick(Direction.UP);
        dr = dmc.tick(Direction.LEFT);

        // Light bulb should still be off cause not the same tick
        assertTrue(TestUtils.countEntityOfType(dr, "light_bulb_off") == 1);
    }






    // @Test
    // @DisplayName("Test complex case with xor bulb")
    // public void testLogicANDORXORBulb() {
    //     // x represents wire
    //     /*                            bomb      treasure
    //      *                              x                                       wall
    //      * Player Boulder switch x co_and_switch    x      x switch boulder  zombie_toast wall
    //      *                                                                      wall
    //      */
    //     DungeonManiaController dmc = new DungeonManiaController();
    //     DungeonResponse dr = dmc.newGame("d_logicTest_coAndSwitchBomb", "c_bombTest_placeBombRadius2");

    //     // Make sure bomb is still there
    //     assertTrue(TestUtils.countEntityOfType(dr, "bomb") == 1);
    //     assertTrue(TestUtils.countEntityOfType(dr, "treasure") == 1);

    //     // Push boulder onto switch, zombie should push boulder
    //     dr = dmc.tick(Direction.RIGHT);

    //     // Make sure treasure blew up
    //     assertTrue(TestUtils.countEntityOfType(dr, "treasure") == 0);
    //     assertTrue(TestUtils.countEntityOfType(dr, "bomb") == 0);
    // }
}
