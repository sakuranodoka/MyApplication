package invoice.item;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 7/3/2560.
 */

public class ParcelableInvoice implements Parcelable {

    private ArrayList<ItemInvoicePreview> listInvoice;
    private String invoiceLatitude;
    private String invoiceLongitude;
    private String invoiceUserID;
    private String invoiceUserImageBase64;

    // Main Constructor
//    public ParcelableInvoice(ArrayList<ItemInvoicePreview> listInvoice, String invoiceLatitude, String invoiceLongitude, String invoiceUserID, String invoiceUserImageBase64) {
//        this.listInvoice = listInvoice;
//        this.invoiceLatitude = invoiceLatitude;
//        this.invoiceLongitude = invoiceLongitude;
//        this.invoiceUserID = invoiceUserID;
//        this.invoiceUserImageBase64 = invoiceUserImageBase64;
//    }

    public ParcelableInvoice() {
        listInvoice = null;
    }

    protected ParcelableInvoice(Parcel in) {
        this.listInvoice = in.readArrayList(null);
        this.invoiceLatitude = in.readString();
        this.invoiceLongitude = in.readString();
        this.invoiceUserID = in.readString();
        this.invoiceUserImageBase64 = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.listInvoice);
        dest.writeString(this.invoiceLatitude);
        dest.writeString(this.invoiceLongitude);
        dest.writeString(this.invoiceUserID);
        dest.writeString(this.invoiceUserImageBase64);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ParcelableInvoice> CREATOR = new Creator<ParcelableInvoice>() {
        @Override
        public ParcelableInvoice createFromParcel(Parcel in) {
            return new ParcelableInvoice(in);
        }

        @Override
        public ParcelableInvoice[] newArray(int size) {
            return new ParcelableInvoice[size];
        }
    };

    public ArrayList<ItemInvoicePreview> getListInvoice() {
        return listInvoice;
    }

    public void setListInvoice(ArrayList<ItemInvoicePreview> listInvoice) {
        this.listInvoice = listInvoice;
    }

    public String getInvoiceLatitude() {
        return invoiceLatitude;
    }

    public void setInvoiceLatitude(String invoiceLatitude) {
        this.invoiceLatitude = invoiceLatitude;
    }

    public String getInvoiceLongitude() {
        return invoiceLongitude;
    }

    public void setInvoiceLongitude(String invoiceLongitude) {
        this.invoiceLongitude = invoiceLongitude;
    }

    public String getInvoiceUserID() {
        return invoiceUserID;
    }

    public void setInvoiceUserID(String invoiceUserID) {
        this.invoiceUserID = invoiceUserID;
    }

    public String getInvoiceUserImageBase64() {
        return invoiceUserImageBase64;
    }

    public void setInvoiceUserImageBase64(String invoiceUserImageBase64) {
        this.invoiceUserImageBase64 = invoiceUserImageBase64;
    }

}
