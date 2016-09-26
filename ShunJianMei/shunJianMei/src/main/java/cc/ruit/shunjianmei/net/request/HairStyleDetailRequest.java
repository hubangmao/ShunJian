package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * 
 * @ClassName: HairStyleDetailRequest
 * @Description: 发型列表详情
 * @author: 欧阳
 * @date: 2015年10月25日 下午1:34:06
 */
public class HairStyleDetailRequest extends BaseRequest {
	// 用户id
	private String UserID;// 用户ID
	private String ID;// 发型ID

	/**
	 * @Title:ActivityListRequest
	 * @Description:活动
	 * @param UserId
	 */
	public HairStyleDetailRequest(String UserID, String ID) {
		super("HairStyleDetail", "1.0");
		this.UserID = UserID;
		this.ID = ID;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "HairStyleDetail"));
	}

	/**
	 * @Title: toJsonString
	 * @Description: 把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(HairStyleDetailRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "HairStyleDetailRequest [UserID=" + UserID + ", ID=" + ID + "]";
	}

}
