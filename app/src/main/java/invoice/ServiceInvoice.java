package invoice;

import android.os.Bundle;
import android.os.Debug;
import android.provider.Settings;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.IOException;
import java.security.Security;
import java.util.List;
import java.util.concurrent.TimeUnit;

import URL.ServiceURL;
import invoice.item.ItemInvoicePreview;
import invoice.item.ParcelInvoice;
import log.LogIndentify;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okio.BufferedSink;
import retrofit.DataWrapper;
import retrofit.InterfaceListen;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import seller.SellerData;
import seller.TypeSellerReport;
import seller.pojo.SellerBestSellerMonthToDatePOJO;
import seller.pojo.SellerBestSellerPOJO;
import seller.pojo.SellerCollectionPOJO;
import seller.pojo.SellerStockKeeperPOJO;
import seller.pojo.SellerStorageDateCoverPOJO;
import seller.services.prototype.InterfaceBestSeller;
import seller.services.prototype.InterfaceBestSellerMonthToDate;
import seller.services.prototype.InterfaceCollection;
import seller.services.prototype.InterfaceStorageDateCover;

/**
 * Created by Administrator on 1/3/2560.
 */

public class ServiceInvoice {

    private void styzf(Observable<?> observable, final InterfaceListen listener, final Retrofit retrofit) {
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<Object>() {
                @Override
                public void onCompleted() {}

                @Override
                public void onError(Throwable e) {
                    Log.e("error", e.getMessage().toString() + " " + LogIndentify._LOG_RETROFIT_ERROR_[1]);
                }

                @Override
                public void onNext(Object data) {
                    if (data == null) {
                        Log.e("error", "Retrofit got error is null.");
                        listener.onBodyErrorIsNull();
                    } else {
                        listener.onResponse( data, retrofit );
                    }
                }
            });
    }

    public void callServer(final InterfaceListen listener, int method, Object data) {

        int unitTiming = 600;

        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder()
                .connectTimeout(unitTiming, TimeUnit.SECONDS)
                .readTimeout(unitTiming, TimeUnit.SECONDS)
                .writeTimeout(unitTiming, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServiceURL.PROCUCT_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        InterfaceInvoice interfaceInvoice = retrofit.create( InterfaceInvoice.class );

        Observable<List<DataWrapper>> observable = null;
        if( data == null ) {
        } else {
            if( data instanceof Bundle) {
							 	// Clone Bundle
	                     Bundle b = (Bundle) data;

							  // Unwrap Parcelable to Object
							  ParcelInvoice p = Parcels.unwrap(b.getParcelable(InvoiceData.INVOICE_PARCEL));

							 	Gson gson = new GsonBuilder().create();
							 	JsonArray cVzsFx = gson.toJsonTree(p.getListInvoice()).getAsJsonArray();
							 	RequestBody jsonItmInC4 = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), cVzsFx.toString());

							 // Create Request body From Bitmap String
							  RequestBody base64bMPx1 = RequestBody.create(MediaType.parse("text/plain"), p.getBitmap());

							 	RequestBody latitude = RequestBody.create(MediaType.parse("text/plain"), p.getLatitude());

							 	RequestBody longitude = RequestBody.create(MediaType.parse("text/plain"), p.getLongitude());

							 	RequestBody userID = RequestBody.create(MediaType.parse("text/plain"), p.getUserID());

							 	RequestBody userFullName = RequestBody.create(MediaType.parse("text/plain"), p.getUserFullName());

	                     RequestBody username = RequestBody.create(MediaType.parse("text/plain"), p.getUsername());

							  // Put data to interface
							 	observable = interfaceInvoice.sendBmPx1Server(jsonItmInC4,
												latitude,
												longitude,
												userID,
												base64bMPx1,
												userFullName,
										      username);
            }

					 	this.styzf(observable, listener, retrofit);
        }
    }
}
