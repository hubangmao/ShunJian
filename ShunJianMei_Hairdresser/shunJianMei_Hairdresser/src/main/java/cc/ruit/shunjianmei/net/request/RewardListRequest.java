package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * @ClassName: RewardListRequest
 * @Description: 个人信息获取
 * @author: lee
 * @date: 2015年8月31日 下午11:00:48
 */
public class RewardListRequest extends BaseRequest {
	// 用户id
	private String UserId;
	private String Type;//类别（0全部，1徒弟，2徒孙）

	/**
	 * @Title:RewardListRequest
	 * @Description:TODO
	 * @param UserId
	 * @param Type 类别（0全部，1徒弟，2徒孙）
	 */
	public RewardListRequest(String UserId,String Type) {
		super("RewardList", "1.0");
		this.UserId = UserId;
		this.Type = Type;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "RewardList"));
	}

	/**
	 * @Title: toJsonString
	 * @Description:  把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(RewardListRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "RewardListRequest [UserId=" + UserId + ", Type=" + Type
				+ ", Method=" + Method + ", Infversion=" + Infversion
				+ ", Key=" + Key + ", UID=" + UID + "]";
	}

}
