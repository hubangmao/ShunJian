package cc.ruit.shunjianmei.net.api;

import java.io.UnsupportedEncodingException;

import cc.ruit.shunjianmei.constants.Constant;
import cc.ruit.shunjianmei.net.request.SaveEvaluationRequest;
import cc.ruit.utils.sdk.http.HttpHelper;

import com.lidroid.xutils.http.callback.RequestCallBack;
/**
 * 
 * @ClassName: SaveEvaluationApi
 * @Description: 保存接口评论接口
 * @author: 欧阳
 * @date: 2015年10月27日 下午1:25:03
 */
public class SaveEvaluationApi {
	/**
	 * 
	 * @Title: SaveEvaluation
	 * @Description: 保存评论接口
	 * @author: 欧阳
	 * @param request
	 * @param callBack
	 * @return: void
	 */
	public static void SaveEvaluation(SaveEvaluationRequest request,
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
