package lewei50.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.webkit.JsResult;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import lewei50.entities.*;
import lewei50.greenhome.R;

public class HtmlHelper {

	public static String API = "http://192.168.1.49/api";

	public static String UserKey = "";

	public static String RequestType = "get";

	public static String getJsonString(String urlPath) throws Exception {
		URL url = new URL(urlPath);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setConnectTimeout(5000);
		connection.setReadTimeout(10000);
		connection.connect();
		InputStream inputStream = connection.getInputStream();
		// 对应的字符编码转换
		Reader reader = new InputStreamReader(inputStream, "UTF-8");
		BufferedReader bufferedReader = new BufferedReader(reader);
		String str = null;
		StringBuffer sb = new StringBuffer();
		while ((str = bufferedReader.readLine()) != null) {
			sb.append(str);
		}
		reader.close();
		connection.disconnect();
		return sb.toString();
	}

	// /登录的判断
	public static CurrentSession Login(String UserName, String Password) {
		// {"Data":null,"Successful":false,"Message":"错误的用户名密码"}
		CurrentSession cs = new CurrentSession();
		String res;
		try {
			res = getJsonString("http://open.lewei50.com/api/v1/user/login?username="
					+ UserName + "&password=" + Password);
			JSONObject jsonObject = new JSONObject(res);
			cs.IsLogin = Boolean.parseBoolean(jsonObject
					.getString("Successful"));
			cs.UserName = UserName;
			if (cs.IsLogin) {
				cs.SessionKey = jsonObject.getString("Data");
				HtmlHelper.UserKey = cs.SessionKey;
			} else
				cs.Message = jsonObject.getString("Message");
			// JSONArray childs= jsonObject.getJSONArray("Childs");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cs.IsLogin = false;
			cs.Message = e.getMessage();
		}

		return cs;
	}

	public static gateway GetUserGateway() {
		gateway gt = new gateway();
		String res;
		try {
			res = getJsonString("http://open.lewei50.com/api/v1/user/GetControllersWithGateway?userkey="
					+ UserKey);
			Gson gson = new Gson();
			Type type = new TypeToken<List<gateway>>() {
			}.getType();

			List<gateway> list = gson.fromJson(res, type);
			gt = list.get(0);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return gt;
	}

	// /get方式获取网关api数据
	public static jResult<String> GetUrlJsonData(String f, String[] p) {

		jResult<String> result = new jResult<String>();
		result.successful = false;
		String linkString = "?";
		if (API.indexOf("?") > -1)
			linkString = "&";
		String urlString = API + linkString + "userkey=" + UserKey + "&f=" + f;
		if (p != null) {
			for (int i = 0; i < p.length; i++) {

				urlString = urlString + "&p" + (i + 1) + "=" + p[i];

			}
		}

		try {
			result.data = getJsonString(urlString);
			result.successful = true;

		} catch (Exception e) {
			result.message = "API地址错误";

		}
		return result;

	}

	// /post方式获取网关api数据
	public static jResult<String> PostUrlJsonData(String f, Object p) {
		jResult<String> result = new jResult<String>();
		result.successful = false;
		String destUrl = API;

		HttpEntityEnclosingRequestBase httpRequest = new HttpPost(destUrl);

		httpRequest.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
		httpRequest.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				10000);

		jRequest jr = new jRequest();
		jr.userkey = UserKey;
		jr.f = f;
		jr.p = p;
		Gson gson = new Gson();
		try {

			httpRequest.setEntity(new StringEntity(gson.toJson(jr)));

			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(httpRequest);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				result.data = EntityUtils.toString(httpResponse.getEntity());
				result.successful = true;

			} else {
				result.message = "API地址错误";
			}

		} catch (Exception e) {
			result.message = "API地址错误";

		}

		return result;
	}

	// /post方式获取网关api数据
	public static uploadResult UploadSensorData(String gatewayNo,
			List<uploadData> p) {
		uploadResult result = new uploadResult();
		result.Successful = false;

		String destUrl = "http://www.lewei50.com/api/V1/Gateway/UpdateSensors/"
				+ gatewayNo;

		HttpEntityEnclosingRequestBase httpRequest = new HttpPost(destUrl);

		httpRequest.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
		httpRequest.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				10000);
		httpRequest.setHeader("userkey", UserKey);
		Gson gson = new Gson();
		try {

			httpRequest.setEntity(new StringEntity(gson.toJson(p)));

			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(httpRequest);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				result = gson.fromJson(
						EntityUtils.toString(httpResponse.getEntity()),
						uploadResult.class);

			} else {
				result.Message = "API地址错误";
			}

		} catch (Exception e) {
			result.Message = "API地址错误";

		}

		return result;
	}

	// /获取当前网关的所有可控设备
	public static jResult<List<jSensor>> getAllSensors() {

		jResult<List<jSensor>> rr = new jResult<List<jSensor>>();

		jResult<String> result;
		if (RequestType.equals("get"))
			result = HtmlHelper.GetUrlJsonData("getAllSensors", null);
		else {
			result = HtmlHelper.PostUrlJsonData("getAllSensors", null);
		}

		Type type = new TypeToken<jResult<List<jSensor>>>() {
		}.getType();

		Gson gson = new Gson();
		if (result.successful == true) {

			rr = gson.fromJson(result.data, type);

		} else {
			rr.successful = false;
			rr.message = result.message;

		}
		return rr;
	}
}
