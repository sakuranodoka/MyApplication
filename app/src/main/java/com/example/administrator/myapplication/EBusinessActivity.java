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
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import AppBar.ApplicationBar;
import AppBar.BarType;
import user.UserAdapter;
import user.UserBaseItem;
import user.user.item.ItemMenu;
import user.user.item.ItemSection;

public class EBusinessActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static RecyclerView userRecyclerView;

    public static UserAdapter userAdapter;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {

        DrawerLayout fullLayout = (DrawerLayout) getLayoutInflater().inflate(layoutResID, null);

        FrameLayout frameLayout = (FrameLayout) fullLayout.findViewById(R.id.content);

        getLayoutInflater().inflate(R.layout.activity_ebusiness, frameLayout, true);

        super.setContentView(fullLayout);

        new ApplicationBar(this, BarType.TYPE_APPLICATION_NAME).setAppName("Administrator");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        userRecyclerView = (RecyclerView) findViewById(R.id.userRecyclerView);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        userAdapter = new UserAdapter();
        userRecyclerView.setAdapter(userAdapter);

        List<UserBaseItem> userBaseItems = new ArrayList<>();

        int sectionTheme = ContextCompat.getColor(getApplicationContext(), R.color.sky_light_blue);

        ItemSection itemSection = new ItemSection();
        itemSection.setSection("ระบบหน้าร้าน");
        itemSection.setColor( sectionTheme );
        userBaseItems.add(itemSection);

        ItemMenu itemMenu = null;

        itemMenu = new ItemMenu();
        itemMenu.setImageSource(R.drawable.ic_my_location_white_24dp);
        itemMenu.setImageResourceColor(sectionTheme);
        itemMenu.setMenuName("พิกัดพนักงาน");
        itemMenu.setDetailName("แสดงพิกัดของพนักงานตามหมายเลขโทรศัพท์มือถือที่ท่านเลือก");
        itemMenu.setIntent(null, LocationActivity.class);
        userBaseItems.add(itemMenu);

        itemMenu = new ItemMenu();
        itemMenu.setImageSource(R.drawable.ic_view_array_black_24dp);
        itemMenu.setImageResourceColor(sectionTheme);
        itemMenu.setMenuName("ตำแหน่งหน้าร้าน");
        itemMenu.setDetailName("แสดงตำแหน่งหน้าร้านทั้งหมดในแผนที่ บันทึกพิกัดหน้าร้าน");
        itemMenu.setIntent(null, ShopInfoActivity.class);
        userBaseItems.add(itemMenu);

        sectionTheme = ContextCompat.getColor(getApplicationContext(), R.color.honest_green);
        itemSection = new ItemSection();
        itemSection.setSection("ระบบยอดขาย");
        itemSection.setColor( sectionTheme );
        userBaseItems.add(itemSection);

        itemMenu = new ItemMenu();
        itemMenu.setImageSource(R.drawable.ic_strikethrough_s_black_24dp);
        itemMenu.setImageResourceColor(sectionTheme);
        itemMenu.setMenuName("รายการยอดขายสุทธิ");
        itemMenu.setDetailName("ยอดขายทั้งหมดของผลิตภัณฑ์จำแนกตามรหัส Collection ทั้งหมดของหน้าร้าน ");
        itemMenu.setIntent(null, SellerActivity.class);
        userBaseItems.add(itemMenu);

        userAdapter.setRecyclerAdapter(userBaseItems);
        userAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
