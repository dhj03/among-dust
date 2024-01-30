package amongdust.dungeon.player.playerstate.states;

import amongdust.dungeon.entities.Entity;
import amongdust.dungeon.entities.physicalentities.Tickable;
import amongdust.response.models.ItemResponse;

public abstract class TemporaryPlayerState implements PlayerState, Tickable {
    private int remainingDuration;
    private Entity item;

    public TemporaryPlayerState(Entity item, int duration) {
        this.item = item;
        this.remainingDuration = duration;
    }

    public boolean isFinished() {
        return remainingDuration <= 0;
    }

    @Override
    public void tick() {
        remainingDuration--;
    }

    public ItemResponse getItemResponse() {
        return new ItemResponse(item.getId(), item.getType());
    }
}
