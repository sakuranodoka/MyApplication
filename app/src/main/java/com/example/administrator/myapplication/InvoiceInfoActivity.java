package com.example.administrator.myapplication;

import android.content.Intent;
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
import fragment.FragmentToolbar;
import invoice.FragmentInvoiceDetail;
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

public class InvoiceInfoActivity extends AppCompatActivity {

	private FragmentTransaction fm = getSupportFragmentManager().beginTransaction();

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

		FragmentToolbar fToolbar = new FragmentToolbar("รายการใบสั่งสินค้า");
		fm.replace(R.id.layout_toolbar, fToolbar);
		fm.commit();

		Intent t = getIntent();
		if(t.getExtras() == null) b = null;
		else b = t.getExtras();

		if(b != null && b.containsKey(InvoiceData.INVOICE_INFO_TAG)) {
			if(b.getInt(InvoiceData.INVOICE_INFO_TAG) == InvoiceData.INVOICE_INFO_INNER_APP) {
				FragmentInvoiceDetail fragmentInvoiceDetail = new FragmentInvoiceDetail(b);
				fm = getSupportFragmentManager().beginTransaction();
				fm.replace(R.id.blankFrameLayout, fragmentInvoiceDetail);
				fm.commit();
			} else if(b.getInt(InvoiceData.INVOICE_INFO_TAG) == InvoiceData.INVOICE_INFO_WITH_USER_ID) {
				Log.e("stfu", "true");
				new ServiceRetrofit().callServer(interfaceListen, RetrofitAbstract.RETROFIT_PRE_INVOICE, b);
			}
		}
	}

	private InterfaceListen interfaceListen = new InterfaceListen() {
		@Override
		public void onResponse(Object data, Retrofit retrofit) {

			//Bundle b = new Bundle();
			ParcelInvoice pi = new ParcelInvoice();

			List<InvoicePOJO> pojoList = (List<InvoicePOJO>) data;
			ArrayList<ItemInvoicePreview> listInvoice = new ArrayList<>();
			for(InvoicePOJO i : pojoList ) {
				ItemInvoicePreview temp = new ItemInvoicePreview();
				temp.setInvoicePreview(i.getInfoInvoice());
				temp.setInvoiceDate(i.getInfoTime());
				listInvoice.add(temp);
			}

			pi.setListInvoice(listInvoice);
			b.putParcelable(InvoiceData.INVOICE_PARCEL, Parcels.wrap(pi));

			fm = getSupportFragmentManager().beginTransaction();

			FragmentInvoiceDetail fragmentInvoiceDetail = new FragmentInvoiceDetail(b);
			fm.replace(R.id.blankFrameLayout, fragmentInvoiceDetail);
			fm.commit();
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
	public boolean onSupportNavigateUp() {
		onBackPressed();
		return true;
	}
}
