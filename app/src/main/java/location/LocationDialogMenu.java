package location;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.myapplication.LocationActivity;
import com.example.administrator.myapplication.MyDialogFragment;
import com.example.administrator.myapplication.R;
import com.google.android.gms.maps.GoogleMap;

import seller.InterfaceOnOption;

/**
 * Created by Administrator on 6/12/2559.
 */
public class LocationDialogMenu {

    private Activity activity;

    private Dialog dialog;

    public void showDialog(final AppCompatActivity activity, final GoogleMap mMap, final String usr_mac_address) {

        this.activity = activity;

        final FragmentManager fm = activity.getSupportFragmentManager();

        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_location_option);

        ImageView dialogButton = (ImageView) dialog.findViewById(R.id.dialog_close);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);

        TextView dialogHeader = (TextView) dialog.findViewById(R.id.dialog_header);
        dialogHeader.setText("ส่วนแสดงผลเพิ่มเติม");

        LinearLayout rangeTimePicked = (LinearLayout) dialog.findViewById(R.id.layout_range_picked);

        if(rangeTimePicked != null) {
            rangeTimePicked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //new LocationDialogRangeTime().showDialog(activity);

                            MyDialogFragment newFragment = new MyDialogFragment(mMap, usr_mac_address);
                            newFragment.show(fm, "Open Dialog");
                        }
                    }, 500);
                }
            });
        }

        dialog.show();
    }
}
