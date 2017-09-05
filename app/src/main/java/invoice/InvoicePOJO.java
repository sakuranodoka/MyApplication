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

	 @SerializedName("BILL_NO")
	 @Expose
	 public String BILL_NO = "";

	@SerializedName("TOTAL_BOX")
	@Expose
	public String TOTAL_BOX = "";

	@SerializedName("BILL_DATE")
	@Expose
	public String BILL_DATE = "";

	@SerializedName("NET_AMOUNT")
	@Expose
	public String NET_AMOUNT = "";

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

	public String getBILL_NO() {
		return BILL_NO;
	}

	public void setBILL_NO(String BILL_NO) {
		this.BILL_NO = BILL_NO;
	}

	public String getTOTAL_BOX() {
		return TOTAL_BOX;
	}

	public void setTOTAL_BOX(String TOTAL_BOX) {
		this.TOTAL_BOX = TOTAL_BOX;
	}

	public String getBILL_DATE() {
		return BILL_DATE;
	}

	public void setBILL_DATE(String BILL_DATE) {
		this.BILL_DATE = BILL_DATE;
	}

	public String getNET_AMOUNT() {
		return NET_AMOUNT;
	}

	public void setNET_AMOUNT(String NET_AMOUNT) {
		this.NET_AMOUNT = NET_AMOUNT;
	}
}
