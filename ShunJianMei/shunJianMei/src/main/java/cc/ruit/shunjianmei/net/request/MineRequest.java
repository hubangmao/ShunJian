package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * 
 * @ClassName: MineRequest
 * @Description: 我的主页
 * @author: Johnny
 * @date: 2015年10月12日 下午10:23:16
 */
public class MineRequest extends BaseRequest {
	// 用户id
	private String UserID;

	/**
	 * 
	 * @Title:HomePageRequest
	 * @Description:我的
	 * @param UserId
	 */
	public MineRequest(String UserId) {
		super("Mine", "1.0");
		this.UserID = UserId;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "Mine"));
	}

	/**
	 * @Title: toJsonString
	 * @Description:  把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(MineRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "MineRequest [UserID=" + UserID + "]";
	}

}
