package cc.ruit.shunjianmei.net.api;

import java.io.UnsupportedEncodingException;

import cc.ruit.shunjianmei.constants.Constant;
import cc.ruit.shunjianmei.net.request.GetCustomerServicePhoneRequest;
import cc.ruit.utils.sdk.http.HttpHelper;

import com.lidroid.xutils.http.callback.RequestCallBack;
/**
 * 
 * @ClassName: CustomerServicePhoneApi
 * @Description: 客服电话接口
 * @author: 欧阳
 * @date: 2015年10月29日 下午2:57:11
 */
public class CustomerServicePhoneApi {
	
	public static void CustomerServicePhone(GetCustomerServicePhoneRequest request,
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
