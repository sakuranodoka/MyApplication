package seller.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

/**
 * Created by Administrator on 13/12/2559.
 */
public class SellerDescriptionViewHolder extends RecyclerView.ViewHolder {

    public TextView shopDescription;
    public TextView reportDescription;

    public TextView dateDescription;
    public LinearLayout extendDatePart;
    public TextView extendDateText;

    public SellerDescriptionViewHolder(View itemView) {
        super(itemView);
        //shopDescription = (TextView) itemView.findViewById(R.id.shop_description);
        //reportDescription = (TextView) itemView.findViewById(R.id.report_description);

        //dateDescription = (TextView) itemView.findViewById(R.id.date_description);
        //extendDatePart = (LinearLayout) itemView.findViewById(R.id.extend_date_part);
        //extendDateText = (TextView) itemView.findViewById(R.id.extend_date_text);
    }
}
