package cc.ruit.shunjianmei.net.request;

import android.util.Base64;
import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitBase64;
import com.oruit.oruitkey.OruitKey;

/**
 * @ClassName: FeedbackRequest
 * @Description: 用户反馈入参
 * @author: Johnny
 * @date: 2015年8月31日 下午11:00:48
 */
public class FeedbackRequest extends BaseRequest {
	
	private String UserID;// 用户id
	private String Contact;//联系方式  手机或邮箱
	private String Content;//内容

	public FeedbackRequest(String userId, String content,String contact) {
		super("H_Feedback", "1.0");
		UserID = userId;
		Contact = contact;
		Content = OruitBase64.encode(content.getBytes());
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "H_Feedback"));
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
		return "FeedbackRequest [UserID=" + UserID + ", Contact=" + Contact
				+ ", Content=" + Content + ", Method=" + Method
				+ ", Infversion=" + Infversion + ", Key=" + Key + ", UID="
				+ UID + "]";
	}

}
