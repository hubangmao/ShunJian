package cc.ruit.shunjianmei.net.request;

import java.util.List;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

import cc.ruit.shunjianmei.base.BaseRequest;
import cc.ruit.shunjianmei.net.response.SaveHairdresserScheduleResponse;

public class SaveHairdresserScheduleRequest extends BaseRequest {
	// 用户id
	private String UserID;
	private String Date;
	private List<SaveHairdresserScheduleResponse> Item;
	private String Address;

	public SaveHairdresserScheduleRequest(String UserID, String Date,
			List<SaveHairdresserScheduleResponse> Item, String Address) {
		super("H_SaveHairdresserSchedule", "1.0");
		this.UserID = UserID;
		this.Item = Item;
		this.Address = Address;
		this.Date = Date;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "H_SaveHairdresserSchedule"));
	}

	/**
	 * @Title: toJsonString
	 * @Description: 把对象转成json格式的字符串
	 * @author: lee
	 * @param request
	 * @return: String
	 */
	public String toJsonString(SaveHairdresserScheduleRequest request) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(request);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "H_SaveHairdresserScheduleRequest [UserID=" + UserID
				+ ", Method=" + Method + ", Infversion=" + Infversion
				+ ", Key=" + Key + ", Item=" + Item + ", Address=" + Address
				+ ", Date=" + Date + ", UID=" + UID + "]";
	}
}
