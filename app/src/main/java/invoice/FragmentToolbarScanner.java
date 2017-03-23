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
				headerMessage.append("กรุณากำหนดโหมด");
				break;
		}
		textViewToolbarHeader.setText(headerMessage.toString());

		return view;
	}
}
