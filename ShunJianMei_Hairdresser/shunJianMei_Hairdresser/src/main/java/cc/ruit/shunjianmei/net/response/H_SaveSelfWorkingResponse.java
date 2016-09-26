package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cc.ruit.shunjianmei.base.BaseResponse;

public class H_SaveSelfWorkingResponse extends BaseResponse implements Serializable {

	private String OrderID;// 订单ID
	private static final long serialVersionUID = 1L;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getOrderID() {
		return OrderID;
	}

	public void setOrderID(String orderID) {
		OrderID = orderID;
	}

	public H_SaveSelfWorkingResponse(String orderID) {
		super();
		OrderID = orderID;
	}

	public static H_SaveSelfWorkingResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			H_SaveSelfWorkingResponse lists = gson.fromJson(json, H_SaveSelfWorkingResponse.class);
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<H_SaveSelfWorkingResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<H_SaveSelfWorkingResponse> lists = new ArrayList<H_SaveSelfWorkingResponse>();
			lists = gson.fromJson(json, new TypeToken<List<H_SaveSelfWorkingResponse>>() {
			}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String toString() {
		return "H_SaveSelfWorkingResponse [OrderID=" + OrderID + "]";
	}

}
