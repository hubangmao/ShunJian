package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @ClassName: FavoriteHairdresserListResponse
 * @Description: 收藏美发师列表
 * @author: Johnny
 * @date: 2015年10月14日 下午4:51:49
 */
public class FavoriteHairdresserListResponse implements Serializable {
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description:
	 */
	private static final long serialVersionUID = 1L;
	private String ID;//用户ID
	private String Name;// 昵称
	private String Photo;// 头像
	private String Image;//配图
	private String Star;// 星座
	private String LevelName;// 等级
	private String Score;// 评分
	private String OrderNum;// 接单数

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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "FavoriteHairdresserListResponse [ID=" + ID + ", Name=" + Name
				+ ", Photo=" + Photo + ", Image=" + Image + ", Star=" + Star
				+ ", LevelName=" + LevelName + ", Score=" + Score
				+ ", OrderNum=" + OrderNum + "]";
	}

	public static FavoriteHairdresserListResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			FavoriteHairdresserListResponse lists = gson.fromJson(json,
					FavoriteHairdresserListResponse.class);
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<FavoriteHairdresserListResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<FavoriteHairdresserListResponse> lists = new ArrayList<FavoriteHairdresserListResponse>();
			lists = gson.fromJson(json,
					new TypeToken<List<FavoriteHairdresserListResponse>>() {
					}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
