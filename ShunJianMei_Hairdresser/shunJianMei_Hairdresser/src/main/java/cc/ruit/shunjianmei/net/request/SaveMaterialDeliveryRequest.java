package cc.ruit.shunjianmei.net.request;

import java.util.ArrayList;

import cc.ruit.shunjianmei.base.BaseRequest;
import cc.ruit.shunjianmei.home.manager.material.ProductInstance;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

public class SaveMaterialDeliveryRequest extends BaseRequest {

	// 用户id
	private String UserID;
	private String Date;
	private String AddressID;
	private ArrayList<ProductInstance> Item;

	public SaveMaterialDeliveryRequest(String UserId, String Date, String AddressID,
			ArrayList<ProductInstance> Item) {
		super("H_SaveMaterialDelivery", "1.0");
		this.UserID = UserId;
		this.Date = Date;
		this.AddressID = AddressID;
		this.Item = Item;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "H_SaveMaterialDelivery"));
	}

	/**
	 * @Title: toJsonString
	 * @Description: 把对象转成json格式的字符串
	 * @author: lee
	 * @param request
	 * @return: String
	 */
	public String toJsonString(SaveMaterialDeliveryRequest request) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(request);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "H_SaveMaterialDelivery [UserID=" + UserID + ", Method="
				+ Method + ", Infversion=" + Infversion + ", Key=" + Key
				+ ", Date=" + Date + ", AddressID=" + AddressID + ", Item="
				+ Item + ", UID=" + UID + "]";
	}
}
