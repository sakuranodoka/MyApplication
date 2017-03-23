package authen;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthenticatePOJO {
	 @SerializedName("status")
	 @Expose
	 public String status;

	 @SerializedName("username")
	 @Expose
	 public String username;

	 @SerializedName("name")
	 @Expose
	 public String name;

	 public String getStatus() {
			return status;
	 }

	 public void setStatus(String status) {
			this.status = status;
	 }

	 public String getUsername() {
			return username;
	 }

	 public void setUsername(String username) {
			this.username = username;
	 }

	 public String getName() {
			return name;
	 }

	 public void setName(String name) {
			this.name = name;
	 }
}
