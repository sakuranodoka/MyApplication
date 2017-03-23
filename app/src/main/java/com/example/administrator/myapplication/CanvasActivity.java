package com.example.administrator.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.LayoutRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;

import AppBar.ApplicationBar;
import fragment.FragmentToolbar;
import invoice.InvoiceData;

public class CanvasActivity extends AppCompatActivity {

    LinearLayout mContent;
    signature mSignature;
    Button btnSignatureClear;
	 private Button btnSignatureSubmit;
    public static String tempDir;
    public int count = 1;
    public String current = null;
    private Bitmap bitmap;
    View mView;
    File mypath;

    EditText inputUserFullname;

    String encodeResult = "";

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
        setContentView(R.layout.activity_canvas);

	    Toolbar myToolbar = (Toolbar) findViewById(R.id.app_toolbar);
	    setSupportActionBar(myToolbar);

	    // Get a support ActionBar corresponding to this toolbar
	    ActionBar ab = getSupportActionBar();

	    // Enable the Up button
	    ab.setDisplayHomeAsUpEnabled(true);

	    // Remove title name
	    ab.setDisplayShowTitleEnabled(false);

	    FragmentToolbar fToolbarScanner = new FragmentToolbar("กรุณาเซ็นต์ชื่อผู้รับ ในพื้นว่างด้านล่าง");
	    FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
	    fm.replace(R.id.layout_toolbar, fToolbarScanner);
	    fm.commit();

	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		 LinearLayout layoutCanvasReset = (LinearLayout) findViewById(R.id.layout_canvas_reset);
		 layoutCanvasReset.bringToFront();
		 layoutCanvasReset.invalidate();

//        tempDir = Environment.getExternalStorageDirectory() + "/" + getResources().getString(R.string.external_dir) + "/";
//        ContextWrapper cw = new ContextWrapper(getApplicationContext());
//        File directory = cw.getDir(getResources().getString(R.string.external_dir), Context.MODE_PRIVATE);

//        prepareDirectory();
//        uniqueId = getTodaysDate() + "_" + getCurrentTime() + "_" + Math.random();
//        current = uniqueId + ".png";
//        mypath= new File(directory,current);

        mContent = (LinearLayout) findViewById(R.id.linearLayout);
        mSignature = new signature(this, null);
        mSignature.setBackgroundColor(Color.WHITE);
        mContent.addView(mSignature, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        inputUserFullname = (EditText) findViewById(R.id.input_user_fullname);

        btnSignatureSubmit = (Button) findViewById(R.id.btn_signature_submit);
        btnSignatureSubmit.setEnabled(false);
        btnSignatureSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //boolean error = captureSignature();
                //if(!error){
            mView.setDrawingCacheEnabled(true);
            mSignature.save(mView);

            if (!validate()) {
                return;
            }

            Bundle b = new Bundle();

            b.putString("status", "done");
            b.putString(InvoiceData.ENCODED_IMAGE_PATH, encodeResult);
						b.putString(InvoiceData.USER_FULLNAME, inputUserFullname.getText().toString());

            Intent intent = new Intent();
            intent.putExtras(b);
            setResult(RESULT_OK ,intent);
            finish();
                //}
            }
        });

        btnSignatureClear = (Button)findViewById(R.id.btn_signature_clear);
        btnSignatureClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //signature.clear();

                mSignature.clear();
                btnSignatureSubmit.setEnabled(false);
            }
        });

        mView = mContent;

        mView.setDrawingCacheEnabled(true);
    }

	@Override
	public boolean onSupportNavigateUp() {
		onBackPressed();
		return true;
	}

	public boolean validate() {
        boolean valid = true;

        String userFullName = inputUserFullname.getText().toString();

        if (userFullName.isEmpty()) {
            inputUserFullname.setError("กรุณาพิมพ์ชื่อของท่าน");
            valid = false;
        } else {
            inputUserFullname.setError(null);
        }
        return valid;
    }

    public class signature extends View {
        private static final float STROKE_WIDTH = 5f;
        private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
        private Paint paint = new Paint();
        private Path path = new Path();

        private float lastTouchX;
        private float lastTouchY;
        private final RectF dirtyRect = new RectF();

        public signature(Context context, AttributeSet attrs) {
            super(context, attrs);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(STROKE_WIDTH);
        }

        public void save(View v) {
            Log.e("log_tag", "Width: " + v.getWidth());
            Log.e("log_tag", "Height: " + v.getHeight());
            if(bitmap == null) {
                bitmap =  Bitmap.createBitmap (mContent.getWidth(), mContent.getHeight(), Bitmap.Config.RGB_565);
            }

            Canvas canvas = new Canvas(bitmap);

            try {
                //FileOutputStream mFileOutStream = new FileOutputStream(mypath);

                v.draw(canvas);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 0, baos);

                byte[] imageBytes = baos.toByteArray();
                String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                //Log.e("bitmap", encodedImage);

                encodeResult = encodedImage;

//                List<String> e = new ArrayList<>();
//                e.add(encodedImage);
                //new ServiceInvoice().callServer(interfaceListen, 0, e);

//                mBitmap.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);
//                mFileOutStream.flush();
//                mFileOutStream.close();
//                String url = MediaStore.Images.Media.insertImage(getContentResolver(), mBitmap, "title", null);
//                Log.v("log_tag","url: " + url);
                //In case you want to delete the file
                //boolean deleted = mypath.delete();
                //Log.v("log_tag","deleted: " + mypath.toString() + deleted);
                //If you want to convert the image to string use base64 converter

            }
            catch(Exception e) {
                Log.e("error_canvas", e.toString());
            }
        }

        public void clear() {
            path.reset();
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawPath(path, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();
            btnSignatureSubmit.setEnabled(true);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(eventX, eventY);
                    lastTouchX = eventX;
                    lastTouchY = eventY;
                    return true;

                case MotionEvent.ACTION_MOVE:

                case MotionEvent.ACTION_UP:

                    resetDirtyRect(eventX, eventY);
                    int historySize = event.getHistorySize();
                    for (int i = 0; i < historySize; i++) {
                        float historicalX = event.getHistoricalX(i);
                        float historicalY = event.getHistoricalY(i);
                        expandDirtyRect(historicalX, historicalY);
                        path.lineTo(historicalX, historicalY);
                    }
                    path.lineTo(eventX, eventY);
                    break;

                default:
                    debug("Ignored touch event: " + event.toString());
                    return false;
            }

            invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                    (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

            lastTouchX = eventX;
            lastTouchY = eventY;

            return true;
        }

        private void debug(String string) {}

        private void expandDirtyRect(float historicalX, float historicalY) {
            if (historicalX < dirtyRect.left) {
                dirtyRect.left = historicalX;
            } else if (historicalX > dirtyRect.right) {
                dirtyRect.right = historicalX;
            }

            if (historicalY < dirtyRect.top) {
                dirtyRect.top = historicalY;
            } else if (historicalY > dirtyRect.bottom) {
                dirtyRect.bottom = historicalY;
            }
        }

        private void resetDirtyRect(float eventX, float eventY) {
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }
    }
}
