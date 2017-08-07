package invoice.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.administrator.myapplication.R;

public class ProgressBarViewHolder extends RecyclerView.ViewHolder {

	public ProgressBar progressBar;
	public ProgressBarViewHolder(View v) {
		super(v);
		progressBar = (ProgressBar)v.findViewById(R.id.progressBar);
	}
}
