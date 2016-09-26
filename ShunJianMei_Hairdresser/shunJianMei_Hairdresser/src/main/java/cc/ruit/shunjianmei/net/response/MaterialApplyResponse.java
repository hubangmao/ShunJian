package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MaterialApplyResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	// "ID": "1",
	// "Date": "2015-9-23",
	// "Address": "木北造型(科技园店),地址：丰台富丰路4号",
	// "Item": [
	private String ID;// 记录ID
	private String Date;// 提取日期
	private String Address;// 提取地点
	List<MaterialApplyItemResponse> Item;// 物料

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getDate() {
		return Date;
	}

	public void setDate(String date) {
		Date = date;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public List<MaterialApplyItemResponse> getItem() {
		return Item;
	}

	public void setItem(List<MaterialApplyItemResponse> item) {
		Item = item;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "MaterialApplyResponse [ID=" + ID + ", Date=" + Date
				+ ", Address=" + Address + ", Item=" + Item + "]";
	}

	public static MaterialApplyResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			MaterialApplyResponse lists = gson.fromJson(json,
					MaterialApplyResponse.class);
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<MaterialApplyResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<MaterialApplyResponse> lists = new ArrayList<MaterialApplyResponse>();
			lists = gson.fromJson(json,
					new TypeToken<List<MaterialApplyResponse>>() {
					}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
