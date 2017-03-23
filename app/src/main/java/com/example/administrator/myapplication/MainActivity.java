package com.example.administrator.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import fragment.FragmentExample;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
	 public static final String _PREF_MODE = "_0OXFXx";

	 //user : infoshop
    // Pass : !nf0sh@p

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
			setContentView(R.layout.view_login);

			Toolbar myToolbar = (Toolbar) findViewById(R.id.app_toolbar);
			setSupportActionBar(myToolbar);

			// Get a support ActionBar corresponding to this toolbar
			ActionBar ab = getSupportActionBar();

			// Enable the Up button
			ab.setDisplayHomeAsUpEnabled(true);

	      // Remove title name
			ab.setDisplayShowTitleEnabled(false);

			FragmentExample fExample = new FragmentExample();
			FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
			fm.replace(R.id.layout_toolbar, fExample);
			fm.commit();

			LinearLayout llUsers = (LinearLayout) findViewById(R.id.users);
			llUsers.setOnClickListener(new View.OnClickListener() {
			   @Override
			   public void onClick(View v) {
			       Intent t = new Intent(MainActivity.this, UserActivity.class);
			       startActivity(t);
			   }
			});
//
//			LinearLayout llAdmin = (LinearLayout) findViewById(R.id.admin);
//			llAdmin.setOnClickListener(new View.OnClickListener() {
//			   @Override
//			   public void onClick(View v) {
//			       Intent t = new Intent(MainActivity.this, EBusinessActivity.class);
//			       startActivity(t);
//			   }
//			});
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        //googleApiClient.connect();
        super.onStart();

        Log.i("status", "App Start");
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
