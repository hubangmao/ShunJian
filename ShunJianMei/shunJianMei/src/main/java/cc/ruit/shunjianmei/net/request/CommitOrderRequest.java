package cc.ruit.shunjianmei.net.request;

import java.io.Serializable;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * 
 * @ClassName: CommitOrderRequest
 * @Description: 提交订单
 * @author: Johnny
 * @date: 2015年10月24日 下午4:57:16
 */
public class CommitOrderRequest extends BaseRequest implements Serializable {
	private String UserID;// 用户ID
	private String ID;// 发型师ID
	private String HairStyleID;// 发型ID(我的发型表的主键)
	private String StyleID;// 发型项目ID
	private String Times;// 总时长
	private String WHID;// 发型师工时ID
	private String StoreID;// 店铺ID
	private String CouponID;// 优惠券ID
	private String Total;// 总金额
	private String Amount;// 需支付金额
	private String type;// 套餐类型 0普通，1套餐
	private List<Item> Item;

	public static class Item {

		public Item() {
			super();
			// TODO Auto-generated constructor stub
		}

		public Item(String code, String times, String price, String materialID) {
			super();
			Code = code;
			Times = times;
			Price = price;
			MaterialID = materialID;
		}

		public String Code;// 服务编码
		public String Times;// 时长
		public String Price;// 服务价格
		public String MaterialID;// 物料ID

	}

	/**
	 * @param UserID
	 * @param ID
	 * @param HairStyleID
	 * @param HairStyleID
	 * @param StyleID
	 * @param Times
	 * @param WHID
	 * @param StoreID
	 * @param CouponID
	 * @param Total
	 * @param Amount
	 * @param Item
	 * @Title:ActivityListRequest
	 * @Description:活动
	 * @param UserId
	 */
	public CommitOrderRequest(String UserID, String ID, String HairStyleID, String StyleID, String Times,
			String CouponID, String Total, String Amount, String WHID, String StoreID, String Type,
			List<cc.ruit.shunjianmei.net.request.CommitOrderRequest.Item> Item) {
		super("CommitOrder", "1.0");
		this.UserID = UserID;
		this.ID = ID;
		this.HairStyleID = HairStyleID;
		this.StyleID = StyleID;
		this.Times = Times;
		this.WHID = WHID;
		this.StoreID = StoreID;
		this.CouponID = CouponID;
		this.Total = Total;
		this.Amount = Amount;
		this.type = Type;
		this.Item = Item;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, "CommitOrder"));
	}

	/**
	 * @Title: toJsonString
	 * @Description: 把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(CommitOrderRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "CommitOrderRequest [UserID=" + UserID + ", ID=" + ID + ", HairStyleID=" + HairStyleID + ", StyleID="
				+ StyleID + ", Times=" + Times + ", WHID=" + WHID + ", StoreID=" + StoreID + ", CouponID=" + CouponID
				+ ", Total=" + Total + ", Amount=" + Amount + ", Item=" + Item + ", Method=" + Method + ", Infversion="
				+ Infversion + ", Key=" + Key + ", UID=" + UID + ",type=" + type + "]";
	}

}
