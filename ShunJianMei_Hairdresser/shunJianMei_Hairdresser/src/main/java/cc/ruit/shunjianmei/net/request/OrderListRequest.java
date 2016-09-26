package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;
import cc.ruit.shunjianmei.constants.Constant;
import cc.ruit.shunjianmei.home.order.OrderFragment;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * @ClassName: OrderListRequest
 * @Description: 订单列表
 * @author: lee
 * @date: 2015年8月31日 下午11:00:48
 */
public class OrderListRequest extends BaseRequest {
	private String UserID;// 用户ID
	private String State;// 状态 0全部 1已预约
	private String Sort;// 排序 1由近到远 2由远到近
	private String PageIndex;// 开始编号
	private String PageSize;// 开始编号
	public OrderListRequest(String userId, String state, String sort, String pageSize, String pageIndex) {
		super(OrderFragment.requestMethod, "1.0");
		UserID = userId;
		State = state;
		Sort = sort;
		PageIndex = pageIndex;
		PageSize = pageSize;
		String uid = System.currentTimeMillis() + "";
		setUid(uid, OruitKey.encrypt(uid, OrderFragment.requestMethod));
	}

	/**
	 * @Title: toJsonString
	 * @Description: 把对象转成json格式的字符串
	 * @author: lee
	 * @param obj
	 * @return: String
	 */
	public String toJsonString(OrderListRequest obj) {
		GsonBuilder gson = new GsonBuilder();
		gson.disableHtmlEscaping();
		String json = gson.create().toJson(obj);
		LogUtils.i("cord==" + json);
		return json;
	}

	@Override
	public String toString() {
		return "MessageListRequest [UserID=" + UserID + ", State=" + State + ", Sort=" + Sort + ", PageStart="
				+ PageIndex + ", PageSize=" + PageSize + ", Method=" + Method + ", Infversion=" + Infversion + ", Key="
				+ Key + ", UID=" + UID + "]";
	}

}
