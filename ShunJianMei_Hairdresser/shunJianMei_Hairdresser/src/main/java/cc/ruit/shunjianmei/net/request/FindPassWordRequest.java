package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

public class FindPassWordRequest extends BaseRequest {
	private String UserName;
	private String PassWord;
	private String VCode;
	private String Type;//2找回密码4发型师找回密码;

	/**
	 * 
	 * @Title:FindPassWordRequest
	 * @Description: 查找密码
	 * @param userName 用户名
	 * @param passWord 密码
	 * @param vCode 验证码
	 */
	public FindPassWordRequest(String userName, String passWord, String vCode) {
		super("FindPassWord", "1.0");
		this.UserName = userName;
		this.PassWord = passWord;
		this.VCode = vCode;
		this.Type = "4";
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "FindPassWord"));
	}

	// 把对象转成json格式的字符串
	public static String toJsonString(FindPassWordRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "FindPassWordRequest [UserName=" + UserName + ", PassWord="
				+ PassWord + ", VCode=" + VCode + "]";
	}
	

}
