package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @ClassName: StoreListResponse
 * @Description: TODO
 * @author: Johnny
 * @date: 2015年10月14日 下午4:51:49
 */
public class StoreListResponse implements Serializable {
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description:
	 */
	private static final long serialVersionUID = 1L;
	private String ID;// 发型店ID
	private String Name;// 发型店名称
	private String Photo;// 列表图
	private String Address;// 地址
	private String Score;// 评分
	private String Distance;// 离我距离
	private String OrderNum;// 接单数量
	private String CarNum;// 车位数
	private String istype;// 类型

	public String getIstype() {
		return istype;
	}

	public void setIstype(String istype) {
		this.istype = istype;
	}

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

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getScore() {
		return Score;
	}

	public void setScore(String score) {
		Score = score;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "StoreListResponse [ID=" + ID + ", Name=" + Name + ", Photo=" + Photo + ", Address=" + Address
				+ ", Score=" + Score + ", Distance=" + Distance + ", OrderNum=" + OrderNum + ", CarNum=" + CarNum + "]";
	}

	public static StoreListResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			StoreListResponse lists = gson.fromJson(json, StoreListResponse.class);
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<StoreListResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<StoreListResponse> lists = new ArrayList<StoreListResponse>();
			lists = gson.fromJson(json, new TypeToken<List<StoreListResponse>>() {
			}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
