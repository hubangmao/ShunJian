package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * @ClassName: CashRecordRequest
 * @Description: 提现
 * @author: Johnny
 * @date: 2015年8月31日 下午11:00:48
 */
public class CashRecordRequest extends BaseRequest {
	private String UserID;//用户ID
	private String Name;//开户人名字
	private String CardID;//卡号
	private String Bank;//开户行
	private String Amount;//金额
	
	public CashRecordRequest(String userId, String Name, String CardID, String Bank, String Amount) {
		super("H_CashRecord", "1.0");
		UserID = userId;
		this.Name = Name;
		this.CardID = CardID;
		this.Bank = Bank;
		this.Amount = Amount;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "H_CashRecord"));
	}

	/**
	 * @Title: toJsonString
	 * @Description:  把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(CashRecordRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "CashRecordRequest [UserID=" + UserID + ", Name=" + Name
				+ ", CardID=" + CardID + ", Bank=" + Bank + ", Amount="
				+ Amount + "]";
	}

}
