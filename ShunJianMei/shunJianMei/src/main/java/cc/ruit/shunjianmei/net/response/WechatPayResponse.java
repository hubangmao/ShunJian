package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
/**
 * @ClassName: WechatPayResponse
 * @Description: 微信支付id返回
 * @author: lee
 * @date: 2015年9月5日 下午5:41:17
 */
public class WechatPayResponse implements Serializable{
	/**@fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: 
	 */
	private static final long serialVersionUID = 1L;
	public String WechatPayID;// 微信支付id



	public String getWechatPayID() {
		return WechatPayID;
	}

	public void setWechatPayID(String wechatPayID) {
		WechatPayID = wechatPayID;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "WechatPayResponse [WechatPayID=" + WechatPayID + "]";
	}

	public static WechatPayResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			WechatPayResponse obj = gson.fromJson(json, WechatPayResponse.class);
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static List<WechatPayResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<WechatPayResponse> lists = new ArrayList<WechatPayResponse>();
			lists = gson.fromJson(json, new TypeToken<List<WechatPayResponse>>() {
			}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
