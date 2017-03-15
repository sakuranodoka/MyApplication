package invoice.item;

/**
 * Created by Administrator on 6/3/2560.
 */

public class InvoiceBaseItem {
    public int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public InvoiceBaseItem(int type) {
        this.type = type;
    }
}
