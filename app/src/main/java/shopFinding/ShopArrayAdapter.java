package shopFinding;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 17/11/2559.
 */
public class ShopArrayAdapter extends ArrayAdapter<ShopDetailPOJO> {
    private final Context context;
    private final List<ShopDetailPOJO> shopDetailPOJOs;
    private final List<ShopDetailPOJO> mShopDetailPOJO_All;
    private final List<ShopDetailPOJO> mShopDetailPOJO_suggestions;

    private final List<ShopDetailPOJO> temp_itemp;

    private final int resource;


    public ShopArrayAdapter(Context context, int resource, List<ShopDetailPOJO> shopDetailPOJOs) {
        super(context, resource, shopDetailPOJOs);
        this.context = context;
        this.resource = resource;

        this.shopDetailPOJOs = new ArrayList<>(shopDetailPOJOs);
        this.mShopDetailPOJO_All = new ArrayList<>(shopDetailPOJOs);
        this.mShopDetailPOJO_suggestions = new ArrayList<>();
        this.temp_itemp = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return shopDetailPOJOs ==null?0: shopDetailPOJOs.size();
    }

    @Override
    public ShopDetailPOJO getItem(int position) {
        return shopDetailPOJOs ==null?null: shopDetailPOJOs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            if (convertView == null) {
                LayoutInflater inflater = ((AppCompatActivity) this.context).getLayoutInflater();
                convertView = inflater.inflate(resource, parent, false);
            }
            ShopDetailPOJO sp = getItem(position);
            TextView name = (TextView) convertView.findViewById(R.id.searchTV);
            name.setText(sp.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    private boolean sQWzx = false;

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            public String convertResultToString(Object resultValue) {
                return ((ShopDetailPOJO) resultValue).toString();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint != null) {
                    mShopDetailPOJO_suggestions.clear();
                    temp_itemp.clear();
                    for (ShopDetailPOJO department : mShopDetailPOJO_All) {

                        if (department.toString().startsWith(constraint.toString().toLowerCase())) {
                            //newValues.add(value);
                            mShopDetailPOJO_suggestions.add(department);
                        } else {
                            // department = ชื่อร้านค้า
                            final String[] determinate = department.toString().split("-");

                            if(determinate.length > 0) {
                                for (int k = 0; k < determinate.length; k++) {
                                    if(k <= 1) {
                                        continue;
                                    }
                                    if (determinate[k].startsWith(constraint.toString())) {
                                        mShopDetailPOJO_suggestions.add(department);
                                        break;
                                    }
                                }
                            }

                            final String[] words = department.toString().split(" ");
                            final int wordCount = words.length;

                            sQWzx = true;

                            // Start at index 0, in case valueText starts with space(s)
                            for (int k = 0; k < wordCount; k++) {
                                // เว้น 1 ไว้เพื่อ รหัสร้านค้า
                                if(k <= 1) {
                                    continue;
                                }
                                if (words[k].startsWith(constraint.toString())) {
                                    //newValues.add(value);
                                    mShopDetailPOJO_suggestions.add(department);

                                    sQWzx = false;

                                    break;
                                }
                            }

                            // Develop = 0
                            if(sQWzx) {
                                for (int k = 0; k < wordCount; k++) {
                                    if(k <= 1) {
                                        continue;
                                    }
                                    if( words[k].indexOf(constraint.toString().charAt(0)) >= 0 ) {
                                        temp_itemp.add(department);
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    for (ShopDetailPOJO x : temp_itemp)
                        mShopDetailPOJO_suggestions.add(x);


                    FilterResults filterResults = new FilterResults();
                    filterResults.values = mShopDetailPOJO_suggestions;
                    filterResults.count = mShopDetailPOJO_suggestions.size();
                    return filterResults;
                } else {
                    return new FilterResults();
                }
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                shopDetailPOJOs.clear();
                if (results != null && results.count > 0) {
                    // avoids unchecked cast warning when using mDepartments.addAll((ArrayList<Department>) results.values);
                    List<?> result = (List<?>) results.values;
                    for (Object object : result) {
                        if (object instanceof ShopDetailPOJO) {
                            shopDetailPOJOs.add((ShopDetailPOJO) object);
                        }
                    }
                } else if (constraint == null) {
                    // no filter, add entire original list back in
                    shopDetailPOJOs.addAll(mShopDetailPOJO_All);
                }
                notifyDataSetChanged();
            }
        };
    }
}
