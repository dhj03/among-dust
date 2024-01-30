package amongdust.dungeon.battle;

import java.io.Serializable;

public class Stats implements Serializable {
    private final double MAX_HEALTH;
    private final double BASE_ATTACK;
    private double health;

    public Stats(double maxHealth, double baseAttack) {
        MAX_HEALTH = maxHealth;
        BASE_ATTACK = baseAttack;

        health = MAX_HEALTH;
    }

    public double getHealth() {
        return health;
    }

    public void addHealth(double amount) {
        health += amount;
    }

    public double getBaseAttack() {
        return BASE_ATTACK;
    }

    public boolean isAlive() {
        return health > 0.0001;
    }
}
