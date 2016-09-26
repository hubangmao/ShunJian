package cc.ruit.shunjianmei.net.response;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * @ClassName: SaveShareRecordResponse
 * @Description: 保存分享记录的请求
 * @author: 欧阳
 * @date: 2015年10月29日 上午9:12:26
 */
public class SaveShareRecordResponse {
	private String URL;// 评论ID
	public static SaveShareRecordResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			SaveShareRecordResponse response = gson.fromJson(json,
					SaveShareRecordResponse.class);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<SaveShareRecordResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<SaveShareRecordResponse> lists = new ArrayList<SaveShareRecordResponse>();
			lists = gson.fromJson(json,
					new TypeToken<List<SaveShareRecordResponse>>() {
					}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	@Override
	public String toString() {
		return "SaveShareRecordResponse [URL=" + URL + "]";
	}

}
