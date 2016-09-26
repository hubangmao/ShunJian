package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * @ClassName: ActivityListRequest
 * @Description: 活动列表
 * @author: lee
 * @date: 2015年8月31日 下午11:00:48
 */
public class ActivityListRequest extends BaseRequest {
	// 用户id
	private String UserId;
	private String Type;

	/**
	 * @Title:ActivityListRequest
	 * @Description:活动
	 * @param UserId
	 */
	public ActivityListRequest(String UserId,String Type) {
		super("ActivityList", "1.0");
		this.UserId = UserId;
		this.Type = Type;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "ActivityList"));
	}

	/**
	 * @Title: toJsonString
	 * @Description:  把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(ActivityListRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "ActivityListRequest [UserId=" + UserId + ", Type=" + Type
				+ ", Method=" + Method + ", Infversion=" + Infversion
				+ ", Key=" + Key + ", UID=" + UID + "]";
	}

}
