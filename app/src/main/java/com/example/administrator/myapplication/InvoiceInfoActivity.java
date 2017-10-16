package com.example.administrator.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.zxing.integration.android.IntentIntegrator;

import org.parceler.Parcels;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import AppBar.ApplicationBar;
import AppBar.BarType;
import authen.AuthenData;
import datepicker.DatePickerFragment;
import fragment.FragmentToolbar;
import intent.IntentKeycode;
import intent.IntentParcel;
import invoice.BillPOJO;
import invoice.FragmentInvoiceDetail;
import invoice.InterfaceInvoiceInfo;
import invoice.InvoiceData;
import invoice.InvoicePOJO;
import invoice.ParcelQuery;
import invoice.item.ItemInvoice;
import invoice.item.ItemInvoicePreview;
import invoice.item.ParcelBill;
import invoice.item.ParcelInvoice;
import okhttp3.ResponseBody;
import retrofit.DataWrapper;
import retrofit.InterfaceListen;
import retrofit.RetrofitAbstract;
import retrofit.ServiceRetrofit;
import retrofit2.Retrofit;
import sqlite.DbHelper;
import toolbars.ToolbarOptions;

public class InvoiceInfoActivity extends AppCompatActivity {

	private FragmentTransaction fm = getSupportFragmentManager().beginTransaction();

	private FragmentInvoiceDetail fragmentInvoiceDetail = null;

	private Bundle b = new Bundle();

	@Override
	public void setContentView(@LayoutRes int layoutResID) {
		DrawerLayout fullLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.layout_main, null);
		FrameLayout frameLayout = (FrameLayout) fullLayout.findViewById(R.id.layout_content);
		getLayoutInflater().inflate(layoutResID, frameLayout, true);
		super.setContentView(fullLayout);
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_blank);

		Toolbar myToolbar = (Toolbar) findViewById(R.id.app_toolbar);
		setSupportActionBar(myToolbar);

		// Get a support ActionBar corresponding to this toolbar
		ActionBar ab = getSupportActionBar();

		// Enable the Up button
		ab.setDisplayHomeAsUpEnabled(true);

		// Remove title name
		ab.setDisplayShowTitleEnabled(false);

		ToolbarOptions callbacks = new ToolbarOptions() {
			@Override
			public void response(Bundle response) {

				if (response.containsKey(IntentKeycode.INTENT)) {
					// from parcelable --> setTo(...)
					String intentkeys = response.getString(IntentKeycode.INTENT);

					IntentParcel p = Parcels.unwrap(response.getParcelable(IntentKeycode.INTENT));

					Intent t = new Intent(InvoiceInfoActivity.this, p.getTo());
					startActivityForResult(t, IntentKeycode.RESULT_INVOICE_SEARCH);
				}
			}
		};

		Bundle dataCallbacks = new Bundle();
		IntentParcel p = new IntentParcel();
		p.setTo(AppliedSearchActivity.class);

		dataCallbacks.putParcelable(IntentKeycode.INTENT, Parcels.wrap(p));
		FragmentToolbar fToolbar = new FragmentToolbar("รายการใบสั่งสินค้า", callbacks, dataCallbacks);
		fm.replace(R.id.layout_toolbar, fToolbar);
		fm.commit();

		Intent t = getIntent();
		if(t.getExtras() == null) b = new Bundle();
		else b = t.getExtras();

		if(savedInstanceState == null) { async(); }
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	protected void async() {
		SharedPreferences sp = getSharedPreferences(MainActivity._PREF_MODE, Context.MODE_PRIVATE);
		if(this.b != null && sp != null) {
			String SHIP_NO = sp.getString(AuthenData.USERNAME, "");

			b.putString(InvoiceData.INVOICE_LIMIT, "0");
			b.putString(AuthenData.USERNAME, SHIP_NO);

			new ServiceRetrofit().callServer(interfaceListen, RetrofitAbstract.RETROFIT_PRE_INVOICE, b);
		} else {
			Log.e("Error", "Bundle is null or Unauthorized.");
		}
	}

	private InterfaceInvoiceInfo interfaceInvoiceInfo = new InterfaceInvoiceInfo() {
		@Override
		public void onOptionalChange(String optionIDs) {
			if(b != null) {
				b.putString(InvoiceData.INVOICE_DAY_TAG, optionIDs);
				async();
			} else {
				Log.e("Error", "Bundle is null.");
			}
		}

		@Override
		public void onBarcodeScan(Bundle clickeddata) {
			Intent t = new Intent(getApplication(), CustomScannerActivity.class);

			t.putExtras(clickeddata);

			b.putInt(InvoiceData.SHARED_PREFERENCES_BILL_POSITION,  clickeddata.getInt(InvoiceData.SHARED_PREFERENCES_BILL_POSITION));

			int BILL_COUNT = clickeddata.getInt(InvoiceData.BILL_COUNT);
			int TOTAL_BOX = clickeddata.getInt(InvoiceData.TOTAL_BOX);

			if(BILL_COUNT >= TOTAL_BOX) {
				// ...
				t = new Intent(InvoiceInfoActivity.this, CanvasActivity.class);
				startActivityForResult(t, IntentKeycode.RESULT_CANVAS);
			} else {
				startActivityForResult(t, IntentIntegrator.REQUEST_CODE);
			}
		}
	};

  	private final InterfaceListen interfaceListen = new InterfaceListen() {
		@Override
		public void onResponse(Object data, Retrofit retrofit) {

				ParcelBill pb = new ParcelBill();

				List<BillPOJO> pojoList = (List<BillPOJO>) data;

				BillPOJO onceBill = new BillPOJO();

				ArrayList<BillPOJO> list = new ArrayList<>();
				int countingRound = 0;

				for (BillPOJO i : pojoList) {
					BillPOJO temp = new BillPOJO();
					temp.setBILL_NO(i.getBILL_NO());
					temp.setBILL_DATE(i.getBILL_DATE());
					temp.setNET_AMOUNT(i.getNET_AMOUNT());
					temp.setTOTAL_BOX(i.getTOTAL_BOX());
					temp.setBILL_COUNT(i.getBILL_COUNT());

					if(countingRound == 0)
						onceBill = temp;

					countingRound++;

					list.add(temp);
				}

				ParcelQuery pq = Parcels.unwrap(b.getParcelable(InvoiceData.INVOICE_PARCEL_QUERY));

				if(pq.isIncreaseOne()) {

					pq.setIncreaseOne(false);

					b.putParcelable(InvoiceData.INVOICE_PARCEL_QUERY, Parcels.wrap(pq));

					int counting = Integer.parseInt(onceBill.getBILL_COUNT());

					if(counting >= Integer.parseInt(onceBill.getTOTAL_BOX())) {
						// Prepare to drawing name

						AlertDialog.Builder builder1 = new AlertDialog.Builder(InvoiceInfoActivity.this);
						builder1.setMessage("หมายเลขบิลล์นี้สแกนครบทุกกล่องแล้ว แตะเพื่อเซ็นต์ชื่อรับสินค้า");
						builder1.setCancelable(true);

						builder1.setNegativeButton(
								  "ปิด",
								  new DialogInterface.OnClickListener() {
									  public void onClick(DialogInterface dialog, int id) {
										  dialog.cancel();
									  }
								  });

						AlertDialog alert11 = builder1.create();
						alert11.show();

						pb.setListBill(list);
						b.putParcelable(InvoiceData.INVOICE_PARCEL_CONTENT, Parcels.wrap(pb));

						fm = getSupportFragmentManager().beginTransaction();

						fragmentInvoiceDetail = new FragmentInvoiceDetail(b, interfaceInvoiceInfo);
						fm.replace(R.id.blankFrameLayout, fragmentInvoiceDetail);
						fm.commit();

					} else {

						counting += 1;

						onceBill.setBILL_COUNT(counting + "");

						Bundle instanceBundle = new Bundle();
						instanceBundle.putParcelable(InvoiceData.BILL_POJO, Parcels.wrap(onceBill));

						final int finalCounting = counting;
						final BillPOJO finalOnceBill = onceBill;
						final InterfaceListen updateInterface = new InterfaceListen() {
							@Override
							public void onResponse(Object data, Retrofit retrofit) {
								// Set List in fragment update data XD
								DataWrapper message = (DataWrapper) data;

								async();


							/*DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									switch (which) {
										case DialogInterface.BUTTON_POSITIVE:
											//Yes button clicked
											Bundle bundle = new Bundl e();
											bundle.putString(InvoiceData.INVOICE_SCANNER_STRING, finalOnceBill.getBILL_NO());
											bundle.putInt(InvoiceData.BILL_COUNT, Integer.parseInt(finalOnceBill.getBILL_COUNT()));
											bundle.putInt(InvoiceData.TOTAL_BOX, Integer.parseInt(finalOnceBill.getTOTAL_BOX()));
											interfaceInvoiceInfo.onBarcodeScan(bundle);
											break;

										case DialogInterface.BUTTON_NEGATIVE:
											//No button clicked
											break;
									}
								}
							};*/

							/*AlertDialog.Builder builder = new AlertDialog.Builder(InvoiceInfoActivity.this);
							builder.setMessage("สแกนบิลล์นี้ต่อไปใช่ไหม ?").setPositiveButton("ตกลง", dialogClickListener)
									  .setNegativeButton("ยกเลิก", dialogClickListener).show();*/
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

						new ServiceRetrofit().callServer(updateInterface, RetrofitAbstract.RETROFIT_SET_BILL_COUNT, instanceBundle);
					}
				} else {
					Log.e("IncreaseOne", "+0");

					pb.setListBill(list);
					b.putParcelable(InvoiceData.INVOICE_PARCEL_CONTENT, Parcels.wrap(pb));

					fm = getSupportFragmentManager().beginTransaction();

					fragmentInvoiceDetail = new FragmentInvoiceDetail(b, interfaceInvoiceInfo);
					fm.replace(R.id.blankFrameLayout, fragmentInvoiceDetail);
					fm.commit();
				}
		}

		@Override
		public void onBodyError(ResponseBody responseBodyError) {}

		@Override
		public void onBodyErrorIsNull() {}

		@Override
		public void onFailure(Throwable t) {}
	};

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if(b != null) {
			b.putString(InvoiceData.INVOICE_LIMIT, "0");
			outState.putAll(b);
		}
		Log.e("onSaveInstanceState", "true");
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		Log.e("onRestoreInstanceState", "true");

		if(savedInstanceState != null) {
			b = savedInstanceState;

			fm = getSupportFragmentManager().beginTransaction();
			fragmentInvoiceDetail = new FragmentInvoiceDetail(b, interfaceInvoiceInfo);
			fm.replace(R.id.blankFrameLayout, fragmentInvoiceDetail);
			fm.commit();

		} else {
			Log.e ("onRestoreInstanceState", "savedInstanceState is null");
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		Log.e("onActivityResult", "Result Code : "+resultCode+" | Request Code : "+requestCode+ " |Result OK : "+RESULT_OK);
		if(resultCode == RESULT_OK) {
			Bundle temp = null;
			int position = -1;
			switch(requestCode) {
				case IntentKeycode.RESULT_INVOICE_SEARCH :
					temp = data.getExtras();
					if(temp.containsKey(InvoiceData.INVOICE_PARCEL_QUERY)) {
						b.putString(InvoiceData.INVOICE_LIMIT, "0");

						fragmentInvoiceDetail.setNewLimited();

						Log.e("SUCCESSFULLY", "WRAP BILL OR DATE FRPM APPLIED SEARCH ACTIVITY");

						// กำหนด บิลล์ และ วันที่ เรียบร้อยแล้ว
						ParcelQuery pq = Parcels.unwrap(temp.getParcelable(InvoiceData.INVOICE_PARCEL_QUERY));

						b.putParcelable(InvoiceData.INVOICE_PARCEL_QUERY, Parcels.wrap(pq));

						async();
					}
					break;
				case IntentIntegrator.REQUEST_CODE :
					temp = data.getExtras();
					final Bundle finalTemp = temp;

					// Below is set bill count ++ (in server and show message client)
					String result = data.getExtras().getString("SCAN_RESULT");

					position = -1;
					if(b.containsKey(InvoiceData.SHARED_PREFERENCES_BILL_POSITION)) {
						position = b.getInt(InvoiceData.SHARED_PREFERENCES_BILL_POSITION);
					} else {
						break;
					}

					if(fragmentInvoiceDetail.getBILLPOJO(position) == null) break;

					final BillPOJO pojo = fragmentInvoiceDetail.getBILLPOJO(position);
					final int finalposition = position;

					if(pojo.getBILL_NO().trim().equals(result.trim())) {
						int counting = Integer.parseInt(pojo.getBILL_COUNT());
						counting+= 1;

						// set ++
						pojo.setBILL_COUNT(counting+"");

						Bundle instanceBundle = new Bundle();
						instanceBundle.putParcelable(InvoiceData.BILL_POJO, Parcels.wrap(pojo));

						final int finalCounting = counting;
						final InterfaceListen updateInterface = new InterfaceListen() {
							@Override
							public void onResponse(Object data, Retrofit retrofit) {
								// Set List in fragment update data XD
								DataWrapper message = (DataWrapper) data;
								Log.e("MESSAGE", message.toString());

								fragmentInvoiceDetail.increaseCounting(finalposition, finalCounting);

								DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										switch (which) {
											case DialogInterface.BUTTON_POSITIVE:
												//Yes button clicked
												Bundle bundle = new Bundle();
												bundle.putString(InvoiceData.INVOICE_SCANNER_STRING, pojo.getBILL_NO());
												bundle.putInt(InvoiceData.BILL_COUNT, Integer.parseInt(pojo.getBILL_COUNT()));
												bundle.putInt(InvoiceData.TOTAL_BOX, Integer.parseInt(pojo.getTOTAL_BOX()));
												interfaceInvoiceInfo.onBarcodeScan(bundle);
												break;

											case DialogInterface.BUTTON_NEGATIVE:
												//No button clicked
												break;
										}
									}
								};

								AlertDialog.Builder builder = new AlertDialog.Builder(InvoiceInfoActivity.this);
								builder.setMessage("สแกนบิลล์นี้ต่อไปใช่ไหม ?").setPositiveButton("ตกลง", dialogClickListener)
										  .setNegativeButton("ยกเลิก", dialogClickListener).show();
							}

							@Override
							public void onBodyError(ResponseBody responseBodyError) {}

							@Override
							public void onBodyErrorIsNull() {}

							@Override
							public void onFailure(Throwable t) {}
						};

						new ServiceRetrofit().callServer(updateInterface, RetrofitAbstract.RETROFIT_SET_BILL_COUNT, instanceBundle);

						break;
					} else {

						AlertDialog.Builder builder1 = new AlertDialog.Builder(InvoiceInfoActivity.this);
						builder1.setMessage("หมายเลขบิลล์ข้างกล่อง ไม่ตรงกับหมายเลขบิลล์ในระบบ");
						builder1.setCancelable(true);

						builder1.setNegativeButton(
								  "ปิด",
								  new DialogInterface.OnClickListener() {
									  public void onClick(DialogInterface dialog, int id) {
										  dialog.cancel();
									  }
								  });

						AlertDialog alert11 = builder1.create();
						alert11.show();
					}
					break;
				case IntentKeycode.RESULT_CANVAS:

					position = -1;
					if(b.containsKey(InvoiceData.SHARED_PREFERENCES_BILL_POSITION)) {
						position = b.getInt(InvoiceData.SHARED_PREFERENCES_BILL_POSITION);
					} else {
						break;
					}

					final int removeposition = position;

					if(fragmentInvoiceDetail.getBILLPOJO(position) == null) break;

					final BillPOJO pojotemp = fragmentInvoiceDetail.getBILLPOJO(position);

					Bundle instanceBundle = data.getExtras();

					final int BILL_COUNT = Integer.parseInt(pojotemp.getBILL_COUNT());
					final String BILL_NO = pojotemp.getBILL_NO();

					final InterfaceListen setCompleteBillInterface = new InterfaceListen() {
						@Override
						public void onResponse(Object data, Retrofit retrofit) {
							// ... complete 1 bill -.-
							DataWrapper message = (DataWrapper) data;
							Log.e("MESSAGE", message.toString());

							// remove in position (fragment adapter's list)
							fragmentInvoiceDetail.removeByPosition(removeposition);

							// remove in bundle
							ParcelBill pb = Parcels.unwrap(b.getParcelable(InvoiceData.INVOICE_PARCEL_CONTENT));

							ArrayList<BillPOJO> instancelistpojo = pb.getListBill();
							int bundleindex = instancelistpojo.indexOf(pojotemp);

							if( bundleindex != -1 ) {
								 instancelistpojo.remove(bundleindex);
								 pb.setListBill(instancelistpojo);
								 b.putParcelable(InvoiceData.INVOICE_PARCEL_CONTENT, Parcels.wrap(pb));
							}

							AlertDialog.Builder builder = new AlertDialog.Builder(InvoiceInfoActivity.this);
							builder.setMessage("สแกนบิลล์ "+ BILL_NO + " เสร็จสิ้น");
							builder.setCancelable(true);

							builder.setNegativeButton(
									  "ปิด",
									  new DialogInterface.OnClickListener() {
										  public void onClick(DialogInterface dialog, int id) {
											  dialog.cancel();
										  }
									  });

							AlertDialog dialog = builder.create();
							dialog.show();
						}

						@Override
						public void onBodyError(ResponseBody responseBodyError) {}

						@Override
						public void onBodyErrorIsNull() {}

						@Override
						public void onFailure(Throwable t) {}
					};

					instanceBundle.putString(InvoiceData.BILL_NO, BILL_NO);
					instanceBundle.putString(InvoiceData.BILL_COUNT, BILL_COUNT+"");

					// required update that's complete bill
					new ServiceRetrofit().callServer(setCompleteBillInterface, RetrofitAbstract.RETROFIT_SET_COMPLETE_BILL, instanceBundle);
					break;
			}
		} else {
			Log.e("STATE", "PRESSED BACK");
		}
	}

	@Override
	public boolean onSupportNavigateUp() {
		onBackPressed();
		return true;
	}
}
