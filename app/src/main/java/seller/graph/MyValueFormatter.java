package seller.graph;

import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;


import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import seller.SellerData;

/**
 * Created by Administrator on 21/12/2559.
 */
public class MyValueFormatter implements ValueFormatter {

    private DecimalFormat mFormat;

    public MyValueFormatter() {
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        if(value >= 0.00 ) {
            String finalText = NumberFormat.getNumberInstance(Locale.US).format((int) value);

            return finalText + SellerData.graphUnit;
        } else {
            return "";
        }
    }
}
