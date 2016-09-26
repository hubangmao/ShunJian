package cc.ruit.shunjianmei.net.api;

import java.io.UnsupportedEncodingException;

import cc.ruit.shunjianmei.constants.Constant;
import cc.ruit.shunjianmei.net.request.HomePageRequest;
import cc.ruit.utils.sdk.http.HttpHelper;

import com.lidroid.xutils.http.callback.RequestCallBack;
/**
 * 
 * @ClassName: HomePageApi
 * @Description: 首页请求
 * @author: 欧阳
 * @date: 2015年10月20日 上午8:00:41
 */
public class HomePageApi {
	/**
	 * @Title: DailyAttendance
	 * @Description: 签到
	 * @author: lee
	 * @param request
	 * @param callBack
	 * @return: void
	 */
	public static void HomePage(HomePageRequest request,
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
