package amongdust.dungeon.player.playerstate.observer;

import amongdust.dungeon.player.playerstate.states.PlayerState;

public interface PlayerStateSubscriber {
    public void update(PlayerState state);
}
