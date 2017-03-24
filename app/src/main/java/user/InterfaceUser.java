package user;

import android.os.Bundle;

public interface InterfaceUser {
    void onCapture();

    void onBarcodeScan(int mode, String preview);

    void onIntentCallback(Class<?> target, Bundle callbackState);
}
