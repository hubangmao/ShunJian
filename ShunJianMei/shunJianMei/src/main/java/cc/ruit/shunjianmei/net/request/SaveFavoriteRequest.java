package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * 
 * @ClassName: SaveFavoriteRequest
 * @Description: 收藏
 * @author: 欧阳
 * @date: 2015年10月24日 下午3:44:50
 */
public class SaveFavoriteRequest extends BaseRequest {
	// 用户id
	private String UserID;
	private String Type;
	private String ID;
	private String State;

	/**
	 * @Title:ActivityListRequest
	 * @Description:活动
	 * @param UserId
	 */
	public SaveFavoriteRequest(String UserID, String Type, String ID,
			String State) {
		super("SaveFavorite", "1.0");
		this.UserID = UserID;
		this.Type = Type;
		this.ID = ID;
		this.State = State;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "SaveFavorite"));
	}

	/**
	 * @Title: toJsonString
	 * @Description: 把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(SaveFavoriteRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "SaveFavoriteRequest [UserID=" + UserID + ", Type=" + Type
				+ ", ID=" + ID + ", State=" + State + "]";
	}

}
