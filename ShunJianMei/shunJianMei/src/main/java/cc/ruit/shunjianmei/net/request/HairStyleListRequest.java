package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * 
 * @ClassName: HairStyleListRequest
 * @Description: 发型列表
 * @author: 欧阳
 * @date: 2015年10月24日 下午4:57:16
 */
public class HairStyleListRequest extends BaseRequest {
	// 用户id
	private String UserID;// 用户ID
	private String ID;// 发型分类ID 0代表全部发型
	private String Sort;// 价格排序 0低到高,1高到低
	private String CityID;// 城市ID
	private String PageSize;// 返回数据行数
	private String PageIndex;// 当前页数 从1开始

	/**
	 * @Title:ActivityListRequest
	 * @Description:活动
	 * @param UserId
	 */
	public HairStyleListRequest(String UserID, String ID, String Sort, String CityID, String PageSize,
			String PageIndex) {
		super("HairStyleList2", "2.0");
		this.UserID = UserID;
		this.ID = ID;
		this.Sort = Sort;
		this.CityID = CityID;
		this.PageSize = PageSize;
		this.PageIndex = PageIndex;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "HairStyleList2"));
	}

	/**
	 * @Title: toJsonString
	 * @Description: 把对象转成json格式的字符串
	 * @author: lee
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
		return "HairStyleListRequest [UserID=" + UserID + ", ID=" + ID + ", Sort=" + Sort + ", CityID=" + CityID
				+ ", PageSize=" + PageSize + ", PageIndex=" + PageIndex + "]";
	}

}
