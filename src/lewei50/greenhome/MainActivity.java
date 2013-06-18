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

	// ����λ�÷���
	public void LoadLBS() {
		String contextService = Context.LOCATION_SERVICE;
		// ͨ��ϵͳ����ȡ��LocationManager����
		LocationManager loctionManager = (LocationManager) getSystemService(contextService);

		String provider = LocationManager.GPS_PROVIDER;

		loctionManager.requestLocationUpdates(provider, 2000, 10,
				locationListener);

	}

	// λ�ü�����
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

		// ��λ�ñ仯ʱ����
		@Override
		public void onLocationChanged(Location location) {
			// ʹ���µ�location����TextView��ʾ
			updateWithNewLocation(location);
		}
	};

	// /�õ�λ��֮�����
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
		// �õ���ǰѡ�е�MenuItem��ID,
		int item_id = item.getItemId();

		switch (item_id) {
		case R.id.menu_settings:
			Toast.makeText(this, "click menu", Toast.LENGTH_SHORT).show();
			break;

		}
		return true;
	}

	// �ҵĴ������б�
	public void showMyList(View view) {
		sensorAsyn taskAsyn = new sensorAsyn();
		taskAsyn.execute();

	}

	// ʵʱͳ��
	public void showRealTime(View view) {
		Intent intent = new Intent(this, SensorWebActivity.class);
		intent.putExtra("catId", "c12");
		intent.putExtra("sensorId", App.getDefaultSensorId());

		startActivity(intent);

	}

	// �������
	public void showControl(View view) {
		Intent intent = new Intent(this, ControlActivity.class);

		startActivity(intent);
	}

	// ��ʷ����
	public void showHistory(View view) {
		Intent intent = new Intent(this, SensorWebActivity.class);
		intent.putExtra("catId", "c21");
		intent.putExtra("sensorId", App.getDefaultSensorId());

		startActivity(intent);

	}

	// ���ݷֲ�
	public void showDataColume(View view) {
		Intent intent = new Intent(this, SensorWebActivity.class);
		intent.putExtra("catId", "c22");
		intent.putExtra("sensorId", App.getDefaultSensorId());

		startActivity(intent);
	}

	// ��������
	public void showAlarmAnalysis(View view) {
		Intent intent = new Intent(this, SensorWebActivity.class);
		intent.putExtra("catId", "c23");
		intent.putExtra("sensorId", App.getDefaultSensorId());

		startActivity(intent);

	}

	// ͨѶ¼
	public void showContact(View view) {
		Intent intent = new Intent(this, SensorWebActivity.class);
		intent.putExtra("catId", "c31");
		intent.putExtra("sensorId", 0);

		startActivity(intent);

	}

	// Ⱥ������
	public void showMultiSms(View view) {
		Intent intent = new Intent(this, SensorWebActivity.class);
		intent.putExtra("catId", "c32");
		intent.putExtra("sensorId", 0);

		startActivity(intent);

	}

	// ������ʷ
	public void showAlarmHistory(View view) {
		Intent intent = new Intent(this, SensorWebActivity.class);
		intent.putExtra("catId", "c33");
		intent.putExtra("sensorId", 0);

		startActivity(intent);

	}

	// ר�ҽ���
	public void showSuggestion(View view) {
		Toast.makeText(getApplicationContext(), "ר�ҽ��鼴������", Toast.LENGTH_SHORT)
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
			MyDialog.setMessage("�����������У����Ժ�......");
			MyDialog.show();

		}

	}

}
