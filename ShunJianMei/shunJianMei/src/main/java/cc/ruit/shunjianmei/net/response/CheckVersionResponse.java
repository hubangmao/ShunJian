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
public class CheckVersionResponse implements Serializable{
	/**@fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: 
	 */
	private static final long serialVersionUID = 1L;
	public String Name;// 版本名称
	public String Code;// 版本号
	public String Intro;// 版本简介
	public String URL;//下载地址
	public String Important;//重要更新 0否1是

	public String getName() {
		return Name;
	}


	public void setName(String name) {
		Name = name;
	}


	public String getCode() {
		return Code;
	}


	public void setCode(String code) {
		Code = code;
	}


	public String getIntro() {
		return Intro;
	}


	public void setIntro(String intro) {
		Intro = intro;
	}


	public String getURL() {
		return URL;
	}


	public void setURL(String uRL) {
		URL = uRL;
	}


	public String getImportant() {
		return Important;
	}


	public void setImportant(String important) {
		Important = important;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	@Override
	public String toString() {
		return "CheckVersionResponse [Name=" + Name + ", Code=" + Code
				+ ", Intro=" + Intro + ", URL=" + URL + ", Important="
				+ Important + "]";
	}

	public static CheckVersionResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			CheckVersionResponse obj = gson.fromJson(json, CheckVersionResponse.class);
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static List<CheckVersionResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<CheckVersionResponse> lists = new ArrayList<CheckVersionResponse>();
			lists = gson.fromJson(json, new TypeToken<List<CheckVersionResponse>>() {
			}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
