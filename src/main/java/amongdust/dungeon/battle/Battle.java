package amongdust.dungeon.battle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import amongdust.dungeon.entities.Entity;
import amongdust.dungeon.entities.physicalentities.PhysicalEntity;
import amongdust.dungeon.player.Equipment;
import amongdust.dungeon.player.Player;
import amongdust.response.models.BattleResponse;
import amongdust.response.models.ItemResponse;
import amongdust.response.models.RoundResponse;

public class Battle implements Serializable {
    public static BattleResponse startBattle(Player player, Fighter enemy) {
        List<Equipment> equipmentList = player.getListEquipment();
        List<ItemResponse> weaponryUsed = new ArrayList<>();
        BattleModifier modifier = new BattleModifier();

        // If there are n instances of a type of item, the respective bonus will be applied n times
        for (Equipment equipment : equipmentList) {
            if (equipment.isBroken()) {
                continue;
            }

            equipment.addModifications(modifier);

            Entity item = (Entity) equipment;
            weaponryUsed.add(new ItemResponse(
                item.getId(),
                item.getType()
            ));
        }

        // Add respective modifications based on allies
        player.addAllyModifiers(modifier);

        ItemResponse potionResponse = player.getPotionResponse();
        if (potionResponse != null) {
            weaponryUsed.add(potionResponse);
        }

        double initialPlayerHealth = player.getHealth();
        double initialEnemyHealth = enemy.getHealth();
        List<RoundResponse> rounds = new ArrayList<>();
        while (player.isAlive() && enemy.isAlive()) {
            double dPlayerHealth = player.getDeltaHealth(player.getState(), enemy.getStats(), modifier);
            double dEnemyHealth = enemy.getDeltaHealth(player.getState(), player.getStats(), modifier);
            player.addHealth(dPlayerHealth);
            enemy.addHealth(dEnemyHealth);

            // The list being returned should be replaced with a WeaponryUsed list, that includes potions
            rounds.add(
                new RoundResponse(dPlayerHealth, dEnemyHealth, weaponryUsed)
            );
        }

        if (!player.isAlive()) {
            player.die();
        }
        if (!enemy.isAlive()) {
            enemy.die();
            player.incrementKillCount();
        }

        String type = ((PhysicalEntity) enemy).getType();
        return new BattleResponse(type, rounds, initialPlayerHealth, initialEnemyHealth);
    }
}