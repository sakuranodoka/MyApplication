package invoice.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.administrator.myapplication.R;

import autocomplete.InstantAutocomplete;

public class InvoiceHeaderViewHolder extends RecyclerView.ViewHolder {

	public InstantAutocomplete dropdownCustomDate;

	public InvoiceHeaderViewHolder(View itemView) {
		super(itemView);
		dropdownCustomDate = (InstantAutocomplete) itemView.findViewById(R.id.dropdown_custom_date);
	}
}