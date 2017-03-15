package seller.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 18/11/2559.
 */
public class SellerBestSellerPOJO {
    @SerializedName("data")
    @Expose
    private List<Data> data = new ArrayList<Data>();

    /**
     * @return The data
     */
    public List<Data> getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("collection")
        @Expose
        private String collection;
        @SerializedName("net")
        @Expose
        private String net;
        @SerializedName("lastSumNet")
        @Expose
        private String lastSumNet;
        @SerializedName("bal")
        @Expose
        private String BAL;

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

        public String getBAL() {
            return BAL;
        }

        public void setBAL(String BAL) {
            this.BAL = BAL;
        }

        public String getLastSumNet() {
            return lastSumNet;
        }

        public void setLastSumNet(String lastSumNet) {
            this.lastSumNet = lastSumNet;
        }
    }
}
