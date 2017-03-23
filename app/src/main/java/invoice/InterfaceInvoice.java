package invoice;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import invoice.item.ItemInvoicePreview;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit.DataWrapper;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 1/3/2560.
 */

public interface InterfaceInvoice {
	 @Multipart
	 @POST("/Application/uploadSignature.php")
	 Observable<List<DataWrapper>> sendBmPx1Server (
			@Part("jsonItmInC4") RequestBody jsonItmInC4,
			@Part("latitude") RequestBody latitude,
			@Part("longitude") RequestBody longitude,
			@Part("userID") RequestBody userID,
			@Part("base64bMPx1") RequestBody base64bMPx1,
			@Part("userFullName") RequestBody userFullName,
			@Part("username") RequestBody username
	 );
}
