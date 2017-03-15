package seller.item;

import java.util.List;

/**
 * Created by Administrator on 20/1/2560.
 */
public class ItemSellerStorageDateCover extends SellerBaseItem {

    public String collectionItemCode;
    public String collectionBal;
    public String collectionCandidateBal;
    public String collectionStorage;
    public String collectionXBar;
    public String collectionDayCover;

    public List<ItemSellerStorageDateCover> forGraph;

    public ItemSellerStorageDateCover(int type) {
        super(type);
        this.forGraph = null;
    }

    public static ItemSellerStorageDateCover makeItem(int report_id) {
        return new ItemSellerStorageDateCover(report_id);
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

    public String getCollectionCandidateBal() {
        return collectionCandidateBal;
    }

    public void setCollectionCandidateBal(String collectionCandidateBal) {
        this.collectionCandidateBal = collectionCandidateBal;
    }

    public String getCollectionStorage() {
        return collectionStorage;
    }

    public void setCollectionStorage(String collectionStorage) {
        this.collectionStorage = collectionStorage;
    }

    public String getCollectionXBar() {
        return collectionXBar;
    }

    public void setCollectionXBar(String collectionXBar) {
        this.collectionXBar = collectionXBar;
    }

    public String getCollectionDayCover() {
        return collectionDayCover;
    }

    public void setCollectionDayCover(String collectionDayCover) {
        this.collectionDayCover = collectionDayCover;
    }
}
