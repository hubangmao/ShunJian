package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * 
 * @ClassName: WalletPayRequest
 * @Description: 钱包支付
 * @author: Johnny
 * @date: 2015年10月12日 下午10:23:16
 */
public class WalletPayRequest extends BaseRequest {
	
	private String UserID;// 用户id
	private String OrderID;// 订单ID
	private String Amount;// 需支付金额

	/**
	 * 
	 * WalletPayRequest
	 * @Description:钱包支付
	 * @param UserId
	 */
	public WalletPayRequest(String UserId,String OrderId,String amount) {
		super("WalletPay", "1.0");
		UserID = UserId;
		OrderID = OrderId;
		Amount= amount;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "WalletPay"));
	}

	/**
	 * @Title: toJsonString
	 * @Description:  把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(WalletPayRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "WalletPayRequest [UserID=" + UserID + ", OrderID=" + OrderID
				+ ", Amount=" + Amount + "]";
	}

}
