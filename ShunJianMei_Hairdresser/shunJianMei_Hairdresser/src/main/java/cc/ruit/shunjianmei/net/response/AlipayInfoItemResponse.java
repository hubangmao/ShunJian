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
public class AlipayInfoItemResponse implements Serializable{
	/**@fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: 
	 */
	private static final long serialVersionUID = 1L;
	private String Money;// 金额

	public String getMoney() {
		return Money;
	}

	public void setMoney(String money) {
		Money = money;
	}


	@Override
	public String toString() {
		return "AlipayInfoItemResponse [Money=" + Money + "]";
	}

	public static AlipayInfoItemResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			AlipayInfoItemResponse response = gson.fromJson(json,AlipayInfoItemResponse.class);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static List<AlipayInfoItemResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<AlipayInfoItemResponse> lists = new ArrayList<AlipayInfoItemResponse>();
			lists = gson.fromJson(json, new TypeToken<List<AlipayInfoItemResponse>>() {
			}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
