package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * 
 * @ClassName: SaveShareRecordRequest
 * @Description: 保存分享记录
 * @author: 欧阳
 * @date: 2015年10月29日 上午9:05:21
 */
public class SaveShareRecordRequest extends BaseRequest {
	
	private String UserID;// 用户id
	private String EvaID;//评论ID
	private String PicID;//图片ID,多图用过|连接

	/**
	 * 
	 * @Title:SaveShareRecordRequest
	 * @Description:分享记录
	 * @param UserID
	 * @param EvaID
	 * @param PicID
	 */
	public SaveShareRecordRequest(String UserID, String EvaID, String PicID) {
		super("SaveShareRecord", "1.0");
		this.UserID = UserID;
		this.EvaID = EvaID;
		this.PicID = PicID;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "SaveShareRecord"));
	}

	/**
	 * @Title: toJsonString
	 * @Description: 把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(SaveShareRecordRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "SaveShareRecordRequest [UserID=" + UserID + ", EvaID=" + EvaID
				+ ", PicID=" + PicID + "]";
	}

}
