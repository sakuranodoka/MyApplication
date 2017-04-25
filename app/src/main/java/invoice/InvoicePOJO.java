package invoice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class InvoicePOJO {
    @SerializedName("info_invoice")
    @Expose
    public String infoInvoice = "";

    @SerializedName("info_time")
    @Expose
    public String infoTime = "";

    @SerializedName("info_latitude")
    @Expose
    public String infoLatitude = "";

    @SerializedName("info_longitude")
    @Expose
    public String infoLongitude = "";

	 @SerializedName("info_sublocality")
	 @Expose
	 public String infoSubLocality = "";

	 @SerializedName("info_locality")
	 @Expose
	 public String infoLocality = "";

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

    public String getInfoLatitude() {
        return infoLatitude;
    }

    public void setInfoLatitude(String infoLatitude) {
        this.infoLatitude = infoLatitude;
    }

    public String getInfoLongitude() {
        return infoLongitude;
    }

    public void setInfoLongitude(String infoLongitude) {
        this.infoLongitude = infoLongitude;
    }

	public String getInfoSubLocality() {
		return infoSubLocality;
	}

	public void setInfoSubLocality(String infoSubLocality) {
		this.infoSubLocality = infoSubLocality;
	}

	public String getInfoLocality() {
		return infoLocality;
	}

	public void setInfoLocality(String infoLocality) {
		this.infoLocality = infoLocality;
	}
}
