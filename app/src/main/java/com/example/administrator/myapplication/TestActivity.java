package com.example.administrator.myapplication;

import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

public class TestActivity extends AppCompatActivity {

    private static final String TAG = "TestActivity";

    NotificationData data;

    public static interface callB {
        void onSended(String txt);
    }

    public TestActivity.callB callBack = new TestActivity.callB() {


        @Override
        public void onSended(String txt) {
            Log.e("vvvvvv", txt);
            LinearLayout linearLayout = (LinearLayout) TestActivity.this.findViewById(R.id.ll);
        }
    };

    //callB callBackPlease =

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        //data = new NotificationData("");
        NotificationData.ctx = getApplicationContext();

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }

        Button logTokenButton = (Button) findViewById(R.id.button);
        logTokenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "InstanceID token: " + FirebaseInstanceId.getInstance().getToken());
            }
        });


    }

    @Subscribe
    public void getMessage(NotificationData data) {
        Log.e("vvvvvv", data.getMessage());

        TextView tv = new TextView(this);
        tv.setText(data.getMessage());
        //tv.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
        //tv.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
        tv.setGravity(Gravity.RIGHT);
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
        ll.addView(tv);
        //Toast.makeText(this, data.getMessage(), Toast.LENGTH_LONG).show();
    }

//    @Produce
//    public NotificationData lastestMessage() {
//        //Log.v("vvvvvv", data.getMessage());
//
//        //Toast.makeText(this, data.getMessage(), Toast.LENGTH_LONG).show();
//        return new NotificationData(this.data.getMessage());
//    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
        //NotificationData.ctx = getApplicationContext();
        //Log.v("vvvvvv", NotificationData.Data.getMessage());
    }

    @Override
    protected void onPause() {
        super.onPause();
        //NotificationData.ctx = null;
        BusProvider.getAnyInstance().unregister(this);
    }
}
