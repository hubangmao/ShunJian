package cc.ruit.shunjianmei.net.response;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
/**
 * 
 * @ClassName: SaveEvaluationResponse
 * @Description: 保存店铺请求
 * @author: 欧阳
 * @date: 2015年10月29日 上午8:37:32
 */
public class SaveEvaluationResponse {
	private String EvalID;//评论ID
	public static SaveEvaluationResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			SaveEvaluationResponse response = gson.fromJson(json,SaveEvaluationResponse.class);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static List<SaveEvaluationResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<SaveEvaluationResponse> lists = new ArrayList<SaveEvaluationResponse>();
			lists = gson.fromJson(json, new TypeToken<List<SaveEvaluationResponse>>() {
			}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public String getEvalID() {
		return EvalID;
	}
	public void setEvalID(String evalID) {
		EvalID = evalID;
	}
	
}
