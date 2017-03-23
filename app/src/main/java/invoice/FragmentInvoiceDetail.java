package invoice;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import invoice.item.InvoiceBaseItem;
import invoice.item.ItemInvoice;
import invoice.item.ItemInvoicePreview;
import invoice.item.ParcelInvoice;
import invoice.viewholder.InvoiceViewHolder;

/**
 * Created by Administrator on 6/3/2560.
 */

public class FragmentInvoiceDetail extends Fragment {

    private RecyclerView recyclerView;
    private InvoiceDetailAdapter adapter;
    public static final int INVOICE_KEY_CODE = 19993;

    private Bundle b;

    public FragmentInvoiceDetail() {
        super();
        this.b = null;
    }

    public FragmentInvoiceDetail(Bundle b) {
        super();
        this.b = b;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);

        View rootView = inflater.inflate(R.layout.layout_recycler_view, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViews);

        int orientation = this.getResources().getConfiguration().orientation;
        if( orientation == Configuration.ORIENTATION_PORTRAIT ) {
            //code for portrait mode (แนวตั้ง)
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            //code for landscape mode (แนวนอน)
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }

        List<InvoiceBaseItem> listItem = null;
        if(b != null) {
	        listItem = new ArrayList<>();
			  if(b.containsKey(InvoiceData.INVOICE_PARCEL)) {
	           ParcelInvoice pi = Parcels.unwrap(b.getParcelable(InvoiceData.INVOICE_PARCEL));
	           for( ItemInvoicePreview i : pi.getListInvoice()) {
	              ItemInvoice item = new ItemInvoice(INVOICE_KEY_CODE);
	              item.setInvoicePreview(i.getInvoicePreview());
	              item.setInvoiceDate(i.getInvoiceDate());
	              listItem.add(item);
		        }
			  }
        }
        adapter = new InvoiceDetailAdapter(listItem);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return rootView;
    }

    public class InvoiceDetailAdapter extends RecyclerView.Adapter {

        InvoiceDetailAdapter() {}
        InvoiceDetailAdapter(List<InvoiceBaseItem> listItem) { this.listItem = listItem; }

        public List<InvoiceBaseItem> listItem = new ArrayList<>();

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType == INVOICE_KEY_CODE) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.view_invoice_info, parent, false);
                return new InvoiceViewHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(!listItem.isEmpty()) {
                if(holder instanceof InvoiceViewHolder) {
                    ItemInvoice item = (ItemInvoice) listItem.get(position);

                    InvoiceViewHolder vh = (InvoiceViewHolder) holder;
                    TextView textViewInvoicePreview = (TextView) vh.textViewInvoicePreview;
                    textViewInvoicePreview.setText(item.getInvoicePreview());

                    TextView textViewInvoiceDate = (TextView) vh.textViewInvoiceDate;
                    textViewInvoiceDate.setText(item.getInvoiceDate());
                }
            }
        }

        @Override
        public int getItemCount() {
            if(this.listItem != null) {
                return this.listItem.size();
            } else {
                return 0;
            }
        }

        @Override
        public int getItemViewType(int position) {
            if(this.listItem != null) {
                return this.listItem.get(position).getType();
            } else {
                return 0;
            }
        }
    }
}
