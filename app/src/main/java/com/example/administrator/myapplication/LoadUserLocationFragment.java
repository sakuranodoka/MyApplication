package com.example.administrator.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 21/10/2559.
 */
public class LoadUserLocationFragment extends Fragment {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public LoadUserLocationFragment() {}

    public GoogleMap mMap;

    public Context context;

    public String usr_id;

    public String dateStart;

    public String dateEnd;

    public String method;

    public LoadUserLocationFragment(GoogleMap mMap, String usr_id,Context context){
        this.usr_id = usr_id;

        this.mMap = mMap;
        this.dateStart = "2016-01-01 00:00:00";
        this.dateEnd = "2016-01-01 00:00:00";
        this.method = "SET";

        this.context = context;
    }

    public LoadUserLocationFragment(GoogleMap mMap, String dateStart, String dateEnd, String method, String usr_id,Context context) {
        this.usr_id = usr_id;

        this.mMap = mMap;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.method = method;

        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        new LoadUserLocationTask().execute();
    }

    private class LoadUserLocationTask extends AsyncTask<String, Integer, String> {
        private ProgressDialog pd = null;

        private OkHttpClient client;

        private final String URL = "http://angsila.informatics.buu.ac.th/~55160509/android/getdata.php";

        private String res;

        private RequestBody body;

        private Request request;

        private JSONObject jsonObject;

        private JSONObject jsonRequest;

        private JSONArray jsonArray;

        public LoadUserLocationTask() {}

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.setTitle("กรุณารอสักครู่...");
            pd.setCancelable(false);
            pd.setIndeterminate(false);
            pd.setMax(100);
            pd.setProgress(0);
            pd.show();
        }

//    int hourSt;
//    int minuteSt;
//    int hourEd;
//    int minuteEd;

        @Override
        protected void onPostExecute(String result)  {
            pd.dismiss();

            if(mMap != null) {
                mMap.clear();
            } else {

            }
            //Log.i("NPC-Testing", result);

            // Re-used variable.
            try {
                jsonObject = new JSONObject(result);

                jsonArray = jsonObject.getJSONArray("data");

                int jsSize = jsonArray.length();
                JSONObject temp;

                PolylineOptions rectLine = new PolylineOptions()
                        .width(13)
                        .color(Color.rgb(0x23, 0x92, 0x99));

                LatLng latLng = null;

                Calendar cal1 = Calendar.getInstance();
                Calendar cal2 = Calendar.getInstance();

                Date date1 = null;
                Date date2 = null;

                if(jsSize == 0) {
                    new AlertDialog.Builder(context)
                            .setTitle("แจ้งเตือน")
                            .setMessage("ไม่พบตำแหน่งของผู้ใช้ในช่วงระยะเวลาดังกล่าว")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } else {

                    for (int i = 0; i < jsSize; i++) {
                        temp = jsonArray.getJSONObject(i);

                        latLng = new LatLng(Float.parseFloat(temp.get("and_lat").toString()), Float.parseFloat(temp.get("and_lng").toString()));
                        rectLine.add(latLng);

//                try {
//                    if(i == 0) {
//                        date1 = format.parse(temp.get("and_date").toString());
//                        rectLine.add(latLng);
//                    } else {
//                        //cal1.setTime(date1);
//
//                        date2 = format.parse(temp.get("and_date").toString());
//                        //cal2.setTime( date2 );
//
//                        this.hourSt = date1.getHours();
//                        this.minuteSt = date1.getMinutes();
//
//                        this.hourEd = date2.getHours();
//                        this.minuteEd = date2.getMinutes();
//
//                        if( Math.abs(((this.hourSt*60)+(this.minuteSt)) - ((this.hourEd*60)+(this.minuteEd))) <= 5 ) {
//                            rectLine.add(latLng);
//                        } else {
//
//                            if(latLng != null) {
//                                mMap.addMarker(new MarkerOptions().position(latLng).title("ตำแหน่งปัจจุบัน"));
//                                mMap.addPolyline(rectLine);
//                            }
//
//                            rectLine = new PolylineOptions()
//                                    .width(13)
//                                    .color(Color.rgb(0x23, 0x92, 0x99));
//                        }
//
//                    }
//                     date1 = format.parse(temp.get("and_date").toString());
//
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }

                    }
                }

                if(latLng != null) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    mMap.addMarker(new MarkerOptions().position(latLng).title("ตำแหน่งปลายทาง"));
                    mMap.addPolyline(rectLine);
                }
            } catch (JSONException e) {
                Log.i("vvvvvv", "JSON Parse failed.");
                e.printStackTrace();
            }
        }

        protected void onProgressUpdate(Integer... values) {
            pd.setProgress(values[0]);
        }

        @Override
        protected String doInBackground(String... params) {
            client = new OkHttpClient();

            jsonObject = new JSONObject();
            try {

                Log.i("test_time", dateStart+" "+dateEnd);

                jsonObject.put("usr_id", usr_id);
                jsonObject.put("time_start", dateStart);
                jsonObject.put("time_ending", dateEnd);
                jsonObject.put("method", method);

                jsonArray = new JSONArray();
                jsonArray.put(jsonObject);

                jsonRequest = new JSONObject();
                jsonRequest.put("data", jsonArray);

                //Log.i("vvvvvv", "responseObj: " + jsonRequest.toString());

                body = RequestBody.create(JSON, jsonRequest.toString());
                request = new Request.Builder()
                        .url(URL)
                        .post(body)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    JSONObject responseObj = new JSONObject(response.body().string());

                    // Log for check response data
                    //Log.i("vvvvvv", "responseObj: " + responseObj.toString());

                    this.res = responseObj.toString();
                } catch (IOException e) {
                    Log.v("vvvvvv", "response json error");
                    e.printStackTrace();
                } catch (JSONException e) {
                    Log.v("vvvvvv", "json error");
                    e.printStackTrace();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return this.res;
        }
    }
}
