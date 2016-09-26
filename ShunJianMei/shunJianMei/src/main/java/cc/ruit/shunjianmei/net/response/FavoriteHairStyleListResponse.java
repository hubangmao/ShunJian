package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @ClassName: FavoriteHairdresserListResponse
 * @Description: 收藏发型列表
 * @author: Johnny
 * @date: 2015年10月14日 下午4:51:49
 */
public class FavoriteHairStyleListResponse implements Serializable {
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description:
	 */
	private static final long serialVersionUID = 1L;
	private String ID;//发型ID
	private String Name;// 发型名称
	private String Photo;// 列表图
	private String Price;//价格
	private String UsedNum;// 使用次数

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

	public String getPrice() {
		return Price;
	}

	public void setPrice(String price) {
		Price = price;
	}

	public String getUsedNum() {
		return UsedNum;
	}

	public void setUsedNum(String usedNum) {
		UsedNum = usedNum;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "FavoriteHairStyleListResponse [ID=" + ID + ", Name=" + Name
				+ ", Photo=" + Photo + ", Price=" + Price + ", UsedNum="
				+ UsedNum + "]";
	}

	public static FavoriteHairStyleListResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			FavoriteHairStyleListResponse lists = gson.fromJson(json,
					FavoriteHairStyleListResponse.class);
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<FavoriteHairStyleListResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<FavoriteHairStyleListResponse> lists = new ArrayList<FavoriteHairStyleListResponse>();
			lists = gson.fromJson(json,
					new TypeToken<List<FavoriteHairStyleListResponse>>() {
					}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
