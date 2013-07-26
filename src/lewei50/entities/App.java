package lewei50.entities;

import java.util.ArrayList;

import java.util.List;

import lewei50.helpers.HtmlHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Application;

public class App extends Application {
	public CurrentSession CurrentSession;

	// /获得当前用户所有的网关和传感器数据
	public List<gateway> getGatewayList() {
		List<gateway> list = new ArrayList<gateway>();

		String res;
		try {
			res = HtmlHelper
					.getJsonString("http://open.lewei50.com/api/V1/User/GetSensorswithgateway?userkey="
							+ CurrentSession.SessionKey);

			JSONArray jsonArray = new JSONArray(res);

			int iSize = jsonArray.length();

			for (int i = 0; i < iSize; i++) {

				JSONObject gatewayObj = jsonArray.getJSONObject(i);

				JSONArray sensorsArray = new JSONArray(
						gatewayObj.getString("sensors"));

				List<sensor> sensors = new ArrayList<sensor>();

				int sSize = sensorsArray.length();

				for (int j = 0; j < sSize; j++) {
					JSONObject sensorObj = sensorsArray.getJSONObject(j);
					sensor ss = new sensor(sensorObj.getLong("id"),
							sensorObj.getString("name"),
							sensorObj.getString("value"),
							sensorObj.getString("type"),
							sensorObj.getString("typeName"),
							sensorObj.getString("unit"),
							sensorObj.getString("lastUpdateTime"),
							sensorObj.getBoolean("isOnline"),
							sensorObj.getBoolean("isError"),
							sensorObj.getBoolean("isAlarm"));

					sensors.add(ss);
				}

				gateway gt = new gateway();
				gt.id = gatewayObj.getLong("id");
				gt.name = gatewayObj.getString("name");
				gt.typeName = gatewayObj.getString("typeName");
				gt.description = gatewayObj.getString("description");
				gt.sensors = sensors;
				
				//code added by yangbo @2013.7.26
				gt.apiAddress = gatewayObj.getString("apiAddress");
				gt.apiAddressInternet = gatewayObj.getString("apiAddressInternet");
				//end code add

				list.add(gt);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static List<gateway> GatewayList;

	// /根据sensorid获取sensor
	public static sensor getSensor(long sensorId) {
		sensor rr = new sensor(0, "", "", "", "", "", "", false, false, false);
		for (gateway gt : GatewayList) {
			for (sensor ss : gt.sensors) {
				if (sensorId == ss.getId()) {
					rr = ss;
					break;
				}
			}
		}

		return rr;

	}

	// /获取默认的sensorid
	public static long getDefaultSensorId() {
		if (GatewayList.size() > 0 && GatewayList.get(0).sensors.size() > 0) {
			return GatewayList.get(0).sensors.get(0).getId();
		} else {

			return 0l;
		}
	}
}
