package seller.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 20/1/2560.
 */
public class SellerStorageDateCoverPOJO {
    @SerializedName("collection")
    @Expose
    private String collection;

    @SerializedName("dayCover")
    @Expose

    private String bal;
    @SerializedName("bal")
    @Expose
    private String candidateBal;

    @SerializedName("storage")
    @Expose
    private String storage;

    @SerializedName("XBar")
    @Expose
    private String XBar;

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getBal() {
        return bal;
    }

    public void setBal(String bal) {
        this.bal = bal;
    }

    public String getCandidateBal() {
        return candidateBal;
    }

    public void setCandidateBal(String candidateBal) {
        this.candidateBal = candidateBal;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getXBar() {
        return XBar;
    }

    public void setXBar(String XBar) {
        this.XBar = XBar;
    }
}
