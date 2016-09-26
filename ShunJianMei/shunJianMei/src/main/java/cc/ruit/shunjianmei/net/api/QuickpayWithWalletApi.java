package cc.ruit.shunjianmei.net.api;

import java.io.UnsupportedEncodingException;

import cc.ruit.shunjianmei.constants.Constant;
import cc.ruit.shunjianmei.net.request.QuickpayWithWalletRequest;
import cc.ruit.utils.sdk.http.HttpHelper;

import com.lidroid.xutils.http.callback.RequestCallBack;
/**
 * @ClassName: QuickpayWithWalletApi
 * @Description: 钱包的闪惠支付接口
 * @author: jiazhaohui
 */
public class QuickpayWithWalletApi {
	/**
	 * @Title: QuickpayWithWalletApi
	 * @Description: 钱包的闪惠支付
	 * @author: Johnny
	 * @param request
	 * @param callBack
	 * @return: void
	 */
	public static void QuickpayWithWallet(QuickpayWithWalletRequest request,
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
