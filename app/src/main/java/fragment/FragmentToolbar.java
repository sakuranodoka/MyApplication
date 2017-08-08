package fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

import toolbars.ToolbarOptions;

public class FragmentToolbar extends Fragment {

	public FragmentToolbar() {}

	private String appName;
	private Object callbacks = null;
	private Bundle bundle = null;
	public FragmentToolbar(String appName) {
		this.appName = appName;
	}

	public FragmentToolbar(String appName, Object layout, Bundle bundle) {
		this.appName = appName;
		this.callbacks = layout;
		this.bundle = bundle;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = null;

		// No callbacks callings ...
		if(this.callbacks == null) {
			view = inflater.inflate(R.layout.layout_toolbar_default, container, false);
			TextView textViewToolbarHeader = (TextView) view.findViewById(R.id.text_toolbar_header);
			if(appName != null && !appName.isEmpty())
				textViewToolbarHeader.setText(appName);
		} else {
			if(this.callbacks instanceof ToolbarOptions) {
				final ToolbarOptions callbacks = ((ToolbarOptions) this.callbacks);
				view = inflater.inflate(R.layout.layout_toolbar_options, container, false);

				TextView textViewToolbarHeader = (TextView) view.findViewById(R.id.text_toolbar_header);
				if(appName != null && !appName.isEmpty())
					textViewToolbarHeader.setText(appName);

				RelativeLayout options = (RelativeLayout) view.findViewById(R.id.options_toolbar_header);

				RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
									RelativeLayout.LayoutParams.WRAP_CONTENT,
									RelativeLayout.LayoutParams.WRAP_CONTENT);
				layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

				int roundpaddings = (int) getResources().getDimension(R.dimen.round_padding);
				layoutParams.setMargins(roundpaddings, roundpaddings, roundpaddings, roundpaddings);

				ImageButton search = new ImageButton(getContext());
				search.setLayoutParams(layoutParams);
				search.setImageResource(R.drawable.ic_search_black_24dp);
				search.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));

				search.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						callbacks.response(bundle);
					}
				});

				options.addView(search);
			}
		}
		return view;
	}
}
