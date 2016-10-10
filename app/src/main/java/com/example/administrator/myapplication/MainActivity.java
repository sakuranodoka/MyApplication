package com.example.administrator.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    public SharedPreferences sp;
    public SharedPreferences.Editor editor;

    final String _PREF_MODE = "_0x66fd";

    private static final int CONTENT_REQUEST = 1337;

    public GoogleApiClient googleApiClient;
    public GoogleMap mMap;

    public double la = 0.00;
    public double lo = 0.00;

    public static Bus bus;

    private JSONObject JsonObject = null;
    private JSONArray JsonArray = null;
    private JSONObject TempJson = null;

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public class TestData {
        public String message;
    }

    int[] networkTypes = new int[]{
            ConnectivityManager.TYPE_BLUETOOTH,
            ConnectivityManager.TYPE_DUMMY,
            ConnectivityManager.TYPE_ETHERNET,
            ConnectivityManager.TYPE_MOBILE,
            ConnectivityManager.TYPE_MOBILE_DUN,
            ConnectivityManager.TYPE_MOBILE_HIPRI,
            ConnectivityManager.TYPE_MOBILE_MMS,
            ConnectivityManager.TYPE_MOBILE_SUPL,
            ConnectivityManager.TYPE_VPN,
            ConnectivityManager.TYPE_WIFI,
            ConnectivityManager.TYPE_WIMAX
};

    public boolean isOnline() {
//        ConnectivityManager cm =
//                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo netInfo = cm.getActiveNetworkInfo();

//        try {
//            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//            for (int networkType : networkTypes) {
//                NetworkInfo netInfo = cm.getNetworkInfo(networkType);
//                if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
//                    return true;
//                }
//            }
//        } catch (Exception e) {
//            return false;
//        }
//        return false;

        //return netInfo.isAvailable();

        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;


        //return netInfo != null && netInfo.isConnected();
//        boolean status = true;
//        if ( cm.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED
//                || cm.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING ) {
//
//            // notify user you are online
//            status = true;
//        }
//        else if ( cm.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED
//                || cm.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {
//            status = false;
//            // notify user you are not online
//        } else {
//            status = false;
//        }
//        return status;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("status", "App Created");

        if (isOnline()) {
            Toast.makeText(this, "Internet available", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Internet is not available", Toast.LENGTH_SHORT).show();
        }

        bus = new Bus(ThreadEnforcer.MAIN);
        bus.register(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Button captureBtn = (Button) findViewById(R.id.capture);
        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int REQUEST_CAMERA = 0;
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                String imageFileName = "IMG_" + timeStamp + ".jpg";

                File f = new File(Environment.getExternalStorageDirectory()
                        , "DCIM/Camera/" + imageFileName);

                Uri uri = Uri.fromFile(f);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                startActivityForResult(Intent.createChooser(intent
                        , "Take a picture with"), REQUEST_CAMERA);
            }
        });

        Button scanBtn = (Button) findViewById(R.id.scan);
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator scanIntegrator = new IntentIntegrator(MainActivity.this);
                scanIntegrator.initiateScan();
            }
        });

        googleApiClient = new GoogleApiClient.Builder(this, this, this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        Button openMapBtn = (Button) findViewById(R.id.openMapBtn);
        openMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(t);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {

        super.onResume();

    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();

        Log.i("status", "App Start");
    }

    @Override
    protected void onStop() {
        super.onStop();
//        if (googleApiClient != null && googleApiClient.isConnected()) {
//            googleApiClient.disconnect();
//        }
    }

    @Override
    protected void onDestroy() {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.gc();

        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (scanningResult != null) {
            //we have a result
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();

            TextView textView = (TextView) findViewById(R.id.textView);
            //String contents = data.getStringExtra("SCAN_RESULT");
            textView.setText(scanContent + " " + scanFormat);
            Toast toast = Toast.makeText(getApplicationContext(),
                    scanContent + " " + scanFormat, Toast.LENGTH_SHORT);
            toast.show();
        }

        if (resultCode == RESULT_OK) {
            if (requestCode == CONTENT_REQUEST) {

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT);
            } else if (resultCode == 2) {
                String contents = data.getStringExtra("SCAN_RESULT");
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        //LocationAvailability locationAvailability = LocationServices.FusedLocationApi.getLocationAvailability(googleApiClient);
        //if (locationAvailability.isLocationAvailable()) {
            LocationRequest locationRequest = new LocationRequest()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(9000);
            //Log.i("vvvvvv","vvvvvv");
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
//        } else {
//            Toast.makeText(this, "Location provider no longer available !!!", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public void onLocationChanged(Location location) {

        la = location.getLatitude();
        lo = location.getLongitude();
        TextView textView = (TextView) findViewById(R.id.textView2);
        textView.setText("Latitude : " + location.getLatitude() + "\n" +
                "Longistudesmd : " + location.getLongitude());
//3433
        ArrayList<String> temp;
//
//        sp = getSharedPreferences(_PREF_MODE, Context.MODE_PRIVATE);
//        Log.i("dataD", sp.getString("data", "[]").toString().length()+"");
//        if(isOnline()) {
//            if(sp.getString("data", "[]").equals("[]")) {
//                temp = new ArrayList<String>();
//                // add ปกติ
//                TempJson = new JSONObject();
//                try {
//                    TempJson.put("usr_id", "11");
//                    TempJson.put("lat",  String.valueOf(la));
//                    TempJson.put("lng",  String.valueOf(lo));
//                    TempJson.put("time", "NOW");
//
//                    JsonArray = new JSONArray();
//                    JsonArray.put(TempJson);
//
//                    temp.add(JsonArray.toString());
//
//                    new serviceProgress(temp).execute();
//
//                    //Toast.makeText(this, "send to server as normally", Toast.LENGTH_SHORT).show();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                // เคสของ เมื่อออฟไลน์มานาน มีข้อมูลอยู่เยอะ จะให้ทำการส่งข้อมูลไปยังเซิร์ฟเวอร์
//                temp = new ArrayList<String>();
//                temp.add(sp.getString("data", "[]"));
//
//                new serviceProgress(temp).execute();
//
//                sp.edit().clear().commit();
//            }
//        } else {
//            java.util.Date dt = new java.util.Date();
//            java.text.SimpleDateFormat sdf =
//                    new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//            String currentTime = sdf.format(dt);
//
//            editor = sp.edit();
//            JsonArray = new JSONArray();
//
//            try {
//
//                TempJson = new JSONObject();
//                TempJson.put("usr_id", "11");
//                TempJson.put("lat",  String.valueOf(la));
//                TempJson.put("lng",  String.valueOf(lo));
//                TempJson.put("time", currentTime);
//
//                JsonArray.put(TempJson);
//
//                String json = "";   // [] = default value.
//
//                Log.i("dataTest",sp.getString("data", "[]"));
//
//                if( sp.getString("data", "[]").equals("[]") ) {
//
//                    json = "";
//                    editor.putString("data", JsonArray.toString());
//
//                } else {
//                    json = sp.getString("data", "[]");
//                    json = json.substring(1);
//                    json = json.substring(0, json.length()-1);
//
//                    editor.putString("data", "["+json+","+JsonArray.toString().substring(1));
//                }
//                editor.commit();
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//                Log.v("vvvvvv", "Error");
//            }
//
//            //Toast.makeText(this, "Get location at "+currentTime, Toast.LENGTH_SHORT).show();
//        }
    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.i("GPS-PROVIDER","suppended.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("GPS-PROVIDER","not available.");
    }

    private class serviceProgress extends AsyncTask<String, Integer, String> {
        //private ProgressDialog pd;

        private OkHttpClient client;

        private ArrayList<String> arrayList;

        private final String url = "http://angsila.informatics.buu.ac.th/~55160509/android/update.php";

        private String res;

        private RequestBody body;
        private Request request;

        public serviceProgress(ArrayList<String> temp) {
            this.arrayList = temp;
            this.res = "";
        }

        public serviceProgress(ArrayList<String> temp, boolean status) {
            this.arrayList = temp;
            this.res = "";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pd = new ProgressDialog(MainActivity.this);
//            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            pd.setTitle("Loading...");
//            pd.setMessage("Loading images...");
//            pd.setCancelable(false);
//            pd.setIndeterminate(false);
//            pd.setMax(100);
//            pd.setProgress(0);
//            pd.show();
        }

        @Override
        protected void onPostExecute(String result)  {
//            pd.dismiss();
           // TextView textViews = (TextView) findViewById(R.id.textView);
            //textViews.setText(result);
            //Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
        }

        protected void onProgressUpdate(Integer... values) {
            //pd.setProgress(values[0]);
        }

        @Override
        protected String doInBackground(String... params)   {
            client = new OkHttpClient();

            this.res = "{\"data\":" + this.arrayList.get(0) + "}";

            body = RequestBody.create(JSON, this.res);
            request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
            try {
                //Response response =
                client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";//this.res;
        }
    }

    String beforeEnable;

    private void turnGPSOn () {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(MainActivity.this).build();
            googleApiClient.connect();
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            // **************************
            builder.setAlwaysShow(true); // this is the key ingredient
            // **************************

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                    .checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result
                            .getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            // All location settings are satisfied. The client can
                            // initialize location
                            // requests here.
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be
                            // fixed by showing the user
                            // a dialog.
                            try {
                                // Show the dialog by calling
                                // startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(MainActivity.this, 1000);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have
                            // no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            });
        }
    }
}
