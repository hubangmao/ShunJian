package cc.ruit.shunjianmei.net.api;

import java.io.UnsupportedEncodingException;

import cc.ruit.shunjianmei.constants.Constant;
import cc.ruit.shunjianmei.net.request.StoreEvaluationListRequest;
import cc.ruit.utils.sdk.http.HttpHelper;

import com.lidroid.xutils.http.callback.RequestCallBack;
/**
 * @ClassName: StoreEvaluationList
 * @Description: 发型师评论列表接口
 * @author: Johnny
 * @date: 2015年9月5日 下午5:35:27
 */
public class StoreEvaluationListApi {
	/**
	 * 
	 * @Title: StoreEvaluationList
	 * @Description: 商户评论列表
	 * @author: Johnny
	 * @param request
	 * @param callBack
	 * @return: void
	 */
	public static void storeEvaluationList(StoreEvaluationListRequest request,
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
