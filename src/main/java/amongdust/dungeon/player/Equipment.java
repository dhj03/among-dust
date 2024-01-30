package amongdust.dungeon.player;

import java.io.Serializable;

import amongdust.dungeon.battle.BattleModifier;

public interface Equipment extends Serializable {
    public void addModifications(BattleModifier modifier);
    public boolean isBroken();
}
