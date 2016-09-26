package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class BusinessAreaStoreListResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	private String ID;// 记录ID
	private String Name;// 名称
	private String Address;// 地址

	public BusinessAreaStoreListResponse(String iD, String name, String address) {
		super();
		ID = iD;
		Name = name;
		Address = address;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "HairdresserScheduleResponse [ID=" + ID + ", Name=" + Name
				+ ", Address=" + Address + "]";
	}

	public static BusinessAreaStoreListResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			BusinessAreaStoreListResponse lists = gson.fromJson(json,
					BusinessAreaStoreListResponse.class);
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<BusinessAreaStoreListResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<BusinessAreaStoreListResponse> lists = new ArrayList<BusinessAreaStoreListResponse>();
			lists = gson.fromJson(json,
					new TypeToken<List<BusinessAreaStoreListResponse>>() {
					}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
