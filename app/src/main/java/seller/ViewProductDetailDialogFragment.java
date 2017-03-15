package seller;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

import okhttp3.ResponseBody;
import retrofit.InterfaceListen;
import retrofit2.Retrofit;
import seller.pojo.SellerBestSellerPOJO;
import seller.pojo.SellerCollectionPOJO;
import seller.services.retrofit.ServiceCollection;

/**
 * Created by Administrator on 5/1/2560.
 */
public class ViewProductDetailDialogFragment extends DialogFragment {

    private InterfaceOnProductDetail setUpDialog = null;

    private int reportId = 0;
    private String collection = "";
    private String shopCode = "";

    @SuppressLint("ValidFragment")
    public ViewProductDetailDialogFragment(int reportId, String collection, String shopCode, InterfaceOnProductDetail setUpDialog) {
        this.setUpDialog = setUpDialog;
        this.reportId = reportId;
        this.collection = collection;
        this.shopCode = shopCode;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);

            LinearLayout content = (LinearLayout) dialog.findViewById(R.id.dialog_content);

            LinearLayout.LayoutParams llPm =
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            float density = getActivity().getResources().getDisplayMetrics().density;
            int paddingDp = (int)(7 * density);

            LinearLayout llNr = new LinearLayout(getActivity());

            llNr.setLayoutParams(llPm);
            llNr.setOrientation(LinearLayout.HORIZONTAL);
            llNr.setPadding(paddingDp,paddingDp,paddingDp,paddingDp);
            llNr.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.angel_white));
            llNr.setGravity(Gravity.CENTER_HORIZONTAL);

            ProgressBar progressBar = new ProgressBar(getActivity());
            llNr.addView(progressBar);
            content.addView(llNr);

        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(true);

        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    private View passView = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.dialog_seller_detail, container, false);

        passView = v;

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new ServiceCollection().callServer(interfaceListen, reportId, shopCode, collection);
            }
        }, 1000);

        ImageView backPressedState = (ImageView) v.findViewById(R.id.backPressedState);
        backPressedState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

//        final InstantAutocomplete option = (InstantAutocomplete) v.findViewById(R.id.option);
//
//        final ItemSellerTitle itemSellerTitle = new ItemSellerTitle();
//        itemSellerTitle.setListTitleDescription(SellerTitleBar.getSellerTitleList());
//
//        option.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                option.showDropDown();
//            }
//        });
//        option.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //interfaceTitleCallback.onChangeTitleCallBack(itemSellerTitle.listOptionValue.get(position).getId());
//                reportPicked = position;
//            }
//        });
//
//        ArrayAdapter<SellerTitleDAO> autoCompleteAdapter = new ArrayAdapter<>(getContext(), android.R.layout.select_dialog_singlechoice, itemSellerTitle.getListTitleDescription());
//        option.setAdapter(autoCompleteAdapter);
//
//        Button viewDialogDescriptionClose = (Button) v.findViewById(R.id.view_dialog_description_close);
//        viewDialogDescriptionClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getDialog().dismiss();
//            }
//        });
//
//        Button viewDialogDescriptionSubmit = (Button) v.findViewById(R.id.view_dialog_description_submit);
//        viewDialogDescriptionSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //interfaceTitleCallback.onChangeTitleCallBack(itemSellerTitle.listOptionValue.get(reportPicked).getId(), itemSellerTitle.listOptionValue.get(reportPicked).getTitle());
//                getDialog().dismiss();
//            }
//        });

        return v;
    }

    InterfaceListen interfaceListen = new InterfaceListen() {

        @Override
        public void onResponse(Object data, Retrofit retrofit) {

            LinearLayout content = (LinearLayout) passView.findViewById(R.id.dialog_content);

            content.removeAllViews();

            TextView dialogHeaderTextview = (TextView) passView.findViewById(R.id.dialog_header);

            if (data instanceof SellerCollectionPOJO) {

                dialogHeaderTextview.setText("รายละเอียดจำนวนสินค้าหน้าร้าน");

                SellerCollectionPOJO temp = (SellerCollectionPOJO) data;

                if (temp != null && temp.getData() != null) {

                    for (SellerCollectionPOJO.Data each : temp.getData()) {
                        LinearLayout llNr = new LinearLayout(getActivity());

                        LinearLayout.LayoutParams llPm =
                                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                        float density = getActivity().getResources().getDisplayMetrics().density;
                        int paddingDp = (int) (7 * density);

                        llNr.setLayoutParams(llPm);
                        llNr.setOrientation(LinearLayout.HORIZONTAL);
                        llNr.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
                        llNr.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.angel_white));

                        TextView itemCodeTitle = new TextView(getActivity());
                        itemCodeTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        itemCodeTitle.setText("");
                        itemCodeTitle.setTextColor(ContextCompat.getColor(getActivity(), R.color.supreme_blue));
                        itemCodeTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 21);
                        llNr.addView(itemCodeTitle);

                        TextView itemCodeBestSeller = new TextView(getActivity());
                        itemCodeBestSeller.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        itemCodeBestSeller.setText(each.getCollection());
                        itemCodeBestSeller.setTextColor(ContextCompat.getColor(getActivity(), R.color.jet_black));
                        itemCodeBestSeller.setTextSize(TypedValue.COMPLEX_UNIT_SP, 21);
                        llNr.addView(itemCodeBestSeller);

                        content.addView(llNr);

                        llNr = new LinearLayout(getActivity());
                        llNr.setLayoutParams(llPm);
                        llNr.setOrientation(LinearLayout.HORIZONTAL);
                        llNr.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
                        llNr.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.angel_white));

                        TextView netTitle = new TextView(getActivity());
                        netTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        netTitle.setText("จำนวนสินค้า ");
                        netTitle.setTextColor(ContextCompat.getColor(getActivity(), R.color.dark_honest_green));
                        netTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        llNr.addView(netTitle);

                        TextView netBestSeller = new TextView(getActivity());
                        netBestSeller.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        netBestSeller.setText(each.getBAL());
                        netBestSeller.setTextColor(ContextCompat.getColor(getActivity(), R.color.space_gray));
                        netBestSeller.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        llNr.addView(netBestSeller);

                        TextView netCurrency = new TextView(getActivity());
                        netCurrency.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        netCurrency.setText(" ชิ้น");
                        netCurrency.setTextColor(ContextCompat.getColor(getActivity(), R.color.dark_honest_green));
                        netCurrency.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        llNr.addView(netCurrency);

                        content.addView(llNr);
                    }
                }

            } else if (data instanceof SellerBestSellerPOJO) {

                SellerBestSellerPOJO temp = (SellerBestSellerPOJO) data;

                dialogHeaderTextview.setText("รายละเอียดยอดขายสินค้า");

                if (temp != null && temp.getData() != null) {

                    for (SellerBestSellerPOJO.Data each : temp.getData()) {
                        LinearLayout llNr = new LinearLayout(getActivity());

                        LinearLayout.LayoutParams llPm =
                                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                        float density = getActivity().getResources().getDisplayMetrics().density;
                        int paddingDp = (int) (7 * density);

                        llNr.setLayoutParams(llPm);
                        llNr.setOrientation(LinearLayout.HORIZONTAL);
                        llNr.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
                        llNr.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.angel_white));

                        TextView itemCodeTitle = new TextView(getActivity());
                        itemCodeTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        itemCodeTitle.setText("");
                        itemCodeTitle.setTextColor(ContextCompat.getColor(getActivity(), R.color.supreme_blue));
                        itemCodeTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 21);
                        llNr.addView(itemCodeTitle);

                        TextView itemCodeBestSeller = new TextView(getActivity());
                        itemCodeBestSeller.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        itemCodeBestSeller.setText(each.getCollection());
                        itemCodeBestSeller.setTextColor(ContextCompat.getColor(getActivity(), R.color.jet_black));
                        itemCodeBestSeller.setTextSize(TypedValue.COMPLEX_UNIT_SP, 21);
                        llNr.addView(itemCodeBestSeller);

                        content.addView(llNr);

                        llNr = new LinearLayout(getActivity());
                        llNr.setLayoutParams(llPm);
                        llNr.setOrientation(LinearLayout.HORIZONTAL);
                        llNr.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
                        llNr.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.angel_white));

                        TextView netTitle = new TextView(getActivity());
                        netTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        netTitle.setText("ยอดขายสุทธิ ");
                        netTitle.setTextColor(ContextCompat.getColor(getActivity(), R.color.dark_honest_green));
                        netTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        llNr.addView(netTitle);

                        TextView netBestSeller = new TextView(getActivity());
                        netBestSeller.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        netBestSeller.setText(each.getNet());
                        netBestSeller.setTextColor(ContextCompat.getColor(getActivity(), R.color.space_gray));
                        netBestSeller.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        llNr.addView(netBestSeller);

                        TextView netCurrency = new TextView(getActivity());
                        netCurrency.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        netCurrency.setText(" บาท");
                        netCurrency.setTextColor(ContextCompat.getColor(getActivity(), R.color.dark_honest_green));
                        netCurrency.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        llNr.addView(netCurrency);

                        content.addView(llNr);
                    }
                }
            }
        }

        @Override
        public void onBodyError(ResponseBody responseBodyError) {

        }

        @Override
        public void onBodyErrorIsNull() {

        }

        @Override
        public void onFailure(Throwable t) {

        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.setUpDialog.onProductDetailDestroy();
    }
}