package com.example.administrator.myapplication;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.util.Log;
import android.view.animation.Animation;

import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 20/10/2559.
 */

@SuppressLint("ValidFragment")
public class UpdateLocationFragment extends Fragment {


    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    ArrayList<String> arrayList = null;

    public UpdateLocationFragment() { this.arrayList = null; }

    @SuppressLint("ValidFragment")
    public UpdateLocationFragment(ArrayList<String> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Log.v("vvvvvv", "create Fragment");

        setRetainInstance(true);

        new UpdateLocationTask().execute();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public class resultData {
        String result;

        public resultData(String result) {
            this.result = result;
        }

        public resultData() {}

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }

    private class UpdateLocationTask extends AsyncTask<String, Integer, Void> {

        public UpdateLocationTask() { this.data = null; }

        private OkHttpClient client;

        private final String URL = "http://angsila.informatics.buu.ac.th/~55160509/android/update.php";

        private String res;

        private Request request;

        private resultData data;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
            //Log.i("vvvvvv", "data After Bus : " + this.data.getResult());
        }

        protected void onProgressUpdate(Integer... values) {}

        @Override
        protected Void doInBackground(String... params) {
            this.client = new OkHttpClient();


            Log.v("vvvvvv", arrayList.get(0));

            this.res = "{\"data\":" + arrayList.get(0) + "}";

            request = new Request.Builder()
                    .url(URL)
                    .post( RequestBody.create(JSON, this.res) )
                    .build();

            //resultData rs = new resultData();
            //rs.setResult("2EZ4RTZ");

            try {
                client.newCall(request).execute();
                Log.v("vvvvvv", "Clear Text");
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Set Response
            return null;
        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled();
            //BusProvider.getAnyInstance().unregister(this);
        }
    }
}
