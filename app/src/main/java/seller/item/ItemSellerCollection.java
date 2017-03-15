package seller.item;

import java.util.List;

/**
 * Created by Administrator on 18/11/2559.
 */
public class ItemSellerCollection extends SellerBaseItem {

    public String collectionItemCode;
    public String collectionBal;
    public String collectionNet;

    public List<ItemSellerCollection> forGraph;

    public ItemSellerCollection(int report_id) {
        super(report_id);
    }

    public static ItemSellerCollection makeItem(int report_id) {
        return new ItemSellerCollection(report_id);
    }

    public String getCollectionItemCode() {
        return collectionItemCode;
    }

    public void setCollectionItemCode(String collectionItemCode) {
        this.collectionItemCode = collectionItemCode;
    }

    public String getCollectionBal() {
        return collectionBal;
    }

    public void setCollectionBal(String collectionBal) {
        this.collectionBal = collectionBal;
    }

    public String getCollectionNet() {
        return collectionNet;
    }

    public void setCollectionNet(String collectionNet) {
        this.collectionNet = collectionNet;
    }

    public List<ItemSellerCollection> getForGraph() {
        return forGraph;
    }

    public void setForGraph(List<ItemSellerCollection> forGraph) {
        this.forGraph = forGraph;
    }
}
