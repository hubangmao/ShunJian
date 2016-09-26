package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @ClassName: CanUseCouponsResponse
 * @Description: 可用优惠券
 * @author: lee
 * @date: 2015年9月5日 下午5:41:17
 */
public class CanUseCouponsResponse implements Serializable {
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description:
	 */
	private static final long serialVersionUID = 1L;
	private String ID;// 我的优惠券ID
	private String Amount;// 优惠金额

	@Override
	public String toString() {
		return "CanUseCouponsResponse [ID=" + ID + ", Amount=" + Amount + "]";
	}

	

	public String getID() {
		return ID;
	}



	public void setID(String iD) {
		ID = iD;
	}



	public String getAmount() {
		return Amount;
	}



	public void setAmount(String amount) {
		Amount = amount;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static CanUseCouponsResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			CanUseCouponsResponse response = gson.fromJson(json,
					CanUseCouponsResponse.class);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<CanUseCouponsResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<CanUseCouponsResponse> lists = new ArrayList<CanUseCouponsResponse>();
			lists = gson.fromJson(json,
					new TypeToken<List<CanUseCouponsResponse>>() {
					}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
