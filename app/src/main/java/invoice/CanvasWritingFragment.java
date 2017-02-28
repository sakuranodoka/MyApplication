package invoice;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;

import com.example.administrator.myapplication.R;

/**
 * Created by Administrator on 28/2/2560.
 */

public class CanvasWritingFragment extends Fragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setContentView(R.layout.view_signature_canvas);
    }
}
