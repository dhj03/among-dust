package amongdust.dungeon.entities.buildableentities;

public class ItemQuantifier {
    private int amount;
    private boolean isConsumed;

    public ItemQuantifier(int amount, boolean isConsumed) {
        this.amount = amount;
        this.isConsumed = isConsumed;
    }

    public int getAmount() {
        return amount;
    }

    public boolean isConsumed() {
        return isConsumed;
    }
}
