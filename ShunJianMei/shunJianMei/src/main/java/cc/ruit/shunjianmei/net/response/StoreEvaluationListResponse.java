package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;
/**
 * 
 * @ClassName: EvaluationListResponse
 * @Description: 商户评论列表
 * @author: Johnny
 * @date: 2015年10月12日 下午10:26:45
 */
public class StoreEvaluationListResponse implements Serializable{
	/**@fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: 
	 */
	private static final long serialVersionUID = 1L;
	private String ID;// 评论ID
	private String Name;// 用户名称
	private String Photo;// 用户头像
	private String Score;//评分
	private String Time;//评论时间
	private String Content;//评论内容
	private List<Images> Images;
	
	public static class Images {
		public String Image;
	}
	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getPhoto() {
		return Photo;
	}

	public void setPhoto(String photo) {
		Photo = photo;
	}

	public String getScore() {
		return Score;
	}

	public void setScore(String score) {
		Score = score;
	}

	public String getTime() {
		if (Time.length() < 4) {
			return Time;
		}
		return Time.substring(0, Time.length()-3);
	}

	public void setTime(String time) {
		Time = time;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public List<Images> getImages() {
		return Images;
	}

	public void setImages(List<Images> images) {
		Images = images;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "EvaluationListResponse [ID=" + ID + ", Name=" + Name
				+ ", Photo=" + Photo + ", Score=" + Score + ", Time=" + Time
				+ ", Content=" + Content + ", Images=" + Images + "]";
	}

	public static StoreEvaluationListResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			StoreEvaluationListResponse lists = gson.fromJson(json, StoreEvaluationListResponse.class);
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static List<StoreEvaluationListResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<StoreEvaluationListResponse> lists = new ArrayList<StoreEvaluationListResponse>();
			lists = gson.fromJson(json, new TypeToken<List<StoreEvaluationListResponse>>() {
			}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
