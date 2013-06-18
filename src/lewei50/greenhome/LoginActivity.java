package lewei50.greenhome;

import lewei50.entities.App;
import lewei50.helpers.HtmlHelper;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.R.bool;
import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

public class LoginActivity extends Activity {

	ProgressDialog MyDialog;

	private EditText textUserName;

	private EditText textPassword;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);

		textUserName = (EditText) findViewById(R.id.login_edit_account);
		textPassword = (EditText) findViewById(R.id.login_edit_pwd);
		loadDefaultUserNameAndPassword();

	}

	public void loadDefaultUserNameAndPassword() {
		SharedPreferences sharedPreferences = getSharedPreferences("config",
				MODE_PRIVATE);
		// SharedPreferences.Editor editor=sharedPreferences.edit();
		String usernameString = sharedPreferences.getString("username", "");
		String passwordString = sharedPreferences.getString("password", "");
		if (usernameString != "")
			textUserName.setText(usernameString);
		if (passwordString != "")
			textPassword.setText(passwordString);

	}

	public void setDefaultUserNameAndPassword() {
		SharedPreferences sharedPreferences = getSharedPreferences("config",
				MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("username", textUserName.getText().toString());
		editor.putString("password", textPassword.getText().toString());
		editor.commit();

	}

	public void loginClick(View view) {
		setDefaultUserNameAndPassword();
		// 验证是否联网
		ConnectivityManager cwjManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cwjManager.getActiveNetworkInfo();
		if (info != null && info.isAvailable()) {

			LoginAsyn taskAsyn = new LoginAsyn();
			taskAsyn.execute();

		} else {
			new AlertDialog.Builder(this).setIcon(R.drawable.warning)
					.setTitle("登录验证失败").setPositiveButton("确定", null)
					.setMessage("没有联网").create().show();

		}
	}

	App app;
	HtmlHelper hh;

	class LoginAsyn extends AsyncTask<String, String, Boolean> {

		protected Boolean doInBackground(String... params) {
			hh = new HtmlHelper();
			app = (App) getApplicationContext();

			app.CurrentSession = hh.Login(textUserName.getText().toString(),
					textPassword.getText().toString());
			if (app.CurrentSession.IsLogin)
				App.GatewayList = app.getGatewayList();
			return app.CurrentSession.IsLogin;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			MyDialog.cancel();
			if (result) {
				// 验证成功，打开传感器列表
				Intent intent = new Intent(LoginActivity.this,
						MainActivity.class);
				startActivity(intent);
			} else {
				Dialog alertDialog = new AlertDialog.Builder(LoginActivity.this)
						.setIcon(R.drawable.warning).setTitle("登录验证失败")
						.setPositiveButton("确定", null)
						.setMessage(app.CurrentSession.Message).create();
				alertDialog.show();
			}
		}

		@Override
		protected void onPreExecute() {
			MyDialog = new ProgressDialog(LoginActivity.this);
			MyDialog.setIndeterminateDrawable(getResources().getDrawable(
					R.drawable.progressbar));

			MyDialog.setIcon(null);
			MyDialog.setFeatureDrawableAlpha(Window.FEATURE_OPTIONS_PANEL, 0);
			MyDialog.setTitle("");
			MyDialog.setMessage("登录中，请稍候......");
			// MyDialog.setContentView(R.layout.layout_progressbar);
			MyDialog.show();

		}

	}
}
