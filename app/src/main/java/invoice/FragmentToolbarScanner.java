package invoice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

/**
 * Created by Administrator on 22/3/2560.
 */

public class FragmentToolbarScanner extends Fragment {
	public FragmentToolbarScanner() {}

	private int mode = 0;
	public FragmentToolbarScanner(int mode) {
		this.mode = mode;
	}

	private String bills = "";
	private int capacitynumber = 0;
	private int maximizenumber = 0;
	private Bundle b = null;
	//public FragmentToolbarScanner(String bills, int capacitynumber, int maximizenumber) {
	public FragmentToolbarScanner(Bundle b) {
		this.b = b;
		this.bills = bills;
		this.capacitynumber = capacitynumber;
		this.maximizenumber = maximizenumber;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_toolbar_scanner, container, false);

		TextView textViewToolbarHeader = (TextView) view.findViewById(R.id.text_toolbar_header);

		StringBuilder headerMessage = new StringBuilder(128);
		switch( mode ) {
			case InvoiceData.INVOICE_CASE_INVOICE_USER_ID:
				headerMessage.append("สแกนบัตรพนักงานของท่าน");
				RelativeLayout layoutForward = (RelativeLayout) view.findViewById(R.id.layout_forward);

				Button btnForward = (Button) view.findViewById(R.id.btn_forward);
				btnForward.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						getActivity().onBackPressed();
					}
				});

				layoutForward.setVisibility(View.VISIBLE);
				break;
			case InvoiceData.INVOICE_CASE_INVOICE_PREVIEW:
				headerMessage.append("สแกนบาร์โค้ดใบสั่งสินค้า");
				break;
			default:
				//headerMessage.append("กรุณากำหนดโหมด");
				if(b.containsKey(InvoiceData.INVOICE_SCANNER_STRING)) {
					headerMessage.append(b.getString(InvoiceData.INVOICE_SCANNER_STRING));
				}
				if(b.containsKey(InvoiceData.INVOICE_SCANNER_CAPACITY) && b.containsKey(InvoiceData.INVOICE_SCANNER_MAXIMIZE)) {
					RelativeLayout layoutCapacity = (RelativeLayout) view.findViewById(R.id.layout_capacity);
					layoutCapacity.setVisibility(View.VISIBLE);

					TextView textScannerCapacity = (TextView) view.findViewById(R.id.text_scanner_capacity);
					textScannerCapacity.setText(b.getInt(InvoiceData.INVOICE_SCANNER_CAPACITY)+"");

					TextView textScannerMaximize = (TextView) view.findViewById(R.id.text_scanner_maximize);
					textScannerMaximize.setText(b.getInt(InvoiceData.INVOICE_SCANNER_MAXIMIZE)+"");
				}
				break;
		}
		textViewToolbarHeader.setText(headerMessage.toString());
		return view;
	}
}
