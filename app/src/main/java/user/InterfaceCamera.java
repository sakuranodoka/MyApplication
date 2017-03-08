package user;

import android.support.annotation.LayoutRes;
import android.view.View;

/**
 * Created by Administrator on 12/1/2560.
 */
public interface InterfaceCamera {
    public void onCapture();

    public void onBarcodeScan(int mode, @LayoutRes int layoutID);

    int BARCODE_INVOICE_SC = 7667;
    int BARCODE_PERSONAL_SC = 7632;

    String E_O_S = "end_of_scan";
}
