package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cc.ruit.shunjianmei.base.BaseResponse;

public class H_SetMasterDefaultSettingResponse extends BaseResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	public static H_SetMasterDefaultSettingResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			H_SetMasterDefaultSettingResponse lists = gson.fromJson(json, H_SetMasterDefaultSettingResponse.class);
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<H_SetMasterDefaultSettingResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<H_SetMasterDefaultSettingResponse> lists = new ArrayList<H_SetMasterDefaultSettingResponse>();
			lists = gson.fromJson(json, new TypeToken<List<H_SetMasterDefaultSettingResponse>>() {
			}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
