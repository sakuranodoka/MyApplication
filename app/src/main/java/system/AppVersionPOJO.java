package system;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AppVersionPOJO {
	@SerializedName("status")
	@Expose
	private String status;
	@SerializedName("reupdate")
	@Expose
	private Boolean reupdate;
	@SerializedName("url")
	@Expose
	private String url;
	@SerializedName("VERSION_NAME")
	@Expose
	private List<VERSIONNAME> vERSIONNAME = null;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getReupdate() {
		return reupdate;
	}

	public void setReupdate(Boolean reupdate) {
		this.reupdate = reupdate;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<VERSIONNAME> getVERSIONNAME() {
		return vERSIONNAME;
	}

	public void setVERSIONNAME(List<VERSIONNAME> vERSIONNAME) {
		this.vERSIONNAME = vERSIONNAME;
	}

	public class VERSIONNAME {

		@SerializedName("name")
		@Expose
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
