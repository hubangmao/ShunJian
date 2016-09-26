package cc.ruit.shunjianmei.net.api;

import java.io.UnsupportedEncodingException;

import cc.ruit.shunjianmei.constants.Constant;
import cc.ruit.shunjianmei.net.request.AdditionalOrderRequest;
import cc.ruit.utils.sdk.http.HttpHelper;

import com.lidroid.xutils.http.callback.RequestCallBack;
/**
 * 
 * @ClassName: AdditionalOrderApi
 * @Description: 加单接口
 * @author: 欧阳
 * @date: 2015年10月22日 下午2:34:23
 */
public class AdditionalOrderApi {
	/**
	 * 
	 * @Title: AdditionalOrder
	 * @Description: 加单
	 * @author: 欧阳
	 * @param request
	 * @param callBack
	 * @return: void
	 */
	public static void AdditionalOrder(AdditionalOrderRequest request,
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
