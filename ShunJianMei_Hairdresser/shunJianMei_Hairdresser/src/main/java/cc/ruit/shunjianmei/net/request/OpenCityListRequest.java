package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * 
 * @ClassName: OpenCityListRequest
 * @Description: 开通城市
 * @author: 欧阳
 * @date: 2015年10月20日 上午10:28:48
 */
public class OpenCityListRequest extends BaseRequest {
	// 用户id
	private String UserId;

	/**
	 * @Title:ActivityListRequest
	 * @Description:活动
	 * @param UserId
	 */
	public OpenCityListRequest(String UserId) {
		super("OpenCityList", "1.0");
		this.UserId = UserId;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "OpenCityList"));
	}

	/**
	 * @Title: toJsonString
	 * @Description: 把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(OpenCityListRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "OpenCityListRequest [UserId=" + UserId + "]";
	}

}
