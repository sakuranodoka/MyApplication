package seller.convert.data;

import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import seller.TypeSellerReport;
import seller.item.ItemSellerBestSeller;
import seller.item.ItemSellerBestSellerMonthToDate;
import seller.item.ItemSellerCollection;
import seller.item.ItemSellerLoadingScreen;
import seller.item.ItemSellerStockKeeper;
import seller.item.ItemSellerStorageDateCover;
import seller.pojo.SellerBestSellerMonthToDatePOJO;
import seller.pojo.SellerBestSellerPOJO;
import seller.pojo.SellerCollectionPOJO;
import seller.pojo.SellerStockKeeperPOJO;
import seller.pojo.SellerStorageDateCoverPOJO;

/**
 * Created by Administrator on 18/11/2559.
 */
public class ConvertContent {
    public static List<ItemSellerCollection> listItemSellerCollection(SellerCollectionPOJO sellerCollectionPOJO) {
        List<ItemSellerCollection> temp = new ArrayList<>();
        for(SellerCollectionPOJO.Data eachData : sellerCollectionPOJO.getData()) {
            ItemSellerCollection itemSellerCollection = new ItemSellerCollection(TypeSellerReport.TYPE_SELLER_COLLECTION);
            itemSellerCollection.setCollectionItemCode( eachData.getCollection() );
            itemSellerCollection.setCollectionBal( eachData.getBAL() );
            //itemSellerCollection.setCollectionNet( eachData.getNet() );
            temp.add(itemSellerCollection);
        }
        return temp;
    }

    public static ItemSellerCollection itemSellerCollectionGraph(SellerCollectionPOJO sellerCollectionPOJO, int reportId) {
        ItemSellerCollection temp = new ItemSellerCollection(reportId);

        temp.forGraph = new ArrayList<>();
        try {
            for (SellerCollectionPOJO.Data eachData : sellerCollectionPOJO.getData()) {
                ItemSellerCollection itemSellerCollection = new ItemSellerCollection(reportId);
                itemSellerCollection.setCollectionItemCode( eachData.getCollection() );
                itemSellerCollection.setCollectionBal( eachData.getBAL() );

                temp.forGraph.add(itemSellerCollection);
            }
        } catch (NumberFormatException e ) {
            Log.e("NumberFormatError", e.getMessage());
        }
        return temp;
    }

    public static List<ItemSellerBestSeller> listItemSellerBestSeller(SellerBestSellerPOJO sellerBestSellerPOJO) {
        List<ItemSellerBestSeller> temp = new ArrayList<>();
        for(SellerBestSellerPOJO.Data eachData : sellerBestSellerPOJO.getData()) {
            ItemSellerBestSeller itemSellerBestSeller = ItemSellerBestSeller.makeItem(TypeSellerReport.TYPE_SELLER_BEST_SELLER);
            itemSellerBestSeller.setCollectionNet( eachData.getNet() );
            itemSellerBestSeller.setCollectionItemCode( eachData.getCollection() );
            temp.add(itemSellerBestSeller);
        }
        return temp;
    }

    public static ItemSellerBestSeller itemSellerBestSellerGraph(SellerBestSellerPOJO sellerBestSellerPOJO, int reportId) {
        // แก้ไอตรงนี้ด้วยนะ 30/11/59
        ItemSellerBestSeller temp = ItemSellerBestSeller.makeItem( reportId );
        temp.forGraph = new ArrayList<>();
        Double sum = 0.00;
        try {
            for (SellerBestSellerPOJO.Data eachData : sellerBestSellerPOJO.getData()) {
                ItemSellerBestSeller itemSellerBestSeller = ItemSellerBestSeller.makeItem( reportId );
                itemSellerBestSeller.setCollectionNet(eachData.getNet());
                itemSellerBestSeller.setCollectionItemCode(eachData.getCollection());
                itemSellerBestSeller.setCollectionLastSumNet(eachData.getLastSumNet());
                sum+= Double.parseDouble( eachData.getNet().replaceAll("[$,]", "") );
                temp.forGraph.add(itemSellerBestSeller);
            }
        } catch (NumberFormatException e ) {
            Log.e("NumberFormatError", e.getMessage());
            sum = 0.00;
        }
        temp.setSum(sum);
        return temp;
    }

    public static ItemSellerBestSellerMonthToDate itemSellerBestSellerMonthToDateGraph(List <SellerBestSellerMonthToDatePOJO> pojo, int reportId) {
        ItemSellerBestSellerMonthToDate temp = ItemSellerBestSellerMonthToDate.makeItem( reportId );
        temp.forGraph = new ArrayList<>();

        int i =0;
        for(SellerBestSellerMonthToDatePOJO eachData : pojo) {
            ItemSellerBestSellerMonthToDate inner = ItemSellerBestSellerMonthToDate.makeItem( reportId );

            inner.setCollectionItemCode(eachData.getCollection());
            inner.setCollectionNet(eachData.getNet().replaceAll("[$,]", ""));
            temp.forGraph.add(inner);
            i++;
        }
        return temp;
    }

    public static List<ItemSellerStorageDateCover> itemSellerStorageDateCover(List <SellerStorageDateCoverPOJO> pojo, int reportId) {
        List<ItemSellerStorageDateCover>  temp = new ArrayList<>();

        for(SellerStorageDateCoverPOJO eachData : pojo) {
            ItemSellerStorageDateCover inner = new ItemSellerStorageDateCover(reportId);

            if(eachData.getCollection() != null) { inner.setCollectionItemCode(eachData.getCollection()); }

            if(eachData.getDayCover() != null) { inner.setCollectionDayCover(eachData.getDayCover()); }

            if(eachData.getCandidateBal() != null) { inner.setCollectionCandidateBal(eachData.getCandidateBal()); }

            if(eachData.getXBar() != null) { inner.setCollectionXBar(eachData.getXBar()); }

            if(eachData.getStorage() != null) { inner.setCollectionStorage(eachData.getStorage()); }

            temp.add(inner);
        }
        return temp;
    }

    public static ItemSellerStorageDateCover itemSellerStorageDateCoverGraph(List <SellerStorageDateCoverPOJO> pojo, int reportId) {
        ItemSellerStorageDateCover temp = ItemSellerStorageDateCover.makeItem( reportId );
        temp.forGraph = new ArrayList<>();

        int i = 0;
        for(SellerStorageDateCoverPOJO eachData : pojo) {
            ItemSellerStorageDateCover inner = ItemSellerStorageDateCover.makeItem( reportId );

            inner.setCollectionItemCode(eachData.getCollection());
            inner.setCollectionDayCover(eachData.getDayCover());
            inner.setCollectionCandidateBal(eachData.getCandidateBal());
            temp.forGraph.add(inner);
            i++;
        }
        return temp;
    }

    public static ItemSellerStockKeeper itemSellerStockKeeperGraph(List <SellerStockKeeperPOJO> pojo, int reportId) {

        ItemSellerStockKeeper temp = ItemSellerStockKeeper.makeItem( reportId );

        temp.forGraph = new ArrayList<>();

        for(SellerStockKeeperPOJO eachData : pojo) {
            ItemSellerStockKeeper inner = ItemSellerStockKeeper.makeItem( reportId );

            if(eachData.getCollection() != null) { inner.setCollection(eachData.getCollection()); }
            if(eachData.getStock() != null) { inner.setStock(eachData.getStock()); }

            temp.forGraph.add(inner);
        }
        return temp;
    }

    public static ItemSellerLoadingScreen getLoadingScreenItem() {
        ItemSellerLoadingScreen temp = new ItemSellerLoadingScreen();
        return temp;
    }
}
