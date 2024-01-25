package dungeonmania.dungeon.entities;

import dungeonmania.dungeon.player.Player;
import dungeonmania.exceptions.InvalidActionException;

public interface Interactable {
    public void interact(Player player) throws InvalidActionException;
}
