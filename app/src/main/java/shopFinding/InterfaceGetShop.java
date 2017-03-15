package shopFinding;

import java.util.List;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 23/11/2559.
 */
public interface InterfaceGetShop {
    @POST("/Application/getShop.php")
    Observable<List<ShopDetailPOJO>> getShopDetail();
}
