package com.example.administrator.myapplication;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import junit.framework.Test;

/**
 * Created by Administrator on 24/10/2559.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "Message";

    Context ctx;
    View v;
    LinearLayout ll;// = (LinearLayout) findViewById(R.id.ll);

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        //BusProvider.getInstance().register(this);

        // Maybe an error occurred.
        final String txt = remoteMessage.getData().get("Test");
        final NotificationData nd = new NotificationData(txt);

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.d("UI thread", "I am the UI thread");

                BusProvider.getInstance().post(nd);
                //TestActivity.callBack.onSended(txt);
                /*if (NotificationData.ctx != null) {


                    //ctx = NotificationData.ctx;
                    //v = LayoutInflater.from(NotificationData.ctx).inflate(R.layout.activity_test, null , false);
                    //(LayoutInflater) NotificationData.ctx.
                            //ctx.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                    //v = inflater.inflate( R.layout.activity_test, null );

                    ll = (LinearLayout) v.findViewById(R.id.ll);

                } else {

                }*/
            }
        });

        //BusProvider.getInstance().post(nd);

        /*if (NotificationData.ctx != null) {
            ctx = NotificationData.ctx;
            inflater = (LayoutInflater) ctx.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            v = inflater.inflate( R.layout.activity_test, null );

            ll = (LinearLayout) v.findViewById(R.id.ll);

        } else {

        }*/


        //BusProvider.getInstance().post(nd);

        Log.v("vvvvvv", "test01:"+txt);

        //BusProvider.getInstance().unregister(TestActivity.class);
        //String box = remoteMessage.getData().get("box");
    }
}
