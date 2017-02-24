package com.example.administrator.myapplication;

import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import AppBar.ApplicationBar;
import AppBar.BarType;
import shopFinding.InterfaceOnShop;
import shopFinding.SetShopAutoCompleteView;
import user.UserAdapter;
import user.UserBaseItem;
import user.user.item.ItemMenu;
import user.user.item.ItemSection;

public class ShopInfoActivity extends AppCompatActivity
    implements InterfaceOnShop {

    private RecyclerView recyclerView;

    public static UserAdapter userAdapter;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {

        DrawerLayout fullLayout = (DrawerLayout) getLayoutInflater().inflate(layoutResID, null);

        FrameLayout frameLayout = (FrameLayout) fullLayout.findViewById(R.id.content);

        getLayoutInflater().inflate(R.layout.activity_shop_info, frameLayout, true);

        super.setContentView(fullLayout);

        new ApplicationBar(this, BarType.TYPE_SHOP_SEARCH).setShopBar();

        new SetShopAutoCompleteView().setView(this, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.shopInfoRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userAdapter = new UserAdapter();
        recyclerView.setAdapter(userAdapter);

        List<UserBaseItem> userBaseItems = new ArrayList<>();

        ItemSection itemSection = new ItemSection();
        itemSection.setSection("เมนูแผนที่หน้าร้าน");
        itemSection.setColor( ContextCompat.getColor(getApplicationContext(), R.color.lemon_wingless) );
        userBaseItems.add(itemSection);

        ItemMenu itemMenu = null;

        itemMenu = new ItemMenu();
        itemMenu.setImageSource(R.drawable.ic_my_location_white_24dp);
        itemMenu.setImageResourceColor(ContextCompat.getColor(getApplicationContext(), R.color.lemon_wingless));
        itemMenu.setMenuName("แสดงตำแหน่งหน้าร้าน");
        itemMenu.setDetailName("แสดงตำแหน่งหน้าร้านทั้งหมดในแผนที่");
        //itemMenu.setIntent(null, ShopInfoActivity.class);
        userBaseItems.add(itemMenu);

        itemMenu = new ItemMenu();
        itemMenu.setImageSource(R.drawable.ic_my_location_white_24dp);
        itemMenu.setImageResourceColor(ContextCompat.getColor(getApplicationContext(), R.color.sky_light_blue));
        itemMenu.setMenuName("บันทึกตำแหน่ง");
        itemMenu.setDetailName("เพิ่มตำแหน่งของหน้าร้านเข้าสู่ระบบ\nจากตำแหน่งที่ท่านอยู่ด้วย GPS");
        //itemMenu.setIntent(null, LocationActivity.class);
        userBaseItems.add(itemMenu);

        itemMenu = new ItemMenu();
        itemMenu.setImageSource(R.drawable.ic_my_location_white_24dp);
        itemMenu.setImageResourceColor(ContextCompat.getColor(getApplicationContext(), R.color.seasonal_orange));
        itemMenu.setMenuName("แก้ไขตำแหน่ง");
        itemMenu.setDetailName("แก้ไขตำแหน่งของหน้าร้าน");
        //itemMenu.setIntent(null, SellerActivity.class);
        userBaseItems.add(itemMenu);

        userAdapter.setRecyclerAdapter(userBaseItems);
        userAdapter.notifyDataSetChanged();
    }

    @Override
    public void shopSelected(String shopName) {

    }
}