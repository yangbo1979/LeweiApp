package lewei50.entities;

import java.util.List;

public class gateway {
	public long id;
	public String name;
	public String typeName;
	public String description;

	public Boolean isControlled;

	public Boolean internetAvailable;

	public String apiAddress;

	public String apiAddressInternet;

	public List<sensor> sensors;

	public List<jController> controllers;

}
