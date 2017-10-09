package invoice;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.io.Serializable;

@Parcel
public class ParcelQuery implements Serializable {
	public String bill;
	public String datetime;

	public boolean increaseOne = false;

	public ParcelQuery() {
	}

	public String getBill() {
		return bill;
	}

	public void setBill(String bill) {
		this.bill = bill;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public boolean isIncreaseOne() {
		return increaseOne;
	}

	public void setIncreaseOne(boolean increaseOne) {
		this.increaseOne = increaseOne;
	}
}
