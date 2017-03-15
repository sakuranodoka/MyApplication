package seller.viewholder;

import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.example.administrator.myapplication.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;

/**
 * Created by Administrator on 30/11/2559.
 */
public class BarChartViewHolder extends RecyclerView.ViewHolder {

    public HorizontalBarChart barChart;

    public LinearLayout barChartLegend;

    public BarChartViewHolder(View itemView) {
        super(itemView);
        barChart = (HorizontalBarChart) itemView.findViewById(R.id.barChart);
        barChartLegend = (LinearLayout) itemView.findViewById(R.id.barChartLegend);
    }
}