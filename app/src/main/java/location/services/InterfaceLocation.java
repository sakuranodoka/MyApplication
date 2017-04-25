package location.services;

import java.util.List;

import invoice.InvoicePOJO;
import location.pojo.GeoCoderPOJO;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface InterfaceLocation {

	@GET("/maps/api/geocode/json")
	Observable<GeoCoderPOJO> getGeoCoder (
			  @Query("latlng") String laglng,
			  @Query("sensor") boolean sensor,
			  @Query("language") String language
	);
}
