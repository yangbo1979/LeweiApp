package lewei50.greenhome;

import lewei50.entities.App;
import lewei50.entities.sensor;
import android.os.Bundle;
import android.R.string;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.ZoomDensity;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

///公共的展示web的界面
public class SensorWebActivity extends Activity {

	WebView mainWebView;
	TextView sensorTextView;
	TextView catTextView;
	ImageView catImageView;
	ProgressBar loadingProgressBar;

	TextView selectorTextView;
	ImageView selectorImageView;

	String catId;
	long sensorId;
	Resources ress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sensor_web);
		loadControl();
		loadWeb();
	}

	// 加载要使用的控件
	private void loadControl() {
		mainWebView = (WebView) this.findViewById(R.id.mainWebView);
		sensorTextView = (TextView) this.findViewById(R.id.sensorTextView);
		catTextView = (TextView) this.findViewById(R.id.catTextView);
		catImageView = (ImageView) this.findViewById(R.id.catImageView);
		loadingProgressBar = (ProgressBar) this
				.findViewById(R.id.loadingProgressBar);

		selectorImageView = (ImageView) this
				.findViewById(R.id.selectorImageButton);
		selectorTextView = (TextView) this.findViewById(R.id.selectorTextView);

		// 获得传递过来的值
		Intent intent = this.getIntent();
		catId = intent.getStringExtra("catId");
		sensorId = intent.getLongExtra("sensorId", 0);

		// 赋值到控件

		ress = this.getResources();
		int index = ress
				.getIdentifier(catId, "drawable", this.getPackageName());
		catImageView.setImageResource(index);

		int tIndex = ress.getIdentifier(catId, "string", this.getPackageName());
		catTextView.setText(tIndex);

		// 如果sensorid为0，就是和传感器无关的页面，就隐藏更换传感器按钮
		if (sensorId == 0) {
			selectorImageView.setVisibility(8);
			selectorTextView.setVisibility(8);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case 1:
			switch (resultCode) {
			case 2:
				sensorId = data.getLongExtra("sensorId", 0);
				loadWeb();

				break;

			default:
				break;
			}
			break;

		default:
			break;
		}

	}

	private void loadWeb() {

		mainWebView.setWebChromeClient(new WebChromeClient() {

			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onProgressChanged(WebView view, int newProgress) {

				super.onProgressChanged(view, newProgress);
				loadingProgressBar.setProgress(newProgress);
				if (newProgress > 50) {

					loadingProgressBar.setVisibility(8);
				}
			}
		});

		WebSettings settings = mainWebView.getSettings();

		settings.setJavaScriptEnabled(true);
		settings.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
		settings.setDefaultZoom(ZoomDensity.CLOSE);

		App app = (App) getApplicationContext();

		int tIndex = ress.getIdentifier("url_" + catId, "string",
				this.getPackageName());

		String url = ress.getString(tIndex);

		if (sensorId > 0) {
			sensor ss = App.getSensor(sensorId);
			sensorTextView.setText(ss.getName());
			mainWebView.loadUrl("http://open.lewei50.com" + url + "/"
					+ sensorId + "?userkey=" + app.CurrentSession.SessionKey);

		} else {
			sensorTextView.setText("");
			mainWebView.loadUrl("http://open.lewei50.com" + url + "?userkey="
					+ app.CurrentSession.SessionKey);
		}
	}

	// /选择传感器
	public void selectSensor(View view) {

		Intent intent = new Intent(this, SensorSelectorActivity.class);
		startActivityForResult(intent, 1);
	}

}
