package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;
/**
 * 
 * @ClassName: SaveUserPhotoRequest
 * @Description: 保存头像
 * @author: 欧阳
 * @date: 2015年10月31日 下午6:04:21
 */
public class SaveUserPhotoRequest extends BaseRequest {
	// 用户id
	private String UserID;
	private String ID;

	/**
	 * @Title:ActivityListRequest
	 * @Description:活动
	 * @param UserId
	 */
	public SaveUserPhotoRequest(String UserID,String ID) {
		super("SaveUserPhoto", "1.0");
		this.UserID = UserID;
		this.ID = ID;
		//ID=UUID.randomUUID().toString();
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "SaveUserPhoto"));
	}

	/**
	 * @Title: toJsonString
	 * @Description:  把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(SaveUserPhotoRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "SaveUserPhotoRequest [UserID=" + UserID + ", ID=" + ID + "]";
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	

}
