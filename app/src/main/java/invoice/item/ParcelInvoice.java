package invoice.item;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 7/3/2560.
 */

@Parcel
public class ParcelInvoice implements Serializable {

	 @SerializedName("test")
	 public ArrayList<ItemInvoicePreview> listInvoice;
	 public String latitude;
	 public String longitude;
	 public String userID;
	 public String bitmap;
	 public String userFullName;
	 public String username;

	 public ParcelInvoice() {}

//	 public ParcelInvoice(ArrayList<ItemInvoicePreview> listInvoice, String latitude, String longitude, String userID, String bitmap) {
//			this.listInvoice = listInvoice;
//			this.latitude = latitude;
//			this.longitude = longitude;
//			this.userID = userID;
//			this.bitmap = bitmap;
//	 }

	 public ArrayList<ItemInvoicePreview> getListInvoice() {
			return listInvoice;
	 }

	 public void setListInvoice(ArrayList<ItemInvoicePreview> listInvoice) {
			this.listInvoice = listInvoice;
	 }

	 public String getLatitude() {
			return latitude;
	 }

	 public void setLatitude(String latitude) {
			this.latitude = latitude;
	 }

	 public String getLongitude() {
			return longitude;
	 }

	 public void setLongitude(String longitude) {
			this.longitude = longitude;
	 }

	 public String getUserID() {
			return userID;
	 }

	 public void setUserID(String userID) {
			this.userID = userID;
	 }

	 public String getBitmap() {
			return bitmap;
	 }

	 public void setBitmap(String bitmap) {
			this.bitmap = bitmap;
	 }

	 public String getUserFullName() {
			return userFullName;
	 }

	 public void setUserFullName(String userFullName) {
			this.userFullName = userFullName;
	 }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void clearData() {
			listInvoice.clear();
			latitude = "0.00";
			longitude = "0.00";
			userID = "-";
			bitmap = "";
			userFullName = "";
			this.username = "";
	 }
}
