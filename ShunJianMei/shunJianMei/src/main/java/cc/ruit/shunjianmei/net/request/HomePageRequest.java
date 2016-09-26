package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * 
 * @ClassName: HomePageRequest
 * @Description: 首页请求
 * @author: 欧阳
 * @date: 2015年10月20日 上午7:57:40
 */
public class HomePageRequest extends BaseRequest {
	// 用户id
	private String UserId;

	/**
	 * @Title:ActivityListRequest
	 * @Description:活动
	 * @param UserId
	 */
	public HomePageRequest(String UserId) {
		super("HomePage", "1.0");
		this.UserId = UserId;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "HomePage"));
	}

	/**
	 * @Title: toJsonString
	 * @Description: 把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(HomePageRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "HomePageRequest [UserId=" + UserId + "]";
	}

}
