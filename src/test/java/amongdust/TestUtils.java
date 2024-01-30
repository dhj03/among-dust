package amongdust;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONObject;

import amongdust.response.models.AnimationQueue;
import amongdust.response.models.BattleResponse;
import amongdust.response.models.DungeonResponse;
import amongdust.response.models.EntityResponse;
import amongdust.response.models.ItemResponse;
import amongdust.response.models.RoundResponse;
import amongdust.util.FileLoader;
import amongdust.util.Position;

public class TestUtils {
    public static Stream<EntityResponse> getEntitiesStream(DungeonResponse res, String type) {
        if (type.equals("zombie_toast")){
            return res.getEntities().stream()
                    .filter(it -> it.getType().startsWith(type))
                    .filter(it -> !it.getType().startsWith("zombie_toast_spawner"));
        }
        return res.getEntities().stream().filter(it -> it.getType().startsWith(type));
    }

    public static int countEntityOfType(DungeonResponse res, String type) {
        return getEntities(res, type).size();
    }

    public static Position getPlayerPosition(DungeonResponse res) {
        return getPlayer(res).get().getPosition();
    }

    public static Optional<EntityResponse> getPlayer(DungeonResponse res) {
        return getEntitiesStream(res, "player").findFirst();
    }

    public static List<EntityResponse> getEntities(DungeonResponse res, String type) {
        return getEntitiesStream(res, type).collect(Collectors.toList());
    }

    public static List<ItemResponse> getInventory(DungeonResponse res, String type) {
        return res.getInventory().stream()
                                 .filter(it -> it.getType().startsWith(type))
                                 .collect(Collectors.toList());
    }

    public static String getGoals(DungeonResponse dr) {
        String goals = dr.getGoals();
        return goals != null ? goals : "";
    }

    public static String getValueFromConfigFile(String fieldName, String configFilePath) {
        try {
            JSONObject config = new JSONObject(FileLoader.loadResourceFile("/configs/" + configFilePath + ".json"));

            if (!config.isNull(fieldName)) {
                return config.get(fieldName).toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public static List<String> getBuildables(DungeonResponse res) {
        return res.getBuildables();
    }

    public static void compareDungeons(DungeonResponse d1, DungeonResponse d2) {
        // Compare lists
        assertTrue(equalItemLists(d1.getInventory(), d2.getInventory()));
        assertTrue(equalEntityLists(d1.getEntities(), d2.getEntities()));
        assertTrue(equalBattleLists(d1.getBattles(), d2.getBattles()));
        assertTrue(equalAnimLists(d1.getAnimations(), d2.getAnimations()));
        assertTrue(equalStringLists(d1.getBuildables(), d2.getBuildables()));


        // Check all non-list attributes
        assertEquals(d1.getGoals(), d2.getGoals());
        assertEquals(d1.getDungeonName(), d2.getDungeonName());
        assertEquals(d1.getDungeonId(), d2.getDungeonId());

    }

    private static boolean equalStringLists(List<String> l1, List<String> l2) {
        if (l1.size() != l2.size()) return false;

        for (String str : l1) {
            if (l2.stream().noneMatch(s -> s.equals(str))) {return false;}
        }

        return true;
    }

    private static boolean equalItemLists(List<ItemResponse> i1, List<ItemResponse> i2) {
        if (i1.size() != i2.size()) return false;
        for (ItemResponse item : i1) {
            // If no match, return false
            if (i2.stream().noneMatch((i -> (i.getId().equals(item.getId())) && (i.getType().equals(item.getType()))))) { return false;}
        }
        return true;
    }

    private static boolean equalEntityLists(List<EntityResponse> e1, List<EntityResponse> e2) {
        if (e1.size() != e2.size()) return false;

        for (EntityResponse entity : e1) {
            // Check id & type equal
            if (e2.stream().noneMatch((e ->
                (e.getId().equals(entity.getId())) &&
                (e.getType().equals(entity.getType())) &&
                (e.getPosition().equals(entity.getPosition()))
                ))) {
                    return false;
                }
        }
        return true;
    }

    private static boolean equalBattleLists(List<BattleResponse> b1, List<BattleResponse> b2) {
        if (b1.size() != b2.size()) return false;

        for (BattleResponse battle : b1) {
            // Check id & type equal
            if (b2.stream().noneMatch((b ->
                (b.getEnemy().equals(battle.getEnemy())) &&
                (b.getInitialEnemyHealth() == battle.getInitialEnemyHealth()) &&
                (b.getInitialPlayerHealth() == battle.getInitialPlayerHealth()) &&
                (equalRoundLists(b.getRounds(), battle.getRounds()))
                ))) {
                    return false;
                }

        }
        return true;
    }

    private static boolean equalRoundLists(List<RoundResponse> r1, List<RoundResponse> r2) {
        if (r1.size() != r2.size()) return false;

        for (RoundResponse round : r1) {
            // Check id & type equal
            if (r2.stream().noneMatch((r ->
                (r.getDeltaCharacterHealth() == round.getDeltaCharacterHealth()) &&
                (r.getDeltaEnemyHealth() == round.getDeltaEnemyHealth()) &&
                (equalItemLists(r.getWeaponryUsed(), round.getWeaponryUsed()))
                ))) {
                    return false;
                }
        }
        return true;
    }

    private static boolean equalAnimLists(List<AnimationQueue> a1, List<AnimationQueue> a2) {
        if (a1.size() != a2.size()) return false;

        for (AnimationQueue anim : a1) {
            // Check id & type equal
            if (a2.stream().noneMatch((a ->
                (a.getDuration() == anim.getDuration()) &&
                (a.getEntityId().equals(anim.getEntityId())) &&
                (equalStringLists(a.getQueue(), anim.getQueue()))
                ))) {
                    return false;
                }
        }
        return true;
    }
}
