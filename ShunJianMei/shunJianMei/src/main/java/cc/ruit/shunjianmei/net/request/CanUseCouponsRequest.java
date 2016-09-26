package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * @ClassName: CanUseCouponsRequest
 * @Description: 可用优惠券
 * @author: Johnny
 * @date: 2015年10月14日 下午4:41:29
 */
public class CanUseCouponsRequest extends BaseRequest {
	private String UserID;//用户ID
	private String Amount;//金额
	private String Code;//金额
	

	public CanUseCouponsRequest(String userID, 
			String amount,String code) {
		super("CanUseCoupons", "1.0");
		this.UserID = userID;
		this.Amount = amount;
		this.Code = code;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "CanUseCoupons"));
	}

	/**
	 * @Title: toJsonString
	 * @Description:  把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(CanUseCouponsRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "CanUseCouponsRequest [UserID=" + UserID + ", Amount=" + Amount
				+ "]";
	}

}
