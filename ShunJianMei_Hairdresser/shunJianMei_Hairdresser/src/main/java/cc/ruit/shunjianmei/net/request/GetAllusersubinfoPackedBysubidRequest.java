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
public class GetAllusersubinfoPackedBysubidRequest extends BaseRequest {
	// 用户id
	private String id;

	/**
	 * @Title:ActivityListRequest
	 * @Description:活动
	 * @param UserId
	 */
	public GetAllusersubinfoPackedBysubidRequest(String id) {
		super("GetAllusersubinfoPackedBysubid", "2.0");
		this.id = id;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "GetAllusersubinfoPackedBysubid"));
	}

	/**
	 * @Title: toJsonString
	 * @Description: 把对象转成json格式的字符串
	 * @author: lee
	 * @param request
	 * @return: String
	 */
	public String toJsonString(GetAllusersubinfoPackedBysubidRequest request) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(request);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "GetCustomerServicePhoneRequest [UserID=" + id + "]";
	}

}
