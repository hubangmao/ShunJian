package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cc.ruit.shunjianmei.base.BaseResponse;

public class H_GetMasterDefaultSettingResponse extends BaseResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String WorkDay = "";
	private String DayWorkHour = "";
	private String ID;
	private String Name;
	private String Address;

	public String getWorkDay() {
		return WorkDay;
	}

	public void setWorkDay(String workDay) {
		WorkDay = workDay;
	}

	public String getDayWorkHour() {
		return DayWorkHour;
	}

	public void setDayWorkHour(String dayWorkHour) {
		DayWorkHour = dayWorkHour;
	}

	public String getWorkShopID() {
		return ID;
	}

	public void setWorkShopID(String ID) {
		this.ID = ID;
	}

	public String getWorkShop() {
		return Name;
	}

	public void setWorkShop(String Name) {
		this.Name = Name;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	@Override
	public String toString() {
		return "H_GetMasterDefaultSettingResponse [WorkDay=" + WorkDay + ", DayWorkHour=" + DayWorkHour + ", ID=" + ID
				+ ", Name=" + Name + ", Address=" + Address + "]";
	}

	public static H_GetMasterDefaultSettingResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			H_GetMasterDefaultSettingResponse lists = gson.fromJson(json, H_GetMasterDefaultSettingResponse.class);
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<H_GetMasterDefaultSettingResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<H_GetMasterDefaultSettingResponse> lists = new ArrayList<H_GetMasterDefaultSettingResponse>();
			lists = gson.fromJson(json, new TypeToken<List<H_GetMasterDefaultSettingResponse>>() {
			}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
