package com.example.administrator.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
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
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import authen.AuthenData;
import authen.AuthenMethod;
import fragment.FragmentExample;
import fragment.FragmentToolbar;
import intent.IntentKeycode;
import location.LocationData;
import location.pojo.GeoCoderPOJO;
import okhttp3.ResponseBody;
import retrofit.InterfaceListen;
import retrofit.RetrofitAbstract;
import retrofit.ServiceRetrofit;
import retrofit2.Retrofit;
import system.SystemData;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
	 public static final String _PREF_MODE = "_0OXFXx";

	 //user : infoshop
    // Pass : !nf0sh@p

	private SharedPreferences sp;

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
			//setContentView(R.layout.view_login);
	      setContentView(R.layout.layout_blank);

			Configuration config = new Configuration();

			config.locale = new Locale("th");
			getResources().updateConfiguration(config, null);

			Toolbar myToolbar = (Toolbar) findViewById(R.id.app_toolbar);
			setSupportActionBar(myToolbar);

			// Get a support ActionBar corresponding to this toolbar
			ActionBar ab = getSupportActionBar();

			// Enable the Up button
			//ab.setDisplayHomeAsUpEnabled(true);

	      // Remove title name
			//ab.setDisplayShowTitleEnabled(false);

			FragmentToolbar fToolbar = new FragmentToolbar();
			FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
			fm.replace(R.id.layout_toolbar, fToolbar);
			fm.commit();

			sp = getSharedPreferences(MainActivity._PREF_MODE, Context.MODE_PRIVATE);

			//Log.e("APP VERSION", sp.getString(SystemData.SHARED_App_Version_KEY, "Empty"));
			//Toast.makeText(this, "Hi", Toast.LENGTH_SHORT).show();

//	    if(sp != null && sp.getString(AuthenData.USERNAME, "").equals("")) {
//		    Intent t = new Intent(this, AuthenActivity.class);
//		    t.putExtras(b);
//		    startActivityForResult(t, IntentKeycode.RESULT_AUTHEN);
//	    } else {

//			LinearLayout llUsers = (LinearLayout) findViewById(R.id.users);
//			llUsers.setOnClickListener(new View.OnClickListener() {
//			   @Override
//			   public void onClick(View v) {
//			       //Intent t = new Intent(MainActivity.this, UserActivity.class);
//				   Intent t = new Intent(MainActivity.this, GuideActivity.class);
//			       startActivity(t);
//			   }
//			});
//
//			LinearLayout llAdmin = (LinearLayout) findViewById(R.id.admin);
//			llAdmin.setOnClickListener(new View.OnClickListener() {
//			   @Override
//			   public void onClick(View v) {
//			       Intent t = new Intent(MainActivity.this, EBusinessActivity.class);
//			       startActivity(t);
//			   }
//			});

//	    Bundle gBundle = new Bundle();
//	    gBundle.putString(LocationData.lat, "9.92");
//	    gBundle.putString(LocationData.lng, "100.51");
//
//	    new ServiceRetrofit().callServer(interfaceListen, RetrofitAbstract.RETROFIT_GEOCODING, gBundle);
    }

    private InterfaceListen interfaceListen = new InterfaceListen() {
	    @Override
	    public void onResponse(Object data, Retrofit retrofit) {
		    if(data instanceof GeoCoderPOJO) {
			    GeoCoderPOJO geoCoderData = (GeoCoderPOJO) data;
			    if(geoCoderData.getResults().size() > 0) {
				    Log.e("tambol", geoCoderData.getResults().get(1).getAddressComponents().get(1).getLongName());
				    Log.e("tambol", geoCoderData.getResults().get(1).getAddressComponents().get(2).getLongName());
			    }
		    }
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
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
	    super.onResume();
	    if(sp != null && sp.getString(AuthenData.USERNAME, "").equals("")) {
		    Intent t = new Intent(this, AuthenActivity.class);
		    startActivityForResult(t, IntentKeycode.RESULT_AUTHEN);
	    } else {
		    switch( sp.getInt(AuthenData.USER_ROLE, 0)) {
			    case 1:
				    startActivity(new Intent(MainActivity.this, EBusinessActivity.class));
				    break;
			    case 2:
				    startActivity(new Intent(MainActivity.this, GuideActivity.class));
				    break;
			    default:
				    Intent t = new Intent(this, AuthenActivity.class);
				    startActivityForResult(t, IntentKeycode.RESULT_AUTHEN);
               Log.e("error", "role is 0");
//				    if(AuthenMethod.setLogout(sp)) {
//					    Log.e("active", "set log out (auto)");
//				    }
//			    	Log.e("error", "role is 0");
			    	break;
		    }
	    }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("status", "App Start");
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == IntentKeycode.RESULT_AUTHEN) {
			if(sp != null) {
				SharedPreferences.Editor editor = sp.edit();
				if(data != null) {
					Bundle sc = data.getExtras();
					editor.putString(AuthenData.USERNAME, sc.getString(AuthenData.USERNAME));
					editor.putString(AuthenData.FULLNAME, sc.getString(AuthenData.FULLNAME));
					editor.putInt(AuthenData.USER_ROLE, sc.getInt(AuthenData.USER_ROLE));

					switch(sc.getInt(AuthenData.USER_ROLE)) {
						case 1:
			            startActivity(new Intent(MainActivity.this, EBusinessActivity.class));
							break;
						case 2:
			            startActivity(new Intent(MainActivity.this, GuideActivity.class));
							break;
					}
					Log.e("sp status", sc.getString(AuthenData.FULLNAME));
				}
				editor.apply();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
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
