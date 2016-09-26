package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * 
 * @ClassName: StoreEvaluationListRequest
 * @Description: 商户评论列表
 * @author: Johnny
 * @date: 2015年10月12日 下午10:23:16
 */
public class StoreEvaluationListRequest extends BaseRequest {

	private String UserID;// 用户id
	private String StoreID;// 商户ID
	private String ID;// 评论ID 首次为0

	/**
	 * 
	 * @Title:StoreEvaluationListRequest
	 * @Description:商户评论列表
	 * @param UserId
	 * @param Hairdresser 
	 * @param StyleID 
	 * @param ID 
	 */
	public StoreEvaluationListRequest(String UserId, String storeID, String id) {
		super("StoreEvaluationList", "1.0");
		UserID = UserId;
		StoreID = storeID;
		ID = id;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "StoreEvaluationList"));
	}

	/**
	 * @Title: toJsonString
	 * @Description: 把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(StoreEvaluationListRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "StoreEvaluationListRequest [UserID=" + UserID + ", StoreID="
				+ StoreID + ", ID=" + ID + "]";
	}

}
