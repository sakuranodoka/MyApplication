package com.example.administrator.myapplication;

import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import AppBar.ApplicationBar;
import authen.FragmentAuthen;
import authen.FragmentSignUp;
import fragment.FragmentScope;
import fragment.InterfaceReplace;
import intent.IntentKeycode;
import invoice.FragmentInvoiceDetail;

public class AuthenActivity extends AppliedAppCompat implements InterfaceReplace {

	 private FragmentTransaction ft;

	 private Bundle b;

	 public AuthenActivity() {
			super(R.layout.layout_blank);
	 }

	 private int fragmentMode;
	 private String fragmentModeTag = "authen";

	 @Override
	 protected void onCreate(@Nullable Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setApplicationToolbar("เข้าสู่ระบบ", ApplicationBar.APPLICATION_APP_BACK_PRESSED);

			Intent t = getIntent();
			b = t.getExtras();

			this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


			fragmentMode = FragmentScope.AUTH;
	 }

	 @Override
	 public void replacing(int fragmentNext) {
			fragmentMode = fragmentNext;
			ft = getSupportFragmentManager().beginTransaction();
			switch(fragmentNext) {
				 case FragmentScope.AUTH :
						FragmentAuthen fragmentAuthen = new FragmentAuthen(b, this);
						ft.replace(R.id.blankFrameLayout, fragmentAuthen);
						break;
				 case FragmentScope.SIGN_UP :
						FragmentSignUp fragmentSignUp = new FragmentSignUp(b, this);
						ft.replace(R.id.blankFrameLayout, fragmentSignUp);
						break;
				 default:break;
			}
			ft.commit();
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
			if(outState != null) {
				 outState.putInt(fragmentModeTag, fragmentMode);
			}
			super.onSaveInstanceState(outState);
	 }

	 @Override
	 protected void onRestoreInstanceState(Bundle savedInstanceState) {
			super.onRestoreInstanceState(savedInstanceState);
			if(savedInstanceState != null) {
				 fragmentMode = savedInstanceState.getInt(fragmentModeTag);
			} else {
				 fragmentMode = FragmentScope.AUTH;
			}
	 }

	 @Override
	 protected void onResume() {
			super.onResume();
			replacing(fragmentMode);
	 }
}
