package amongdust;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import amongdust.exceptions.InvalidActionException;
import amongdust.response.models.DungeonResponse;
import amongdust.response.models.EntityResponse;
import amongdust.response.models.ItemResponse;
import amongdust.util.Direction;
import amongdust.util.Position;

public class PotionTests {
    @Test
    @DisplayName("Test invincibility potion can be picked up and consumed")
    public void testInvincibilityPickupAndUse() {
        AmongDustController dmc = new AmongDustController();
        DungeonResponse dr = dmc.newGame("d_basicPotionTest", "c_potionTestConfigLength1");

        assertEquals(1, TestUtils.countEntityOfType(dr, "invincibility_potion"));

        // Pick up invincibility potion
        dmc.tick(Direction.RIGHT);
        dr = dmc.getDungeonResponseModel();
        List<ItemResponse> ir = TestUtils.getInventory(dr, "invincibility_potion");
        String potionId = ir.get(0).getId();
        assertEquals(0, TestUtils.countEntityOfType(dr, "invincibility_potion"));

        // Use it
        assertDoesNotThrow(() -> dmc.tick(potionId));

        dr = dmc.getDungeonResponseModel();
        ir = TestUtils.getInventory(dr, "invincibility_potion");
        assertEquals(0, ir.size());
        assertEquals(0, TestUtils.countEntityOfType(dr, "invincibility_potion"));
    }

    @Test
    @DisplayName("Test invisibility potion can be picked up and consumed")
    public void testInvisibilityPickupAndUse() {
        AmongDustController dmc = new AmongDustController();
        DungeonResponse dr = dmc.newGame("d_basicPotionTest", "c_potionTestConfigLength1");

        assertEquals(1, TestUtils.countEntityOfType(dr, "invisibility_potion"));

        // Pick up invisibility potion
        dmc.tick(Direction.LEFT);
        dr = dmc.getDungeonResponseModel();
        List<ItemResponse> ir = TestUtils.getInventory(dr, "invisibility_potion");
        String potionId = ir.get(0).getId();
        assertEquals(0, TestUtils.countEntityOfType(dr, "invisibility_potion"));

        // Use it
        assertDoesNotThrow(() -> dmc.tick(potionId));

        dr = dmc.getDungeonResponseModel();
        ir = TestUtils.getInventory(dr, "invisibility_potion");
        assertEquals(0, ir.size());
        assertEquals(0, TestUtils.countEntityOfType(dr, "invisibility_potion"));
    }

    @Test
    @DisplayName("Test that spelling the potion wrong throws an exception")
    public void testInvalidPotionNameThrowsException() {
        AmongDustController dmc = new AmongDustController();
        dmc.newGame("d_basicPotionTest", "c_potionTestConfigLength1");

        // Pick up both potions
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);

        List<ItemResponse> ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "invisibility_potion");
        String potionId = ir.get(0).getId();

        // Use it (but spell wrong)
        assertThrows(InvalidActionException.class, () -> dmc.tick(potionId + "oklmasdjklmajklnmd"));
    }

    @Test
    @DisplayName("Test the effects of the invincibility potion only last for a limited time")
    public void testInvincibilityDuration1() {
        AmongDustController dmc = new AmongDustController();
        dmc.newGame("d_potionTestMercenary", "c_potionTestConfigLength1");

        // Pick up both potions
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        // Use invincibility potion
        List<ItemResponse> ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "invincibility_potion");
        String potionId = ir.get(0).getId();
        assertDoesNotThrow(() -> dmc.tick(potionId));

        DungeonResponse dr = dmc.getDungeonResponseModel();
        Position oldPosition = TestUtils.getEntities(dr, "mercenary").get(0).getPosition();

        // The potion has now worn off, expect the mercenary to move in the opposite direction
        for (int i = 0; i < 20; i++) {
            dmc.tick(Direction.RIGHT);

            dr = dmc.getDungeonResponseModel();
            Position newPosition = TestUtils.getEntities(dr, "mercenary").get(0).getPosition();

            assertEquals(new Position(1, 0), Position.calculatePositionBetween(oldPosition, newPosition));
            oldPosition = newPosition;
        }
    }

    @Test
    @DisplayName("Test the effects of the invincibility potion only last for a limited time")
    public void testInvincibilityDuration5() {
        AmongDustController dmc = new AmongDustController();
        dmc.newGame("d_potionTestMercenary", "c_potionTestConfigLength5");

        // Pick up both potions
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        // Use invincibility potion
        List<ItemResponse> ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "invincibility_potion");
        String potionId = ir.get(0).getId();
        assertDoesNotThrow(() -> dmc.tick(potionId));

        DungeonResponse dr = dmc.getDungeonResponseModel();
        Position oldPosition = TestUtils.getEntities(dr, "mercenary").get(0).getPosition();
        for (int i = 0; i < 4; i++) {
            dmc.tick(Direction.RIGHT);

            dr = dmc.getDungeonResponseModel();
            Position newPosition = TestUtils.getEntities(dr, "mercenary").get(0).getPosition();

            assertEquals(new Position(-1, 0), Position.calculatePositionBetween(oldPosition, newPosition));
            oldPosition = newPosition;
        }

        // The potion has now worn off, expect the mercenary to move in the opposite direction
        for (int i = 0; i < 20; i++) {
            dmc.tick(Direction.RIGHT);

            dr = dmc.getDungeonResponseModel();
            Position newPosition = TestUtils.getEntities(dr, "mercenary").get(0).getPosition();

            assertEquals(new Position(1, 0), Position.calculatePositionBetween(oldPosition, newPosition));
            oldPosition = newPosition;
        }
    }

    @Test
    @DisplayName("Test the effects of the invincibility potion only last for a limited time")
    public void testInvincibilityDuration50() {
        AmongDustController dmc = new AmongDustController();
        dmc.newGame("d_potionTestMercenary", "c_potionTestConfigLength50");

        // Pick up both potions
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        // Use invincibility potion
        List<ItemResponse> ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "invincibility_potion");
        String potionId = ir.get(0).getId();
        assertDoesNotThrow(() -> dmc.tick(potionId));

        DungeonResponse dr = dmc.getDungeonResponseModel();
        Position oldPosition = TestUtils.getEntities(dr, "mercenary").get(0).getPosition();
        for (int i = 0; i < 49; i++) {
            dmc.tick(Direction.LEFT);

            dr = dmc.getDungeonResponseModel();
            Position newPosition = TestUtils.getEntities(dr, "mercenary").get(0).getPosition();

            assertEquals(new Position(-1, 0), Position.calculatePositionBetween(oldPosition, newPosition));
            oldPosition = newPosition;
        }

        // The potion has now worn off, expect the mercenary to move in the opposite direction
        for (int i = 0; i < 20; i++) {
            dmc.tick(Direction.RIGHT);

            dr = dmc.getDungeonResponseModel();
            Position newPosition = TestUtils.getEntities(dr, "mercenary").get(0).getPosition();

            assertEquals(new Position(1, 0), Position.calculatePositionBetween(oldPosition, newPosition));
            oldPosition = newPosition;
        }
    }

    @Test
    @DisplayName("Test the effects of the invisibility potion only last for a limited time")
    public void testInvisibilityDuration1() {
        AmongDustController dmc = new AmongDustController();
        dmc.newGame("d_potionTestMercenary", "c_potionTestConfigLength1");

        // Pick up invisibility potion
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        List<ItemResponse> ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "invisibility_potion");
        String potionId = ir.get(0).getId();

        // Use it
        assertDoesNotThrow(() -> dmc.tick(potionId));

        DungeonResponse dr = dmc.getDungeonResponseModel();
        Position oldPosition = TestUtils.getEntities(dr, "mercenary").get(0).getPosition();

        // The potion has now worn off, expect the mercenary to move in the opposite direction
        for (int i = 0; i < 20; i++) {
            dmc.tick(Direction.RIGHT);

            dr = dmc.getDungeonResponseModel();
            Position newPosition = TestUtils.getEntities(dr, "mercenary").get(0).getPosition();

            assertEquals(new Position(1, 0), Position.calculatePositionBetween(oldPosition, newPosition));
            oldPosition = newPosition;
        }
    }

    @Test
    @DisplayName("Test the effects of the invisibility potion only last for a limited time")
    public void testInvisibilityDuration5() {
        AmongDustController dmc = new AmongDustController();
        dmc.newGame("d_potionTestMercenary", "c_potionTestConfigLength5");

        // Pick up invisibility potion
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        List<ItemResponse> ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "invisibility_potion");
        String potionId = ir.get(0).getId();

        // Use it
        assertDoesNotThrow(() -> dmc.tick(potionId));

        for (int i = 0; i < 4; i++) {
            dmc.tick(Direction.RIGHT);
        }

        DungeonResponse dr = dmc.getDungeonResponseModel();
        Position oldPosition = TestUtils.getEntities(dr, "mercenary").get(0).getPosition();

        int rightCounter = 0;
        // The potion has now worn off, expect the mercenary to move in the opposite direction
        for (int i = 0; i < 20; i++) {
            dmc.tick(Direction.RIGHT);

            dr = dmc.getDungeonResponseModel();
            Position newPosition = TestUtils.getEntities(dr, "mercenary").get(0).getPosition();

            rightCounter += Position.calculatePositionBetween(oldPosition, newPosition).equals(new Position(1, 0)) ? 1 : 0;
            oldPosition = newPosition;
        }
        assertTrue(rightCounter >= 16);
    }

    // @Test
    // @DisplayName("Test the effects of the invisibility potion only last for a limited time")
    // public void testInvisibilityDuration50() {
    //     AmongDustController dmc = new AmongDustController();
    //     dmc.newGame("d_potionTestMercenary", "c_potionTestConfigLength50");

    //     // Pick up invisibility potion
    //     dmc.tick(Direction.RIGHT);
    //     dmc.tick(Direction.RIGHT);
    //     List<ItemResponse> ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "invisibility_potion");
    //     String potionId = ir.get(0).getId();

    //     // Use it
    //     assertDoesNotThrow(() -> dmc.tick(potionId));

    //     for (int i = 0; i < 49; i++) {
    //         dmc.tick(Direction.RIGHT);
    //     }

    //     DungeonResponse dr = dmc.getDungeonResponseModel();
    //     Position oldPosition = TestUtils.getEntities(dr, "mercenary").get(0).getPosition();

    //     // The potion has now worn off, expect the mercenary to move in the opposite direction
    //     for (int i = 0; i < 20; i++) {
    //         dmc.tick(Direction.LEFT);

    //         dr = dmc.getDungeonResponseModel();
    //         Position newPosition = TestUtils.getEntities(dr, "mercenary").get(0).getPosition();

    //         assertEquals(new Position(1, 0), Position.calculatePositionBetween(oldPosition, newPosition));
    //         oldPosition = newPosition;
    //     }
    // }

    @Test
    @DisplayName("Test that spiders movement remains unchanged with an invincibility potion")
    public void testInvincibilitySpiderMovement() {
        AmongDustController dmc = new AmongDustController();
        DungeonResponse dr = dmc.newGame("d_potionTestSpider", "c_potionTestConfigLength5");

        Position pos = TestUtils.getEntities(dr, "spider").get(0).getPosition();

        List<Position> movementTrajectory = new ArrayList<Position>();
        int x = pos.getX();
        int y = pos.getY();
        int nextPositionElement = 0;
        movementTrajectory.add(new Position(x+1, y+1));
        movementTrajectory.add(new Position(x  , y+1));
        movementTrajectory.add(new Position(x-1, y+1));
        movementTrajectory.add(new Position(x-1, y));
        movementTrajectory.add(new Position(x-1, y-1));
        movementTrajectory.add(new Position(x  , y-1));
        movementTrajectory.add(new Position(x+1, y-1));
        movementTrajectory.add(new Position(x+1, y));

        // Pick up both potions
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        // Use invincibility potion
        List<ItemResponse> ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "invincibility_potion");
        String potionId = ir.get(0).getId();
        assertDoesNotThrow(() -> dmc.tick(potionId));

        // Assert Circular Movement of Spider
        for (int i = 0; i <= 20; ++i) {
            dr = dmc.tick(Direction.UP);
            assertEquals(movementTrajectory.get(nextPositionElement), TestUtils.getEntities(dr, "spider").get(0).getPosition());

            nextPositionElement++;
            if (nextPositionElement == 8){
                nextPositionElement = 0;
            }
        }
    }

    @Test
    @DisplayName("Test that spiders movement remains unchanged with an invisibility potion")
    public void testInvisibilitySpiderMovement() {
        AmongDustController dmc = new AmongDustController();
        DungeonResponse dr = dmc.newGame("d_potionTestSpider", "c_potionTestConfigLength5");

        Position pos = TestUtils.getEntities(dr, "spider").get(0).getPosition();

        List<Position> movementTrajectory = new ArrayList<Position>();
        int x = pos.getX();
        int y = pos.getY();
        int nextPositionElement = 0;
        movementTrajectory.add(new Position(x+1, y+1));
        movementTrajectory.add(new Position(x  , y+1));
        movementTrajectory.add(new Position(x-1, y+1));
        movementTrajectory.add(new Position(x-1, y));
        movementTrajectory.add(new Position(x-1, y-1));
        movementTrajectory.add(new Position(x  , y-1));
        movementTrajectory.add(new Position(x+1, y-1));
        movementTrajectory.add(new Position(x+1, y));

        // Pick up both potions
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        // Use invisibility potion
        List<ItemResponse> ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "invisibility_potion");
        String potionId = ir.get(0).getId();
        assertDoesNotThrow(() -> dmc.tick(potionId));

        // Assert Circular Movement of Spider
        for (int i = 0; i <= 20; ++i) {
            dr = dmc.tick(Direction.UP);
            assertEquals(movementTrajectory.get(nextPositionElement), TestUtils.getEntities(dr, "spider").get(0).getPosition());

            nextPositionElement++;
            if (nextPositionElement == 8){
                nextPositionElement = 0;
            }
        }
    }

    @Test
    @DisplayName("Test that zombie toast's movement changes to running away with an invincibility potion")
    public void testInvincibilityZombieMovement() {
        AmongDustController dmc = new AmongDustController();
        dmc.newGame("d_potionTestZombie", "c_potionTestConfigLength50");

        // Pick up both potions
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        // Use invincibility potion
        List<ItemResponse> ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "invincibility_potion");
        String potionId = ir.get(0).getId();
        assertDoesNotThrow(() -> dmc.tick(potionId));

        DungeonResponse dr = dmc.getDungeonResponseModel();
        Position oldPosition = TestUtils.getEntities(dr, "zombie_toast").get(0).getPosition();
        for (int i = 0; i < 49; i++) {
            dmc.tick(Direction.RIGHT);

            dr = dmc.getDungeonResponseModel();
            Position newPosition = TestUtils.getEntities(dr, "zombie_toast").get(0).getPosition();

            assertEquals(new Position(-1, 0), Position.calculatePositionBetween(oldPosition, newPosition));
            oldPosition = newPosition;
        }
    }

    @Test
    @DisplayName("Test that zombie toast's movement remains unchanged with an invisibility potion")
    public void testInvisibilityZombieMovement() {
        AmongDustController dmc = new AmongDustController();
        dmc.newGame("d_potionTestZombie", "c_potionTestConfigLength5");

        // Pick up both potions
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        // Use invisibility potion
        List<ItemResponse> ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "invisibility_potion");
        String potionId = ir.get(0).getId();
        assertDoesNotThrow(() -> dmc.tick(potionId));

        DungeonResponse dr = dmc.getDungeonResponseModel();
        Position oldPosition = TestUtils.getEntities(dr, "zombie_toast").get(0).getPosition();
        for (int i = 0; i < 50; i++) {
            dmc.tick(Direction.RIGHT);
        }

        dr = dmc.getDungeonResponseModel();
        Position newPosition = TestUtils.getEntities(dr, "zombie_toast").get(0).getPosition();

        Position delta = Position.calculatePositionBetween(oldPosition, newPosition);
        int tolerance = 18;
        assertTrue(Math.abs(delta.getX()) < tolerance);
        assertTrue(Math.abs(delta.getY()) < tolerance);
    }

    @Test
    @DisplayName("Test that mercenary's movement changes to running away with an invincibility potion")
    public void testInvincibilityMercenaryMovement() {
        AmongDustController dmc = new AmongDustController();
        dmc.newGame("d_potionTestMercenary", "c_potionTestConfigLength50");

        // Pick up both potions
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        // Use invincibility potion
        List<ItemResponse> ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "invincibility_potion");
        String potionId = ir.get(0).getId();
        assertDoesNotThrow(() -> dmc.tick(potionId));

        DungeonResponse dr = dmc.getDungeonResponseModel();
        Position oldPosition = TestUtils.getEntities(dr, "mercenary").get(0).getPosition();
        for (int i = 0; i < 49; i++) {
            dmc.tick(Direction.RIGHT);

            dr = dmc.getDungeonResponseModel();
            Position newPosition = TestUtils.getEntities(dr, "mercenary").get(0).getPosition();

            assertEquals(new Position(-1, 0), Position.calculatePositionBetween(oldPosition, newPosition));
            oldPosition = newPosition;
        }
    }

    @Test
    @DisplayName("Test that mercenary's run away downards when using an invincibility potion above them")
    public void testInvincibilityMercenaryMovementDown() {
        AmongDustController dmc = new AmongDustController();
        dmc.newGame("d_potionTestMercenaryDown", "c_potionTestConfigLength50");

        // Pick up both potions
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);

        // Use invincibility potion
        List<ItemResponse> ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "invincibility_potion");
        String potionId = ir.get(0).getId();
        assertDoesNotThrow(() -> dmc.tick(potionId));

        DungeonResponse dr = dmc.getDungeonResponseModel();
        Position oldPosition = TestUtils.getEntities(dr, "mercenary").get(0).getPosition();
        for (int i = 0; i < 49; i++) {
            dmc.tick(Direction.DOWN);

            dr = dmc.getDungeonResponseModel();
            Position newPosition = TestUtils.getEntities(dr, "mercenary").get(0).getPosition();

            assertEquals(new Position(0, 1), Position.calculatePositionBetween(oldPosition, newPosition));
            oldPosition = newPosition;
        }
    }

    @Test
    @DisplayName("Test that mercenary's run away upwards when using an invincibility potion below them")
    public void testInvincibilityMercenaryMovementUp() {
        AmongDustController dmc = new AmongDustController();
        dmc.newGame("d_potionTestMercenaryUp", "c_potionTestConfigLength50");

        // Pick up both potions
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);

        // Use invincibility potion
        List<ItemResponse> ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "invincibility_potion");
        String potionId = ir.get(0).getId();
        assertDoesNotThrow(() -> dmc.tick(potionId));

        DungeonResponse dr = dmc.getDungeonResponseModel();
        Position oldPosition = TestUtils.getEntities(dr, "mercenary").get(0).getPosition();
        for (int i = 0; i < 49; i++) {
            dmc.tick(Direction.UP);

            dr = dmc.getDungeonResponseModel();
            Position newPosition = TestUtils.getEntities(dr, "mercenary").get(0).getPosition();

            assertEquals(new Position(0, -1), Position.calculatePositionBetween(oldPosition, newPosition));
            oldPosition = newPosition;
        }
    }

    @Test
    @DisplayName("Test that a mercenary's movement changes to random with an invisibility potion")
    public void testInvisibilityMercenaryMovement() {
        AmongDustController dmc = new AmongDustController();
        DungeonResponse dr = dmc.newGame("d_potionTestMercenary", "c_potionTestConfigLength50");

        // Bribe merc
        EntityResponse merc = TestUtils.getEntities(dr, "mercenary").get(0);
        assertDoesNotThrow(() -> dmc.interact(merc.getId()));

        // Pick up both potions
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        // Use invisibility potion
        List<ItemResponse> ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "invisibility_potion");
        String potionId = ir.get(0).getId();
        assertDoesNotThrow(() -> dmc.tick(potionId));

        dr = dmc.getDungeonResponseModel();
        Position oldPosition = TestUtils.getEntities(dr, "mercenary").get(0).getPosition();
        for (int i = 0; i < 49; i++) {
            dmc.tick(Direction.RIGHT);
        }

        dr = dmc.getDungeonResponseModel();
        Position newPosition = TestUtils.getEntities(dr, "mercenary").get(0).getPosition();

        Position delta = Position.calculatePositionBetween(oldPosition, newPosition);
        int tolerance = 18;
        assertTrue(Math.abs(delta.getX()) < tolerance);
        assertTrue(Math.abs(delta.getY()) < tolerance);
    }

    @Test
    @DisplayName("Test that a bribed mercenary's movement changes to random with an invisibility potion")
    public void testInvisibilityBribedMercenaryMovement() {
        AmongDustController dmc = new AmongDustController();
        DungeonResponse dr = dmc.newGame("d_potionTestMercenary", "c_potionTestConfigLength50");

        // Bribe merc
        EntityResponse merc = TestUtils.getEntities(dr, "mercenary").get(0);
        assertDoesNotThrow(() -> dmc.interact(merc.getId()));

        // Pick up invisibility potions
        dmc.tick(Direction.UP);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dr = dmc.tick(Direction.DOWN);

        // Use invisibility potion
        List<ItemResponse> ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "invisibility_potion");
        String potionId = ir.get(0).getId();
        assertDoesNotThrow(() -> dmc.tick(potionId));

        dr = dmc.getDungeonResponseModel();
        Position oldPosition = TestUtils.getEntities(dr, "mercenary").get(0).getPosition();
        for (int i = 0; i < 49; i++) {
            dmc.tick(Direction.RIGHT);
        }

        dr = dmc.getDungeonResponseModel();
        Position newPosition = TestUtils.getEntities(dr, "mercenary").get(0).getPosition();

        Position delta = Position.calculatePositionBetween(oldPosition, newPosition);
        int tolerance = 18;
        assertTrue(Math.abs(delta.getX()) < tolerance);
        assertTrue(Math.abs(delta.getY()) < tolerance);
    }

    @Test
    @DisplayName("Test that an assassin's movement changes to follows the player with an invisibility potion when inside of range")
    public void testInvisibilityAssassinMovementInRange() {
        AmongDustController dmc = new AmongDustController();
        dmc.newGame("d_potionTestAssassin", "c_assassinRange3");

        // Pick up potion
        dmc.tick(Direction.RIGHT);

        // Use invincibility potion
        List<ItemResponse> ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "invisibility_potion");
        String potionId = ir.get(0).getId();
        assertDoesNotThrow(() -> dmc.tick(potionId));

        DungeonResponse dr = dmc.getDungeonResponseModel();
        Position oldPosition = TestUtils.getEntities(dr, "assassin").get(0).getPosition();
        for (int i = 0; i < 49; i++) {
            dmc.tick(Direction.RIGHT);

            dr = dmc.getDungeonResponseModel();
            Position newPosition = TestUtils.getEntities(dr, "assassin").get(0).getPosition();

            assertEquals(new Position(1, 0), Position.calculatePositionBetween(oldPosition, newPosition));
            oldPosition = newPosition;
        }
    }

    @Test
    @DisplayName("Test that an assassin's movement changes to random with an invisibility potion when outside of range")
    public void testInvisibilityAssassinMovementOutsideRange() {
        AmongDustController dmc = new AmongDustController();
        DungeonResponse dr = dmc.newGame("d_potionTestAssassin", "c_assassinRange2");

        // Pick up invisibility potions
        dmc.tick(Direction.RIGHT);

        // Use invisibility potion
        List<ItemResponse> ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "invisibility_potion");
        String potionId = ir.get(0).getId();
        assertDoesNotThrow(() -> dmc.tick(potionId));

        dr = dmc.getDungeonResponseModel();
        Position oldPosition = TestUtils.getEntities(dr, "assassin").get(0).getPosition();
        for (int i = 0; i < 49; i++) {
            dmc.tick(Direction.RIGHT);
        }

        dr = dmc.getDungeonResponseModel();
        Position newPosition = TestUtils.getEntities(dr, "assassin").get(0).getPosition();

        Position delta = Position.calculatePositionBetween(oldPosition, newPosition);
        int tolerance = 18;
        assertTrue(Math.abs(delta.getX()) < tolerance);
        assertTrue(Math.abs(delta.getY()) < tolerance);
    }

    @Test
    @DisplayName("Test that a bribed mercenary's movement is stil following with an invincibility potion")
    public void testInvincibilityyBribedMercenaryMovement() {
        AmongDustController dmc = new AmongDustController();
        DungeonResponse dr = dmc.newGame("d_potionTestMercenary", "c_potionTestConfigLength50");

        // Bribe merc
        EntityResponse merc = TestUtils.getEntities(dr, "mercenary").get(0);
        assertDoesNotThrow(() -> dmc.interact(merc.getId()));

        // Pick up invincibility potion
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.UP);

        // Use invincibility potion
        List<ItemResponse> ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "invincibility_potion");
        String potionId = ir.get(0).getId();
        assertDoesNotThrow(() -> dmc.tick(potionId));

        Position oldPosition = TestUtils.getEntities(dr, "mercenary").get(0).getPosition();
        Position oldPlayerPosition = TestUtils.getPlayerPosition(dr);

        // Ensure mercenary is still following
        for (int i = 0; i < 50; i++) {
            dr = dmc.tick(Direction.RIGHT);
        }

        dr = dmc.getDungeonResponseModel();
        Position playerPosition = TestUtils.getPlayerPosition(dr);
        Position newPosition = TestUtils.getEntities(dr, "mercenary").get(0).getPosition();

        // Distance between merc and player before moving must be approximately the same as after, if it's following
        assertTrue(Math.abs(oldPlayerPosition.getDistanceTo(oldPosition) - playerPosition.getDistanceTo(newPosition)) <= 2);
        oldPosition = newPosition;
    }

    @Test
    @DisplayName("Test that potions are queued correctly")
    public void testPotionQueue() {
        AmongDustController dmc = new AmongDustController();
        dmc.newGame("d_potionTestMercenary", "c_potionTestConfigLength5");

        // Pick up both potion
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        List<ItemResponse> ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "invisibility_potion");
        String invisibilityId = ir.get(0).getId();
        ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "invincibility_potion");
        String invincibilityId = ir.get(0).getId();

        // Use both
        assertDoesNotThrow(() -> dmc.tick(invisibilityId));

        DungeonResponse dr = dmc.getDungeonResponseModel();
        Position oldPosition = TestUtils.getEntities(dr, "mercenary").get(0).getPosition();

        assertDoesNotThrow(() -> dmc.tick(invincibilityId));

        for (int i = 0; i < 3; i++) {
            dmc.tick(Direction.RIGHT);
        }

        dr = dmc.getDungeonResponseModel();
        Position newPosition = TestUtils.getEntities(dr, "mercenary").get(0).getPosition();

        Position delta = Position.calculatePositionBetween(oldPosition, newPosition);
        int tolerance = 18;
        assertTrue(Math.abs(delta.getX()) < tolerance);
        assertTrue(Math.abs(delta.getY()) < tolerance);

        oldPosition = newPosition;

        // The potion has now worn off, expect the mercenary to move in the opposite direction
        for (int i = 0; i < 5; i++) {
            dmc.tick(Direction.RIGHT);

            dr = dmc.getDungeonResponseModel();
            newPosition = TestUtils.getEntities(dr, "mercenary").get(0).getPosition();

            assertEquals(new Position(-1, 0), Position.calculatePositionBetween(oldPosition, newPosition));
            oldPosition = newPosition;
        }

        // Ran out of invincibility, mercenary should track player.
        dmc.tick(Direction.RIGHT);

        dr = dmc.getDungeonResponseModel();
        newPosition = TestUtils.getEntities(dr, "mercenary").get(0).getPosition();

        assertEquals(new Position(1, 0), Position.calculatePositionBetween(oldPosition, newPosition));
    }
}
