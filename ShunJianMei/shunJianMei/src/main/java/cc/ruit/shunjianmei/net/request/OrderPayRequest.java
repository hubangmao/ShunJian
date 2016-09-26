package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * 
 * @ClassName: OrderPayRequest
 * @Description: 提交订单
 * @author: Johnny
 * @date: 2015年10月12日 下午10:23:16
 */
public class OrderPayRequest extends BaseRequest {
	
	private String UserID;// 用户id
	private String OrderID;// 订单ID

	/**
	 * 
	 * @Title:OrderPayRequest
	 * @Description:提交订单
	 * @param UserId
	 */
	public OrderPayRequest(String UserId,String OrderId) {
		super("OrderPay", "1.0");
		UserID = UserId;
		OrderID = OrderId;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "OrderPay"));
	}

	/**
	 * @Title: toJsonString
	 * @Description:  把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(OrderPayRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "OrderPayRequest [UserID=" + UserID + ", OrderID=" + OrderID
				+ "]";
	}

}
