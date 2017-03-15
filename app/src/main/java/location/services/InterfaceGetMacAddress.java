package location.services;

import java.util.List;

import location.pojo.MacAddressPOJO;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import seller.pojo.SellerBestSellerPOJO;

/**
 * Created by Administrator on 6/12/2559.
 */
public interface InterfaceGetMacAddress {

    @GET("/~55160509/android/SimpleOrder/getmacaddress.php")
    Observable<List<MacAddressPOJO>> getMacAddress ();

}
