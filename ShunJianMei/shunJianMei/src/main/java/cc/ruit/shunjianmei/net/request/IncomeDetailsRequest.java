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
public class IncomeDetailsRequest extends BaseRequest {
	private String UserId;//用户ID
	private String Type;//类型类型（0全部；1任务，2徒弟）
	private String PageSize;//分页大小
	private String PageStart;//开始编号

	public IncomeDetailsRequest(String userId, String type, String pageSize, String pageStart) {
		super("IncomeDetails", "1.0");
		UserId = userId;
		Type = type;
		PageSize = pageSize;
		PageStart = pageStart;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "IncomeDetails"));
	}

	/**
	 * @Title: toJsonString
	 * @Description:  把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(IncomeDetailsRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "IncomeDetails [UserId=" + UserId + ", Type=" + Type
				+ ", PageSize=" + PageSize + ", PageStart=" + PageStart
				+ ", Method=" + Method + ", Infversion=" + Infversion
				+ ", Key=" + Key + ", UID=" + UID + "]";
	}

}
