package invoice;

/**
 * Created by Administrator on 19/10/2560.
 */

public class BarcodeWrapper {
	private BillPOJO billPOJO;
	private int position;

	public BarcodeWrapper() {
	}

	public BillPOJO getBillPOJO() {
		return billPOJO;
	}

	public void setBillPOJO(BillPOJO billPOJO) {
		this.billPOJO = billPOJO;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
}
