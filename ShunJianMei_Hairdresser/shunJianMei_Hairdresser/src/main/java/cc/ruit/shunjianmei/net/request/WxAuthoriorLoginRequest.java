package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * @ClassName: UserLoginRequest
 * @Description: 登录
 * @author: lee
 * @date: 2015年8月31日 下午11:00:48
 */
public class WxAuthoriorLoginRequest extends BaseRequest {
	// 微信OpenId
	private String OpenId;
	// Unionid
	private String UnionId;
	private String NickName;//昵称
	private String Photo;//头像

	/**
	 * @Title:WxAuthoriorLoginRequest
	 * @Description:TODO
	 * @param Mobile// 微信OpenId
	 * @param UnionId//Unionid
	 * @param NickName//昵称
	 * @param Photo//头像
	 */
	public WxAuthoriorLoginRequest(String OpenId, String UnionId, String NickName, String Photo) {
		super("WxAuthoriorLogin", "1.0");
		this.OpenId = OpenId;
		this.UnionId = UnionId;
		this.NickName = NickName;
		this.Photo = Photo;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "WxAuthoriorLogin"));
	}

	/**
	 * @Title: toJsonString
	 * @Description:  把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(WxAuthoriorLoginRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "WxAuthoriorLoginRequest [OpenId=" + OpenId + ", UnionId="
				+ UnionId + ", NickName=" + NickName + ", Photo=" + Photo
				+ ", Method=" + Method + ", Infversion=" + Infversion
				+ ", Key=" + Key + ", UID=" + UID + "]";
	}

}
