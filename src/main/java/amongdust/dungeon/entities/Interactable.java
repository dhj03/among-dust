package amongdust.dungeon.entities;

import amongdust.dungeon.player.Player;
import amongdust.exceptions.InvalidActionException;

public interface Interactable {
    public void interact(Player player) throws InvalidActionException;
}
