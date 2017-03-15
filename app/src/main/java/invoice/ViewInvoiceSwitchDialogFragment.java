package invoice;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.administrator.myapplication.CanvasActivity;
import com.example.administrator.myapplication.InvoiceInfoActivity;
import com.example.administrator.myapplication.R;

import intent.IntentKeycode;
import user.InterfaceCamera;

/**
 * Created by Administrator on 24/2/2560.
 */

public class ViewInvoiceSwitchDialogFragment extends DialogFragment {

    private InterfaceCamera interfaceCamera;
    private Bundle b;

    public ViewInvoiceSwitchDialogFragment(InterfaceCamera interfaceCamera, Bundle b) {
			 this.interfaceCamera = interfaceCamera;
			 this.b = b;
    }

    // ใช้สำหรับรับส่ง Interface ขณะ ปิด/หมุนจอ/ Everything (ใช้ดีมาก)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if(dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
				 setRetainInstance(true);

				 View v = inflater.inflate(R.layout.dialog_invoice, container, false);

				 ImageButton backPressedState = (ImageButton) v.findViewById(R.id.backPressedState);
			 	 backPressedState.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							 getDialog().dismiss();
						}
				 });

				 Button btnMoreScan = (Button) v.findViewById(R.id.btn_more_scan);
				 if(btnMoreScan != null) {
						btnMoreScan.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									 getDialog().dismiss();
									 interfaceCamera.onBarcodeScan(InvoiceData.INVOICE_CASE_INVOICE_PREVIEW, InvoiceData.INVOICE_PREVIEW_PRODUCT, R.layout.view_barcode);
								}
						});
				 }

				 Button btnMoreInfo = (Button) v.findViewById(R.id.btn_more_info);
				 btnMoreInfo.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							 Intent t = new Intent(getActivity(), InvoiceInfoActivity.class);
							 Bundle _b_ = b;
							 t.putExtras(_b_);
							 getActivity().startActivityForResult(t, 1929);
						}
				 });

				 Button btnNext = (Button) v.findViewById(R.id.btn_next);
				 btnNext.setOnClickListener(new View.OnClickListener() {
				 @Override
				 public void onClick(View v) {
						getDialog().dismiss();
						Intent t = new Intent(getActivity(), CanvasActivity.class);
						getActivity().startActivityForResult(t, IntentKeycode.RESULT_CANVAS);
				 }
				 });

				 return v;
    }

    @Override
    public void onDestroyView() {
        Dialog dialog = getDialog();
        // thank stack
        // handles https://code.google.com/p/android/issues/detail?id=17423
        if (dialog != null && getRetainInstance()) {
            dialog.setDismissMessage(null);
        }
        super.onDestroyView();
    }
}
