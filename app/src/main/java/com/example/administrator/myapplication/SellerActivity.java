package com.example.administrator.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
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
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import autocomplete.InstantAutocomplete;
import autocomplete.InterfaceOnShop;
import autocomplete.SetAutoCompleteView;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import seller.CustomLinearLayoutManager;
import seller.InterfaceOnItem;
import seller.InterfaceOnOption;
import seller.SellerAdapter;
import seller.SellerBaseItem;
import seller.SellerData;
import seller.SellerGraphType;
import seller.SellerReportDialogFragment;
import seller.SellerType;
import seller.ViewDialogProductDetail;
import seller.ViewDialogOption;
import seller.autocomplete.GetSellerTitle;
import seller.autocomplete.InterfaceTitleCallback;
import seller.autocomplete.SellerTitleDAO;
import seller.convert.data.ConvertContent;
import seller.item.ItemSellerDescription;
import seller.item.ItemSellerTitle;
import seller.pojo.SellerBestSellerPOJO;
import seller.pojo.SellerCollectionPOJO;
import retrofit.InterfaceListen;
import seller.services.retrofit.ServiceCollection;
import seller.viewholder.DialogFragmentGraphOption;

public class SellerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        InterfaceTitleCallback,
        InterfaceOnItem,
        InterfaceOnShop
{

    private String shopName = "";
    private String reportName = "";
    private String date = "";
    private String dateExtended = "";

    private final FragmentManager fm = getSupportFragmentManager();

    private RecyclerView sellerRecyclerView;

    private SellerAdapter sellerAdapter;

    private List<SellerBaseItem> listSellerBaseItem = new ArrayList<>();

    private ActionBarDrawerToggle toggle;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        DrawerLayout fullLayout = (DrawerLayout) getLayoutInflater().inflate(layoutResID, null);

        FrameLayout frameLayout = (FrameLayout) fullLayout.findViewById(R.id.content);

        getLayoutInflater().inflate(R.layout.activity_seller, frameLayout, true);

        super.setContentView(fullLayout);

        RelativeLayout shopSearchBar = (RelativeLayout) findViewById(R.id.shopSearchBar);
        shopSearchBar.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_test) {
            // เลือกวันเวลาหรับเลือกช่วงของยอดขาย
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //setHeaderButtonEffected();

        sellerRecyclerView = (RecyclerView) findViewById(R.id.sellerRecyclerView);

        CustomLinearLayoutManager customLayoutManager = new CustomLinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

        //sellerRecyclerView.setLayoutManager( new LinearLayoutManager(this) );
        sellerRecyclerView.setLayoutManager( customLayoutManager );

        sellerRecyclerView.setNestedScrollingEnabled(false);

        sellerAdapter = new SellerAdapter(this);
        //sellerAdapter.setInterfaceTitleCallback(interfaceTitleCallback);
        sellerAdapter.setInterfaceOnItem(this);
        sellerRecyclerView.setAdapter(sellerAdapter);


//        sellerRecyclerView.canScrollVertically(0);
        //sellerRecyclerView.setLayoutFrozen(true);

        List<SellerTitleDAO> a = GetSellerTitle.getSellerTitleList();

//        ItemSellerTitle itemSellerTitle = new ItemSellerTitle();
//        itemSellerTitle.setListOptionValue(GetSellerTitle.getSellerTitleList());
//        itemSellerTitle.setSettingsBackgroundColor(ContextCompat.getColor(getApplicationContext() , R.color.dark_honest_green) );
//        itemSellerTitle.setSettingsTint(ContextCompat.getColor(getApplicationContext() , R.color.angel_white) );
//        listSellerBaseItem.add(itemSellerTitle);

        //setDescription();

        // ค้นหาชื่อร้านค้าทั้งหมดจาก DB
        new SetAutoCompleteView().setView(this);
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

                if (SellerData.graphOptionId == 0) {
                    //listSellerBaseItem.addAll(ConvertContent.listItemSellerBestSeller(temp));

                    listSellerBaseItem.add(ConvertContent.itemSellerBestSellerGraph(temp, SellerType.TYPE_REPORT_BAR));

                    sellerAdapter.setRecyclerAdapter(listSellerBaseItem);
                    sellerAdapter.notifyDataSetChanged();
                } else {
                    int reportId = 0;
                    switch(SellerData.graphOptionId) {
                        case SellerGraphType.TYPE_GRAPH_BAR :
                            reportId = SellerType.TYPE_REPORT_BAR;
                            break;
                        case SellerGraphType.TYPE_GRAPH_LINE :
                            reportId = SellerType.TYPE_REPORT_LINE;
                            break;
                        case SellerGraphType.TYPE_GRAPH_PIE :
                        default:
                            reportId = SellerType.TYPE_REPORT_PIE;
                    }

                    listSellerBaseItem.add(ConvertContent.itemSellerBestSellerGraph(temp, reportId));

                    sellerAdapter.setRecyclerAdapter(listSellerBaseItem);

                    sellerAdapter.notifyDataSetChanged();
                }
            }
        }

        @Override
        public void onBodyError(ResponseBody responseBodyError) {
            Log.e("error", "body error");
        }

        @Override
        public void onBodyErrorIsNull() {
            Log.e("error", "body is null");
        }

        @Override
        public void onFailure(Throwable t) {
            t.printStackTrace();
        }
    };

    InterfaceOnOption optionInterface = new InterfaceOnOption() {
        @Override
        public void GraphSelected(int graphId) {
            SellerData.graphOptionId = graphId;
            setContentData();
        }
    };

    private void clearData() {
        //16/12/2559
        int size = this.listSellerBaseItem.size();
        //if (size > 1) {
            for (int i = 0; i < size; i++) {
                this.listSellerBaseItem.remove(i);
            }
            //sellerAdapter.setRecyclerAdapter(listSellerBaseItem);
            //sellerAdapter.notifyDataSetChanged();
            //this.sellerAdapter.notifyItemRangeRemoved(0, size-1);
        //} else {
            // เมื่อไม่มี title

            //for (int i = 0; i < size-1; i++) {
            //    this.listSellerBaseItem.remove(0);
            //}

            //sellerAdapter.setRecyclerAdapter(listSellerBaseItem);
            //this.sellerAdapter.notifyItemRangeRemoved(0, size-1);
        //}
    }

    private void setContentData() {
        new ServiceCollection().callServer(interfaceListen, SellerData.reportId, SellerData.shopCode, "2-TWICE");
    }

    private void setDescription() {

        if ((this.listSellerBaseItem != null) && (this.listSellerBaseItem.size() != 0)) {
            this.listSellerBaseItem.remove(0);
        }

        ItemSellerDescription itemSellerDescription = new ItemSellerDescription();
        itemSellerDescription.setShopDescription(shopName);
        itemSellerDescription.setReportDescription(reportName);
        itemSellerDescription.setDateDescription(date);

        itemSellerDescription.setExtendDateDescription(dateExtended);

        if(!dateExtended.equals("")) {
            itemSellerDescription.isExtended = true;
        } else {
            itemSellerDescription.isExtended = false;
        }

        listSellerBaseItem.add(itemSellerDescription);

        sellerAdapter.setRecyclerAdapter(listSellerBaseItem);
        sellerAdapter.notifyDataSetChanged();
    }

//    private void setHeaderButtonEffected() {
//        final Button shopSearchIcon = (Button) findViewById(R.id.shop_icon_id);
//        shopSearchIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //RelativeLayout optionBar = (RelativeLayout) findViewById(R.id.option_bar);
//                //optionBar.setVisibility(View.GONE);
//
//                // ไม่ควรจะมาทำในนี้ แต่นี่แค่ Prototyping
//
//                final InstantAutocomplete searchBox = (InstantAutocomplete) findViewById(R.id.search_box);
//
//                //searchBox.setVisibility(View.VISIBLE);
//                searchBox.requestFocus();
//
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.showSoftInput(searchBox, InputMethodManager.SHOW_IMPLICIT);
//
//                searchBox.showDropDown();
//
////                searchBox.animate()
////                        .translationY(0)
////                        .alpha(0.0f)
////                        .setListener(new AnimatorListenerAdapter() {
////                            @Override
////                            public void onAnimationStart(Animator animation) {
////                                super.onAnimationStart(animation);
////                                searchBox.setVisibility(View.VISIBLE);
////                                searchBox.showDropDown();
////                            }
////
////                            @Override
////                            public void onAnimationEnd(Animator animation) {
////                                super.onAnimationEnd(animation);
////                                searchBox.setVisibility(View.VISIBLE);
////                            }
////                        });
//            }
//        });
//
//        Button reportPickedIcon = (Button) findViewById(R.id.report_icon_id);
//        reportPickedIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // this's error
//                SellerReportDialogFragment newFragment = new SellerReportDialogFragment(SellerActivity.this);
//                newFragment.show(fm, "Open Dialog");
//            }
//        });
//
//        //reportPickedIcon.setHintTextColor( ContextCompat.getColor(this, R.color.angel_white) );
//        //reportPickedIcon.setEnabled(false);
//    }

    // ====== CALL BACK ทั้งหลายแหล่ =======================================================================>

    // เมื่อร้านค้าเปลี่ยน หรือถูกเลือก
    @Override
    public void shopSelected(String shopName) {
        //Log.e("AIRLINE", "45531");

        this.shopName = shopName;

        //clearData();

        //setDescription();
     //   setContentData();
    }

    // Implements method .........................
    // Implements from Seller Adapter when item's detail will be touch
    // เมื่อโปรดักถูกคลิก เพื่อดูรหัส SKU 12 หลัก
    @Override
    public void onItemClickListener(String itemCode) {
        ViewDialogProductDetail alert = new ViewDialogProductDetail();
        alert.showDialog(this, itemCode);
    }

    // Implement from Seller Adapter when a title is done changing ...
    // เปลี่ยน report
    @Override
    public void onChangeTitleCallBack(int reportId, String reportName) {
        SellerData.reportId = reportId;
        SellerData.graphOptionId = 0;

        this.reportName = reportName;

        //setDescription();

        setContentData();
    }

    // Implement from Seller Adapter when a setting's icon in the title is done clicking ...
    // เลือกกราฟ ...
    @Override
    public void onTitleSettingClickCallBack() {
        ViewDialogOption alert = new ViewDialogOption();
        alert.showDialog(this, optionInterface);
    }
}
