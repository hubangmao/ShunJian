package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * @ClassName: OrderListRequest
 * @Description: 订单列表
 * @author: lee
 * @date: 2015年8月31日 下午11:00:48
 */
public class ScheduleOrderListRequest extends BaseRequest {
	private String UserID;//用户ID
	private String Date;//日期 "2015-9-29"
	public ScheduleOrderListRequest(String userId, String date) {
		super("H_ScheduleOrderList", "1.0");
		UserID = userId;
		Date = date;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "H_ScheduleOrderList"));
	}

	/**
	 * @Title: toJsonString
	 * @Description:  把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(ScheduleOrderListRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "ScheduleOrderListRequest [UserID=" + UserID + ", Date=" + Date
				+ ", Method=" + Method + ", Infversion=" + Infversion
				+ ", Key=" + Key + ", UID=" + UID + "]";
	}

}
