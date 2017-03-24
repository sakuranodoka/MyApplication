package authen;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;

import com.example.administrator.myapplication.R;

/**
 * Created by Administrator on 24/2/2560.
 */

public class ViewAuthenDialogFragment extends DialogFragment {

    private Bundle b;

    public ViewAuthenDialogFragment(Bundle b) {
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

				 View v = inflater.inflate(R.layout.dialog_authen, container, false);

				 ImageButton backPressedState = (ImageButton) v.findViewById(R.id.backPressedState);
			 	 backPressedState.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							 getDialog().dismiss();
						}
				 });
				 return v;
    }

    @Override
    public void onDestroyView() {
        Dialog dialog = getDialog();
        if (dialog != null && getRetainInstance()) {
            dialog.setDismissMessage(null);
        }
        super.onDestroyView();
    }
}
