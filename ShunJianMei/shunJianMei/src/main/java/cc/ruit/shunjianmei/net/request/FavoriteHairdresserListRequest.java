package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * @ClassName: FavoriteHairdresserListRequest
 * @Description: 收藏发型列表
 * @author: Johnny
 * @date: 2015年10月14日 下午4:41:29
 */
public class FavoriteHairdresserListRequest extends BaseRequest {
	private String UserID;// 用户ID
	private String PageIndex;// 返回数据行数
	private String PageSize;// 当前页数 从1开始

	public FavoriteHairdresserListRequest(String userID, String pageIndex, String pageSize) {
		super("FavoriteHairdresserList", "1.0");
		UserID = userID;
		PageIndex = pageIndex;
		PageSize = pageSize;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "FavoriteHairdresserList"));
	}

	/**
	 * @Title: toJsonString
	 * @Description: 把对象转成json格式的字符串
	 * @author: Johnny
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(FavoriteHairdresserListRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "FavoriteHairdresserListRequest [UserID=" + UserID
				+ ", PageIndex=" + PageIndex + ", PageSize=" + PageSize + "]";
	}

}
