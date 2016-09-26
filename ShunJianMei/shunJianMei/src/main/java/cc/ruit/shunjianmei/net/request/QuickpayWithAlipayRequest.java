package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * @ClassName: QuickpayWithAlipayRequest
 * @Description: 支付宝开始闪惠的请求
 * @author: gaoj
 * @date: 2015年10月14日 下午4:41:29
 */
public class QuickpayWithAlipayRequest extends BaseRequest {
	private String UserID;//用户ID
	private String StoreID;//发型店ID
	private String MasterName;	// 美发师姓名
	private String OriginalPrice;	// 支付的原价
	

	public QuickpayWithAlipayRequest(String userID, String storeID, String masterName, String originalPrice) {
		super("QuickpayWithAlipay", "1.0");
		this.UserID = userID;
		this.StoreID = storeID;
		this.MasterName = masterName;
		this.OriginalPrice = originalPrice;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "QuickpayWithAlipay"));
	}

	/**
	 * @Title: toJsonString
	 * @Description:  把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(QuickpayWithAlipayRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "QuickpayWithAlipayRequest [UserID=" + UserID + ", StoreID=" + StoreID 
				+ ", MasterName=" + MasterName + ", OriginalPrice=" + OriginalPrice
				+ "]";
	}

}
