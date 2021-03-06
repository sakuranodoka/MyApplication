package retrofit;

import android.os.Bundle;
import android.util.Log;

import org.parceler.Parcels;

import java.util.List;
import java.util.concurrent.TimeUnit;

import URL.ServiceURL;
import authen.AuthenData;
import authen.AuthenticatePOJO;
import authen.InterfaceAuthen;
import invoice.BillPOJO;
import invoice.InterfaceInvoice;
import invoice.InvoiceData;
import invoice.InvoicePOJO;
import invoice.ParcelQuery;
import invoice.ServiceBill;
import invoice.item.ParcelInvoice;
import location.LocationData;
import location.pojo.GeoCoderPOJO;
import location.services.InterfaceLocation;
import log.LogIndentify;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import system.AppVersionPOJO;
import system.InterfaceApplication;
import system.UpdateApplication;

public class ServiceRetrofit {

	 private void styzf(Observable<?> observable, final InterfaceListen listener, final Retrofit retrofit) {
		   if(observable != null) {
			   observable.subscribeOn(Schedulers.io())
					     .observeOn(AndroidSchedulers.mainThread())
					     .subscribe(new Observer<Object>() {
						     @Override
						     public void onCompleted() {
						     }

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
								     listener.onResponse(data, retrofit);
							     }
						     }
					     });
		   } else {
			   Log.e("system", "error because observable is null");
		   }
	 }

	 public void callServer(final InterfaceListen listener, int mode, Object data) {
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

		Observable<?> observable = null;

		switch(mode) {
			case RetrofitAbstract.RETROFIT_SELF_UPDATE:
				InterfaceApplication interfaceApplication = retrofit.create(InterfaceApplication.class);
				if(data instanceof Bundle) {
					Bundle x = (Bundle) data;
					Observable<AppVersionPOJO> ex = interfaceApplication.getAppVersion(x.getString(UpdateApplication.BUNDLE_Version_Key_Tag));
					observable = ex;
				}
				break;
			case RetrofitAbstract.RETROFIT_AUTHEN :
				InterfaceAuthen interfaceAuthen = retrofit.create(InterfaceAuthen.class);
				if(data instanceof Bundle) {
					 Bundle x = (Bundle) data;
					 Observable<List<AuthenticatePOJO>> ex = interfaceAuthen.authenticate(x.getString(AuthenData.USERNAME), x.getString(AuthenData.PASSWORD));
					 observable = ex;
				}
				break;
			case RetrofitAbstract.RETROFIT_SIGN_UP :
				InterfaceAuthen interfaceSignup = retrofit.create(InterfaceAuthen.class);
				if(data instanceof Bundle) {
					 Bundle x = (Bundle) data;
					 Observable<List<AuthenticatePOJO>> ex = interfaceSignup.register(x.getString(AuthenData.FULLNAME), x.getString(AuthenData.USERNAME), x.getString(AuthenData.PASSWORD));
					 observable = ex;
				}
				break;
			case RetrofitAbstract.RETROFIT_INVOICE :
				InterfaceInvoice interfaceInvoice = retrofit.create(InterfaceInvoice.class);
				if(data instanceof Bundle)
					observable = null;
				break;
			case RetrofitAbstract.RETROFIT_PRE_INVOICE:
				observable = ServiceBill.getBillList((Bundle) data, retrofit);
			   break;
			case RetrofitAbstract.RETROFIT_SET_BILL_COUNT:
				observable = ServiceBill.setBillCount((Bundle) data, retrofit);
				break;
			case RetrofitAbstract.RETROFIT_SET_COMPLETE_BILL:
				observable = ServiceBill.setCompleteBill((Bundle) data, retrofit);
				break;
			case RetrofitAbstract.RETROFIT_GEOCODING:

				retrofit = new Retrofit.Builder()
					   .baseUrl("http://maps.googleapis.com")
					   .client(okHttpClient)
					   .addConverterFactory(GsonConverterFactory.create())
					   .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
					   .build();

				if(data instanceof Bundle) {
					Bundle sB = (Bundle) data;

					String latLng = sB.getString(LocationData.lat)+","+sB.getString(LocationData.lng);

					InterfaceLocation interfaceLocation = retrofit.create(InterfaceLocation.class);
					Observable<GeoCoderPOJO> ex = interfaceLocation.getGeoCoder(latLng, false, "th");
					observable = ex;
				}
			   break;
			case RetrofitAbstract.RETROFIT_STATIC_GPS:
				observable = ServiceBill.setGPS((Bundle) data, retrofit);
				break;
		}
		this.styzf(observable, listener, retrofit);
	 }
}
