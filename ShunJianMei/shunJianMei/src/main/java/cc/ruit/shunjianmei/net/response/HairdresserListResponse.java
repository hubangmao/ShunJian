package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * @ClassName: HairdresserListResponse
 * @Description: 发型师列表
 * @author: 欧阳
 * @date: 2015年10月20日 下午9:37:38
 */
public class HairdresserListResponse implements Serializable {
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description:
	 */
	private static final long serialVersionUID = 1L;
	private String ID;// 用户ID
	private String Name;// 姓名
	private String Photo;// 头像
	private String Image;// 图片
	private String Star;// 星座
	private String LevelName;// 等级
	private String Score;// 分数
	private String OrderNum;// 接单数量
	private String Price;// 价格
	private String Nationality; // 国籍
	private String Language; // 语言
	private String Flagpng; // 国旗图片
	private String istypeString;// 商品类型
	private String hasPacked;// 是否有此套餐

	public String getIstypeString() {
		return istypeString;
	}

	public void setIstypeString(String istypeString) {
		this.istypeString = istypeString;
	}

	public String getHasPacked() {
		return hasPacked;
	}

	public void setHasPacked(String hasPacked) {
		this.hasPacked = hasPacked;
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

	public String getImage() {
		return Image;
	}

	public void setImage(String image) {
		Image = image;
	}

	public String getStar() {
		return Star;
	}

	public void setStar(String star) {
		Star = star;
	}

	public String getLevelName() {
		return LevelName;
	}

	public void setLevelName(String levelName) {
		LevelName = levelName;
	}

	public String getScore() {
		return Score;
	}

	public void setScore(String score) {
		Score = score;
	}

	public String getOrderNum() {
		return OrderNum;
	}

	public void setOrderNum(String orderNum) {
		OrderNum = orderNum;
	}

	public String getPrice() {
		return Price;
	}

	public void setPrice(String price) {
		Price = price;
	}

	public String getFlagpng() {
		return Flagpng;
	}

	public void setFlagpng(String flagpng) {
		Flagpng = flagpng;
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

	@Override
	public String toString() {
		return "HairdresserListResponse [ID=" + ID + ", Name=" + Name + ", Photo=" + Photo + ", Image=" + Image
				+ ", Star=" + Star + ", LevelName=" + LevelName + ", Score=" + Score + ", OrderNum=" + OrderNum
				+ ", Price=" + Price + ", Nationality=" + Nationality + ", Language" + Language + ", Flagpng" + Flagpng
				+ "]";
	}

	public static HairdresserListResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			HairdresserListResponse response = gson.fromJson(json, HairdresserListResponse.class);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<HairdresserListResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<HairdresserListResponse> lists = new ArrayList<HairdresserListResponse>();
			lists = gson.fromJson(json, new TypeToken<List<HairdresserListResponse>>() {
			}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
