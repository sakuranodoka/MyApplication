package seller;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.myapplication.R;

import java.util.List;

import seller.graph.BarChartData;
import seller.item.SellerBaseItem;
import seller.viewholder.BarChartViewHolder;
import seller.viewholder.SubSellerTitleViewHolder;

/**
 * Created by Administrator on 25/1/2560.
 */

public class SellerSubAdapter extends RecyclerView.Adapter {

    public List<SellerBaseItem> listSellerBaseItem;

    public SellerSubAdapter(){}

    public void setRecyclerAdapter( List<SellerBaseItem> listSellerBaseItem) {
        this.listSellerBaseItem = listSellerBaseItem;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        switch(viewType) {
            case TypeSellerReport.TYPE_SELLER_SUB_TITLE :
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_sub_seller_title, parent, false);
                return new SubSellerTitleViewHolder(v);
            case TypeSellerReport.TYPE_REPORT_BAR :
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_bar_chart, parent, false);
                return new BarChartViewHolder(v);
            default:break;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof SubSellerTitleViewHolder) {

        } else if(holder instanceof BarChartViewHolder) {

            BarChartViewHolder vh = (BarChartViewHolder) holder;

            BarChartData barChartData = new BarChartData(vh);

            barChartData.setData(listSellerBaseItem.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return this.listSellerBaseItem!=null?this.listSellerBaseItem.size():0;
    }

    @Override
    public int getItemViewType(int position) {
        return this.listSellerBaseItem!=null?this.listSellerBaseItem.get(position).getType():0;
    }
}