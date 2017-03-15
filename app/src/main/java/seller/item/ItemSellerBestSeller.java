package seller.item;

import java.util.List;

import seller.graph.ChartCandidateType;

/**
 * Created by Administrator on 18/11/2559.
 */
public class ItemSellerBestSeller extends SellerBaseItem {

    public String collectionItemCode;
    public String collectionNet;
    public String collectionLastSumNet;

    public Double sum = 0.00;

    public List<ItemSellerBestSeller> forGraph = null;

    public int bestSellerCandidateType;
    public int bestSellerOrderType;

    public static ItemSellerBestSeller makeItem(int report_id) {
        return new ItemSellerBestSeller(report_id);
    }

    public ItemSellerBestSeller(int type) {
        super(type);
        bestSellerCandidateType = ChartCandidateType.TYPE_30_CANDIDATE;
        bestSellerOrderType = ChartCandidateType.TYPE_BEST_SELLER_TOP_10;
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

    public List<ItemSellerBestSeller> getForGraph() {
        return forGraph;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public Double getSum() {
        return sum;
    }

    public String getCollectionLastSumNet() {
        return collectionLastSumNet;
    }

    public void setCollectionLastSumNet(String collectionLastSumNet) {
        this.collectionLastSumNet = collectionLastSumNet;
    }

    public int getBestSellerCandidateType() {
        return bestSellerCandidateType;
    }

    public void setBestSellerCandidateType(int bestSellerCandidateType) {
        this.bestSellerCandidateType = bestSellerCandidateType;
    }

    public int getBestSellerOrderType() {
        return bestSellerOrderType;
    }

    public void setBestSellerOrderType(int bestSellerOrderType) {
        this.bestSellerOrderType = bestSellerOrderType;
    }
}
