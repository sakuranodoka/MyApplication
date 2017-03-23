package com.example.administrator.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.LayoutRes;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

public class MapsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public GoogleMap mMap;

    public double la;
    public double lo;

    public static int hourStart = -1;
    public static int minuteStart = -1;

    public static int hourEning = -1;
    public static int minuteEnding = -1;

    private SearchUserData data = null;

    //private android.app.FragmentManager fragmentManager;
    private LoadUserLocationFragment ulFragment;
    private static final String TAG_TASK_FRAGMENT = "LOAD_USER_LOCATION";

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        DrawerLayout fullLayout = (DrawerLayout) getLayoutInflater().inflate(layoutResID, null);

        FrameLayout frameLayout = (FrameLayout) fullLayout.findViewById(R.id.content);

        getLayoutInflater().inflate(R.layout.activity_maps, frameLayout, true);

        super.setContentView(fullLayout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        //BusProvider.getInstance().register(this);

        Log.i("vvvvvv", "MapsActivity've already created");

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                if(mMap == null) {
                    mMap = googleMap;

                    //LatLng latLng = new LatLng(la, lo);

                    //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                    //mMap.addMarker(new MarkerOptions().position(latLng).title("ตำแหน่งปัจจุบัน"));

                    //new LoadTaskData(mMap, "11").execute();
                }
            }
        });

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Application");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Navigaton Drawer
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_maps_menu);

            navigationView.setNavigationItemSelectedListener(this);

//            ImageButton imageButton = (ImageButton) findViewById(R.id.backPressed);
//            imageButton.setVisibility(View.VISIBLE);
//            imageButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    finish();
//            }
//        });
        // Navigaton Drawer Ending.

//        Button btn1 = (Button) findViewById(R.id.button2);
//        btn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //showDialog(TIME_DIALOG_ID);
//
//                FragmentManager fm = getSupportFragmentManager();
//                MyDialogFragment newFragment = new MyDialogFragment(mMap);
//                newFragment.show(fm, "3434");
//            }
//        });

        //Log.v("vvvvvv","first first");
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fm = null;

        if (id == R.id.nav_user) {
            fm = getSupportFragmentManager();
            new LoadSearchUserData(fm, MapsActivity.this).execute();
//            fm = getSupportFragmentManager();
//            SearchUserDialogFragment newFragment = new SearchUserDialogFragment();
//            newFragment.show(fm, "Open Dialog");
        } else if (id == R.id.nav_current) {
            if( produceData() == null ) {
                new AlertDialog.Builder(MapsActivity.this)
                        .setTitle("แจ้งเตือน")
                        .setMessage("กรุณาเลือกผู้ใช้งานก่อน")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                /*fm = getSupportFragmentManager();
                MyDialogFragment newFragment = new MyDialogFragment(mMap);
                newFragment.show(fm, "Open Dialog");*/
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                //GoogleMap mMap, String dateStart, String dateEnd, String method, String usr_id) {
                ulFragment = new LoadUserLocationFragment(mMap,
                        this.data.getUsr_mac_address(),
                        MapsActivity.this);
//
                fragmentManager.beginTransaction().add(ulFragment,TAG_TASK_FRAGMENT).commit();
            }

        } else if (id == R.id.nav_datetime) {
            if( produceData() == null ) {
                new AlertDialog.Builder(MapsActivity.this)
                        .setTitle("แจ้งเตือน")
                        .setMessage("กรุณาเลือกผู้ใช้งานก่อน")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
//                fm = getSupportFragmentManager();
//                MyDialogFragment newFragment = new MyDialogFragment(mMap);
//                newFragment.show(fm, "Open Dialog");
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Subscribe
    public void getMessage(ParallelDataOne data) {
        //Toast.makeText(this, data.getHour()+" : "+data.getMinute() , Toast.LENGTH_SHORT).show();
        hourStart = data.getHour();
        minuteStart = data.getMinute();
        //Log.v("vvvvvv", data.getHour()+" 1: "+data.getMinute());
    }

    @Subscribe
    public void getMessage(ParallelDataTwo data) {
        hourEning = data.getHour();
        minuteEnding = data.getMinute();
        //Log.v("vvvvvv", data.getHour()+" 2: "+data.getMinute());
        //Toast.makeText(this, data.getHour()+" : "+data.getMinute() , Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    public void getMessage(SearchUserData data) {
        this.data = data;
        //Log.v("vvvvvv", "Activity Maps : "+data.getUsr_mac_address());
        //Toast.makeText(this, data.getHour()+" : "+data.getMinute() , Toast.LENGTH_SHORT).show();
    }

    @Produce
    public SearchUserData sendMessage() {
        return this.data;
    }

    //@Produce
    public SearchUserData produceData() {
        return this.data;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("vvvvvv","MapActivity has been destroyed.");
    }

    static final int TIME_DIALOG_ID = 999;
    static final int OPEN_DIALOG_ID = 642;

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case OPEN_DIALOG_ID:
                break;
        }
        return null;
    }
}
