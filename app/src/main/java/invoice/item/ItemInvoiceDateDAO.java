package invoice.item;

import com.google.gson.annotations.SerializedName;

public class ItemInvoiceDateDAO {
	@SerializedName("title")
	private String title;

	@SerializedName("id")
	private String id;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return title;
	}
}
