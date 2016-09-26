package cc.ruit.shunjianmei.net.request;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

import cc.ruit.shunjianmei.base.BaseRequest;

public class H_GetSelfWorkingRequest extends BaseRequest {

	private String OrderID;
	private String StartHour;
	private String Name;
	private String Mobile;
	private String Content;
	private String lastHour;

	public H_GetSelfWorkingRequest(String orderID, String startHour, String name, String mobile, String content,
			String lastHour) {
		super("H_GetSelfWorking", "1.0");
		OrderID = orderID;
		StartHour = startHour;
		Name = name;
		Mobile = mobile;
		Content = content;
		this.lastHour = lastHour;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "H_GetMasterDefaultSetting"));
	}

	/**
	 * @Title: toJsonString
	 * @Description: 把对象转成json格式的字符串
	 * @author: lee
	 * @param request
	 * @return: String
	 */
	public String toJsonString(H_GetSelfWorkingRequest request) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(request);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "H_GetSelfWorkingRequest [OrderID=" + OrderID + ", StartHour=" + StartHour + ", Name=" + Name
				+ ", Mobile=" + Mobile + ", Content=" + Content + ", lastHour=" + lastHour + ", Method=" + Method
				+ ", Infversion=" + Infversion + ", Key=" + Key + ", UID=" + UID + "]";
	}

}
