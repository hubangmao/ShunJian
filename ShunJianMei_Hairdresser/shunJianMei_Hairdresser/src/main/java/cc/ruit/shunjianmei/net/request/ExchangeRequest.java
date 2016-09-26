package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * @ClassName: ExchangeRequest
 * @Description: 流量兑换
 * @author: lee
 * @date: 2015年8月31日 下午11:00:48
 */
public class ExchangeRequest extends BaseRequest {
	private String UserId;//用户ID
	private String Id;//商品编号
	private String Num;//数量（默认1）
	private String Mobile;//手机号

	public ExchangeRequest(String UserId,
			String Id, String Num, String Mobile) {
		super("Exchange", "1.0");
		this.UserId = UserId;
		this.Id = Id;
		this.Num = Num;
		this.Mobile = Mobile;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "Exchange"));
	}

	/**
	 * @Title: toJsonString
	 * @Description:  把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(ExchangeRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "ExchangeRequest [UserId=" + UserId + ", Id=" + Id + ", Num="
				+ Num + ", Mobile=" + Mobile + "]";
	}

}
