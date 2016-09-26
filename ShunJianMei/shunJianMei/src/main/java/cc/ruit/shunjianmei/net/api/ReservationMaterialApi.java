package cc.ruit.shunjianmei.net.api;

import java.io.UnsupportedEncodingException;

import cc.ruit.shunjianmei.constants.Constant;
import cc.ruit.shunjianmei.net.request.ReservationMaterialAgainRequest;
import cc.ruit.shunjianmei.net.request.ReservationMaterialRequest;
import cc.ruit.utils.sdk.http.HttpHelper;

import com.lidroid.xutils.http.callback.RequestCallBack;
/**
 * @ClassName: ReservationMaterialApi
 * @Description: 用户预约服务
 * @author: Johnny
 * @date: 2015年9月5日 下午5:35:27
 */
public class ReservationMaterialApi {
	/**
	 * @Title: ReservationMaterial
	 * @Description: 用户预约服务
	 * @author: Johnny
	 * @param request
	 * @param callBack
	 * @return: void
	 */
	public static void reservationMaterial(ReservationMaterialRequest request,
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
	 * @Title: ReservationMaterial
	 * @Description: 用户预约服务
	 * @author: Johnny
	 * @param request
	 * @param callBack
	 * @return: void
	 */
	public static void ReservationMaterialAgain(ReservationMaterialAgainRequest request,
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
