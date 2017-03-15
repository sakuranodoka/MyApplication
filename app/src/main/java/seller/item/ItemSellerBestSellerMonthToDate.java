package seller.item;

import java.util.List;

/**
 * Created by Administrator on 19/1/2560.
 */
public class ItemSellerBestSellerMonthToDate extends SellerBaseItem {

    public String collectionItemCode;
    public String collectionNet;

    public List<ItemSellerBestSellerMonthToDate> forGraph;

    private double sum;

    public static ItemSellerBestSellerMonthToDate makeItem(int report_id) {
        return new ItemSellerBestSellerMonthToDate(report_id);
    }

    public ItemSellerBestSellerMonthToDate(int type) {
        super(type);
        this.forGraph = null;
        this.sum = 0;
    }

    public String getCollectionItemCode() {
        return collectionItemCode;
    }

    public void setCollectionItemCode(String collectionItemCode) {
        this.collectionItemCode = collectionItemCode;
    }

    public String getCollectionNet() {
        return collectionNet;
    }

    public void setCollectionNet(String collectionNet) {
        this.collectionNet = collectionNet;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }
}
