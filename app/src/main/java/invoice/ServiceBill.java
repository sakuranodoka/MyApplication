package invoice;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.administrator.myapplication.MainActivity;

import org.parceler.Parcels;

import java.util.List;

import authen.AuthenData;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit.DataWrapper;
import retrofit2.Retrofit;
import rx.Observable;

public class ServiceBill {
	public static Observable<List<BillPOJO>> getBillList(Bundle bundle, Retrofit retrofit) {
		if(bundle == null) try {
			throw new Exception();
		} catch (Exception e) {
			e.printStackTrace();
		}
		InterfaceInvoice i = retrofit.create(InterfaceInvoice.class);

		StringBuilder BILL_NO_STRING = new StringBuilder(64);
		StringBuilder BILL_DATE_STRING = new StringBuilder(64);

		BILL_NO_STRING.append("");
		BILL_DATE_STRING.append("");
		if (bundle.containsKey(InvoiceData.INVOICE_PARCEL_QUERY)) {
			 ParcelQuery pq = Parcels.unwrap(bundle.getParcelable(InvoiceData.INVOICE_PARCEL_QUERY));
			 BILL_NO_STRING.append(pq.getBill());
			 BILL_DATE_STRING.append(pq.getDatetime());
		}

		Log.e("Parcel Query", BILL_NO_STRING.toString() + " | " + BILL_DATE_STRING.toString());

		//Log.e("Parcel Query", BILL_DATE_temp + " | " + BILL_DATE_temp);

//		SharedPreferences sp = AppCompatActivity.getSharedPreferences(MainActivity._PREF_MODE, Context.MODE_PRIVATE);

		RequestBody BILL_NO = RequestBody.create(MediaType.parse("text/plain"), BILL_NO_STRING.toString());
		RequestBody BILL_DATE = RequestBody.create(MediaType.parse("text/plain"), BILL_DATE_STRING.toString());
		RequestBody SHIP_NO = RequestBody.create(MediaType.parse("text/plain"), bundle.getString(AuthenData.USERNAME));
		RequestBody LIMIT = RequestBody.create(MediaType.parse("text/plain"), bundle.getString(InvoiceData.INVOICE_LIMIT));

		return i.getBillList(BILL_NO, BILL_DATE, SHIP_NO ,LIMIT);
	}

	public static Observable<DataWrapper> setBillCount(Bundle bundle, Retrofit retrofit) {
		InterfaceInvoice i = retrofit.create(InterfaceInvoice.class);

		BillPOJO pojo = Parcels.unwrap(bundle.getParcelable(InvoiceData.BILL_POJO));

		RequestBody BILL_NO = RequestBody.create(MediaType.parse("text/plain"), pojo.getBILL_NO());
		RequestBody BILL_COUNT = RequestBody.create(MediaType.parse("text/plain"), pojo.getBILL_COUNT());

		return i.setBillCount(BILL_NO, BILL_COUNT);
	}

	public static Observable<DataWrapper> setCompleteBill(Bundle bundle, Retrofit retrofit) {
		if (bundle == null) try {
			 throw new Exception();
		} catch (Exception e) {
			e.printStackTrace();
		}
		InterfaceInvoice i = retrofit.create(InterfaceInvoice.class);

		RequestBody FK_BILL_NO = RequestBody.create(MediaType.parse("text/plain"), bundle.getString(InvoiceData.BILL_NO));

		RequestBody LATITUTE = RequestBody.create(MediaType.parse("text/plain"), bundle.getString(InvoiceData.LATITUDE));
		RequestBody LONGITUTE = RequestBody.create(MediaType.parse("text/plain"), bundle.getString(InvoiceData.LONGITUDE));

		RequestBody COUNTING = RequestBody.create(MediaType.parse("text/plain"), bundle.getString(InvoiceData.BILL_COUNT));

		RequestBody SIGN_NAME = RequestBody.create(MediaType.parse("text/plain"), bundle.getString(InvoiceData.USER_FULLNAME));
		RequestBody BITMAP_PATH = RequestBody.create(MediaType.parse("text/plain"), bundle.getString(InvoiceData.ENCODED_IMAGE_PATH));

		return i.setCompleteBill(FK_BILL_NO,
					  LATITUTE,
					  LONGITUTE,
					  COUNTING,
					  SIGN_NAME,
					  BITMAP_PATH);
	}

	public static Observable<DataWrapper> setGPS(Bundle bundle, Retrofit retrofit) {
		if(bundle == null) try {
			throw new Exception();
		} catch (Exception e) {
			e.printStackTrace();
		}
		InterfaceInvoice i = retrofit.create(InterfaceInvoice.class);

		RequestBody LATITUTE = RequestBody.create(MediaType.parse("text/plain"), bundle.getString(InvoiceData.LATITUDE));
		RequestBody LONGITUTE = RequestBody.create(MediaType.parse("text/plain"), bundle.getString(InvoiceData.LONGITUDE));

		return i.setGPS(LATITUTE, LONGITUTE);
	}
}
