package lewei50.greenhome;

import java.util.ArrayList;
import java.util.List;

import lewei50.entities.App;
import lewei50.entities.uploadData;
import lewei50.helpers.HtmlHelper;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		// LoadLBS();
	}

	// 加载位置服务
	public void LoadLBS() {
		String contextService = Context.LOCATION_SERVICE;
		// 通过系统服务，取得LocationManager对象
		LocationManager loctionManager = (LocationManager) getSystemService(contextService);

		String provider = LocationManager.GPS_PROVIDER;

		loctionManager.requestLocationUpdates(provider, 2000, 10,
				locationListener);

	}

	// 位置监听器
	private final LocationListener locationListener = new LocationListener() {
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		// 当位置变化时触发
		@Override
		public void onLocationChanged(Location location) {
			// 使用新的location更新TextView显示
			updateWithNewLocation(location);
		}
	};

	// /得到位置之后操作
	public void updateWithNewLocation(Location location) {
		String value = String.valueOf(location.getLongitude()) + ","
				+ String.valueOf(location.getLatitude());
		Toast.makeText(this, value, Toast.LENGTH_SHORT).show();
		uploadData udData = new uploadData();
		udData.Name = "G1";
		udData.Value = value;
		List<uploadData> list = new ArrayList<uploadData>();
		list.add(udData);
		HtmlHelper.UploadSensorData("01", list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// 得到当前选中的MenuItem的ID,
		int item_id = item.getItemId();

		switch (item_id) {
		case R.id.menu_settings:
			Toast.makeText(this, "click menu", Toast.LENGTH_SHORT).show();
			break;

		}
		return true;
	}

	// 我的传感器列表
	public void showMyList(View view) {
		sensorAsyn taskAsyn = new sensorAsyn();
		taskAsyn.execute();

	}

	// 实时统计
	public void showRealTime(View view) {
		Intent intent = new Intent(this, SensorWebActivity.class);
		intent.putExtra("catId", "c12");
		intent.putExtra("sensorId", App.getDefaultSensorId());

		startActivity(intent);

	}

	// 反向控制
	public void showControl(View view) {
		Intent intent = new Intent(this, ControlActivity.class);

		startActivity(intent);
	}

	// 历史走势
	public void showHistory(View view) {
		Intent intent = new Intent(this, SensorWebActivity.class);
		intent.putExtra("catId", "c21");
		intent.putExtra("sensorId", App.getDefaultSensorId());

		startActivity(intent);

	}

	// 数据分布
	public void showDataColume(View view) {
		Intent intent = new Intent(this, SensorWebActivity.class);
		intent.putExtra("catId", "c22");
		intent.putExtra("sensorId", App.getDefaultSensorId());

		startActivity(intent);
	}

	// 报警分析
	public void showAlarmAnalysis(View view) {
		Intent intent = new Intent(this, SensorWebActivity.class);
		intent.putExtra("catId", "c23");
		intent.putExtra("sensorId", App.getDefaultSensorId());

		startActivity(intent);

	}

	// 通讯录
	public void showContact(View view) {
		Intent intent = new Intent(this, SensorWebActivity.class);
		intent.putExtra("catId", "c31");
		intent.putExtra("sensorId", 0);

		startActivity(intent);

	}

	// 群发短信
	public void showMultiSms(View view) {
		Intent intent = new Intent(this, SensorWebActivity.class);
		intent.putExtra("catId", "c32");
		intent.putExtra("sensorId", 0);

		startActivity(intent);

	}

	// 报警历史
	public void showAlarmHistory(View view) {
		Intent intent = new Intent(this, SensorWebActivity.class);
		intent.putExtra("catId", "c33");
		intent.putExtra("sensorId", 0);

		startActivity(intent);

	}

	// 专家建议
	public void showSuggestion(View view) {
		Toast.makeText(getApplicationContext(), "专家建议即将开放", Toast.LENGTH_SHORT)
				.show();
	}

	App app;
	ProgressDialog MyDialog;

	class sensorAsyn extends AsyncTask<String, String, Boolean> {

		protected Boolean doInBackground(String... params) {

			app = (App) getApplicationContext();

			App.GatewayList = app.getGatewayList();
			return app.CurrentSession.IsLogin;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			MyDialog.cancel();
			Intent intent = new Intent(MainActivity.this, BuddyActivity.class);
			startActivity(intent);

		}

		@Override
		protected void onPreExecute() {
			MyDialog = new ProgressDialog(MainActivity.this);
			MyDialog.setIndeterminateDrawable(getResources().getDrawable(
					R.drawable.progressbar));
			MyDialog.setIcon(null);
			MyDialog.setTitle("");
			MyDialog.setMessage("传感器加载中，请稍候......");
			MyDialog.show();

		}

	}

}
