package cc.ruit.shunjianmei.net.request;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

import cc.ruit.shunjianmei.base.BaseRequest;

public class H_SetMasterDefaultSettingRequest extends BaseRequest {
	private String UserID;
	private String WorkDay;
	private String DayWorkHour;
	private String WorkShopID;
	private String WorkShop;
	private String Address;

	public H_SetMasterDefaultSettingRequest(String UserID, String WorkDay, String DayWorkHour, String WorkShopID,String WorkShop,String Address) {
		super("H_SetMasterDefaultSetting", "1.0");
		this.UserID = UserID;
		this.WorkDay = WorkDay;
		this.DayWorkHour = DayWorkHour;
		this.WorkShopID = WorkShopID;
		this.WorkShop = WorkShop;
		this.Address = Address;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "H_SetMasterDefaultSetting"));

	}

	/**
	 * @Title: toJsonString
	 * @Description: 把对象转成json格式的字符串
	 * @author: lee
	 * @param request
	 * @return: String
	 */
	public String toJsonString(H_SetMasterDefaultSettingRequest request) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(request);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "H_SetMasterDefaultSettingRequest [UserID=" + UserID + ", WorkDay=" + WorkDay + ", DayWorkHour="
				+ DayWorkHour + ", WorkShopID=" + WorkShopID + ", WorkShop=" + WorkShop + ", Address=" + Address
				+ ", Method=" + Method + ", Infversion=" + Infversion + ", Key=" + Key + ", UID=" + UID + "]";
	}

}
