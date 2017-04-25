package invoice.item;

import java.io.Serializable;

public class ItemInvoicePreview implements Serializable {

	private String InvoicePreview;
	private String InvoiceDate;
	private String InvoiceLat;
	private String InvoiceLng;
	private String InvoiceSublocality;
	private String InvoiceLocality;

	public String getInvoicePreview() {
		return InvoicePreview;
	}
	public void setInvoicePreview(String invoicePreview) {
		InvoicePreview = invoicePreview;
	}
	public String getInvoiceDate() {
		return InvoiceDate;
	}
	public void setInvoiceDate(String invoiceDate) {
			InvoiceDate = invoiceDate;
	 }
	public String getInvoiceLat() {
		return InvoiceLat;
	}
	public void setInvoiceLat(String invoiceLat) {
		InvoiceLat = invoiceLat;
	}
	public String getInvoiceLng() {
		return InvoiceLng;
	}
	public void setInvoiceLng(String invoiceLng) {
		InvoiceLng = invoiceLng;
	}
	public String getInvoiceSublocality() {
		return InvoiceSublocality;
	}
	public void setInvoiceSublocality(String invoiceSublocality) {
		InvoiceSublocality = invoiceSublocality;
	}
	public String getInvoiceLocality() {
		return InvoiceLocality;
	}
	public void setInvoiceLocality(String invoiceLocality) {
		InvoiceLocality = invoiceLocality;
	}
}
