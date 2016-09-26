package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cc.ruit.shunjianmei.base.BaseResponse;

public class H_GetSelfWorkingResponse extends BaseResponse implements Serializable {
	private String UserID;
	private String OrderID;
	private static final long serialVersionUID = 1L;

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public String getOrderID() {
		return OrderID;
	}

	public void setOrderID(String orderID) {
		OrderID = orderID;
	}

	@Override
	public String toString() {
		return "H_GetSelfWorkingResponse [UserID=" + UserID + ", OrderID=" + OrderID + "]";
	}

	public static H_GetSelfWorkingResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			H_GetSelfWorkingResponse lists = gson.fromJson(json, H_GetSelfWorkingResponse.class);
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<H_GetSelfWorkingResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<H_GetSelfWorkingResponse> lists = new ArrayList<H_GetSelfWorkingResponse>();
			lists = gson.fromJson(json, new TypeToken<List<H_GetSelfWorkingResponse>>() {
			}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
