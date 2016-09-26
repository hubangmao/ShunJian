package cc.ruit.shunjianmei.net.request;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

import cc.ruit.shunjianmei.base.BaseRequest;
/**
 * @ClassName: HairStyleListRequest
 * @Description: 发型详情 我的发型
 * @author: 高俊
 * @date: 2015年10月14日 上午10:00:30
 */
public class HairStyleDetailRequest extends BaseRequest {
	
	
	private String UserID; //用户ID
	private String ID; //发型ID
	/**
	 * @Title:HairStyleListRequest
	 * @Description:TODO
	 * @param UserID 用户ID
	 */
	public HairStyleDetailRequest(String UserID,String ID) {
		super("H_HairStyleDetail", "1.0");
		// TODO Auto-generated constructor stub
		this.UserID=UserID;
		this.ID=ID;
		String uid=System.currentTimeMillis()+"";
		setUid(uid, OruitKey.encrypt(uid, "H_HairStyleDetail"));
	}
	
	/**
	 * @Title: toJsonString
	 * @Description:  把对象转成json格式的字符串
	 * @author: 高俊
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(HairStyleDetailRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}
	@Override
	public String toString() {
		return "HairStyleDetailRequest [UserID=" + UserID + ", ID=" + ID
				+ ", Method=" + Method + ", Infversion=" + Infversion
				+ ", Key=" + Key + ", UID=" + UID + "]";
	}
	

	

}
