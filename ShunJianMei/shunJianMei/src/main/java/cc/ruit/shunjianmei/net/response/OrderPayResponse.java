package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cc.ruit.shunjianmei.base.BaseResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;
/**
 * 
 * @ClassName: OrderPayResponse
 * @Description: 支付订单
 * @author: Johnny
 * @date: 2015年10月12日 下午10:26:45
 */
public class OrderPayResponse implements Serializable{
	/**@fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: 
	 */
	private static final long serialVersionUID = 1L;
	
	private String OrderID;// 订单ID
	private String Total;// 总金额
	private String Discount;// 优惠金额
	private String Amount;// 需支付金额
	private List<Coupon> Coupon;// 优惠券
	private List<Item> Item;
	private String Wallet;//是否可以钱包支付
	
	public static class Coupon{
		public String ID;//优惠券ID
		public String Name;//优惠券名称
		public String Amount;//金额
		public String StartTime;//开始时间
		public String EndTime;//结束时间
		public String Selected;//是否选中
		@Override
		public String toString() {
			return "Coupon [ID=" + ID + ", Name=" + Name + ", Amount=" + Amount
					+ ", StartTime=" + StartTime + ", EndTime=" + EndTime
					+ ", Selected=" + Selected + "]";
		}
		
	}
	
	public static class Item{
		public String Code; //编码    
		public String Name; //名字   
		public String Coupon; //优惠券金额 
		public String Amount; //金额   
		
		public Item(String code, String name, String coupon, String amount) {
			super();
			Code = code;
			Name = name;
			Coupon = coupon;
			Amount = amount;
		}

		public Item() {
			super();
			// TODO Auto-generated constructor stub
		}

		@Override
		public String toString() {
			return "Item [Code=" + Code + ", Name=" + Name + ", Coupon="
					+ Coupon + ", Amount=" + Amount + "]";
		}
	}
	
	
	public String getOrderID() {
		return OrderID;
	}

	public void setOrderID(String orderID) {
		OrderID = orderID;
	}

	public String getTotal() {
		return Total;
	}

	public void setTotal(String total) {
		Total = total;
	}

	public String getDiscount() {
		return Discount;
	}

	public void setDiscount(String discount) {
		Discount = discount;
	}

	public String getAmount() {
		return Amount;
	}

	public void setAmount(String amount) {
		Amount = amount;
	}

	public List<Coupon> getCoupon() {
		return Coupon;
	}

	public void setCoupon(List<Coupon> coupon) {
		Coupon = coupon;
	}

	public List<Item> getItem() {
		return Item;
	}

	public void setItem(List<Item> item) {
		Item = item;
	}

	public String getWallet() {
		return Wallet;
	}

	public void setWallet(String wallet) {
		Wallet = wallet;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "OrderPayResponse [OrderID=" + OrderID + ", Total=" + Total
				+ ", Discount=" + Discount + ", Amount=" + Amount + ", Coupon="
				+ Coupon + ", Item=" + Item + ", Wallet=" + Wallet + "]";
	}

	public static OrderPayResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			OrderPayResponse lists = gson.fromJson(json, OrderPayResponse.class);
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static List<OrderPayResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<OrderPayResponse> lists = new ArrayList<OrderPayResponse>();
			lists = gson.fromJson(json, new TypeToken<List<OrderPayResponse>>() {
			}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
