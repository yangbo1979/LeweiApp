package lewei50.greenhome;

import lewei50.entities.App;
import lewei50.entities.sensor;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

public class SensorSelectorActivity extends Activity {

	ExpandableListView expandablelistview;

	App app;

	BuddyAdapterSelector adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sensor_selector);

		expandablelistview = (ExpandableListView) findViewById(R.id.buddy_expandablelistview);
		app = (App) getApplicationContext();
		bindList();
	}

	private void bindList() {

		// App.GatewayList = app.getGatewayList();

		adapter = new BuddyAdapterSelector(this, App.GatewayList);
		expandablelistview.setAdapter(adapter);
		expandablelistview.setGroupIndicator(null);

		// 分组展开
		expandablelistview
				.setOnGroupExpandListener(new OnGroupExpandListener() {
					public void onGroupExpand(int groupPosition) {
					}
				});
		// 分组关闭
		expandablelistview
				.setOnGroupCollapseListener(new OnGroupCollapseListener() {
					public void onGroupCollapse(int groupPosition) {
					}
				});
		// 子项单击
		expandablelistview.setOnChildClickListener(new OnChildClickListener() {
			public boolean onChildClick(ExpandableListView arg0, View arg1,
					int groupPosition, int childPosition, long arg4) {
				sensor ss = App.GatewayList.get(groupPosition).sensors
						.get(childPosition);

				Intent intent = new Intent();
				intent.putExtra("sensorId", ss.getId());
				setResult(2, intent);
				finish();

				return false;
			}
		});
		int groupCount = expandablelistview.getCount();
		for (int i = 0; i < groupCount; i++) {
			expandablelistview.expandGroup(i);
		}
		;
	}
}
