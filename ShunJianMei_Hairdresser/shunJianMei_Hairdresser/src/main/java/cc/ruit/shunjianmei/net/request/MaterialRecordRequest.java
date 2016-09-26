package cc.ruit.shunjianmei.net.request;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

import cc.ruit.shunjianmei.base.BaseRequest;

public class MaterialRecordRequest extends BaseRequest {
	// 用户id
	private String UserID;
	private String ID;

	public MaterialRecordRequest(String UserID,String ID) {
		super("H_MaterialRecord", "1.0");
		this.UserID = UserID;
		this.ID = ID;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "H_MaterialRecord"));
	}

	/**
	 * @Title: toJsonString
	 * @Description: 把对象转成json格式的字符串
	 * @author: lee
	 * @param request
	 * @return: String
	 */
	public String toJsonString(MaterialRecordRequest request) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(request);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "MaterialRecordRequest [UserID=" + UserID + ", ID=" + ID
				+ ", Method=" + Method + ", Infversion=" + Infversion
				+ ", Key=" + Key + ", UID=" + UID + "]";
	}
}
