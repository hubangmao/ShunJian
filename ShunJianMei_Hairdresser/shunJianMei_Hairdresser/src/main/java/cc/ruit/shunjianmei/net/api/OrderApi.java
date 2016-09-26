package cc.ruit.shunjianmei.net.api;

import java.io.UnsupportedEncodingException;

import cc.ruit.shunjianmei.constants.Constant;
import cc.ruit.shunjianmei.net.request.BusinessAreaStoreListRequest;
import cc.ruit.shunjianmei.net.request.H_DeleteSelfWorkingRequest;
import cc.ruit.shunjianmei.net.request.H_GetDayWorkingRequest;
import cc.ruit.shunjianmei.net.request.H_GetMasterDefaultSettingRequest;
import cc.ruit.shunjianmei.net.request.H_GetSelfWorkingRequest;
import cc.ruit.shunjianmei.net.request.H_QuickSettingRequest;
import cc.ruit.shunjianmei.net.request.H_SaveSelfWorkingRequest;
import cc.ruit.shunjianmei.net.request.H_SetMasterDefaultSettingRequest;
import cc.ruit.shunjianmei.net.request.HairdresserScheduleRequest;
import cc.ruit.shunjianmei.net.request.MessageInfoRequest;
import cc.ruit.shunjianmei.net.request.OrderListRequest;
import cc.ruit.shunjianmei.net.request.SaveHairdresserScheduleRequest;
import cc.ruit.shunjianmei.net.request.ScheduleOrderListRequest;
import cc.ruit.utils.sdk.http.HttpHelper;

import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * @ClassName: MessageApi
 * @Description: 消息
 * @author: lee
 * @date: 2015年9月5日 下午5:35:27
 */
public class OrderApi {
	/**
	 * @Title: DailyAttendance
	 * @Description: 签到
	 * @author: lee
	 * @param request
	 * @param callBack
	 * @return: void
	 */
	public static void OrderList(OrderListRequest request,
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
	public static void ScheduleOrderList(ScheduleOrderListRequest request,
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

	/**
	 * 获取指定日程信息
	 * 
	 * @param request
	 * @param requestCallBack
	 */
	public static void hairdresserSchedule(HairdresserScheduleRequest request,
			RequestCallBack<String> requestCallBack) {
		try {
			String json = request.toJsonString(request);
			HttpHelper.postJSON(Constant.HOSTURL, json, requestCallBack);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 保存指定日程信息
	 * 
	 * @param request
	 * @param requestCallBack
	 */
	public static void saveHairdresserSchedule(
			SaveHairdresserScheduleRequest request,
			RequestCallBack<String> requestCallBack) {
		try {
			String json = request.toJsonString(request);
			HttpHelper.postJSON(Constant.HOSTURL, json, requestCallBack);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取商圈列表
	 * 
	 * @param request
	 * @param requestCallBack
	 */
	public static void BusinessAreaStoreList(
			BusinessAreaStoreListRequest request,
			RequestCallBack<String> requestCallBack) {
		try {
			String json = request.toJsonString(request);
			HttpHelper.postJSON(Constant.HOSTURL, json, requestCallBack);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 美发师"我的日程"信息
	 */
	public static void GetDayWorkingRequest(H_GetDayWorkingRequest request, RequestCallBack<String> requestCallBack) {
		try {
			String json = request.toJsonString(request);
			HttpHelper.post(Constant.HOSTURL, json, requestCallBack);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 获得“一键快捷设置信息”接口
	 * 
	 * @param request
	 * @param requestCallBack
	 */
	public static void GetMasterDefaultSettingRequest(H_GetMasterDefaultSettingRequest request,
			RequestCallBack<String> requestCallBack) {
		try {
			String json = request.toJsonString(request);
			HttpHelper.postJSON(Constant.HOSTURL, json, requestCallBack);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 设置“一键快捷设置信息”接口
	 * 
	 * @param request
	 * @param requestCallBack
	 */
	public static void SetMasterDefaultSetting(H_SetMasterDefaultSettingRequest request,
			RequestCallBack<String> requestCallBack) {
		try {
			String json = request.toJsonString(request);
			HttpHelper.postJSON(Constant.HOSTURL, json, requestCallBack);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 一键设置信息功能
	 * 
	 * @param request
	 * @param requestCallBack
	 */
	public static void SaveQuickSetting(H_QuickSettingRequest request, RequestCallBack<String> requestCallBack) {
		try {
			String json = request.toJsonString(request);
			HttpHelper.postJSON(Constant.HOSTURL, json, requestCallBack);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 美发师保存自己的工作内容
	 * 
	 * @param request
	 * @param requestCallBack
	 */
	public static void SaveSelfWorking(H_SaveSelfWorkingRequest request, RequestCallBack<String> requestCallBack) {
		try {
			String json = request.toJsonString(request);
			HttpHelper.postJSON(Constant.HOSTURL, json, requestCallBack);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 美发师删除自编辑内容
	 * 
	 * @param request
	 * @param callBack
	 */
	public static void DeleteSelfWorking(H_DeleteSelfWorkingRequest request, RequestCallBack<String> callBack) {
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
	 * 美发师得到自编辑订单的内容
	 * 
	 * @param request
	 * @param callBack
	 */
	public static void GetSelfWorking(H_GetSelfWorkingRequest request, RequestCallBack<String> callBack) {
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
