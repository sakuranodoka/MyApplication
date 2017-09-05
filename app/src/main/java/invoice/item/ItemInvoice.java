package invoice.item;

import com.google.gson.annotations.SerializedName;

import invoice.BillPOJO;

// ส่วนของอินเตอร์เฟส (ตัวแปร)
public class ItemInvoice extends InvoiceBaseItem {

    public ItemInvoice(int type) { super(type); }

	 public BillPOJO billPOJO;

    public String invoicePreview;

    public String invoiceDate;

    public String invoiceLatitude;

    public String invoiceLongitude;

    public String invoiceUserID;

	 public String invoiceSubLocality;

	 public String invoiceLocality;

	 public String BILL_NO;

	public BillPOJO getBillPOJO() {
		return billPOJO;
	}

	public void setBillPOJO(BillPOJO billPOJO) {
		this.billPOJO = billPOJO;
	}

	public String getInvoicePreview() {
        return invoicePreview;
    }

    public void setInvoicePreview(String invoicePreview) {
        this.invoicePreview = invoicePreview;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
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

	public String getInvoiceSubLocality() {
		return invoiceSubLocality;
	}

	public void setInvoiceSubLocality(String invoiceSubLocality) {
		this.invoiceSubLocality = invoiceSubLocality;
	}

	public String getInvoiceLocality() {
		return invoiceLocality;
	}

	public void setInvoiceLocality(String invoiceLocality) {
		this.invoiceLocality = invoiceLocality;
	}

	public String getBILL_NO() {
		return BILL_NO;
	}

	public void setBILL_NO(String BILL_NO) {
		this.BILL_NO = BILL_NO;
	}
}
