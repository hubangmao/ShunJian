package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cc.ruit.shunjianmei.base.BaseResponse;

public class H_QuickSettingResponse extends BaseResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	public static H_QuickSettingResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			H_QuickSettingResponse lists = gson.fromJson(json, H_QuickSettingResponse.class);
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<H_QuickSettingResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<H_QuickSettingResponse> lists = new ArrayList<H_QuickSettingResponse>();
			lists = gson.fromJson(json, new TypeToken<List<H_QuickSettingResponse>>() {
			}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
