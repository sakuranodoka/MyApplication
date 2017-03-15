package shopFinding;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 17/11/2559.
 */
public class ShopDetailPOJO {
    @SerializedName("shop_code")
    String shopId;

    @SerializedName("shop_name")
    String shopName;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    @Override
    public String toString() {
        return shopId + " : " + shopName;
    }
}
