package seller;

import android.app.Activity;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

/**
 * Created by Administrator on 29/11/2559.
 */
public class ViewDialogOption extends DialogFragment {

    private String itemCode;

    private Activity activity;

    private Dialog dialog;

    final RadioButton [] rb = new RadioButton[3];

    public void showDialog(final Activity activity, final InterfaceOnOption optionInterface) {

        setRetainInstance(true);

        this.activity = activity;

        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_seller_detail);

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
        //dialogHeader.setText("เลือกประเภทรายงานการแสดงผล");

        LinearLayout content = (LinearLayout) dialog.findViewById(R.id.dialog_content);

        LinearLayout.LayoutParams llPm =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        float density = activity.getResources().getDisplayMetrics().density;
        int paddingDp = (int)(7 * density);

        LinearLayout llNr = new LinearLayout(activity);

        llNr.setLayoutParams(llPm);
        llNr.setOrientation(LinearLayout.VERTICAL);
        llNr.setPadding(paddingDp,paddingDp,paddingDp,paddingDp);
        llNr.setBackgroundColor(ContextCompat.getColor(activity, R.color.angel_white));
        llNr.setGravity(Gravity.CENTER_HORIZONTAL);

        dialogHeader.setText("เลือกรูปแบบการแสดงผล");

        //final RadioButton[] rb = new RadioButton[3];
        // Secondary let's standing with radio group
        final RadioGroup rg = new RadioGroup(activity);
        rg.setId(0);
        rg.setOrientation(RadioGroup.HORIZONTAL);
        rg.setGravity(Gravity.CENTER);
        rg.setLayoutParams(new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT));

//                LinearLayout setLg = new LinearLayout(activity);
//
//                setLg.setLayoutParams(llPm);
//                setLg.setOrientation(LinearLayout.VERTICAL);
//
//                    // next, stand for vertical align
//                    LinearLayout ll1Pad = new LinearLayout(activity);
//
//                    ll1Pad.setLayoutParams(llPm);
//                    ll1Pad.setOrientation(LinearLayout.VERTICAL);
//                    ll1Pad.setPaddingRelative(paddingDp,paddingDp,paddingDp,paddingDp);
//                    ll1Pad.setBackgroundColor(ContextCompat.getColor(activity, R.color.angel_white));
//
//                    // Inside this horizontal linear layout combine one of radio and set its label
//                    LinearLayout llInside = null;
//
//                    llInside = new LinearLayout(activity);
//                    llInside.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
//                    llInside.setOrientation(LinearLayout.HORIZONTAL);
//                    llInside.setGravity(Gravity.RIGHT);
//
//                    //RadioButton radioButton = createRadioButton(activity, "กราฟแท่ง", 0);
//                    rb[0] = new RadioButton(activity);
//                    rb[0].setText("กราฟแท่ง");
//                    rb[0].setId(0+100);
//
//                    llInside.addView(rb[0]);
//
//                    TextView detailTextView = createTextDetail(activity, "เปรียบเทียบยอดขายสินค้าแบบทบยอด");
//
//                    ll1Pad.addView(llInside);
//                    ll1Pad.addView(detailTextView);
//
//                setLg.addView(ll1Pad);
//
//                    ll1Pad = new LinearLayout(activity);
//
//                    ll1Pad.setLayoutParams(llPm);
//                    ll1Pad.setOrientation(LinearLayout.VERTICAL);
//                    ll1Pad.setPaddingRelative(paddingDp,paddingDp,paddingDp,paddingDp);
//                    ll1Pad.setBackgroundColor(ContextCompat.getColor(activity, R.color.angel_white));
//
//                    // Inside this horizontal linear layout combine one of radio and set its label
//                    llInside = new LinearLayout(activity);
//                    llInside.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
//                    llInside.setOrientation(LinearLayout.HORIZONTAL);
//                    llInside.setGravity(Gravity.RIGHT);
//
//                    //radioButton = createRadioButton(activity, "กราฟเส้น", 1);
//                    rb[1] = new RadioButton(activity);
//                    rb[1].setText("กราฟเส้น");
//                    rb[1].setId(1+100);
//
//                    llInside.addView(rb[1]);
//
//                    detailTextView = createTextDetail(activity, "เปรียบเทียบยอดขายแบบเส้น");
//
//                    ll1Pad.addView(llInside);
//                    ll1Pad.addView(detailTextView);
//
//                setLg.addView(ll1Pad);
//
//                    ll1Pad = new LinearLayout(activity);
//
//                    ll1Pad.setLayoutParams(llPm);
//                    ll1Pad.setOrientation(LinearLayout.VERTICAL);
//                    ll1Pad.setPaddingRelative(paddingDp,paddingDp,paddingDp,paddingDp);
//                    ll1Pad.setBackgroundColor(ContextCompat.getColor(activity, R.color.angel_white));
//
//                    // Inside this horizontal linear layout combine one of radio and set its label
//
//                    llInside = new LinearLayout(activity);
//                    llInside.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
//                    llInside.setOrientation(LinearLayout.HORIZONTAL);
//                    llInside.setGravity(Gravity.RIGHT);
//
//                    //radioButton = createRadioButton(activity, "กราฟพาย", 2);
//                    rb[2] = new RadioButton(activity);
//                    rb[2].setText("กราฟพาย");
//                    rb[2].setId(2+100);
//
//                    llInside.addView(rb[2]);
//
//
//                    detailTextView = createTextDetail(activity, "เปรียบเทียบยอดขายสินค้าเชิงสัดส่วน");
//
//                    ll1Pad.addView(llInside);
//                    ll1Pad.addView(detailTextView);
//
//                setLg.addView(ll1Pad);

        //rg.addView(setLg);


        rb[0] = new RadioButton(activity);
        rb[0].setText("กราฟเส้น");
        rb[0].setId(SellerGraphType.TYPE_GRAPH_LINE+0);

        rg.addView(rb[0]);

        rb[1] = new RadioButton(activity);
        rb[1].setText("กราฟแท่ง");
        rb[1].setId(SellerGraphType.TYPE_GRAPH_BAR+0);

        rg.addView(rb[1]);

        rb[2] = new RadioButton(activity);
        rb[2].setText("กราฟพาย");
        rb[2].setId(SellerGraphType.TYPE_GRAPH_PIE+0);

        rg.addView(rb[2]);

        rg.check(SellerGraphType.TYPE_GRAPH_PIE+0);

        llNr.addView(rg);

        content.addView(llNr);

        llNr = new LinearLayout(activity);

        llPm.setMargins(0, 14 , 0 , 0);

        llNr.setLayoutParams(llPm);
        llNr.setOrientation(LinearLayout.HORIZONTAL);
        llNr.setPadding(paddingDp,paddingDp,paddingDp,paddingDp);
        llNr.setBackgroundColor(ContextCompat.getColor(activity, R.color.angel_white));
        llNr.setGravity(Gravity.CENTER);

        Button submitButton = new Button(activity);
        submitButton.setGravity(Gravity.CENTER);
        submitButton.setPaddingRelative(110, paddingDp, 110, paddingDp);
        submitButton.setTextColor( ContextCompat.getColor(activity, R.color.angel_white) );
        submitButton.setBackgroundColor(  ContextCompat.getColor(activity, R.color.honest_green) );
        submitButton.setText("เลือก");
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = rg.getCheckedRadioButtonId();
                dialog.dismiss();

                optionInterface.GraphSelected(selectedId);
                Log.e("eeeeee", selectedId+"");
            }
        });

        llNr.addView(submitButton);

        content.addView(llNr);

        dialog.show();
    }

//    private LinearLayout createLL1Pad(Activity activity) {
//
//        float density = activity.getResources().getDisplayMetrics().density;
//        int paddingDp = (int)(7 * density);
//
//        LinearLayout ll1Pad = new LinearLayout(activity);
//
//        ll1Pad.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//        ll1Pad.setOrientation(LinearLayout.VERTICAL);
//        ll1Pad.setPaddingRelative(paddingDp,paddingDp,paddingDp,paddingDp);
//        ll1Pad.setBackgroundColor(ContextCompat.getColor(activity, R.color.angel_white));
//
//
//    }

    private RadioButton createRadioButton(Activity activity, final String label, final int labelId) {
        rb[labelId] = new RadioButton(activity);
        rb[labelId].setText(label);
        rb[labelId].setId(labelId+100);
        return rb[labelId];
    }

    private TextView createTextDetail(Activity activity, final String label) {
        TextView detailTextView = new TextView(activity);
        detailTextView.setLayoutParams
                (new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        detailTextView.setText(label);
        detailTextView.setGravity(Gravity.RIGHT);
        detailTextView.setTextColor(ContextCompat.getColor(activity, R.color.space_gray));
        detailTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        return detailTextView;
    }
}
