package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @ClassName: QuickpayWithWalletResponse
 * @Description: 钱包的闪惠支付的返回数据
 * @author: jiazhaohui
 */
public class QuickpayWithWalletResponse implements Serializable {

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
	
	@Override
	public String toString() {
		return "QuickpayWithWalletResponse [Order = "
				+ OrderID + ", StoreName = " + StoreName
				+ ", MasterName =" + MasterName
				+ ", PayAmount = " + PayAmount
				+ ", ReduceAmount = " + ReduceAmount
				+ "]";
	}

	public static QuickpayWithWalletResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			QuickpayWithWalletResponse lists = gson.fromJson(json,
					QuickpayWithWalletResponse.class);
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<QuickpayWithWalletResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<QuickpayWithWalletResponse> lists = new ArrayList<QuickpayWithWalletResponse>();
			lists = gson.fromJson(json,
					new TypeToken<List<QuickpayWithWalletResponse>>() {
					}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
