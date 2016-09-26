package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;
/**
 * @ClassName: UserRegisterRequest
 * @Description: 注册
 * @author: lee
 * @date: 2015年10月22日 下午3:56:21
 */
public class UserRegisterRequest extends BaseRequest {
	private String UserName;
	private String PassWord;
	private String VCode;
	private String Referee;
	private String Device;

	/**
	 * 
	 * @Title:FindPassWordRequest
	 * @Description: 查找密码
	 * @param userName 用户名
	 * @param passWord 密码
	 * @param vCode 验证码
	 */
	public UserRegisterRequest(String userName, String passWord, String vCode, String Referee, String Device) {
		super("UserRegister", "1.0");
		this.UserName = userName;
		this.PassWord = passWord;
		this.VCode = vCode;
		this.Referee = Referee;
		this.Device = Device;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "UserRegister"));
	}

	// 把对象转成json格式的字符串
	public static String toJsonString(UserRegisterRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "UserRegisterRequest [UserName=" + UserName + ", PassWord="
				+ PassWord + ", VCode=" + VCode + ", Referee=" + Referee
				+ ", Method=" + Method + ", Infversion=" + Infversion
				+ ", Key=" + Key + ", UID=" + UID + ", Device=" + Device + "]";
	}
	

}
