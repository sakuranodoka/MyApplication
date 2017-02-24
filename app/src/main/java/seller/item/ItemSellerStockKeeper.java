package seller.item;

import java.util.List;

/**
 * Created by Administrator on 8/2/2560.
 */

public class ItemSellerStockKeeper extends SellerBaseItem {

    public String collection;
    public String stock;

    public List<ItemSellerStockKeeper> forGraph;

    public ItemSellerStockKeeper(int type) {
        super(type);
        this.forGraph = null;
    }

    public static ItemSellerStockKeeper makeItem(int report_id) { return new ItemSellerStockKeeper(report_id); }

    public String getCollection() { return collection; }

    public void setCollection(String collection) { this.collection = collection; }

    public String getStock() { return stock; }

    public void setStock(String stock) { this.stock = stock; }
}
