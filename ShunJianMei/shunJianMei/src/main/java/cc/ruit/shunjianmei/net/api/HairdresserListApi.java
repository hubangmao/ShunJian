package cc.ruit.shunjianmei.net.api;

import java.io.UnsupportedEncodingException;

import cc.ruit.shunjianmei.constants.Constant;
import cc.ruit.shunjianmei.net.request.HairdresserListRequest;
import cc.ruit.utils.sdk.http.HttpHelper;

import com.lidroid.xutils.http.callback.RequestCallBack;
/**
 * 
 * @ClassName: BusinessAreaApi
 * @Description: 商圈信息
 * @author: 欧阳
 * @date: 2015年10月21日 上午10:49:13
 */
public class HairdresserListApi {
	/**
	 * 
	 * @Title: BusinessArea
	 * @Description: 区域的信息
	 * @author: 欧阳
	 * @param request
	 * @param callBack
	 * @return: void
	 */
	public static void HairdresserList(HairdresserListRequest request,
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
