package seller.titlebar;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 17/11/2559.
 */
public class SellerTitleDAO {
    @SerializedName("title")
    private String title;

    @SerializedName("id")
    private int id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return title;
    }
}
