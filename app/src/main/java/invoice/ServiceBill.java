package invoice;

import android.os.Bundle;
import android.util.Log;

import org.parceler.Parcels;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit.DataWrapper;
import retrofit2.Retrofit;
import rx.Observable;

public class ServiceBill {
	public static Observable<DataWrapper> setBillCount(Bundle bundle, Retrofit retrofit) {
		InterfaceInvoice i = retrofit.create(InterfaceInvoice.class);

		BillPOJO pojo = Parcels.unwrap(bundle.getParcelable(InvoiceData.BILL_POJO));

		RequestBody BILL_NO = RequestBody.create(MediaType.parse("text/plain"), pojo.getBILL_NO());
		RequestBody BILL_COUNT = RequestBody.create(MediaType.parse("text/plain"), pojo.getBILL_COUNT());

		return i.setBillCount(BILL_NO, BILL_COUNT);
	}
}
