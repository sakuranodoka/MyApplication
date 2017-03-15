package user.user.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

/**
 * Created by Administrator on 15/11/2559.
 */
public class ViewHolderSection extends RecyclerView.ViewHolder {

    public TextView sectionTextView;

    public LinearLayout sectionBar;

    public ViewHolderSection(View itemView) {
        super(itemView);
        sectionTextView = (TextView) itemView.findViewById(R.id.sectionTextView);
        sectionBar = (LinearLayout) itemView.findViewById(R.id.sectionBar);
    }
}