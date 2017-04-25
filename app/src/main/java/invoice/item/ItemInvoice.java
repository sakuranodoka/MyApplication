package invoice.item;

public class ItemInvoice extends InvoiceBaseItem {

    public ItemInvoice(int type) { super(type); }

    public String invoicePreview;

    public String invoiceDate;

    public String invoiceLatitude;

    public String invoiceLongitude;

    public String invoiceUserID;

	 public String invoiceSubLocality;

	 public String invoiceLocality;

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
}
