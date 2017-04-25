package authen;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

public class AuthenMethod {

	@SuppressWarnings("static using")
	public static boolean setLogout(@NonNull SharedPreferences sp) {
		boolean status = false;
		try {
			SharedPreferences.Editor editor = sp.edit();
			editor.remove(AuthenData.USERNAME);
			editor.remove(AuthenData.FULLNAME);
			editor.remove(AuthenData.USER_ROLE);
			editor.apply();

			// Logout complete
			status = true;
		} catch (NullPointerException e ) {
			status = false;
		} catch (Exception e) {
			status = false;
		}
		return status;
	}
}
