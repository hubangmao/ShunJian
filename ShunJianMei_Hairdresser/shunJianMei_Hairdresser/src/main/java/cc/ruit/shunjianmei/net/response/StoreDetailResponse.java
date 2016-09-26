package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @ClassName: StoreDetailResponse
 * @Description: 商店详情出参
 * @author: Johnny
 * @date: 2015年10月14日 下午4:51:49
 */
public class StoreDetailResponse implements Serializable {
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description:
	 */
	private static final long serialVersionUID = 1L;
	private String UserId;//用户ID
	private String ID;//发型店ID
	private String Name;//发型店名称
	private String Address;//地址
	private String BusinessHours;//营业时间
	private String Score;//评分
	private String Distance;//离我距离
	private String OrderNum;//接单数量
	private String CarNum;//车位数
	private String Tel;//电话
	private String Intro;//简介
	private List<Images> Images;//Photo 
	
	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getUserId() {
		return UserId;
	}

	public void setUserId(String userId) {
		UserId = userId;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getBusinessHours() {
		return BusinessHours;
	}

	public void setBusinessHours(String businessHours) {
		BusinessHours = businessHours;
	}

	public String getDistance() {
		return Distance;
	}

	public void setDistance(String distance) {
		Distance = distance;
	}

	public String getOrderNum() {
		return OrderNum;
	}

	public void setOrderNum(String orderNum) {
		OrderNum = orderNum;
	}

	public String getCarNum() {
		return CarNum;
	}

	public void setCarNum(String carNum) {
		CarNum = carNum;
	}

	public String getTel() {
		return Tel;
	}

	public void setTel(String tel) {
		Tel = tel;
	}

	public String getScore() {
		return Score;
	}

	public void setScore(String score) {
		Score = score;
	}

	public String getIntro() {
		return Intro;
	}

	public void setIntro(String intro) {
		Intro = intro;
	}

	public List<Images> getImages() {
		return Images;
	}

	public void setImages(List<Images> images) {
		Images = images;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "StoreDetailResponse [UserId=" + UserId + ", ID=" + ID
				+ ", Name=" + Name + ", Address=" + Address
				+ ", BusinessHours=" + BusinessHours + ", Distance=" + Distance
				+ ", OrderNum=" + OrderNum + ", CarNum=" + CarNum + ", Tel="
				+ Tel + ", Intro=" + Intro + ", Images=" + Images + "]";
	}

	public static StoreDetailResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			StoreDetailResponse lists = gson.fromJson(json,
					StoreDetailResponse.class);
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<StoreDetailResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<StoreDetailResponse> lists = new ArrayList<StoreDetailResponse>();
			lists = gson.fromJson(json,
					new TypeToken<List<StoreDetailResponse>>() {
					}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
