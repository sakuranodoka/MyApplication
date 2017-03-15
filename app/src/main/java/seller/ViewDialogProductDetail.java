package seller;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import seller.pojo.SellerBestSellerPOJO;
import seller.pojo.SellerCollectionPOJO;
import retrofit.InterfaceListen;
import seller.services.retrofit.ServiceCollection;

/**
 * Created by Administrator on 22/11/2559.
 */
public class ViewDialogProductDetail {

    private String itemCode;

    private Activity activity;

    private Dialog dialog;

    public void showDialog(Activity activity, final String itemCode) {

        this.activity = activity;

        this.itemCode = itemCode;

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

        LinearLayout content = (LinearLayout) dialog.findViewById(R.id.dialog_content);

        LinearLayout.LayoutParams llPm =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        float density = activity.getResources().getDisplayMetrics().density;
        int paddingDp = (int)(7 * density);

        LinearLayout llNr = new LinearLayout(activity);

        llNr.setLayoutParams(llPm);
        llNr.setOrientation(LinearLayout.HORIZONTAL);
        llNr.setPadding(paddingDp,paddingDp,paddingDp,paddingDp);
        llNr.setBackgroundColor(ContextCompat.getColor(activity, R.color.angel_white));
        llNr.setGravity(Gravity.CENTER_HORIZONTAL);

        ProgressBar progressBar = new ProgressBar(activity);
        llNr.addView(progressBar);
        content.addView(llNr);

        dialog.show();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new ServiceCollection().callServer(interfaceListen, SellerData.reportId, SellerData.shopCode, itemCode);
            }
        }, 1000);
    }

    InterfaceListen interfaceListen = new InterfaceListen() {

        @Override
        public void onResponse(Object data, Retrofit retrofit) {

            LinearLayout content = (LinearLayout) dialog.findViewById(R.id.dialog_content);

            content.removeAllViews();

            if(data instanceof SellerCollectionPOJO) {

                SellerCollectionPOJO temp = (SellerCollectionPOJO) data;

                if(temp != null && temp.getData() != null) {

                    List<SellerCollectionPOJO.Data> dataList = temp.getData();

                    for(SellerCollectionPOJO.Data each : dataList) {

                        LinearLayout llNr = new LinearLayout(activity);

                        LinearLayout.LayoutParams llPm =
                                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                        float density = activity.getResources().getDisplayMetrics().density;
                        int paddingDp = (int) (7 * density);

                        llNr.setLayoutParams(llPm);
                        llNr.setOrientation(LinearLayout.HORIZONTAL);
                        llNr.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
                        llNr.setBackgroundColor(ContextCompat.getColor(activity, R.color.angel_white));

                        TextView itemCodeTitle = new TextView(activity);
                        itemCodeTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        itemCodeTitle.setText("รหัสผลิตภัณฑ์ : ");
                        itemCodeTitle.setTextColor(ContextCompat.getColor(activity, R.color.supreme_blue));
                        itemCodeTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 21);
                        llNr.addView(itemCodeTitle);

                        TextView itemCodeBestSeller = new TextView(activity);
                        itemCodeBestSeller.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        itemCodeBestSeller.setText(each.getCollection());
                        itemCodeBestSeller.setTextColor(ContextCompat.getColor(activity, R.color.jet_black));
                        itemCodeBestSeller.setTextSize(TypedValue.COMPLEX_UNIT_SP, 21);
                        llNr.addView(itemCodeBestSeller);

                        content.addView(llNr);

                        llNr = new LinearLayout(activity);
                        llNr.setLayoutParams(llPm);
                        llNr.setOrientation(LinearLayout.HORIZONTAL);
                        llNr.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
                        llNr.setBackgroundColor(ContextCompat.getColor(activity, R.color.angel_white));

                        TextView balTitle = new TextView(activity);
                        balTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        balTitle.setText("BAL : ");
                        balTitle.setTextColor(ContextCompat.getColor(activity, R.color.dark_honest_green));
                        balTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        llNr.addView(balTitle);

                        TextView balCollection = new TextView(activity);
                        balCollection.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        balCollection.setText(each.getBAL());
                        balCollection.setTextColor(ContextCompat.getColor(activity, R.color.space_gray));
                        balCollection.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        llNr.addView(balCollection);

                        content.addView(llNr);

                        llNr = new LinearLayout(activity);
                        llNr.setLayoutParams(llPm);
                        llNr.setOrientation(LinearLayout.HORIZONTAL);
                        llNr.setPadding(paddingDp, paddingDp, 0, 0);
                        llNr.setBackgroundColor(ContextCompat.getColor(activity, R.color.little_light_gray));

                        content.addView(llNr);
                    }
                }


            } else if(data instanceof SellerBestSellerPOJO) {

                SellerBestSellerPOJO temp = (SellerBestSellerPOJO) data;

                if(temp != null && temp.getData() != null) {

                    List<SellerBestSellerPOJO.Data> dataList = temp.getData();

                    for(SellerBestSellerPOJO.Data each : dataList) {

                        LinearLayout llNr = new LinearLayout(activity);

                        LinearLayout.LayoutParams llPm =
                                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                        float density = activity.getResources().getDisplayMetrics().density;
                        int paddingDp = (int) (7 * density);

                        llNr.setLayoutParams(llPm);
                        llNr.setOrientation(LinearLayout.HORIZONTAL);
                        llNr.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
                        llNr.setBackgroundColor(ContextCompat.getColor(activity, R.color.angel_white));

                        TextView itemCodeTitle = new TextView(activity);
                        itemCodeTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        itemCodeTitle.setText("รหัสผลิตภัณฑ์ : ");
                        itemCodeTitle.setTextColor(ContextCompat.getColor(activity, R.color.supreme_blue));
                        itemCodeTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 21);
                        llNr.addView(itemCodeTitle);

                        TextView itemCodeBestSeller = new TextView(activity);
                        itemCodeBestSeller.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        itemCodeBestSeller.setText(each.getCollection());
                        itemCodeBestSeller.setTextColor(ContextCompat.getColor(activity, R.color.jet_black));
                        itemCodeBestSeller.setTextSize(TypedValue.COMPLEX_UNIT_SP, 21);
                        llNr.addView(itemCodeBestSeller);

                        content.addView(llNr);

                        llNr = new LinearLayout(activity);
                        llNr.setLayoutParams(llPm);
                        llNr.setOrientation(LinearLayout.HORIZONTAL);
                        llNr.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
                        llNr.setBackgroundColor(ContextCompat.getColor(activity, R.color.angel_white));

                        TextView netTitle = new TextView(activity);
                        netTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        netTitle.setText("ยอดขายทั้งหมด : ");
                        netTitle.setTextColor(ContextCompat.getColor(activity, R.color.dark_honest_green));
                        netTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        llNr.addView(netTitle);

                        TextView netBestSeller = new TextView(activity);
                        netBestSeller.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        netBestSeller.setText(each.getNet());
                        netBestSeller.setTextColor(ContextCompat.getColor(activity, R.color.space_gray));
                        netBestSeller.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        llNr.addView(netBestSeller);

                        TextView netCurrency = new TextView(activity);
                        netCurrency.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        netCurrency.setText(" ฿.");
                        netCurrency.setTextColor(ContextCompat.getColor(activity, R.color.dark_honest_green));
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
}
