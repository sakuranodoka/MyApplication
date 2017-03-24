package invoice.item;

import java.util.List;

public class ItemInvoiceDateDropdown extends InvoiceBaseItem {
	public ItemInvoiceDateDropdown(int type) {
		super(type);
	}

	public List<ItemInvoiceDateDAO> dateTag;

	public List<ItemInvoiceDateDAO> getDateTag() {
		return dateTag;
	}

	public void setDateTag(List<ItemInvoiceDateDAO> dateTag) {
		this.dateTag = dateTag;
	}
}
