package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * 
 * @ClassName: AdditionalOrderRequest
 * @Description: 加单请求
 * @author: 欧阳
 * @date: 2015年10月22日 下午2:31:45
 */
public class AdditionalOrderRequest extends BaseRequest {
	// 用户id
	private String UserID;
	private String OrderID;
	private String Content;
	private String Amount;

	public AdditionalOrderRequest(String UserID, String OrderID,
			String Content, String Amount) {
		super("AdditionalOrder", "1.0");
		this.UserID = UserID;
		this.OrderID = OrderID;
		this.Content = Content;
		this.Amount = Amount;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "AdditionalOrder"));
	}

	/**
	 * @Title: toJsonString
	 * @Description: 把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(AdditionalOrderRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "AdditionalOrderRequest [UserID=" + UserID + ", OrderID="
				+ OrderID + ", Content=" + Content + ", Amount=" + Amount + "]";
	}

}
