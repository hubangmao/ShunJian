package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @ClassName: StartQuickPayResponse
 * @Description: 开始闪惠支付的返回数据
 * @author: jiazhaohui
 * @date: 2015年10月14日 下午4:51:49
 */
public class StartQuickPayResponse implements Serializable {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description:
	 */
	private static final long serialVersionUID = 1L;
	private String MasterList;		// 美发师列表
	private String CouponPayCustom;	// 用户优惠金额
	private String CouponPayCusDesc;	// 优惠策略描述
	private String EnoughMoney;		// 满足多少金额才可以优惠
	private String Wallet;			// 用户钱包中有多少钱
	
	public String getMasterList()
	{
		return MasterList;
	}
	
	public void setMasterList(String masterList)
	{
		MasterList = masterList;
	}
	
	public String getCouponPayCustom()
	{
		return CouponPayCustom;
	}
	
	public void setCouponPayCustom(String couponPayCustom)
	{
		CouponPayCustom = couponPayCustom;
	}
	
	public String getCouponPayCusDesc()
	{
		return CouponPayCusDesc;
	}
	
	public void setCouponPayCusDesc(String couponPayCusDesc)
	{
		CouponPayCusDesc = couponPayCusDesc;
	}
	
	public String getEnoughMoney()
	{
		return EnoughMoney;
	}
	
	public void setEnoughMoney(String enoughMoney)
	{
		EnoughMoney = enoughMoney;
	}
	
	public String getWallet()
	{
		return Wallet;
	}
	
	public void setWallet(String wallet)
	{
		Wallet = wallet;
	}
	
	@Override
	public String toString() {
		return "StartQuickPayResponse [MasterList = "
				+ MasterList + ", CouponPayCustom = " + CouponPayCustom
				+ ", CouponPayCusDesc =" + CouponPayCusDesc
				+ ", EnoughMoney = " + EnoughMoney
				+ ", Wallet = " + Wallet
				+ "]";
	}

	public static StartQuickPayResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			StartQuickPayResponse lists = gson.fromJson(json,
					StartQuickPayResponse.class);
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<StartQuickPayResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<StartQuickPayResponse> lists = new ArrayList<StartQuickPayResponse>();
			lists = gson.fromJson(json,
					new TypeToken<List<StartQuickPayResponse>>() {
					}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
