package invoice;

import android.os.Bundle;

public interface InterfaceInvoiceInfo {
	// When date picker ...
	void onOptionalChange(String optionIDs);

	void onBarcodeScan(Bundle b);
}
