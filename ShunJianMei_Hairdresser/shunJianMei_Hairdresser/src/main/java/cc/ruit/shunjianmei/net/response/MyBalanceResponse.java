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
 * @ClassName: MeResponse
 * @Description: 我的出参
 * @author: Johnny
 * @date: 2015年10月12日 下午10:26:45
 */
public class MyBalanceResponse implements Serializable{
	/**@fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: 
	 */
	private static final long serialVersionUID = 1L;
	
	private String Total;// 钱包合计金额
	private String Amount;// 可提现金额
	
	public String getTotal() {
		return Total;
	}

	public void setTotal(String total) {
		Total = total;
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
		return "MyBalanceResponse [Total=" + Total + ", Amount=" + Amount + "]";
	}

	public static MyBalanceResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			MyBalanceResponse lists = gson.fromJson(json, MyBalanceResponse.class);
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static List<MyBalanceResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<MyBalanceResponse> lists = new ArrayList<MyBalanceResponse>();
			lists = gson.fromJson(json, new TypeToken<List<MyBalanceResponse>>() {
			}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
