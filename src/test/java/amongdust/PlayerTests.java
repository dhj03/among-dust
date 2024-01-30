package amongdust;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import amongdust.response.models.DungeonResponse;
import amongdust.response.models.EntityResponse;
import amongdust.response.models.ItemResponse;
import amongdust.util.Direction;
import amongdust.util.Position;

import static amongdust.TestUtils.getPlayer;

public class PlayerTests {

    @Test
    @DisplayName("Test if player appears on the map")
    public void testBasicPlayerPlacement() {
        AmongDustController dmc = new AmongDustController();
        // Make player at (0, 0)
        DungeonResponse dr = dmc.newGame("d_basicPlayerTest00", "c_movementTest_testMovementDown");
        assertEquals(1, dr.getEntities().size());
        EntityResponse initPlayer00 = getPlayer(dr).get();

        // Make sure Player is found at (0, 0)
        EntityResponse expectedPlayer00 = new EntityResponse(initPlayer00.getId(), initPlayer00.getType(), new Position(0, 0), false);
        assertEquals(initPlayer00, expectedPlayer00);

        // Make new Dungeon with player at (5, 7)
        DungeonResponse dr2 = dmc.newGame("d_basicPlayerTest57", "c_movementTest_testMovementDown");
        assertEquals(1, dr2.getEntities().size());
        EntityResponse initPlayer57 = getPlayer(dr2).get();

        // Make sure Player is found at (5, 7)
        EntityResponse expectedPlayer57 = new EntityResponse(initPlayer57.getId(), initPlayer57.getType(), new Position(5, 7), false);
        assertEquals(initPlayer57, expectedPlayer57);
    }


    // Movement Paths for Testing
    public static TestPath[] movementSingle() {
        return new TestPath[] {
            new TestPath(new ArrayList<Direction>(Arrays.asList(Direction.UP)), new Position(0, -1)),
            new TestPath(new ArrayList<Direction>(Arrays.asList(Direction.DOWN)), new Position(0, 1)),
            new TestPath(new ArrayList<Direction>(Arrays.asList(Direction.LEFT)), new Position(-1, 0)),
            new TestPath(new ArrayList<Direction>(Arrays.asList(Direction.RIGHT)), new Position(1, 0))
        };
    }

    @ParameterizedTest
    @MethodSource(value = "movementSingle")
    @DisplayName("Test Player Movement single unit")
    public void testPlayerMovementSingle(TestPath path) {
        playerMovementTester(path);
    }





    public static TestPath[] movementLine() {
        return new TestPath[] {
            new TestPath(new ArrayList<Direction>(Arrays.asList(Direction.UP, Direction.UP, Direction.UP, Direction.UP, Direction.UP, Direction.UP)), new Position(0, -6)),
            new TestPath(new ArrayList<Direction>(Arrays.asList(Direction.DOWN, Direction.DOWN, Direction.DOWN, Direction.DOWN, Direction.DOWN, Direction.DOWN)), new Position(0, 6)),
            new TestPath(new ArrayList<Direction>(Arrays.asList(Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.RIGHT)), new Position(6, 0)),
            new TestPath(new ArrayList<Direction>(Arrays.asList(Direction.LEFT, Direction.LEFT, Direction.LEFT, Direction.LEFT, Direction.LEFT, Direction.LEFT)), new Position(-6, 0))
        };
    }

    @ParameterizedTest
    @MethodSource(value = "movementLine")
    @DisplayName("Test Player Movement in single line")
    public void testPlayerMovementLine(TestPath path) {
        playerMovementTester(path);
    }





    public static TestPath[] movementCircle() {
        return new TestPath[] {
            new TestPath(new ArrayList<Direction>(Arrays.asList(Direction.UP, Direction.LEFT, Direction.DOWN, Direction.RIGHT)), new Position(0, 0)),
            new TestPath(new ArrayList<Direction>(Arrays.asList(Direction.UP, Direction.UP, Direction.LEFT, Direction.DOWN, Direction.DOWN, Direction.RIGHT)), new Position(0, 0)),
            new TestPath(new ArrayList<Direction>(Arrays.asList(Direction.DOWN, Direction.RIGHT, Direction.RIGHT, Direction.UP, Direction.LEFT, Direction.LEFT)), new Position(0, 0)),
            new TestPath(new ArrayList<Direction>(Arrays.asList(Direction.RIGHT, Direction.RIGHT, Direction.DOWN, Direction.LEFT, Direction.LEFT, Direction.UP)), new Position(0, 0))
        };
    }

    @ParameterizedTest
    @MethodSource(value = "movementCircle")
    @DisplayName("Test Player Movement in a small loop")
    public void testPlayerMovementCircle(TestPath path) {
        playerMovementTester(path);
    }







    public static TestPath[] movementCircleLarge() {
        return new TestPath[] {
            new TestPath(new ArrayList<Direction>(
                Arrays.asList(Direction.UP, Direction.UP, Direction.UP, Direction.LEFT, Direction.LEFT, Direction.LEFT, Direction.DOWN, Direction.DOWN, Direction.DOWN, Direction.RIGHT, Direction.RIGHT, Direction.RIGHT)),
                new Position(0, 0)),
            new TestPath(new ArrayList<Direction>(
                Arrays.asList(Direction.LEFT, Direction.LEFT, Direction.LEFT, Direction.DOWN, Direction.DOWN, Direction.DOWN, Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.UP, Direction.UP, Direction.UP)),
                new Position(0, 0)),
            new TestPath(new ArrayList<Direction>(
                Arrays.asList(Direction.LEFT, Direction.LEFT, Direction.LEFT, Direction.DOWN, Direction.DOWN, Direction.DOWN, Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.UP, Direction.UP, Direction.UP)),
                new Position(0, 0)),
            new TestPath(new ArrayList<Direction>(
                Arrays.asList(Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.UP, Direction.UP, Direction.UP, Direction.LEFT, Direction.LEFT, Direction.LEFT, Direction.DOWN, Direction.DOWN, Direction.DOWN)),
                new Position(0, 0))
        };
    }

    @ParameterizedTest
    @MethodSource(value = "movementCircleLarge")
    @DisplayName("Test Player Movement in a large loop")
    public void testPlayerMovementCircleLarge(TestPath path) {
        playerMovementTester(path);
    }





    public static TestPath[] movementL() {
        return new TestPath[] {
            new TestPath(new ArrayList<Direction>(Arrays.asList(Direction.UP, Direction.UP, Direction.LEFT, Direction.LEFT, Direction.LEFT, Direction.LEFT)), new Position(-4, -2)),
            new TestPath(new ArrayList<Direction>(Arrays.asList(Direction.DOWN, Direction.LEFT, Direction.LEFT, Direction.LEFT, Direction.LEFT, Direction.LEFT)), new Position(-5, 1)),
            new TestPath(new ArrayList<Direction>(Arrays.asList(Direction.LEFT, Direction.LEFT, Direction.UP, Direction.UP, Direction.UP, Direction.UP)), new Position(-2, -4)),
            new TestPath(new ArrayList<Direction>(Arrays.asList(Direction.RIGHT, Direction.RIGHT, Direction.DOWN, Direction.DOWN, Direction.DOWN, Direction.DOWN)), new Position(2, 4))
        };
    }

    @ParameterizedTest
    @MethodSource(value = "movementL")
    @DisplayName("Test Player Movement in an L shape")
    public void testPlayerMovementLShape(TestPath path) {
        playerMovementTester(path);
    }





    public static TestPath[] movementBack() {
        return new TestPath[] {
            new TestPath(new ArrayList<Direction>(Arrays.asList(Direction.UP, Direction.UP, Direction.UP, Direction.DOWN, Direction.DOWN, Direction.DOWN)), new Position(0, 0)),
            new TestPath(new ArrayList<Direction>(Arrays.asList(Direction.DOWN, Direction.DOWN, Direction.DOWN, Direction.UP, Direction.UP, Direction.UP)), new Position(0, 0)),
            new TestPath(new ArrayList<Direction>(Arrays.asList(Direction.LEFT, Direction.LEFT, Direction.LEFT, Direction.RIGHT, Direction.RIGHT, Direction.RIGHT)), new Position(0, 0)),
            new TestPath(new ArrayList<Direction>(Arrays.asList(Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.LEFT, Direction.LEFT, Direction.LEFT)), new Position(0, 0))
        };
    }
    @ParameterizedTest
    @MethodSource(value = "movementBack")
    @DisplayName("Test Player Movement backwards")
    public void testPlayerMovementBack(TestPath path) {
        playerMovementTester(path);
    }

    public static TestPath[] randomMovementSurroundedByWalls() {
        return new TestPath[] {
            new TestPath(randomDirectionList(20), new Position(0, 0)),
            new TestPath(randomDirectionList(50), new Position(0, 0)),
            new TestPath(randomDirectionList(100), new Position(0, 0))
        };
    }
    @ParameterizedTest
    @MethodSource(value = "randomMovementSurroundedByWalls")
    @DisplayName("Test Player can't move through walls")
    public void testPlayerMovementSurroundedByWalls(TestPath path) {
        playerMovementTester(path, "d_surroundedByWallsTest");
    }

    @Test
    @DisplayName("Test Player can pick up 1 item")
    public void testBasicItemPickup() {
        AmongDustController dmc = new AmongDustController();
        dmc.newGame("d_basicCollectiblePickupTest", "c_movementTest_testMovementDown");

        for (int i = 0; i < 2; i++) {
            dmc.tick(Direction.RIGHT);
        }

        List<ItemResponse> ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "arrow");
        assertEquals(1, ir.size());
    }

    @Test
    @DisplayName("Test Player can pick up 2 of the same item")
    public void testBasicMultipleSameItemPickup() {
        AmongDustController dmc = new AmongDustController();
        dmc.newGame("d_basicCollectiblePickupTest", "c_movementTest_testMovementDown");

        for (int i = 0; i < 3; i++) {
            dmc.tick(Direction.RIGHT);
        }

        List<ItemResponse> ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "arrow");
        assertEquals(2, ir.size());
    }

    @Test
    @DisplayName("Test Player can pick up 2 different items")
    public void testBasicMultipleItemPickup() {
        AmongDustController dmc = new AmongDustController();
        dmc.newGame("d_basicCollectiblePickupTest", "c_movementTest_testMovementDown");

        for (int i = 0; i < 4; i++) {
            dmc.tick(Direction.RIGHT);
        }

        List<ItemResponse> ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "arrow");
        assertEquals(2, ir.size());
        ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "wood");
        assertEquals(1, ir.size());
    }

    @Test
    @DisplayName("Test Player can pick up a bunch of items")
    public void testComplexItemPickup() {
        AmongDustController dmc = new AmongDustController();
        dmc.newGame("d_basicCollectiblePickupTest", "c_movementTest_testMovementDown");

        for (int i = 0; i < 6; i++) {
            dmc.tick(Direction.RIGHT);
        }

        List<ItemResponse> ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "arrow");
        assertEquals(2, ir.size());
        ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "wood");
        assertEquals(1, ir.size());
        ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "wood");
        assertEquals(1, ir.size());
    }

    @Test
    @DisplayName("Test Player can pick up an item in a wall")
    public void testItemPickupInWall() {
        AmongDustController dmc = new AmongDustController();
        dmc.newGame("d_basicCollectiblePickupTest", "c_movementTest_testMovementDown");

        dmc.tick(Direction.UP);

        List<ItemResponse> ir = TestUtils.getInventory(dmc.getDungeonResponseModel(), "sword");
        assertEquals(0, ir.size());
    }



    // Helper Testing Function
    private void playerMovementTester(TestPath path) {
        playerMovementTester(path, "d_basicPlayerTest00");
    }

    private void playerMovementTester(TestPath path, String dungeonName) {
        AmongDustController dmc = new AmongDustController();
        dmc.newGame(dungeonName, "c_movementTest_testMovementDown");

        EntityResponse actualPlayer = null;

        // move player through path
        for (Direction dir : path.getDirections()) {
            DungeonResponse actualDungonRes = dmc.tick(dir);
            actualPlayer = getPlayer(actualDungonRes).get();
        }

        // assert after movement
        assertEquals(path.getExpectedRelPosition(), actualPlayer.getPosition());
    }

    private static List<Direction> randomDirectionList(int pathLength) {
        final Direction[] d = {
            Direction.UP,
            Direction.DOWN,
            Direction.LEFT,
            Direction.RIGHT
        };

        Random r = new Random();
        List<Direction> directionList = new ArrayList<>();
        for (int i = 0; i < pathLength; i++) {
            int randomIndex = Math.abs(r.nextInt() % 4);
            directionList.add(d[randomIndex]);
        }
        return directionList;
    }
}
