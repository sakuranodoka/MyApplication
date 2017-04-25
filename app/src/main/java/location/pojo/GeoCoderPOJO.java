package location.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GeoCoderPOJO {
	@SerializedName("results")
	@Expose
	private List<Result> results = null;

	public List<Result> getResults() {
		return results;
	}

	public void setResults(List<Result> results) {
		this.results = results;
	}

	public class Result {

		@SerializedName("address_components")
		@Expose
		private List<AddressComponent> addressComponents = null;

		public List<AddressComponent> getAddressComponents() {
			return addressComponents;
		}

		public void setAddressComponents(List<AddressComponent> addressComponents) {
			this.addressComponents = addressComponents;
		}

		public class AddressComponent {

			@SerializedName("long_name")
			@Expose
			private String longName;
			@SerializedName("short_name")
			@Expose
			private String shortName;
			@SerializedName("types")
			@Expose
			private List<String> types = null;

			public String getLongName() {
				return longName;
			}

			public void setLongName(String longName) {
				this.longName = longName;
			}

			public String getShortName() {
				return shortName;
			}

			public void setShortName(String shortName) {
				this.shortName = shortName;
			}

			public List<String> getTypes() {
				return types;
			}

			public void setTypes(List<String> types) {
				this.types = types;
			}
		}
	}
}
