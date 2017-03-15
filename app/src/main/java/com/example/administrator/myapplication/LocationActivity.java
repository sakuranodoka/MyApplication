package com.example.administrator.myapplication;

import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import AppBar.ApplicationBar;
import AppBar.BarType;
import autocomplete.InstantAutocomplete;
import location.LocationDialogMenu;
import location.pojo.MacAddressPOJO;
import location.services.LocationRetrofit;
import okhttp3.ResponseBody;
import retrofit.InterfaceListen;
import retrofit2.Retrofit;

public class LocationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public GoogleMap mMap;

    public String usr_mac_address = "";

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_choose_phone);

        // If first page selection
        new LocationRetrofit().callServer(getMacAddressCallBack);
    }

    private InterfaceListen getMacAddressCallBack = new InterfaceListen() {

        @Override
        public void onResponse(Object data, Retrofit retrofit) {

            final List<MacAddressPOJO> temp = (List<MacAddressPOJO>) data;

            List<String> a = new ArrayList<>();

            for(MacAddressPOJO macAddressPOJO : temp) {
                a.add(macAddressPOJO.toString());
            }

            final InstantAutocomplete autoCompleteTextView = (InstantAutocomplete) findViewById(R.id.location_search_tv);

            autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    autoCompleteTextView.showDropDown();
                }
            });

            autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //setContentView(R.layout.activity_location);

                    usr_mac_address = temp.get(position).getUsrMacAddress();

                    Intent t = new Intent(LocationActivity.this, LocationCaseFormActivity.class);

                    Bundle b = new Bundle();

                    b.putString("USER_MAC_ADDRESS", usr_mac_address);

                    t.putExtra("LOCATION_BUNDLE", b);

                    startActivity(t);

                    //changeContent();
                }
            });

            String [] b = a.toArray(new String[a.size()]);

            autoCompleteTextView.setThreshold(0);

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(LocationActivity.this, android.R.layout.select_dialog_singlechoice, b);

            autoCompleteTextView.setAdapter(arrayAdapter);
            arrayAdapter.setNotifyOnChange(true);
        }

        @Override
        public void onBodyError(ResponseBody responseBodyError) {

        }

        @Override
        public void onBodyErrorIsNull() {

        }

        @Override
        public void onFailure(Throwable t) {

        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    private void changeContent() {

//        ImageView mapOption = (ImageView) findViewById(R.id.map_option);
//        mapOption.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new LocationDialogMenu().showDialog(LocationActivity.this, mMap, usr_mac_address);
//            }
//        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                if(mMap == null) {
                    mMap = googleMap;
                }
            }
        });
    }
}
