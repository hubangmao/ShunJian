package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
/**
 * @ClassName: WorkDailyAttendanceResponse
 * @Description: 签到返回
 * @author: lee
 * @date: 2015年9月5日 下午5:41:33
 */
public class WorkDailyAttendanceResponse implements Serializable{
	/**@fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: 
	 */
	private static final long serialVersionUID = 1L;
	private String Continuous;// 连续签到天数
	private String Experience;// 获得经验值
	private String Content;// 其他说明内容

	public String getContinuous() {
		return Continuous;
	}

	public void setContinuous(String continuous) {
		Continuous = continuous;
	}

	public String getExperience() {
		return Experience;
	}

	public void setExperience(String experience) {
		Experience = experience;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "WorkDailyAttendanceResponse [Continuous=" + Continuous
				+ ", Experience=" + Experience + ", Content=" + Content + "]";
	}

	public static WorkDailyAttendanceResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			WorkDailyAttendanceResponse response = gson.fromJson(json,WorkDailyAttendanceResponse.class );
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static List<WorkDailyAttendanceResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<WorkDailyAttendanceResponse> lists = new ArrayList<WorkDailyAttendanceResponse>();
			lists = gson.fromJson(json, new TypeToken<List<WorkDailyAttendanceResponse>>() {
			}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
