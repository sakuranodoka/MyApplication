package com.example.administrator.myapplication;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import guide.FragmentGuide;
import guide.FragmentGuide_2;

public class GuideActivity extends AppCompatActivity {

	ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_view_pager);

		MyPageAdapter adapter = new MyPageAdapter(getSupportFragmentManager());
		this.viewPager = (ViewPager) findViewById(R.id.view_pager_main);
		this.viewPager.setAdapter(adapter);
	}

	public class MyPageAdapter extends FragmentPagerAdapter {

		private final int PAGE_NUM = 2;

		public MyPageAdapter(FragmentManager fm) {
			super(fm);
		}

		public int getCount() {
			return this.PAGE_NUM;
		}

		public Fragment getItem(int position) {
			switch(position) {
				case 0:
					return new FragmentGuide();
				case 1:
					return new FragmentGuide_2();
				default:
					return null;
			}
		}
	}
}
