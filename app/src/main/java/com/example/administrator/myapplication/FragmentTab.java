package com.example.administrator.myapplication;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import org.w3c.dom.Text;

/**
 * Created by Administrator on 29/9/2559.
 */
public class FragmentTab extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private TimePicker timePicker;

    public static Bus bus;

    ParallelDataOne parallelDataOne;
    ParallelDataTwo parallelDataTwo;

    String value;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_blank,
                null);

        bus = new Bus(ThreadEnforcer.MAIN);
        bus.register(getContext());

        //tv = (TextView) v.findViewById(R.id.tv);

        value = getArguments().getString("key");

        timePicker = (TimePicker) v.findViewById(R.id.timePicker3);

        if(MapsActivity.hourStart == -1) {
            parallelDataOne = new ParallelDataOne();
            parallelDataOne.setMinute(timePicker.getCurrentMinute());
            parallelDataOne.setHour(timePicker.getCurrentHour());
            bus.post(parallelDataOne);
        }

        if(MapsActivity.hourEning == -1) {
            parallelDataTwo = new ParallelDataTwo();
            parallelDataTwo.setMinute(timePicker.getCurrentMinute());
            parallelDataTwo.setHour(timePicker.getCurrentHour());
            bus.post(parallelDataTwo);
        }

        if ((MapsActivity.hourStart != -1) && (MapsActivity.hourEning != -1)) {
            switch (value) {
                case "tab1":
                    timePicker.setCurrentHour(MapsActivity.hourStart);
                    timePicker.setCurrentMinute(MapsActivity.minuteStart);
                    break;
                case "tab2":
                    timePicker.setCurrentHour(MapsActivity.hourEning);
                    timePicker.setCurrentMinute(MapsActivity.minuteEnding);
                    break;
                default:
                    break;
            }
        }

        timePicker.setOnTimeChangedListener(
                new TimePicker.OnTimeChangedListener() {

                    @Override
                    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                        if (getArguments() != null) {
                            //
                            try {
                                //tv.setText("Current Tab is: " + value);
                                switch(value) {
                                    case "tab1" :
                                        parallelDataOne = new ParallelDataOne();
                                        parallelDataOne.setMinute(minute);
                                        parallelDataOne.setHour(hourOfDay);
                                        // Send Otto
                                        bus.post(parallelDataOne);

                                        Log.i("vvvvvv-time", "start:" +MapsActivity.hourStart +"|"+ MapsActivity.minuteStart+" |stop:" +MapsActivity.hourEning+"|"+ MapsActivity.minuteEnding);
                                        break;
                                    case "tab2" :
                                        parallelDataTwo = new ParallelDataTwo();
                                        //ts.message = "Hello from the activity";
                                        parallelDataTwo.setMinute(minute);
                                        parallelDataTwo.setHour(hourOfDay);
                                        bus.post(parallelDataTwo);

                                        Log.i("vvvvvv-time", "start:" +MapsActivity.hourStart +"|"+ MapsActivity.minuteStart+" |stop:" +MapsActivity.hourEning+"|"+ MapsActivity.minuteEnding);
                                        break;
                                    default:break;
                                }
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );

        //TextView tv = (TextView) v.findViewById(R.id.text);
        //tv.setText(this.getTag() + " Content");
        return v;
    }

    @Override
    public void onStop() {
        super.onStop();
        //Log.v("vvvvvv", "delete Bus");
        bus.unregister(getContext());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
        }
    }
}
