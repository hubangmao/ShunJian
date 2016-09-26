package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * 
 * @ClassName: EvaluationListRequest
 * @Description: 发型师评论列表
 * @author: 欧阳
 * @date: 2015年10月24日 上午11:38:08
 */
public class EvaluationListRequest extends BaseRequest {
	private String UserID;//用户ID
	private String Hairdresser;//发型师ID
	private String StyleID;//0全部 发型类型
	private String ID;//评论ID 首次为0 

	/**
	 * @Title:ActivityListRequest
	 * @Description:活动
	 * @param UserId
	 */
	public EvaluationListRequest(String UserID, String Hairdresser,
			String StyleID, String ID) {
		super("EvaluationList", "1.0");
		this.UserID = UserID;
		this.Hairdresser = Hairdresser;
		this.StyleID = StyleID;
		this.ID = ID;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "EvaluationList"));
	}

	/**
	 * @Title: toJsonString
	 * @Description: 把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(EvaluationListRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "EvaluationListRequest [UserID=" + UserID + ", Hairdresser="
				+ Hairdresser + ", StyleID=" + StyleID + ", ID=" + ID + "]";
	}

}
