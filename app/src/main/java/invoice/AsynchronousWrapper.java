package invoice;

import android.os.Bundle;

import invoice.ParcelQuery;

public class AsynchronousWrapper {

	private boolean required;

	private int RetrofitAbstractLayer;

	private Bundle instanceBundle;

	/** **/

	public AsynchronousWrapper() {}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public int getRetrofitAbstractLayer() {
		return RetrofitAbstractLayer;
	}

	public void setRetrofitAbstractLayer(int retrofitAbstractLayer) {
		RetrofitAbstractLayer = retrofitAbstractLayer;
	}

	public Bundle getInstanceBundle() {
		return instanceBundle;
	}

	public void setInstanceBundle(Bundle instanceBundle) {
		this.instanceBundle = instanceBundle;
	}
}
