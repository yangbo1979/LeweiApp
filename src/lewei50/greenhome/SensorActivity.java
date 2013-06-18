package lewei50.greenhome;

import lewei50.entities.App;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;

public class SensorActivity extends Activity {

	private WebView webView1;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensor);
		webView1 = (WebView) findViewById(R.id.webView1);

		webView1.setWebViewClient(new WebViewClient() {

			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});

		WebSettings settings = webView1.getSettings();

		settings.setJavaScriptEnabled(true);
		settings.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
		settings.setDefaultZoom(ZoomDensity.CLOSE);

		Intent intent = getIntent();

		App app = (App) getApplicationContext();
		String sensorId = intent.getStringExtra("sensorId");
		webView1.loadUrl("http://greenhome.lewei50.com/home/doc/" + sensorId
				+ "?userkey=" + app.CurrentSession.SessionKey);
	}

}
