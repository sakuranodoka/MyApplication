package seller.services.prototype;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;
import seller.pojo.SellerCollectionPOJO;
import seller.pojo.SellerStockKeeperPOJO;

/**
 * Created by Administrator on 18/11/2559.
 */
public interface InterfaceCollection {

    @GET("/~55160509/android/SimpleOrder/getminor.php")
    Observable<SellerCollectionPOJO> getCollection(
            @Query("shop_code") String shopCode
    );

//    @GET("/~55160509/android/SimpleOrder/getminor.php")
//    Observable<SellerCollectionPOJO> getCollectionByItemCode(
//            @Query("shop_code") String shopCode,
//            @Query("itemcode") String itemCode
//    );

    @GET("/Application/getStorageTwice.php")
    Observable<SellerCollectionPOJO> getStorageTwice (
            @Query("shop_code") String shopCode
    );

    @GET("/Application/getStorageTwice.php")
    Observable<SellerCollectionPOJO> getStorageTwiceByItemCode (
            @Query("shop_code") String shopCode,
            @Query("itemcode") String itemCode
    );

    @GET("/Application/getStock.php")
    Observable<List<SellerStockKeeperPOJO>> getStockKeeper (
            @Query("shop_code") String shopCode
    );
}
