package dungeonmania.dungeon.battle;

public class BattleModifier {
    private int attackAdditive;
    private int attackMultiplier;
    private int defenceAdditive;

    public BattleModifier() {
        this.attackAdditive = 0;
        this.attackMultiplier = 1;
        this.defenceAdditive = 0;
    }

    public int getAttackAdditive() {
        return attackAdditive;
    }

    public void addAttackAdditive(int attackAdditive) {
        this.attackAdditive += attackAdditive;
    }

    public int getAttackMultiplier() {
        return attackMultiplier;
    }

    public void multiplyAttackMultiplier(int attackMultiplier) {
        this.attackMultiplier *= attackMultiplier;
    }

    public int getDefenceAdditive() {
        return defenceAdditive;
    }

    public void addDefenceAdditive(int defenceAdditive) {
        this.defenceAdditive += defenceAdditive;
    }
}
