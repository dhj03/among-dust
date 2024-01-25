package dungeonmania;

import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.countEntityOfType;
import static dungeonmania.TestUtils.getValueFromConfigFile;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;

public class BattleTests {
    private static DungeonResponse genericBattleSequence(DungeonManiaController controller, String enemyType, String configFile) {
        /*
         *  [  ]    [  ]    [  ]
         * player  entity   [  ]
         *  [  ]    [  ]    [  ]
         */
        DungeonResponse initialResponse = controller.newGame("d_battleTest_basic_" + enemyType, configFile);

        assertEquals(1, countEntityOfType(initialResponse, "player"));
        assertEquals(1, countEntityOfType(initialResponse, enemyType));
        return controller.tick(Direction.RIGHT);
    }

    private void assertBattleCalculations(String enemyType, BattleResponse battle, boolean enemyDies, String configFilePath) {
        List<RoundResponse> rounds = battle.getRounds();
        double playerHealth = Double.parseDouble(getValueFromConfigFile("player_health", configFilePath));
        double enemyHealth = Double.parseDouble(getValueFromConfigFile(enemyType + "_health", configFilePath));
        double playerAttack = Double.parseDouble(getValueFromConfigFile("player_attack", configFilePath));
        double enemyAttack = Double.parseDouble(getValueFromConfigFile(enemyType + "_attack", configFilePath));

        for (RoundResponse round : rounds) {
            assertEquals(round.getDeltaCharacterHealth(), -(enemyAttack / 10));
            assertEquals(round.getDeltaEnemyHealth(), -(playerAttack / 5));
            enemyHealth += round.getDeltaEnemyHealth();
            playerHealth += round.getDeltaCharacterHealth();
        }

        if (enemyDies) {
            assertTrue(enemyHealth <= 0);
        } else {
            assertTrue(playerHealth <= 0);
        }
    }

    @Test
    @DisplayName("Test basic battle calculations - spider - player loses")
    public void testHealthBelowZeroSpider() {
        String enemyType = "spider";
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse postBattleResponse = genericBattleSequence(controller, enemyType, "c_battleTests_basicSpiderPlayerDies");
        BattleResponse battle = postBattleResponse.getBattles().get(0);
        assertBattleCalculations(enemyType, battle, false, "c_battleTests_basicSpiderPlayerDies");
    }

    @Test
    @DisplayName("Test basic battle calculations - spider - player wins")
    public void testRoundCalculationsSpider() {
        String enemyType = "spider";
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse postBattleResponse = genericBattleSequence(controller, enemyType, "c_battleTests_basicSpiderSpiderDies");
        BattleResponse battle = postBattleResponse.getBattles().get(0);
        assertBattleCalculations(enemyType, battle, true, "c_battleTests_basicSpiderSpiderDies");
    }

    @Test
    @DisplayName("Test basic battle calculations - zombie - player loses")
    public void testHealthBelowZeroZombie() {
        String enemyType = "zombie_toast";
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse postBattleResponse = genericBattleSequence(controller, enemyType, "c_battleTests_basicZombiePlayerDies");
        BattleResponse battle = postBattleResponse.getBattles().get(0);
        assertBattleCalculations("zombie", battle, false, "c_battleTests_basicZombiePlayerDies");
    }

    @Test
    @DisplayName("Test basic battle calculations - zombie - player wins")
    public void testRoundCalculationsZombie() {
        String enemyType = "zombie_toast";
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse postBattleResponse = genericBattleSequence(controller, enemyType, "c_battleTests_basicZombieZombieDies");
        BattleResponse battle = postBattleResponse.getBattles().get(0);
        assertBattleCalculations("zombie", battle, true, "c_battleTests_basicZombieZombieDies");
    }

    @Test
    @DisplayName("Test battling mercenary with sword")
    public void testBattleMercenaryWithSword() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_battleTest_sword_mercenary", "c_battleTests_8Health2Attack");
        controller.tick(Direction.RIGHT);
        BattleResponse battle = controller.tick(Direction.RIGHT).getBattles().get(0);

        assert(battle.getRounds().size() == 8);
    }

    @Test
    @DisplayName("Test battling mercenary with bow")
    public void testBattleMercenaryWithBow() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_battleTest_bow_mercenary", "c_battleTests_8Health2Attack");
        controller.tick(Direction.RIGHT);
        controller.tick(Direction.RIGHT);
        controller.tick(Direction.RIGHT);
        controller.tick(Direction.RIGHT);
        assertDoesNotThrow(() -> controller.build("bow"));
        BattleResponse battle = controller.tick(Direction.RIGHT).getBattles().get(0);

        assert(battle.getRounds().size() == 10);
    }

    @Test
    @DisplayName("Test battling mercenary with shield")
    public void testBattleMercenaryWithShield() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_battleTest_shield_mercenary", "c_battleTests_8Health2Attack");
        controller.tick(Direction.RIGHT);
        controller.tick(Direction.RIGHT);
        controller.tick(Direction.RIGHT);
        assertDoesNotThrow(() -> controller.build("shield"));
        BattleResponse battle = controller.tick(Direction.RIGHT).getBattles().get(0);

        assert(battle.getRounds().size() == 20);
        assert(battle.getRounds().get(0).getDeltaCharacterHealth() == -0.1);
    }

    @Test
    @DisplayName("Test battling mercenary with sword and bow")
    public void testBattleMercenaryWithSwordAndBow() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_battleTest_sword_bow_mercenary", "c_battleTests_8Health2Attack");
        controller.tick(Direction.RIGHT);
        controller.tick(Direction.RIGHT);
        controller.tick(Direction.RIGHT);
        controller.tick(Direction.RIGHT);
        controller.tick(Direction.RIGHT);
        assertDoesNotThrow(() -> controller.build("bow"));
        BattleResponse battle = controller.tick(Direction.RIGHT).getBattles().get(0);

        assert(battle.getRounds().size() == 4);
    }

    @Test
    @DisplayName("Test battling two mercenaries with sword breaking after first battle")
    public void testBattleTwoMercenariesWithBrokenSword() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_battleTest_sword_two_mercenaries", "c_battleTests_8Health2Attack");
        controller.tick(Direction.RIGHT);
        controller.tick(Direction.RIGHT);
        List<BattleResponse> battles = controller.tick(Direction.RIGHT).getBattles();

        assert(battles.get(0).getRounds().size() == 8);
        assert(battles.get(1).getRounds().size() == 20);
    }

    @Test
    @DisplayName("Test battling mercenary with midnight armour")
    public void testBattleMercenaryWithMidnightArmour() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_battleTest_midnight_armour_merc", "c_battleTests_8Health2Attack");
        controller.tick(Direction.RIGHT);
        controller.tick(Direction.RIGHT);
        assertDoesNotThrow(() -> controller.build("midnight_armour"));
        BattleResponse battle = controller.tick(Direction.RIGHT).getBattles().get(0);

        assert(battle.getRounds().size() == 8);
        assert(battle.getRounds().get(0).getDeltaCharacterHealth() == -0.1);
    }

    @Test
    @DisplayName("Test if an Invisible Player will not Battle")
    public void testInvisible() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_battleTest_invisible_mercenary", "c_battleTests_8Health2Attack");
        DungeonResponse dr = controller.tick(Direction.RIGHT);

        assertDoesNotThrow(() -> controller.tick(dr.getInventory().get(0).getId()));

        List<BattleResponse> battles = controller.tick(Direction.RIGHT).getBattles();
        assert(battles.isEmpty());
    }

    @Test
    @DisplayName("Test if an Invincible Player will win a Battle")
    public void testInvincible() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_battleTest_invincible_mercenary", "c_battleTests_8Health2Attack");
        DungeonResponse dr = controller.tick(Direction.RIGHT);
        assertTrue(dr.getBattles().isEmpty());

        String id = dr.getInventory().get(0).getId();
        assertDoesNotThrow(() -> controller.tick(id));

        controller.tick(Direction.RIGHT);
        controller.tick(Direction.RIGHT);
        dr = controller.tick(Direction.RIGHT);

        BattleResponse battle = dr.getBattles().get(0);
        assertTrue(battle.getRounds().size() == 1);

        RoundResponse round = battle.getRounds().get(0);
        assertTrue(round.getDeltaCharacterHealth() == 0);
        assertTrue(round.getDeltaEnemyHealth() <= battle.getInitialEnemyHealth());
        assertTrue(round.getWeaponryUsed().size() == 1);
        assertTrue(round.getWeaponryUsed().get(0).getType().equals("invincibility_potion"));
    }

    @Test
    @DisplayName("Test Mercenary Ally attack: Player attack zero")
    public void testZombieMercAlly() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("d_battleTest_mercAlly", "c_battleTests_60Ally0PlayerAttack");

        List<EntityResponse> mercenaries = getEntities(dr, "mercenary");
        String mercId = mercenaries.get(0).getId();
        // Try to interact
        assertDoesNotThrow(() -> dmc.interact(mercId));

        dr = dmc.tick(Direction.UP);

        long noZombies = dr.getEntities().stream().filter(e -> e.getType().equals("zombie_toast")).count();
        assertTrue(noZombies == 0);
    }


    @Test
    @DisplayName("Test Mercenary Two Allies attack: Player attack zero")
    public void testZombieMercAlly2() {

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("d_battleTest_mercAlly", "c_battleTests_30Ally0PlayerAttack");

        List<EntityResponse> mercenaries = getEntities(dr, "mercenary");
        String mercId1 = mercenaries.get(0).getId();
        String mercId2 = mercenaries.get(1).getId();
        // Try to Bribe
        assertDoesNotThrow(() -> dmc.interact(mercId1));
        assertDoesNotThrow(() -> dmc.interact(mercId2));

        dr = dmc.tick(Direction.UP);


        long noZombies = dr.getEntities().stream().filter(e -> e.getType().equals("zombie_toast")).count();
        assertTrue(noZombies == 0);
    }

    @Test
    @DisplayName("Test a hydra spawns")
    public void testHydraSpawns() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse dr = controller.newGame("d_battleTest_hydra", "c_battleTests_hydra");

        assertTrue(TestUtils.getEntities(dr, "hydra").size() == 1);
    }

    @Test
    @DisplayName("Test the regen rate of Hyrda. Also test how much it regens by")
    public void testHyrdaRegen() {
        int SAMPLE_SIZE = 1000;
        double TOLERANCE = 0.05;
        int numRegens = 0;
        // Make a bunch of games
        for (int i = 0; i < SAMPLE_SIZE; i++) {
            DungeonManiaController dmc = new DungeonManiaController();
            dmc.newGame("d_battleTest_hydra", "c_hydraRegenRatePoint7");

            // Fight the hyra (which will insta kill the player) and check the health afterwards
            DungeonResponse dr = dmc.tick(Direction.RIGHT);
            List<BattleResponse> brs = dr.getBattles();
            assertTrue(brs.size() == 1);

            BattleResponse br = brs.get(0);
            assertTrue(br.getRounds().size() == 1);

            double deltaHealth = br.getRounds().get(0).getDeltaEnemyHealth();
            if (deltaHealth == 5) {
                numRegens++;
            } else {
                assertTrue(deltaHealth == -2);
            }
        }
        double actualRate = (double) numRegens / SAMPLE_SIZE;
        assertTrue(Math.abs(actualRate - 0.7) < TOLERANCE);
    }

    @Test
    @DisplayName("Test if a Player will battling against hydra")
    public void testBattleAgainstHydra() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_battleTest_hydra", "c_battleTests_hydra");

        // Battle the hydra
        BattleResponse battle = controller.tick(Direction.RIGHT).getBattles().get(0);
        assertTrue(battle.getRounds().size() == 4);
        RoundResponse round = battle.getRounds().get(0);
        assertTrue(round.getDeltaCharacterHealth() == -2);
        assertTrue(round.getDeltaEnemyHealth() == -1);
    }
}
