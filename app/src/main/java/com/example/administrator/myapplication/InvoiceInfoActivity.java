package com.example.administrator.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.FrameLayout;

import com.google.zxing.integration.android.IntentIntegrator;
import com.squareup.otto.Subscribe;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import authen.AuthenData;
import fragment.FragmentToolbar;
import intent.IntentKeycode;
import intent.IntentParcel;
import invoice.BarcodeWrapper;
import invoice.BillPOJO;
import invoice.FragmentInvoiceDetail;
import invoice.InterfaceInvoiceInfo;
import invoice.InvoiceData;
import invoice.LimitWrapper;
import invoice.ParcelQuery;
import invoice.item.ParcelBill;
import invoice.AsynchronousWrapper;
import object.Clone;
import okhttp3.ResponseBody;
import retrofit.DataWrapper;
import retrofit.InterfaceListen;
import retrofit.RetrofitAbstract;
import retrofit.ServiceRetrofit;
import retrofit2.Retrofit;
import toolbars.ToolbarOptions;
import user.LocationAppData;

public class InvoiceInfoActivity extends AppCompatActivity {

	private FragmentTransaction fm = getSupportFragmentManager().beginTransaction();

	private FragmentInvoiceDetail fragmentInvoiceDetail = null;

	private Bundle b = new Bundle();

	private Bundle originalBundle = new Bundle();

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

		/*Intent t = getIntent();
		if(t.getExtras() == null) b = new Bundle();
		else b = t.getExtras();*/

		BusProvider.getInstance().register(this);

		if (savedInstanceState == null) {
			 //async();
			 asynchronous(RetrofitAbstract.RETROFIT_PRE_INVOICE, null);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	protected void async() {
		SharedPreferences sp = getSharedPreferences(MainActivity._PREF_MODE, Context.MODE_PRIVATE);
		if (this.b != null && sp != null) {

			 String SHIP_NO = sp.getString(AuthenData.USERNAME, "");

			 b.putString(InvoiceData.INVOICE_LIMIT, "0");
			 b.putString(AuthenData.USERNAME, SHIP_NO);

			 new ServiceRetrofit().callServer(interfaceListen, RetrofitAbstract.RETROFIT_PRE_INVOICE, b);
		} else {
			Log.e("Error", "Bundle is null or Unauthorized.");
		}
	}

	@Subscribe
	public void limitedOverwrite(LimitWrapper wrapper) {

		if( this.originalBundle == null) this.originalBundle = new Bundle();

		this.originalBundle.putString(InvoiceData.INVOICE_LIMIT, wrapper.getLimit()+"");
	}

	@Subscribe
	public void requestAsynchronous(AsynchronousWrapper wrapper) {
		if (wrapper.isRequired()) {
			 asynchronous(wrapper.getRetrofitAbstractLayer(), wrapper.getInstanceBundle());
		}
	}

	protected void asynchronous(int RetrofitAbstractLayer, Bundle instanceBundle) {

		try {

			SharedPreferences sp = getSharedPreferences(MainActivity._PREF_MODE, Context.MODE_PRIVATE);

			if (sp == null) throw new Exception("SharedPreferences (SHIP_NO) is null");

			if (instanceBundle == null) instanceBundle = this.originalBundle;

			String SHIP_NO = sp.getString(AuthenData.USERNAME, "");

			instanceBundle.putString(AuthenData.USERNAME, SHIP_NO);

			if (!instanceBundle.containsKey(InvoiceData.INVOICE_LIMIT))
				 instanceBundle.putString(InvoiceData.INVOICE_LIMIT, "0");

			Log.e("LIMIT", instanceBundle.getString(InvoiceData.INVOICE_LIMIT));

			new ServiceRetrofit().callServer(this.callback, RetrofitAbstractLayer, instanceBundle);

		} catch (Exception err) {
			Log.e("Fatal Error", err.getMessage());
		}
	}

	private InterfaceListen callback = new InterfaceListen() {
		@Override
		public void onResponse(Object data, Retrofit retrofit) {
			 // Validate callback data
			 if (data instanceof ArrayList && ((ArrayList) data).size() > 0 && (((ArrayList) data).get(0)) instanceof BillPOJO) {
				  ParcelBill pb = new ParcelBill();
				  pb.setListBill((ArrayList<BillPOJO>) data);

				  fm = getSupportFragmentManager().beginTransaction();
				  if (fragmentInvoiceDetail == null) {
					   fragmentInvoiceDetail = new FragmentInvoiceDetail(pb);
					   fm.replace(R.id.blankFrameLayout, fragmentInvoiceDetail);
					   fm.commit();
				 } else {
					   fragmentInvoiceDetail.setData(pb);
				 }
			 } else if (data instanceof DataWrapper && ((DataWrapper) data).getStatus().equals("update")) {
				 if (fragmentInvoiceDetail != null) {
					  final int position = originalBundle.getInt(InvoiceData.SHARED_PREFERENCES_BILL_POSITION);
					  fragmentInvoiceDetail.increaseCounting(position);

					  String dialogHeader = "";
					  int dialogCase = 1;

					  final BillPOJO pojo = fragmentInvoiceDetail.getBILLPOJO(position);
					  if (pojo.getBILL_COUNT().equals(pojo.getTOTAL_BOX())) {
						   // สแกนครบแล้ว เซ็นชื่อได้เลย
						   dialogHeader = "สแกนบิลล์นี้ครบทุกกล่องแล้ว ต้องการจะเซ็นต์ชื่อรับสินค้าหรือไม่";
						   dialogCase = 1;
					  } else {
						   // ถามว่าจะสแกนต่อใช่ไหม ? (สแกนยังไม่ครบ)
						   dialogHeader = "สแกนบิลล์ "+pojo.getBILL_NO()+" ต่อไปใช่ไหม ?";
						   dialogCase = 2;
					  }

					  final int finalDialogCase = dialogCase;
					  DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
						   @Override
						   public void onClick(DialogInterface dialog, int which) {
							   switch (which) {
								   case DialogInterface.BUTTON_POSITIVE :

									  if (finalDialogCase == 1) {
										  Intent t = new Intent(InvoiceInfoActivity.this, CanvasActivity.class);
										  startActivityForResult(t, IntentKeycode.RESULT_CANVAS);
									  } else if (finalDialogCase == 2) {
										  BarcodeWrapper wrapper = new BarcodeWrapper();
										  wrapper.setBillPOJO(pojo);
										  wrapper.setPosition(position);

										  onBarCodeScan(wrapper);
									  }
									  break;
								   case DialogInterface.BUTTON_NEGATIVE :
									  //No button clicked
									  break;
							   }
						   }
				      };

				      AlertDialog.Builder builder = new AlertDialog.Builder(InvoiceInfoActivity.this);
				      builder.setMessage(dialogHeader).setPositiveButton("ตกลง", dialogClickListener)
							    .setNegativeButton("ยกเลิก", dialogClickListener).show();

				 } else {
					  Log.e("Fatal Error", "Fragment invoice detail is null.");
				 }
			 } else if (data instanceof DataWrapper && ((DataWrapper) data).getStatus().equals("complete")) {
				 if (fragmentInvoiceDetail != null) {
					  int position = originalBundle.getInt(InvoiceData.SHARED_PREFERENCES_BILL_POSITION);
					  fragmentInvoiceDetail.removeByPosition(position);
				 }
			 } else {
				 Log.e("Fatal Error", "No remaining services.");
			 }
		}

		@Override
		public void onBodyError(ResponseBody responseBodyError) {}

		@Override
		public void onBodyErrorIsNull() {}

		@Override
		public void onFailure(Throwable t) {}
	};

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

					 if (countingRound == 0)
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
		if (this.originalBundle != null) {
			 //this.originalBundle.putString(InvoiceData.INVOICE_LIMIT, "0");

			 ParcelBill pb = fragmentInvoiceDetail.getParcelBill();
			 this.originalBundle.putParcelable(InvoiceData.BILL_PARCEL, Parcels.wrap(pb));

			 outState.putAll(this.originalBundle);
		}
		Log.e("onSaveInstanceState", "true");
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		Log.e("onRestoreInstanceState", "true");

		if (savedInstanceState != null) {

			 this.originalBundle = savedInstanceState;

			 //this.originalBundle.putString(InvoiceData.INVOICE_LIMIT, "0");

			 ParcelBill pb = Parcels.unwrap(this.originalBundle.getParcelable(InvoiceData.BILL_PARCEL));

			 int PRE_RESTORE_LIMIT = Integer.parseInt(this.originalBundle.getString(InvoiceData.INVOICE_LIMIT));

			 fm = getSupportFragmentManager().beginTransaction();

			 // Now fragmentInvoiceDetail == null
			 fragmentInvoiceDetail = new FragmentInvoiceDetail(pb);
			 fm.replace(R.id.blankFrameLayout, fragmentInvoiceDetail);
			 fm.commit();

			 fragmentInvoiceDetail.fixedLimited(PRE_RESTORE_LIMIT);

			 if (this.originalBundle.containsKey(InvoiceData.SHARED_PREFERENCES_BILL_POSITION)) {
				  int position = this.originalBundle.getInt(InvoiceData.SHARED_PREFERENCES_BILL_POSITION);
				  Log.e("MOVE_TO_POSITION", position+"");
				  fragmentInvoiceDetail.goToPosition(position);
			 }

			 // fragmentInvoiceDetail.fixedLimited(Integer.parseInt(this.originalBundle.getString(InvoiceData.INVOICE_LIMIT)));

			 //asynchronous(RetrofitAbstract.RETROFIT_PRE_INVOICE, this.originalBundle);

			 /*fm = getSupportFragmentManager().beginTransaction();
			 fragmentInvoiceDetail = new FragmentInvoiceDetail(b, interfaceInvoiceInfo);
			 fm.replace(R.id.blankFrameLayout, fragmentInvoiceDetail);
			 fm.commit();*/

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

					/*if (b.containsKey(InvoiceData.SHARED_PREFERENCES_BILL_POSITION)) {
						 position = b.getInt(InvoiceData.SHARED_PREFERENCES_BILL_POSITION);
					} else {
						 break;
					}*/
					Bundle instanceBundle = new Bundle();
					if (this.originalBundle.containsKey(InvoiceData.BarcodeWrapper)) {
						 instanceBundle = this.originalBundle.getBundle(InvoiceData.BarcodeWrapper);
					} else break;

					position = instanceBundle.getInt(InvoiceData.SHARED_PREFERENCES_BILL_POSITION);

					// Set to main original bundle
					// Important
					this.originalBundle.putInt(InvoiceData.SHARED_PREFERENCES_BILL_POSITION, position);

					if (fragmentInvoiceDetail == null) {
						 Log.e("Fatal Error", "III Fragment is null");
						 break;
					}

					//if(fragmentInvoiceDetail.getBILLPOJO(position) == null) break;

					BillPOJO pojo = fragmentInvoiceDetail.getBILLPOJO(position);

					if (pojo.getBILL_NO().trim().equals(result.trim())) {
						 int counting = Integer.parseInt(pojo.getBILL_COUNT());
						 counting+= 1;

						 Clone cn = new Clone();

						 final BillPOJO cloned = (BillPOJO) cn.cloneObject(pojo);

						 // counting please
						 // count
						 // c /c.scan() => ?
						 // why set this pojo then adapter's data changed ?
						 // Oh I know that, you need to clone before change it
						 cloned.setBILL_COUNT(counting + "");

						 Bundle tempBundle = new Bundle();
						 tempBundle.putParcelable(InvoiceData.BILL_POJO, Parcels.wrap(cloned));

						 asynchronous(RetrofitAbstract.RETROFIT_SET_BILL_COUNT, tempBundle);

						 // set ++
						 /*pojo.setBILL_COUNT(counting+"");

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

						break;*/
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

					instanceBundle = data.getExtras();

					if (this.originalBundle.containsKey(InvoiceData.BarcodeWrapper)) {
						 position = this.originalBundle.getBundle(InvoiceData.BarcodeWrapper).getInt(InvoiceData.SHARED_PREFERENCES_BILL_POSITION);
					} else break;

					// Set to main original bundle
					// Important
					this.originalBundle.putInt(InvoiceData.SHARED_PREFERENCES_BILL_POSITION, position);

					if (fragmentInvoiceDetail == null) {
						 Log.e("Fatal Error", "III Fragment is null");
						 break;
					}

					pojo = fragmentInvoiceDetail.getBILLPOJO(position);

					Clone cn = new Clone();

					BillPOJO Cloned = (BillPOJO) cn.cloneObject(pojo);

					instanceBundle.putString(InvoiceData.BILL_NO, Cloned.getBILL_NO());
					instanceBundle.putString(InvoiceData.BILL_COUNT, Cloned.BILL_COUNT);

					// Put instance of Lat Lng
					if (this.originalBundle.containsKey(InvoiceData.LATITUDE)) {
						 instanceBundle.putString(InvoiceData.LATITUDE, this.originalBundle.getString(InvoiceData.LATITUDE));
					} else { instanceBundle.putString(InvoiceData.LATITUDE, "0.00"); }

					if (this.originalBundle.containsKey(InvoiceData.LONGITUDE)) {
						 instanceBundle.putString(InvoiceData.LONGITUDE, this.originalBundle.getString(InvoiceData.LONGITUDE));
					} else { instanceBundle.putString(InvoiceData.LONGITUDE, "0.00"); }

					// required update that's complete bill
					asynchronous(RetrofitAbstract.RETROFIT_SET_COMPLETE_BILL, instanceBundle);
					break;
/*
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

					// Put instance of Lat Lng
					if (b.containsKey(InvoiceData.LATITUDE)) {
						 instanceBundle.putString(InvoiceData.LATITUDE, b.getString(InvoiceData.LATITUDE));
					} else { instanceBundle.putString(InvoiceData.LATITUDE, "0.00"); }

					if (b.containsKey(InvoiceData.LONGITUDE)) {
						 instanceBundle.putString(InvoiceData.LONGITUDE, b.getString(InvoiceData.LONGITUDE));
					} else { instanceBundle.putString(InvoiceData.LONGITUDE, "0.00"); }

					// required update that's complete bill
					new ServiceRetrofit().callServer(setCompleteBillInterface, RetrofitAbstract.RETROFIT_SET_COMPLETE_BILL, instanceBundle);
					break;*/
			}
		} else {
			Log.e("STATE", "PRESSED BACK");
		}
	}

	@Subscribe
	public void getLocation(LocationAppData lad) {
		if (this.originalBundle != null) {
			 this.originalBundle.putString(InvoiceData.LATITUDE, lad.getLatitute());
			 this.originalBundle.putString(InvoiceData.LONGITUDE, lad.getLongitute());
		} else {
			 Log.e("Error", "Fatal Error : Original bundle iS null");
		}
	}

	@Subscribe
	public void onBarCodeScan(BarcodeWrapper wrapper) {
		Intent t = new Intent(getApplication(), CustomScannerActivity.class);

		Bundle instanceBundle = new Bundle();
		instanceBundle.putString(InvoiceData.INVOICE_SCANNER_STRING, wrapper.getBillPOJO().getBILL_NO());
		instanceBundle.putInt(InvoiceData.BILL_COUNT, Integer.parseInt(wrapper.getBillPOJO().getBILL_COUNT()));
		instanceBundle.putInt(InvoiceData.TOTAL_BOX, Integer.parseInt(wrapper.getBillPOJO().getTOTAL_BOX()));
		instanceBundle.putInt(InvoiceData.SHARED_PREFERENCES_BILL_POSITION, wrapper.getPosition());

		t.putExtras(instanceBundle);

		// This is no way to fixed it.
		// I spend so much for best practice. But everyone know no one is the best.
		// Just do it. Learn and alive.
		this.originalBundle.putBundle(InvoiceData.BarcodeWrapper, instanceBundle);

		//b.putInt(InvoiceData.SHARED_PREFERENCES_BILL_POSITION,  clickeddata.getInt(InvoiceData.SHARED_PREFERENCES_BILL_POSITION));

		int BILL_COUNT = instanceBundle.getInt(InvoiceData.BILL_COUNT);
		int TOTAL_BOX = instanceBundle.getInt(InvoiceData.TOTAL_BOX);

		if (BILL_COUNT >= TOTAL_BOX) {
			// ...
			t = new Intent(InvoiceInfoActivity.this, CanvasActivity.class);
			startActivityForResult(t, IntentKeycode.RESULT_CANVAS);
		} else {
			startActivityForResult(t, IntentIntegrator.REQUEST_CODE);
		}
	}

	@Override
	public boolean onSupportNavigateUp() {
		onBackPressed();
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		BusProvider.getInstance().unregister(this);
	}
}
