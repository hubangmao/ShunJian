package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * 
 * @ClassName: EvaluationListRequest
 * @Description: 发型师评论列表
 * @author: Johnny
 * @date: 2015年10月12日 下午10:23:16
 */
public class EvaluationListRequest extends BaseRequest {

	private String UserID;// 用户id
	private String Hairdresser;// 发型师ID
	private String StyleID;// 0全部
	private String ID;// 评论ID 首次为0

	/**
	 * 
	 * @Title:EvaluationListRequest
	 * @Description:发型师评论列表
	 * @param UserId
	 * @param Hairdresser 
	 * @param StyleID 
	 * @param ID 
	 */
	public EvaluationListRequest(String UserId, String Hairdresser, String StyleID, String ID) {
		super("EvaluationList", "1.0");
		this.UserID = UserId;
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
