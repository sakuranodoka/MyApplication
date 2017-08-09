package com.example.administrator.myapplication;

import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import datepicker.DatePickerFragment;
import fragment.FragmentToolbar;
import intent.IntentKeycode;

public class AppliedSearchActivity extends AppCompatActivity {

	private FragmentTransaction fm = getSupportFragmentManager().beginTransaction();

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
		setContentView(R.layout.activity_applied_search);

		Toolbar myToolbar = (Toolbar) findViewById(R.id.app_toolbar);
		setSupportActionBar(myToolbar);

		// Get a support ActionBar corresponding to this toolbar
		ActionBar ab = getSupportActionBar();

		// Enable the Up button
		ab.setDisplayHomeAsUpEnabled(true);

		// Remove title name
		ab.setDisplayShowTitleEnabled(false);

		FragmentToolbar fToolbar = new FragmentToolbar("ค้นหา");
		fm.replace(R.id.layout_toolbar, fToolbar);
		fm.commit();

		TextView datepicker = (TextView) findViewById(R.id.edit_text_date_picker);
		datepicker.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDatePickerDialog();
			}
		});

		Button submit = (Button) findViewById(R.id.button_submit);
		submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent t = new Intent();

				

				t.putExtra("some_key", "String data");
				setResult(IntentKeycode.RESULT_INVOICE_CALLBACKS, t);

				finish();
			}
		});
	}

	public void showDatePickerDialog() {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getSupportFragmentManager(), "datePicker");
	}

//	Intent resultIntent = new Intent();
//// TODO Add extras or a data URI to this intent as appropriate.
//resultIntent.putExtra("some_key", "String data");
//	setResult(Activity.RESULT_OK, resultIntent);

	@Override
	public boolean onSupportNavigateUp() {
		onBackPressed();
		return true;
	}
}
