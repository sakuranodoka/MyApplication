package seller.viewholder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

import seller.SellerGraphType;

/**
 * Created by Administrator on 8/12/2559.
 */
public class DialogFragmentGraphOption extends DialogFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    final RadioButton [] rb = new RadioButton[3];

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCancelable(false);

        View v = inflater.inflate(R.layout.dialog_seller_detail, container, false);

//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        lp.copyFrom( getActivity().getWindow().getAttributes());
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        lp.gravity = Gravity.CENTER;
//
//        getActivity().getWindow().setAttributes(lp);

        getDialog().setTitle("My Dialog Title");

//        TextView dialogHeader = (TextView) v.findViewById(R.id.dialog_header);
//        //dialogHeader.setText("เลือกประเภทรายงานการแสดงผล");
//
//        LinearLayout content = (LinearLayout) v.findViewById(R.id.dialog_content);
//
//        LinearLayout.LayoutParams llPm =
//                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//
//        float density = getActivity().getResources().getDisplayMetrics().density;
//        int paddingDp = (int)(7 * density);
//
//        LinearLayout llNr = new LinearLayout(getContext());
//
//        llNr.setLayoutParams(llPm);
//        llNr.setOrientation(LinearLayout.VERTICAL);
//        llNr.setPadding(paddingDp,paddingDp,paddingDp,paddingDp);
//        llNr.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.angel_white));
//        llNr.setGravity(Gravity.CENTER_HORIZONTAL);
//
//        dialogHeader.setText("เลือกรูปแบบการแสดงผล");
//
//        //final RadioButton[] rb = new RadioButton[3];
//        // Secondary let's standing with radio group
//        final RadioGroup rg = new RadioGroup(getContext());
//        rg.setId(0);
//        rg.setOrientation(RadioGroup.HORIZONTAL);
//        rg.setGravity(Gravity.CENTER);
//        rg.setLayoutParams(new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT));
//
//        rb[0] = new RadioButton(getContext());
//        rb[0].setText("กราฟเส้น");
//        rb[0].setId(SellerGraphType.TYPE_GRAPH_LINE+0);
//
//        rg.addView(rb[0]);
//
//        rb[1] = new RadioButton(getContext());
//        rb[1].setText("กราฟแท่ง");
//        rb[1].setId(SellerGraphType.TYPE_GRAPH_BAR+0);
//
//        rg.addView(rb[1]);
//
//        rb[2] = new RadioButton(getContext());
//        rb[2].setText("กราฟพาย");
//        rb[2].setId(SellerGraphType.TYPE_GRAPH_PIE+0);
//
//        rg.addView(rb[2]);
//
//        rg.check(SellerGraphType.TYPE_GRAPH_PIE+0);
//
//        llNr.addView(rg);
//
//        content.addView(llNr);
//
//        llNr = new LinearLayout(getContext());
//
//        llPm.setMargins(0, 14 , 0 , 0);
//
//        llNr.setLayoutParams(llPm);
//        llNr.setOrientation(LinearLayout.HORIZONTAL);
//        llNr.setPadding(paddingDp,paddingDp,paddingDp,paddingDp);
//        llNr.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.angel_white));
//        llNr.setGravity(Gravity.CENTER);
//
//        Button submitButton = new Button(getContext());
//        submitButton.setGravity(Gravity.CENTER);
//        submitButton.setPaddingRelative(110, paddingDp, 110, paddingDp);
//        submitButton.setTextColor( ContextCompat.getColor(getContext(), R.color.angel_white) );
//        submitButton.setBackgroundColor(  ContextCompat.getColor(getContext(), R.color.honest_green) );
//        submitButton.setText("เลือก");
//        submitButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int selectedId = rg.getCheckedRadioButtonId();
//                getDialog().dismiss();
//
//                //optionInterface.GraphSelected(selectedId);
//                Log.e("eeeeee", selectedId+"");
//            }
//        });
//
//        llNr.addView(submitButton);
//
//        content.addView(llNr);

        return v;
    }
}
