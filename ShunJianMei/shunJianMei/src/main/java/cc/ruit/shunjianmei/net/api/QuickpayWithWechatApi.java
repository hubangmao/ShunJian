package cc.ruit.shunjianmei.net.api;

import java.io.UnsupportedEncodingException;

import cc.ruit.shunjianmei.constants.Constant;
import cc.ruit.shunjianmei.net.request.QuickpayWithWechatRequest;
import cc.ruit.utils.sdk.http.HttpHelper;

import com.lidroid.xutils.http.callback.RequestCallBack;
/**
 * @ClassName: QuickpayWithWechatApi
 * @Description: 微信的闪惠支付接口
 * @author: jiazhaohui
 */
public class QuickpayWithWechatApi {
	/**
	 * @Title: QuickpayWithWechatApi
	 * @Description: 微信的闪惠支付
	 * @author: jiazhaohui
	 * @param request
	 * @param callBack
	 * @return: void
	 */
	public static void QuickpayWithWechat(QuickpayWithWechatRequest request,
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
