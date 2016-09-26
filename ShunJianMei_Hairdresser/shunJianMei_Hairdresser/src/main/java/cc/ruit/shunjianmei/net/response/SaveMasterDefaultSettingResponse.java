package cc.ruit.shunjianmei.net.response;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class SaveMasterDefaultSettingResponse {
	private String WorkDay;
	private String DayWorkHour;

	public SaveMasterDefaultSettingResponse(String workDay, String dayWorkHour) {
		super();
		WorkDay = workDay;
		DayWorkHour = dayWorkHour;
	}

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

	@Override
	public String toString() {
		return "SaveMasterDefaultSetting [WorkDay=" + WorkDay + ", DayWorkHour=" + DayWorkHour + "]";
	}

	public static SaveMasterDefaultSettingResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			SaveMasterDefaultSettingResponse lists = gson.fromJson(json, SaveMasterDefaultSettingResponse.class);
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<SaveMasterDefaultSettingResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<SaveMasterDefaultSettingResponse> lists = new ArrayList<SaveMasterDefaultSettingResponse>();
			lists = gson.fromJson(json, new TypeToken<List<SaveMasterDefaultSettingResponse>>() {
			}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
