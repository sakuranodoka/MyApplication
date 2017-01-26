package seller.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

/**
 * Created by Administrator on 25/1/2560.
 */

public class SubSellerTitleViewHolder extends RecyclerView.ViewHolder {

    public TextView storage;
    public TextView quantity;
    public TextView dayCover;

    public SubSellerTitleViewHolder(View itemView) {
        super(itemView);

        storage = (TextView) itemView.findViewById(R.id.storage);
        quantity = (TextView) itemView.findViewById(R.id.quantity);
        dayCover = (TextView) itemView.findViewById(R.id.day_cover);

    }
}
