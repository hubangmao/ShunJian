package cc.ruit.shunjianmei.net.request;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

import cc.ruit.shunjianmei.base.BaseRequest;

public class H_SetInformationRequest extends BaseRequest {

	private String UserID;// 用户ID
	private String PicID;// 头像图片ID
	private String PicID1;// 第一张形象照片ID
	private String PicID2;// 第二张形象照片ID
	private String PicID3;// 第三张形象照片ID
	private String NickName;// 昵称
	private String Intro;// 个人简介
	private String Hairstyle;// 擅长发型
	private String Hobbies;// 个人爱好
	private String Nationality;// 国籍
	private String Language;// 语言

	public H_SetInformationRequest(String userID, String piclID, String piclID1, String piclID2, String piclID3,
			String nickName, String intro, String hairstyle, String hobbies, String nationality, String language) {
		super("H_SetInformation", "1.0");
		UserID = userID;
		PicID = piclID;
		PicID1 = piclID1;
		PicID2 = piclID2;
		PicID3 = piclID3;
		NickName = nickName;
		this.Intro = intro;
		Hairstyle = hairstyle;
		Hobbies = hobbies;
		Nationality = nationality;
		Language = language;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "H_SetInformation"));
	}

	/**
	 * @Title: toJsonString
	 * @Description: 把对象转成json格式的字符串
	 * @author: lee
	 * @param request
	 * @return: String
	 */
	public String toJsonString(H_SetInformationRequest request) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(request);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "H_SetInformationRequest [UserID=" + UserID + ", PiclID=" + PicID + ", PiclID1=" + PicID1
				+ ", PiclID2=" + PicID2 + ", PiclID3=" + PicID3 + ", NickName=" + NickName + ", intro=" + Intro
				+ ", Hairstyle=" + Hairstyle + ", Hobbies=" + Hobbies + ", Nationality=" + Nationality + ", Language="
				+ Language + ", Method=" + Method + ", Infversion=" + Infversion + ", Key=" + Key + ", UID=" + UID
				+ "]";
	}

}
