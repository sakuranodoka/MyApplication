package com.example.administrator.myapplication;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.parceler.Parcels;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import AppBar.ApplicationBar;
import AppBar.BarType;
import authen.AuthenData;
import authen.InterfaceAuthen;
import authen.ViewAuthenDialogFragment;
import dialog.DialogFragmentData;
import fragment.FragmentExample;
import fragment.FragmentToolbar;
import intent.IntentKeycode;
import invoice.InvoiceData;
import invoice.ServiceInvoice;
import invoice.ViewInvoiceSwitchDialogFragment;
import invoice.item.ItemInvoicePreview;
import invoice.item.ParcelInvoice;
import okhttp3.ResponseBody;
import retrofit.InterfaceListen;
import retrofit2.Retrofit;
import user.InterfaceCamera;
import user.UserAdapter;
import user.UserBaseItem;
import user.user.item.ItemBill;
import user.user.item.ItemMenu;
import user.user.item.ItemSection;
import user.user.item.MenuMethod;

public class UserActivity extends AppCompatActivity implements
		  NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener,
		  authen.InterfaceAuthen.innerApp {

		// Menu
		public static RecyclerView userRecyclerView;
		public static UserAdapter userAdapter;

		private GoogleApiClient googleApiClient;

		private Uri uri;

		// Bundle data
		private Bundle b = null;
		private ParcelInvoice pi = null;

		// Dialog Fragment status
		private boolean mIsStateAlreadySaved = false;
		private boolean mPendingShowDialog = false;
		private String TAG_NAME = "TEST";

		// Shared Preferences
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
			setContentView(R.layout.activity_user);

			Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
			setSupportActionBar(toolbar);

			DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
			ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
			      this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

	      toggle.setDrawerIndicatorEnabled(true);

			drawer.setDrawerListener(toggle);
			toggle.syncState();

			NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
			navigationView.setNavigationItemSelectedListener(this);

			FragmentToolbar fToolbar = new FragmentToolbar("ยินดีต้อนรับ");
			FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
			fm.replace(R.id.layout_toolbar, fToolbar);
			fm.commit();

			DrawerLayout.LayoutParams params = new DrawerLayout.LayoutParams(DrawerLayout.LayoutParams.WRAP_CONTENT, DrawerLayout.LayoutParams.MATCH_PARENT);
			params.gravity = Gravity.START;

			NavigationView nv = (NavigationView) findViewById(R.id.nav_view);
			nv.setLayoutParams(params);

			b = new Bundle();
			pi = new ParcelInvoice();
			pi.setLatitude("0.0000");
			pi.setLongitude("0.0000");

			buildGoogleAPI();

			userRecyclerView = (RecyclerView) findViewById(R.id.userRecyclerView);
			userRecyclerView.setLayoutManager(new LinearLayoutManager(this));

			userAdapter = new UserAdapter();
			userRecyclerView.setAdapter(userAdapter);

			userAdapter.setInterfaceCamera(interfaceCamera);
			userAdapter.setBundle(b);

			List<UserBaseItem> userBaseItems = new ArrayList<>();

			ItemSection itemSection = new ItemSection();
			itemSection.setSection("โหมดบันทึกสินค้า");
			itemSection.setColor( ContextCompat.getColor(getApplicationContext(), R.color.lemon_wingless));
			userBaseItems.add(itemSection);

			ItemMenu itemMenu = null;

			itemMenu = new ItemMenu();
			itemMenu.setMenuMethod(MenuMethod.T_BARCODE);
			itemMenu.setImageSource(R.drawable.ic_view_array_black_24dp);
			itemMenu.setMenuName("สแกนใบสั่งสินค้า");
			itemMenu.setImageResourceColor( ContextCompat.getColor(getApplicationContext(), R.color.lemon_wingless));
			itemMenu.setDetailName("แสกนแถบบาร์โค้ด (ใบสั่งสินค้า) ของสินค้าเข้าระบบด้วยโทรศัพท์ของท่าน");
			userBaseItems.add(itemMenu);

			itemMenu = new ItemMenu();
			itemMenu.setMenuMethod(MenuMethod.T_SHOW_INVOICE);
			itemMenu.setImageSource(R.drawable.ic_list_24);
			itemMenu.setMenuName("แสดงใบสินค้า");
			itemMenu.setImageResourceColor( ContextCompat.getColor(getApplicationContext(), R.color.lemon_wingless));
			itemMenu.setDetailName("แสดงรายการใบสั่งสินค้าทั้งหมดที่ท่านได้สแกนไปแล้ว");
			userBaseItems.add(itemMenu);

//			itemMenu = new ItemMenu();
//			itemMenu.setMenuMethod(MenuMethod.T_PHOTO);
//			itemMenu.setImageSource(R.drawable.ic_camera_black_24dp);
//			itemMenu.setMenuName("ถ่ายภาพสินค้า");
//			itemMenu.setImageResourceColor( ContextCompat.getColor(getApplicationContext(), R.color.lemon_wingless));
//			itemMenu.setDetailName("ถ่ายภาพประกอบรายละเอียดของสินค้า");
//			itemMenu.setIntent(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
//			userBaseItems.add(itemMenu);

			userAdapter.setRecyclerAdapter(userBaseItems);
			userAdapter.notifyDataSetChanged();

			sp = getSharedPreferences(MainActivity._PREF_MODE, Context.MODE_PRIVATE);
    }

	 	protected synchronized void buildGoogleAPI() {
			 googleApiClient = new GoogleApiClient.Builder(this, this, this)
							 .addApi(LocationServices.API)
							 .addConnectionCallbacks(this)
							 .addOnConnectionFailedListener(this)
							 .build();
		}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.gc();

        Log.e("Intent ended", "true | " + requestCode);

        if( resultCode == RESULT_OK ) {
            if( requestCode == IntentIntegrator.REQUEST_CODE ) {
                // ยิงบาร์โค้ด หรือ QR Code
                IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

                if( scanningResult != null ) {
                    String scanContent = scanningResult.getContents();
                    String scanFormat = scanningResult.getFormatName();

                    if( b != null && b.containsKey(InvoiceData.INVOICE_CASE)) {
											  Parcelable wrapped = null;

											  int mode = b.getInt(InvoiceData.INVOICE_CASE);
                        switch( mode ) {
									 default:
									 case InvoiceData.INVOICE_CASE_INVOICE_PREVIEW :
											// ผลลัพธ์จากการสแกน ใบสั่งสินค้า
											Toast toast = Toast.makeText(getApplicationContext(),
														 "สแกนใบสั่งสินค้า " + scanContent + " เสร็จสิ้น", Toast.LENGTH_LONG);
											toast.show();

											if( pi != null ) {
												  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
												   Calendar c = Calendar.getInstance();

												   format.format(c.getTime());

												   ItemInvoicePreview el = new ItemInvoicePreview();
												   el.setInvoicePreview(scanContent.trim());
												   el.setInvoiceDate(format.format(c.getTime()));

												   ArrayList<ItemInvoicePreview> aList = null;

												   if( pi.getListInvoice() == null ) {
														  aList = new ArrayList<>();
												  } else {
												       aList = pi.getListInvoice();
												  }
												  aList.add(el);
												  pi.setListInvoice(aList);
											}

											wrapped = Parcels.wrap(pi);
											b.putParcelable(InvoiceData.INVOICE_PARCEL, wrapped);

											// Create dialog tag
											b.putInt(DialogFragmentData.DIALOG_FRAGMENT_TAG, DialogFragmentData.DIALOG_FRAGMENT_TAG_INVOICE);

											try {
												 showInvoiceSwitchDialogFragment(b);
											} catch( IllegalStateException ignored ) {
												 Log.e("error","open dialog catch true");
												 // There's no way to avoid getting this if saveInstanceState has already been called.
											}
											break;
									 case InvoiceData.INVOICE_CASE_INVOICE_USER_ID :
											// ผลลัพธ์จากการสแกน รหัสพนักงาน
											//b.putString(InvoiceData.USER_ID, scanContent);
											if(pi != null) {
												pi.setUserID(scanContent.trim());
												if(sp != null && !sp.getString(AuthenData.USERNAME, "").equals("")) {
													pi.setUsername(sp.getString(AuthenData.USERNAME, ""));
												} else {
													pi.setUsername(InvoiceData.NON_VALUES);
												}

												wrapped = Parcels.wrap(pi);
												b.putParcelable(InvoiceData.INVOICE_PARCEL, wrapped);
											}

											ParcelInvoice temp = Parcels.unwrap( b.getParcelable(InvoiceData.INVOICE_PARCEL) );

											for( ItemInvoicePreview i : temp.getListInvoice() ) {
													 Log.e("Invoice Preview", i.getInvoicePreview());
													 Log.e("Invoice Date", i.getInvoiceDate());
											}
											Log.e("Invoice Image Path", temp.getBitmap());
											Log.e("Invoice Lat", temp.getLatitude());
											Log.e("Invoice Lng", temp.getLongitude());
											Log.e("Invoice User Id", temp.getUserID());

											async();
											Toast.makeText(UserActivity.this, "บันทึกข้อมูลเสร็จสิ้น", Toast.LENGTH_LONG).show();
											break;
                        }
                    }
                }

            } else if( requestCode == 9999 ) {
							Toast.makeText(this, "Saved.", Toast.LENGTH_SHORT).show();
							getContentResolver().notifyChange(uri, null);
							ContentResolver cr = getContentResolver();

							try {

								Bitmap bitmap = MediaStore.Images.Media.getBitmap(cr, uri);
								Toast.makeText(getApplicationContext()
													, uri.getPath(), Toast.LENGTH_SHORT).show();

							} catch( Exception e ) {
								e.printStackTrace();
							}

            } else if( requestCode == RESULT_CANCELED ) {
              Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT).show();
            } else if( requestCode == IntentKeycode.RESULT_CANVAS ) {

                // บังคับเปิดกล้อง สแกนบัตรพนักงาน
                // กลับมาจาก canvas Activity (หลังจากวาดรูปเสร็จ)
                if( data != null ) {
									  if( pi != null ) {
											  pi.setBitmap(data.getExtras().getString(InvoiceData.ENCODED_IMAGE_PATH));
											 	pi.setUserFullName(data.getExtras().getString(InvoiceData.USER_FULLNAME));
										}
                    interfaceCamera.onBarcodeScan(InvoiceData.INVOICE_CASE_INVOICE_USER_ID, InvoiceData.INVOICE_PREVIEW_USER_ID);
                } else {
                    Log.e("system tracking", "canvas data is null");
                }
            }
        } else {
					 // กรณีกด back กลับมา (Back press state pressed.)
					 if( requestCode == IntentIntegrator.REQUEST_CODE ) {
							 if ( b != null && b.containsKey(InvoiceData.INVOICE_CASE) ) {
										int mode = b.getInt(InvoiceData.INVOICE_CASE);
										switch( mode ) {
											  default :
											  case InvoiceData.INVOICE_CASE_INVOICE_PREVIEW :
														 if( pi.getListInvoice()!= null && pi.getListInvoice().size() != 0 ) {

																 // ใส่ dialog tag
																 b.putInt(DialogFragmentData.DIALOG_FRAGMENT_TAG, DialogFragmentData.DIALOG_FRAGMENT_TAG_INVOICE);
																 try {
																	 showInvoiceSwitchDialogFragment(b);
																 } catch( IllegalStateException ignored ) {
																		Log.e("error","Open dialog catch true");
																	// There's no way to avoid getting this if saveInstanceState has already been called.
																 }
														 }
														 break;
											  case InvoiceData.INVOICE_CASE_INVOICE_USER_ID :
														//this one
														 if( pi != null ) {
																 pi.setUserID(InvoiceData.NON_VALUES);
																 if(sp != null && !sp.getString(AuthenData.USERNAME, "").equals("")) {
																	 pi.setUsername(sp.getString(AuthenData.USERNAME, ""));
																 } else {
																	 pi.setUsername(InvoiceData.NON_VALUES);
																 }

																 Parcelable wrapped = Parcels.wrap(pi);
																 b.putParcelable(InvoiceData.INVOICE_PARCEL, wrapped);
																 async();
																 Toast.makeText(UserActivity.this, "บันทึกข้อมูลเสร็จสิ้น", Toast.LENGTH_LONG).show();
														 }
														 break;
										}
							}
					 } else if( requestCode == IntentKeycode.RESULT_CANVAS ) {
						 	// ยกเลิกการวาดรูป
							b.putInt(DialogFragmentData.DIALOG_FRAGMENT_TAG, DialogFragmentData.DIALOG_FRAGMENT_TAG_INVOICE);
							try {
								 showInvoiceSwitchDialogFragment(b);
							} catch( IllegalStateException ignored ) {
								 Log.e("error","open dialog catch true");
								 // There's no way to avoid getting this if saveInstanceState has already been called.
							}
					 }
					 Log.e("error", "result code is not ok | Request code = "+requestCode);
        }

			 	// ไม่สน result ok
				if( requestCode == IntentKeycode.RESULT_AUTHEN ) {
						if( sp != null ) {
							 SharedPreferences.Editor editor = sp.edit();
							 if(data!=null) {
									 Bundle sc = data.getExtras();
									 editor.putString(AuthenData.USERNAME, sc.getString(AuthenData.USERNAME));
									 editor.putString(AuthenData.FULLNAME, sc.getString(AuthenData.FULLNAME));
									 Log.e("sp status", sc.getString(AuthenData.FULLNAME));
							 }
							 //editor.putString()
							 //editor.remove(AuthenData.USERNAME);
							 editor.apply();
						}
				}
    }

	 	protected void async() {
			 if(b != null && pi != null) {
					 //new ServiceInvoice().callServer(interfaceListen, 0, b);
				 pi.clearData();
				 Parcelable wrapped = Parcels.wrap(pi);
				 b.putParcelable(InvoiceData.INVOICE_PARCEL, wrapped);
			 }
		}

	 	private InterfaceListen interfaceListen = new InterfaceListen() {
			 @Override
			 public void onResponse(Object data, Retrofit retrofit) {
//					b = new Bundle();
					pi.clearData();
					Parcelable wrapped = Parcels.wrap(pi);
					b.putParcelable(InvoiceData.INVOICE_PARCEL, wrapped);
			 }

			 @Override
			 public void onBodyError(ResponseBody responseBodyError) {}

			 @Override
			 public void onBodyErrorIsNull() {}

			 @Override
			 public void onFailure(Throwable t) {}
		};

    protected InterfaceCamera interfaceCamera = new InterfaceCamera() {
        @Override
        public void onCapture() {
            int REQUEST_CAMERA = 17;
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "IMG_" + timeStamp + ".jpg";
            File f = new File(Environment.getExternalStorageDirectory()
                    , "DCIM/Camera/" + imageFileName);

            uri = Uri.fromFile(f);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            intent.putExtra("imageFileName", imageFileName);

            startActivityForResult(Intent.createChooser(intent
                    , "กรุณาเลือกแอปพลิเคชันกล้อง"), REQUEST_CAMERA);
        }

        @Override
        public void onBarcodeScan(int mode, String preview) {
					 if(b != null) {
						 b.putInt(InvoiceData.INVOICE_CASE, mode);
					 }

					 Intent t = new Intent(UserActivity.this, CustomScannerActivity.class);
					 Bundle zxingBn = new Bundle();
					 zxingBn.putInt(InvoiceData.INVOICE_CASE, mode);
					 zxingBn.putString(InvoiceData.INVOICE_PREVIEW, preview);
					 t.putExtras(zxingBn);
					 startActivityForResult(t, IntentIntegrator.REQUEST_CODE);
        }
    };

    private void showInvoiceSwitchDialogFragment(Bundle b) {
        // สเต็บนี้ยัง งง ๆ นะว่าทำงานยังไง = =//
        if( mIsStateAlreadySaved ) {
            mPendingShowDialog = true;
            Log.e("dialog state", "2-times set show with mIsStateAlreadySaved is true.");
        } else {
            if( b != null ) {
							 	b.putInt(DialogFragmentData.DIALOG_FRAGMENT_TAG, DialogFragmentData.DIALOG_FRAGMENT_TAG_INVOICE);
							 	FragmentManager fm = getSupportFragmentManager();
								ViewInvoiceSwitchDialogFragment dialogFragment = new ViewInvoiceSwitchDialogFragment(interfaceCamera, b);
								dialogFragment.show(fm, TAG_NAME);
            } else {
                Log.e("error" , "System error because bundle is null when dialog is setting up open individual away.");
            }
        }
    }

	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//		if( gravityCompat != 0 ) {
//			switch(gravityCompat) {
//				case GravityCompat.START:
//					drawer.closeDrawer(GravityCompat.START);
//					break;
//				case GravityCompat.END:
//					drawer.closeDrawer(GravityCompat.END);
//					break;
//			}
//		} else {
//			drawer.closeDrawer(GravityCompat.END);
//		}
		drawer.closeDrawer(GravityCompat.START);

		int id = item.getItemId();
		switch(id) {
			case R.id.nav_logout :
				if(this instanceof authen.InterfaceAuthen.innerApp) {
					this.onLogout();
				}
				break;
		}
		return true;
	}

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

	 @Override
	 protected void onResume() {
			super.onResume();
			Log.e("system", "resume");

			if( sp != null && sp.getString(AuthenData.USERNAME, "").equals("")) {
				  Intent t = new Intent(this, AuthenActivity.class);
				  t.putExtras(b);
				  startActivityForResult(t, IntentKeycode.RESULT_AUTHEN);
			}
	 }

	 @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        mIsStateAlreadySaved = false;
        if( mPendingShowDialog ) {
            mPendingShowDialog = false;

					 	if( b != null && b.containsKey(DialogFragmentData.DIALOG_FRAGMENT_TAG) ) {
							 	switch( b.getInt(DialogFragmentData.DIALOG_FRAGMENT_TAG) ) {
									 case DialogFragmentData.DIALOG_FRAGMENT_TAG_INVOICE :
											showInvoiceSwitchDialogFragment(b);
											Log.e("dialog state", "Resume fragments with mPendingShowDialog is true");
											break;
									 default:
											Log.e("dialog state", "Error");
											break;
								}
						}
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIsStateAlreadySaved = true;
        Log.e("dialog state", "Pause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if( googleApiClient != null && googleApiClient.isConnected() ) {
            googleApiClient.disconnect();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Parcelable wrapped = Parcels.wrap(pi);
        outState.putParcelable(InvoiceData.INVOICE_PARCEL, wrapped);
        outState.putBundle(InvoiceData.STATE_BUNDLE, b);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
			  Log.e("system", "on Restore");
        if( savedInstanceState != null ) {
            b = savedInstanceState.getBundle(InvoiceData.STATE_BUNDLE);
            if( savedInstanceState.containsKey(InvoiceData.INVOICE_PARCEL) ) {
                pi = Parcels.unwrap(savedInstanceState.getParcelable(InvoiceData.INVOICE_PARCEL));
            }
        } else {
            b = new Bundle();
            pi = new ParcelInvoice();
					 	pi.setLatitude("0.0000");
					 	pi.setLongitude("0.0000");
        }
    }

	 @Override
	 public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
			super.onRequestPermissionsResult(requestCode, permissions, grantResults);
			switch (requestCode) {
				 case 7629:
						buildGoogleAPI();
						googleApiClient.connect();
						break;
			}
	 }

	 @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000);

			if( ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
								ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            // TODO: Consider calling
					 	Log.e("permission","Permission not ok");

					 ActivityCompat.requestPermissions(this,
									 new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
									 7629);

            return;
        }
			 	Toast.makeText(this, "เชื่อมต่อกับระบบ GPS แล้ว", Toast.LENGTH_SHORT).show();
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
//			 	Toast.makeText(this, location.getLatitude()+" "+location.getLongitude(), Toast.LENGTH_SHORT).show();
        if( pi != null && b != null ) {
            pi.setLatitude( location.getLatitude() + "" );
            pi.setLongitude( location.getLongitude() + "" );
					  Parcelable wrapped = Parcels.wrap(pi);
					  b.putParcelable(InvoiceData.INVOICE_PARCEL, wrapped);
        }
    }

	 // ล้อกเอ้าท์
	 @Override
	 public void onLogout() {
			if( sp != null ) {
				 SharedPreferences.Editor editor = sp.edit();
				 editor.remove(AuthenData.USERNAME);
				 editor.apply();

				 Toast.makeText(this, "ขอบคุณที่ใช้บริการ", Toast.LENGTH_LONG).show();

				 onResume();
			}
	 }
}
