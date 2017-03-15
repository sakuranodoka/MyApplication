package seller.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.administrator.myapplication.R;
import com.github.mikephil.charting.charts.PieChart;

/**
 * Created by Administrator on 21/11/2559.
 */
public class PieChartViewHolder extends RecyclerView.ViewHolder {

    public PieChart bestSellerPieChart;

    public PieChartViewHolder(View itemView) {
        super(itemView);
        bestSellerPieChart = (PieChart) itemView.findViewById(R.id.bestSellerPieChart);
    }
}
