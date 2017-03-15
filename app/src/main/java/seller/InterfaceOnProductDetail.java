package seller;

import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 5/1/2560.
 */
public interface InterfaceOnProductDetail extends Serializable {
    public void onProductDetailDestroy();
}
