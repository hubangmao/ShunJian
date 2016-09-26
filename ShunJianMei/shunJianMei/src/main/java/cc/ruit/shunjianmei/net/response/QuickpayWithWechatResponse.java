package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @ClassName: QuickpayWithAlipayResponse
 * @Description: 微信的闪惠支付的返回数据
 * @author: jiazhaohui
 */
public class QuickpayWithWechatResponse implements Serializable {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description:
	 */
	private static final long serialVersionUID = 1L;
	String OrderID;		// 闪惠支付的订单ID
	String StoreName;	// 支付对象美发店名称
	String MasterName;	// 支付对象美发师名称
	String PayAmount;	// 支付总金额
	String ReduceAmount;	// 优惠的总金额 
	String WechatPayID;		// 微信预支付订单ID
	
	public String getOrderID()
	{
		return OrderID;
	}
	
	public void setOrderID(String orderID)
	{
		OrderID = orderID;
	}
	
	public String getStoreName()
	{
		return StoreName;
	}
	
	public void setStoreName(String storeName)
	{
		StoreName = storeName;
	}
	
	public String getMasterName()
	{
		return MasterName;
	}
	
	public void setMasterName(String masterName)
	{
		MasterName = masterName;
	}
	
	public String getPayAmount()
	{
		return PayAmount;
	}
	
	public void setPayAmount(String payAmount)
	{
		PayAmount = payAmount;
	}
	
	public String getReduceAmount()
	{
		return ReduceAmount;
	}
	
	public void setReduceAmount(String reduceAmount)
	{
		ReduceAmount = reduceAmount;
	}
	
	public String getWechatPayID()
	{
		return WechatPayID;
	}
	
	public void setWechatPayID(String wechatPayID)
	{
		WechatPayID = wechatPayID;
	}
	
	@Override
	public String toString() {
		return "QuickpayWithWechatResponse [Order = "
				+ OrderID + ", StoreName = " + StoreName
				+ ", MasterName =" + MasterName
				+ ", PayAmount = " + PayAmount
				+ ", ReduceAmount = " + ReduceAmount
				+ ", WechatPayID = " + WechatPayID
				+ "]";
	}

	public static QuickpayWithWechatResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			QuickpayWithWechatResponse lists = gson.fromJson(json,
					QuickpayWithWechatResponse.class);
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<QuickpayWithWechatResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<QuickpayWithWechatResponse> lists = new ArrayList<QuickpayWithWechatResponse>();
			lists = gson.fromJson(json,
					new TypeToken<List<QuickpayWithWechatResponse>>() {
					}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
