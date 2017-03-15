package location.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 6/12/2559.
 */
public class MacAddressPOJO {
    @SerializedName("usr_id")
    private String usrId;

    @SerializedName("usr_mac_address")
    private String usrMacAddress;

    /**
     *
     * @return
     * The usrId
     */
    public String getUsrId() {
        return usrId;
    }

    /**
     *
     * @param usrId
     * The usr_id
     */
    public void setUsrId(String usrId) {
        this.usrId = usrId;
    }

    /**
     *
     * @return
     * The usrMacAddress
     */
    public String getUsrMacAddress() {
        return usrMacAddress;
    }

    /**
     *
     * @param usrMacAddress
     * The usr_mac_address
     */
    public void setUsrMacAddress(String usrMacAddress) {
        this.usrMacAddress = usrMacAddress;
    }

    @Override
    public String toString() {
        return usrMacAddress;
    }
}
