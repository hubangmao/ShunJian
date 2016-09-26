package cc.ruit.shunjianmei.net.request;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

import cc.ruit.shunjianmei.base.BaseRequest;

public class UpdateRecordStateRequest extends BaseRequest {
	// 用户id
	private String UserId;
	private String ID;

	public UpdateRecordStateRequest(String UserId,String ID) {
		super("H_UpdateRecordState", "1.0");
		this.UserId = UserId;
		this.ID = ID;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "H_UpdateRecordState"));
	}

	/**
	 * @Title: toJsonString
	 * @Description: 把对象转成json格式的字符串
	 * @author: lee
	 * @param request
	 * @return: String
	 */
	public String toJsonString(UpdateRecordStateRequest request) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(request);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "H_MaterialApply [UserId=" + UserId + ", Method=" + Method
				+ ", Infversion=" + Infversion + ", Key=" + Key + ", ID=" + ID + ", UID="
				+ UID + "]";
	}
}
