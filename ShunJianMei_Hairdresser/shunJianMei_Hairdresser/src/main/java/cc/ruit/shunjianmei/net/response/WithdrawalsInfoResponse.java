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
public class WithdrawalsInfoResponse implements Serializable{
	/**@fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: 
	 */
	private static final long serialVersionUID = 1L;
	private String Gold;// 金币数量
	private String Money;// 金额
	private String FeedRate;// 换算比例
	private String Mobile;//手机
	List<WithdrawalsInfoItemBean> Items;
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

	public String getMobile() {
		return Mobile;
	}

	public void setMobile(String mobile) {
		Mobile = mobile;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<WithdrawalsInfoItemBean> getItems() {
		return Items;
	}

	public void setItems(List<WithdrawalsInfoItemBean> items) {
		Items = items;
	}

	@Override
	public String toString() {
		return "WithdrawalsInfoResponse [Gold=" + Gold + ", Money=" + Money
				+ ", FeedRate=" + FeedRate + ", Mobile=" + Mobile + ", Items="
				+ Items + "]";
	}

	public static WithdrawalsInfoResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			WithdrawalsInfoResponse response = gson.fromJson(json,WithdrawalsInfoResponse.class);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static List<WithdrawalsInfoResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<WithdrawalsInfoResponse> lists = new ArrayList<WithdrawalsInfoResponse>();
			lists = gson.fromJson(json, new TypeToken<List<WithdrawalsInfoResponse>>() {
			}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
