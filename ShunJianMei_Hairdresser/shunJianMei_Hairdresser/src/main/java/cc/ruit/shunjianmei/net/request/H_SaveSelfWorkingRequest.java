package cc.ruit.shunjianmei.net.request;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

import cc.ruit.shunjianmei.base.BaseRequest;

public class H_SaveSelfWorkingRequest extends BaseRequest {
	private String UserID;
	private String StartHour;
	private String Year;
	private String Month;
	private String Day;
	private String Name;
	private String Mobile;
	private String Content;
	private String LastHour;
	private String OrderID;

	public H_SaveSelfWorkingRequest(String userID, String startHour, String year, String month, String day, String name,
			String mobile, String content, String lastHour, String orderID) {
		super("H_SaveSelfWorking", "1.0");
		UserID = userID;
		StartHour = startHour;
		Year = year;
		Month = month;
		Day = day;
		Name = name;
		Mobile = mobile;
		Content = content;
		LastHour = lastHour;
		OrderID = orderID;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "H_SaveSelfWorking"));
	}

	/**
	 * @Title: toJsonString
	 * @Description: 把对象转成json格式的字符串
	 * @author: lee
	 * @param request
	 * @return: String
	 */
	public String toJsonString(H_SaveSelfWorkingRequest request) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(request);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "H_SaveSelfWorkingRequest [UserID=" + UserID + ", StartHour=" + StartHour + ", Year=" + Year + ", Month="
				+ Month + ", Day=" + Day + ", Name=" + Name + ", Mobile=" + Mobile + ", Content=" + Content
				+ ", LastHour=" + LastHour + ", OrderID=" + OrderID + ", Method=" + Method + ", Infversion="
				+ Infversion + ", Key=" + Key + ", UID=" + UID + "]";
	}

}
