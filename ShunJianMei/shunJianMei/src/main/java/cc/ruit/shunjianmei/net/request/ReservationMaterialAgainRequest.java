package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * @ClassName: ReservationMaterialRequest
 * @Description: 预约物料
 * @author: Johnny
 * @date: 2015年10月14日 下午4:41:29
 */
public class ReservationMaterialAgainRequest extends BaseRequest {
	private String UserID;//用户ID
	private String OrderID;//发型项目ID

	public ReservationMaterialAgainRequest(String userID, String orderID) {
		super("ReservationMaterialAgain", "1.0");
		UserID = userID;
		OrderID = orderID;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "ReservationMaterialAgain"));
	}

	/**
	 * @Title: toJsonString
	 * @Description:  把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(ReservationMaterialAgainRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "ReservationMaterialAgainRequest [UserID=" + UserID
				+ ", OrderID=" + OrderID + ", Method=" + Method
				+ ", Infversion=" + Infversion + ", Key=" + Key + ", UID="
				+ UID + "]";
	}

}
