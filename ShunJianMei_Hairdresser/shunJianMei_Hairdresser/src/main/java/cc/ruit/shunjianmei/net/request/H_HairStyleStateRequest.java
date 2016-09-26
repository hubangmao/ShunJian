package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * 
 * @ClassName: H_HairStyleStateRequest
 * @Description: 更新我的发型状态
 * @author: 欧阳
 * @date: 2015年10月18日 上午10:07:00
 */
public class H_HairStyleStateRequest extends BaseRequest {
	// 用户id
	private String UserID;// 用户ID
	private String ID;// 发型ID
	private String State;// 状态 2撤销 3发布

	/**
	 * @Title:ActivityListRequest
	 * @Description:活动
	 * @param UserId
	 */
	public H_HairStyleStateRequest(String UserID, String ID, String State) {
		super("H_HairStyleState", "1.0");
		this.ID = ID;
		this.UserID = UserID;
		this.State = State;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "H_HairStyleState"));
	}

	/**
	 * @Title: toJsonString
	 * @Description: 把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(H_HairStyleStateRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "H_HairStyleStateRequest [UserID=" + UserID + ", ID=" + ID
				+ ", State=" + State + "]";
	}

}
