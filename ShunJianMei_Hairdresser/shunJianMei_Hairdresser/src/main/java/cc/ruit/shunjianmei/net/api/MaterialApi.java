package cc.ruit.shunjianmei.net.api;

import java.io.UnsupportedEncodingException;

import cc.ruit.shunjianmei.constants.Constant;
import cc.ruit.shunjianmei.net.request.MaterialApplyRequest;
import cc.ruit.shunjianmei.net.request.MaterialInfoRequest;
import cc.ruit.shunjianmei.net.request.MaterialRecordRequest;
import cc.ruit.shunjianmei.net.request.SaveMaterialDeliveryRequest;
import cc.ruit.shunjianmei.net.request.UpdateRecordStateRequest;
import cc.ruit.utils.sdk.http.HttpHelper;

import com.lidroid.xutils.http.callback.RequestCallBack;

public class MaterialApi {
	/**
	 * 
	 * @Title: getMaterialInfo
	 * @Description: 获取物料信息
	 * @param request
	 * @param callBack
	 * @return: void
	 */
	public static void getMaterialInfo(MaterialInfoRequest request,
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
	 * 
	 * @Title: saveMaterialDelivery
	 * @Description: 保存将提取的物料信息
	 * @param request
	 * @param callBack
	 * @return: void
	 */
	public static void saveMaterialDelivery(
			SaveMaterialDeliveryRequest request,
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
	 * 
	 * @Title: materialApplyDelivery
	 * @Description: 获取在进行中的提取的物料信息
	 * @param request
	 * @param callBack
	 * @return: void
	 */
	public static void materialApplyDelivery(MaterialApplyRequest request,
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
	 * 
	 * @Title: materialRecordDelivery
	 * @Description: 已经领取里的物料记录
	 * @param request
	 * @param callBack
	 * @return: void
	 */
	public static void materialRecordDelivery(MaterialRecordRequest request,
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
	 * 
	 * @Title: updateRecordStateDelivery
	 * @Description: 更新提取状态 标记申请已完成
	 * @param request
	 * @param callBack
	 * @return: void
	 */
	public static void updateRecordStateDelivery(
			UpdateRecordStateRequest request,
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
}
