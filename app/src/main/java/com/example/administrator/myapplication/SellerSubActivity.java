package com.example.administrator.myapplication;

import android.support.annotation.LayoutRes;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;

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
    private String stock;
    private String stockUnit;
    private String XBar;
    private String XBarUnit;
    private String dayCover;
    private String dayCoverUnit;

    private int innerType;

    private RecyclerView sellerSubRecyclerView;

    private SellerSubAdapter sellerSubAdapter;

    private List<SellerBaseItem> listSellerBaseItem = new ArrayList<>();

    public SellerSubActivity() {
        this.reportId = 0;
        this.item = "";
        this.shopCode = "EMPTY";

        this.stockUnit = "ชิ้น";//getApplicationContext().getResources().getString(R.string.currency_piece_th);
        this.XBarUnit = "ชิ้น";//getApplicationContext().getResources().getString(R.string.currency_piece_th);
        this.dayCoverUnit = "วัน";//getApplicationContext().getResources().getString(R.string.currency_date_th);
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

            this.stock = b.getString("stock");
            this.XBar = b.getString("XBar");
            this.dayCover = b.getString("dayCover");
        }

        setContentView(R.layout.activity_main);

        sellerSubRecyclerView = (RecyclerView) findViewById(R.id.sellerSubRecyclerView);

        CustomLinearLayoutManager customLayoutManager = new CustomLinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);

        sellerSubRecyclerView.setLayoutManager( new LinearLayoutManager(this) );

        sellerSubRecyclerView.setNestedScrollingEnabled(false);

        sellerSubAdapter = new SellerSubAdapter();

        sellerSubRecyclerView.setAdapter(sellerSubAdapter);

        setContentTitle();
    }

    @Override
    protected void onResume() {
        super.onResume();

        setContentData(this.reportId);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState == null) {
            outState = new Bundle();
        } else {
            outState.putString("item", this.item);
            outState.putInt("reportId", this.reportId);
            outState.putString("shopCode", this.shopCode);

            outState.putString("stock", this.stock);
            outState.putString("XBar", this.XBar);
            outState.putString("dayCover", this.dayCover);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null) {
            if (savedInstanceState.containsKey("item")) { this.item = savedInstanceState.getString("item"); }
            if (savedInstanceState.containsKey("reportId")) { this.reportId = savedInstanceState.getInt("reportId"); }
            if (savedInstanceState.containsKey("shopCode")) { this.shopCode = savedInstanceState.getString("shopCode"); }
            if (savedInstanceState.containsKey("stock")) { this.stock = savedInstanceState.getString("item"); }
            if (savedInstanceState.containsKey("XBar")) { this.XBar = savedInstanceState.getString("XBar"); }
            if (savedInstanceState.containsKey("dayCover")) { this.dayCover = savedInstanceState.getString("dayCover"); }
        }
    }

    private InterfaceListen interfaceListen = new InterfaceListen() {
        @Override
        public void onResponse(Object data, Retrofit retrofit) {

            if(data instanceof List && (((List) data) != null) && (((List) data).get(0) instanceof SellerStorageDateCoverPOJO) ) {

                List<SellerStorageDateCoverPOJO> temp = (List <SellerStorageDateCoverPOJO>) data;

                //listSellerBaseItem.add(ConvertContent.itemSellerStorageDateCoverGraph(temp, TypeSellerReport.TYPE_REPORT_BAR));
                listSellerBaseItem.addAll(ConvertContent.itemSellerStorageDateCover(temp, reportId));

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

        TextView stock = (TextView) findViewById(R.id.stock);
        TextView XBar = (TextView) findViewById(R.id.XBar);
        TextView dayCover = (TextView) findViewById(R.id.day_cover);

        stock.setText(this.stock+" "+this.stockUnit);
        if(!this.XBar.equals("0.0001")) {
            XBar.setText(this.XBar + " " + this.XBarUnit);
        } else {
            XBar.setText("-");
        }
        dayCover.setText((int) Math.floor( Float.parseFloat(this.dayCover))+" "+this.dayCoverUnit);

//        listSellerBaseItem.add(new ItemSubSellerTitle(TypeSellerReport.TYPE_SELLER_SUB_TITLE));
//
//        sellerSubAdapter.setRecyclerAdapter(listSellerBaseItem);
//
//        sellerSubAdapter.notifyDataSetChanged();
    }

    private void setContentData(int subSellerReport) {

        clearData();

        List<String> listData = new ArrayList<>();
        listData.add(this.item);
        new ServiceCollection().callServer(interfaceListen, this.reportId, shopCode, listData);
    }

    private void clearData() {

        this.listSellerBaseItem = new ArrayList<>();

        sellerSubAdapter.setRecyclerAdapter(listSellerBaseItem);

        sellerSubAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BarChartData.setUpDialogDetail.onProductDetailDestroy();
    }
}
