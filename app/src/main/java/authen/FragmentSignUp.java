package authen;

import android.app.ProgressDialog;
import android.content.Context;
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

import com.example.administrator.myapplication.AuthenActivity;
import com.example.administrator.myapplication.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fragment.FragmentScope;
import fragment.InterfaceReplace;
import okhttp3.ResponseBody;
import retrofit.InterfaceListen;
import retrofit.RetrofitAbstract;
import retrofit.ServiceRetrofit;
import retrofit2.Retrofit;

/**
 * Created by Administrator on 16/3/2560.
 */

public class FragmentSignUp extends Fragment {

	 private Bundle b;
	 private Bundle instanceState;

	 private static final String TAG = "LoginActivity";
	 private static final int REQUEST_SIGNUP = 0;

	 private EditText _nameText;
	 private EditText _usernameText;
	 private EditText _passwordText;
	 private Button _signupButton;

	 private ProgressDialog progressDialog;

	 InterfaceReplace interfaceReplace;

	 public FragmentSignUp() {
			super();
			this.b = null;
	 }

	 public FragmentSignUp(Bundle b, InterfaceReplace interfaceReplace) {
			super();
			this.b = b;
			this.interfaceReplace = interfaceReplace;
	 }

	 @Override
	 public void onSaveInstanceState(Bundle outState) {
			if(instanceState != null) {
				 //instanceState.putString();
				 //instanceState.putString();
				 //instanceState.putString();
				 outState.putAll(instanceState);
			}
			super.onSaveInstanceState(outState);
	 }

	 @Override
	 public void onActivityCreated(@Nullable Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			hideKeyBoard();
	 }

	 @Nullable
	 @Override
	 public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.layout_sign_up, container, false);
			ButterKnife.inject(rootView);
			if(savedInstanceState != null)
				instanceState = savedInstanceState;
			else
				instanceState = new Bundle();

			_nameText = (EditText) rootView.findViewById(R.id.input_name);
			_usernameText = (EditText) rootView.findViewById(R.id.input_username);
			_passwordText = (EditText) rootView.findViewById(R.id.input_password);
			_signupButton = (Button) rootView.findViewById(R.id.btn_signup);
			_signupButton.setOnClickListener(new View.OnClickListener() {
				 @Override
				 public void onClick(View v) {
						signup();
				 }
			});

			return rootView;
	 }

	 public void signup() {
			Log.d(TAG, "Signup");

			if (!validate()) {
			//	 onSignupFailed();
				 return;
			}

			_signupButton.setEnabled(false);

			progressDialog = new ProgressDialog(getActivity());
			progressDialog.setIndeterminate(true);
			progressDialog.setMessage("ดำเนินการสมัครสมาชิก...");
			progressDialog.show();

			String name = _nameText.getText().toString();
			String username = _usernameText.getText().toString();
			String password = _passwordText.getText().toString();

			instanceState.putString(AuthenData.USERNAME, username);
			instanceState.putString(AuthenData.PASSWORD, password);
			instanceState.putString(AuthenData.FULLNAME, name);

			// TODO: Implement your own signup logic here.

			new android.os.Handler().postDelayed(
							new Runnable() {
								 public void run() {
										async();
								 }
							}, 2000);
	 }

	 public boolean validate() {
			boolean valid = true;

			String name = _nameText.getText().toString();
			String username = _usernameText.getText().toString();
			String password = _passwordText.getText().toString();

			if (name.isEmpty()) {
				 _nameText.setError("กรุณากรอกชื่อ-สกุลของท่าน");
				 valid = false;
			} else {
				 _nameText.setError(null);
			}

			if (username.isEmpty() || username.length() < 4) {
				 _usernameText.setError("ชื่อบัญชีผู้ใช้ต้องมีมากกว่า 4 ตัวอักษรขึ้นไป");
				 valid = false;
			} else {
				 _usernameText.setError(null);
			}

			if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
				 _passwordText.setError("รหัสผ่านควรจะตั้งแต่ 4 ถึง 10 ตัวอักษร");
				 valid = false;
			} else {
				 _passwordText.setError(null);
			}

			return valid;
	 }

	 private void async() {
			new ServiceRetrofit().callServer(interfaceListen, RetrofitAbstract.RETROFIT_SIGN_UP, instanceState);
	 }

	 private InterfaceListen interfaceListen = new InterfaceListen() {
			@Override
			public void onResponse(Object data, Retrofit retrofit) {
				 progressDialog.dismiss();

				 List<AuthenticatePOJO> pojo = (List<AuthenticatePOJO>) data;

				 Log.e("status authen", pojo.get(0).getStatus());
				 if(pojo.get(0).getStatus().equals("success")) {
//						Bundle sc = new Bundle();
//						sc.putString(AuthenData.FULLNAME, pojo.get(0).getName());
//						sc.putString(AuthenData.USERNAME, pojo.get(0).getUsername());
						onSignUpSuccess();
				 } else {
						onSignUpFailed();
				 }
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

	 private void onSignUpSuccess() {
			_signupButton.setEnabled(true);
			Toast.makeText(getActivity(), "สมัครสมาชิกเสร็จสิ้น", Toast.LENGTH_LONG).show();
			interfaceReplace.replacing(FragmentScope.AUTH);
	 }

	 private void onSignUpFailed() {
			hideKeyBoard();
			Toast.makeText(getActivity(), "ไม่สามารถสมัครสมาชิกได้เนื่องจาก User นี้มีอยู่แล้ว", Toast.LENGTH_LONG).show();
			_signupButton.setEnabled(true);
	 }

	 private void hideKeyBoard() {
			final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
	 }

	 @Override
	 public void onAttach(Context context) {
			super.onAttach(context);
			if (context instanceof InterfaceReplace) {
				  interfaceReplace = ((InterfaceReplace) context);
			}
	 }
}
