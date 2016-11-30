package com.example.administrator.myapplication;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import autocomplete.SetAutoCompleteView;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import seller.InterfaceOnItemClick;
import seller.InterfaceOnOption;
import seller.SellerAdapter;
import seller.SellerBaseItem;
import seller.SellerData;
import seller.SellerType;
import seller.ViewDialogProductDetail;
import seller.ViewDialogOption;
import seller.autocomplete.GetSellerTitle;
import seller.autocomplete.InterfaceTitleCallback;
import seller.autocomplete.SellerTitleDAO;
import seller.convert.data.ConvertContent;
import seller.item.ItemSellerTitle;
import seller.pojo.SellerBestSellerPOJO;
import seller.pojo.SellerCollectionPOJO;
import seller.services.prototype.InterfaceListen;
import seller.services.retrofit.ServiceCollection;

public class SellerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        InterfaceTitleCallback,
        InterfaceOnItemClick
{

    private RecyclerView sellerRecyclerView;

    private SellerAdapter sellerAdapter;

    private List<SellerBaseItem> listSellerBaseItem = new ArrayList<>();

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        DrawerLayout fullLayout = (DrawerLayout) getLayoutInflater().inflate(layoutResID, null);

        FrameLayout frameLayout = (FrameLayout) fullLayout.findViewById(R.id.content);

        getLayoutInflater().inflate(R.layout.activity_seller, frameLayout, true);

        super.setContentView(fullLayout);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new SetAutoCompleteView().setView(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        sellerRecyclerView = (RecyclerView) findViewById(R.id.sellerRecyclerView);
        sellerRecyclerView.setLayoutManager( new LinearLayoutManager(this) );

        sellerAdapter = new SellerAdapter(this);
        sellerAdapter.setInterfaceTitleCallback(this);
        sellerAdapter.setInterfaceOnItemClick(this);
        sellerRecyclerView.setAdapter(sellerAdapter);

        List<SellerTitleDAO> a = GetSellerTitle.getSellerTitleList();

        ItemSellerTitle itemSellerTitle = new ItemSellerTitle();
        itemSellerTitle.setListOptionValue(GetSellerTitle.getSellerTitleList());
        itemSellerTitle.setSettingsBackgroundColor(ContextCompat.getColor(getApplicationContext() , R.color.dark_honest_green) );
        itemSellerTitle.setSettingsTint(ContextCompat.getColor(getApplicationContext() , R.color.angel_white) );
        listSellerBaseItem.add(itemSellerTitle);

        sellerAdapter.setRecyclerAdapter(listSellerBaseItem);
        sellerAdapter.notifyDataSetChanged();
    }

    InterfaceListen interfaceListen = new InterfaceListen() {
        @Override
        public void onResponse(Object data, Retrofit retrofit) {
            if(data instanceof SellerCollectionPOJO) {
                clearData();

                SellerCollectionPOJO temp = (SellerCollectionPOJO) data;

                listSellerBaseItem.addAll(ConvertContent.listItemSellerCollection(temp) );

                sellerAdapter.setRecyclerAdapter(listSellerBaseItem);
                sellerAdapter.notifyDataSetChanged();

            } else if(data instanceof SellerBestSellerPOJO) {
                clearData();

                SellerBestSellerPOJO temp = (SellerBestSellerPOJO) data;

                if (SellerData.reportId == SellerType.TYPE_SELLER_BEST_SELLER) {
                    listSellerBaseItem.addAll(ConvertContent.listItemSellerBestSeller(temp));
                } else {
                    listSellerBaseItem.add(ConvertContent.itemSellerBestSellerGraph(temp));
                }

                sellerAdapter.setRecyclerAdapter(listSellerBaseItem);
                sellerAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onBodyError(ResponseBody responseBodyError) {

        }

        @Override
        public void onBodyErrorIsNull() {

        }

        @Override
        public void onFailure(Throwable t) {
            t.printStackTrace();
        }
    };

    InterfaceOnOption optionInterface = new InterfaceOnOption() {
        @Override
        public void GraphSelected(int graphId) {
            SellerData.reportId = graphId;
            new ServiceCollection().callServer(interfaceListen, SellerData.reportId, SellerData.shopCode, null);
        }
    };

    private void clearData() {
        int size = this.listSellerBaseItem.size();
        if (size > 1) {
            for (int i = 0; i < size-1; i++) {
                this.listSellerBaseItem.remove(1);
            }
            this.sellerAdapter.notifyItemRangeRemoved(0, size-1);
        }
    }

    private void setContentData() {
        new ServiceCollection().callServer(interfaceListen, SellerData.reportId, SellerData.shopCode, null);
    }

    // Implements method .........................
    // Implements from Seller Adapter when item's detail will be touch
    @Override
    public void onItemClickListener(String itemCode) {
        ViewDialogProductDetail alert = new ViewDialogProductDetail();
        alert.showDialog(this, itemCode);
    }

    // Implement from Seller Adapter when a title is done changing ...
    @Override
    public void onChangeTitleCallBack(int reportId) {
        SellerData.reportId = reportId;

        setContentData();
    }

    // Implement from Seller Adapter when a setting's icon in the title is done clicking ...
    @Override
    public void onTitleSettingClickCallBack(int reportId) {
        ViewDialogOption alert = new ViewDialogOption();
        alert.showDialog(this, optionInterface, reportId);
    }
}
