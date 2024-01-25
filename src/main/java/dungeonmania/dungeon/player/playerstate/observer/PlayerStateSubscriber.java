package dungeonmania.dungeon.player.playerstate.observer;

import dungeonmania.dungeon.player.playerstate.states.PlayerState;

public interface PlayerStateSubscriber {
    public void update(PlayerState state);
}
