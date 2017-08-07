package com.example.administrator.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcelable;
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

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import AppBar.ApplicationBar;
import AppBar.BarType;
import authen.AuthenData;
import fragment.FragmentToolbar;
import invoice.FragmentInvoiceDetail;
import invoice.InterfaceInvoiceInfo;
import invoice.InvoiceData;
import invoice.InvoicePOJO;
import invoice.item.ItemInvoice;
import invoice.item.ItemInvoicePreview;
import invoice.item.ParcelInvoice;
import okhttp3.ResponseBody;
import retrofit.InterfaceListen;
import retrofit.RetrofitAbstract;
import retrofit.ServiceRetrofit;
import retrofit2.Retrofit;
import sqlite.DbHelper;

public class InvoiceInfoActivity extends AppCompatActivity {

	private FragmentTransaction fm = getSupportFragmentManager().beginTransaction();

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

		FragmentToolbar fToolbar = new FragmentToolbar("รายการใบสั่งสินค้า");
		fm.replace(R.id.layout_toolbar, fToolbar);
		fm.commit();

		sp = getSharedPreferences(MainActivity._PREF_MODE, Context.MODE_PRIVATE);

		Intent t = getIntent();
		if(t.getExtras() == null) b = new Bundle();
		else b = t.getExtras();

		if(b != null && b.containsKey(InvoiceData.INVOICE_INFO_TAG)) {
			if(b.getInt(InvoiceData.INVOICE_INFO_TAG) == InvoiceData.INVOICE_INFO_INNER_APP) {
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
				fm.commit();
			} else if(b.getInt(InvoiceData.INVOICE_INFO_TAG) == InvoiceData.INVOICE_INFO_WITH_USER_ID) {
				async();
			}
		}
	}

	protected void async() {
		if(this.b != null) {
			b.putString(InvoiceData.INVOICE_LIMIT, "1");
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
	};

	private final InterfaceListen interfaceListen = new InterfaceListen() {
		@Override
		public void onResponse(Object data, Retrofit retrofit) {
				ParcelInvoice pi = new ParcelInvoice();

				List<InvoicePOJO> pojoList = (List<InvoicePOJO>) data;

				ArrayList<ItemInvoicePreview> listInvoice = new ArrayList<>();
				for (InvoicePOJO i : pojoList) {
					ItemInvoicePreview temp = new ItemInvoicePreview();
					temp.setInvoicePreview(i.getInfoInvoice());
					temp.setInvoiceSublocality(i.getInfoSubLocality());
					temp.setInvoiceLocality(i.getInfoLocality());
					temp.setInvoiceDate(i.getInfoTime());
					listInvoice.add(temp);
				}
				pi.setListInvoice(listInvoice);
				b.putParcelable(InvoiceData.INVOICE_PARCEL_CONTENT, Parcels.wrap(pi));

				fm = getSupportFragmentManager().beginTransaction();

				FragmentInvoiceDetail fragmentInvoiceDetail = new FragmentInvoiceDetail(b, interfaceInvoiceInfo);
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
			outState.putAll(b);
		}
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if(savedInstanceState != null) {
			b = savedInstanceState;

			if(b != null && b.containsKey(InvoiceData.INVOICE_INFO_TAG)) {
				if(b.getInt(InvoiceData.INVOICE_INFO_TAG) == InvoiceData.INVOICE_INFO_WITH_USER_ID)
					async();
				else {
					// Retain
				}
			}
		}
	}

	@Override
	public boolean onSupportNavigateUp() {
		onBackPressed();
		return true;
	}
}
