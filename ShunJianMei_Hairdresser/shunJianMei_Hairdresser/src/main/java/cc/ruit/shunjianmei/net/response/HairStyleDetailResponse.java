package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cc.ruit.shunjianmei.home.manager.HairStyle;
import cc.ruit.shunjianmei.home.manager.ServiceTimes;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
/**
 * @ClassName: HairListResponse
 * @Description: 提现信息返回
 * @author: gaoj
 * @date: 2015年10月14日 下午6:06:38
 */
public class HairStyleDetailResponse implements Serializable{

	
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 1L;
	private String ID;  //我的发型ID
	private String Name; //发型名称
	private String PicID1;//列表图ID
	private String PicID2;//详情图ID
	private String Photo; //列表图
	private String Image; //详情图
	private List<HairStyle> HairStyle; //发型项目
	private List<ServiceTimes> ServiceTimes; //服务时长
	private String Intro; //发型简介
	
	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getPhoto() {
		return Photo;
	}

	public void setPhoto(String photo) {
		Photo = photo;
	}

	public String getImage() {
		return Image;
	}

	public void setImage(String image) {
		Image = image;
	}

	public List<HairStyle> getHairStyle() {
		return HairStyle;
	}

	public void setHairStyle(List<HairStyle> hairStyle) {
		HairStyle = hairStyle;
	}

	public List<ServiceTimes> getServiceTimes() {
		return ServiceTimes;
	}

	public void setServiceTimes(List<ServiceTimes> serviceTimes) {
		ServiceTimes = serviceTimes;
	}

	public String getIntro() {
		return Intro;
	}

	public void setIntro(String intro) {
		Intro = intro;
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

	@Override
	public String toString() {
		return "HairStyleDetailResponse [ID=" + ID + ", Name=" + Name
				+ ", Photo=" + Photo + ", Image=" + Image + ", HairStyle="
				+ HairStyle + ", ServiceTimes=" + ServiceTimes + ", Intro="
				+ Intro + "]";
	}

	public static HairStyleDetailResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			HairStyleDetailResponse lists = gson.fromJson(json,
					HairStyleDetailResponse.class);
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<HairStyleDetailResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<HairStyleDetailResponse> lists = new ArrayList<HairStyleDetailResponse>();
			lists = gson.fromJson(json,
					new TypeToken<List<HairStyleDetailResponse>>() {
					}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	


}
