package invoice.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

public class InvoiceContentViewHolder extends RecyclerView.ViewHolder {

    public Object textViewInvoicePreview;
    public Object textViewInvoiceAddress;
    public Object textViewInvoiceDate;
    public Object btnRemove;

    public InvoiceContentViewHolder(View itemView) {
        super(itemView);

        textViewInvoicePreview = itemView.findViewById(R.id.text_view_invoice_preview);
        textViewInvoiceAddress = itemView.findViewById(R.id.text_view_invoice_address);
        textViewInvoiceDate = itemView.findViewById(R.id.text_view_invoice_date);
        btnRemove = itemView.findViewById(R.id.btn_remove);
    }
}
