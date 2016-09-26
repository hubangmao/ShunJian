package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * 
 * @ClassName: H_OrderDetailRequest
 * @Description: 订单详情
 * @author: 欧阳
 * @date: 2015年10月19日 上午11:09:57
 */
public class H_OrderDetailRequest extends BaseRequest {
	private String UserID;// 用户ID
	private String OrderID;// 订单ID
	/**
	 * 
	 * @Title:H_OrderDetailRequest
	 * @Description:订单详情接口
	 * @param UserID
	 * @param OrderID
	 */
	public H_OrderDetailRequest(String UserID, String OrderID) {
		super("H_OrderDetail", "1.0");
		this.UserID = UserID;
		this.OrderID = OrderID;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "H_OrderDetail"));
	}

	/**
	 * @Title: toJsonString
	 * @Description: 把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(H_OrderDetailRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "H_OrderDetailRequest [UserID=" + UserID + ", OrderID="
				+ OrderID + "]";
	}

}
