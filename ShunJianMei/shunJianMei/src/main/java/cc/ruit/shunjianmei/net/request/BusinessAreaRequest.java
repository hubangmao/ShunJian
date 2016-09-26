package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * 
 * @ClassName: BusinessAreaRequest
 * @Description: 商圈信息
 * @author: 欧阳
 * @date: 2015年10月21日 上午11:09:32
 */
public class BusinessAreaRequest extends BaseRequest {
	// 用户id
	private String UserID;//用户ID
	private String CityID;//城市ID

	public BusinessAreaRequest(String UserID, String CityID) {
		super("BusinessArea", "1.0");
		this.UserID = UserID;
		this.CityID = CityID;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "BusinessArea"));
	}

	/**
	 * @Title: toJsonString
	 * @Description: 把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(BusinessAreaRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "BusinessAreaRequest [UserID=" + UserID + ", CityID=" + CityID
				+ "]";
	}

}
