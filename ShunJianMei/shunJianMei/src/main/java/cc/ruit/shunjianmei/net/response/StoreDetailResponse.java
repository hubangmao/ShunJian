package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @ClassName: StoreDetailResponse
 * @Description: 商店详情出参
 * @author: Johnny
 * @date: 2015年10月14日 下午4:51:49
 */
public class StoreDetailResponse implements Serializable {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description:
	 */
	private static final long serialVersionUID = 1L;
	private String ID;// 发型店ID
	private String Name;// 发型店名称
	private String Address;// 地址
	private String BusinessHours;// 营业时间
	private String Score;// 评分
	private String Distance;// 离我距离
	private String OrderNum;// 接单数量
	private String CarNum;// 车位数
	private String Longitude;// 经度
	private String Latitude;// 纬度
	private String IsFavorite;// 是否收藏 0否1是
	private String Tel;// 电话
	private String Intro;// 简介
	private List<Images> Images;// Photo
	private List<Schedule> Schedule;// 发型师
	private String CommentNum;// 评论数
	private Comment Comment;// 发型师
	// add by jiazhaohui
	private String Quickpay;
	private String Perferiential;
	private String istype;

	public String getIstype() {
		return istype;
	}

	public void setIstype(String istype) {
		this.istype = istype;
	}

	public String getLongitude() {
		return Longitude;
	}

	public void setLongitude(String longitude) {
		Longitude = longitude;
	}

	public String getLatitude() {
		return Latitude;
	}

	public void setLatitude(String latitude) {
		Latitude = latitude;
	}

	public String getID() {
		return ID;
	}

	public String getIsFavorite() {
		return IsFavorite;
	}

	public void setIsFavorite(String isFavorite) {
		IsFavorite = isFavorite;
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

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getBusinessHours() {
		return BusinessHours;
	}

	public void setBusinessHours(String businessHours) {
		BusinessHours = businessHours;
	}

	public String getDistance() {
		return Distance;
	}

	public void setDistance(String distance) {
		Distance = distance;
	}

	public String getOrderNum() {
		return OrderNum;
	}

	public void setOrderNum(String orderNum) {
		OrderNum = orderNum;
	}

	public String getCarNum() {
		return CarNum;
	}

	public void setCarNum(String carNum) {
		CarNum = carNum;
	}

	public String getTel() {
		return Tel;
	}

	public void setTel(String tel) {
		Tel = tel;
	}

	public String getScore() {
		return Score;
	}

	public void setScore(String score) {
		Score = score;
	}

	public String getIntro() {
		return Intro;
	}

	public void setIntro(String intro) {
		Intro = intro;
	}

	public List<Images> getImages() {
		return Images;
	}

	public void setImages(List<Images> images) {
		Images = images;
	}

	public List<Schedule> getSchedule() {
		return Schedule;
	}

	public void setSchedule(List<Schedule> schedule) {
		Schedule = schedule;
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

	public String getQuickpay() {
		return Quickpay;
	}

	public void setQuickpay(String quickPay) {
		Quickpay = quickPay;
	}

	public String getPerferiential() {
		return Perferiential;
	}

	public void setPerferiential(String perferiential) {
		Perferiential = perferiential;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static class Images {
		public String Photo;// 评论配图
	}

	public static class Schedule {
		public String Date;// 日期
		public String WeekDay;// 星期
		public List<Item> Item;// 日期

		public static class Item {
			public String ID; // 发型师ID
			public String Name;// 发型师名字
			public String Photo;// 头像
			public String LevelName;// 职称
			public String Star;// 星座
			public String Score;// 评分
			public String OrderNum;// 接单量
		}
	}

	public static class Comment {
		public String ID;// 评论ID
		public String Name;// 评论者
		public String Photo;// 头像
		public String Time;// 评论时间
		public String Content;// 评论内容
		public String Score;// 评分
		public List<Item> Item;

		public static class Item {
			public String Photo;// 评论图片
		}
	}

	@Override
	public String toString() {
		return "StoreDetailResponse [ID=" + ID + ", Name=" + Name + ", Address=" + Address + ", BusinessHours="
				+ BusinessHours + ", Score=" + Score + ", Distance=" + Distance + ", OrderNum=" + OrderNum + ", CarNum="
				+ CarNum + ", Longitude=" + Longitude + ", Latitude=" + Latitude + ", IsFavorite=" + IsFavorite
				+ ", Perferiential=" + Perferiential + ", Quickpay" + Quickpay + ", Tel=" + Tel + ", Intro=" + Intro
				+ ", Images=" + Images + ", Schedule=" + Schedule + ", CommentNum=" + CommentNum + ", Comment="
				+ Comment + "]";
	}

	public static StoreDetailResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			StoreDetailResponse lists = gson.fromJson(json, StoreDetailResponse.class);
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<StoreDetailResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<StoreDetailResponse> lists = new ArrayList<StoreDetailResponse>();
			lists = gson.fromJson(json, new TypeToken<List<StoreDetailResponse>>() {
			}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
