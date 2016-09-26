package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cc.ruit.shunjianmei.base.BaseResponse;

public class H_GetInformationResponse extends BaseResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String PicID;// 头像图片ID
	private String PicturePath;// 头像图片路径
	private String PicID1;// 第一张形象照片ID
	private String PicturePath1;// 第一张形象照片路径
	private String PicID2;// 第二张形象照片ID
	private String PicturePath2;// 第二张形象照片路径
	private String PicID3;// 第三张形象照片ID
	private String PicturePath3;// 第三张形象照片路径
	private String NickName;// 昵称
	private String Intro;// 个人简介
	private String Hairstyle;// 擅长发型
	private String Hobbies;// 个人爱好
	private String Nationality;// 国籍
	private String Language;// 语言

	public String getPicID() {
		return PicID;
	}

	public void setPicID(String picID) {
		PicID = picID;
	}

	public String getPicID1() {
		return PicID1;
	}

	public void setPicID1(String picID1) {
		PicID1 = picID1;
	}

	public String getPicID2() {
		return PicID2;
	}

	public void setPicID2(String picID2) {
		PicID2 = picID2;
	}

	public String getPicID3() {
		return PicID3;
	}

	public void setPicID3(String picID3) {
		PicID3 = picID3;
	}

	public String getNickName() {
		return NickName;
	}

	public void setNickName(String nickName) {
		NickName = nickName;
	}

	public String getIntro() {
		return Intro;
	}

	public void setIntro(String intro) {
		Intro = intro;
	}

	public String getHairstyle() {
		return Hairstyle;
	}

	public void setHairstyle(String hairstyle) {
		Hairstyle = hairstyle;
	}

	public String getHobbies() {
		return Hobbies;
	}

	public void setHobbies(String hobbies) {
		Hobbies = hobbies;
	}

	public String getNationality() {
		return Nationality;
	}

	public void setNationality(String nationality) {
		Nationality = nationality;
	}

	public String getLanguage() {
		return Language;
	}

	public void setLanguage(String language) {
		Language = language;
	}

	public String getPicturePath() {
		return PicturePath;
	}

	public void setPicturePath(String picturePath) {
		PicturePath = picturePath;
	}

	public String getPicturePath1() {
		return PicturePath1;
	}

	public void setPicturePath1(String picturePath1) {
		PicturePath1 = picturePath1;
	}

	public String getPicturePath2() {
		return PicturePath2;
	}

	public void setPicturePath2(String picturePath2) {
		PicturePath2 = picturePath2;
	}

	public String getPicturePath3() {
		return PicturePath3;
	}

	public void setPicturePath3(String picturePath3) {
		PicturePath3 = picturePath3;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static H_GetInformationResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			H_GetInformationResponse lists = gson.fromJson(json, H_GetInformationResponse.class);
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<H_GetInformationResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<H_GetInformationResponse> lists = new ArrayList<H_GetInformationResponse>();
			lists = gson.fromJson(json, new TypeToken<List<H_GetInformationResponse>>() {
			}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String toString() {
		return "H_GetInformationResponse [PicID=" + PicID + ", PicturePath=" + PicturePath + ", PicID1=" + PicID1
				+ ", PicturePath1=" + PicturePath1 + ", PicID2=" + PicID2 + ", PicturePath2=" + PicturePath2
				+ ", PicID3=" + PicID3 + ", PicturePath3=" + PicturePath3 + ", NickName=" + NickName + ", Intro="
				+ Intro + ", Hairstyle=" + Hairstyle + ", Hobbies=" + Hobbies + ", Nationality=" + Nationality
				+ ", Language=" + Language + "]";
	}

}
