package AppBar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.myapplication.MainActivity;
import com.example.administrator.myapplication.R;

import authen.InterfaceAuthen;

/**
 * Created by Administrator on 12/1/2560.
 */

public class ApplicationBar implements NavigationView.OnNavigationItemSelectedListener {

    public static final int APPLICATION_APP_MENU_BAR = 201;
    public static final int APPLICATION_APP_BACK_PRESSED = 202;

    public static final int APPLICATION_SHOP_BACK_PRESS = 902;

    private AppCompatActivity activity;

    private int type;

    private FABController _FAB_instance;

	 	private int gravityCompat = 0;

    public ApplicationBar(AppCompatActivity activity, int type) {
        this.activity = activity;
        this.type = type;

        this._FAB_instance = null;
    }

    public void setAppName(String appName) {
        setNavigatorMenu();

        RelativeLayout appNameBar = (RelativeLayout) activity.findViewById(R.id.appNameBar);
        appNameBar.setVisibility(View.VISIBLE);

        TextView textAppName = (TextView) activity.findViewById(R.id.textAppName);

//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        params.setMargins(-1*R.dimen.double_round , 0 , 0 ,0);
//
//        textAppName.setLayoutParams(params);
        textAppName.setText(appName);
    }

    public void setAppNameWithBackPressed(String appName) {
        setStateBackPressed();

        RelativeLayout appNameBar = (RelativeLayout) activity.findViewById(R.id.appNameBar);
        appNameBar.setVisibility(View.VISIBLE);

        TextView textAppName = (TextView) activity.findViewById(R.id.textAppName);
        textAppName.setText(appName);
    }

    public void setShopBar() {
        setStateBackPressed();

        RelativeLayout shopSearchBar = (RelativeLayout) activity.findViewById(R.id.shopSearchBar);
        shopSearchBar.setVisibility(View.VISIBLE);
    }

    // กำหนดว่าปุ่มนี้กดแล้วต้องกลับเท่านั้น
    private void setStateBackPressed() {
        if(activity != null) {
            Button backPressedState = (Button) activity.findViewById(R.id.backPressedState);
            backPressedState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.finish();
                }
            });
        }
    }

	  public void setGravityCompat(int gravityCompat) {
			 this.gravityCompat = gravityCompat;
		}

    @NonNull
    @Nullable
    private void setNavigatorMenu() {
        if(activity != null) {

            LinearLayout frontOptionLayout = (LinearLayout) activity.findViewById(R.id.frontOptionLayout);
            frontOptionLayout.setVisibility(View.GONE);

            Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);

            DrawerLayout drawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    activity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) activity.findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
        }
    }

    // Floating Action Button ...
    public void createFABInstance() {this._FAB_instance = new FABController();}
    public FABController getFABInstance() { return this._FAB_instance!=null? this._FAB_instance:null; }
    public class FABController {
        public void setFABDrawable(int drawable) {
            if(activity != null) {
                FloatingActionButton fab = (FloatingActionButton) activity.findViewById(R.id.floatingActionButton);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    fab.setImageDrawable(activity.getResources().getDrawable(drawable, activity.getTheme()));
                } else {
                    fab.setImageDrawable(activity.getResources().getDrawable(drawable));
                }
            }
        }

        public void turnOnNavigatorMenu() {
            if(activity != null) {
                final FloatingActionButton fab = (FloatingActionButton) activity.findViewById(R.id.floatingActionButton);

                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DrawerLayout drawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
                        drawer.openDrawer(GravityCompat.END);
                    }
                });
            }
        }

        public void setToggle() {
            if(activity != null) {
                final FloatingActionButton fab = (FloatingActionButton) activity.findViewById(R.id.floatingActionButton);

                if(fab.getVisibility() == View.VISIBLE) {
                    fab.setVisibility(View.GONE);
                } else {
                    fab.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        DrawerLayout drawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
			 	if( gravityCompat != 0 ) {
					 	switch(gravityCompat) {
							 case GravityCompat.START:
									drawer.closeDrawer(GravityCompat.START);
									break;
							 case GravityCompat.END:
									drawer.closeDrawer(GravityCompat.END);
									break;
						}
				} else {
					 	drawer.closeDrawer(GravityCompat.END);
				}

			 	int id = item.getItemId();
			 	switch(id) {
					 case R.id.nav_logout :
							if( activity instanceof authen.InterfaceAuthen.innerApp ) {
								 ((InterfaceAuthen.innerApp) activity).onLogout();
							}
							break;
				}
        return true;
    }
}
