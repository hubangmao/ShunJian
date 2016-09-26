package cc.ruit.shunjianmei.net.api;

import java.io.UnsupportedEncodingException;

import cc.ruit.shunjianmei.constants.Constant;
import cc.ruit.shunjianmei.net.request.CheckVersionRequest;
import cc.ruit.shunjianmei.net.request.FeedbackRequest;
import cc.ruit.shunjianmei.net.request.WorkDailyAttendanceRequest;
import cc.ruit.utils.sdk.http.HttpHelper;

import com.lidroid.xutils.http.callback.RequestCallBack;
/**
 * @ClassName: SettingApi
 * @Description: 设置相关接口
 * @author: lee
 * @date: 2015年9月5日 下午5:35:27
 */
public class SettingApi {
	/**
	 * @Title: Feedback
	 * @Description: 意见反馈
	 * @author: lee
	 * @param request
	 * @param callBack
	 * @return: void
	 */
	public static void Feedback(FeedbackRequest request,
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
	 * @Title: CheckVersion
	 * @Description: 意见反馈
	 * @author: lee
	 * @param request
	 * @param callBack
	 * @return: void
	 */
	public static void CheckVersion(CheckVersionRequest request,
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
