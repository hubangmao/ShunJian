package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * @ClassName: HairStyleDetailResponse
 * @Description: 发型详情响应
 * @author: 欧阳
 * @date: 2015年10月25日 下午1:37:28
 */
public class HairStyleDetailResponse implements Serializable {
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description:
	 */
	private static final long serialVersionUID = 1L;
	private String ID;// 发型ID
	private String Name;// 发型师名称
	private String Photo;// 头像
	private String Price;// 价格
	private ServiceTimes ServiceTimes;// 各种服务时长
	private User User;
	private String IsFavorite;// 是否收藏 0否1是
	private String CommentNum;// 评论数
	private Comment Comment;// 评论
	private String Intro;

	public ServiceTimes getServiceTimes() {
		return ServiceTimes;
	}

	public void setServiceTimes(ServiceTimes serviceTimes) {
		ServiceTimes = serviceTimes;
	}

	public static class User {
		public String UserID; // 美发师ID
		public String Name; // 美发师名字
		public String Photo; // 头像
		public String LevelName; // 职称等级
		public String Star; // 星座
		public String Score; // 评分
		public String OrderNum; // 接单量
		public String istype;// 类型
		public String hasPacked;// 是否有套餐

	}

	public static class ServiceTimes {
		public String 洗剪吹; // 用户ID
		public String 烫发; // 美发师名字
		public String 染发; // 头像
		public String 护理; // 职称等级
		public String 设计剪发;
		public String 造型;

		public String toString(String 染发, String 烫发) {
			StringBuffer sb = new StringBuffer();
			if (!TextUtils.isEmpty(染发)) {
				sb.append("染发约" + 染发 + "分钟  ");
			}
			if (!TextUtils.isEmpty(烫发)) {
				sb.append("烫发约" + 烫发 + "分钟  ");
			}
			return sb.toString();
		}

		/**
		 * 
		 * @Title: toString2
		 * @Description: 第二行
		 * @author: 欧阳
		 * @param 护理
		 * @param 设计剪发
		 * @param 造型
		 * @return
		 * @return: String
		 */
		public String toString2(String 设计剪发) {
			StringBuffer sb = new StringBuffer();
			if (!TextUtils.isEmpty(设计剪发)) {
				sb.append("设计剪发约" + 设计剪发 + "分钟  ");
			}
			return sb.toString();
		}
	}

	public static class Item {
		public String Name;// Name 服务名称
	}

	public static class Images {
		public static String Photo;// photo 照片
	}

	public static HairStyleDetailResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			HairStyleDetailResponse response = gson.fromJson(json, HairStyleDetailResponse.class);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<HairStyleDetailResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<HairStyleDetailResponse> lists = new ArrayList<HairStyleDetailResponse>();
			lists = gson.fromJson(json, new TypeToken<List<HairStyleDetailResponse>>() {
			}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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

	public String getIsFavorite() {
		return IsFavorite;
	}

	public void setIsFavorite(String isFavorite) {
		IsFavorite = isFavorite;
	}

	public String getCommentNum() {
		return CommentNum;
	}

	public void setCommentNum(String commentNum) {
		CommentNum = commentNum;
	}

	public Comment getComment() {
		return Comment;
	}

	public void setComment(Comment comment) {
		Comment = comment;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getPrice() {
		return Price;
	}

	public void setPrice(String price) {
		Price = price;
	}

	// public ServiceTimes getServiceTimes() {
	// return ServiceTimes;
	// }
	//
	// public void setServiceTimes(ServiceTimes serviceTimes) {
	// ServiceTimes = serviceTimes;
	// }

	public User getUser() {
		return User;
	}

	public void setUser(User user) {
		User = user;
	}

	public String getIntro() {
		return Intro;
	}

	public void setIntro(String intro) {
		Intro = intro;
	}

	@Override
	public String toString() {
		return "HairStyleDetailResponse [ID=" + ID + ", Name=" + Name + ", Photo=" + Photo + ", Price=" + Price
				+ ",  User=" + User + ", IsFavorite=" + IsFavorite + ", CommentNum=" + CommentNum + ", Comment="
				+ Comment + ", Intro=" + Intro + "]";
	}

}
