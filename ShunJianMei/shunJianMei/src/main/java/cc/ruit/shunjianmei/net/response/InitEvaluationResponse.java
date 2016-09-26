package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * @ClassName: InitEvaluationResponse
 * @Description: 评论接口
 * @author: 欧阳
 * @date: 2015年10月29日 下午3:16:32
 */
public class InitEvaluationResponse implements Serializable {
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 1L;
	public List<User> User;// 用户
	public List<Store> Store;// 用户
	/**
	 * 
	 * @ClassName: User
	 * @Description: 用户评论
	 * @author: 欧阳
	 * @date: 2015年10月29日 下午3:25:37
	 */
	public static class User {
		public String UserID;// 发型师ID
		public String Name;// 顾客名字
		public String LevelName;// 发型师的水平
		public String Photo;// 顾客头像
		public String Star;// 星座
		public String Score;// 订单评分
		public String OrderNum;// 订单数量
	}
	/**
	 * 
	 * @ClassName: Store
	 * @Description: 店铺ID
	 * @author: 欧阳
	 * @date: 2015年10月29日 下午3:25:48
	 */
	public static class Store {
		public String ID;// 订单ID
		public String Name;// 商家名字
		public String Photo;// 商家头像
		public String Score;// 商家综合评分
		public String OrderNum;// 商家订单数量
		public String CarNum;// 车位数
		public String Address;// 商家地址
	}

	/**
	 * 
	 * @Title: getclazz
	 * @Description: 解析json数据
	 * @author: Johnny
	 * @param json
	 * @return
	 * @return: List<TodayOrderListResponce>
	 */
	public static List<InitEvaluationResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<InitEvaluationResponse> lists = new ArrayList<InitEvaluationResponse>();
			lists = gson.fromJson(json,
					new TypeToken<List<InitEvaluationResponse>>() {
					}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static InitEvaluationResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			InitEvaluationResponse lists = gson.fromJson(json,
					InitEvaluationResponse.class);
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
