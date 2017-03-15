package seller.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.administrator.myapplication.R;

/**
 * Created by Administrator on 6/1/2560.
 */
public class LoadingScreenViewHoler extends RecyclerView.ViewHolder {

    public ProgressBar progressBar;

    public LoadingScreenViewHoler(View itemView) {
        super(itemView);
        this.progressBar = (ProgressBar) itemView.findViewById(R.id.spinnerLoading);
    }
}
