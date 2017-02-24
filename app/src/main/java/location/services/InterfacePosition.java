package location.services;

import location.pojo.PositionPOJO;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 22/2/2560.
 */

public interface InterfacePosition {
    @GET("/Application/getPosition.php")
    Observable<PositionPOJO> getPosition (
            @Query("mac_address") String mac_address
    );
}
