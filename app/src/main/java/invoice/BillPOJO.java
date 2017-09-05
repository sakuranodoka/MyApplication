package invoice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BillPOJO implements Serializable {
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
