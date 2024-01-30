package amongdust;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import amongdust.response.models.DungeonResponse;
import amongdust.response.models.EntityResponse;
import amongdust.util.Direction;
import amongdust.util.Position;

import static amongdust.TestUtils.getEntities;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MovingEntityTests {
    @Test
    @DisplayName("Test if moving entity (spider/zombie toast/Mercenary) spawns on the map")
    public void testMovingEntityPlacement() {
        AmongDustController dmc = new AmongDustController();
        DungeonResponse initDungonRes = dmc.newGame("d_movingEntityTest", "c_movementTest_testMovementDown");
        List<EntityResponse> spiders = getEntities(initDungonRes, "spider");
        List<EntityResponse> mercenaries = getEntities(initDungonRes, "mercenary");
        List<EntityResponse> zombieToasts = getEntities(initDungonRes, "zombie_toast");

        assertEquals(spiders.get(0).getPosition(), new Position(4, 1));
        assertEquals(mercenaries.get(0).getPosition(), new Position(3, 5));
        assertEquals(zombieToasts.get(0).getPosition(), new Position(6, 9));
    }

    @Test
    @DisplayName("Test that zombie toast can't escape being surrounded by walls")
    public void testZombieToastWalls() {
        AmongDustController dmc = new AmongDustController();
        dmc.newGame("d_zombieToastWall", "c_movementTest_testMovementDown");

        // Move around and assert that he stays on the same square
        for (int i = 0; i < 30; i++) {
            DungeonResponse dr = dmc.tick(Direction.RIGHT);
            assertEquals(new Position(0, 0), getEntities(dr, "zombie_toast").get(0).getPosition());
        }
    }

    @Test
    @DisplayName("Test that zombie toast can push boulder")
    public void testZombieToastPushesBoulder() {
        AmongDustController dmc = new AmongDustController();
        DungeonResponse dr = dmc.newGame("d_zombieToastBoulder", "c_movementTest_testMovementDown");

        EntityResponse zombie = getEntities(dr, "zombie_toast").get(0);
        EntityResponse boulder = getEntities(dr, "boulder").get(0);

        Position zombieOrigin = zombie.getPosition();
        Position boulderOrigin = boulder.getPosition();

        dr = dmc.tick(Direction.RIGHT);

        zombie = getEntities(dr, "zombie_toast").get(0);
        boulder = getEntities(dr, "boulder").get(0);

        assertEquals(zombieOrigin.translateBy(Direction.RIGHT), zombie.getPosition());
        assertEquals(boulderOrigin.translateBy(Direction.RIGHT), boulder.getPosition());
    }

    @Test
    @DisplayName("Test that zombie toast can't use portals")
    public void testZombieToastPortal() {
        AmongDustController dmc = new AmongDustController();
        dmc.newGame("d_zombieToastPortal", "c_movementTest_testMovementDown");

        // Move around and assert that he stays on the same square
        for (int i = 0; i < 30; i++) {
            DungeonResponse dr = dmc.tick(Direction.RIGHT);
            assertEquals(new Position(0, 0), getEntities(dr, "zombie_toast").get(0).getPosition());
        }
    }

    @Test
    @DisplayName("Test that zombie toast generally moves")
    public void testZombieToastNormal() {
        AmongDustController dmc = new AmongDustController();
        dmc.newGame("d_zombieToastGeneralMovement", "c_movementTest_testMovementDown");

        // Move player around and assert that the zombie also moves
        Position oldPosition = new Position(99999, 99999); // Big numbers == won't be true first time around (hopefully)
        for (int i = 0; i < 30; i++) {
            DungeonResponse dr = dmc.tick(Direction.RIGHT);
            Position newPosition = getEntities(dr, "zombie_toast").get(0).getPosition();
            assertNotEquals(oldPosition, newPosition);
            oldPosition = newPosition;
        }
    }

    @Test
    @DisplayName("Test that zombie escapes 3 walls after 1 tick")
    public void testZombieToastCanEscape3Walls() {
        AmongDustController dmc = new AmongDustController();
        dmc.newGame("d_zombieToast3Walls", "c_movementTest_testMovementDown");

        // Tick once, don't care where player moves
        dmc.tick(Direction.UP);
        DungeonResponse dr = dmc.getDungeonResponseModel();

        assertEquals(new Position(1, 0), getEntities(dr, "zombie_toast").get(0).getPosition());
    }

    @Test
    @DisplayName("Test spiders can't move over boulders")
    public void testSpiderCantEscapeBoulders() {
        AmongDustController dmc;
        dmc = new AmongDustController();
        DungeonResponse res = dmc.newGame("d_testSpiderBoulder", "c_spiderTest_basicMovement");

        // Assert Circular Movement of Spider
        for (int i = 0; i <= 20; ++i) {
            res = dmc.tick(Direction.UP);
            boolean spiderOnZeroZero = getEntities(res, "spider").stream().anyMatch(s -> s.getPosition().equals(new Position(0, 0)));
            assertTrue(spiderOnZeroZero);
        }
    }

    @Test
    @DisplayName("Test spiders can move over basically anything that isn't a boulder")
    public void testSpiderCanMoveOverNonBoulder() {
        AmongDustController dmc;
        dmc = new AmongDustController();
        DungeonResponse res = dmc.newGame("d_testSpiderMovementGeneral", "c_spiderTest_basicMovement");
        Position pos = getEntities(res, "spider").get(0).getPosition();

        List<Position> movementTrajectory = new ArrayList<Position>();
        int x = pos.getX();
        int y = pos.getY();
        int nextPositionElement = 0;
        movementTrajectory.add(new Position(x  , y-1));
        movementTrajectory.add(new Position(x+1, y-1));
        movementTrajectory.add(new Position(x+1, y));
        movementTrajectory.add(new Position(x+1, y+1));
        movementTrajectory.add(new Position(x  , y+1));
        movementTrajectory.add(new Position(x-1, y+1));
        movementTrajectory.add(new Position(x-1, y));
        movementTrajectory.add(new Position(x-1, y-1));

        // Assert Circular Movement of Spider
        for (int i = 0; i <= 20; ++i) {
            res = dmc.tick(Direction.UP);
            assertEquals(movementTrajectory.get(nextPositionElement), getEntities(res, "spider").get(0).getPosition());

            nextPositionElement++;
            if (nextPositionElement == 8){
                nextPositionElement = 0;
            }
        }
    }

    @Test
    @DisplayName("Test spider reverses direction")
    public void testSpiderReversing() {
        AmongDustController dmc;
        dmc = new AmongDustController();
        DungeonResponse res = dmc.newGame("d_testSpiderMovementComplex", "c_spiderTest_basicMovement");
        Position pos = getEntities(res, "spider").get(0).getPosition();

        List<Position> movementTrajectory = new ArrayList<Position>();
        int x = pos.getX();
        int y = pos.getY();
        movementTrajectory.add(new Position(x  , y-1));
        movementTrajectory.add(new Position(x+1, y-1));
        movementTrajectory.add(new Position(x+1, y));
        movementTrajectory.add(new Position(x+1, y+1));
        movementTrajectory.add(new Position(x  , y+1));
        movementTrajectory.add(new Position(x-1, y+1));
        movementTrajectory.add(new Position(x-1, y));
        movementTrajectory.add(new Position(x-1, y+1));
        movementTrajectory.add(new Position(x  , y+1));
        movementTrajectory.add(new Position(x+1, y+1));
        movementTrajectory.add(new Position(x+1, y));
        movementTrajectory.add(new Position(x+1, y-1));
        movementTrajectory.add(new Position(x  , y-1));
        movementTrajectory.add(new Position(x+1, y-1));
        movementTrajectory.add(new Position(x+1, y));
        movementTrajectory.add(new Position(x+1, y+1));
        movementTrajectory.add(new Position(x  , y+1));
        movementTrajectory.add(new Position(x-1, y+1));
        movementTrajectory.add(new Position(x-1, y));
        movementTrajectory.add(new Position(x-1, y+1));

        // Assert Circular Movement of Spider
        for (int i = 0; i < 20; ++i) {
            res = dmc.tick(Direction.UP);
            assertEquals(movementTrajectory.get(i), getEntities(res, "spider").get(0).getPosition());
        }
    }

    @Test
    @DisplayName("Test spiders spawning")
    public void testSpiderSpawning() {
        AmongDustController dmc;
        dmc = new AmongDustController();
        DungeonResponse res = dmc.newGame("d_testSpiderSpawnRate", "c_spiderTest_spawnRate1");

        // Tick and make sure the spider spawns every time
        for (int i = 0; i <= 20; ++i) {
            res = dmc.tick(Direction.UP);
            int numSpiders = getEntities(res, "spider").stream().collect(Collectors.toList()).size();
            assertTrue(numSpiders == i + 1);
        }
    }

    @Test
    @DisplayName("Test spiders spawning spawnrate 2")
    public void testSpiderSpawning2() {
        AmongDustController dmc;
        dmc = new AmongDustController();
        DungeonResponse res = dmc.newGame("d_testSpiderSpawnRate", "c_spiderTest_spawnRate2");

        // Tick and make sure the spider spawns every time
        for (int i = 0; i <= 20; ++i) {
            res = dmc.tick(Direction.UP);
            res = dmc.tick(Direction.UP);
            int numSpiders = getEntities(res, "spider").stream().collect(Collectors.toList()).size();
            assertTrue(numSpiders == i + 1);
        }
    }

    @Test
    @DisplayName("Test spiders spawning spawnrate 10")
    public void testSpiderSpawning10() {
        AmongDustController dmc;
        dmc = new AmongDustController();
        DungeonResponse res = dmc.newGame("d_testSpiderSpawnRate", "c_spiderTest_spawnRate10");

        // Tick and make sure the spider spawns every time
        for (int i = 0; i <= 20; ++i) {
            for (int j = 0; j < 10; j++) {
                res = dmc.tick(Direction.UP);
            }
            int numSpiders = getEntities(res, "spider").stream().collect(Collectors.toList()).size();
            assertTrue(numSpiders == i + 1);
        }
    }

    @Test
    @DisplayName("Test that an assassin bribe fails a certain proportion of the time")
    public void testAssassinBribeRate() {
        double[] bribeRates = {0.4, 0.7};
        final int SAMPLE_SIZE = 60;
        final double TOLERANCE = 0.15;
        for (double rate : bribeRates) {
            // Create a bunch of dungeons and check how often the bribe works
            int numSuccess = 0;
            for (int i = 0; i < SAMPLE_SIZE; i++) {
                AmongDustController dmc = new AmongDustController();
                dmc.newGame("d_testAssassinBribeRate", "c_assassinBribeRate" + (int) (rate * 10));

                // Get assassin id
                DungeonResponse res = dmc.getDungeonResponseModel();
                EntityResponse er = getEntities(res, "assassin").get(0);
                assertTrue(er.isInteractable());
                String id = er.getId();

                // Pick up a single coin
                dmc.tick(Direction.RIGHT);

                // Attempt to bribe
                assertDoesNotThrow(() -> dmc.interact(id));

                // Check if it did bribe
                res = dmc.getDungeonResponseModel();
                er = getEntities(res, "assassin").get(0);
                if (!er.isInteractable()) {
                    numSuccess++;
                }
            }

            // Check if it's close to expected value
            double successRate = (double) numSuccess / SAMPLE_SIZE;
            double diff = Math.abs(successRate - rate);

            assertTrue(diff < TOLERANCE);
        }
    }
}
