package intent;

import org.parceler.Parcel;

import java.io.Serializable;

@Parcel
public class IntentParcel implements Serializable {
	public Class<?> to;
	public Class<?> from;

	public IntentParcel() {}

	public Class<?> getTo() {
		return to;
	}

	public void setTo(Class<?> to) {
		this.to = to;
	}

	public Class<?> getFrom() {
		return from;
	}

	public void setFrom(Class<?> from) {
		this.from = from;
	}
}
