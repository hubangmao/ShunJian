package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @ClassName: CommitOrderResponse
 * @Description: 提交订单
 * @author: lee
 * @date: 2015年9月5日 下午5:41:17
 */
public class UpdateOrderCouponResponse implements Serializable {
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description:
	 */
	private static final long serialVersionUID = 1L;
	private String OrderID;// 订单ID
	private String Total;// 总金额
	private String Discount;// 优惠金额
	private String Amount;// 需支付金额
	private List<Coupon> Coupon;// 优惠券

	public static class Coupon {
		public String ID;// 优惠券ID
		public String Name;// 优惠券名称
		public String Amount;// 金额
		public String StartTime;// 开始时间
		public String EndTime;// 结束时间
		public String Selected;// 是否选中
	}

	private List<Item> Item;

	public static class Item {
		public String Code;// 编码
		public String Name;// 名字
		public String Amount;// 金额

	}
	
	private String Wallet;// 是否可以钱包支付

	@Override
	public String toString() {
		return "CommitOrderResponse [OrderID=" + OrderID + ", Total=" + Total
				+ ", Discount=" + Discount + ", Amount=" + Amount + ", Coupon="
				+ Coupon + ", Item=" + Item + "]";
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



	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static UpdateOrderCouponResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			UpdateOrderCouponResponse response = gson.fromJson(json,
					UpdateOrderCouponResponse.class);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<UpdateOrderCouponResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<UpdateOrderCouponResponse> lists = new ArrayList<UpdateOrderCouponResponse>();
			lists = gson.fromJson(json,
					new TypeToken<List<UpdateOrderCouponResponse>>() {
					}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getWallet() {
		return Wallet;
	}

	public void setWallet(String wallet) {
		Wallet = wallet;
	}
}
