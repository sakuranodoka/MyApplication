package seller.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 18/11/2559.
 */
public class SellerCollectionPOJO {

    @SerializedName("data")
    @Expose
    private List<Data> data = new ArrayList<Data>();

    /**
     *
     * @return
     * The data
     */
    public List<Data> getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(List<Data> data) {
        this.data = data;
    }

    public static class Data {

        @SerializedName("collection")
        @Expose
        private String collection;
        @SerializedName("bal")
        @Expose
        private String bAL;
        @SerializedName("net")
        @Expose
        private String net;

        /**
         * @return The collection
         */
        public String getCollection() {
            return collection;
        }

        /**
         * @param collection The collection
         */
        public void setCollection(String collection) {
            this.collection = collection;
        }

        /**
         * @return The bAL
         */
        public String getBAL() {
            return bAL;
        }

        /**
         * @param bAL The BAL
         */
        public void setBAL(String bAL) {
            this.bAL = bAL;
        }

        /**
         * @return The net
         */
        public String getNet() {
            return net;
        }

        /**
         * @param net The net
         */
        public void setNet(String net) {
            this.net = net;
        }
    }
}
