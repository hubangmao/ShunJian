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
public class OrderListResponse implements Serializable {
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description:
	 */
	private static final long serialVersionUID = 1L;
	private String OrderID;// 订单ID
	private String OrderCode;// 订单编号
	private String Name;// 顾客名字
	private String LevelName;// 顾客电话
	private String Photo;// 顾客头像
	private String Mobile;// 顾客电话
	private String AppointmentTime;// 预约时间
	private String State;// 状态1待支付2待确认3已预约4已签到5服务中6服务完成7评论完成8订单完成9已取消10异常处理
	private String StateName;// 状态名称
	private String StyleName;// 发型项目名称
	private String StoreName;// 商家名称
	private String StoreTel;// 商家电话
	private String Amount;// 支付金额
	private String Address;// 商家地址
	private String ishairpacked;// 是否是套餐
	private String userid;// 设计师id

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getIshairpacked() {
		return ishairpacked;
	}

	public void setIshairpacked(String ishairpacked) {
		this.ishairpacked = ishairpacked;
	}

	public String getOrderID() {
		return OrderID;
	}

	public void setOrderID(String orderID) {
		OrderID = orderID;
	}

	public String getOrderCode() {
		return OrderCode;
	}

	public void setOrderCode(String orderCode) {
		OrderCode = orderCode;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getPhoto() {
		return Photo;
	}

	public void setPhoto(String photo) {
		Photo = photo;
	}

	public String getMobile() {
		return Mobile;
	}

	public void setMobile(String mobile) {
		Mobile = mobile;
	}

	public String getAppointmentTime() {
		return AppointmentTime;
	}

	public void setAppointmentTime(String appointmentTime) {
		AppointmentTime = appointmentTime;
	}

	public String getState() {
		return State;
	}

	public void setState(String state) {
		State = state;
	}

	public String getStateName() {
		return StateName;
	}

	public void setStateName(String stateName) {
		StateName = stateName;
	}

	public String getStyleName() {
		return StyleName;
	}

	public void setStyleName(String styleName) {
		StyleName = styleName;
	}

	public String getStoreName() {
		return StoreName;
	}

	public void setStoreName(String storeName) {
		StoreName = storeName;
	}

	public String getStoreTel() {
		return StoreTel;
	}

	public void setStoreTel(String storeTel) {
		StoreTel = storeTel;
	}

	public String getAmount() {
		return Amount;
	}

	public void setAmount(String amount) {
		Amount = amount;
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

	public String getLevelName() {
		return LevelName;
	}

	public void setLevelName(String levelName) {
		LevelName = levelName;
	}

	@Override
	public String toString() {
		return "OrderListResponse [OrderID=" + OrderID + ", OrderCode=" + OrderCode + ", Name=" + Name + ", Photo="
				+ Photo + ", Mobile=" + Mobile + ", AppointmentTime=" + AppointmentTime + ", State=" + State
				+ ", StateName=" + StateName + ", StyleName=" + StyleName + ", StoreName=" + StoreName + ", StoreTel="
				+ StoreTel + ", Amount=" + Amount + ", Address=" + Address + "]";
	}

	public static OrderListResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			OrderListResponse lists = gson.fromJson(json, OrderListResponse.class);
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<OrderListResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<OrderListResponse> lists = new ArrayList<OrderListResponse>();
			lists = gson.fromJson(json, new TypeToken<List<OrderListResponse>>() {
			}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
