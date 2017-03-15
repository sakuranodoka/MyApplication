package seller.services.prototype;

import java.util.Iterator;
import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import seller.pojo.SellerBestSellerMonthToDatePOJO;
import seller.pojo.SellerBestSellerPOJO;

/**
 * Created by Administrator on 18/1/2560.
 */
public interface InterfaceBestSellerMonthToDate {

    @GET("/Application/getBestSellerMonthToDate.php")
    Observable<List<SellerBestSellerMonthToDatePOJO>> getBestSellerMonthToDate (
            @Query("shop_code") String shopCode
    );
}
