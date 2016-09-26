package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * @ClassName: MyInfoRequest
 * @Description: 个人信息获取
 * @author: lee
 * @date: 2015年8月31日 下午11:00:48
 */
public class MyInfoRequest extends BaseRequest {
	// 用户id
	private String UserId;

	/**
	 * @Title:UserLoginRequest
	 * @Description:TODO
	 * @param Mobile 手机号
	 * @param VCode 验证码
	 */
	public MyInfoRequest(String UserId) {
		super("MyInfo", "1.0");
		this.UserId = UserId;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "MyInfo"));
	}

	/**
	 * @Title: toJsonString
	 * @Description:  把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(MyInfoRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "HomeInfoRequest [UserId=" + UserId + ", Method=" + Method
				+ ", Infversion=" + Infversion + ", Key=" + Key + ", UID="
				+ UID + "]";
	}

}
