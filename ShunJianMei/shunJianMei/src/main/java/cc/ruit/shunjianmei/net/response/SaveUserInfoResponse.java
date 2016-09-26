package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * @ClassName: SaveUserInfoResponse
 * @Description: 用户
 * @author: 欧阳
 * @date: 2015年10月31日 下午5:59:31
 */
public class SaveUserInfoResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	private String UserID;// 用户ID
	private String NickName;// 昵称
	private String Sex;// 性别 1男2女
	private String Photo;// 图像

	public static SaveUserInfoResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			SaveUserInfoResponse response = gson.fromJson(json,
					SaveUserInfoResponse.class);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<SaveUserInfoResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<SaveUserInfoResponse> lists = new ArrayList<SaveUserInfoResponse>();
			lists = gson.fromJson(json,
					new TypeToken<List<SaveUserInfoResponse>>() {
					}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String toString() {
		return "SaveUserInfoResponse [UserID=" + UserID + ", NickName="
				+ NickName + ", Sex=" + Sex + ", Photo=" + Photo + "]";
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

	public String getPhoto() {
		return Photo;
	}

	public void setPhoto(String photo) {
		Photo = photo;
	}

}
