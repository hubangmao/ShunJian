package cc.ruit.shunjianmei.net.api;

import java.io.UnsupportedEncodingException;

import cc.ruit.shunjianmei.constants.Constant;
import cc.ruit.shunjianmei.net.request.OrderListRequest;
import cc.ruit.utils.sdk.http.HttpHelper;

import com.lidroid.xutils.http.callback.RequestCallBack;
/**
 * 
 * @ClassName: OrderListApi
 * @Description: 订单列表接口
 * @author: 欧阳
 * @date: 2015年10月26日 下午2:34:47
 */
public class OrderListApi {
	/**
	 * 
	 * @Title: OrderList
	 * @Description:订单列表
	 * @author: 欧阳
	 * @param request
	 * @param callBack
	 * @return: void
	 */
	public static void OrderList(OrderListRequest request,
			RequestCallBack<String> callBack) {
		try {
			String json = request.toJsonString(request);
			HttpHelper.postJSON(Constant.HOSTURL, json, callBack);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
