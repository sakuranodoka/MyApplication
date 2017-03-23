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

import AppBar.ApplicationBar;
import AppBar.BarType;
import fragment.FragmentToolbar;
import invoice.FragmentInvoiceDetail;
import invoice.item.ItemInvoice;

public class InvoiceInfoActivity extends AppCompatActivity {

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
		FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
		fm.replace(R.id.layout_toolbar, fToolbar);

		Intent t = getIntent();
		Bundle b = t.getExtras();

		FragmentInvoiceDetail fragmentInvoiceDetail = new FragmentInvoiceDetail(b);
		fm.replace(R.id.blankFrameLayout, fragmentInvoiceDetail);
		fm.commit();
	}

	@Override
	public boolean onSupportNavigateUp() {
		onBackPressed();
		return true;
	}
}
