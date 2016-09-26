package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
/**
 * @ClassName: HairListResponse
 * @Description: 提现信息返回
 * @author: gaoj
 * @date: 2015年10月14日 下午6:06:38
 */
public class HairListResponse implements Serializable{

	
	private String ID;  //发型ID
	private String Name; //发型名称
	private String Photo; //列表图
	private String Price; //价格
	private String UsedNum; //使用次数
	private String State; //状态 1审核中2审核失败3待发布4已发布审核中,无操作审核失败,可编辑,保存是审核中待发布,可编辑,保存是审核中已发布,可编辑,保存是待发布
	public String getID() {
		return ID;
	}


	public void setID(String iD) {
		ID = iD;
	}


	public String getName() {
		return Name;
	}


	public void setName(String name) {
		Name = name;
	}


	public String getPhoto() {
		return Photo;
	}


	public void setPhoto(String photo) {
		Photo = photo;
	}


	public String getPrice() {
		return Price;
	}


	public void setPrice(String price) {
		Price = price;
	}


	public String getUsedNum() {
		return UsedNum;
	}


	public void setUsedNum(String usedNum) {
		UsedNum = usedNum;
	}


	public String getState() {
		return State;
	}


	public void setState(String state) {
		State = state;
	}


	@Override
	public String toString() {
		return "HairBean [ID=" + ID + ", Name=" + Name + ", Photo=" + Photo
				+ ", Price=" + Price + ", UsedNum=" + UsedNum + ", State="
				+ State + "]";
	}

	public static HairListResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			HairListResponse lists = gson.fromJson(json,
					HairListResponse.class);
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<HairListResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<HairListResponse> lists = new ArrayList<HairListResponse>();
			lists = gson.fromJson(json,
					new TypeToken<List<HairListResponse>>() {
					}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	


}
