package com.example.administrator.myapplication;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import fragment.FragmentExample;
import fragment.FragmentToolbar;
import invoice.FragmentToolbarScanner;
import invoice.InvoiceData;

/**
 * Created by Administrator on 14/3/2560.
 */

public class CustomScannerActivity extends AppCompatActivity implements
				DecoratedBarcodeView.TorchListener {

	 private CaptureManager capture;
	 private DecoratedBarcodeView barcodeScannerView;
	 private Button switchFlashlightButton;

	 private int x = 0;

	 private Bundle b = new Bundle();

	@Override
	public void setContentView(@LayoutRes int layoutResID) {
		DrawerLayout fullLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.layout_main, null);
		FrameLayout frameLayout = (FrameLayout) fullLayout.findViewById(R.id.layout_content);
		getLayoutInflater().inflate(layoutResID, frameLayout, true);
		super.setContentView(fullLayout);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_custom_scanner);

		Toolbar myToolbar = (Toolbar) findViewById(R.id.app_toolbar);
		setSupportActionBar(myToolbar);

		// Get a support ActionBar corresponding to this toolbar
		ActionBar ab = getSupportActionBar();

		// Enable the Up button
		ab.setDisplayHomeAsUpEnabled(true);

		// Remove title name
		ab.setDisplayShowTitleEnabled(false);

		b = getIntent().getExtras();
		if(b != null) {// && b.containsKey(InvoiceData.INVOICE_CASE) ) {
			//FragmentToolbarScanner fToolbarScanner = new FragmentToolbarScanner( b.getInt(InvoiceData.INVOICE_CASE));

			FragmentToolbarScanner fToolbarScanner = new FragmentToolbarScanner(b);
			FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
			fm.replace(R.id.layout_toolbar, fToolbarScanner);
			fm.commit();
	   }

		barcodeScannerView = (DecoratedBarcodeView) findViewById(R.id.zxing_barcode_scanner);
		barcodeScannerView.setTorchListener(this);
		barcodeScannerView.setStatusText("หมายเหตุ : กรุณาทาบเส้นสีแดงให้คลอบคลุมรหัสบาร์โค้ด");

		capture = new CaptureManager(this, barcodeScannerView);
		Intent stable = getIntent();
		stable.putExtra("TEST", "01");
		//capture.initializeFromIntent(getIntent(), savedInstanceState);
		capture.initializeFromIntent(stable, savedInstanceState);
		capture.decode();
	}

	 @Override
	 protected void onResume() {
			super.onResume();
			capture.onResume();
	 }

	 @Override
	 protected void onPause() {
			super.onPause();
			capture.onPause();
	 }

	 @Override
	 protected void onDestroy() {
			super.onDestroy();
			capture.onDestroy();
	 }

	 @Override
	 protected void onSaveInstanceState(Bundle outState) {
			super.onSaveInstanceState(outState);
			capture.onSaveInstanceState(outState);
			outState.putAll(b);
	 }

	 @Override
	 protected void onRestoreInstanceState(Bundle savedInstanceState) {
			super.onRestoreInstanceState(savedInstanceState);
			if( savedInstanceState != null ) {
				 	b = savedInstanceState;
			} else if( getIntent().getExtras() != null ) {
				  b = getIntent().getExtras();
			} else { b = new Bundle(); }
	 }

	@Override
	public boolean onSupportNavigateUp() {
		onBackPressed();
		return true;
	}

	@Override
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
			return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
	 }

	 /**
		* Check if the device's camera has a Flashlight.
		* @return true if there is Flashlight, otherwise false.
		*/
	 private boolean hasFlash() {
			return getApplicationContext().getPackageManager()
							.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
	 }

	 public void switchFlashlight(View view) {
//			if (getString(R.string.turn_on_flashlight).equals(switchFlashlightButton.getText())) {
//				 barcodeScannerView.setTorchOn();
//			} else {
//				 barcodeScannerView.setTorchOff();
//			}
	 }

	 @Override
	 public void onTorchOn() {
			//switchFlashlightButton.setText(R.string.turn_off_flashlight);
	 }

	 @Override
	 public void onTorchOff() {
			//switchFlashlightButton.setText(R.string.turn_on_flashlight);
	 }


}
