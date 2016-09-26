package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @ClassName: ReservationServiceResponse
 * @Description: 预约出参
 * @author: Johnny
 * @date: 2015年10月14日 下午4:51:49
 */
public class ReservationServiceResponse implements Serializable {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description:
	 */
	private static final long serialVersionUID = 1L;

	private String ID;// 发型师ID
	private String Name;// 发型师名字
	private String Photo;// 头像
	private String LevelName;// 等级
	private String Star;// 星座
	private String Score;// 评分
	private String OrderNum;// 接单数量
	private List<Schedule> Schedule;// 日程
	/**
	 * @ClassName: Schedule
	 * @Description: 日程
	 * @author: lee
	 * @date: 2015年11月8日 下午1:48:15
	 */
	public static class Schedule {
		public String Date;// 日期
		public String WeekDay;// 星期
		public List<Item> Item;

		public static class Item {
			public String WHID;// 工时ID
			public String Hour;// 时间
			public String State; // 状态 0不可约1可约
		}
	}

	private List<HairStyle> HairStyle;// 发型项目
	/**
	 * @ClassName: HairStyle
	 * @Description: 发型
	 * @author: lee
	 * @date: 2015年11月8日 下午1:47:37
	 */
	public static class HairStyle {
		public String ID;// 项目ID
		public String Name;// 项目名称
		public String Times;// 时长（计算总的）
		public boolean isChecked = false;//是否点选的标记
		public List<Item> Item;

		public static class Item {
			public String Code;// 服务编码
			public String Price;// 价格
			public String Name;// 服务名称
			public String Times;// 时长(单项服务)
		}
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

	public List<Schedule> getSchedule() {
		return Schedule;
	}

	public void setSchedule(List<Schedule> schedule) {
		Schedule = schedule;
	}

	public List<HairStyle> getHairStyle() {
		return HairStyle;
	}

	public void setHairStyle(List<HairStyle> hairStyle) {
		HairStyle = hairStyle;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static ReservationServiceResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			ReservationServiceResponse lists = gson.fromJson(json,
					ReservationServiceResponse.class);
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<ReservationServiceResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<ReservationServiceResponse> lists = new ArrayList<ReservationServiceResponse>();
			lists = gson.fromJson(json,
					new TypeToken<List<ReservationServiceResponse>>() {
					}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
