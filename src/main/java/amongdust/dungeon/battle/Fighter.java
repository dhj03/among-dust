package amongdust.dungeon.battle;

import amongdust.dungeon.player.playerstate.states.PlayerState;

public interface Fighter {
    public Stats getStats();
    public default boolean isAlive() {
        return getStats().isAlive();
    }
    public default double getHealth() {
        return getStats().getHealth();
    }
    public default void addHealth(double damage) {
        getStats().addHealth(damage);
    }
    public double getDeltaHealth(PlayerState state, Stats opponentStats, BattleModifier modifier);
    public void die();
}
