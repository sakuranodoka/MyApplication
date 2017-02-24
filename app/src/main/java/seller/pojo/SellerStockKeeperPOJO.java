package seller.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 8/2/2560.
 */

public class SellerStockKeeperPOJO {
    @SerializedName("collection")
    @Expose
    private String collection;
    @SerializedName("stock")
    @Expose
    private String stock;

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }
}
