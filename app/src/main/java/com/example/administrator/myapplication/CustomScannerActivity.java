package com.example.administrator.myapplication;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import invoice.InvoiceData;

/**
 * Created by Administrator on 14/3/2560.
 */

public class CustomScannerActivity extends AppCompatActivity implements
				DecoratedBarcodeView.TorchListener {

	 private CaptureManager capture;
	 private DecoratedBarcodeView barcodeScannerView;
	 private Button switchFlashlightButton;

	 private Bundle b = new Bundle();

	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_custom_scanner);

			barcodeScannerView = (DecoratedBarcodeView)findViewById(R.id.zxing_barcode_scanner);
			barcodeScannerView.setTorchListener(this);
			barcodeScannerView.setStatusText("หมายเหตุ : กรุณาทาบเส้นสีแดงให้คลอบคลุมรหัสบาร์โค้ด");

			b = getIntent().getExtras();

			//switchFlashlightButton = (Button)findViewById(R.id.switch_flashlight);

			// if the device does not have flashlight in its camera,
			// then remove the switch flashlight button...
			if( !hasFlash() ) {
				 	//switchFlashlightButton.setVisibility(View.GONE);
			}

			capture = new CaptureManager(this, barcodeScannerView);
			capture.initializeFromIntent(getIntent(), savedInstanceState);
			capture.decode();

			Button back = (Button) findViewById(R.id.backPressedState);
			Button next = (Button) findViewById(R.id.nextPressedState);

			if( b != null ) {
				 	RelativeLayout ns = (RelativeLayout) findViewById(R.id.view_zxing_next_state);
				 	if( ns != null && b.containsKey(InvoiceData.INVOICE_CASE) ) {
						  switch( b.getInt(InvoiceData.INVOICE_CASE) ) {
									case InvoiceData.INVOICE_CASE_INVOICE_USER_ID:
										 back.setVisibility(View.GONE);
										 break;
									case InvoiceData.INVOICE_CASE_INVOICE_PREVIEW:
									default:
										 ns.setVisibility(View.GONE);
										 break;
							}
				 	}

				 	TextView textScannerPreview = (TextView) findViewById(R.id.textScannerPreview);
				 	if( textScannerPreview != null && b.containsKey(InvoiceData.INVOICE_PREVIEW ) ) {
						 	textScannerPreview.setText(b.getString(InvoiceData.INVOICE_PREVIEW));
					}
			}

			View.OnClickListener onClick = new View.OnClickListener() {
				 @Override
				 public void onClick(View v) {
						finish();
				 }
			};

			back.setOnClickListener(onClick);
			next.setOnClickListener(onClick);
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
