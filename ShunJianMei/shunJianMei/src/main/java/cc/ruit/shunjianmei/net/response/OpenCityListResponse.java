package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * @ClassName: OpenCityListResponse
 * @Description: 开通城市列表
 * @author: 欧阳
 * @date: 2015年10月20日 上午10:30:24
 */
public class OpenCityListResponse implements Serializable {
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description:
	 */
	private static final long serialVersionUID = 1L;
	private String ID;// 城市ID
	private String Name;// 城市名称
	
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

	@Override
	public String toString() {
		return "OpenCityListResponse [ID=" + ID + ", Name=" + Name + "]";
	}

	public static OpenCityListResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			OpenCityListResponse response = gson.fromJson(json,
					OpenCityListResponse.class);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<OpenCityListResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<OpenCityListResponse> lists = new ArrayList<OpenCityListResponse>();
			lists = gson.fromJson(json,
					new TypeToken<List<OpenCityListResponse>>() {
					}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
