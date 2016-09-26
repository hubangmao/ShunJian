package cc.ruit.utils.sdk.http;

import java.io.File;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import cc.ruit.utils.R;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.HttpHandler.State;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.util.PreferencesCookieStore;

public class HttpHelper {
	private static HttpUtils client = new HttpUtils();
	private static PreferencesCookieStore cookie;
	public static HttpHandler<?> handler;
	public static Context ctx;
	
	public static void init(Context context){
		ctx = context;
		client.configRequestThreadPoolSize(4);
        client.configCookieStore(cookie);
        client.configTimeout(1000 * 5);
        cookie = new PreferencesCookieStore(context);
	}
    public static void get(String url, RequestParams params,
            RequestCallBack<String> callBack) {
        addHeader();
        printLog(url,params);
        handler = client.send(HttpMethod.GET, url, params, callBack);
    }

    public static void get(String url, RequestCallBack<String> callBack) {
        addHeader();
        printLog(url);
        handler = client.send(HttpMethod.GET, url, callBack);
    }

    public static void post(String url, RequestParams params,
            RequestCallBack<String> callBack) {
        addHeader();
        printLog(url,params);
        handler = client.send(HttpMethod.POST, url, params, callBack);
    }
    public static void post(String url, HttpEntity entity,
            RequestCallBack<String> callBack) {
        addHeader();
        RequestParams params = new RequestParams("UTF-8");
        params.setBodyEntity(entity);
        printLog(url);
        handler = client.send(HttpMethod.POST, url, params, callBack);
    }
    
    public static void postJSON(String url, String paramsJson,
			RequestCallBack<String> callBack)throws UnsupportedEncodingException {
		addHeader();
		RequestParams params = new RequestParams("UTF-8");
		StringEntity postingString = null;
//		postingString = new StringEntity(paramsJson);// json传递
		postingString = new StringEntity(paramsJson, "UTF-8");
//		String encodeJson = URLEncoder.encode(paramsJson, "UTF-8");
		params.setContentType("application/json");
//		params.setBodyEntity(encodeJson);
		params.setBodyEntity(postingString);
		printLog(url,paramsJson);
		handler = client.send(HttpMethod.POST, url, params, callBack);
	}

    public static void post(String url, RequestCallBack<String> callBack) {
        addHeader();
        printLog(url);
        handler = client.send(HttpMethod.POST, url, callBack);
    }

	public static void post(String url, String paramsJson,
			RequestCallBack<String> callBack)
			throws UnsupportedEncodingException {
		addHeader();
		RequestParams params = new RequestParams("UTF-8");
		StringEntity postingString = null;
		postingString = new StringEntity(paramsJson);// json传递
		params.setContentType("application/json");
		params.setBodyEntity(postingString);
		printLog(url,paramsJson);
		handler = client.send(HttpMethod.POST, url, params, callBack);
	}
	
	public static void send(HttpMethod method, String url,
            RequestParams params, RequestCallBack<Object> callBack) {
        addHeader();
        printLog(url,params);
        handler = client.send(method, url, params, callBack);
    }
	
	public static void download( String url,String target,
            RequestParams params, RequestCallBack<File> callBack){
		handler = client.download(HttpMethod.POST, url, target, params, callBack);
	}
	
    private static void addHeader() {
        // 平台/机型 / 系统名称/系统版本号/应用名称/应用版本号/渠道/设备id/ip地址/包名
		
//         09-04 14:48:36.882: I/test(24952): Android/MI
//         2S/Android/4.1.1/sellerhelper/1.0/android market
//         添加user-agent
//         client.addHeader("User-Agent", String.format("Android/%s/Android/%s/%s/%s/%s",Build.MODEL,Build.VERSION.RELEASE, 
//        		 "guaguazhuan", "1.0","android market"));
		String model = Build.MODEL;
		if (model.length() > 19) {
			model = model.substring(0, 18);
		}
		 // 平台/机型 / 系统名称/系统版本号/应用名称/应用版本号/渠道/设备id/ip地址/包名
		String header = String.format("Android/%s/Android/%s/%s/%s/%s/%s/%s/%s",
				Build.MODEL, Build.VERSION.RELEASE, "guaguazhuan", "1.0",
				"guaguazhuanguanfang", UUIDUtil.getUUID(ctx),
				CommonUtil.getLocalIpAddress(),
				CommonUtil.getPackageName(ctx));
		Log.i("addHeader",header );
		client.configUserAgent(header);
    }

    public static void stop() {
        if (handler != null
                && (handler.getState() == State.LOADING || handler.getState() == State.WAITING)) {
            handler.cancel();
        }
    }
    
    /*---------内容打印------------*/
    private static void printLog(String url) {
		LogUtils.i("url>>"+url);
	}
    private static void printLog(String url, RequestParams params) {
    	LogUtils.i("url>>"+url);
    	LogUtils.i("params>>"+params.getBodyParams().toString());
	}
    private static void printLog(String url, String paramsJson) {
    	LogUtils.i("url>>"+url);
    	LogUtils.i("params>>"+paramsJson);
    }
    /*---------内容打印------------*/
}
