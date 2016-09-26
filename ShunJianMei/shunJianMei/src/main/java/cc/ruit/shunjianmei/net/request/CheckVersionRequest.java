package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;
/**
 * @ClassName: CheckVersionRequest
 * @Description: 版本检测
 * @author: lee
 * @date: 2015年9月12日 下午4:50:12
 */
public class CheckVersionRequest extends BaseRequest {

	private String VersionCode;
	private String SystemType;

	/**
	 * 
	 * @Title:CheckVersionRequest
	 * @Description: 检测版本
	 * @param versionCode
	 *            版本号
	 * @param systemType
	 *            系统类型
	 */
	public CheckVersionRequest(String versionCode, String systemType) {
		super("CheckVersion", "1.0");
		VersionCode = versionCode;
		SystemType = systemType;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "CheckVersion"));
	}

	// 把对象转成json格式的字符串
	public static String toJsonString(CheckVersionRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "CheckVersionRequest [VersionCode=" + VersionCode
				+ ", SystemType=" + SystemType + ", Method=" + Method
				+ ", Infversion=" + Infversion + ", Key=" + Key + ", UID="
				+ UID + "]";
	}

}
