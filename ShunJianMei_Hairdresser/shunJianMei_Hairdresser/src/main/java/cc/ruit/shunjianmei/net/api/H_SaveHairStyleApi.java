package cc.ruit.shunjianmei.net.api;

import java.io.UnsupportedEncodingException;

import cc.ruit.shunjianmei.constants.Constant;
import cc.ruit.shunjianmei.net.request.H_SaveHairStyleRequest;
import cc.ruit.utils.sdk.http.HttpHelper;

import com.lidroid.xutils.http.callback.RequestCallBack;
/**
 * 
 * @ClassName: H_SaveHairStyleApi
 * @Description: 上传发型
 * @author: 欧阳
 * @date: 2015年10月17日 下午3:51:18
 */
public class H_SaveHairStyleApi {
	/**
	 * 
	 * @Title: HairStyleList
	 * @Description: 发型上传
	 * @author: 欧阳
	 * @param request
	 * @param callBack
	 * @return: void
	 */
	public static void HairStyleList(H_SaveHairStyleRequest request,
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
