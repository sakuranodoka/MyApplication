package seller.services.retrofit;

import android.util.Log;


import java.util.List;
import java.util.concurrent.TimeUnit;

import URL.ServiceURL;
import log.LogIndentify;
import okhttp3.OkHttpClient;
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
import retrofit.InterfaceListen;
import seller.services.prototype.InterfaceStorageDateCover;

/**
 * Created by Administrator on 18/11/2559.
 */
public class ServiceCollection {

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

    public void callServer(final InterfaceListen listener, int reportId, String shopCode, Object data) {

        int unitTiming = 600;

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
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

        if (reportId == TypeSellerReport.TYPE_SELLER_COLLECTION) {
            InterfaceCollection interfaceCollection = retrofit.create( InterfaceCollection.class );
            Observable<SellerCollectionPOJO> observable = null;
            if(data == null) {
                //observable = interfaceCollection.getStorageTwice(shopCode);
            } else {
                if(data instanceof List && ((List<String>) data).get(0).equals(SellerData._PURE_DATA_TRANSFER_PORT_)) {
                    observable = interfaceCollection.getStorageTwice(shopCode);
                } else {
                    observable = interfaceCollection.getStorageTwiceByItemCode(shopCode, "");
                }
            }

            this.styzf(observable, listener, retrofit);
        } else if (reportId == TypeSellerReport.TYPE_SELLER_BEST_SELLER) {
            InterfaceBestSeller interfaceBestSeller = retrofit.create( InterfaceBestSeller.class );
            Observable<SellerBestSellerPOJO> observable = null;

            if(data == null) {
            } else {
                if(data instanceof List && ((List<String>) data).get(0).equals(SellerData._PURE_DATA_TRANSFER_PORT_)) {
                    int BestSellerOptional = SellerData.reportOptional;
                    observable = interfaceBestSeller.getBestSellerTwice(shopCode, 0, BestSellerOptional);
                } else {
                    observable = interfaceBestSeller.getBestSellerByItemCode(shopCode, "", SellerData.dataSetNumber);
                }
            }

            this.styzf(observable, listener, retrofit);
        } else if (reportId == TypeSellerReport.TYPE_BEST_SELLER_MONTH_TO_DATE) {

            InterfaceBestSellerMonthToDate styzfInterface = retrofit.create( InterfaceBestSellerMonthToDate.class );
            Observable<List<SellerBestSellerMonthToDatePOJO>> observable = null;

            if(data == null) {

            } else {
                if(data instanceof List && ((List<String>) data).get(0).equals(SellerData._PURE_DATA_TRANSFER_PORT_)) {
                    observable = styzfInterface.getBestSellerMonthToDate(shopCode);
                }
            }

            this.styzf(observable, listener, retrofit);
        } else if (reportId == TypeSellerReport.TYPE_STORAGE_DATE_COVER) {

            InterfaceStorageDateCover styzfInterface = retrofit.create( InterfaceStorageDateCover.class );
            Observable<List<SellerStorageDateCoverPOJO>> observable = null;

            if(data == null) {

            } else {
                if(data instanceof List && (((List) data).get(0).equals(SellerData._PURE_DATA_TRANSFER_PORT_))) {
                    observable = styzfInterface.getStorageDateCover(shopCode);
                } else {
                    observable = styzfInterface.getSkuDayCover(shopCode, (String) ((List) data).get(0));
                }
            }

            this.styzf(observable, listener, retrofit);
        } else if (reportId == TypeSellerReport.TYPE_STORAGE_UNDEFINED_DAY_COVER) {

            InterfaceStorageDateCover styzfInterface = retrofit.create( InterfaceStorageDateCover.class );
            Observable<List<SellerStorageDateCoverPOJO>> observable = null;

            if(data == null) {

            } else {
                if(data instanceof List && (((List) data).get(0).equals(SellerData._PURE_DATA_TRANSFER_PORT_))) {
                    observable = styzfInterface.getSkuUndefinedDayCover(shopCode, "", "true");
                } else {
                    observable = styzfInterface.getSkuUndefinedDayCover(shopCode, (String) ((List) data).get(0), "true");
                }
            }

            this.styzf(observable, listener, retrofit);
        } else if (reportId == TypeSellerReport.TYPE_SELLER_SUB_SKU_DAY_COVER) {

            InterfaceStorageDateCover styzfInterface = retrofit.create( InterfaceStorageDateCover.class );
            Observable<List<SellerStorageDateCoverPOJO>> observable = null;

            if(data == null) {

            } else {
                if(data instanceof List) {
                    if (((List) data).get(0).equals(SellerData._PURE_DATA_TRANSFER_PORT_)) {}
                    else {
                        observable = styzfInterface.getSkuDayCover(shopCode, (String) ((List) data).get(0));
                    }
                } else {
                    // data ไม่ใช่ List
                }
            }

            this.styzf(observable, listener, retrofit);
        } else if (reportId == TypeSellerReport.TYPE_STOCK_KEEPER) {
            InterfaceCollection styzfInterface = retrofit.create( InterfaceCollection.class );
            Observable<List<SellerStockKeeperPOJO>> observable = null;

            if(data == null) {

            } else {
                if(data instanceof List) {
                    if (((List) data).get(0).equals(SellerData._PURE_DATA_TRANSFER_PORT_)) {
                        observable = styzfInterface.getStockKeeper(shopCode);
                    }
                } else {
                    // data ไม่ใช่ List
                }

                this.styzf(observable, listener, retrofit);
            }
        }
    }
}
