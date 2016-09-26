package cc.ruit.shunjianmei.base;

import org.json.JSONObject;

import android.text.TextUtils;

import com.lidroid.xutils.util.LogUtils;
/**
 * 
 * @ClassName: BaseResponse
 * @Description: TODO 服务器响应统一格式基类，因为服务器返回json格式统一，所以用于解析服务返回的json
 * @author: lee
 * @date: 2015年7月22日 上午10:44:28
 */
public class BaseResponse {
	private int code;
	private String msg = "失败";
	private String data;
	private int total;
	
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
	public BaseResponse() {
		super();
	}
	
	
	public BaseResponse(int code, String msg, String data, int total) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
		this.total = total;
	}
	public static BaseResponse getBaseResponse(String json){
		LogUtils.i("onSuccess return:"+json);
		if(TextUtils.isEmpty(json)){
			return null;
		}
		try {
			BaseResponse br = new BaseResponse();
			JSONObject job = new JSONObject(json);
			br = new BaseResponse(job.optInt("code"), job.optString("msg"), job.optString("data"),job.optInt("total"));
			return br;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public String toString() {
		return "BaseResponse [code=" + code + ", msg=" + msg + ", data=" + data
				+ ", total=" + total + "]";
	}
	
}
