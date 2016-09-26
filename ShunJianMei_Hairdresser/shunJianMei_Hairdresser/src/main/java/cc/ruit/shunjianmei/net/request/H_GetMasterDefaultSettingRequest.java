package cc.ruit.shunjianmei.net.request;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

import cc.ruit.shunjianmei.base.BaseRequest;

/**
 * 一键设置，获取数据，发送请求
 * 
 * @author Administrator
 *
 */
public class H_GetMasterDefaultSettingRequest extends BaseRequest {
	private String UserID;// 用户id

	public H_GetMasterDefaultSettingRequest(String id) {
		super("H_GetMasterDefaultSetting", "1.0");
		this.UserID = id;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "H_GetMasterDefaultSetting"));
	}

	/**
	 * @Title: toJsonString
	 * @Description: 把对象转成json格式的字符串
	 * @author: lee
	 * @param request
	 * @return: String
	 */
	public String toJsonString(H_GetMasterDefaultSettingRequest request) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(request);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "H_GetMasterDefaultSettingRequest [UserID=" + UserID + ", Method=" + Method + ", Infversion="
				+ Infversion + ", Key=" + Key + ", UID=" + UID + "]";
	}

}
