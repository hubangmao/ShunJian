package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * 
 * @ClassName: TradeRecordRequest
 * @Description: 我的消费记录
 * @author: Johnny
 * @date: 2015年10月12日 下午10:23:16
 */
public class TradeRecordRequest extends BaseRequest {

	private String UserID;// 用户id
	private String PageSize;// 返回数据行数
	private String PageIndex;// 当前页数 从1开始
	private String Type;// 0全部 1支付2赔偿3退款
	private String Sort;// 0由近到远1由远到近

	/**
	 * 
	 * @Title:TradeRecordRequest
	 * @Description:我的消费记录
	 * @param UserId
	 * @param PageSize 
	 * @param PageIndex 
	 * @param Type 
	 * @param Sort 
	 */
	public TradeRecordRequest(String UserId, String PageSize, String PageIndex, String Type, String Sort) {
		super("H_TradeRecord", "1.0");
		this.UserID = UserId;
		this.PageSize = PageSize;
		this.PageIndex = PageIndex;
		this.Type = Type;
		this.Sort = Sort;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "H_TradeRecord"));
	}

	/**
	 * @Title: toJsonString
	 * @Description: 把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(TradeRecordRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "TradeRecordRequest [UserID=" + UserID + ", PageSize="
				+ PageSize + ", PageIndex=" + PageIndex + ", Type=" + Type
				+ ", Sort=" + Sort + "]";
	}

}
