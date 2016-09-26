package cc.ruit.shunjianmei.net.request;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

import cc.ruit.shunjianmei.base.BaseRequest;

/**
 * 2.20 获取指定日期日程
 */
public class HairdresserScheduleRequest extends BaseRequest {
	// 用户id
	private String UserID;
	private String Date;

	public HairdresserScheduleRequest(String UserID, String Date) {
		super("H_HairdresserSchedule", "1.0");
		this.UserID = UserID;
		this.Date = Date;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "H_HairdresserSchedule"));
	}

	/**
	 * @Title: toJsonString
	 * @Description: 把对象转成json格式的字符串
	 * @author: lee
	 * @param request
	 * @return: String
	 */
	public String toJsonString(HairdresserScheduleRequest request) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(request);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "HairdresserScheduleRequest [UserID=" + UserID + ", Method=" + Method
				+ ", Infversion=" + Infversion + ", Key=" + Key + ", Date="
				+ Date + ", UID=" + UID + "]";
	}
}
