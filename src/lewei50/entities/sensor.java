package lewei50.entities;

import android.R.bool;

public class sensor {
	private long id;
	private String name;
	private String value;
	private String type;
	private String typeName;
	private String unit;
	private String lastUpdateTime;
	private boolean isOnline;
	private boolean isError;
	private boolean isAlarm;
	
	public sensor(long id,String name,String value,String type,String typeName,String unit,String lastUpdateTime,boolean isOnline,boolean isError,boolean isAlarm)
	{
		this.id=id;
		this.name=name;
		this.value=value;
		this.type=type;
		this.typeName=typeName;
		this.unit=unit;
		this.lastUpdateTime=lastUpdateTime;
		this.isOnline=isOnline;
		this.isError=isError;
		this.isAlarm=isAlarm;
	}
	
	public long getId()
	{
		return this.id;
	}
	public void setId(long id)
	{
		this.id=id;
	}
	
	public String getName()
	{
		return this.name;
	}
	public void setName(String name)
	{
		this.name=name;
	}
	
	public String getTypeName()
	{
		return this.typeName;
	}
	public void setTypeName(String typeName)
	{
		this.typeName=typeName;
	}
	
	public String getValue()
	{
		return this.value;
	}
	public void setValue(String value)
	{
		this.value=value;
	}
	
	public String getType()
	{
		return this.type;
	}
	public void setType(String type)
	{
		this.type=type;
	}
	
	public String getUnit()
	{
		return this.unit;
	}
	public void setUnit(String unit)
	{
		this.unit=unit;
	}
	
	public String getLastUpdateTime()
	{
		return this.lastUpdateTime;
	}
	public void setLastUpdateTime(String lastUpdateTime)
	{
		this.lastUpdateTime=lastUpdateTime;
	}
	
	public boolean getIsOnline()
	{
		return this.isOnline;
	}
	public void setIsOnline(boolean isOnline)
	{
		this.isOnline=isOnline;
	}
	
	public boolean getIsError()
	{
		return this.isError;
	}
	public void setIsError(boolean isError)
	{
		this.isError=isError;
	}
	
	public boolean getIsAlarm()
	{
		return this.isAlarm;
	}
	public void setIsAlarm(boolean isAlarm)
	{
		this.isAlarm=isAlarm;
	}
}
