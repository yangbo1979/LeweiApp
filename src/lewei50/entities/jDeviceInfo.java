package lewei50.entities;

import java.io.Serializable;

import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.string;

public class jDeviceInfo implements Serializable {

	public String ip;

	public String deviceName;

	public String username;

	public int port;

	public String api;

	public String requestType;

	public String getAPIAddress() {
		return "http://" + ip + ":" + port + "/" + api;
	}

	public static jDeviceInfo getDeviceInfo(String json, String ip) {
		Gson gson = new Gson();

		jDeviceInfo diDeviceInfo = gson.fromJson(json, jDeviceInfo.class);

		diDeviceInfo.ip = ip;

		return diDeviceInfo;
	}
}
