package cc.ruit.utils.sdk.http;

import android.content.Context;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.HttpHandler.State;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.util.PreferencesCookieStore;

public class FileUploadHttpHelper {
	private static HttpUtils client = new HttpUtils();
	private static PreferencesCookieStore cookie;
	public static HttpHandler<?> handler;
	
	
	public static void init(Context context){
		 client.configRequestThreadPoolSize(4);
	        client.configCookieStore(cookie);
	        client.configTimeout(1000 * 60 * 3 );//文件上传超时时间，3分钟还不成功就放弃吧。。。。
        cookie = new PreferencesCookieStore(context);
	}
	
    public static void post(String url, RequestParams params,
            RequestCallBack<String> callBack) {
        addHeader();
        printLog(url,params);
        handler = client.send(HttpMethod.POST, url, params, callBack);
    }
    
	public static void send(HttpMethod method, String url,
            RequestParams params, RequestCallBack<String> callBack) {
        addHeader();
        handler = client.send(method, url, params, callBack);
    }

    private static void printLog(String url, RequestParams params) {
    	LogUtils.i("url>>"+url);
    	LogUtils.i("params>>"+params.getBodyParams().toString());
	}
    
    private static void addHeader() {
        // 平台/机型 / 系统名称/系统版本号/应用名称/应用版本号/渠道
        // LogUtil.i(String.format("Android/%s/Android/%s/%s/%s/%s",
        // Build.MODEL,
        // Build.VERSION.RELEASE, AppConstant.appName,
        // GetVersion.getVersionName(MPluginApplication.getContext()),
        // "android market"));
        // 09-04 14:48:36.882: I/test(24952): Android/MI
        // 2S/Android/4.1.1/sellerhelper/1.0/android market
        // 添加user-agent
        // client.addHeader("User-Agent", String.format(
        // "Android/%s/Android/%s/%s/%s/%s", Build.MODEL,
        // Build.VERSION.RELEASE, AppConstant.appName,
        // GetVersion.getVersionName(MPluginApplication.getContext()),
        // "android market"));
        // String model = Build.MODEL;
        // if (model.length()>19) {
        // model = model.substring(0, 18);
        // }
        // client.configUserAgent( String.format(
        // "Android/%s/Android/%s/%s/%s/%s", model,
        // Build.VERSION.RELEASE, App.getContext().getString(R.string.app_name),
        // GetVersion.getVersionName(App.getContext()),
        // MetaDataUtil.getMetaData(App.getContext(), "UMENG_CHANNEL")));
    }

    public static void stop() {
        if (handler != null
                && (handler.getState() == State.LOADING || handler.getState() == State.WAITING)) {
            handler.cancel();
        }
    }
}
