package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * @ClassName: ReservationServiceRequest
 * @Description: 用户预约服务
 * @author: Johnny
 * @date: 2015年10月14日 下午4:41:29
 */
public class ReservationServiceRequest extends BaseRequest {
	private String UserID;//用户ID
	private String ID;//发型店ID
	private String HairStyleID;//发型ID 

	public ReservationServiceRequest(String userID, String id,String hairStyleID) {
		super("ReservationService", "1.0");
		UserID = userID;
		ID = id;
		HairStyleID = hairStyleID;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "ReservationService"));
	}

	/**
	 * @Title: toJsonString
	 * @Description:  把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(ReservationServiceRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "ReservationServiceRequest [UserID=" + UserID + ", ID=" + ID
				+ ", HairStyleID=" + HairStyleID + "]";
	}

}
