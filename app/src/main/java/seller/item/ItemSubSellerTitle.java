package seller.item;

import java.util.List;

/**
 * Created by Administrator on 25/1/2560.
 */

public class ItemSubSellerTitle extends SellerBaseItem {

    public String storage;
    public String quantity;
    public String dayCover;

    public List<ItemSubSellerTitle> forGraph;

    public static ItemSubSellerTitle makeItem(int report_id) {
        return new ItemSubSellerTitle(report_id);
    }

    // Generator

    public ItemSubSellerTitle(int type) {
        super(type);
        this.forGraph = null;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDayCover() {
        return dayCover;
    }

    public void setDayCover(String dayCover) {
        this.dayCover = dayCover;
    }

    public List<ItemSubSellerTitle> getForGraph() {
        return forGraph;
    }

    public void setForGraph(List<ItemSubSellerTitle> forGraph) {
        this.forGraph = forGraph;
    }
}
