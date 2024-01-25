package dungeonmania;

import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getPlayer;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class MercenaryTests {
    @Test
    @DisplayName("Test the mercenary moves straight towards the player")
    public void testMercenaryMovesStraightTowardsPlayer() {
        testMercenaryMovesStraightTowardsPlayer(Direction.LEFT, "d_mercTest_leftMovement");
        testMercenaryMovesStraightTowardsPlayer(Direction.RIGHT, "d_mercTest_rightMovement");
        testMercenaryMovesStraightTowardsPlayer(Direction.UP, "d_mercTest_upMovement");
        testMercenaryMovesStraightTowardsPlayer(Direction.DOWN, "d_mercTest_downMovement");
    }

    private void testMercenaryMovesStraightTowardsPlayer(Direction dir, String dungeonName) {
        Position playerOrigin = new Position(0, 0);
        Position mercOrigin = playerOrigin.translateBy(-dir.getOffset().getX(), -dir.getOffset().getY());

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame(dungeonName, "c_battleTests_basicMercenaryMercenaryDies");

        EntityResponse player = getPlayer(dr).get();
        assertEquals(playerOrigin, player.getPosition());

        List<EntityResponse> mercenaries = getEntities(dr, "mercenary");
        assertEquals(1, mercenaries.size());
        assertEquals(mercOrigin, mercenaries.get(0).getPosition());

        for (int i = 1; i <= 10; i++) {
            dmc.tick(dir);
            dr = dmc.getDungeonResponseModel();

            Position translation = new Position(dir.getOffset().getX() * i, dir.getOffset().getY() * i);

            player = getPlayer(dr).get();
            assertEquals(playerOrigin.translateBy(translation), player.getPosition());

            mercenaries = getEntities(dr, "mercenary");
            assertEquals(mercOrigin.translateBy(translation), mercenaries.get(0).getPosition());
        }
    }

    @Test
    @DisplayName("Test the mercenary moves diagonally towards the player")
    public void testMercenaryMovesDiagonallyTowardsPlayer() {
        Position playerOrigin = new Position(0, 0);
        Position mercOrigin = playerOrigin.translateBy(-5, -5);

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("d_mercTest_diagonalMovement", "c_battleTests_basicMercenaryMercenaryDies");

        EntityResponse player = getPlayer(dr).get();
        assertEquals(playerOrigin, player.getPosition());

        List<EntityResponse> mercenaries = getEntities(dr, "mercenary");
        assertEquals(1, mercenaries.size());
        assertEquals(mercOrigin, mercenaries.get(0).getPosition());

        double dist = calculateDistanceBetween(playerOrigin, mercOrigin);
        for (int i = 1; i <= 2; i++) {
            dr = dmc.tick(Direction.UP);

            // Player will be moving into a wall and thus cannot move.
            player = getPlayer(dr).get();
            assertEquals(playerOrigin, player.getPosition());

            mercenaries = getEntities(dr, "mercenary");
            double newDist = calculateDistanceBetween(playerOrigin, mercenaries.get(0).getPosition());
            assertTrue(dist > newDist);
            dist = newDist;
        }
    }

    private double calculateDistanceBetween(Position p1, Position p2) {
        double a = p2.getX() - p1.getX();
        double b = p2.getY() - p1.getY();
        return Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
    }

    @Test
    @DisplayName("Test the mercenary moves around a vertical wall to get to the player")
    public void testMercenaryMovesAroundVerticalWall() {
        /*
         *  [  ]   [  ]   [  ]
         *  merc   wall  player
         *  [  ]   [  ]   [  ]
         */

        Position playerOrigin = new Position(0, 0);
        Position mercOrigin = playerOrigin.translateBy(-2, 0);

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("d_mercTest_wallMovementV", "c_battleTests_basicMercenaryMercenaryDies");

        assertEquals(playerOrigin, getPlayer(dr).get().getPosition());

        List<EntityResponse> mercenaries = getEntities(dr, "mercenary");
        assertEquals(1, mercenaries.size());
        assertEquals(mercOrigin, mercenaries.get(0).getPosition());

        // Mercenary will move up/down the wall for the first tick.
        dr = dmc.tick(Direction.LEFT);
        assertEquals(mercOrigin.getX(), getEntities(dr, "mercenary").get(0).getPosition().getX());

        // Moves to the right for the next two ticks.
        for (int i = 1; i <= 2; i++) {
            dr = dmc.tick(Direction.LEFT);
            assertEquals(mercOrigin.getX() + i, getEntities(dr, "mercenary").get(0).getPosition().getX());
        }

        EntityResponse mercenary = getEntities(dr, "mercenary").get(0);
        assertEquals(playerOrigin.getX(), mercenary.getPosition().getX());

        EntityResponse player = getPlayer(dr).get();
        assertTrue(Position.isAdjacent(mercenary.getPosition(), player.getPosition()));
    }

    @Test
    @DisplayName("Test the mercenary moves around a horizonal wall to get to the player")
    public void testMercenaryMovesAroundHorizonalWall() {
        /*
         *  [  ]   merc   [  ]
         *  [  ]   wall   [  ]
         *  [  ]  player  [  ]
         */
        Position playerOrigin = new Position(0, 0);
        Position mercOrigin = playerOrigin.translateBy(0, -2);

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("d_mercTest_wallMovementH", "c_battleTests_basicMercenaryMercenaryDies");

        assertEquals(playerOrigin, getPlayer(dr).get().getPosition());

        List<EntityResponse> mercenaries = getEntities(dr, "mercenary");
        assertEquals(1, mercenaries.size());
        assertEquals(mercOrigin, mercenaries.get(0).getPosition());

        // Mercenary will move left/right the wall for the first tick.
        dr = dmc.tick(Direction.UP);
        assertEquals(mercOrigin.getY(), getEntities(dr, "mercenary").get(0).getPosition().getY());

        // Moves down for the next two ticks.
        for (int i = 1; i <= 2; i++) {
            dr = dmc.tick(Direction.UP);
            assertEquals(mercOrigin.getY() + i, getEntities(dr, "mercenary").get(0).getPosition().getY());
        }

        EntityResponse mercenary = getEntities(dr, "mercenary").get(0);
        assertEquals(playerOrigin.getY(), mercenary.getPosition().getY());

        EntityResponse player = getPlayer(dr).get();
        assertTrue(Position.isAdjacent(mercenary.getPosition(), player.getPosition()));
    }

    @Test
    @DisplayName("Test the mercenary moves around a 3 tile wide horizonal wall to get to the player")
    public void testMercenaryMovesAround3HorizonalWalls() {
        /*
         *  [  ]   [  ]   merc   [  ]   [  ]
         *  [  ]   wall   wall   wall   [  ]
         *  [  ]   [  ]  player  [  ]   [  ]
         */

        Position playerOrigin = new Position(0, 0);
        Position mercOrigin = playerOrigin.translateBy(0, -2);

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("d_mercTest_wallMovement3H", "c_battleTests_basicMercenaryMercenaryDies");

        assertEquals(playerOrigin, getPlayer(dr).get().getPosition());

        List<EntityResponse> mercenaries = getEntities(dr, "mercenary");
        assertEquals(1, mercenaries.size());
        assertEquals(mercOrigin, mercenaries.get(0).getPosition());

        // Moves left/right for the first two ticks.
        for (int i = 1; i <= 2; i++) {
            dr = dmc.tick(Direction.UP);
            assertEquals(mercOrigin.getY(), getEntities(dr, "mercenary").get(0).getPosition().getY());
            assertNotEquals(mercOrigin.getX(), getEntities(dr, "mercenary").get(0).getPosition().getX());
        }

        // Moves down for the next two ticks.
        for (int i = 1; i <= 2; i++) {
            dr = dmc.tick(Direction.UP);
            assertEquals(mercOrigin.getY() + i, getEntities(dr, "mercenary").get(0).getPosition().getY());
        }

        // Moves left/right for the last tick, ending up next to the player.
        dr = dmc.tick(Direction.UP);

        EntityResponse mercenary = getEntities(dr, "mercenary").get(0);
        assertEquals(playerOrigin.getY(), mercenary.getPosition().getY());

        EntityResponse player = getPlayer(dr).get();
        assertTrue(Position.isAdjacent(mercenary.getPosition(), player.getPosition()));
    }

    @Test
    @DisplayName("Test the mercenary moves around an L-shaped wall towards the player")
    public void testMercenaryMovesAroundLShapedWall() {
        /*
         *  [  ]   wall  [   ]   [  ]   [  ]
         *  player wall   merc   [  ]   [  ]
         *  [  ]   wall   wall   wall   wall
         */

        Position playerOrigin = new Position(-1, -1);
        Position mercOrigin = playerOrigin.translateBy(2, 0);

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("d_mercTest_LShapeWall", "c_battleTests_basicMercenaryMercenaryDies");

        assertEquals(playerOrigin, getPlayer(dr).get().getPosition());

        List<EntityResponse> mercenaries = getEntities(dr, "mercenary");
        assertEquals(1, mercenaries.size());
        assertEquals(mercOrigin, mercenaries.get(0).getPosition());

        // Mercenary will move up the wall for the first two ticks.
        Position newPos = mercOrigin;
        for (int i = 1; i <= 2; i++) {
            dr = dmc.tick(Direction.RIGHT);
            newPos = mercOrigin.translateBy(0, -i);
            assertEquals(newPos, getEntities(dr, "mercenary").get(0).getPosition());
        }

        // Moves left for the next two ticks.
        for (int i = 1; i <= 2; i++) {
            dr = dmc.tick(Direction.RIGHT);
            newPos = mercOrigin.translateBy(-i, -2);
            assertEquals(newPos, getEntities(dr, "mercenary").get(0).getPosition());
        }

        // Moves down for the next tick.
        dr = dmc.tick(Direction.RIGHT);
        assertEquals(playerOrigin.getY() - 1, getEntities(dr, "mercenary").get(0).getPosition().getY());

        EntityResponse mercenary = getEntities(dr, "mercenary").get(0);
        assertEquals(playerOrigin.getX(), mercenary.getPosition().getX());

        EntityResponse player = getPlayer(dr).get();
        assertTrue(Position.isAdjacent(mercenary.getPosition(), player.getPosition()));
    }

    @Test
    @DisplayName("Test the mercenary goes through portal when given option between that and walking more")
    public void testMercenaryPortalBasic() {
        /*
         *  PORTAL  merc   [  ]   [  ]   wall  player  [  ]  PORTAL
         */

        Position playerOrigin = new Position(4, 0);
        Position mercOrigin = playerOrigin.translateBy(-4, 0);

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("d_mercTest_basicPortal", "c_battleTests_basicMercenaryMercenaryDies");

        assertEquals(playerOrigin, getPlayer(dr).get().getPosition());

        List<EntityResponse> mercenaries = getEntities(dr, "mercenary");
        assertEquals(1, mercenaries.size());
        assertEquals(mercOrigin, mercenaries.get(0).getPosition());

        // Mercenary will move into the portal
        dr = dmc.tick(Direction.LEFT);

        EntityResponse mercenary = getEntities(dr, "mercenary").get(0);
        assertEquals(playerOrigin.translateBy(Direction.RIGHT), mercenary.getPosition());

        EntityResponse player = getPlayer(dr).get();
        assertTrue(Position.isAdjacent(mercenary.getPosition(), player.getPosition()));
    }

    @Test
    @DisplayName("Test the mercenary goes through portal when given option between that and walking more")
    public void testMercenaryPortalFurther() {
        /*
         *   [  ]   [  ]   [  ]   [  ]   [  ]   [  ]   [  ]   [  ]   wall   [  ]   [  ]
         *  PORTAL  [  ]   [  ]   merc   [  ]   [  ]   [  ]   [  ]  player  [  ]  PORTAL
         */

        Position playerOrigin = new Position(0, 0);
        Position mercOrigin = playerOrigin.translateBy(-5, 0);

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("d_mercTest_portalFurther", "c_battleTests_basicMercenaryMercenaryDies");

        assertEquals(playerOrigin, getPlayer(dr).get().getPosition());

        List<EntityResponse> mercenaries = getEntities(dr, "mercenary");
        assertEquals(1, mercenaries.size());

        EntityResponse mercenary = mercenaries.get(0);
        assertEquals(mercOrigin, mercenary.getPosition());

        // Moves left twice
        for (int i = 1; i <= 2; i++) {
            dr = dmc.tick(Direction.UP);
            mercenary = getEntities(dr, "mercenary").get(0);
            assertEquals(mercOrigin.translateBy(-i, 0), mercenary.getPosition());
        }

        // Go through the Portal and appear beside the player.
        dr = dmc.tick(Direction.UP);
        mercenary = getEntities(dr, "mercenary").get(0);
        EntityResponse player = getPlayer(dr).get();
        assertTrue(Position.isAdjacent(mercenary.getPosition(), player.getPosition()));
    }

    @Test
    @DisplayName("Test the mercenary will avoid swamp tiles")
    public void testMercenaryPathAvoidSwamp1() {
        /*
         *  [  ]   [  ]   [  ]   [  ]   wall
         *  [  ]   [  ]   [  ]   [  ]  player
         *  [  ]   wall   wall   wall   swmp
         *  [  ]   wall   [  ]   wall   [  ]
         *  [  ]   wall   wall   wall   [  ]
         *  merc   [  ]   [  ]   [  ]   [  ]
         */
        testMercenaryPathSwamp("d_pathTest_avoidSwamp1", new Position(0, 1));
    }

    @Test
    @DisplayName("Test the mercenary will avoid swamp tiles")
    public void testMercenaryPathAvoidSwamp2() {
        /*
         *  [  ]   [  ]   [  ]   [  ]   wall
         *  [  ]   [  ]   [  ]   [  ]  player
         *  [  ]   wall   wall   wall   [  ]
         *  [  ]   wall   [  ]   wall   [  ]
         *  [  ]   wall   wall   wall   [  ]
         *  merc   [  ]   [  ]   [  ]   swmp
         */
        testMercenaryPathSwamp("d_pathTest_avoidSwamp2", new Position(0, 4));
    }

    @Test
    @DisplayName("Test the mercenary will avoid swamp tiles")
    public void testMercenaryPathAvoidSwamp3() {
        /*
         *  [  ]   [  ]   [  ]   [  ]   wall
         *  [  ]   [  ]   [  ]   [  ]  player
         *  [  ]   wall   wall   wall   [  ]
         *  [  ]   wall   [  ]   wall   [  ]
         *  [  ]   wall   wall   wall   [  ]
         *  merc   swmp   [  ]   [  ]   [  ]
         */
        testMercenaryPathSwamp("d_pathTest_avoidSwamp3", new Position(-3, 4));
    }

    @Test
    @DisplayName("Test the mercenary will go through swamp tiles if closer")
    public void testMercenaryPathThroughSwamp1() {
        /*
         *  [  ]   [  ]   [  ]   [  ]   wall
         *  [  ]   [  ]   [  ]   swmp  player
         *  [  ]   wall   wall   wall   [  ]
         *  [  ]   wall   [  ]   wall   [  ]
         *  [  ]   wall   wall   wall   wall
         *  merc   [  ]   [  ]   [  ]   [  ]
         */
        testMercenaryPathSwamp("d_pathTest_throughSwamp1", new Position(-1, 0));
    }

    @Test
    @DisplayName("Test the mercenary will go through swamp tiles if closer")
    public void testMercenaryPathThroughSwamp2() {
        /*
         *  [  ]   [  ]   [  ]   [  ]   wall
         *  swmp   [  ]   [  ]   [  ]  player
         *  [  ]   wall   wall   wall   [  ]
         *  [  ]   wall   [  ]   wall   [  ]
         *  [  ]   wall   wall   wall   wall
         *  merc   [  ]   [  ]   [  ]   [  ]
         */
        testMercenaryPathSwamp("d_pathTest_throughSwamp2", new Position(-4, 0));
    }

    @Test
    @DisplayName("Test the mercenary will go through swamp tiles if closer")
    public void testMercenaryPathThroughSwamp3() {
        /*
         *  [  ]   [  ]   [  ]   [  ]   wall
         *  [  ]   [  ]   [  ]   [  ]  player
         *  [  ]   wall   wall   wall   [  ]
         *  [  ]   wall   [  ]   wall   [  ]
         *  swmp   wall   wall   wall   wall
         *  merc   [  ]   [  ]   [  ]   [  ]
         */
        testMercenaryPathSwamp("d_pathTest_throughSwamp3", new Position(-4, -3));
    }

    private void testMercenaryPathSwamp(String dungeonName, Position swampPos) {
        Position playerOrigin = new Position(0, 0);
        Position mercOrigin = playerOrigin.translateBy(-4, 4);

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame(dungeonName, "c_movementTest_testMovementDown");

        EntityResponse mercenary = getEntities(dr, "mercenary").get(0);
        assertEquals(mercOrigin, mercenary.getPosition());

        int dy = swampPos.getX() == mercenary.getPosition().getX() && swampPos.getY() != playerOrigin.getY() ? 5 : 4;

        // Mercenary moves up for dy ticks.
        for (int i = 1; i <= dy; i++) {
            dr = dmc.tick(Direction.UP);
        }
        mercenary = getEntities(dr, "mercenary").get(0);
        assertEquals(mercOrigin.translateBy(0, -4), mercenary.getPosition());

        int dx = swampPos.getY() == mercenary.getPosition().getY() ? 4 : 3;

        // Mercenary moves right for dx ticks.
        for (int i = 1; i <= dx; i++) {
            dr = dmc.tick(Direction.UP);
        }
        mercenary = getEntities(dr, "mercenary").get(0);
        assertEquals(mercOrigin.translateBy(3, -4), mercenary.getPosition());

        // Should be next to the player.
        EntityResponse player = getPlayer(dr).get();
        assertTrue(Position.isAdjacent(mercenary.getPosition(), player.getPosition()));
    }

    public static String[] outsideRadius_5_99() {
        return new String[] {
            "d_mercTest_PlayerDiagonal5",
            "d_mercTest_PlayerSide5",
            "d_mercTest_PlayerDiagonal999",
            "d_mercTest_PlayerSide999",
        };
    }

    @ParameterizedTest
    @MethodSource(value = "outsideRadius_5_99")
    @DisplayName("Test the mercenary can't be bribed if outside bribe radius of 1")
    public void testMercenaryBribeOutsideRadius1(String dungeon) {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame(dungeon, "c_movementTest_testMovementDown");

        List<EntityResponse> mercenaries = getEntities(dr, "mercenary");
        String mercId = mercenaries.get(0).getId();
        // Try to interact
        assertThrows(InvalidActionException.class, () -> dmc.interact(mercId));
    }

    @ParameterizedTest
    @MethodSource(value = "outsideRadius_5_99")
    @DisplayName("Test the mercenary can't be bribed if outside bribe radius of 4")
    public void testMercenaryBribeOutsideRadius4(String dungeon) {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame(dungeon, "c_mercTest_bribe1rad4");

        List<EntityResponse> mercenaries = getEntities(dr, "mercenary");
        String mercId = mercenaries.get(0).getId();
        // Try to interact
        assertThrows(InvalidActionException.class, () -> dmc.interact(mercId));
    }

    @Test
    @DisplayName("Test the mercenary can't be bribed if inside the bribe radius of 1 but not enough gold")
    public void testMercenaryBribeNotEnoughGoldRad1() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("d_mercTest_downMovement", "c_movementTest_testMovementDown");

        List<EntityResponse> mercenaries = getEntities(dr, "mercenary");
        String mercId = mercenaries.get(0).getId();
        // Try to interact
        assertThrows(InvalidActionException.class, () -> dmc.interact(mercId));
    }

    @Test
    @DisplayName("Test the mercenary can't be bribed if inside the bribe radius of 4 but not enough gold")
    public void testMercenaryBribeNotEnoughGoldRad4() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("d_mercTest_downMovement", "c_mercTest_bribe1rad4");

        List<EntityResponse> mercenaries = getEntities(dr, "mercenary");
        String mercId = mercenaries.get(0).getId();
        // Try to interact
        assertThrows(InvalidActionException.class, () -> dmc.interact(mercId));
    }

    @Test
    @DisplayName("Test the mercenary can be bribed if inside the bribe radius of 1 with enough gold")
    public void testMercenaryBribeEnoughGoldRad1Diagonal() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("d_mercTest_PlayerDiagonal1", "c_mercTest_bribe0rad1");

        List<EntityResponse> mercenaries = getEntities(dr, "mercenary");
        String mercId = mercenaries.get(0).getId();
        // Try to interact
        assertDoesNotThrow(() -> dmc.interact(mercId));

        // Check whether the merc follows the player's old position
        for (int i = 0; i < 10; i++) {
            dr = dmc.tick(Direction.RIGHT);
        }
        mercenaries = getEntities(dr, "mercenary");
        Position finalMercPosition = mercenaries.get(0).getPosition();

        List<EntityResponse> players = getEntities(dr, "player");
        Position finalPlayerPosition = players.get(0).getPosition();

        // Ensure the merc is now at most 3 blocks away from player, meaning it was led away
        assertTrue(finalMercPosition.getDistanceTo(finalPlayerPosition) <= 3);
    }

    public static String[] insideRadius10() {
        return new String[] {
            "d_mercTest_PlayerDiagonal1",
            "d_mercTest_PlayerSide1",
            "d_mercTest_PlayerDiagonal5",
            "d_mercTest_PlayerSide5"
        };
    }

    @ParameterizedTest
    @MethodSource(value = "insideRadius10")
    @DisplayName("Test the mercenary can be bribed if inside the bribe radius of 10 with enough gold of 1")
    public void testMercenaryBribeEnoughGoldRad4(String dungeon) {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame(dungeon, "c_mercTest_bribe1rad10");

        // Collect the treasure (gold 1)
        dmc.tick(Direction.RIGHT);

        List<EntityResponse> mercenaries = getEntities(dr, "mercenary");
        String mercId = mercenaries.get(0).getId();
        // Try to interact
        assertDoesNotThrow(() -> dmc.interact(mercId));

        // Check whether the merc follows the player's old position
        for (int i = 0; i < 10; i++) {
            dr = dmc.tick(Direction.DOWN);
        }
        mercenaries = getEntities(dr, "mercenary");
        Position finalMercPosition = mercenaries.get(0).getPosition();

        List<EntityResponse> players = getEntities(dr, "player");
        Position finalPlayerPosition = players.get(0).getPosition();

        // Ensure the merc is now at at most 10 blocks from player, meaning it was led away
        assertTrue(finalMercPosition.getDistanceTo(finalPlayerPosition) <= 10);
    }

    @Test
    @DisplayName("Test that a mercenary's movement changes to random when too far away from the player")
    public void testMercenaryMovementTooFar() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("d_mercTest_PlayerSide999", "c_potionTestConfigLength50");

        Position origin = getEntities(dr, "mercenary").get(0).getPosition();


        int ticks = 5;

        boolean right = true;
        for (int i = 0; i < ticks; i++) {
            dmc.tick(right ? Direction.RIGHT : Direction.LEFT);
            right = !right;
        }

        Position actual = getEntities(dr, "mercenary").get(0).getPosition();

        // Assuming the player is to the left of the mercenary.
        assertNotEquals(new Position(origin.translateBy(-ticks, 0)), actual);
    }
}
