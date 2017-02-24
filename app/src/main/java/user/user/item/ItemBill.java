package user.user.item;

import java.util.List;

/**
 * Created by Administrator on 23/2/2560.
 */

public class ItemBill {
    private List<String> billCode;
    private String userId;
    private String userSignature;

    public ItemBill() {
        this.billCode = null;
        this.userId = null;
        this.userSignature = null;
    }

    public List<String> getBillCode() {
        return billCode;
    }

    public void setBillCode(List<String> billCode) {
        this.billCode = billCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserSignature() {
        return userSignature;
    }

    public void setUserSignature(String userSignature) {
        this.userSignature = userSignature;
    }
}
