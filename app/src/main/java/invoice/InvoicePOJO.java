package invoice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class InvoicePOJO {
    @SerializedName("info_invoice")
    @Expose
    public String infoInvoice;

    @SerializedName("info_time")
    @Expose
    public String infoTime;

    public String getInfoInvoice() {
        return infoInvoice;
    }

    public void setInfoInvoice(String infoInvoice) {
        this.infoInvoice = infoInvoice;
    }

    public String getInfoTime() {
        return infoTime;
    }

    public void setInfoTime(String infoTime) {
        this.infoTime = infoTime;
    }
}
