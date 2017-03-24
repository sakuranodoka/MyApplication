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
import invoice.InterfaceInvoice;
import invoice.InvoiceData;
import invoice.InvoicePOJO;
import invoice.item.ParcelInvoice;
import log.LogIndentify;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 16/3/2560.
 */

public class ServiceRetrofit {

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

	 public void callServer(final InterfaceListen listener, int mode, Object data) {

			int unitTiming = 600;

			OkHttpClient okHttpClient = new OkHttpClient()
							.newBuilder()
							.connectTimeout(unitTiming, TimeUnit.SECONDS)
							.readTimeout(unitTiming, TimeUnit.SECONDS)
							.writeTimeout(unitTiming, TimeUnit.SECONDS)
							.build();

			final Retrofit retrofit = new Retrofit.Builder()
							.baseUrl(ServiceURL.PROCUCT_BASE_URL)
							.client(okHttpClient)
							.addConverterFactory(GsonConverterFactory.create())
							.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
							.build();

			Observable<?> observable = null;

			switch(mode) {
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
					   InterfaceInvoice interfacePreInvoice = retrofit.create(InterfaceInvoice.class);
						if(data instanceof Bundle) {
							Bundle x = (Bundle) data;
							ParcelInvoice p = Parcels.unwrap(x.getParcelable(InvoiceData.INVOICE_PARCEL));
							Observable<List<InvoicePOJO>> ex = interfacePreInvoice.getInvoiceInfo(p.getUsername());
							observable = ex;
						}
				 	   break;
			}
			this.styzf(observable, listener, retrofit);
	 }
}
