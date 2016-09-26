package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * @ClassName: HairdresserDetailResponse
 * @Description: 发型师详情响应
 * @author: 欧阳
 * @date: 2015年10月23日 下午4:17:25
 */
public class HairdresserDetailResponse implements Serializable {
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description:
	 */
	private static final long serialVersionUID = 1L;
	private String UserID;// 发型师ID
	private String Name;// 发型师名称
	private String Photo;// 头像
	private String LevelName;// 职称等级
	private String WorkingLife;// 工作年限
	private String Star;// 星座
	private String Score;// 评分
	private String OrderNum;// 接单量
	private String IsFavorite;// 是否收藏 0否1是
	private String Price;//
	private String Nationality; // 国籍
	private String Language; // 语言
	private String CommentNum;// 评论数
	private Comment Comment;// 评论
	private List<Images> Images;// Photo
	private Intro Intro;
	private List<HairStyleListResponse> HairStyle;// 发型
	private String hasPacked;// 是否有此套餐

	public String getHasPacked() {
		return hasPacked;
	}

	public void setHasPacked(String hasPacked) {
		this.hasPacked = hasPacked;
	}

	public static class Item implements Serializable {
		/**
		 * @fieldName: serialVersionUID
		 * @fieldType: long
		 * @Description: TODO
		 */
		private static final long serialVersionUID = 1L;
		public String Name;// Name 服务名称
	}

	public static class Images implements Serializable {
		/**
		 * @fieldName: serialVersionUID
		 * @fieldType: long
		 * @Description: TODO
		 */
		private static final long serialVersionUID = 1L;
		public String Photo;// photo 照片
	}

	public static HairdresserDetailResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			HairdresserDetailResponse response = gson.fromJson(json, HairdresserDetailResponse.class);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<HairdresserDetailResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<HairdresserDetailResponse> lists = new ArrayList<HairdresserDetailResponse>();
			lists = gson.fromJson(json, new TypeToken<List<HairdresserDetailResponse>>() {
			}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
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

	public String getLevelName() {
		return LevelName;
	}

	public void setLevelName(String levelName) {
		LevelName = levelName;
	}

	public String getStar() {
		return Star;
	}

	public void setStar(String star) {
		Star = star;
	}

	public String getScore() {
		return Score;
	}

	public void setScore(String score) {
		Score = score;
	}

	public String getOrderNum() {
		return OrderNum;
	}

	public void setOrderNum(String orderNum) {
		OrderNum = orderNum;
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

	public List<Images> getImages() {
		return Images;
	}

	public void setImages(List<Images> images) {
		Images = images;
	}

	public Intro getIntro() {
		return Intro;
	}

	public void setIntro(Intro intro) {
		Intro = intro;
	}

	public String getWorkingLife() {
		return WorkingLife;
	}

	public void setWorkingLife(String workingLife) {
		WorkingLife = workingLife;
	}

	// add by jiazhaohui
	public String getNationality() {
		return Nationality;
	}

	public void setNationality(String nationality) {
		Nationality = nationality;
	}

	public String getLanguage() {
		return Language;
	}

	public void setLanguage(String language) {
		Language = language;
	}

	public List<HairStyleListResponse> getHairStyle() {
		return HairStyle;
	}

	public void setHairStyle(List<HairStyleListResponse> hairStyle) {
		HairStyle = hairStyle;
	}

	public String getPrice() {
		return Price;
	}

	public void setPrice(String price) {
		Price = price;
	}

	@Override
	public String toString() {
		return "HairdresserDetailResponse [UserID=" + UserID + ", Name=" + Name + ", Photo=" + Photo + ", LevelName="
				+ LevelName + ", WorkingLife=" + WorkingLife + ", Star=" + Star + ", Score=" + Score + ", OrderNum="
				+ OrderNum + ", IsFavorite=" + IsFavorite + ", Price=" + Price + ", CommentNum=" + CommentNum
				+ ", Comment=" + Comment + ", Images=" + Images + ", Intro=" + Intro + ", HairStyle=" + HairStyle
				+ ", Nationality=" + Nationality + "， Language=" + Language + "]";
	}

}
