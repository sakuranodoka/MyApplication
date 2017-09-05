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

	// Inner Sqlite
	private DbHelper dbHelper;
	private SQLiteDatabase sqlite;
	private Cursor cursor;

	// Shared Preferences
	private SharedPreferences sp;

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

		sp = getSharedPreferences(MainActivity._PREF_MODE, Context.MODE_PRIVATE);

		Intent t = getIntent();
		if(t.getExtras() == null) b = new Bundle();
		else b = t.getExtras();

		if(b != null && b.containsKey(InvoiceData.INVOICE_INFO_TAG)) {
			if(b.getInt(InvoiceData.INVOICE_INFO_TAG) == InvoiceData.INVOICE_INFO_INNER_APP) {
				// Last version
				/*
				// ลบเพื่อจะได้ไม่ซ้ำซ้อนใน Fragment
				b.remove(InvoiceData.INVOICE_PARCEL_CONTENT);

				Bundle temp = (Bundle) b.clone();

				String username = "";
				if(sp != null && !sp.getString(AuthenData.USERNAME, "").equals(""))
					username = sp.getString(AuthenData.USERNAME, "");

				dbHelper = new DbHelper(this);
				sqlite = dbHelper.getWritableDatabase();
				//cursor = sqlite.rawQuery("SELECT " + dbHelper.COL_INVOICE + "," + dbHelper.COL_TIME +" FROM " + dbHelper.TABLE_NAME, null);
				cursor = sqlite.query(dbHelper.TABLE_NAME,  new String[] { dbHelper.COL_INVOICE,dbHelper.COL_TIME,dbHelper.COL_LATITUDE,dbHelper.COL_LONGITUDE }, dbHelper.COL_EMPLOYEE + "=?", new String[] { username }, null, null, null);

				cursor.moveToFirst();

				ParcelInvoice pPpi = new ParcelInvoice();
				ArrayList<ItemInvoicePreview> list = new ArrayList<>();
				while(!cursor.isAfterLast()) {
					ItemInvoicePreview item = new ItemInvoicePreview();
					item.setInvoicePreview(cursor.getString(cursor.getColumnIndex(dbHelper.COL_INVOICE)));
					item.setInvoiceDate(cursor.getString(cursor.getColumnIndex(dbHelper.COL_TIME)));
					list.add(item);
					cursor.moveToNext();
				}
				pPpi.setListInvoice(list);
				Parcelable wrapped = Parcels.wrap(pPpi);

				temp.putParcelable(InvoiceData.INVOICE_PARCEL, wrapped);

				FragmentInvoiceDetail fragmentInvoiceDetail = new FragmentInvoiceDetail(temp, interfaceInvoiceInfo);
				fm = getSupportFragmentManager().beginTransaction();
				fm.replace(R.id.blankFrameLayout, fragmentInvoiceDetail);
				fm.commit();*/
			} else if(b.getInt(InvoiceData.INVOICE_INFO_TAG) == InvoiceData.INVOICE_INFO_WITH_USER_ID) {

				// Set limited
				/*b.putString(InvoiceData.INVOICE_LIMIT, "0");

				ParcelQuery pq = new ParcelQuery();
				pq.setBill("");
				pq.setDatetime("");
				b.putParcelable(InvoiceData.INVOICE_PARCEL_QUERY, Parcels.wrap(pq));

				async();*/
			}

			if(savedInstanceState == null) {
				async();
			}
		}
	}

	protected void async() {
		if(this.b != null) {
			new ServiceRetrofit().callServer(interfaceListen, RetrofitAbstract.RETROFIT_PRE_INVOICE, b);
		}
	}

	private InterfaceInvoiceInfo interfaceInvoiceInfo = new InterfaceInvoiceInfo() {
		@Override
		public void onOptionalChange(String optionIDs) {
			if(b != null) {
				b.putString(InvoiceData.INVOICE_DAY_TAG, optionIDs);
				async();
			} else {
				Log.e("error", "bundle is null.");
			}
		}

		@Override
		public void onBarcodeScan(Bundle clickeddata) {
			Intent t = new Intent(getApplication(), CustomScannerActivity.class);
			Bundle zxingBn = new Bundle();
			//zxingBn.putInt(InvoiceData.INVOICE_CASE, InvoiceData.INVOICE_CASE_INVOICE_PREVIEW);
			//zxingBn.putString(InvoiceData.INVOICE_SCANNER_STRING, "1234eeeee");
			//t.putExtras(zxingBn);
			t.putExtras(clickeddata);

			startActivityForResult(t, IntentIntegrator.REQUEST_CODE);
		}
	};

  	private final InterfaceListen interfaceListen = new InterfaceListen() {
		@Override
		public void onResponse(Object data, Retrofit retrofit) {
				//ParcelInvoice pi = new ParcelInvoice();
				ParcelBill pb = new ParcelBill();

				List<BillPOJO> pojoList = (List<BillPOJO>) data;

				//ArrayList<ItemInvoicePreview> listInvoice = new ArrayList<>();
				ArrayList<BillPOJO> list = new ArrayList<>();
				for (BillPOJO i : pojoList) {
					BillPOJO temp = new BillPOJO();
					temp.setBILL_NO(i.getBILL_NO());
					temp.setBILL_DATE(i.getBILL_DATE());
					temp.setNET_AMOUNT(i.getNET_AMOUNT());
					temp.setTOTAL_BOX(i.getTOTAL_BOX());
					/*temp.setInvoicePreview(i.getInfoInvoice());
					temp.setInvoiceSublocality(i.getInfoSubLocality());
					temp.setInvoiceLocality(i.getInfoLocality());
					temp.setInvoiceDate(i.getInfoTime());*/
					list.add(temp);
				}
				//pi.setListInvoice(listInvoice);
				pb.setListBill(list);
				b.putParcelable(InvoiceData.INVOICE_PARCEL_CONTENT, Parcels.wrap(pb));

				fm = getSupportFragmentManager().beginTransaction();

				fragmentInvoiceDetail = new FragmentInvoiceDetail(b, interfaceInvoiceInfo);
				fragmentInvoiceDetail.clearance();
				fm.replace(R.id.blankFrameLayout, fragmentInvoiceDetail);
				fm.commit();
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
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		if(savedInstanceState != null) {
			b = savedInstanceState;

			async();

			if(b != null) {
//				for (String key: b.keySet())
//				{
//					Log.e ("myApplication", key + " is a key in the bundle");
//				}
				//async();
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		Log.e("CODE", "Result Code : "+resultCode+" | Request Code : "+requestCode+ " |Result OK : "+RESULT_OK);
		if(resultCode == RESULT_OK) {
			Bundle temp = null;
			switch(requestCode) {
				case IntentKeycode.RESULT_INVOICE_SEARCH :
					temp = data.getExtras();
					if(temp.containsKey(InvoiceData.INVOICE_PARCEL_QUERY)) {
						b.putString(InvoiceData.INVOICE_LIMIT, "0");

						Log.e("SUCCESSFULLY", "WRAP BILL OR DATE FRPM APPLIED SEARCH ACTIVITY");

						// กำหนด บิลล์ และ วันที่ เรียบร้อยแล้ว
						ParcelQuery pq = Parcels.unwrap(temp.getParcelable(InvoiceData.INVOICE_PARCEL_QUERY));

						b.putParcelable(InvoiceData.INVOICE_PARCEL_QUERY, Parcels.wrap(pq));

						async();
					}
					break;
				case 5566 :
					for (String key: data.getExtras().keySet())
					{
						Log.e ("ELISTIER", key + " is a key in the bundle");
					}
					break;
				case IntentIntegrator.REQUEST_CODE :
					temp = data.getExtras();
					final Bundle finalTemp = temp;
					DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							switch (which){
								case DialogInterface.BUTTON_POSITIVE :
									//Yes button clicked
									Bundle tempdata = new Bundle();
									//tempdata

									//tempdata.putString(InvoiceData.INVOICE_SCANNER_STRING, finalTemp.getString());
									//Log.e ("ALLKEYS", data.getExtras().getString("SCAN_RESULT_FORMAT"));
									/*for (String key: data.getExtras().getBundle("SCAN_RESULT").keySet())
									{
										Log.e ("ALLKEYS", key + " is a key in the bundle");
									}*/

									for (String key: b.keySet())
									{
										Log.e ("STARSERIES", key + " is a key in the bundle");
									}

									//interfaceInvoiceInfo.onBarcodeScan(tempdata);
									break;

								case DialogInterface.BUTTON_NEGATIVE :
									//No button clicked
									break;
							}
						}
					};

					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setMessage("สแกนบิลล์นี้ต่อไปใช่ไหม ?").setPositiveButton("ตกลง", dialogClickListener)
							  .setNegativeButton("ยกเลิก", dialogClickListener).show();

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
