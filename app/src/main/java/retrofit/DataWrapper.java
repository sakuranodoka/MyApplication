package retrofit;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 17/3/2560.
 */

public class DataWrapper {
	 @SerializedName("status")
	 @Expose
	 public String status;

	 public String getStatus() {
			return status;
	 }

	 public void setStatus(String status) {
			this.status = status;
	 }
}
