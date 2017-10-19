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
import seller.pojo.SellerCollectionPOJO;

public interface InterfaceInvoice {
	 @Multipart
	 @POST("/Application/uploadSignature.php")
	 Observable<List<DataWrapper>> sendBmPx1Server (
			@Part("jsonItmInC4") RequestBody jsonItmInC4,
//			@Part("latitude") RequestBody latitude,
//			@Part("longitude") RequestBody longitude,
			@Part("userID") RequestBody userID,
			@Part("base64bMPx1") RequestBody base64bMPx1,
			@Part("userFullName") RequestBody userFullName,
			@Part("username") RequestBody username
	 );

	//@GET("/Application/getInvoiceInfo.php")
//	@GET("/Application/tracking/services/getInvoiceInfo.php")
//	Observable<List<InvoicePOJO>> getInvoiceInfo (
//		  @Query("username") String username,
//		  @Query("timestack") String timestack,
//		  @Query("limit") String limit
//	);

	@GET("/Application/tracking/services/getInvoiceInfo.php")
	Observable<List<InvoicePOJO>> getInvoiceInfo (
			  @Query("username") String username,
			  @Query("bill") String bill,
			  @Query("timestack") String timestack,
			  @Query("limit") String limit
	);

	@Multipart
	@POST("/Application/tracking/services/getInvoiceInfo.home.php")
	Observable<List<BillPOJO>> getBillList (
			  @Part("BILL_NO") RequestBody BILL_NO,
			  @Part("BILL_DATE") RequestBody BILL_DATE,
			  @Part("SHIP_NO") RequestBody SHIP_NO,
			  @Part("LIMIT") RequestBody LIMIT
	);

	@Multipart
	@POST("/Application/tracking/services/setInvoiceInfo.home.php")
	Observable<DataWrapper> setBillCount (
		  @Part("BILL_NO") RequestBody BILL_NO,
		  @Part("BILL_COUNT") RequestBody BILL_COUNT
	);

	@Multipart
	@POST("/Application/tracking/services/setCompleteBill.home.php")
	Observable<DataWrapper> setCompleteBill (
			  @Part("FK_BILL_NO") RequestBody FK_BILL_NO,
			  @Part("LATITUTE") RequestBody LATITUTE,
			  @Part("LONGITUTE") RequestBody LONGITUTE,
			  @Part("COUNTING") RequestBody COUNTING,
			  @Part("SIGN_NAME") RequestBody SIGN_NAME,
	        @Part("BITMAP_PATH") RequestBody BITMAP_PATH
	);
}
