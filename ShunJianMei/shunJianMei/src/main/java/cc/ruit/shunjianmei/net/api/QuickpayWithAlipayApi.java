package cc.ruit.shunjianmei.net.api;

import java.io.UnsupportedEncodingException;

import cc.ruit.shunjianmei.constants.Constant;
import cc.ruit.shunjianmei.net.request.QuickpayWithAlipayRequest;
import cc.ruit.utils.sdk.http.HttpHelper;

import com.lidroid.xutils.http.callback.RequestCallBack;
/**
 * @ClassName: QuickpayWithAlipayApi
 * @Description: 支付宝的闪惠支付接口
 * @author: jiazhaohui
 */
public class QuickpayWithAlipayApi {
	/**
	 * @Title: QuickpayWithAlipayApi
	 * @Description: 支付宝的闪惠支付
	 * @author: jiazhaohui
	 * @param request
	 * @param callBack
	 * @return: void
	 */
	public static void QuickpayWithAlipay(QuickpayWithAlipayRequest request,
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
