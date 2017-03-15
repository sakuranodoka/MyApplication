package location;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTabHost;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.myapplication.FragmentTab;
import com.example.administrator.myapplication.R;

/**
 * Created by Administrator on 6/12/2559.
 */
public class LocationDialogRangeTime extends DialogFragment {

    private Activity activity;

    private Dialog dialog;

    private FragmentTabHost mTabHost;

    public void showDialog(final Activity activity) {

        this.activity = activity;

        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_timepick);

//        ImageView dialogButton = (ImageView) dialog.findViewById(R.id.dialog_close);
//        dialogButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);

//        TextView dialogHeader = (TextView) dialog.findViewById(R.id.dialog_header);
//        dialogHeader.setText("ระบุเวลาสำหรับแสดงผล");

        mTabHost = (FragmentTabHost) activity.findViewById(R.id.tabs);
        mTabHost.setup(activity, getFragmentManager(), R.id.realtabcontent);

        Bundle b = new Bundle();
        b.putString("key", "tab1");
        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("Start"), FragmentTab.class, b);

        b = new Bundle();
        b.putString("key", "tab2");
        mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator("Ending"), FragmentTab.class, b);

        Button btnCancel = (Button) activity.findViewById(R.id.cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        Button btnDone = (Button) activity.findViewById(R.id.done);

        dialog.show();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }
}