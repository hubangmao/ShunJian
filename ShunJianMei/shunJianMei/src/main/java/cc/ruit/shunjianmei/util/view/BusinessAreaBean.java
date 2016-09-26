package cc.ruit.shunjianmei.util.view;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class BusinessAreaBean {
	private String ID;
	private String Name;

	public BusinessAreaBean() {
		super();
	}

	public BusinessAreaBean(String iD, String name) {
		super();
		ID = iD;
		Name = name;
	}

	@Override
	public String toString() {
		return "BusinessAreaBean [ID=" + ID + ", Name=" + Name + "]";
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

	public static BusinessAreaBean getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			BusinessAreaBean areaBean = gson.fromJson(json,
					BusinessAreaBean.class);
			return areaBean;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<BusinessAreaBean> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<BusinessAreaBean> lists = new ArrayList<BusinessAreaBean>();
			lists = gson.fromJson(json,
					new TypeToken<List<BusinessAreaBean>>() {
					}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
