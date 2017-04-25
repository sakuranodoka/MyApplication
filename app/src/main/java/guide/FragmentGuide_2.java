package guide;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.administrator.myapplication.GuideActivity;
import com.example.administrator.myapplication.MainActivity;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.UserActivity;

public class FragmentGuide_2 extends Fragment {

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.help_restful_2, container, false);
		ImageButton skip = (ImageButton) view.findViewById(R.id.icon_skip);
		skip.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent t = new Intent(getActivity(), UserActivity.class);
				startActivity(t);
			}
		});
		return view;
	}
}
