package cc.ruit.shunjianmei.net.api;

import java.io.UnsupportedEncodingException;

import com.lidroid.xutils.http.callback.RequestCallBack;

import cc.ruit.shunjianmei.constants.Constant;
import cc.ruit.shunjianmei.net.request.H_GetInformationRequest;
import cc.ruit.utils.sdk.http.HttpHelper;

public class H_GetInformationApi {
	public static void GetInformation(H_GetInformationRequest request,
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
