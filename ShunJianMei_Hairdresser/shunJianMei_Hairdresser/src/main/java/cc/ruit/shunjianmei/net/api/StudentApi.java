package cc.ruit.shunjianmei.net.api;

import java.io.UnsupportedEncodingException;

import cc.ruit.shunjianmei.constants.Constant;
import cc.ruit.shunjianmei.net.request.ApprenticeIndexInfoRequest;
import cc.ruit.shunjianmei.net.request.RewardListRequest;
import cc.ruit.utils.sdk.http.HttpHelper;

import com.lidroid.xutils.http.callback.RequestCallBack;
/**
 * @ClassName: WorkApi
 * @Description: 任务相关接口
 * @author: lee
 * @date: 2015年9月5日 下午5:35:27
 */
public class StudentApi {
	/**
	 * @Title: DailyAttendance
	 * @Description: 签到
	 * @author: lee
	 * @param request
	 * @param callBack
	 * @return: void
	 */
	public static void ApprenticeIndexInfo(ApprenticeIndexInfoRequest request,
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
	 * @Title: RewardList
	 * @Description: 徒弟徒孙列表
	 * @author: lee
	 * @param request
	 * @param callBack
	 * @return: void
	 */
	public static void RewardList(RewardListRequest request,
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
