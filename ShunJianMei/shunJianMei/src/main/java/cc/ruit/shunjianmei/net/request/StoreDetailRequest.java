package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * @ClassName: StoreDetailRequest
 * @Description: 店铺详情
 * @author: Johnny
 * @date: 2015年10月14日 下午4:41:29
 */
public class StoreDetailRequest extends BaseRequest {
	private String UserID;//用户ID
	private String ID;//发型店ID
	private String Latitude;//纬度
	private String Longitude;//经度
	

	public StoreDetailRequest(String userID, String cityID,String latitude,String longitude) {
		super("StoreDetail", "1.0");
		this.UserID = userID;
		this.ID = cityID;
		Latitude = latitude;
		Longitude = longitude;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "StoreDetail"));
	}

	/**
	 * @Title: toJsonString
	 * @Description:  把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(StoreDetailRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "StoreDetailRequest [UserID=" + UserID + ", ID=" + ID
				+ ", Latitude=" + Latitude + ", Longitude=" + Longitude + "]";
	}

}
