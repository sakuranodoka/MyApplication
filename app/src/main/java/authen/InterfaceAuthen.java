package authen;

import java.util.List;

import okhttp3.RequestBody;
import retrofit.DataWrapper;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

public interface InterfaceAuthen {

	 @FormUrlEncoded
	 @POST("/Application/authen.php")
	 Observable<List<AuthenticatePOJO>> authenticate (
		 @Field("username") String username,
		 @Field("password") String password
	 );

	 @FormUrlEncoded
	 @POST("/Application/signup.php")
	 Observable<List<AuthenticatePOJO>> register (
					 @Field("name") String name,
					 @Field("username") String username,
					 @Field("password") String password
	 );

	 interface innerApp {
			void onLogout();
	 }
}
