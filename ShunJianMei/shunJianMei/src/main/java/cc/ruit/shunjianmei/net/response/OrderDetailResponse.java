package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * @ClassName: H_OrderDetailResponse
 * @Description: 订单详情接口
 * @author: 欧阳
 * @date: 2015年10月19日 上午11:13:33
 */
public class OrderDetailResponse implements Serializable {
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 1L;
	public String OrderID;// 订单ID
	public String OrderCode;// 订单编号
	public String State;// 状态1待支付2待确认3已预约4已签到5服务中6服务完成7评论完成8订单完成9已取消10异常处理
	public String StateName;// 状态名称
	public String NextStep;// 下一步
	public String AppointmentTime;// 预约时间
	public String ID;// 发型师ID
	public String StoreID;// 商家ID
	public String Name;// 发型师名字
	public String LevelName;// 发型师的水平
	public String Photo;// 顾客头像
	public String Mobile;// 发型师电话
	public String Score;// 综合评分
	public String OrderNum;// 订单数量
	public String StoreName;// 商家名字
	public String StorePhoto;// 商家头像
	public String StoreTel;// 商家电话
	public String StoreScore;// 商家综合评分
	public String StoreOrderNum;// 商家订单数量
	public String CarNum;// 车位数
	public String Address;// 商家地址
	public String StyleName;// 发型项目
	public String AdditionalContent;// 加单内容
	public String AdditionalCode;// 加单编号 当前日期时分秒毫秒
	public String AdditionalAmount;// 加单金额
	public String AdditionalState;// 0无加单1未支付2已支付
	public String Amount;// 金额
	public String Discount;// 优惠
	public Comment Comment;// 发型评论
	public Comment StoreComment;// 店铺评论
	public List<Item> Item = new ArrayList<OrderDetailResponse.Item>();// Name
	public String istype;																	// 服务名称

	/**
	 * 
	 * @Title: getclazz
	 * @Description: 解析json数据
	 * @author: Johnny
	 * @param json
	 * @return
	 * @return: List<TodayOrderListResponce>
	 */
	public static List<OrderDetailResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<OrderDetailResponse> lists = new ArrayList<OrderDetailResponse>();
			lists = gson.fromJson(json,
					new TypeToken<List<OrderDetailResponse>>() {
					}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static OrderDetailResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			OrderDetailResponse lists = gson.fromJson(json,
					OrderDetailResponse.class);
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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

	@Override
	public String toString() {
		return "OrderDetailResponse [OrderID=" + OrderID + ", OrderCode="
				+ OrderCode + ", State=" + State + ", StateName=" + StateName
				+ ", NextStep=" + NextStep + ", AppointmentTime="
				+ AppointmentTime + ", ID=" + ID + ", StoreID=" + StoreID
				+ ", Name=" + Name + ", LevelName=" + LevelName + ", Photo="
				+ Photo + ", Mobile=" + Mobile + ", Score=" + Score
				+ ", OrderNum=" + OrderNum + ", StoreName=" + StoreName
				+ ", StorePhoto=" + StorePhoto + ", StoreTel=" + StoreTel
				+ ", StoreScore=" + StoreScore + ", StoreOrderNum="
				+ StoreOrderNum + ", CarNum=" + CarNum + ", Address=" + Address
				+ ", StyleName=" + StyleName + ", AdditionalContent="
				+ AdditionalContent + ", AdditionalCode=" + AdditionalCode
				+ ", AdditionalAmount=" + AdditionalAmount
				+ ", Additionalstate=" + AdditionalState + ", Amount=" + Amount
				+ ", Discount=" + Discount + ", Comment=" + Comment
				+ ", StoreComment=" + StoreComment + ", Item=" + Item + "]";
	}

	public static class Comment implements Serializable {
		/**
		 * @fieldName: serialVersionUID
		 * @fieldType: long
		 * @Description: TODO
		 */
		private static final long serialVersionUID = 1L;
		public String ID;// ID 评论ID
		public String Name;// Name 评论者
		public String Photo;// Photo 头像
		public String Time;// 评论时间
		public String Content;// 评论内容
		public String Score;// 评论分数
		public List<Item> Item;//

		public static class Item implements Serializable {
			/**
			 * @fieldName: serialVersionUID
			 * @fieldType: long
			 * @Description: TODO
			 */
			private static final long serialVersionUID = 1L;
			public String Photo;// 照片
		}

		@Override
		public String toString() {
			return "Comment [ID=" + ID + ", Name=" + Name + ", Photo=" + Photo + ", Time=" + Time + ", Content="
					+ Content + ", Score=" + Score + ", Item=" + Item + "]";
		}
		
	}
}
