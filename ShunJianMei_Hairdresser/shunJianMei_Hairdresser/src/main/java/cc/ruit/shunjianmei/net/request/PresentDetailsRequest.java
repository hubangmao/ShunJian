package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * @ClassName: PresentDetailsRequest
 * @Description: 提现明细
 * @author: lee
 * @date: 2015年8月31日 下午11:00:48
 */
public class PresentDetailsRequest extends BaseRequest {
	private String UserId;//用户ID
	private String Type;//类型（0全部；1支付宝，2兑换）
	private String PageSize;//分页大小
	private String PageStart;//开始编号

	public PresentDetailsRequest(String userId, String type, String pageSize, String pageStart) {
		super("PresentDetails", "1.0");
		UserId = userId;
		Type = type;
		PageSize = pageSize;
		PageStart = pageStart;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "PresentDetails"));
	}

	/**
	 * @Title: toJsonString
	 * @Description:  把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(PresentDetailsRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "PresentDetailsRequest [UserId=" + UserId + ", Type=" + Type
				+ ", PageSize=" + PageSize + ", PageStart=" + PageStart
				+ ", Method=" + Method + ", Infversion=" + Infversion
				+ ", Key=" + Key + ", UID=" + UID + "]";
	}

}
