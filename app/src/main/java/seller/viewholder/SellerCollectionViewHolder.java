package seller.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

/**
 * Created by Administrator on 17/11/2559.
 */
public class SellerCollectionViewHolder extends RecyclerView.ViewHolder {

    public TextView collectionItemCode;
    public TextView collectionBal;
    public TextView collectionNet;

    public SellerCollectionViewHolder(View itemView) {
        super(itemView);

        collectionItemCode = (TextView) itemView.findViewById(R.id.collectionItemCode);
        collectionBal = (TextView) itemView.findViewById(R.id.collectionBal);
        collectionNet = (TextView) itemView.findViewById(R.id.collectionNet);
    }
}
