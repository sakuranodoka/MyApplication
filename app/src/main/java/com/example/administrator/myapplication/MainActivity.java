package com.example.administrator.myapplication;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.otto.Bus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    //user : infoshop
    // Pass : !nf0sh@p

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

    private static final String TAG_TASK_FRAGMENT = "task_fragment";

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");


    public class TestData {
        public String message;
    }

    private String address;

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnectedOrConnecting();
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

//        Runtime runtime = Runtime.getRuntime();
//        try {
//
//            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
//            int exitValue = ipProcess.waitFor();
//            return (exitValue == 0);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        return false;


        //return netInfo != null && netInfo.isConnected();
        //boolean status = true;
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
        //return status;
    }

//    @Override
//    public void setContentView(@LayoutRes int layoutResID) {
//        DrawerLayout fullLayout = (DrawerLayout) getLayoutInflater().inflate(layoutResID, null);
//
//        FrameLayout frameLayout = (FrameLayout) fullLayout.findViewById(R.id.content);
//
//        getLayoutInflater().inflate(R.layout.content_main, frameLayout, true);
//
//        super.setContentView(fullLayout);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //new StatusBarStyle().setHideStatusBar(getWindow().getDecorView());

        setContentView(R.layout.view_login);

        LinearLayout llUsers = (LinearLayout) findViewById(R.id.users);
        llUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(MainActivity.this, UserActivity.class);
                startActivity(t);
            }
        });

        LinearLayout llAdmin = (LinearLayout) findViewById(R.id.admin);
        llAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(MainActivity.this, EBusinessActivity.class);
                startActivity(t);
            }
        });

//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        //setSupportActionBar(toolbar);
//
////        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
////        fab.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
////            }
////        });
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//
//        Button captureBtn = (Button) findViewById(R.id.capture);
//        captureBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int REQUEST_CAMERA = 0;
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//
//                String imageFileName = "IMG_" + timeStamp + ".jpg";
//
//                File f = new File(Environment.getExternalStorageDirectory()
//                        , "DCIM/Camera/" + imageFileName);
//
//                Uri uri = Uri.fromFile(f);
//
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//
//                startActivityForResult(Intent.createChooser(intent
//                        , "Take a picture with"), REQUEST_CAMERA);
//            }
//        });
//
//        Button scanBtn = (Button) findViewById(R.id.scan);
//        scanBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                IntentIntegrator scanIntegrator = new IntentIntegrator(MainActivity.this);
//                scanIntegrator.initiateScan();
//            }
//        });
//
//        Button openGraphBtn = (Button) findViewById(R.id.openGraphBtn);
//        openGraphBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent t =  new Intent(MainActivity.this, SimplyGraph.class);
//                startActivity(t);
//            }
//        });
//
//        Button openPieGraphBtn = (Button) findViewById(R.id.openPieChartGraphBtn);
//        openPieGraphBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent t =  new Intent(MainActivity.this, SimplyPieChartActivity.class);
//                startActivity(t);
//            }
//        });
//
//        Button openMapBtn = (Button) findViewById(R.id.openMapBtn);
//        openMapBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent t = new Intent(MainActivity.this, MapsActivity.class);
//                startActivity(t);
//            }
//        });
//
//        googleApiClient = new GoogleApiClient.Builder(this, this, this)
//                .addApi(LocationServices.API)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .build();

        //acTextView.bringToFront();
        //acTextView.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {

        if (isOnline()) {

            WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = manager.getConnectionInfo();
            address = info.getMacAddress();

            //Toast.makeText(this, "Internet available | " + address, Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(this, "Internet is not available", Toast.LENGTH_SHORT).show();
        }
        super.onResume();
    }

    @Override
    protected void onStart() {
        //googleApiClient.connect();
        super.onStart();

        Log.i("status", "App Start");
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
//        if (googleApiClient != null && googleApiClient.isConnected()) {
//            googleApiClient.disconnect();
//        }
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        //super.onSaveInstanceState(outState);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

//        if (id == R.id.nav_test) {
//            Intent t = new Intent(MainActivity.this, SellerActivity.class);
//            startActivity(t);
//        }

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

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
                    .setInterval(10000);
            Log.i("vvvvvv","GPS is now connected.");
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
//        } else {
//            Toast.makeText(this, "Location provider no longer available !!!", Toast.LENGTH_SHORT).show();
//        }
    }

    private UpdateLocationFragment ulFragment;

    @Override
    public void onLocationChanged(Location location) {

        la = location.getLatitude();
        lo = location.getLongitude();
        TextView resultLat = (TextView) findViewById(R.id.resultLat);
        resultLat.setText(location.getLatitude()+"");

        TextView resultLng = (TextView) findViewById(R.id.resultLng);
        resultLng.setText(location.getLongitude()+"");

        ArrayList<String> temp;

        Log.v("vvvvvv", "Changed");
//
        sp = getSharedPreferences(_PREF_MODE, Context.MODE_PRIVATE);
        //Log.i("dataD", sp.getString("data", "[]").toString().length()+"");
        if(isOnline()) {
            if(sp.getString("data", "[]").equals("[]")) {
                temp = new ArrayList<String>();
                // add ปกติ
                TempJson = new JSONObject();
                try {
                    TempJson.put("usr_id", address);
                    TempJson.put("lat",  String.valueOf(la));
                    TempJson.put("lng",  String.valueOf(lo));
                    TempJson.put("time", "NOW");

                    JsonArray = new JSONArray();
                    JsonArray.put(TempJson);

                    temp.add(JsonArray.toString());

                    FragmentManager fragmentManager = getFragmentManager();
                    //ulFragment = (UpdateLocationFragment) fragmentManager.findFragmentByTag(TAG_TASK_FRAGMENT);
                    //if (ulFragment == null) {
                    ulFragment = new UpdateLocationFragment(temp);
                    fragmentManager.beginTransaction().add(ulFragment, TAG_TASK_FRAGMENT).commitAllowingStateLoss();
//                    } else {
//
//                    }

                    //new serviceProgress(temp).execute();

                    //Toast.makeText(this, "send to server as normally", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                // เคสของ เมื่อออฟไลน์มานาน มีข้อมูลอยู่เยอะ จะให้ทำการส่งข้อมูลไปยังเซิร์ฟเวอร์
                temp = new ArrayList<String>();
                temp.add(sp.getString("data", "[]"));

                TempJson = new JSONObject();
                try {
                    TempJson.put("usr_id", address);
                    TempJson.put("lat",  String.valueOf(la));
                    TempJson.put("lng",  String.valueOf(lo));
                    TempJson.put("time", "NOW");

                    JsonArray = new JSONArray();
                    JsonArray.put(TempJson);

                    String _temp_ = sp.getString("data", "[]");

                    Log.i("vvvvvv",_temp_.substring(0, _temp_.length()-1) +","+ JsonArray.toString().substring(1));

                    temp.add(_temp_.substring(0, _temp_.length()-1) +","+ JsonArray.toString().substring(1) );

                    new serviceProgress(temp).execute();

                    //Toast.makeText(this, "send to server as normally", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //new serviceProgress(temp).execute();
            }
        } else {
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

            //Toast.makeText(this, "Get location at "+currentTime, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("vvvvvv","suppended.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("vvvvvv","not available.");
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

            sp = getSharedPreferences(_PREF_MODE, Context.MODE_PRIVATE);

            this.res = "{\"data\":" + this.arrayList.get(0) + "}";

            body = RequestBody.create(JSON, this.res);
            request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();

            Log.v("vvvvvv", "Clear Text");
//            try {
//                //Response response =
//                client.newCall(request).execute();
//                sp.edit().clear().commit();
//                Log.v("vvvvvv", "Clear Text");
//            } catch (IOException e) {
//                e.printStackTrace();
//
//
//
////                java.util.Date dt = new java.util.Date();
////                java.text.SimpleDateFormat sdf =
////                        new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////
////                String currentTime = sdf.format(dt);
////
////                editor = sp.edit();
////                JsonArray = new JSONArray();
////
////                try {
////
////                    TempJson = new JSONObject();
////                    TempJson.put("usr_id", "11");
////                    TempJson.put("lat",  String.valueOf(la));
////                    TempJson.put("lng",  String.valueOf(lo));
////                    TempJson.put("time", currentTime);
////
////                    JsonArray.put(TempJson);
////
////                    String json = "";   // [] = default value.
////
////                    Log.i("dataTest",sp.getString("data", "[]"));
////
////                    if( sp.getString("data", "[]").equals("[]") ) {
////
////                        json = "";
////                        editor.putString("data", JsonArray.toString());
////
////                    } else {
////                        json = sp.getString("data", "[]");
////                        json = json.substring(1);
////                        json = json.substring(0, json.length()-1);
////
////                        editor.putString("data", "["+json+","+JsonArray.toString().substring(1));
////                    }
////                    editor.commit();
////
////                } catch (JSONException ex) {
////                    ex.printStackTrace();
////                    Log.v("vvvvvv", "Error");
////                }
//
//                Log.v("vvvvvv", "send to server failed");
//            }
            return "";//this.res;
        }
    }







}
