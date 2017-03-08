package invoice.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

/**
 * Created by Administrator on 6/3/2560.
 */

public class InvoiceViewHolder extends RecyclerView.ViewHolder {

    public Object textViewInvoicePreview;
    public Object textViewInvoiceDate;
    public Object btnRemove;

    public InvoiceViewHolder(View itemView) {
        super(itemView);

        textViewInvoicePreview = itemView.findViewById(R.id.text_view_invoice_preview);
        textViewInvoiceDate = itemView.findViewById(R.id.text_view_invoice_date);
        btnRemove = itemView.findViewById(R.id.btn_remove);
    }
}
