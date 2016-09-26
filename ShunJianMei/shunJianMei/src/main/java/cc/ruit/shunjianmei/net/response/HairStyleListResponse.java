package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * @ClassName: HairStyleListResponse
 * @Description: 发型列表
 * @author: 欧阳
 * @date: 2015年10月24日 下午8:17:13
 */
public class HairStyleListResponse implements Serializable {
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description:
	 */
	private static final long serialVersionUID = 1L;
	public String ID;// 发型ID
	public String Name;// 发型名称
	public String Photo;// 列表图
	public String Price;// 价格
	public String UsedNum;// 使用次数
	public static HairStyleListResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			HairStyleListResponse response = gson.fromJson(json,
					HairStyleListResponse.class);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<HairStyleListResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<HairStyleListResponse> lists = new ArrayList<HairStyleListResponse>();
			lists = gson.fromJson(json,
					new TypeToken<List<HairStyleListResponse>>() {
					}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String toString() {
		return "HairStyleListResponse [ID=" + ID + ", Name=" + Name
				+ ", Photo=" + Photo + ", Price=" + Price + ", UsedNum="
				+ UsedNum + "]";
	}

}
