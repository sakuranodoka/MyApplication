package seller.services.prototype;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import seller.pojo.SellerBestSellerPOJO;
import seller.pojo.SellerStorageDateCoverPOJO;

/**
 * Created by Administrator on 20/1/2560.
 */
public interface InterfaceStorageDateCover {
    @GET("/Application/check_stock.php")
    Observable<List<SellerStorageDateCoverPOJO>> getStorageDateCover (
            @Query("shop_code") String shopCode
    );

    @GET("/Application/check_stock.php")
    Observable<List<SellerStorageDateCoverPOJO>> getSkuDayCover (
            @Query("shop_code") String shopCode,
            @Query("item") String item
    );

    @GET("/Application/getStorageDateCover.php")
    Observable<List<SellerStorageDateCoverPOJO>> getSkuUndefinedDayCover (
            @Query("shop_code") String shopCode,
            @Query("item") String item,
            @Query("getUndefinedDayCover") String isGet
    );
}
