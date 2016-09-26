package cc.ruit.shunjianmei.net.api;

import java.io.UnsupportedEncodingException;

import com.lidroid.xutils.http.callback.RequestCallBack;

import cc.ruit.shunjianmei.constants.Constant;
import cc.ruit.shunjianmei.net.request.H_SetInformationRequest;
import cc.ruit.utils.sdk.http.HttpHelper;

/**
 * 个人信息上传
 */
public class H_SetInformationApi {
	public static void SetInformation(H_SetInformationRequest request, RequestCallBack<String> callBack) {
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
