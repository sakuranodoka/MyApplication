package com.example.administrator.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import AppBar.ApplicationBar;
import AppBar.BarType;

/**
 * Created by Administrator on 6/3/2560.
 */

public class AppliedAppCompat extends AppCompatActivity {

    protected int layoutResID = 0;

    @SuppressLint("DefaultConstructor")
    public AppliedAppCompat() {}

    @SuppressLint("AppCompatActivity")
    public AppliedAppCompat(@NonNull @LayoutRes int layoutResID) {
        this.layoutResID = layoutResID;
    }

    @Override
    public void setContentView(@NonNull @LayoutRes int layoutResID) {
        DrawerLayout fullLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_main, null);
        FrameLayout frameLayout = (FrameLayout) fullLayout.findViewById(R.id.content);
        getLayoutInflater().inflate(layoutResID, frameLayout, true);
        super.setContentView(fullLayout);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this.layoutResID);
    }

    protected void setApplicationToolbar(@NonNull String applicationName, int property) {
        switch(property) {
            case ApplicationBar.APPLICATION_APP_MENU_BAR :
                new ApplicationBar(this, BarType.TYPE_APPLICATION_NAME).setAppName(applicationName);
                break;
            case ApplicationBar.APPLICATION_APP_BACK_PRESSED :
                new ApplicationBar(this, BarType.TYPE_APPLICATION_NAME).setAppNameWithBackPressed(applicationName);
                break;
            default:break;
        }
    }

    protected void setLayoutContent(int layoutResID) {

    }

}
