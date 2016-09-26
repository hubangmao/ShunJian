package cc.ruit.shunjianmei.net.request;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

import cc.ruit.shunjianmei.base.BaseRequest;
/**
 * @ClassName: HairStyleListRequest
 * @Description: 发型列表 获取发型列表
 * @author: 高俊
 * @date: 2015年10月14日 上午10:00:30
 */
public class HairStyleListRequest extends BaseRequest {
	
	
	private String UserID; //用户ID
	/**
	 * @Title:HairStyleListRequest
	 * @Description:TODO
	 * @param UserID 用户ID
	 */
	public HairStyleListRequest(String UserID) {
		super("H_HairStyleList", "1.0");
		// TODO Auto-generated constructor stub
		this.UserID=UserID;
		String uid=System.currentTimeMillis()+"";
		setUid(uid, OruitKey.encrypt(uid, "H_HairStyleList"));
	}
	
	/**
	 * @Title: toJsonString
	 * @Description:  把对象转成json格式的字符串
	 * @author: 高俊
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(HairStyleListRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}
	@Override
	public String toString() {
		return "HairStyleListRequest [UserID=" + UserID + ", Method=" + Method
				+ ", Infversion=" + Infversion + ", Key=" + Key + ", UID="
				+ UID + "]";
	}
	

	

}
