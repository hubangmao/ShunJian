package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * 
 * @ClassName: GetCustomerServicePhoneRequest
 * @Description: 获取客户电话
 * @author: 欧阳
 * @date: 2015年10月29日 下午2:49:47
 */
public class GetCustomerServicePhoneRequest extends BaseRequest {
	// 用户id
	private String UserID;

	/**
	 * @Title:ActivityListRequest
	 * @Description:活动
	 * @param UserId
	 */
	public GetCustomerServicePhoneRequest(String UserID) {
		super("GetCustomerServicePhone", "1.0");
		this.UserID = UserID;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "GetCustomerServicePhone"));
	}

	/**
	 * @Title: toJsonString
	 * @Description: 把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(GetCustomerServicePhoneRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "GetCustomerServicePhoneRequest [UserID=" + UserID + "]";
	}

}
