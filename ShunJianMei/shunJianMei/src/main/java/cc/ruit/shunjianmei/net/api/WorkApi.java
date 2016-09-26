package cc.ruit.shunjianmei.net.api;

import java.io.UnsupportedEncodingException;

import cc.ruit.shunjianmei.constants.Constant;
import cc.ruit.shunjianmei.net.request.ActivityListRequest;
import cc.ruit.shunjianmei.net.request.WorkDailyAttendanceRequest;
import cc.ruit.utils.sdk.http.HttpHelper;

import com.lidroid.xutils.http.callback.RequestCallBack;
/**
 * @ClassName: WorkApi
 * @Description: 任务相关接口
 * @author: lee
 * @date: 2015年9月5日 下午5:35:27
 */
public class WorkApi {
	/**
	 * @Title: DailyAttendance
	 * @Description: 签到
	 * @author: lee
	 * @param request
	 * @param callBack
	 * @return: void
	 */
	public static void DailyAttendance(WorkDailyAttendanceRequest request,
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
	 * @Title: ActivityList
	 * @Description: 签到
	 * @author: lee
	 * @param request
	 * @param callBack
	 * @return: void
	 */
	public static void ActivityList(ActivityListRequest request,
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
