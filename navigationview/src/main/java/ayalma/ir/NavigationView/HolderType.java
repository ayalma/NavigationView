package ayalma.ir.NavigationView;

/**
 * enum that handle recycler view holder type.
 * Created by alimohammadi on 4/30/16.
 *
 * @author alimohammadi.
 */
public enum HolderType
{
    Header(1),Item(2);

    int value;

    HolderType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
