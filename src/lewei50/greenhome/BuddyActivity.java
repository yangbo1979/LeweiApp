package lewei50.greenhome;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import lewei50.entities.App;
import lewei50.entities.gateway;
import lewei50.entities.sensor;
import lewei50.helpers.HtmlHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;
import android.view.View;

public class BuddyActivity extends Activity {
	ExpandableListView expandablelistview;
    
    Timer timer;
    
    App app;
    
    BuddyAdapterTwo adapter;
    
    
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_buddy);
        
        expandablelistview= (ExpandableListView) findViewById(R.id.buddy_expandablelistview);
        app=(App)getApplicationContext();
        
        bindList();
       
        timer=new Timer();
        
        timer.scheduleAtFixedRate(new MyTask(), 20000,20000);
    }

	private class MyTask extends TimerTask {
		@Override
		public void run() {
			
			App.GatewayList =  app.getGatewayList();
			mHandler.sendEmptyMessage(0);
			 
		}
	}
	
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			
			 adapter.ChangeData(App.GatewayList);
			 
		}
	};
	
	
	private void bindList() {
		
        adapter=new BuddyAdapterTwo(this,App.GatewayList);
        expandablelistview.setAdapter(adapter);
        expandablelistview.setGroupIndicator(null);
        
        
        //分组展开
        expandablelistview.setOnGroupExpandListener(new OnGroupExpandListener(){
			public void onGroupExpand(int groupPosition) {
			}
        });
        //分组关闭
        expandablelistview.setOnGroupCollapseListener(new OnGroupCollapseListener(){
			public void onGroupCollapse(int groupPosition) {
			}
        });
        //子项单击
        expandablelistview.setOnChildClickListener(new OnChildClickListener(){
			public boolean onChildClick(ExpandableListView arg0, View arg1,
					int groupPosition, int childPosition, long arg4) {
				sensor ss=App.GatewayList.get(groupPosition).sensors.get(childPosition);
				
				Intent intent=new Intent(BuddyActivity.this,SensorWebActivity.class);
				
				intent.putExtra("catId", "c12");
				
				intent.putExtra("sensorId",ss.getId());
				
	    		startActivity(intent);

	    		return false;
			}
        });
        int groupCount = expandablelistview.getCount();
        for (int i=0; i<groupCount; i++) {
        	expandablelistview.expandGroup(i);
        };
	}
	
	
    ProgressDialog MyDialog;
    class sensorAsyn extends AsyncTask<String, String, Boolean> {

		protected Boolean doInBackground(String... params) {
			
	    	app=(App)getApplicationContext();
	    	   	
	    	App.GatewayList=app.getGatewayList();
	    	return app.CurrentSession.IsLogin;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			MyDialog.cancel();
			bindList();
		}

		@Override
		protected void onPreExecute() {
			MyDialog=new ProgressDialog(BuddyActivity.this);
			MyDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progressbar));
			MyDialog.setIcon(null);
			MyDialog.setTitle("");
			MyDialog.setMessage("传感器加载中，请稍候......");
			MyDialog.show();
			
		}

	}
	
}