package cc.ruit.shunjianmei.net.api;

import java.io.UnsupportedEncodingException;

import cc.ruit.shunjianmei.constants.Constant;
import cc.ruit.shunjianmei.net.request.StoreListRequest;
import cc.ruit.utils.sdk.http.HttpHelper;

import com.lidroid.xutils.http.callback.RequestCallBack;
/**
 * @ClassName: ManagerApi
 * @Description: 管理相关接口
 * @author: gaoj
 * @date: 2015年10月14日 下午4:48:18
 */
public class ManagerApi {
	/**
	 * @Title: StoreList
	 * @Description: 商户列表
	 * @author: gaoj
	 * @param request
	 * @param callBack
	 * @return: void
	 */
	public static void StoreList(StoreListRequest request,
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
