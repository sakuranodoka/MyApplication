package invoice.item;

import java.io.Serializable;

public class ItemInvoicePreview implements Serializable {

	 private String InvoicePreview;
	 private String InvoiceDate;

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
}
