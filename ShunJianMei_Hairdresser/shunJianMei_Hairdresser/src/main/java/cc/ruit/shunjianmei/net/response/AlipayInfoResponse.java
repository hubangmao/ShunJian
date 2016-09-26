package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
/**
 * @ClassName: WithdrawalsInfoResponse
 * @Description: 提现信息返回
 * @author: lee
 * @date: 2015年9月5日 下午5:41:17
 */
public class AlipayInfoResponse implements Serializable{
	/**@fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: 
	 */
	private static final long serialVersionUID = 1L;
	private String Gold;// 金币数量
	private String Money;// 金额
	private String FeedRate;// 换算比例
	private String Minmum;// 最小提现金额
	private String Minfree;// 提现手续费条件
	private String DelayTime;// 到账延迟时间（单位小时）
	private String AlipayId;// 支付宝账号
	private String Payee;// 收款人姓名
	private String IsOK;// 是否能提现（1是，0否）
	private String Warning;// 规则说明
	List<AlipayInfoItemResponse> Items;

	
	public String getWarning() {
		return Warning;
	}

	public void setWarning(String warning) {
		Warning = warning;
	}

	public String getGold() {
		return Gold;
	}

	public void setGold(String gold) {
		Gold = gold;
	}

	public String getMoney() {
		return Money;
	}

	public void setMoney(String money) {
		Money = money;
	}

	public String getFeedRate() {
		return FeedRate;
	}

	public void setFeedRate(String feedRate) {
		FeedRate = feedRate;
	}

	public List<AlipayInfoItemResponse> getItems() {
		return Items;
	}

	public void setItems(List<AlipayInfoItemResponse> items) {
		Items = items;
	}

	public String getMinmum() {
		return Minmum;
	}

	public void setMinmum(String minmum) {
		Minmum = minmum;
	}

	public String getMinfree() {
		return Minfree;
	}

	public void setMinfree(String minfree) {
		Minfree = minfree;
	}

	public String getDelayTime() {
		return DelayTime;
	}

	public void setDelayTime(String delayTime) {
		DelayTime = delayTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getAlipayId() {
		return AlipayId;
	}

	public void setAlipayId(String alipayId) {
		AlipayId = alipayId;
	}

	public String getPayee() {
		return Payee;
	}

	public void setPayee(String payee) {
		Payee = payee;
	}

	public String getIsOK() {
		return IsOK;
	}

	public void setIsOK(String isOK) {
		IsOK = isOK;
	}

	@Override
	public String toString() {
		return "AlipayInfoResponse [Gold=" + Gold + ", Money=" + Money
				+ ", FeedRate=" + FeedRate + ", Minmum=" + Minmum
				+ ", Minfree=" + Minfree + ", DelayTime=" + DelayTime
				+ ", AlipayId=" + AlipayId + ", Payee=" + Payee + ", Items="
				+ Items + "]";
	}

	public static AlipayInfoResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			AlipayInfoResponse response = gson.fromJson(json,AlipayInfoResponse.class);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static List<AlipayInfoResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<AlipayInfoResponse> lists = new ArrayList<AlipayInfoResponse>();
			lists = gson.fromJson(json, new TypeToken<List<AlipayInfoResponse>>() {
			}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
