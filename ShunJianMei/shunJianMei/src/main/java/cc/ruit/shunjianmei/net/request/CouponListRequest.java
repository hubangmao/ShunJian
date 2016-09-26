package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * @ClassName: CouponListRequest
 * @Description: 优惠劵列表
 * @author: Johnny
 * @date: 2015年10月14日 下午4:41:29
 */
public class CouponListRequest extends BaseRequest {
	private String UserID;//用户ID
	private String PageSize;//返回数据行数 
	private String PageIndex;//当前页数 从1开始

	public CouponListRequest(String userID, 
			String pageSize, String pageIndex) {
		super("CouponList", "1.0");
		this.UserID = userID;
		this.PageSize = pageSize;
		this.PageIndex = pageIndex;
		
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "CouponList"));
	}

	/**
	 * @Title: toJsonString
	 * @Description:  把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(CouponListRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "CouponListRequest [UserID=" + UserID + ", PageSize=" + PageSize
				+ ", PageIndex=" + PageIndex + "]";
	}

}
