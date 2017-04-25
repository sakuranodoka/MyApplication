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

	@SerializedName("user_role")
	@Expose
	public int user_role;

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

	public int getUser_role() {
		return user_role;
	}

	public void setUser_role(int user_role) {
		this.user_role = user_role;
	}
}
