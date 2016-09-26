package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @ClassName: ReservationMaterialResponse
 * @Description: 预约物料
 * @author: Johnny
 * @date: 2015年10月14日 下午4:51:49
 */
public class ReservationMaterialResponse implements Serializable {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description:
	 */
	private static final long serialVersionUID = 1L;

	private String Code;// 服务类型(xjc洗剪吹,xc洗吹,tf烫发,rf染发,hl护理)
	private List<Item> Item;

	public static class Item{
		public String ID;//物料ID
		public String Name;//物料名称
		public String Price;//价格
		public boolean isChecked = false;//是否被选中的标记
	}

	
	public String getCode() {
		return Code;
	}

	public void setCode(String code) {
		Code = code;
	}

	public List<Item> getItem() {
		return Item;
	}

	public void setItem(List<Item> item) {
		Item = item;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static ReservationMaterialResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			ReservationMaterialResponse lists = gson.fromJson(json,
					ReservationMaterialResponse.class);
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<ReservationMaterialResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<ReservationMaterialResponse> lists = new ArrayList<ReservationMaterialResponse>();
			lists = gson.fromJson(json,
					new TypeToken<List<ReservationMaterialResponse>>() {
					}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
