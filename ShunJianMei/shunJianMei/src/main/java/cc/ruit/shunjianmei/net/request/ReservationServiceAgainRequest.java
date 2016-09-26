package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * @ClassName: ReservationServiceAgainRequest
 * @Description: 用户再次预约服务
 * @author: lee
 * @date: 2015年11月12日 下午3:53:54
 */
public class ReservationServiceAgainRequest extends BaseRequest {
	private String UserID;//用户ID
	private String OrderID;//订单ID

	public ReservationServiceAgainRequest(String userID, String orderId) {
		super("ReservationServiceAgain", "1.0");
		UserID = userID;
		OrderID = orderId;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "ReservationServiceAgain"));
	}

	/**
	 * @Title: toJsonString
	 * @Description:  把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(ReservationServiceAgainRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "ReservationServiceAgainRequest [UserID=" + UserID
				+ ", OrderID=" + OrderID + ", Method=" + Method
				+ ", Infversion=" + Infversion + ", Key=" + Key + ", UID="
				+ UID + "]";
	}

}
