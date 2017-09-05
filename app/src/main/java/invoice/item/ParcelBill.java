package invoice.item;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.io.Serializable;
import java.util.ArrayList;

import invoice.BillPOJO;

@Parcel
public class ParcelBill implements Serializable {

	public ParcelBill() {}

	@SerializedName("bill")
	@Expose
	public ArrayList<BillPOJO> listBill;

	public ArrayList<BillPOJO> getListBill() {
		return listBill;
	}

	public void setListBill(ArrayList<BillPOJO> listBill) {
		this.listBill = listBill;
	}
}
