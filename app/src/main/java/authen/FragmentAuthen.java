package authen;

import android.app.ProgressDialog;
import android.content.Context;
import android.icu.text.DisplayContext;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;

//import butterknife.ButterKnife;
//import butterknife.InjectView;
import fragment.FragmentScope;
import fragment.InterfaceReplace;
import okhttp3.ResponseBody;
import retrofit.DataWrapper;
import retrofit.InterfaceListen;
import retrofit.RetrofitAbstract;
import retrofit.ServiceRetrofit;
import retrofit2.Retrofit;

public class FragmentAuthen extends Fragment {
	private Bundle instanceState;

	private static final String TAG = "LoginActivity";

	private EditText _emailText;
	private EditText _passwordText;
	private Button _loginButton;

	private TextView _signupLink;

	private ProgressDialog progressDialog;

	private InterfaceReplace interfaceReplace;

	public FragmentAuthen() {
		super();
	}

	public FragmentAuthen(Bundle b, InterfaceReplace interfaceReplace) {
		super();
		this.interfaceReplace = interfaceReplace;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putAll(instanceState);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		hideKeyBoard();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_authen, container, false);

		//ButterKnife.inject(rootView);

		rootView.findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
		login();
		}
		});

		_emailText = (EditText) rootView.findViewById(R.id.input_email);
		_passwordText = (EditText) rootView.findViewById(R.id.input_password);

		if(savedInstanceState != null) {
			_emailText.setText(savedInstanceState.getString(AuthenData.USERNAME));
			_passwordText.setText(savedInstanceState.getString(AuthenData.PASSWORD));
			instanceState = savedInstanceState;
		} else {
			instanceState = new Bundle();
		}

		_loginButton = (Button) rootView.findViewById(R.id.btn_login);
		//_signupLink = (TextView) rootView.findViewById(R.id.link_signup);
//		_signupLink.setOnClickListener(new View.OnClickListener() {
//		@Override
//		public void onClick(View v) {
//		interfaceReplace.replacing(FragmentScope.SIGN_UP);
//		}
//		});

		return rootView;
	}

	public void login() {
		Log.d(TAG, "Login");

		if(!validate()) {
			onLoginFailed();
			return;
		}
		_loginButton.setEnabled(false);

		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setIndeterminate(true);
		progressDialog.setMessage("กำลังเข้าสู่ระบบ...");
		progressDialog.show();

		if(instanceState != null) {
			instanceState.putString(AuthenData.USERNAME, _emailText.getText().toString());
			instanceState.putString(AuthenData.PASSWORD, _passwordText.getText().toString());
		}

		// TODO: Implement your own authentication logic here.

		new android.os.Handler().postDelayed(
		new Runnable() {
			public void run() {
				async();
			}
		}, 2000);
	}

	public boolean validate() {
		boolean valid = true;
		String email = _emailText.getText().toString();
		String password = _passwordText.getText().toString();

		if(email.isEmpty()) {
			_emailText.setError("กรุณากรอก Username");
			valid = false;
		} else
			_emailText.setError(null);

		if(password.isEmpty()) {// || password.length() < 4 || password.length() > 10) {
			_passwordText.setError("กรุณากรอก Password ");
			valid = false;
		} else
			_passwordText.setError(null);
		return valid;
	}

	public void onLoginSuccess(Bundle sc) {
		_loginButton.setEnabled(true);
		Toast.makeText(getContext(), "ยินดีต้อนรับ", Toast.LENGTH_LONG).show();
		interfaceReplace.onLoginSuccess(sc);
	}

	public void onLoginFailed() {
		hideKeyBoard();
		Toast.makeText(getContext(), "ชื่อผู้ใช้หรือรหัสผ่านผิดพลาด", Toast.LENGTH_LONG).show();
		_loginButton.setEnabled(true);
	}

	private void hideKeyBoard() {
		final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
	}

	private void async() {
		new ServiceRetrofit().callServer(interfaceListen, RetrofitAbstract.RETROFIT_AUTHEN, instanceState);
	}

	private InterfaceListen interfaceListen = new InterfaceListen() {
		@Override
		public void onResponse(Object data, Retrofit retrofit) {
			progressDialog.dismiss();

			List<AuthenticatePOJO> pojo = (List<AuthenticatePOJO>) data;

			Log.e("status authen", pojo.get(0).getStatus());
			if(pojo.get(0).getStatus().equals("success")) {
				Bundle sc = new Bundle();
				sc.putString(AuthenData.FULLNAME, pojo.get(0).getName());
				sc.putString(AuthenData.USERNAME, pojo.get(0).getUsername());
				sc.putInt(AuthenData.USER_ROLE, pojo.get(0).getUser_role());
				onLoginSuccess(sc);
			} else
				onLoginFailed();

		}

		@Override
		public void onBodyError(ResponseBody responseBodyError) {

		}

		@Override
		public void onBodyErrorIsNull() {

		}

		@Override
		public void onFailure(Throwable t) {

		}
	};

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof InterfaceReplace)
			interfaceReplace = ((InterfaceReplace) context);
	}
}