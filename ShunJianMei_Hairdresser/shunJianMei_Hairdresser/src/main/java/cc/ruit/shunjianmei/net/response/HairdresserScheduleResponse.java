package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class HairdresserScheduleResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	private String Hour;// 记录ID
	private String State;// 提取日期
	List<BusinessAreaStoreListResponse> Item;//

	public HairdresserScheduleResponse(String Hour, String State) {
		this.Hour = Hour;
		this.State = State;
	}

	public String getHour() {
		return Hour;
	}

	public void setHour(String hour) {
		Hour = hour;
	}

	public String getState() {
		return State;
	}

	public void setState(String state) {
		State = state;
	}

	public List<BusinessAreaStoreListResponse> getItem() {
		return Item;
	}

	public void setItem(List<BusinessAreaStoreListResponse> item) {
		Item = item;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "HairdresserScheduleResponse [Hour=" + Hour + ", State=" + State
				+ ", Item=" + Item + "]";
	}

	public static HairdresserScheduleResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			HairdresserScheduleResponse lists = gson.fromJson(json,
					HairdresserScheduleResponse.class);
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<HairdresserScheduleResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<HairdresserScheduleResponse> lists = new ArrayList<HairdresserScheduleResponse>();
			lists = gson.fromJson(json,
					new TypeToken<List<HairdresserScheduleResponse>>() {
					}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
