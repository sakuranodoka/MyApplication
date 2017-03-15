package seller;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.example.administrator.myapplication.R;

import autocomplete.InstantAutocomplete;
import seller.titlebar.SellerTitleBar;
import seller.titlebar.InterfaceOnTitleBar;
import seller.titlebar.SellerTitleDAO;
import seller.item.ItemSellerTitle;

/**
 * Created by Administrator on 13/12/2559.
 */
public class ViewReportDialogFragment extends DialogFragment {

    private InterfaceOnTitleBar interfaceOnTitleBar;

    private int reportPicked = 0;

    @SuppressLint("ValidFragment")
    public ViewReportDialogFragment(InterfaceOnTitleBar interfaceOnTitleBar) {
        this.interfaceOnTitleBar = interfaceOnTitleBar;
    }

    // ใช้สำหรับรับส่ง Interface ขณะ ปิด/หมุนจอ/ Everything (ใช้ดีมาก)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InterfaceOnTitleBar) {
            interfaceOnTitleBar = ((InterfaceOnTitleBar) context);
        }
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setRetainInstance(true);

        View v = inflater.inflate(R.layout.dialog_report_picker, container, false);

        final InstantAutocomplete option = (InstantAutocomplete) v.findViewById(R.id.option);

        final ItemSellerTitle itemSellerTitle = new ItemSellerTitle();
        itemSellerTitle.setListTitleDescription(SellerTitleBar.getSellerTitleList());

        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                option.showDropDown();
            }
        });
        option.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                reportPicked = position;
            }
        });

        ArrayAdapter<SellerTitleDAO> autoCompleteAdapter = new ArrayAdapter<>(getContext(), android.R.layout.select_dialog_singlechoice, itemSellerTitle.getListTitleDescription());
        option.setAdapter(autoCompleteAdapter);

        Button viewDialogDescriptionClose = (Button) v.findViewById(R.id.view_dialog_description_close);
        viewDialogDescriptionClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        Button viewDialogDescriptionSubmit = (Button) v.findViewById(R.id.view_dialog_description_submit);
        viewDialogDescriptionSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaceOnTitleBar.onTitleChange(itemSellerTitle.listTitleDescription.get(reportPicked).getId(), itemSellerTitle.listTitleDescription.get(reportPicked).getTitle());
                getDialog().dismiss();
            }
        });

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(outState == null) {
            outState = new Bundle();
        } else {

            //outState.putParcelable("interface", this.interfaceOnTitleBar);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
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
