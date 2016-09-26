package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * @ClassName: HairdresserScheduleResponse
 * @Description: 日程信息返回
 * @author: 欧阳
 * @date: 2015年10月26日 上午9:58:12
 */
public class HairdresserScheduleResponse implements Serializable {
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description:
	 */
	private static final long serialVersionUID = 1L;
	private String Date;// 日期
	private String WeekDay;// 星期
	private List<Item> Item;// 开始时间

	public static class Item {
		public String Hour;// 时间
		public String State;// 状态 0不可预约 1可预约
		public List<StoreItem> Item;

		public static class StoreItem {
			public String ID;// 店铺ID
			public String Name;// 店铺名字
			public String Address;// 店铺地址
		}

	}

	public String getDate() {
		return Date;
	}

	public void setDate(String date) {
		Date = date;
	}

	public String getWeekDay() {
		return WeekDay;
	}

	public void setWeekDay(String weekDay) {
		WeekDay = weekDay;
	}

	public List<Item> getItem() {
		return Item;
	}

	public void setItem(List<Item> item) {
		Item = item;
	}

	@Override
	public String toString() {
		return "HairdresserScheduleResponse [Date=" + Date + ", WeekDay="
				+ WeekDay + ", Item=" + Item + "]";
	}

	public static HairdresserScheduleResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			HairdresserScheduleResponse response = gson.fromJson(json,
					HairdresserScheduleResponse.class);
			return response;
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
