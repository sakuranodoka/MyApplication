package system;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.administrator.myapplication.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit.InterfaceListen;
import retrofit.RetrofitAbstract;
import retrofit.ServiceRetrofit;
import retrofit2.Retrofit;

public class UpdateApplication extends AsyncTask<String,Void,Void> {
	private Context context;

	public static final String BUNDLE_Version_Key_Tag = "versionKey";

	private ProgressDialog progressDialog;

	public void setContext(Context contextf) {
		context = contextf;
	}

	public static Boolean checkUpdate() {
		return false;
	}

	public void checkVersion(String versionKey) {
		Bundle b = new Bundle();
		b.putString(BUNDLE_Version_Key_Tag, versionKey);
		new ServiceRetrofit().callServer(interfaceListen, RetrofitAbstract.RETROFIT_SELF_UPDATE, b);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressDialog = ProgressDialog.show(context,"กรุณารอสักครู่" ,"กำลังติดตั้งแอปพลิเคชันในเวอร์ชันใหม่...", true);
	}

	@Override
	protected void onPostExecute(Void aVoid) {
		super.onPostExecute(aVoid);
		if(progressDialog != null)
			progressDialog.dismiss();
	}

	@Override
	protected Void doInBackground(String... arg0) {
		try {
			Log.e("hello 1", "hi");
			URL url = new URL(arg0[0]);
			HttpURLConnection c = (HttpURLConnection) url.openConnection();
			c.setRequestMethod("GET");
			c.setDoOutput(true);
			c.connect();

			String PATH = "/mnt/sdcard/Download/";
			File file = new File(PATH);
			file.mkdirs();
			File outputFile = new File(file, "update.apk");
			if(outputFile.exists()) {
				outputFile.delete();
			}
			FileOutputStream fos = new FileOutputStream(outputFile);

			InputStream is = c.getInputStream();

			byte[] buffer = new byte[32765];
			int len1 = 0;
			while((len1 = is.read(buffer)) != -1) {
				fos.write(buffer, 0, len1);
			}
			fos.close();
			is.close();

			Toast.makeText(context, "ติดตั้งแอปพลิเคชันในเวอร์ชันที่ใหม่กว่า", Toast.LENGTH_SHORT).show();

			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(new File("/mnt/sdcard/Download/update.apk")), "application/vnd.android.package-archive");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
			context.startActivity(intent);

			Log.e("hello 2", "hi");
		} catch (Exception e) {
			Log.e("UpdateAPP", "Update error! " + e.getMessage());
		}
		return null;
	}

	protected InterfaceListen interfaceListen = new InterfaceListen() {
		@Override
		public void onResponse(Object data, Retrofit retrofit) {
			AppVersionPOJO pojo = (AppVersionPOJO) data;
			if(pojo.getReupdate()) {
				SharedPreferences specifiedSP = context.getSharedPreferences(MainActivity._PREF_MODE, Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = specifiedSP.edit();
				editor.putString(SystemData.SHARED_App_Version_KEY, pojo.getVERSIONNAME().get(0).getName());
				editor.apply();

				execute(pojo.getUrl());
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
}
