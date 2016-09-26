package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @ClassName: OrderListResponse
 * @Description: 订单列表
 * @author: lee
 * @date: 2015年10月12日 下午6:38:39
 */
public class TradeRecordResponse implements Serializable {
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description:
	 */
	private static final long serialVersionUID = 1L;
	private String ID;// 交易记录ID
	private String OrderCode;// 订单编号
	private String Remark;// 交易说明
	private String PayTime;// 支付时间
	private String Amount;// 金额
	private String AppointmentTime;//预约时间

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getOrderCode() {
		return OrderCode;
	}

	public String getAppointmentTime() {
		return AppointmentTime;
	}

	public void setAppointmentTime(String appointmentTime) {
		AppointmentTime = appointmentTime;
	}

	public void setOrderCode(String orderCode) {
		OrderCode = orderCode;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

	public String getPayTime() {
		return PayTime;
	}

	public void setPayTime(String payTime) {
		PayTime = payTime;
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

	@Override
	public String toString() {
		return "TradeRecordResponse [ID=" + ID + ", OrderCode=" + OrderCode
				+ ", Remark=" + Remark + ", PayTime=" + PayTime + ", Amount="
				+ Amount + ", AppointmentTime=" + AppointmentTime + "]";
	}

	public static TradeRecordResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			TradeRecordResponse lists = gson.fromJson(json,
					TradeRecordResponse.class);
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<TradeRecordResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<TradeRecordResponse> lists = new ArrayList<TradeRecordResponse>();
			lists = gson.fromJson(json,
					new TypeToken<List<TradeRecordResponse>>() {
					}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
