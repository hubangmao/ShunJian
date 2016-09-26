package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * @ClassName: FeedbackRequest
 * @Description: 登录
 * @author: lee
 * @date: 2015年8月31日 下午11:00:48
 */
public class FeedbackRequest extends BaseRequest {
	
	private String UserID;// 用户id
	private String Content;//内容
	private String Contact;//联系方式

	public FeedbackRequest(String UserID, String contact, String content) {
		super("Feedback", "1.0");
		this.UserID = UserID;
		this.Contact = contact;
		this.Content = content;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "Feedback"));
	}

	/**
	 * @Title: toJsonString
	 * @Description:  把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(FeedbackRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "FeedbackRequest [UserID=" + UserID + ", Content=" + Content
				+ ", Contact=" + Contact + "]";
	}

}
