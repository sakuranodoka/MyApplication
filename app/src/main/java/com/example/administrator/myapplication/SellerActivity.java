package com.example.administrator.myapplication;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import AppBar.ApplicationBar;
import AppBar.BarType;
import autocomplete.InstantAutocomplete;
import seller.TypeSellerReport;
import seller.titlebar.SellerOptionalDAO;
import seller.titlebar.SellerTitleBar;
import shopFinding.InterfaceOnShop;
import shopFinding.SetShopAutoCompleteView;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import seller.CustomLinearLayoutManager;
import seller.SellerAdapter;
import seller.SellerBaseItem;
import seller.SellerData;
import seller.SellerGraphType;
import seller.titlebar.InterfaceOnTitleBar;
import seller.titlebar.SellerTitleDAO;
import seller.convert.data.ConvertContent;
import seller.item.ItemSellerTitle;
import seller.pojo.SellerBestSellerPOJO;
import seller.pojo.SellerCollectionPOJO;
import retrofit.InterfaceListen;
import seller.services.retrofit.ServiceCollection;

public class SellerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        InterfaceOnTitleBar,
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

        new ApplicationBar(this, BarType.TYPE_SHOP_SEARCH).setShopBar();

        setTitle();
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
        //sellerAdapter.setInterfaceOnItem(this);
        sellerRecyclerView.setAdapter(sellerAdapter);

        List<SellerTitleDAO> a = SellerTitleBar.getSellerTitleList();

//        ItemSellerTitle itemSellerTitle = new ItemSellerTitle();
//        itemSellerTitle.setListTitleDescription(SellerTitleBar.getSellerTitleList());
//        itemSellerTitle.setSettingsBackgroundColor(ContextCompat.getColor(getApplicationContext() , R.color.dark_honest_green) );
//        itemSellerTitle.setSettingsTint(ContextCompat.getColor(getApplicationContext() , R.color.angel_white) );
//        listSellerBaseItem.add(itemSellerTitle);

        //setDescription();

        // ค้นหาชื่อร้านค้าทั้งหมดจาก DB
        new SetShopAutoCompleteView().setView(this);
    }

    InterfaceListen interfaceListen = new InterfaceListen() {
        @Override
        public void onResponse(Object data, Retrofit retrofit) {
            // หลังจากเซิร์ฟเวอร์ส่ง Data กลับมา ^ _ ^
            if(data instanceof SellerCollectionPOJO) {
                clearData();

                exchangeOptionBar(false);

                SellerCollectionPOJO temp = (SellerCollectionPOJO) data;

//                if (SellerData.graphOptionId == 0) {
//                    listSellerBaseItem.addAll(ConvertContent.listItemSellerCollection(temp));
//                } else {
//                    listSellerBaseItem.add(ConvertContent.itemSellerCollectionGraph(temp, TypeSellerReport.TYPE_REPORT_BAR));
//                }

                listSellerBaseItem.add(ConvertContent.itemSellerCollectionGraph(temp, TypeSellerReport.TYPE_REPORT_BAR));

                sellerAdapter.setRecyclerAdapter(listSellerBaseItem);
                sellerAdapter.notifyDataSetChanged();

            } else if(data instanceof SellerBestSellerPOJO) {
                clearData();

                exchangeOptionBar(true);

                SellerBestSellerPOJO temp = (SellerBestSellerPOJO) data;

                if (SellerData.graphOptionId == 0) {

                    listSellerBaseItem.add(ConvertContent.itemSellerBestSellerGraph(temp, TypeSellerReport.TYPE_REPORT_BAR));

                    sellerAdapter.setRecyclerAdapter(listSellerBaseItem);
                    sellerAdapter.notifyDataSetChanged();
                } else {
//                    int reportId = 0;
//                    switch(SellerData.graphOptionId) {
//                        case SellerGraphType.TYPE_GRAPH_BAR :
//                            reportId = TypeSellerReport.TYPE_REPORT_BAR;
//                            break;
//                        case SellerGraphType.TYPE_GRAPH_LINE :
//                            reportId = TypeSellerReport.TYPE_REPORT_LINE;
//                            break;
//                        case SellerGraphType.TYPE_GRAPH_PIE :
//                        default:
//                            reportId = TypeSellerReport.TYPE_REPORT_PIE;
//                    }
//                    listSellerBaseItem.add(ConvertContent.itemSellerBestSellerGraph(temp, reportId));
//
//                    sellerAdapter.setRecyclerAdapter(listSellerBaseItem);
//                    sellerAdapter.notifyDataSetChanged();
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

    private void clearData() {
        //16/12/2559
        int size = this.listSellerBaseItem.size();

        for (int i = 0; i < size; i++) {
            this.listSellerBaseItem.remove(i);
        }
    }

    private void setContentData() {
        setLoadingScreen();
        new ServiceCollection().callServer(interfaceListen, SellerData.reportId, SellerData.shopCode, "2-TWICE");
    }

    private void setLoadingScreen() {
        clearData();
        listSellerBaseItem.add(ConvertContent.getLoadingScreenItem());

        sellerAdapter.setRecyclerAdapter(listSellerBaseItem);
        sellerAdapter.notifyDataSetChanged();
    }

    // เลือก Title
    // เลือก Option อาทิเช่น 30 วันย้อนหลัง
    private void setTitle() {
        final InstantAutocomplete reportDescription = (InstantAutocomplete) findViewById(R.id.report_description);
        final InstantAutocomplete reportRange = (InstantAutocomplete) findViewById(R.id.report_range);
        final InstantAutocomplete reportOptional = (InstantAutocomplete) findViewById(R.id.report_optional);

        final ItemSellerTitle itemSellerTitle = new ItemSellerTitle();
        itemSellerTitle.setListTitleDescription(SellerTitleBar.getSellerTitleList());
        //
        itemSellerTitle.setListTitleOptional(SellerTitleBar.getSellerOptional());

        ArrayAdapter<SellerTitleDAO> autoCompleteAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_singlechoice, itemSellerTitle.getListTitleDescription());
        reportDescription.setAdapter(autoCompleteAdapter);

        reportDescription.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                reportDescription.showDropDown();
            }
        });

        reportDescription.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onTitleChange(itemSellerTitle.listTitleDescription.get(position).getId(), itemSellerTitle.listTitleDescription.get(position).getTitle());
            }
        });


        // แท็บออฟชั่น
        ArrayAdapter<SellerOptionalDAO> OptinalAutoCompleteAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_singlechoice, itemSellerTitle.getListTitleOptional());
        reportOptional.setAdapter(OptinalAutoCompleteAdapter);

        reportOptional.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                reportOptional.showDropDown();
            }
        });

        reportOptional.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onOptionalChange(itemSellerTitle.listTitleOptional.get(position).getId());
            }
        });
    }

    // เปิดปิดโหมด Option ใต้ช่องเลือก Title
    private void exchangeOptionBar(boolean action) {
        LinearLayout optional_bar = (LinearLayout) findViewById(R.id.optional_bar);

        if(optional_bar!= null) {
            if(action) {
                optional_bar.setVisibility(View.VISIBLE);
            } else {
                optional_bar.setVisibility(View.GONE);
            }
        }
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
        this.shopName = shopName;
    }

    // Implement from Seller Adapter when a title is done changing ...
    // เปลี่ยน report
    @Override
    public void onTitleChange(int reportId, String reportName) {
        SellerData.reportId = reportId;
        SellerData.graphOptionId = 0;

        this.reportName = reportName;

        setContentData();
    }

    // เปลี่ยน optional เปลี่ยน top10 เปลี่ยน last 10

    @Override
    public void onOptionalChange(int reportOptional) {

        SellerData.reportOptional = reportOptional;

        setContentData();
    }
}
