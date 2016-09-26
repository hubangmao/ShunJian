package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cc.ruit.shunjianmei.base.BaseResponse;

public class H_SetInformationResponse extends BaseResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	public static H_SetInformationResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			H_SetInformationResponse lists = gson.fromJson(json, H_SetInformationResponse.class);
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<H_SetInformationResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<H_SetInformationResponse> lists = new ArrayList<H_SetInformationResponse>();
			lists = gson.fromJson(json, new TypeToken<List<H_SetInformationResponse>>() {
			}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
