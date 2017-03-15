package seller.graph;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.SyncStateContract;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.SellerSubActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.annotations.SerializedName;

import org.parceler.ParcelConstructor;
import org.parceler.Parcels;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import seller.InterfaceOnProductDetail;
import seller.SellerData;
import seller.TypeSellerReport;
import seller.item.ItemSellerBestSeller;
import seller.item.ItemSellerBestSellerMonthToDate;
import seller.item.ItemSellerCollection;
import seller.item.ItemSellerStockKeeper;
import seller.item.ItemSellerStorageDateCover;
import seller.viewholder.BarChartViewHolder;

/**
 * Created by Administrator on 30/11/2559.
 */

public class BarChartData {

    private float granularity = 5.5f;

    private BarData data;

    private final BarChart mChart;

    private final LinearLayout barChartLegend;

    private List<String> dataLabels = new ArrayList<>();

    public static boolean isValueSelected = false;

    public BarChartData(BarChartViewHolder vh) {
        this.barChartLegend = vh.barChartLegend;
        this.mChart = vh.barChart;
    }

    public void setData(Object dataObject) {

        if(dataObject != null) {

            XAxis xl = mChart.getXAxis();
            xl.setLabelCount(100);
            xl.setPosition(XAxis.XAxisPosition.BOTTOM);
            xl.setDrawAxisLine(true);
            xl.setDrawGridLines(false);
            xl.setGranularity(granularity);

            YAxis yl = mChart.getAxisLeft();
            yl.setDrawAxisLine(true);
            yl.setDrawGridLines(true);
            yl.setStartAtZero(true);

            YAxis yr = mChart.getAxisRight();
            yr.setDrawAxisLine(true);
            yr.setDrawGridLines(false);
            yr.setStartAtZero(true);

            mChart.setDescriptionTextSize(14f);

            mChart.setHighlightFullBarEnabled(false);

            mChart.setPinchZoom(true);

            mChart.setDoubleTapToZoomEnabled(false);

            mChart.getLegend().setEnabled(false);

            mChart.setClickable(false);

            mChart.animateXY(2000, 2000);

            mChart.setDrawBarShadow(false);

            mChart.setDrawValueAboveBar(true);

            mChart.setFitBars(true);

            mChart.invalidate();

            mChart.setData(getDataSet(dataObject));

            mChart.setOnChartValueSelectedListener(onChartValueSelectedListener);

            mChart.getXAxis().setValueFormatter(new LabelFormatter(dataLabels));

            mChart.getXAxis().setTextSize(15f);

            mChart.setData(getDataSet(dataObject));
        }
    }

    private BarData getDataSet(Object dataObject) {

        dataLabels = new ArrayList<>();

        float barWidth = 4.5f;
        float spaceForBar = 5.5f;

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals3 = new ArrayList<BarEntry>();

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();

        if(dataObject instanceof ItemSellerCollection) {

            SellerData.graphUnit = " ชิ้น";

            mChart.setDescription("กราฟแสดง Stock ของหน้าร้าน");

            ItemSellerCollection temp = (ItemSellerCollection) dataObject;

            if(temp.forGraph != null) {

                int i = 0;
                int j = 1;

                int next_index = 2;

                for (ItemSellerCollection _t_ : Reversed.reversed(temp.forGraph)) {

                    float secondDigit = -1f;

                    yVals2.add(new BarEntry(i * spaceForBar,  new float[] {0f, -1f}));
                    yVals1.add(new BarEntry(j * spaceForBar,  new float[] {((int) Float.parseFloat(_t_.getCollectionBal().replaceAll("[$,]", ""))), secondDigit} ));

                    j = j+next_index;
                    i = i+next_index;

                    dataLabels.add( "" );
                    dataLabels.add( "\n"+_t_.getCollectionItemCode() );
                }

                dataLabels.add("");

                resetCustomView();

                BarDataSet set1;
                set1 = new BarDataSet(yVals1, "");
                set1.setDrawValues(true);
                set1.setValueTextSize(15f);
                set1.setColor(ContextCompat.getColor(mChart.getContext(), R.color.supreme_blue));
                set1.setStackLabels(new String[]{"จำนวน Storage หน้าร้าน", ""});

                set1.setValueTextColor(ContextCompat.getColor(mChart.getContext(),R.color.supreme_blue));
                legendCustom("จำนวน Storage หน้าร้าน", ContextCompat.getColor(mChart.getContext(), R.color.supreme_blue));

                dataSets.add(set1);
            }
        } else if(dataObject instanceof ItemSellerBestSeller) {

            SellerData.graphUnit = mChart.getContext().getString(R.string.currency_th);

            mChart.setDescription( mChart.getContext().getString(R.string.seller_barchart_best_seller_title) );

            ItemSellerBestSeller temp = (ItemSellerBestSeller) dataObject;

            if(temp.forGraph != null) {

                int i = 0;
                int j = 1;
                int k = 2;

                int next_index = 2;

                for (ItemSellerBestSeller _t_ : Reversed.reversed(temp.forGraph)) {

                    float firstDigit = -1f;
                    float secondDigit = -1f;

                    yVals1.add(new BarEntry(j * spaceForBar, new float[] {Float.parseFloat( _t_.getCollectionNet().replaceAll("[$,]", "")), firstDigit}));
                    yVals2.add(new BarEntry(i * spaceForBar,  new float[] {Float.parseFloat( _t_.getCollectionLastSumNet().replaceAll("[$,]", "")), secondDigit}));
                    yVals3.add(new BarEntry(k * spaceForBar,  new float[] {0f, -1f}));

                    j = j+next_index;
                    i = i+next_index;
                    k = k+next_index;

                    dataLabels.add("");
                    dataLabels.add( _t_.getCollectionItemCode() );
                }
                dataLabels.add("");

                resetCustomView();

                String label1 = mChart.getContext().getString(R.string.seller_barchart_now);
                BarDataSet set1;
                set1 = new BarDataSet(yVals1, "");
                set1.setDrawValues(true);
                set1.setValueTextSize(15f);
                set1.setStackLabels(new String[]{label1, ""});
                set1.setColor(ContextCompat.getColor(mChart.getContext(), R.color.supreme_blue));
                set1.setValueTextColor(ContextCompat.getColor(mChart.getContext(),R.color.supreme_blue));
                legendCustom(label1, ContextCompat.getColor(mChart.getContext(), R.color.supreme_blue));

                String label2 = null;

                switch( temp.getBestSellerCandidateType() ) {
                    case ChartCandidateType.TYPE_30_CANDIDATE:
                        label2 = mChart.getContext().getString(R.string.seller_barchart_30);
                        break;
                    case ChartCandidateType.TYPE_60_CANDIDATE:
                        label2 = mChart.getContext().getString(R.string.seller_barchart_60);
                        break;
                    case ChartCandidateType.TYPE_180_CANDIDATE:
                        label2 = mChart.getContext().getString(R.string.seller_barchart_180);
                        break;
                    default:
                        label2 = mChart.getContext().getString(R.string.seller_barchart_30);
                        break;
                }

                BarDataSet set2;
                set2 = new BarDataSet(yVals2, "");
                set2.setDrawValues(true);
                set2.setValueTextSize(15f);
                set2.setStackLabels(new String[]{label2, ""});
                set2.setColor(ContextCompat.getColor(mChart.getContext(), R.color.deepest_orange));
                set2.setValueTextColor(ContextCompat.getColor(mChart.getContext(),R.color.deepest_orange));
                legendCustom(label2, ContextCompat.getColor(mChart.getContext(), R.color.deepest_orange));

                dataSets.add(set1);
                dataSets.add(set2);
            }
        } else if(dataObject instanceof ItemSellerBestSellerMonthToDate) {
            SellerData.graphUnit = mChart.getContext().getString(R.string.currency_th);

            mChart.setDescription("");

            ItemSellerBestSellerMonthToDate temp = (ItemSellerBestSellerMonthToDate) dataObject;

            if(temp.forGraph != null) {

                int i = 0;
                int j = 1;

                int next_index = 2;

                for (ItemSellerBestSellerMonthToDate _t_ : Reversed.reversed(temp.forGraph)) {

                    float secondDigit = -1f;

                    yVals2.add(new BarEntry(i * spaceForBar,  new float[] {0f, -1f}));
                    yVals1.add(new BarEntry(j * spaceForBar,  new float[] {((int) Float.parseFloat(_t_.collectionNet.replaceAll("[$,]", ""))), secondDigit} ));

                    j = j+next_index;
                    i = i+next_index;

                    dataLabels.add( "" );
                    dataLabels.add( _t_.getCollectionItemCode() );
                }

                dataLabels.add("");

                resetCustomView();

                BarDataSet set1;
                set1 = new BarDataSet(yVals1, "");
                set1.setDrawValues(true);
                set1.setValueTextSize(15f);
                set1.setColor(ContextCompat.getColor(mChart.getContext(), R.color.supreme_blue));
                set1.setStackLabels(new String[]{"", ""});

                set1.setValueTextColor(ContextCompat.getColor(mChart.getContext(),R.color.supreme_blue));
                legendCustom("Month to Date", ContextCompat.getColor(mChart.getContext(), R.color.supreme_blue));

                dataSets.add(set1);
            }
        } else if (dataObject instanceof ItemSellerStorageDateCover) {
            SellerData.graphUnit = mChart.getContext().getString(R.string.currency_date_th);

            mChart.setDescription("");

            ItemSellerStorageDateCover temp = (ItemSellerStorageDateCover) dataObject;

            if(temp.forGraph != null) {

                int i = 0;
                int j = 1;

                int next_index = 2;

                for (ItemSellerStorageDateCover _t_ : Reversed.reversed(temp.forGraph)) {

                    float secondDigit = -1f;

                    yVals2.add(new BarEntry(i * spaceForBar, new float[]{0f, -1f}));
                    yVals1.add(new BarEntry(j * spaceForBar, new float[]{((int) Float.parseFloat(_t_.getCollectionBal().replaceAll("[$,]", "")) ), secondDigit}));

                    j = j+next_index;
                    i = i+next_index;

                    dataLabels.add( "" );
                    dataLabels.add( _t_.getCollectionItemCode() );
                }

                dataLabels.add("");

                resetCustomView();

                BarDataSet set1;
                set1 = new BarDataSet(yVals1, "");
                set1.setDrawValues(true);
                set1.setValueTextSize(15f);
                set1.setStackLabels(new String[]{"", ""});
                set1.setColor(ContextCompat.getColor(mChart.getContext(), R.color.supreme_blue));

                set1.setValueTextColor(ContextCompat.getColor(mChart.getContext(),R.color.supreme_blue));
                legendCustom("จำนวน Day Cover (วัน)", ContextCompat.getColor(mChart.getContext(), R.color.supreme_blue));

                dataSets.add(set1);
            }
        } else if(dataObject instanceof ItemSellerStockKeeper) {

            SellerData.graphUnit = mChart.getContext().getString(R.string.currency_piece_th);

            mChart.setDescription("");

            ItemSellerStockKeeper temp = (ItemSellerStockKeeper) dataObject;

            if(temp.forGraph != null) {

                int i = 0;
                int j = 1;

                int next_index = 2;

                for (ItemSellerStockKeeper _t_ : Reversed.reversed(temp.forGraph)) {

                    float secondDigit = -1f;

                    yVals2.add(new BarEntry(i * spaceForBar, new float[]{0f, -1f}));
                    yVals1.add(new BarEntry(j * spaceForBar, new float[]{((int) Float.parseFloat(_t_.getStock().replaceAll("[$,]", ""))), secondDigit}));

                    j = j + next_index;
                    i = i + next_index;

                    dataLabels.add("");
                    dataLabels.add(_t_.getCollection());
                }

                dataLabels.add("");

                resetCustomView();

                BarDataSet set1;
                set1 = new BarDataSet(yVals1, "");
                set1.setDrawValues(true);
                set1.setValueTextSize(15f);
                set1.setStackLabels(new String[]{"", ""});
                set1.setColor(ContextCompat.getColor(mChart.getContext(), R.color.supreme_blue));

                set1.setValueTextColor(ContextCompat.getColor(mChart.getContext(), R.color.supreme_blue));
                legendCustom("จำนวนสินค้า (ชิ้น)", ContextCompat.getColor(mChart.getContext(), R.color.supreme_blue));

                dataSets.add(set1);
            }
        } else if (data == null) {

        }

        data = new BarData(dataSets);
        data.setValueFormatter(new MyValueFormatter());
        data.setValueTextSize(11.5f);
        data.setBarWidth(barWidth);

        return data;
    }

    public class LabelFormatter implements AxisValueFormatter {
        private final List<String>  mLabels;

        public LabelFormatter(List<String> labels) {
            mLabels = labels;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            if((int) Math.floor(value/granularity) < mLabels.size() ) {
                if((int) Math.floor(value/granularity) < 0) {
                    return "";
                } else {
                    return mLabels.get((int) Math.floor(value / granularity));
                }
            } else {
                return "";
            }
        }

        @Override
        public int getDecimalDigits() {
            return 0;
        }
    }

    public static class Reversed<T> implements Iterable<T> {
        private final List<T> original;

        public Reversed(List<T> original) {
            this.original = original;
        }

        public Iterator<T> iterator() {
            final ListIterator<T> i = original.listIterator(original.size());

            return new Iterator<T>() {
                public boolean hasNext() { return i.hasPrevious(); }
                public T next() { return i.previous(); }
                public void remove() { i.remove(); }
            };
        }

        public static <T> Reversed<T> reversed(List<T> original) {
            return new Reversed<T>(original);
        }
    }

    public void resetCustomView() {
        if((barChartLegend).getChildCount() > 0)
            (barChartLegend).removeAllViews();
    }

    public void legendCustom(String text, int color) {
        LinearLayout.LayoutParams parms_left_layout = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        parms_left_layout.weight = 1F;
        LinearLayout left_layout = new LinearLayout(mChart.getContext());
        left_layout.setOrientation(LinearLayout.HORIZONTAL);
        left_layout.setGravity(Gravity.CENTER);
        left_layout.setLayoutParams(parms_left_layout);

        LinearLayout.LayoutParams parms_legen_layout = new LinearLayout.LayoutParams(
                20, 20);
        parms_legen_layout.setMargins(0, 0, 20, 0);
        LinearLayout legend_layout = new LinearLayout(mChart.getContext());
        legend_layout.setLayoutParams(parms_legen_layout);
        legend_layout.setOrientation(LinearLayout.HORIZONTAL);
        legend_layout.setBackgroundColor(color);
        left_layout.addView(legend_layout);

        TextView txt_unit = new TextView(mChart.getContext());
        txt_unit.setText(text);
        left_layout.addView(txt_unit);

        barChartLegend.addView(left_layout);
    }

    // Call Back ทั้งหลายแหล่่ --------------------------->

    // Call back เมื่อรายละเอียดของผลิตภัณฑ์ถูกกระทำ
    public static InterfaceOnProductDetail setUpDialogDetail = new InterfaceOnProductDetail() {

        @Override
        public void onProductDetailDestroy() {
            isValueSelected = false;
        }
    };

    // Call back เมื่อ แท่ง ถูกคลิก
    private OnChartValueSelectedListener onChartValueSelectedListener = new OnChartValueSelectedListener() {
        @Override
        public void onValueSelected(Entry e, Highlight h) {

            if( h.getStackIndex() == 0) {

                float XAxisIndex = h.getX()%2;

                int numberOfBar = 2;

                int indexValue = (int) Math.floor(h.getX()/granularity);

                int finalIndex = 0;

                Log.e("dataSet", h.getDataSetIndex()+"");

                SellerData.dataSetNumber = h.getDataSetIndex();

                switch( indexValue % 2 ) {
                    case 0:
                        if(indexValue+1 <= dataLabels.size()) {
                            finalIndex = indexValue+1;
                            Log.e("EntryValue", dataLabels.get( indexValue+1 ).toString());

                            // เก็บ data set index แล้วส่งไปเรียก รหัส 6 หลักจาก เซิร์ฟเวอร์
                        } else {
                            Log.e("error", "......");
                        }
                        break;
                    case 1:
                        if(indexValue <= dataLabels.size()) {
                            finalIndex = indexValue;
                            Log.e("EntryValue", dataLabels.get( indexValue ).toString());

                        } else {
                            Log.e("error", "......");
                        }
                        break;
                    default:break;
                }

                if( !isValueSelected ) {
                    isValueSelected = true;

//                    Intent i = new Intent();
//                    Bundle b = new Bundle();
//
//                    int reportType = SellerData.reportId;
//                    b.putInt("reportId", reportType);
//
//                    String item = dataLabels.get( finalIndex ).toString();
//                    b.putString("item", item);
//
//                    String shopCode = SellerData.shopCode;
//                    b.putString("shopCode", shopCode);
//
//                    i.putExtras(b);
//                    i.setClass(mChart.getContext(), SellerSubActivity.class);
//
//                    mChart.getContext().startActivity(i);

                    //ViewProductDetailDialogFragment newFragment = new ViewProductDetailDialogFragment( SellerData.reportId, dataLabels.get( finalIndex ).toString(), SellerData.shopCode, setUpDialogDetail);
                    //newFragment.show(((SellerActivity) mChart.getContext()).getSupportFragmentManager(), "Open Dialog");
                }
            }
        }

        @Override
        public void onNothingSelected() {}
    };
}
