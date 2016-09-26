package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * 
 * @ClassName: SaveUserInfoRequest
 * @Description: 保存用户信息
 * @author: 欧阳
 * @date: 2015年10月31日 下午5:54:04
 */
public class SaveUserInfoRequest extends BaseRequest {
	// 用户id
	private String UserID;
	private String NickName;
	private String Sex;

	/**
	 * @Title:ActivityListRequest
	 * @Description:活动
	 * @param UserId
	 */
	public SaveUserInfoRequest(String UserID, String NickName, String Sex) {
		super("SaveUserInfo", "1.0");
		this.UserID = UserID;
		this.NickName = NickName;
		this.Sex = Sex;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "SaveUserInfo"));
	}

	/**
	 * @Title: toJsonString
	 * @Description: 把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(SaveUserInfoRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public String getNickName() {
		return NickName;
	}

	public void setNickName(String nickName) {
		NickName = nickName;
	}

	public String getSex() {
		return Sex;
	}

	public void setSex(String sex) {
		Sex = sex;
	}

	@Override
	public String toString() {
		return "SaveUserInfoRequest [UserID=" + UserID + ", NickName="
				+ NickName + ", Sex=" + Sex + "]";
	}

}
