package dungeonmania.dungeon.player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dungeonmania.dungeon.entities.Entity;
import dungeonmania.dungeon.entities.buildableentities.Buildable;
import dungeonmania.dungeon.entities.buildableentities.ItemQuantifier;
import dungeonmania.dungeon.entities.physicalentities.collectableentities.Key;
import dungeonmania.dungeon.entities.physicalentities.collectableentities.Usable;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.ItemResponse;

public class Inventory implements Serializable {
    private List<Entity> itemList;

    public Inventory(Inventory old) {
        this.itemList = new ArrayList<>(old.itemList);
    }

    public Inventory() {
        this.itemList = new ArrayList<>();
    }

    public void add(Entity entity) {
        itemList.add(entity);
    }

    public void build(Buildable item) throws InvalidActionException {
        Map<String, ItemQuantifier> validRecipe = item.getRecipes().stream()
                                               .filter(r -> canBuildUsingRecipe(r))
                                               .findFirst()
                                               .orElseThrow(() -> new InvalidActionException(
                                                    "Cannot build due to insufficient materials"
                                                ));

        // Building possible, consume items in recipe
        for (String type : validRecipe.keySet()) {
            ItemQuantifier itemQ = validRecipe.get(type);

            if (itemQ.isConsumed()) {
                removeFromType(type, itemQ.getAmount());
            }
        }
        itemList.add((Entity) item);
    }

    public boolean removeKey(int keyNum) {
        return itemList.removeIf(i -> {
            if (i instanceof Key) {
                Key k = (Key) i;
                if (k.matchesDoor(keyNum)) {
                    return true;
                }
            }
            return false;
        });
    }

    public List<ItemResponse> getResponse() {
        return itemList.stream()
                       .map(item -> new ItemResponse(item.getId(), item.getType()))
                       .collect(Collectors.toList());
    }

    public void useItem(String itemUsedId, Player player) throws InvalidActionException {
        Entity match = itemList.stream()
                               .filter(item -> item.getId().equals(itemUsedId))
                               .findFirst()
                               .orElseThrow(() -> new InvalidActionException("Item not in inventory"));

        if (!(match instanceof Usable)) {
            throw new IllegalArgumentException("Type isn't valid");
        }
        Usable usable = (Usable) match;

        usable.use(player);
    }

    public List<Equipment> getListEquipment() {
        return itemList.stream().filter(item -> item instanceof Equipment)
                       .map(equipment -> (Equipment) equipment)
                       .collect(Collectors.toList());
    }

    public List<Usable> getListUsables() {
        return itemList.stream().filter(item -> item instanceof Usable)
                       .map(usable -> (Usable) usable)
                       .collect(Collectors.toList());
    }

    public void removeFromType(String type, int frequency) {
        // Remove item i amount of times from inventory
        for (int i = 0; i < frequency; i++) {
            Entity removed = itemList.stream()
                                     .filter(item -> item.getType().equals(type))
                                     .collect(Collectors.toList())
                                     .stream().findFirst().orElse(null);
            itemList.remove(removed);
        }
    }

    public void removeFromId(String id) {
        itemList.removeIf(i -> i.getId().equals(id));
    }

    public int countType(String type) {
        return (int) itemList.stream().filter(item -> item.getType().equals(type)).count();
    }

    // Helpers
    private boolean canBuildUsingRecipe(Map<String, ItemQuantifier> recipe) {
        // Return true if we have greater than or equal to the required amount
        return recipe.keySet().stream().allMatch(type -> {
            return countType(type) >= recipe.get(type).getAmount();
        });
    }
}
