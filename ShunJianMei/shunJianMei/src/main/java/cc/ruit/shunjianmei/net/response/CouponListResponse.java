package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @ClassName: StoreListResponse
 * @Description: TODO
 * @author: Johnny
 * @date: 2015年10月14日 下午4:51:49
 */
public class CouponListResponse implements Serializable {
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description:
	 */
	private static final long serialVersionUID = 1L;
	private String ID;//优惠券ID
	private String Name;// 优惠券名称
	private String Amount;// 优惠金额
	private String StartTime;// 生效时间
	private String EndTime;// 过期时间

	

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

	public String getAmount() {
		return Amount;
	}

	public void setAmount(String amount) {
		Amount = amount;
	}

	public String getStartTime() {
		return StartTime;
	}

	public void setStartTime(String startTime) {
		StartTime = startTime;
	}

	public String getEndTime() {
		return EndTime;
	}

	public void setEndTime(String endTime) {
		EndTime = endTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "CouponListResponse [ID=" + ID + ", Name=" + Name + ", Amount="
				+ Amount + ", StartTime=" + StartTime + ", EndTime=" + EndTime
				+ "]";
	}

	public static CouponListResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			CouponListResponse lists = gson.fromJson(json,
					CouponListResponse.class);
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<CouponListResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<CouponListResponse> lists = new ArrayList<CouponListResponse>();
			lists = gson.fromJson(json,
					new TypeToken<List<CouponListResponse>>() {
					}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
