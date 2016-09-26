package cc.ruit.shunjianmei.net.request;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

import cc.ruit.shunjianmei.base.BaseRequest;

public class H_GetDayWorkingRequest extends BaseRequest {
	private String UserID;
	private String Year;
	private String Month;
	private String Day;

	public H_GetDayWorkingRequest(String userID, String year, String month, String day) {
		super("H_GetDayWorking", "1.0");
		UserID = userID;
		Year = year;
		Month = month;
		Day = day;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "H_GetDayWorking"));
	}

	/**
	 * @Title: toJsonString
	 * @Description: 把对象转成json格式的字符串
	 * @author: lee
	 * @param request
	 * @return: String
	 */
	public String toJsonString(H_GetDayWorkingRequest request) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(request);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "H_GetDayWorkingRequest [UserID=" + UserID + ", Year=" + Year + ", Month=" + Month + ", Day=" + Day
				+ ", Method=" + Method + ", Infversion=" + Infversion + ", Key=" + Key + ", UID=" + UID + "]";
	}

}
