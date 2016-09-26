package cc.ruit.shunjianmei.net.api;

import java.io.UnsupportedEncodingException;

import cc.ruit.shunjianmei.constants.Constant;
import cc.ruit.shunjianmei.net.request.AlipayInfoRequest;
import cc.ruit.shunjianmei.net.request.ExchangeRequest;
import cc.ruit.shunjianmei.net.request.PresentDetailsRequest;
import cc.ruit.shunjianmei.net.request.WithdrawalsInfoRequest;
import cc.ruit.shunjianmei.net.request.WithdrawalsRequest;
import cc.ruit.utils.sdk.http.HttpHelper;

import com.lidroid.xutils.http.callback.RequestCallBack;
/**
 * @ClassName: WithdrawApi
 * @Description: 提现相关接口
 * @author: lee
 * @date: 2015年9月5日 下午5:35:27
 */
public class WithdrawApi {
	/**
	 * @Title: WithdrawalsInfo
	 * @Description: 提现首页信息
	 * @author: lee
	 * @param request
	 * @param callBack
	 * @return: void
	 */
	public static void WithdrawalsInfo(WithdrawalsInfoRequest request,
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
	 * @Title: AlipayInfo
	 * @Description: 支付宝提现首页信息
	 * @author: lee
	 * @param request
	 * @param callBack
	 * @return: void
	 */
	public static void AlipayInfo(AlipayInfoRequest request,
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
	 * @Title: Withdrawals
	 * @Description: 支付宝提现首页信息
	 * @author: lee
	 * @param request
	 * @param callBack
	 * @return: void
	 */
	public static void Withdrawals(WithdrawalsRequest request,
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
	 * @Title: Exchange
	 * @Description: 流量兑换
	 * @author: lee
	 * @param request
	 * @param callBack
	 * @return: void
	 */
	public static void Exchange(ExchangeRequest request,
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
	 * @Title: PresentDetails
	 * @Description: 提现明细
	 * @author: lee
	 * @param request
	 * @param callBack
	 * @return: void
	 */
	public static void PresentDetails(PresentDetailsRequest request,
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
