package location.services;

import android.util.Log;

import java.util.List;

import URL.ServiceURL;
import location.pojo.MacAddressPOJO;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import retrofit.InterfaceListen;

/**
 * Created by Administrator on 6/12/2559.
 */
public class LocationRetrofit {

    private void styzf(Observable<?> observable, final InterfaceListen listener, final Retrofit retrofit) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        Log.e("error", e.getMessage().toString());
                    }

                    @Override
                    public void onNext(Object data) {
                        if (data == null) {
                            listener.onBodyErrorIsNull();
                        } else {
                            listener.onResponse( data, retrofit );
                        }
                    }
                });
    }

    public void callServer(final InterfaceListen listener) {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServiceURL.SERVER_BASE_URL )
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        InterfaceGetMacAddress interfaceGetMacAddress = retrofit.create(InterfaceGetMacAddress.class);
        Observable<List<MacAddressPOJO>> observable = null;
        observable = interfaceGetMacAddress.getMacAddress();

        styzf(observable, listener, retrofit);
    }
}