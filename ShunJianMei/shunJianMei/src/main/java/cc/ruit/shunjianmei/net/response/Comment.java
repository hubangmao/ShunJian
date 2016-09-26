package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Comment implements Serializable {
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 3L;
	public String ID;// ID 评论ID
	public String Name;// Name 评论者
	public String Photo;// Photo 头像
	public String Time;// 评论时间
	public String Content;// 评论内容
	public String Score;// 评论分数
	public List<Item> Item;//

	public static class Item implements Serializable {
		public String Photo;// 照片
		@Override
		public String toString() {
			return "Item [Photo=" + Photo + "]";
		}
		
	}

	public static Comment getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			Comment comment = gson.fromJson(json, Comment.class);
			return comment;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<Comment> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<Comment> lists = new ArrayList<Comment>();
			lists = gson.fromJson(json, new TypeToken<List<Comment>>() {
			}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String toString() {
		return "Comment [ID=" + ID + ", Name=" + Name + ", Photo=" + Photo
				+ ", Time=" + Time + ", Content=" + Content + ", Score="
				+ Score + ", Item=" + Item + "]";
	}

}
