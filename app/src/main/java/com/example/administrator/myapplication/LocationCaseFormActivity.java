package com.example.administrator.myapplication;

import android.support.annotation.LayoutRes;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import AppBar.ApplicationBar;
import AppBar.BarType;
import shopFinding.SetShopAutoCompleteView;

public class LocationCaseFormActivity extends AppCompatActivity {

    @Override
    public void setContentView(@LayoutRes int layoutResID) {

        DrawerLayout fullLayout = (DrawerLayout) getLayoutInflater().inflate(layoutResID, null);

        FrameLayout frameLayout = (FrameLayout) fullLayout.findViewById(R.id.content);

        getLayoutInflater().inflate(R.layout.activity_location_case_form, frameLayout, true);

        super.setContentView(fullLayout);

        ApplicationBar temp = new ApplicationBar(this, BarType.TYPE_APPLICATION_NAME);
        temp.setAppNameWithBackPressed("Location Activity Transform");

        temp.createFABInstance();
        if (temp.getFABInstance() != null) {
            temp.getFABInstance().setFABDrawable(R.drawable.ic_menu_36);
            temp.getFABInstance().turnOnNavigatorMenu();
        }

        new SetShopAutoCompleteView().setView(this, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
