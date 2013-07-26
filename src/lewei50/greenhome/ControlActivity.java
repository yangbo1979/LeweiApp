package lewei50.greenhome;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import lewei50.entities.App;

import lewei50.entities.gateway;
import lewei50.entities.jDeviceInfo;
import lewei50.entities.jRequest;
import lewei50.entities.jResult;
import lewei50.entities.jSensor;
import lewei50.greenhome.R.id;
import lewei50.helpers.HtmlHelper;
import lewei50.helpers.UDPClient;
import lewei50.helpers.UDPServer;
import android.os.AsyncTask;
//import lewei50.helpers.UDPServer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ControlActivity extends Activity {

	public static final Gson gson = new Gson();

	UDPClient udpClient;

	EditText apiEditText;
	Spinner requestTypeSpinner;

	// jDeviceInfo device;

	App app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_control);

		app = (App) getApplicationContext();
		loadControl();

	}

	private void loadControl() {
		apiEditText = (EditText) this.findViewById(R.id.apiEditText);
		requestTypeSpinner = (Spinner) this
				.findViewById(R.id.requestTypeSpinner);
		String[] arrString = { "get", "post" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, arrString);
		// 将可选内容与ArrayAdapter连接
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 设置下拉列表的风格
		requestTypeSpinner.setAdapter(adapter);
		// 将adapter添加到Spinner中
		// 插入数据结束
		// 下面开始获取选中的值
		requestTypeSpinner.setSelection(0);
		loadDefaultAPI();

		MyDialog = new ProgressDialog(ControlActivity.this);
		MyDialog.setIndeterminateDrawable(getResources().getDrawable(
				R.drawable.progressbar));
		MyDialog.setIcon(null);
		MyDialog.setTitle("");

		searchDevice(null);
	}

	public void searchDevice(View view) {

		// 运行udpserver准备读取消息
		searchUDPAsyn sua = new searchUDPAsyn();
		sua.execute();

		// 发送udp请求，让支持的设备发送udp消息
		if (udpClient == null)
			udpClient = new UDPClient();
		udpClient.send("255.255.255.255", app.CurrentSession.UserName);

	}

	// 接收udp消息返回，不管是有没有找到都会
	public void searchUDPFinished(String[] result) {
		if (result != null) {
			// MyDialog.cancel();
			// device = result;
			apiEditText.setText(result[0]);
			if (result[1].equals("get"))
				requestTypeSpinner.setSelection(0);
			else
				requestTypeSpinner.setSelection(1);

			showList(null);
		}

	}

	public void showList(View view) {

		if (apiEditText.getText().toString().equals("")) {
			new AlertDialog.Builder(this).setIcon(R.drawable.warning)
					.setTitle("验证失败").setPositiveButton("确定", null)
					.setMessage("请输入正确的API地址").create().show();
		} else {
			getListAsyn getlist = new getListAsyn();
			getlist.execute();
		}

		setDefaultAPI();
	}

	public static List<jSensor> controllerList;

	public void loadDefaultAPI() {
		SharedPreferences sharedPreferences = getSharedPreferences("config",
				MODE_PRIVATE);
		// SharedPreferences.Editor editor=sharedPreferences.edit();
		String api = sharedPreferences.getString("api", "");
		int requestType = sharedPreferences.getInt("requestType", 0);
		if (api != "")
			apiEditText.setText(api);

		requestTypeSpinner.setSelection(requestType);

	}

	public void setDefaultAPI() {
		SharedPreferences sharedPreferences = getSharedPreferences("config",
				MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("api", apiEditText.getText().toString());
		editor.putInt("requestType",
				requestTypeSpinner.getSelectedItemPosition());
		editor.commit();

	}

	ProgressDialog MyDialog;

	// /查找设备api的异步任务
	class searchUDPAsyn extends AsyncTask<String, String, String[]> {

		protected String[] doInBackground(String... params) {

			String[] rsStrings = new String[] { "", "" };
			jDeviceInfo dd = null;

			UDPServer udp = new UDPServer();
			udp.run();
			udp.stop();

			if (udp.ReceiveString != null
					&& udp.ReceiveString.equals("") == false) {
				// 找到了api地址，理论上可用，就不用找别的了
				dd = jDeviceInfo.getDeviceInfo(udp.ReceiveString, udp.dPacket
						.getAddress().toString().replace("/", ""));
				rsStrings[0] = dd.getAPIAddress();
				rsStrings[1] = dd.requestType;
			} else {
				// MyDialog.setMessage("局域网内未发现可控网关，正在获取服务器上配置的API地址");
				// 继续找服务器上的api地址
				gateway gt = HtmlHelper.GetUserGateway();
				if (gt.internetAvailable)
					rsStrings[0] = gt.apiAddressInternet;
				else
					rsStrings[0] = gt.apiAddress;
				rsStrings[1] = "get";
			}

			return rsStrings;
		}

		@Override
		protected void onPostExecute(String[] result) {
			searchUDPFinished(result);
		}

		@Override
		protected void onPreExecute() {

			MyDialog.setMessage("正在查找可控网关API......");
			MyDialog.show();

		}

	}

	class getListAsyn extends AsyncTask<String, String, jResult<List<jSensor>>> {

		protected jResult<List<jSensor>> doInBackground(String... params) {
			HtmlHelper.API = apiEditText.getText().toString();
			HtmlHelper.RequestType = requestTypeSpinner
					.getSelectedItemPosition() == 0 ? "get" : "post";
			HtmlHelper.UserKey = app.CurrentSession.SessionKey;

			jResult<List<jSensor>> result = HtmlHelper.getAllSensors();

			return result;
		}

		@Override
		protected void onPostExecute(jResult<List<jSensor>> result) {
			MyDialog.hide();
			if (result.successful == false) {
				new AlertDialog.Builder(ControlActivity.this)
						.setIcon(R.drawable.warning).setTitle("读取失败")
						.setPositiveButton("确定", null)
						.setMessage(result.message).create().show();
			} else {
				Intent intent = new Intent(ControlActivity.this,
						ListActivity.class);
				intent.putExtra("requestType", requestTypeSpinner
						.getSelectedItem().toString());

//				System.out.println("result.data:");
//				System.out.println(result.data.toString());
				ControlActivity.controllerList = result.data;
				startActivity(intent);

			}
		}

		@Override
		protected void onPreExecute() {

			MyDialog.setMessage("正在读取可控设备列表......");
			MyDialog.show();

		}

	}
}
