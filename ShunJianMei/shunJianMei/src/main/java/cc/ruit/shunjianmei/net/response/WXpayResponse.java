package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
/**
 * @ClassName: MessageListResponse
 * @Description: 消息列表
 * @author: lee
 * @date: 2015年9月5日 下午5:41:17
 */
public class WXpayResponse implements Serializable{
	/**@fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: 
	 */
	private static final long serialVersionUID = 1L;
	public String appid;// appid
	public String noncestr;// 
	public String partnerid;//下载地址
	public String prepayid;//重要更新 0否1是
	public String sign;//重要更新 0否1是
	public String timestamp;//重要更新 0否1是
	
//	{"appid":"1279859301","noncestr":"39e8a69c46f444c5950cdc9a68ef8711","package":"Sign=WXPay","partnerid":"wxe99c393389decb57","prepayid":"wx201511231008381d9755f09b0369990753","sign":"52436275B5EAE321CC5D529C14F4BC5A","timestamp":"1448244518"}



	public String getAppid() {
		return appid;
	}

	@Override
	public String toString() {
		return "WXpayResponse [appid=" + appid + ", noncestr=" + noncestr
				+ ", partnerid=" + partnerid + ", prepayid=" + prepayid
				+ ", sign=" + sign + ", timestamp=" + timestamp + "]";
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getNoncestr() {
		return noncestr;
	}

	public void setNoncestr(String noncestr) {
		this.noncestr = noncestr;
	}

	public String getPartnerid() {
		return partnerid;
	}

	public void setPartnerid(String partnerid) {
		this.partnerid = partnerid;
	}

	public String getPrepayid() {
		return prepayid;
	}

	public void setPrepayid(String prepayid) {
		this.prepayid = prepayid;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static WXpayResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			WXpayResponse obj = gson.fromJson(json, WXpayResponse.class);
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static List<WXpayResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<WXpayResponse> lists = new ArrayList<WXpayResponse>();
			lists = gson.fromJson(json, new TypeToken<List<WXpayResponse>>() {
			}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
