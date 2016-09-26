package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * 
 * @ClassName: H_UpdateOrderStateRequest
 * @Description:更新订单状态
 * @author: 欧阳
 * @date: 2015年10月21日 下午9:51:31
 */
public class H_UpdateOrderStateRequest extends BaseRequest {
	// 用户id
	private String UserID;
	private String OrderID;
	private String State;

	/**
	 * @Title:ActivityListRequest
	 * @Description:活动
	 * @param UserId
	 */
	public H_UpdateOrderStateRequest(String UserID, String OrderID, String State) {
		super("H_UpdateOrderState", "1.0");
		this.UserID = UserID;
		this.OrderID = OrderID;
		this.State = State;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "H_UpdateOrderState"));
	}

	/**
	 * @Title: toJsonString
	 * @Description: 把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(H_UpdateOrderStateRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "H_UpdateOrderStateRequest [UserID=" + UserID + ", OrderID="
				+ OrderID + ", State=" + State + "]";
	}

}
