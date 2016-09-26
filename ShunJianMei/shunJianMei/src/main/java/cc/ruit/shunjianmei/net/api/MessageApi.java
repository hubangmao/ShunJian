package cc.ruit.shunjianmei.net.api;

import java.io.UnsupportedEncodingException;

import cc.ruit.shunjianmei.constants.Constant;
import cc.ruit.shunjianmei.net.request.MessageInfoRequest;
import cc.ruit.shunjianmei.net.request.MessageListRequest;
import cc.ruit.utils.sdk.http.HttpHelper;

import com.lidroid.xutils.http.callback.RequestCallBack;
/**
 * @ClassName: MessageApi
 * @Description: 消息
 * @author: lee
 * @date: 2015年9月5日 下午5:35:27
 */
public class MessageApi {
	/**
	 * @Title: DailyAttendance
	 * @Description: 签到
	 * @author: lee
	 * @param request
	 * @param callBack
	 * @return: void
	 */
	public static void MessageList(MessageListRequest request,
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
	 * @Title: DailyAttendance
	 * @Description: 签到
	 * @author: lee
	 * @param request
	 * @param callBack
	 * @return: void
	 */
	public static void MessageInfo(MessageInfoRequest request,
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
