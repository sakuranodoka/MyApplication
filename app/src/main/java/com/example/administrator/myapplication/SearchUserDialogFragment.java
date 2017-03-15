package com.example.administrator.myapplication;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by Administrator on 17/10/2559.
 */
public class SearchUserDialogFragment extends DialogFragment {

    static SearchUserDialogFragment newInstance() {
        return new SearchUserDialogFragment();
    }

    public SearchUserDialogFragment(){}

    private String[] dataSet = null;

    private Spinner spinner = null;

    private View v = null;

    @SuppressLint("ValidFragment")
    public SearchUserDialogFragment(String[] dataSet) {
        this.dataSet = dataSet;

        //bus = new Bus(ThreadEnforcer.MAIN);
        //bus.register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.dialog_search_user, container, false);
        if(this.dataSet != null) {

            spinner = (Spinner) v.findViewById(R.id.spinnerSearchUser);
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, this.dataSet); //selected item will look like a spinner set from XML
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerArrayAdapter);
        }

        getDialog().setTitle("กรุณาเลือกผู้ใช้งาน");

        Button doneSearchUserBtn = (Button) v.findViewById(R.id.doneSearchUser);
        doneSearchUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btnView) {
                spinner = (Spinner) v.findViewById(R.id.spinnerSearchUser);
                Log.v("vvvvvv", "spinner:value "+spinner.getSelectedItem().toString());

                SearchUserData searchUserData = new SearchUserData( spinner.getSelectedItem().toString() );
                //bus.post(searchUserData);
                //BusProvider busProvider = new BusProvider();
                //BusProvider.getInstance().register(this);//
                BusProvider.getInstance().post(searchUserData);
                getDialog().dismiss();
            }
        });

        Button cancelSearchUserBtn = (Button) v.findViewById(R.id.cancelSearchUser);
        cancelSearchUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btnView) {
                getDialog().cancel();
            }
        });

        return v;
    }

    @Override
    public void onDestroyView() {
        Dialog dialog = getDialog();

        if ((dialog != null) && getRetainInstance())
            dialog.setDismissMessage(null);

        super.onDestroyView();
    }
}
