package amongdust;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import amongdust.response.models.DungeonResponse;
import amongdust.response.models.EntityResponse;
import amongdust.util.Position;

public class GenerationTests {
    @Test
    @DisplayName("Test that a randomly generated Dungeon can be fully traversed")
    public void testDungeonGenerationValid() {
        AmongDustController dmc = new AmongDustController();
        DungeonResponse dr = dmc.generateDungeon(0, 0, 6, 9, "c_movementTest_testMovementDown");

        Position playerPos = TestUtils.getPlayerPosition(dr);

        assertEquals(1, TestUtils.getEntities(dr, "exit").size());
        Position exitPos = TestUtils.getEntities(dr, "exit").get(0).getPosition();

        assertEquals(new Position(0, 0), playerPos);
        assertEquals(new Position(6, 9), exitPos);

        List<EntityResponse> walls = TestUtils.getEntities(dr, "wall");
        walls.stream().forEach(er -> assertNotEquals(exitPos, er.getPosition()));
        assertTrue(walls.stream().filter(er -> Position.isAdjacent(exitPos, er.getPosition())).collect(Collectors.toList()).size() < 4);
        assertTrue(walls.stream().filter(er -> Position.isAdjacent(playerPos, er.getPosition())).collect(Collectors.toList()).size() < 4);
    }
}
