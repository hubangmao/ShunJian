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
 * @Description: 发型师评论列表
 * @author: Johnny
 * @date: 2015年10月12日 下午10:26:45
 */
public class EvaluationListResponse implements Serializable{
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
		return Time;
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

	public static EvaluationListResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			EvaluationListResponse lists = gson.fromJson(json, EvaluationListResponse.class);
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static List<EvaluationListResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<EvaluationListResponse> lists = new ArrayList<EvaluationListResponse>();
			lists = gson.fromJson(json, new TypeToken<List<EvaluationListResponse>>() {
			}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
