package amongdust.dungeon.entities.physicalentities.staticentities.activatable.activatablestrategies;

import org.json.JSONException;
import org.json.JSONObject;

import amongdust.dungeon.entities.physicalentities.collectableentities.Bomb;
import amongdust.dungeon.entities.physicalentities.staticentities.activatable.FloorSwitch;

public class LogicFactory {
    private static final String NA = "none";

    public static ActivatableStrategy createActivatableStrategy(JSONObject JSONEntity) {
        String logicType;
        try {
            logicType = JSONEntity.getString("logic");
        } catch (JSONException e) {
            logicType = NA;
        }
        String entityType = JSONEntity.getString("type");

        ActivatableStrategy logic;
        switch (logicType) {
            case OrActivatable.TYPE:
                logic = new OrActivatable(); break;
            case AndActivatable.TYPE:
                logic = new AndActivatable(); break;
            case XorActivatable.TYPE:
                logic = new XorActivatable(); break;
            case CoAndActivatable.TYPE:
                logic = new CoAndActivatable(); break;
            case NA:
                switch (entityType) {
                    case FloorSwitch.TYPE:
                        logic = new SwitchDefaultActivatable(); break;
                    case Bomb.TYPE:
                        logic = new BombDefaultActivatable(); break;
                    default:
                        logic = new OrActivatable(); break;
                }
                break;
            default:
                throw new IllegalArgumentException("Tried to build Logic Entity with invalid type " + logicType);
        }
        return switchNeedsWrapping(logicType, entityType) ? new LogicSwitchActivatable(logic) : logic;
    }

    private static boolean switchNeedsWrapping(String logicType, String entityType) {
        return (
            !logicType.equals(NA) &&
            entityType.equals(FloorSwitch.TYPE)
        );
    }
}
