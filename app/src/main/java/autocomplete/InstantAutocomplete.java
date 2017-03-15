package autocomplete;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;

import com.example.administrator.myapplication.R;

/**
 * Created by Administrator on 17/11/2559.
 */
public class InstantAutocomplete extends AutoCompleteTextView {

    public InstantAutocomplete(Context context) {
        super(context);
    }

    public InstantAutocomplete(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
    }

    public InstantAutocomplete(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        super.setOnFocusChangeListener(l);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    public void setDropDownWidth(int width) {
        super.setDropDownWidth(width);
    }

    @Override
    public boolean isSuggestionsEnabled() {
        return false;
    }
}
