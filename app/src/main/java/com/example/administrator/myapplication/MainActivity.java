package com.example.administrator.myapplication;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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
import java.util.HashSet;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends FragmentActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, OnMapReadyCallback {


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

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences(_PREF_MODE, Context.MODE_PRIVATE);
        sp.edit().clear().commit();

        if (isOnline()) {
            Toast.makeText(this, "Internet available", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "System Stop !!!", Toast.LENGTH_LONG).show();
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

        //drawer.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#330000ff")));
        //drawer.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#550000ff")));

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Bus bus = new Bus();

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

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        Button openMapBtn = (Button) findViewById(R.id.openMapBtn);
        openMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(MainActivity.this, MapsActivity.class);

                TestData ts = new TestData();
                ts.message = "Hello from the activity";
                bus.post(ts);

                startActivity(t);
            }

        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        //       BusProvider.getInstance().register(this);
    }

    @Override
    protected void onResume() {

        super.onResume();
        googleApiClient.connect();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
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
            // other 'case' lines to check for other
            // permissions this app might request
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
        LocationAvailability locationAvailability = LocationServices.FusedLocationApi.getLocationAvailability(googleApiClient);
        //if(locationAvailability.isLocationAvailable()) {
        LocationRequest locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            Toast.makeText(this, "Location provider no longer available !!!", Toast.LENGTH_SHORT).show();
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
//        } else {
//            Toast.makeText(this, "Location provider no longer available !!!", Toast.LENGTH_LONG).show();
//            // Do something when location provider not available
//        }
    }

    @Override
    public void onLocationChanged(Location location) {
        la = location.getLatitude();
        lo = location.getLongitude();
        TextView textView = (TextView) findViewById(R.id.textView2);
        textView.setText("Latitude : " + location.getLatitude() + "\n" +
                "Longistudesmd : " + location.getLongitude());

        ArrayList<String> temp;

        sp = getSharedPreferences(_PREF_MODE, Context.MODE_PRIVATE);
        if(isOnline()) {
            if(sp.getString("data", "[]").equals("[]")) {
                temp = new ArrayList<String>();
                // add ปกติ
                //temp.add(String.valueOf(la));

                //Toast.makeText(this, "send to server as normally", Toast.LENGTH_LONG).show();
                new serviceProgress(temp).execute();

//                Toast.makeText(this, "online + clear text.", Toast.LENGTH_SHORT).show();
//                // android new asyntask
//                temp = new ArrayList<String>();
//                temp.add(sp.getString("and_lat", "0.00"));
//                temp.add(sp.getString("and_lng", "0.00"));
//                temp.add(sp.getString("and_date", "0000-00-00 00:00:00"));
//
//                new serviceProgress(temp).execute();
//                sp.edit().clear().commit();
            } else {
                temp = new ArrayList<String>();
//                temp.add(sp.getString("and_lat", "0.00"));
//                temp.add(sp.getString("and_lng", "0.00"));
//                temp.add(sp.getString("and_date", "0000-00-00 00:00:00"));

                // ถึงตรงนี้
                temp.add(sp.getString("data", "[]"));

                new serviceProgress(temp).execute();
                //sp.edit().clear().commit();
                Toast.makeText(this, "online + clear text.", Toast.LENGTH_SHORT).show();
            }
        } else {
            java.util.Date dt = new java.util.Date();
            java.text.SimpleDateFormat sdf =
                    new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String currentTime = sdf.format(dt);

            editor = sp.edit();
            //JsonObject = new JSONObject();
            JsonArray = new JSONArray();

            //Log.v("XWV" , sp.getString("data", "[]"));

            try {

                TempJson = new JSONObject();
                TempJson.put("usr_id", "7");
                TempJson.put("lat",  String.valueOf(la));
                TempJson.put("lng",  String.valueOf(lo));
                TempJson.put("time", currentTime);

                JsonArray.put(TempJson);

                String json = "";   // [] = default value.

                if( sp.getString("data", "[]").equals("[]") ) {

                    json = "";
                    JsonObject = new JSONObject();
                    editor.putString("data", JsonArray.toString());

                    //Log.v("vvvvvv", json);
                    //Log.v("vvvvvv", json + "," + JsonObject.toString());
                } else {
                    json = sp.getString("data", "[]");
                    json = json.substring(1);
                    json = json.substring(0, json.length()-1);

                    //JsonObject = new JSONObject(json);
                    editor.putString("data", "["+json+","+JsonArray.toString().substring(1));

                }

                //editor.putString("data", JsonArray.toString());
                editor.commit();

                Log.v("dataTest", sp.getString("data", "[]"));
                //JsonObject = new JSONObject(sp.getString("data", "[]"));
                //Log.v("dataTest2", JsonObject.toString());
                //JsonObject
            } catch (JSONException e) {
                e.printStackTrace();
                //editor.putString("data", "");
                //editor.commit();

                Log.v("vvvvvv", "Error");
            }


//            editor.putString("and_lat", String.valueOf(la));
//            editor.putString("and_lng", String.valueOf(lo));
//            editor.putString("and_date", currentTime);
//            editor.putBoolean("and_status", false);

            //editor.commit();

            Toast.makeText(this,currentTime, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    // google map display
    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    private class serviceProgress extends AsyncTask<String, Integer, String> {
        private ProgressDialog pd;

        private OkHttpClient client;

        private ArrayList<String> arrayList;

        private final String url = "http://angsila.informatics.buu.ac.th/~55160509/android/update.php";

        private String res;

        private RequestBody body;
        private Request request;

        private String jsonData;

        private JSONObject jsonObject;

        JSONArray jsonArray;

        JSONObject tempJson;

        public serviceProgress(ArrayList<String> temp) {
            this.arrayList = temp;
            this.body = null;
            this.request = null;
            this.res = "";
        }

        public serviceProgress(ArrayList<String> temp, boolean status) {
            this.arrayList = temp;
            this.body = null;
            this.request = null;
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
            pd.setProgress(values[0]);
        }

        @Override
        protected String doInBackground(String... params)   {
            this.res = "";

            client = new OkHttpClient();
            //Log.v("NNNNN", this.arrayList.get(0));

            this.res = "{\"data\":" + this.arrayList.get(0) + "}";

            body = RequestBody.create(JSON, this.res);
            request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();

            try {
                Response response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

//            try {
//                jsonObject = new JSONObject(this.arrayList.get(0));
//
////                jsonArray = new JSONArray();
////
////                tempJson = new JSONObject();
////                tempJson.put("usr_id", "7");
////                tempJson.put("lat", this.arrayList.get(0));
////                tempJson.put("lng", this.arrayList.get(1));
////                jsonArray.put(tempJson);
////
////                tempJson = new JSONObject();
////                tempJson.put("usr_id", "7");
////                tempJson.put("lat", "9.53435");
////                tempJson.put("lng", "-13.559");
////                jsonArray.put(tempJson);
////
////                jsonObject.put("data", jsonArray);
////
////                Log.v("vvvvvv",jsonObject.toString());
//
////                body = RequestBody.create(JSON, jsonObject.toString());
////                request = new Request.Builder()
////                        .url(url)
////                        .post(body)
////                        .build();
//
//                //Log.v("vvvvvv", "5555555");
//
////                RequestBody formBody = new FormBody.Builder()
////                        .add("usr_id", "7")
////                        .add("lat", this.arrayList.get(0))
////                        .add("lng", this.arrayList.get(1))
////                        .add("date", this.arrayList.get(2))
////                        .build();
//
////                Request request = new Request.Builder()
////                        .url(url)
////                        .post(formBody)
////                        .build();
//
////                try {
////                    Response response = client.newCall(request).execute();
////
////                    //this.res = response.body().toString();
////                    //textViews.setText("3456");
////
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
//
////                RequestBody formBody = new FormBody.Builder()
////                        .add("message", "Your message")
////                        .build();
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }


            //Request.Builder builder = new Request.Builder();
            //Request request = builder.url("http://date.jsontest.com/").build();

            //Response response = okHttpClient.newCall(request).execute();
//            okHttpClient.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(Request request, IOException e) {
//                    updateView("Error - " + e.getMessage());
//                }
//
//                @Override
//                public void onResponse(Response response) {
//                    if (response.isSuccessful()) {
//                        try {
//                            updateView(response.body().string());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                            updateView("Error - " + e.getMessage());
//                        }
//                    } else {
//                        updateView("Not Success - code : " + response.code());
//                    }
//                }
//
//                public void updateView(final String strResult) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            //Toast.makeText(MainActivity.this, strResult, Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//            });
            return this.res;
        }
    }
}
