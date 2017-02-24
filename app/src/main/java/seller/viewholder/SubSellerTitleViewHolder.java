package seller.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

/**
 * Created by Administrator on 25/1/2560.
 */

public class SubSellerTitleViewHolder extends RecyclerView.ViewHolder {

    public TextView stock;
    public TextView XBar;
    public TextView dayCover;

    public SubSellerTitleViewHolder(View itemView) {
        super(itemView);

        stock = (TextView) itemView.findViewById(R.id.stock);
        XBar = (TextView) itemView.findViewById(R.id.XBar);
        dayCover = (TextView) itemView.findViewById(R.id.day_cover);

    }
}
