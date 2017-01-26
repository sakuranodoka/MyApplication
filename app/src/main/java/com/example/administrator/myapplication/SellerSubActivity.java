package com.example.administrator.myapplication;

import android.support.annotation.LayoutRes;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import AppBar.ApplicationBar;
import AppBar.BarType;
import okhttp3.ResponseBody;
import retrofit.InterfaceListen;
import retrofit2.Retrofit;
import seller.CustomLinearLayoutManager;
import seller.SellerData;
import seller.SellerSubAdapter;
import seller.TypeSellerReport;
import seller.convert.data.ConvertContent;
import seller.graph.BarChartData;
import seller.item.ItemSubSellerTitle;
import seller.item.SellerBaseItem;
import seller.pojo.SellerBestSellerMonthToDatePOJO;
import seller.pojo.SellerStorageDateCoverPOJO;
import seller.services.retrofit.ServiceCollection;

public class SellerSubActivity extends AppCompatActivity {

    private int reportId;
    private String item;
    private String shopCode;

    private int innerType;

    private RecyclerView sellerSubRecyclerView;

    private SellerSubAdapter sellerSubAdapter;

    private List<SellerBaseItem> listSellerBaseItem = new ArrayList<>();

    public SellerSubActivity() {
        this.reportId = 0;
        this.item = "";
        this.shopCode = "EMPTY";
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        DrawerLayout fullLayout = (DrawerLayout) getLayoutInflater().inflate(layoutResID, null);

        FrameLayout frameLayout = (FrameLayout) fullLayout.findViewById(R.id.content);

        getLayoutInflater().inflate(R.layout.activity_seller_sub, frameLayout, true);

        super.setContentView(fullLayout);
        if(item != null && !item.equals("")) {
            new ApplicationBar(this, BarType.TYPE_APPLICATION_NAME).setAppNameWithBackPressed(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = this.getIntent().getExtras();
        if (b != null) {
            this.item = b.getString("item");
            this.reportId = b.getInt("reportId");
            this.shopCode = b.getString("shopCode");
        }

        setContentView(R.layout.activity_main);

        sellerSubRecyclerView = (RecyclerView) findViewById(R.id.sellerSubRecyclerView);

        CustomLinearLayoutManager customLayoutManager = new CustomLinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);

        sellerSubRecyclerView.setLayoutManager( customLayoutManager );

        sellerSubRecyclerView.setNestedScrollingEnabled(false);

        sellerSubAdapter = new SellerSubAdapter();

        sellerSubRecyclerView.setAdapter(sellerSubAdapter);

        setContentTitle();

        setContentData(TypeSellerReport.TYPE_SELLER_SUB_SKU_DAY_COVER);
    }

    private InterfaceListen interfaceListen = new InterfaceListen() {
        @Override
        public void onResponse(Object data, Retrofit retrofit) {

            if(data instanceof List && (((List) data) != null) && (((List) data).get(0) instanceof SellerStorageDateCoverPOJO) ) {

                List<SellerStorageDateCoverPOJO> temp = (List <SellerStorageDateCoverPOJO>) data;

                listSellerBaseItem.add(ConvertContent.itemSellerStorageDateCoverGraph(temp, TypeSellerReport.TYPE_REPORT_BAR));

                sellerSubAdapter.setRecyclerAdapter(listSellerBaseItem);

                sellerSubAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onBodyError(ResponseBody responseBodyError) {Log.e("error", "body error" + responseBodyError.toString());}

        @Override
        public void onBodyErrorIsNull() {Log.e("error", "body is null");}

        @Override
        public void onFailure(Throwable t) {t.printStackTrace();}
    };

    private void setContentTitle() {



//        listSellerBaseItem.add(new ItemSubSellerTitle(TypeSellerReport.TYPE_SELLER_SUB_TITLE));
//
//        sellerSubAdapter.setRecyclerAdapter(listSellerBaseItem);
//
//        sellerSubAdapter.notifyDataSetChanged();
    }

    private void setContentData(int subSellerReport) {
        List<String> listData = new ArrayList<>();
        listData.add(this.item);
        new ServiceCollection().callServer(interfaceListen, subSellerReport, shopCode, listData);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BarChartData.setUpDialogDetail.onProductDetailDestroy();
    }
}
