package user.user.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

/**
 * Created by Administrator on 16/11/2559.
 */
public class ViewHolderMenu extends RecyclerView.ViewHolder {

    public TextView menuName;

    public TextView detailName;

    public ImageView imageSource;

    public ViewHolderMenu(View itemView) {
        super(itemView);
        menuName = (TextView) itemView.findViewById(R.id.menuName);
        detailName = (TextView) itemView.findViewById(R.id.detailName);
        imageSource = (ImageView) itemView.findViewById(R.id.imageSource);
    }
}