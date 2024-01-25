package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class IntegrationTests {
    @Test
    @DisplayName("A full playthrough #1")
    public void testIntegration1() {
        DungeonManiaController dmc = integration1();
        DungeonResponse dr = dmc.tick(Direction.DOWN);
        assertEquals("", dr.getGoals());
    }

    @Test
    @DisplayName("A full playthrough #2")
    public void testIntegration2() {
        DungeonManiaController dmc = integration1();

        // Go pick up the key for the right door
        // as well as some collectables
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.DOWN);
        for (int i = 0; i < 3; i++) {
            dmc.tick(Direction.RIGHT);
        }
        // Check we have the key
        DungeonResponse dr = dmc.getDungeonResponseModel();
        assertEquals(1, TestUtils.getInventory(dr, "key").size());

        for (int i = 0; i < 4; i++) {
            dmc.tick(Direction.DOWN);
        }
        dr = dmc.getDungeonResponseModel();
        assertEquals(1, TestUtils.getInventory(dr, "key").size());
        assertEquals(2, TestUtils.getInventory(dr, "wood").size());
        assertEquals(2, TestUtils.getInventory(dr, "arrow").size());

        for (int i = 0; i < 4; i++) {
            dmc.tick(Direction.RIGHT);
        }
        dr = dmc.getDungeonResponseModel();
        assertEquals(1, TestUtils.getInventory(dr, "key").size());
        assertEquals(3, TestUtils.getInventory(dr, "wood").size());
        assertEquals(3, TestUtils.getInventory(dr, "arrow").size());

        for (int i = 0; i < 5; i++) {
            dmc.tick(Direction.UP);
        }
        // Make sure we're standing where the door is
        // and that the key is gone
        dr = dmc.getDungeonResponseModel();
        assertEquals(0, TestUtils.getInventory(dr, "key").size());
        assertEquals(3, TestUtils.getInventory(dr, "wood").size());
        assertEquals(3, TestUtils.getInventory(dr, "arrow").size());
        Position playerPosition = TestUtils.getPlayerPosition(dr);
        assertEquals(new Position(15, 9), playerPosition);

        // Continue up until we hit the wall, making sure we can't walk into it
        for (int i = 0; i < 6; i++) {
            dmc.tick(Direction.UP);
        }
        dr = dmc.getDungeonResponseModel();
        assertEquals(0, TestUtils.getInventory(dr, "key").size());
        assertEquals(3, TestUtils.getInventory(dr, "wood").size());
        assertEquals(3, TestUtils.getInventory(dr, "arrow").size());
        playerPosition = TestUtils.getPlayerPosition(dr);
        assertEquals(new Position(15, 4), playerPosition);

        // Grab the bomb.
        for (int i = 0; i < 2; i++) {
            dmc.tick(Direction.LEFT);
        }
        dr = dmc.getDungeonResponseModel();
        assertEquals(0, TestUtils.getInventory(dr, "key").size());
        assertEquals(3, TestUtils.getInventory(dr, "wood").size());
        assertEquals(3, TestUtils.getInventory(dr, "arrow").size());
        assertEquals(1, TestUtils.getInventory(dr, "bomb").size());

        // Try (and fail) to craft a shield
        assertThrows(InvalidActionException.class, () -> dmc.build("shield"));

        // Craft a bow and make sure items were consumed
        assertDoesNotThrow(() -> dmc.build("bow"));
        dr = dmc.getDungeonResponseModel();
        assertEquals(0, TestUtils.getInventory(dr, "key").size());
        assertEquals(2, TestUtils.getInventory(dr, "wood").size());
        assertEquals(0, TestUtils.getInventory(dr, "arrow").size());
        assertEquals(1, TestUtils.getInventory(dr, "bomb").size());

        // Make way to the other door
        for (int i = 0; i < 3; i++) {
            dmc.tick(Direction.LEFT);
        }
        for (int i = 0; i < 3; i++) {
            dmc.tick(Direction.DOWN);
        }
        for (int i = 0; i < 3; i++) {
            dmc.tick(Direction.LEFT);
        }
        for (int i = 0; i < 2; i++) {
            dmc.tick(Direction.DOWN);
        }
        for (int i = 0; i < 3; i++) {
            dmc.tick(Direction.LEFT);
        }

        // Go down once and make sure we don't walk into the door (since we don't have the key)
        dr = dmc.getDungeonResponseModel();
        Position prevPlayerPosition = TestUtils.getPlayerPosition(dr);
        dr = dmc.tick(Direction.DOWN);
        playerPosition = TestUtils.getPlayerPosition(dr);
        assertEquals(prevPlayerPosition, playerPosition);
    }

    private DungeonManiaController integration1() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("d_advancedIntegration", "c_potionTestConfigLength1");

        // Make sure we've got the correct number of everything before doing anything
        assertTrue(TestUtils.countEntityOfType(dr, "wall") == 114);
        assertTrue(TestUtils.countEntityOfType(dr, "wood") == 3);
        assertTrue(TestUtils.countEntityOfType(dr, "arrow") == 3);
        assertTrue(TestUtils.countEntityOfType(dr, "key") == 2);
        assertTrue(TestUtils.countEntityOfType(dr, "door") == 2);
        assertTrue(TestUtils.countEntityOfType(dr, "mercenary") == 1);
        assertTrue(TestUtils.countEntityOfType(dr, "player") == 1);
        assertTrue(TestUtils.countEntityOfType(dr, "invisibility_potion") == 1);
        assertTrue(TestUtils.countEntityOfType(dr, "invincibility_potion") == 1);

        // Check I have no items
        assertTrue(TestUtils.getInventory(dr, "").isEmpty());

        // Go right to pick up potions.
        // Make sure that the mercenary moves correct each time
        Position oldMercPosition = TestUtils.getEntities(dr, "mercenary").get(0).getPosition();
        dr = dmc.tick(Direction.RIGHT);
        Position mercPosition = TestUtils.getEntities(dr, "mercenary").get(0).getPosition();
        Position distanceBetween = Position.calculatePositionBetween(mercPosition, oldMercPosition);
        assertTrue(distanceBetween.equals(new Position(1, 0)) || distanceBetween.equals(new Position(0, 1)));

        dr = dmc.tick(Direction.RIGHT);
        mercPosition = TestUtils.getEntities(dr, "mercenary").get(0).getPosition();
        assertEquals(new Position(2, 4), mercPosition);

        assertEquals(2, TestUtils.getInventory(dr, "").size());
        assertEquals(1, TestUtils.getInventory(dr, "invisibility_potion").size());

        // Go right 4 more times, with the dude following each time
        oldMercPosition = mercPosition;
        for (int i = 0; i < 4; i++) {
            dr = dmc.tick(Direction.RIGHT);
            mercPosition = TestUtils.getEntities(dr, "mercenary").get(0).getPosition();
            System.out.println(i);
            distanceBetween = Position.calculatePositionBetween(mercPosition, oldMercPosition);
            Position exp = i == 3 ? new Position(-1, 0) : new Position(0, 1);
            assertEquals(exp, distanceBetween);
            oldMercPosition = mercPosition;
        }

        // Consume the invincibility potion and make sure merc runs away
        String invId = TestUtils.getInventory(dr, "invincibility_potion").get(0).getId();
        assertDoesNotThrow(() -> dmc.tick(invId));
        dr = dmc.getDungeonResponseModel();
        mercPosition = TestUtils.getEntities(dr, "mercenary").get(0).getPosition();
        distanceBetween = Position.calculatePositionBetween(mercPosition, oldMercPosition);
        assertEquals(new Position(1, 0), distanceBetween);

        // Run towards and kill the merc.
        // This will complete the enemy goal and damge the player down to
        dmc.tick(Direction.LEFT);
        dr = dmc.tick(Direction.LEFT);
        assertEquals(":enemies AND :treasure", dr.getGoals());
        dr = dmc.tick(Direction.LEFT);
        assertEquals(":treasure", dr.getGoals());

        // Make sure the health and deltas are as expected

        // Go to the tile above the treasure
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        for (int i = 0; i < 6; i++) {
            assertEquals(":treasure", dr.getGoals());
            dmc.tick(Direction.DOWN);
        }
        return dmc;
    }
}
