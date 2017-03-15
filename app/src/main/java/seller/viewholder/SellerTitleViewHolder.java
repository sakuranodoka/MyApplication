package seller.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.administrator.myapplication.R;

import autocomplete.InstantAutocomplete;

/**
 * Created by Administrator on 17/11/2559.
 */
public class SellerTitleViewHolder extends RecyclerView.ViewHolder {
    public InstantAutocomplete option;
    public ImageView settings;

    public SellerTitleViewHolder(View itemView) {
        super(itemView);
        option = (InstantAutocomplete) itemView.findViewById(R.id.option);
        settings = (ImageView) itemView.findViewById(R.id.settings);
    }
}
