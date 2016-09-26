package cc.ruit.shunjianmei.net.api;

import java.io.UnsupportedEncodingException;

import com.lidroid.xutils.http.callback.RequestCallBack;

import cc.ruit.shunjianmei.constants.Constant;
import cc.ruit.shunjianmei.net.request.GetAllusersubinfoPackedBysubidRequest;
import cc.ruit.utils.sdk.http.HttpHelper;

/**
 * @ClassName: CanUseCouponsApi
 * @Description: 可用优惠券
 * @author: Johnny
 * @date: 2015年9月5日 下午5:35:27
 */
public class GetAllusersubinfoPackedBysubid {
	/**
	 * @Title: canUseCoupons
	 * @Description: 可用优惠券
	 * @author: lee
	 * @param request
	 * @param callBack
	 * @return: void
	 */
	public static void canGetAllusersubinfoPackedBysubid(GetAllusersubinfoPackedBysubidRequest request,
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
