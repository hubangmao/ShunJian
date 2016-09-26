package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * @ClassName: UpdateOrderCouponRequest
 * @Description: 更新订单优惠券
 * @author: Johnny
 * @date: 2015年10月14日 下午4:41:29
 */
public class UpdateOrderCouponRequest extends BaseRequest {
	private String UserID;//用户ID
	private String OrderID;//订单ID
	private String ID;//优惠券ID

	public UpdateOrderCouponRequest(String userID, 
			String orderID, String ID) {
		super("UpdateOrderCoupon", "1.0");
		this.UserID = userID;
		this.OrderID = orderID;
		this.ID = ID;
		
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "UpdateOrderCoupon"));
	}

	/**
	 * @Title: toJsonString
	 * @Description:  把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(UpdateOrderCouponRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "UpdateOrderCouponRequest [UserID=" + UserID + ", OrderID="
				+ OrderID + ", ID=" + ID + "]";
	}

}
