package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * 
 * @ClassName: InitEvaluationRequest
 * @Description: 初始化评论接口
 * @author: 欧阳
 * @date: 2015年10月29日 下午3:14:08
 */
public class InitEvaluationRequest extends BaseRequest {
	// 用户id
	private String UserID;// 用户ID
	private String OrderID;// 评论ID

	/**
	 * @Title:ActivityListRequest
	 * @Description:活动
	 * @param UserId
	 */
	public InitEvaluationRequest(String UserID, String OrderID) {
		super("InitEvaluation", "1.0");
		this.UserID = UserID;
		this.OrderID = OrderID;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "InitEvaluation"));
	}

	/**
	 * @Title: toJsonString
	 * @Description: 把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(InitEvaluationRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "InitEvaluationRequest [UserID=" + UserID + ", OrderID="
				+ OrderID + "]";
	}

}
