package lewei50.greenhome;

import java.lang.reflect.Type;
import java.security.interfaces.RSAKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lewei50.entities.App;
import lewei50.entities.jResult;
import lewei50.entities.jSensor;
import lewei50.helpers.HtmlHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class ListActivity extends Activity {

	private String requestType;

	private SimpleAdapter adapter;

	private ListView listView2;

	private List<Map<String, Object>> DataList = new ArrayList<Map<String, Object>>();

	Gson gson = new Gson();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);

		listView2 = (ListView) findViewById(R.id.listView2);

		Intent intent = this.getIntent();
		requestType = intent.getStringExtra("requestType");

		bindList(false);

		listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				ListView listView = (ListView) arg0;
				// listView.getItemAtPosition(ar)
				@SuppressWarnings("unchecked")
				HashMap<String, Object> oo = (HashMap<String, Object>) listView
						.getItemAtPosition(arg2);
				String id = oo.get("id").toString();
				String value = oo.get("value").toString();

				if (value.equals("0"))
					value = "1";
				else {
					value = "0";
				}
				jResult<String> result;
				if (requestType.equals("get")) {
					HtmlHelper.API  = oo.get("apiAddress").toString();
					result = HtmlHelper.GetUrlJsonData("updateSensor",
							new String[] { id, value });
//					System.out.println("API");
//					System.out.println(HtmlHelper.API);
				} else {
					jSensor js = new jSensor();
					js.id = id;
					js.value = value;
					result = HtmlHelper.PostUrlJsonData("updateSensor", js);
				}

				if (result.successful) {
					Type type = new TypeToken<jResult<jSensor>>() {
					}.getType();
//					jResult<jSensor> r2 = gson.fromJson(result.data, type);
					try
					{
						if (result.successful) {
							// 重新加载数据
							mHandler.sendEmptyMessage(0);
						} else {
							new AlertDialog.Builder(ListActivity.this)
									.setIcon(R.drawable.warning).setTitle("失败")
									.setPositiveButton("确定", null)
	//								.setMessage(r2.message).create()
									.show();
						}
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				} else {
					new AlertDialog.Builder(ListActivity.this)
							.setIcon(R.drawable.warning).setTitle("失败")
							.setPositiveButton("确定", null)
							.setMessage(result.message).create().show();
				}

			}
		});

	}

	private void bindList(Boolean reload) {
		DataList = getData(reload);
		// 清單面版
		adapter = new SimpleAdapter(this, DataList, R.layout.view2content,
				new String[] { "id", "name", "value", "valueImg" }, new int[] {
						R.id.id, R.id.name, R.id.value, R.id.valueImg });
		listView2.setAdapter(adapter);
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);

			bindList(true);

		}
	};

	private List<Map<String, Object>> getData(Boolean reload) {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		List<jSensor> sList = new ArrayList<jSensor>();
		if (reload == false)
		{
			System.out.println("reload == false");
			sList = ControlActivity.controllerList;
			System.out.println(sList.size());
		}
		else {
			System.out.println("reload != false");
			sList = HtmlHelper.getAllSensors().data;
		}
		for (jSensor ss : sList) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", ss.id);
			map.put("name", ss.name);
			map.put("value", ss.value);
			map.put("apiAddress", ss.apiAddress);
			if (Integer.parseInt(ss.value) == 0)
				map.put("valueImg", R.drawable.offline);
			else
				map.put("valueImg", R.drawable.online);
			list.add(map);
		}
		
		return list;

	}

}
