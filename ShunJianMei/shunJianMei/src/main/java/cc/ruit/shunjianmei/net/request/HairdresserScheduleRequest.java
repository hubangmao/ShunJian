package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * 
 * @ClassName: HairdresserScheduleRequest
 * @Description: 发型日程请求
 * @author: 欧阳
 * @date: 2015年10月26日 上午9:54:33
 */
public class HairdresserScheduleRequest extends BaseRequest {
	// 用户id
	private String UserID;
	private String ID;

	/**
	 * @Title:ActivityListRequest
	 * @Description:活动
	 * @param UserId
	 */
	public HairdresserScheduleRequest(String UserID, String ID) {
		super("HairdresserSchedule", "1.0");
		this.UserID = UserID;
		this.ID = ID;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "HairdresserSchedule"));
	}

	/**
	 * @Title: toJsonString
	 * @Description: 把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(HairdresserScheduleRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "HairdresserScheduleRequest [UserID=" + UserID + ", ID=" + ID
				+ "]";
	}

}
