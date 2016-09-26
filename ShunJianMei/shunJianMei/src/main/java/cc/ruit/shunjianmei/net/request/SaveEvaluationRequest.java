package cc.ruit.shunjianmei.net.request;

import java.util.List;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * 
 * @ClassName: SaveEvaluationRequest
 * @Description: 保存评论
 * @author: 欧阳
 * @date: 2015年10月27日 下午1:10:13
 */
public class SaveEvaluationRequest extends BaseRequest {
	// 用户id
	private String UserID;
	private String OrderID;
	private String Type;
	private String Content;
	private List<Item> Item;
	private String Image;

	/**
	 * @Title:ActivityListRequest
	 * @Description:活动
	 * @param UserId
	 */
	public SaveEvaluationRequest(String UserID, String OrderID, String Type,
			String Content,List<Item> Item, String Image) {
		super("SaveEvaluation", "1.0");
		this.UserID = UserID;
		this.Type = Type;
		this.OrderID = OrderID;
		this.Type = Type;
		this.Content = Content;
		this.Item = Item;
		this.Image = Image;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "SaveEvaluation"));
	}

	/**
	 * @Title: toJsonString
	 * @Description: 把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(SaveEvaluationRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	public static class Item {
		private String Code;
		private String Score;

		public String getCode() {
			return Code;
		}

		public void setCode(String code) {
			Code = code;
		}

		public String getScore() {
			return Score;
		}

		public void setScore(String score) {
			Score = score;
		}

		@Override
		public String toString() {
			return "Item [Code=" + Code + ", Score=" + Score + "]";
		}

	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public String getOrderID() {
		return OrderID;
	}

	public void setOrderID(String orderID) {
		OrderID = orderID;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public List<Item> getItem() {
		return Item;
	}

	public void setItem(List<Item> item) {
		Item = item;
	}

	public String getImage() {
		return Image;
	}

	public void setImage(String image) {
		Image = image;
	}

	@Override
	public String toString() {
		return "SaveEvaluationRequest [UserID=" + UserID + ", OrderID="
				+ OrderID + ", Type=" + Type + ", Content=" + Content
				+ ", Item=" + Item + ", Image=" + Image + "]";
	}

}
