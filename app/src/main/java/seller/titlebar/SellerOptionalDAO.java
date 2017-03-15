package seller.titlebar;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 28/12/2559.
 */
public class SellerOptionalDAO {
    @SerializedName("optional")
    private String optional;

    @SerializedName("id")
    private int id;

    public String getOptional() {
        return optional;
    }

    public void setOptional(String optional) {
        this.optional = optional;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return optional;
    }
}
