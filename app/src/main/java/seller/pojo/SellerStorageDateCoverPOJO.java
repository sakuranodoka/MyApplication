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
    @SerializedName("candidateBal")
    @Expose
    private String candidateBal;

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
}
