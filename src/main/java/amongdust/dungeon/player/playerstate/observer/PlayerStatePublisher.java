package amongdust.dungeon.player.playerstate.observer;

public interface PlayerStatePublisher {
    public void publish();
    public void addSubscriber(PlayerStateSubscriber sub);
    public void removeSubscriber(PlayerStateSubscriber sub);
}
