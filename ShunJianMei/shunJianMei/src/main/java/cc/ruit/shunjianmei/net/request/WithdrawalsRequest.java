package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * @ClassName: WithdrawalsRequest
 * @Description: 支付宝提现
 * @author: lee
 * @date: 2015年8月31日 下午11:00:48
 */
public class WithdrawalsRequest extends BaseRequest {
	private String Money;//提现金额
	private String UserId;//用户ID
	private String AlipayId;//支付宝账号
	private String Payee;//收款人姓名

	
	public WithdrawalsRequest(String money,
			String userId, String alipayId, String payee) {
		super("Withdrawals", "1.0");
		Money = money;
		UserId = userId;
		AlipayId = alipayId;
		Payee = payee;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "Withdrawals"));
	}

	/**
	 * @Title: toJsonString
	 * @Description:  把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(WithdrawalsRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "WithdrawalsRequest [Money=" + Money + ", UserId=" + UserId
				+ ", AlipayId=" + AlipayId + ", Payee=" + Payee + ", Method="
				+ Method + ", Infversion=" + Infversion + ", Key=" + Key
				+ ", UID=" + UID + "]";
	}

}
