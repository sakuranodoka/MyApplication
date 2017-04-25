package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

public class FragmentToolbar extends Fragment {

	public FragmentToolbar() {}

	private String appName;
	public FragmentToolbar(String appName) {
		this.appName = appName;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_toolbar_default, container, false);

		TextView textViewToolbarHeader = (TextView) view.findViewById(R.id.text_toolbar_header);
		if(appName != null && !appName.isEmpty())
			textViewToolbarHeader.setText(appName);

		return view;
	}
}
