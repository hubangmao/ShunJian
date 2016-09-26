package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class HairdresserScheduleItemResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	// "ID": "1",
	// "Date": "2015-9-23",
	// "Address": "木北造型(科技园店),地址：丰台富丰路4号",
	// "Item": [
	private String ID;// 记录ID
	private String Name;// 提取日期
	private String Address;// 提取日期

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
		return "MaterialApplyResponse [ID=" + ID + ", Name=" + Name
				+ ", Address=" + Address + "]";
	}

	public static HairdresserScheduleItemResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			HairdresserScheduleItemResponse lists = gson.fromJson(json,
					HairdresserScheduleItemResponse.class);
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<HairdresserScheduleItemResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<HairdresserScheduleItemResponse> lists = new ArrayList<HairdresserScheduleItemResponse>();
			lists = gson.fromJson(json,
					new TypeToken<List<HairdresserScheduleItemResponse>>() {
					}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
