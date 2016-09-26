package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * @ClassName: FavoriteStoreListRequest
 * @Description: 收藏商户列表
 * @author: Johnny
 * @date: 2015年10月14日 下午4:41:29
 */
public class FavoriteStoreListRequest extends BaseRequest {
	private String UserID;// 用户ID
	private String Longitude;// 经度
	private String Latitude;// 纬度
	private String PageIndex;// 返回数据行数
	private String PageSize;// 当前页数 从1开始

	public FavoriteStoreListRequest(String userID, String longitude,
			String latitude, String pageIndex, String pageSize) {
		super("FavoriteStoreList", "1.0");
		UserID = userID;
		Longitude = longitude;
		Latitude = latitude;
		PageIndex = pageIndex;
		PageSize = pageSize;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "FavoriteStoreList"));
	}

	/**
	 * @Title: toJsonString
	 * @Description: 把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(FavoriteStoreListRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "FavoriteStoreListRequest [UserID=" + UserID + ", Longitude="
				+ Longitude + ", Latitude=" + Latitude + ", PageIndex="
				+ PageIndex + ", PageSize=" + PageSize + "]";
	}

	
}
