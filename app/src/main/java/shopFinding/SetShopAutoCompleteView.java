package shopFinding;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.SellerActivity;

import java.util.List;

import URL.ServiceURL;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import seller.SellerData;
import seller.ViewReportDialogFragment;
import seller.titlebar.InterfaceOnTitleBar;

/**
 * Created by Administrator on 17/11/2559.
 */
public class SetShopAutoCompleteView {

    private boolean showing = false;

    public static String language = "[{\"shop_id\":\"11079252\",\"shop_name\":\"พาราก้อน A1\"},{\"shop_id\":\"11079259\",\"shop_name\":\"สยามสแควร์ B3\"}]";

    private AutoCompleteTextView acTextView;

    public SetShopAutoCompleteView() { acTextView = null; }

    public void setView(final AppCompatActivity activity, final boolean autoShowKeyboard) {

        acTextView = (AutoCompleteTextView) activity.findViewById(R.id.input_shop_searching);

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServiceURL.PROCUCT_BASE_URL )
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        InterfaceGetShop interfaceCollection = retrofit.create( InterfaceGetShop.class );
        Observable<List<ShopDetailPOJO>> observable = interfaceCollection.getShopDetail();

        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<Object>() {
                @Override
                public void onCompleted() {}

                @Override
                public void onError(Throwable e) {
                    Log.e("error", e.getMessage().toString());
                }

                @Override
                public void onNext(Object data) {

                    if (data != null) {

                        List<ShopDetailPOJO> temp = (List<ShopDetailPOJO>) data;

                        Display display = ((WindowManager) activity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

                        if(display.getOrientation() == Surface.ROTATION_0 || display.getOrientation() == Surface.ROTATION_180) {
                            acTextView.setFocusable(true);
                            acTextView.setFocusableInTouchMode(true);
                        } else {
                            acTextView.setFocusable(false);
                            acTextView.setFocusableInTouchMode(false);
                        }

                        final ShopArrayAdapter autoCompleteAdapter = new ShopArrayAdapter(activity, R.layout.view_search_paging, temp);

                        acTextView.setLongClickable(false);

                        acTextView.setDropDownWidth(Resources.getSystem().getDisplayMetrics().widthPixels);

                        acTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(showing) {

                                    acTextView.showDropDown();
                                } else {
                                    acTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_clear_black_24dp, 0);

                                    acTextView.showDropDown();

                                    setShowing();
                                }
                            }
                        });

                        acTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if(hasFocus) {
                                    if (showing) {

                                    } else {
                                        acTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_clear_black_24dp, 0);

                                        acTextView.showDropDown();

                                        setShowing();
                                    }
                                }
                            }
                        });

                        acTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                // ควร Callback กลับไปที่ Activity หลัก ไม่ใช่มาทำในนี้ แก้โค้ดบานบอกเลย
                                acTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_search_black_24dp, 0);

                                acTextView.clearFocus();

                                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(activity.getWindow().getCurrentFocus().getWindowToken(), 0);

                                Log.e("autocomplete status", "an item picked and hide dropdown");
                                setShowing();

                                // ตั้งค่าให้เลือก Report ทันที หลังจากเลือกร้านค้า
                                // setFragment(activity);

                                // Call Back to Seller Activity
                                // คอลแบ็คกลับไปทำงานที่ Seller Activity ~
                                if( activity != null) {
                                    InterfaceOnShop interfaceOnShop = (InterfaceOnShop) activity;
                                    interfaceOnShop.shopSelected(acTextView.getText().toString());
                                }
                            }
                        });

                        acTextView.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                final int DRAWABLE_LEFT = 0;
                                final int DRAWABLE_TOP = 1;
                                final int DRAWABLE_RIGHT = 2;
                                final int DRAWABLE_BOTTOM = 3;

                                if(event.getAction() == MotionEvent.ACTION_UP) {

                                    // touch right icon ...
                                    if(event.getRawX() >= (acTextView.getRight() - acTextView.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                                        Log.e("autocomplete status", "right icon touch.");

                                        if(showing) {
                                            // dropdown is already shown.
                                            if(acTextView.getText().toString().equals("")) {
                                                acTextView.clearFocus();

                                                acTextView.dismissDropDown();

                                                setShowing();
                                                Log.e("autocomplete status", "e - hide dropdown");

                                                acTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_search_black_24dp, 0);

                                                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                                                imm.hideSoftInputFromWindow(activity.getWindow().getCurrentFocus().getWindowToken(), 0);

                                            } else {
                                                acTextView.setText("");

                                                acTextView.requestFocus();

                                                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                                                imm.showSoftInput(acTextView, InputMethodManager.SHOW_IMPLICIT);

                                                acTextView.showDropDown();
                                            }

                                        } else {
                                            // all listed dropdown is hidden.

                                            // start showing now.
                                            acTextView.showDropDown();

                                            setShowing();
                                            Log.e("autocomplete status", "e - show dropdown");

                                            acTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_clear_black_24dp, 0);
                                        }
                                        return true;
                                    }
                                }

                                return false;
                            }
                        });

                        acTextView.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                                String SC = s.toString();

                                if(SC.split(":").length > 1 ) {
                                    //SellerData.shopCode = SC.split(":")[0].trim();

                                    //InterfaceOnShop interfaceOnShop = (InterfaceOnShop) activity;
                                    //interfaceOnShop.shopSelected(acTextView.getText().toString());
                                }
                            }
                        });

                        acTextView.setAdapter(autoCompleteAdapter);

                        if (autoShowKeyboard) {
                            acTextView.showDropDown();
                            acTextView.requestFocus();
                        }

                    }
                }
            });
    }

    private void setShowing() {
        if(this.showing) {
            this.showing = false;
        } else {
            this.showing = true;
        }
    }

    private void setFragment(AppCompatActivity activity) {
        ViewReportDialogFragment newFragment = new ViewReportDialogFragment((InterfaceOnTitleBar) activity);
        newFragment.show( activity.getSupportFragmentManager(), "Open Dialog");
    }
}
