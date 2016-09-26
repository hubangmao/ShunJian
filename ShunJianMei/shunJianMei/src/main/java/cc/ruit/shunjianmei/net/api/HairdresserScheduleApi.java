package cc.ruit.shunjianmei.net.api;

import java.io.UnsupportedEncodingException;

import cc.ruit.shunjianmei.constants.Constant;
import cc.ruit.shunjianmei.net.request.HairdresserScheduleRequest;
import cc.ruit.utils.sdk.http.HttpHelper;

import com.lidroid.xutils.http.callback.RequestCallBack;
/**
 * 
 * @ClassName: HairdresserScheduleApi
 * @Description: 发型师日程安排
 * @author: 欧阳
 * @date: 2015年10月26日 上午10:35:52
 */
public class HairdresserScheduleApi {
	
	public static void HairdresserSchedule(HairdresserScheduleRequest request,
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
