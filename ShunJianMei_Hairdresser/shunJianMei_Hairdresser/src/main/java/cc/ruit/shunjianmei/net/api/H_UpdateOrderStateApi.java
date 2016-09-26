package cc.ruit.shunjianmei.net.api;

import java.io.UnsupportedEncodingException;

import cc.ruit.shunjianmei.constants.Constant;
import cc.ruit.shunjianmei.net.request.H_UpdateOrderStateRequest;
import cc.ruit.utils.sdk.http.HttpHelper;

import com.lidroid.xutils.http.callback.RequestCallBack;
/**
 * 
 * @ClassName: H_UpdateOrderStateApi
 * @Description: 更新订单状态
 * @author: 欧阳
 * @date: 2015年10月21日 下午9:55:02
 */
public class H_UpdateOrderStateApi {
	/**
	 * 
	 * @ClassName: H_UpdateOrderStateApi
	 * @Description: 更新订单状态
	 * @author: 欧阳
	 * @date: 2015年10月21日 下午9:55:02
	 */
	public static void H_UpdateOrderState(H_UpdateOrderStateRequest request,
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
