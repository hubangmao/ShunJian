package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * 
 * @ClassName: MeRequest
 * @Description: 我的主页
 * @author: Johnny
 * @date: 2015年10月12日 下午10:23:16
 */
public class MeRequest extends BaseRequest {
	// 用户id
	private String UserID;

	/**
	 * 
	 * @Title:HomePageRequest
	 * @Description:我的
	 * @param UserId
	 */
	public MeRequest(String UserId) {
		super("H_HomePage", "1.0");
		this.UserID = UserId;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "H_HomePage"));
	}

	/**
	 * @Title: toJsonString
	 * @Description:  把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(MeRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "MeRequest [UserID=" + UserID + ", Method=" + Method
				+ ", Infversion=" + Infversion + ", Key=" + Key + ", UID="
				+ UID + "]";
	}

}
