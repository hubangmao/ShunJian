package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * 
 * @ClassName: MyBalanceRequest
 * @Description: 我的余额
 * @author: Johnny
 * @date: 2015年10月12日 下午10:23:16
 */
public class MyBalanceRequest extends BaseRequest {
	// 用户id
	private String UserID;

	/**
	 * 
	 * @Title:MyBalanceRequest
	 * @Description:我的
	 * @param UserId
	 */
	public MyBalanceRequest(String UserId) {
		super("MyBalance", "1.0");
		this.UserID = UserId;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "MyBalance"));
	}

	/**
	 * @Title: toJsonString
	 * @Description:  把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(MyBalanceRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "MyBalanceRequest [UserID=" + UserID + ", Method=" + Method
				+ ", Infversion=" + Infversion + ", Key=" + Key + ", UID="
				+ UID + "]";
	}

}
