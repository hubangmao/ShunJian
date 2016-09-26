package cc.ruit.shunjianmei.net.request;

import java.util.List;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * 
 * @ClassName: H_SaveHairStyleRequest
 * @Description: 保存发型
 * @author: 欧阳
 * @date: 2015年10月17日 下午3:39:14
 */
public class H_SaveHairStyleRequest extends BaseRequest {
	private String UserID;// 用户ID
	private String ID;// 我的发型项目ID 新增为0
	private String PicID1;// 列表图ID
	private String PicID2;// 详情图ID
	private String StyleID;// 发型项目ID
	private String Remark;// 备注说明
	private List<ServiceItem> Item;//

	/**
	 * @Title:ActivityListRequest
	 * @Description:活动
	 * @param UserId
	 */
	public H_SaveHairStyleRequest(String UserID, String ID, String PicID1,
			String PicID2, String StyleID, String Remark, List<ServiceItem> Item) {
		super("H_SaveHairStyle", "1.0");
		this.UserID = UserID;
		this.ID = ID;
		this.PicID1 = PicID1;
		this.PicID2 = PicID2;
		this.StyleID = StyleID;
		this.Remark = Remark;
		this.Item = Item;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "H_SaveHairStyle"));
	}

	/**
	 * @Title: toJsonString
	 * @Description: 把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(H_SaveHairStyleRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}
	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getPicID1() {
		return PicID1;
	}

	public void setPicID1(String picID1) {
		PicID1 = picID1;
	}

	public String getPicID2() {
		return PicID2;
	}

	public void setPicID2(String picID2) {
		PicID2 = picID2;
	}

	public String getStyleID() {
		return StyleID;
	}

	public void setStyleID(String styleID) {
		StyleID = styleID;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

	public List<ServiceItem> getItem() {
		return Item;
	}

	public void setItem(List<ServiceItem> item) {
		Item = item;
	}

	@Override
	public String toString() {
		return "H_SaveHairStyleRequest [UserID=" + UserID + ", ID=" + ID
				+ ", PicID1=" + PicID1 + ", PicID2=" + PicID2 + ", StyleID="
				+ StyleID + ", Remark=" + Remark + ", Item=" + Item + "]";
	}

}
