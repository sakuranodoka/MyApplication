package invoice;

import android.os.Bundle;
import android.util.Log;

import org.parceler.Parcels;

import java.util.List;

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

		String BILL_NO_temp = "";
		String BILL_DATE_temp = "";
		if(bundle.containsKey(InvoiceData.INVOICE_PARCEL_QUERY)) {
			ParcelQuery pq = Parcels.unwrap(bundle.getParcelable(InvoiceData.INVOICE_PARCEL_QUERY));
			BILL_NO_temp = pq.getBill();
			BILL_DATE_temp = pq.getDatetime();
		}

		RequestBody BILL_NO = RequestBody.create(MediaType.parse("text/plain"), BILL_NO_temp);
		RequestBody BILL_DATE = RequestBody.create(MediaType.parse("text/plain"), BILL_DATE_temp);
		RequestBody LIMIT = RequestBody.create(MediaType.parse("text/plain"), bundle.getString(InvoiceData.INVOICE_LIMIT));

		Log.e("LIMITEd",  bundle.getString(InvoiceData.INVOICE_LIMIT));

		return i.getBillList(BILL_NO, BILL_DATE, LIMIT);
	}

	public static Observable<DataWrapper> setBillCount(Bundle bundle, Retrofit retrofit) {
		InterfaceInvoice i = retrofit.create(InterfaceInvoice.class);

		BillPOJO pojo = Parcels.unwrap(bundle.getParcelable(InvoiceData.BILL_POJO));

		RequestBody BILL_NO = RequestBody.create(MediaType.parse("text/plain"), pojo.getBILL_NO());
		RequestBody BILL_COUNT = RequestBody.create(MediaType.parse("text/plain"), pojo.getBILL_COUNT());

		return i.setBillCount(BILL_NO, BILL_COUNT);
	}

	public static Observable<DataWrapper> setCompleteBill(Bundle bundle, Retrofit retrofit) {
		if(bundle == null) try {
			throw new Exception();
		} catch (Exception e) {
			e.printStackTrace();
		}
		InterfaceInvoice i = retrofit.create(InterfaceInvoice.class);

		RequestBody BILL_NO = RequestBody.create(MediaType.parse("text/plain"), bundle.getString(InvoiceData.BILL_NO));
		RequestBody BILL_COUNT = RequestBody.create(MediaType.parse("text/plain"), bundle.getString(InvoiceData.BILL_COUNT));

		RequestBody BILL_BITMAP = RequestBody.create(MediaType.parse("text/plain"), bundle.getString(InvoiceData.ENCODED_IMAGE_PATH));
		RequestBody BILL_USERNAME = RequestBody.create(MediaType.parse("text/plain"), bundle.getString(InvoiceData.USER_FULLNAME));

		return i.setCompleteBill(BILL_NO, BILL_COUNT, BILL_BITMAP, BILL_USERNAME);
	}
}
