package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * @ClassName: MessageInfoRequest
 * @Description: 消除红点
 * @author: lee
 * @date: 2015年8月31日 下午11:00:48
 */
public class MessageInfoRequest extends BaseRequest {
	private String UserId;//用户ID
	private String Id;//消息Id
	public MessageInfoRequest(String userId, String id) {
		super("MessageInfo", "1.0");
		UserId = userId;
		Id = id;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "MessageInfo"));
	}

	/**
	 * @Title: toJsonString
	 * @Description:  把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(MessageInfoRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "MessageInfoRequest [UserId=" + UserId + ", Id=" + Id
				+ ", Method=" + Method + ", Infversion=" + Infversion
				+ ", Key=" + Key + ", UID=" + UID + "]";
	}

}
