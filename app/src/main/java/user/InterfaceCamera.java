package user;

import android.support.annotation.LayoutRes;
import android.view.View;

/**
 * Created by Administrator on 12/1/2560.
 */
public interface InterfaceCamera {
    public void onCapture();

    public void onBarcodeScan(int mode, String preview, @LayoutRes int layoutID);
}
