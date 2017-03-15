package invoice.item;

import java.io.Serializable;

/**
 * Created by Administrator on 7/3/2560.
 */

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
