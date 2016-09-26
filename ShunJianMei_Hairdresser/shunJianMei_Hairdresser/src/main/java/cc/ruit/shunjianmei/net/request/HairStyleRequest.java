package cc.ruit.shunjianmei.net.request;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

import cc.ruit.shunjianmei.base.BaseRequest;
/**
 * @ClassName: HairStyleRequest
 * @Description: 发型分类
 * @author: Johnny
 * @date: 2015年10月14日 上午10:00:30
 */
public class HairStyleRequest extends BaseRequest {
	
	
	private String UserID; //用户ID
	/**
	 * @Title:HairStyleListRequest
	 * @Description:TODO
	 * @param UserID 用户ID
	 */
	public HairStyleRequest(String UserID) {
		super("HairStyle", "1.0");
		this.UserID=UserID;
		String uid=System.currentTimeMillis()+"";
		setUid(uid, OruitKey.encrypt(uid, "HairStyle"));
	}
	
	/**
	 * @Title: toJsonString
	 * @Description:  把对象转成json格式的字符串
	 * @author: Johnny
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(HairStyleRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}
	
	@Override
	public String toString() {
		return "HairStyleRequest [UserID=" + UserID + "]";
	}
	

	

}
