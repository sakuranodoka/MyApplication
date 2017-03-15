package seller.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 18/1/2560.
 */
public class SellerBestSellerMonthToDatePOJO {

    @SerializedName("collection")
    @Expose
    private String collection;
    @SerializedName("net")
    @Expose
    private String net;

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getNet() {
        return net;
    }

    public void setNet(String net) {
        this.net = net;
    }

}
