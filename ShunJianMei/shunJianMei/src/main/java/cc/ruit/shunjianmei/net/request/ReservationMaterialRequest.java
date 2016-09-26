package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * @ClassName: ReservationMaterialRequest
 * @Description: 预约物料
 * @author: Johnny
 * @date: 2015年10月14日 下午4:41:29
 */
public class ReservationMaterialRequest extends BaseRequest {
	private String UserID;//用户ID
	private String ID;//发型项目ID

	public ReservationMaterialRequest(String userID, String id) {
		super("ReservationMaterial", "1.0");
		UserID = userID;
		ID = id;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "ReservationMaterial"));
	}

	/**
	 * @Title: toJsonString
	 * @Description:  把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(ReservationMaterialRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "ReservationMaterialRequest [UserID=" + UserID + ", ID=" + ID
				+ "]";
	}

}
