package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MaterialApplyItemResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String Name;// 记录ID
	private String Num;// 提取日期
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getNum() {
		return Num;
	}
	public void setNum(String num) {
		Num = num;
	}
	@Override
	public String toString() {
		return "MaterialApplyItemResponse [Name=" + Name + ", Num=" + Num
				+ "]";
	}

	public static MaterialApplyItemResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			MaterialApplyItemResponse lists = gson.fromJson(json,
					MaterialApplyItemResponse.class);
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<MaterialApplyItemResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<MaterialApplyItemResponse> lists = new ArrayList<MaterialApplyItemResponse>();
			lists = gson.fromJson(json,
					new TypeToken<List<MaterialApplyItemResponse>>() {
					}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}