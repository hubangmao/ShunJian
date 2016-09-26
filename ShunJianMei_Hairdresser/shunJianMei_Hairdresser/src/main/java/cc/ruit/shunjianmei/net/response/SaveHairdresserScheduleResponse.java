package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
/**
 * @ClassName: SaveHairdresserScheduleResponse
 * @Description: 单日时间是否可约详情
 * @author: lee
 * @date: 2015年11月7日 上午11:40:11
 */
public class SaveHairdresserScheduleResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	private String Hour;// 记录ID
	private String State;// 提取日期

	public SaveHairdresserScheduleResponse(String Hour, String State) {
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "SaveHairdresserScheduleResponse [Hour=" + Hour + ", State="
				+ State + "]";
	}

	public static SaveHairdresserScheduleResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			SaveHairdresserScheduleResponse lists = gson.fromJson(json,
					SaveHairdresserScheduleResponse.class);
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<SaveHairdresserScheduleResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<SaveHairdresserScheduleResponse> lists = new ArrayList<SaveHairdresserScheduleResponse>();
			lists = gson.fromJson(json,
					new TypeToken<List<SaveHairdresserScheduleResponse>>() {
					}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
