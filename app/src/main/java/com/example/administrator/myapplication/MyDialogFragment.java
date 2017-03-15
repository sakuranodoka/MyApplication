package com.example.administrator.myapplication;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.DialogFragment;
import android.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

/**
 * Created by Administrator on 29/9/2559.
 */
public class MyDialogFragment extends DialogFragment {

    private GoogleMap mMap;

    private static final String TAG_TASK_FRAGMENT = "load_location_fragment";

    public ParallelDataOne parallelDataOne;
    public ParallelDataTwo parallelDataTwo;

    private String method = "done";

    private String usr_mac_address = "";

    private LoadUserLocationFragment ulFragment;

    public MyDialogFragment() {
    }

    @SuppressLint("ValidFragment")
    public MyDialogFragment(GoogleMap temp, String usr_mac_address) {
        this.mMap = temp;
        this.usr_mac_address = usr_mac_address;
    }

    static MyDialogFragment newInstance() {
        return new MyDialogFragment();
    }

    private FragmentTabHost mTabHost;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog d = new Dialog(getContext());
        return d;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_timepick, container, false);

        getDialog().setTitle("My Dialog Title");
        mTabHost = (FragmentTabHost) v.findViewById(R.id.tabs);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);
        Bundle b = new Bundle();
        b.putString("key", "tab1");
        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("Start"), FragmentTab.class, b);

        b = new Bundle();
        b.putString("key", "tab2");
        mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator("Ending"), FragmentTab.class, b);

        Button btnCancel = (Button) v.findViewById(R.id.cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });

        Button btnDone = (Button) v.findViewById(R.id.done);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("vvvvvv-time", "start:" + changeTime(MapsActivity.hourStart, MapsActivity.minuteStart)+" |stop:" +changeTime(MapsActivity.hourEning, MapsActivity.minuteEnding));

                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();

                // call services

//                ulFragment = new LoadUserLocationFragment(mMap,
//                        changeTime(MapsActivity.hourStart, MapsActivity.minuteStart),
//                        changeTime(MapsActivity.hourEning, MapsActivity.minuteEnding),
//                        "GROUP",
//                        usr_mac_address,
//                        getContext());
//                fragmentManager.beginTransaction().add(ulFragment, TAG_TASK_FRAGMENT).commit();

                getDialog().dismiss();
            }
        });

        return v;
    }

    @Subscribe
    public void getMessage(SearchUserData data) {
        Log.v("vvvvvv", "In Dialog : "+data.getUsr_mac_address());
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onDestroyView() {
        Dialog dialog = getDialog();

        if ((dialog != null) && getRetainInstance())
            dialog.setDismissMessage(null);

        BusProvider.getInstance().unregister(this);

        super.onDestroyView();
        mTabHost = null;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

    java.util.Date dt = new java.util.Date();
    java.text.SimpleDateFormat sdf =
            new java.text.SimpleDateFormat("yyyy-MM-dd");

    private String changeTime(int hour, int minute) {
        String res = sdf.format(dt)+" ";
        if(hour < 10) {
            res = res + "0" + hour + ":";
        } else {
            res = res + hour + ":";
        }

        if(minute < 10 ) {
            res = res + "0" + minute + ":00";
        } else {
            res = res + minute + ":00";
        }

        return res;
    }
}
