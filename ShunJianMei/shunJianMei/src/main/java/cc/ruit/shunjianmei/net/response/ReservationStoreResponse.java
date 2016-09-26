package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @ClassName: ReservationStoreResponse
 * @Description: 预约商户
 * @author: Johnny
 * @date: 2015年10月14日 下午4:51:49
 */
public class ReservationStoreResponse implements Serializable {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description:
	 */
	private static final long serialVersionUID = 1L;

	private String ID;// 发型店ID
	private String Name;// 发型店名称
	private String Address;// 地址

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

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static ReservationStoreResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			ReservationStoreResponse lists = gson.fromJson(json,
					ReservationStoreResponse.class);
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<ReservationStoreResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<ReservationStoreResponse> lists = new ArrayList<ReservationStoreResponse>();
			lists = gson.fromJson(json,
					new TypeToken<List<ReservationStoreResponse>>() {
					}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
