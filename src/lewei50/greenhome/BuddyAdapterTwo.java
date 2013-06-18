package lewei50.greenhome;

import java.nio.channels.InterruptibleChannel;
import java.util.List;

import lewei50.entities.gateway;
import lewei50.entities.sensor;

import android.R.integer;
import android.R.raw;
import android.R.string;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BuddyAdapterTwo extends BaseExpandableListAdapter {   
    //private String[] group; 
    private List<gateway> buddy;

    private Context context;
    LayoutInflater inflater;
    
    public BuddyAdapterTwo(Context context,List<gateway> buddy){
    	this.context=context;
    	inflater = LayoutInflater.from(context);
    	//this.group=group;
    	this.buddy=buddy;    	
    }
	public sensor getChild(int groupPosition, int childPosition) {
		return buddy.get(groupPosition).sensors.get(childPosition);
				
	}

	public long getChildId(int groupPosition, int childPosition) {
		
		return childPosition;
	}

	public View getChildView(int groupPosition, int childPosition, boolean arg2, View convertView,
			ViewGroup arg4) {
		Resources ress=context.getResources();
		sensor ss=getChild(groupPosition, childPosition);
		convertView = inflater.inflate(R.layout.buddy_listview_child_item, null);
		//convertView.setBackgroundColor(color)
		ImageView avastarImageView =(ImageView)convertView.findViewById(R.id.buddy_listview_child_avatar);
		int index=ress.getIdentifier(ss.getType().toLowerCase(),"drawable",context.getPackageName());
		avastarImageView.setImageResource(index);
		
		ImageView statusImageView =(ImageView)convertView.findViewById(R.id.buddy_listview_child_status);
		if(ss.getIsOnline())
			statusImageView.setImageResource(R.drawable.online);
		else
			statusImageView.setImageResource(R.drawable.offline);
		
		
		TextView nickTextView=(TextView) convertView.findViewById(R.id.buddy_listview_child_name);
		nickTextView.setText(ss.getName());
		
		TextView dTextView=(TextView)convertView.findViewById(R.id.buddy_listview_child_value);
		dTextView.setText(ss.getValue()+" "+ss.getUnit());
		
		TextView lastUpdateTextView=(TextView)convertView.findViewById(R.id.lastUpdateTimeTextView);
		lastUpdateTextView.setText(ss.getLastUpdateTime());
		//如果离线直接显示灰色背景，如果报警显示
		if(ss.getIsOnline()==false)
		{
			convertView.setBackgroundColor(0xffcccccc);
		}
		else
		if(ss.getIsAlarm()==true)
		{
			convertView.setBackgroundColor(0xffffff00);
			dTextView.setTextColor(0xffff0000);
			nickTextView.setTextColor(0xffff0000);
		}
		return convertView;
	}

	public int getChildrenCount(int groupPosition) {
		return buddy.get(groupPosition).sensors.size();
	}

	public gateway getGroup(int groupPosition) {
		return buddy.get(groupPosition);
	}

	public int getGroupCount() {
		return buddy.size();
		
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup arg3) {
		convertView = inflater.inflate(R.layout.buddy_listview_group_item, null);
		TextView groupNameTextView=(TextView) convertView.findViewById(R.id.buddy_listview_group_name);
		TextView groupNumTextView = (TextView) convertView.findViewById(R.id.buddy_listview_group_num);
		gateway gt=getGroup(groupPosition);
		groupNameTextView.setText(gt.name);
		
		groupNumTextView.setText(Integer.toString(gt.sensors.size()));
		
		
		ImageView image = (ImageView) convertView.findViewById(R.id.buddy_listview_image);
		image.setImageResource(R.drawable.list_indecator_button);
		//更换展开分组图片
		if(!isExpanded){
			image.setImageResource(R.drawable.list_indecator_button_down);
		}
		return convertView;
	}

	public boolean hasStableIds() {
		return true;
	}
	// 子选项是否可以选择  
	public boolean isChildSelectable(int arg0, int arg1) {		
		return true;
	}
	
	public void ChangeData(List<gateway> newList)
	{
		buddy=newList;
		this.notifyDataSetChanged();
	}
}