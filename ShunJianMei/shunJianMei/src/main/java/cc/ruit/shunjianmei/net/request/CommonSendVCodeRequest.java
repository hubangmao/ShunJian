package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

public class CommonSendVCodeRequest extends BaseRequest {
	private String Mobile;
	private String Type;

	/**
	 * 
	 * @Title:CommonSendVCodeRequest
	 * @Description: 获取手机验证码
	 * @param mobile
	 *            手机号码
	 * @param type
	 *            验证码类别(1注册2找回密码)
	 */
	public CommonSendVCodeRequest(String mobile, String type) {
		super("CommonSendVCode", "1.0");
		this.Mobile = mobile;
		this.Type = type;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "CommonSendVCode"));
	}

	// 把对象转成json格式的字符串
	public String toJsonString(CommonSendVCodeRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "CommonSendVCodeRequest [Mobile=" + Mobile + ", Type=" + Type
				+ "]";
	}

}
