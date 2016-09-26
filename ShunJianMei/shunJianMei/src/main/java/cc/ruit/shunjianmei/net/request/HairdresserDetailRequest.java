package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * 
 * @ClassName: HairdresserDetailRequest
 * @Description: 发型师详情列表
 * @author: 欧阳
 * @date: 2015年10月23日 下午4:12:11
 */
public class HairdresserDetailRequest extends BaseRequest {
	private String UserID;// 用户ID
	private String ID;// 发型师ID

	/**
	 * 
	 * @Title:HairdresserDetailRequest
	 * @Description:发型师详情列表
	 * @param UserID
	 * @param ID
	 */
	public HairdresserDetailRequest(String UserID, String ID) {
		super("HairdresserDetail", "1.0");
		this.UserID = UserID;
		this.ID = ID;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "HairdresserDetail"));
	}

	/**
	 * @Title: toJsonString
	 * @Description: 把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(HairdresserDetailRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "HairdresserDetailRequest [UserID=" + UserID + ", ID=" + ID + "]";
	}

}
