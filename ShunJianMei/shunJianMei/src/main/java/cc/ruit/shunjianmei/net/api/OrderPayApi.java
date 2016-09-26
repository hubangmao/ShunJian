package cc.ruit.shunjianmei.net.api;

import java.io.UnsupportedEncodingException;

import cc.ruit.shunjianmei.constants.Constant;
import cc.ruit.shunjianmei.net.request.OrderPayRequest;
import cc.ruit.shunjianmei.net.request.WechatPayRequest;
import cc.ruit.utils.sdk.http.HttpHelper;

import com.lidroid.xutils.http.callback.RequestCallBack;
/**
 * @ClassName: OrderPayApi
 * @Description: 支付订单接口
 * @author: Johnny
 * @date: 2015年9月5日 下午5:35:27
 */
public class OrderPayApi {
	/**
	 * 
	 * @Title: orderPay
	 * @Description: 支付订单
	 * @author: Johnny
	 * @param request
	 * @param callBack
	 * @return: void
	 */
	public static void orderPay(OrderPayRequest request,
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
	/**
	 * 
	 * @Title: orderPay
	 * @Description: 支付订单
	 * @author: Johnny
	 * @param request
	 * @param callBack
	 * @return: void
	 */
	public static void WechatPay(WechatPayRequest request,
			RequestCallBack<String> callBack) {
		try {
			String json = request.toJsonString(request);
			HttpHelper.postJSON(Constant.HOSTURL, json, callBack);
//			HttpHelper.postJSON("http://mxzxservice.oruit.net/MingXingZaiXian-http/pay/unifiedorder", json, callBack);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
