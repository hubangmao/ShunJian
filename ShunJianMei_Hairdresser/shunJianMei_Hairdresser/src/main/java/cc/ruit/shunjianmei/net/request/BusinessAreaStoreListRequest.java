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
public class BusinessAreaStoreListRequest extends BaseRequest {
	// 用户id
	private String UserId;
	private String ID;

	/**
	 * @Title:UserLoginRequest
	 * @Description:TODO
	 * @param ID
	 *            商圈id
	 */
	public BusinessAreaStoreListRequest(String UserId, String ID) {
		super("H_BusinessAreaStoreList", "1.0");
		this.UserId = UserId;
		this.ID = ID;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "H_BusinessAreaStoreList"));
	}

	/**
	 * @Title: toJsonString
	 * @Description: 把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(BusinessAreaStoreListRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "BusinessAreaStoreListRequest [UserId=" + UserId + ", Method="
				+ Method + ", Infversion=" + Infversion + ", Key=" + Key
				+ ", ID=" + ID + ", UID=" + UID + "]";
	}

}
