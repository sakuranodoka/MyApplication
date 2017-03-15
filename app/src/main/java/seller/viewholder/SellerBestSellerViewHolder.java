package seller.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

/**
 * Created by Administrator on 18/11/2559.
 */
public class SellerBestSellerViewHolder extends RecyclerView.ViewHolder {
    public TextView sellRanking;
    public TextView collectionItemCode;
    public TextView collectionNet;

    public SellerBestSellerViewHolder(View itemView) {
        super(itemView);
        sellRanking = (TextView) itemView.findViewById(R.id.sellRanking);
        collectionItemCode = (TextView) itemView.findViewById(R.id.collectionItemCode);
        collectionNet = (TextView) itemView.findViewById(R.id.collectionNet);
    }
}
