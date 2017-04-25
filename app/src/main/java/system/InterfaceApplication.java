package system;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface InterfaceApplication {
	@GET("/Application/getAppVersion.php")
	Observable<AppVersionPOJO> getAppVersion (
			  @Query("versionKey") String versionKey
	);
}
