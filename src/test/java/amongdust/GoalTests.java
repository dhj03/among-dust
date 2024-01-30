package amongdust;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import amongdust.response.models.DungeonResponse;
import amongdust.util.Direction;

public class GoalTests {
    @Test
    @DisplayName("Testing a map with 4 conjunction goal")
    public void andAllWithoutEnemy() {
        AmongDustController dmc;
        dmc = new AmongDustController();
        DungeonResponse res = dmc.newGame("d_complexGoalsTest_andAllV2", "c_complexGoalsTest_andAll");

        // ":exit AND :treasure AND :boulders AND :enemies"
        assertTrue(TestUtils.getGoals(res).contains(":exit"));
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));
        assertTrue(TestUtils.getGoals(res).contains(":boulders"));

        res = dmc.tick(Direction.RIGHT);

        // move boulder onto switch
        res = dmc.tick(Direction.RIGHT);
        assertTrue(TestUtils.getGoals(res).contains(":exit"));
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));
        assertFalse(TestUtils.getGoals(res).contains(":boulders"));

        // pickup treasure
        res = dmc.tick(Direction.DOWN);
        assertTrue(TestUtils.getGoals(res).contains(":exit"));
        assertFalse(TestUtils.getGoals(res).contains(":treasure"));
        assertFalse(TestUtils.getGoals(res).contains(":boulders"));

        // move to exit
        res = dmc.tick(Direction.DOWN);
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @DisplayName("Testing a map with 4 conjunction goal")
    public void orAllEnemy() {
        AmongDustController dmc;
        dmc = new AmongDustController();
        DungeonResponse res = dmc.newGame("d_complexGoalsTest_orAll", "c_complexGoalsTest_andAll");

        // ":exit OR :treasure OR :boulders OR :enemies"
        // All the goals are active
        assertTrue(TestUtils.getGoals(res).contains(":exit"));
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));
        assertTrue(TestUtils.getGoals(res).contains(":boulders"));
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // kill spider
        res = dmc.tick(Direction.RIGHT);
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @DisplayName("Testing a map with 4 conjunction goal")
    public void orAllBoulders() {
        AmongDustController dmc;
        dmc = new AmongDustController();
        DungeonResponse res = dmc.newGame("d_complexGoalsTest_orAll", "c_complexGoalsTest_andAll");

        // ":exit OR :treasure OR :boulders OR :enemies"
        // All the goals are active
        assertTrue(TestUtils.getGoals(res).contains(":exit"));
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));
        assertTrue(TestUtils.getGoals(res).contains(":boulders"));
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // push boulder onto switch
        res = dmc.tick(Direction.UP);
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @DisplayName("Testing a map with 4 conjunction goal")
    public void orAllTreasure() {
        AmongDustController dmc;
        dmc = new AmongDustController();
        DungeonResponse res = dmc.newGame("d_complexGoalsTest_orAll", "c_complexGoalsTest_andAll");

        // ":exit OR :treasure OR :boulders OR :enemies"
        // All the goals are active
        assertTrue(TestUtils.getGoals(res).contains(":exit"));
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));
        assertTrue(TestUtils.getGoals(res).contains(":boulders"));
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // pick up coin
        res = dmc.tick(Direction.DOWN);
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @DisplayName("Testing a map with 4 conjunction goal")
    public void orAllExit() {
        AmongDustController dmc;
        dmc = new AmongDustController();
        DungeonResponse res = dmc.newGame("d_complexGoalsTest_orAll", "c_complexGoalsTest_andAll");

        // ":exit OR :treasure OR :boulders OR :enemies"
        // All the goals are active
        assertTrue(TestUtils.getGoals(res).contains(":exit"));
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));
        assertTrue(TestUtils.getGoals(res).contains(":boulders"));
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // go to exit
        res = dmc.tick(Direction.LEFT);
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @DisplayName("Testing a switch goal can be achieved and then become unachieved")
    public void boulderGoalUndo() {
        /*
         * exit   [  ]   [  ]
         * [  ]   swch   [  ]
         * [  ]   bldr   [  ]
         * [  ]   plyr   [  ]
         */
        AmongDustController dmc;
        dmc = new AmongDustController();
        DungeonResponse dr = dmc.newGame("d_goalTest_undoBoulder", "c_complexGoalsTest_andAll");

        // ":exit AND :boulders"
        assertTrue(TestUtils.getGoals(dr).contains(":exit"));
        assertTrue(TestUtils.getGoals(dr).contains(":boulders"));

        // Push Boulder onto switch.
        dr = dmc.tick(Direction.UP);
        assertTrue(TestUtils.getGoals(dr).contains(":exit"));
        assertFalse(TestUtils.getGoals(dr).contains(":boulders"));

        // Push Boulder off switch.
        dr = dmc.tick(Direction.UP);
        assertTrue(TestUtils.getGoals(dr).contains(":exit"));
        assertTrue(TestUtils.getGoals(dr).contains(":boulders"));

        // Push Boulder back on switch.
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.LEFT);
        dr = dmc.tick(Direction.DOWN);
        assertTrue(TestUtils.getGoals(dr).contains(":exit"));
        assertFalse(TestUtils.getGoals(dr).contains(":boulders"));

        // Go to Exit.
        dr = dmc.tick(Direction.LEFT);
        assertEquals("", TestUtils.getGoals(dr));
    }
}
