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
import android.view.WindowManager;
import android.widget.FrameLayout;

import AppBar.ApplicationBar;
import authen.FragmentAuthen;
import authen.FragmentSignUp;
import fragment.FragmentScope;
import fragment.FragmentToolbar;
import fragment.InterfaceReplace;
import intent.IntentKeycode;
import invoice.FragmentInvoiceDetail;

public class AuthenActivity extends AppCompatActivity implements InterfaceReplace {
	private FragmentTransaction ft;

	private Bundle b = new Bundle();

	private int fragmentMode;
	private String fragmentModeTag = "authen";

	private FragmentTransaction fm;

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

		FragmentToolbar fToolbar = new FragmentToolbar("เข้าสู่ระบบ");
		fm = getSupportFragmentManager().beginTransaction();
		fm.replace(R.id.layout_toolbar, fToolbar);
		fm.commit();

		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		fragmentMode = FragmentScope.AUTH;
	}

	@Override
	public void replacing(int fragmentNext) {
		fragmentMode = fragmentNext;
		ft = getSupportFragmentManager().beginTransaction();
		switch(fragmentNext) {
			case FragmentScope.AUTH :
				getSupportActionBar().setDisplayHomeAsUpEnabled(false);

				fm = getSupportFragmentManager().beginTransaction();
				fm.replace(R.id.layout_toolbar, new FragmentToolbar("เข้าสู่ระบบ"));
				fm.commit();

				FragmentAuthen fragmentAuthen = new FragmentAuthen(b, this);
				ft.replace(R.id.blankFrameLayout, fragmentAuthen);
				break;
			case FragmentScope.SIGN_UP :
				getSupportActionBar().setDisplayHomeAsUpEnabled(true);

				fm = getSupportFragmentManager().beginTransaction();
				fm.replace(R.id.layout_toolbar, new FragmentToolbar("สมัครสมาชิก"));
				fm.commit();

				FragmentSignUp fragmentSignUp = new FragmentSignUp(b, this);
				ft.replace(R.id.blankFrameLayout, fragmentSignUp);
				break;
			default:break;
		}
		ft.commit();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		replacing(FragmentScope.AUTH);
	}

	@Override
	public void onLoginSuccess(Bundle sc) {
		Intent t = new Intent();
		t.putExtras(sc);
		setResult(IntentKeycode.RESULT_AUTHEN, t);
		finish();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if(outState != null)
			outState.putInt(fragmentModeTag, fragmentMode);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if(savedInstanceState != null)
			fragmentMode = savedInstanceState.getInt(fragmentModeTag);
		else
			fragmentMode = FragmentScope.AUTH;
	}

	@Override
	protected void onResume() {
		super.onResume();
		replacing(fragmentMode);
	}
}
