package seller.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

/**
 * Created by Administrator on 30/1/2560.
 */

public class SellerStorageDayCoverViewHolder extends RecyclerView.ViewHolder {

    public TextView sku;
    public TextView storage;
    public TextView dayCover;

    public Object sellOutBar;
    public TextView XBar;

    public Button moreInfo;

    public SellerStorageDayCoverViewHolder(View itemView) {
        super(itemView);
        sku = (TextView) itemView.findViewById(R.id.sku);
        storage = (TextView) itemView.findViewById(R.id.storage);
        dayCover = (TextView) itemView.findViewById(R.id.day_cover);

        sellOutBar = itemView.findViewById(R.id.sell_out_bar);
        XBar = (TextView) itemView.findViewById(R.id.XBar);

        moreInfo = (Button) itemView.findViewById(R.id.more_info);
    }
}
