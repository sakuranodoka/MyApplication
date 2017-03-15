package seller.titlebar;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 28/12/2559.
 */
public class SellerRangeDAO {
    @SerializedName("range")
    private String range;

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    @Override
    public String toString() {
        return range;
    }
}
