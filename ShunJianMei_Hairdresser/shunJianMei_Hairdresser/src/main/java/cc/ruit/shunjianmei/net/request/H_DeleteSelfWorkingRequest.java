package cc.ruit.shunjianmei.net.request;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

import cc.ruit.shunjianmei.base.BaseRequest;

public class H_DeleteSelfWorkingRequest extends BaseRequest {
	private String UserID;
	private String OrderID;

	public H_DeleteSelfWorkingRequest(String userID, String orderID) {
		super("H_DeleteSelfWorking", "1.0");
		UserID = userID;
		OrderID = orderID;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "H_DeleteSelfWorking"));
	}

	/**
	 * @Title: toJsonString
	 * @Description: 把对象转成json格式的字符串
	 * @author: lee
	 * @param request
	 * @return: String
	 */
	public String toJsonString(H_DeleteSelfWorkingRequest request) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(request);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "H_DeleteSelfWorkingRequest [UserID=" + UserID + ", OrderID=" + OrderID + ", Method=" + Method
				+ ", Infversion=" + Infversion + ", Key=" + Key + ", UID=" + UID + "]";
	}

}
