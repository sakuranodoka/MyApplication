package com.example.administrator.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 17/10/2559.
 */
public class LoadSearchUserData extends AsyncTask<String, Integer, String> {

    public final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private ProgressDialog pd;

    private final String URL = "http://angsila.informatics.buu.ac.th/~55160509/Application/index.php/getSearchUser/data";

    private String res;

    private OkHttpClient client;

    private RequestBody body;
    private Request request;

    private FragmentManager fragmentManager = null;
    private Context context = null;

    public LoadSearchUserData() {
        this.res = "";
    }

    private String[] userData = null;

    public LoadSearchUserData(FragmentManager fragmentManager, Context context) {
        this.fragmentManager = fragmentManager;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(this.context);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setTitle("กรุณารอสักครู่...");
        pd.setCancelable(false);
        pd.setIndeterminate(false);
        pd.setMax(100);
        pd.setProgress(0);
        pd.show();
    }

    @Override
    protected void onPostExecute(String result) {
        pd.dismiss();

        if(this.fragmentManager != null) {

            try {
                jsonObject = new JSONObject(result);

                jsonArray = jsonObject.getJSONArray("data");

                int jsSize = jsonArray.length();

                List<String> stockList = new ArrayList<String>();

                JSONObject temp = null;

                for(int i = 0 ; i < jsSize; i++) {
                    temp = jsonArray.getJSONObject(i);
                    stockList.add(temp.get("usr_mac_address").toString());
                }

                this.userData = new String[stockList.size()];
                this.userData = stockList.toArray(this.userData);

                //stockList.toArray(new String[stockList.size()]);

                //View rootView = ((AppCompatActivity)this.context).getWindow().getDecorView();//.findViewById(android.R.id.content);

                //String colors[] = {"Red","Blue","White","Yellow","Black", "Green","Purple","Orange","Grey"};

                //ViewGroup add_phone = (ViewGroup) this.context.getLayoutInflater().inflate(R.layout.dialog_search_user, null);

                //LayoutInflater inflater = (LayoutInflater) this.context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                //View rootView = inflater.inflate(R.layout.dialog_search_user, null);

//                Spinner spinner = (Spinner) rootView.findViewById(R.id.spiinerSearchUser);
//                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this.context, android.R.layout.simple_spinner_dropdown_item, this.userData); //selected item will look like a spinner set from XML
//                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                spinner.setAdapter(spinnerArrayAdapter);

                SearchUserDialogFragment newFragment = new SearchUserDialogFragment(this.userData);
                newFragment.show(this.fragmentManager, "Open Dialog");

            } catch (JSONException e) {
                Log.i("vvvvvv", "JSON Parse failed.");
                e.printStackTrace();
            }
        }

//        if (this.mMap != null) {
//            this.mMap.clear();
//        } else {
//
//            try {
//                jsonObject = new JSONObject(result);
//
//                jsonArray = jsonObject.getJSONArray("data");
//
//            } catch (JSONException e) {
//                Log.i("vvvvvv", "JSON Parse failed.");
//                e.printStackTrace();
//            }
////            pd.dismiss();
//        }
    }

    protected void onProgressUpdate(Integer... values) {
        pd.setProgress(values[0]);
    }

    JSONObject jsonObject;

    JSONObject jsonRequest;

    JSONArray jsonArray;

    @Override
    protected String doInBackground(String... params)   {
        client = new OkHttpClient();

        jsonObject = new JSONObject();
        try {

            //jsonObject.put("usr_id", this.usr_id);
            //jsonObject.put("time_start", this.dateStart);
            //jsonObject.put("time_ending", this.dateEnd);
            //jsonObject.put("method", this.method);

            //jsonArray = new JSONArray();
            //jsonArray.put(jsonObject);

            jsonRequest = new JSONObject();
            jsonRequest.put("data", "[]");

            body = RequestBody.create(JSON, jsonRequest.toString());
            request = new Request.Builder()
                    .url(URL)
                    .post(body)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                JSONObject responseObj = new JSONObject(response.body().string());

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