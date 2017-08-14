package datepicker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
		  implements DatePickerDialog.OnDateSetListener {

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		setRetainInstance(true);

		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
		TextView tv1= (TextView) getActivity().findViewById(R.id.edit_text_date_picker);
		tv1.setText(view.getYear()+"-"+(view.getMonth()+1)+"-"+ view.getDayOfMonth());
	}
}
