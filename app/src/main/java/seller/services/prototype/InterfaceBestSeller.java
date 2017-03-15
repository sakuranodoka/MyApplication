package seller.services.prototype;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;
import seller.pojo.SellerBestSellerPOJO;
import seller.pojo.SellerCollectionPOJO;

/**
 * Created by Administrator on 18/11/2559.
 */
public interface InterfaceBestSeller {

    //@GET("/~55160509/android/SimpleOrder/getbestseller.php")
    @GET("/Application/getBestSeller.php")
    Observable<SellerBestSellerPOJO> getBestSeller (
            @Query("shop_code") String shopCode
    );

    @GET("/Application/getBestSellerTwice.php")
    Observable<SellerBestSellerPOJO> getBestSellerByItemCode (
            @Query("shop_code") String shopCode,
            @Query("itemcode") String itemCode,
            @Query("dataSet") int dataSet
    );

    @GET("/Application/getBestSellerTwice.php")
    Observable<SellerBestSellerPOJO> getBestSellerTwice (
            @Query("shop_code") String shopCode,
            @Query("date_range") int dateRange,
            @Query("optional") int optional
    );

}
