package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cc.ruit.shunjianmei.base.BaseResponse;

public class H_GetDayWorkingResponse extends BaseResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	private String OrderID;// 订单ID
	private String Name;// 客户名
	private String Mobile;// 电话
	private String StartHour;// 开始时间
	private String EndHour;// 结束时间
	private String Type = "";// 1表示来自顺间平台的预约，2表示来自美发师自己的编辑
	private String Content = "";// 发型类型

	public String getOrderID() {
		return OrderID;
	}

	public void setOrderID(String orderID) {
		OrderID = orderID;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getMobile() {
		return Mobile;
	}

	public void setMobile(String mobile) {
		Mobile = mobile;
	}

	public String getStartHour() {
		return StartHour;
	}

	public void setStartHour(String startHour) {
		StartHour = startHour;
	}

	public String getEndHour() {
		return EndHour;
	}

	public void setEndHour(String endHour) {
		EndHour = endHour;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	@Override
	public String toString() {
		return "H_GetDayWorkingResponse [OrderID=" + OrderID + ", Name=" + Name + ", Mobile=" + Mobile + ", StartHour="
				+ StartHour + ", EndHour=" + EndHour + ", Type=" + Type + ", Content=" + Content + "]";
	}

	public static H_GetDayWorkingResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			H_GetDayWorkingResponse lists = gson.fromJson(json, H_GetDayWorkingResponse.class);
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<H_GetDayWorkingResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<H_GetDayWorkingResponse> lists = new ArrayList<H_GetDayWorkingResponse>();
			lists = gson.fromJson(json, new TypeToken<List<H_GetDayWorkingResponse>>() {
			}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
