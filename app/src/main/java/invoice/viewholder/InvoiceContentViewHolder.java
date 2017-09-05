package invoice.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

public class InvoiceContentViewHolder extends RecyclerView.ViewHolder {

    public Object relativeInvoiceInfo;

    public Object textViewInvoicePreview;
    public Object textViewInvoiceAddress;
    public Object textViewInvoiceDate;
    public Object btnRemove;

    public Object textViewInvoiceBoxNumber;

    public InvoiceContentViewHolder(View itemView) {
        super(itemView);

        relativeInvoiceInfo = itemView.findViewById(R.id.relative_invoice_info);
        textViewInvoicePreview = itemView.findViewById(R.id.text_view_invoice_preview);
        textViewInvoiceAddress = itemView.findViewById(R.id.text_view_invoice_address);
        textViewInvoiceDate = itemView.findViewById(R.id.text_view_invoice_date);
        btnRemove = itemView.findViewById(R.id.btn_remove);
        textViewInvoiceBoxNumber = itemView.findViewById(R.id.text_view_invoice_box_number);
    }
}
