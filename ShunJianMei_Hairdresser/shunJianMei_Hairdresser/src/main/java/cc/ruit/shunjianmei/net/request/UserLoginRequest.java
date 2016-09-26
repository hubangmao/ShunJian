package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * @ClassName: UserLoginRequest
 * @Description: 登录
 * @author: lee
 * @date: 2015年8月31日 下午11:00:48
 */
public class UserLoginRequest extends BaseRequest {
	private final static String METHOD = "H_UserLogin";
	// 用户名
	private String UserName;
	// 密码
	private String PassWord;
//	UserName
//	PassWord

	/**
	 * @Title:UserLoginRequest
	 * @Description:TODO
	 * @param Mobile 手机号
	 * @param VCode 验证码
	 */
	public UserLoginRequest(String UserName, String PassWord) {
		super(METHOD, "1.0");
		this.UserName = UserName;
		this.PassWord = PassWord;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, METHOD));
	}

	/**
	 * @Title: toJsonString
	 * @Description:  把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(UserLoginRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "UserLoginRequest [UserName=" + UserName + ", PassWord="
				+ PassWord + ", Method=" + Method + ", Infversion="
				+ Infversion + ", Key=" + Key + ", UID=" + UID + "]";
	}

}
