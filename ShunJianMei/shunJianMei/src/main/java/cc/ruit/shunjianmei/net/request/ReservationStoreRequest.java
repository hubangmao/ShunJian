package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * @ClassName: ReservationStoreRequest
 * @Description: 预约商户
 * @author: Johnny
 * @date: 2015年10月14日 下午4:41:29
 */
public class ReservationStoreRequest extends BaseRequest {
	private String UserID;//用户ID
	private String ID;//发型师ID
	private String WHID;//发型师工时ID
	private String Times;//时长(分钟)

	public ReservationStoreRequest(String userID, String id,String whID,String times) {
		super("ReservationStore", "1.0");
		UserID = userID;
		ID = id;
		WHID = whID;
		Times = times;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "ReservationStore"));
	}

	/**
	 * @Title: toJsonString
	 * @Description:  把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(ReservationStoreRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "ReservationStoreRequest [UserID=" + UserID + ", ID=" + ID
				+ ", WHID=" + WHID + ", Times=" + Times + "]";
	}

}
