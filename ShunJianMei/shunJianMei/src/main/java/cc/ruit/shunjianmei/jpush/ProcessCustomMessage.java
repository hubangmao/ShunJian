package cc.ruit.shunjianmei.jpush;

import java.util.Random;

import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import cc.ruit.shunjianmei.MainApplication;
import cc.ruit.shunjianmei.R;
import cc.ruit.shunjianmei.home.MainActivity;
import cc.ruit.shunjianmei.home.order.OrderActivity;
import cc.ruit.shunjianmei.home.order.OrderDetailFragment;
import cc.ruit.shunjianmei.net.response.MessageListResponse;
import cc.ruit.shunjianmei.net.response.OrderListResponse;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cn.jpush.android.api.JPushInterface;

import com.lidroid.xutils.util.LogUtils;

/**
 * @ClassName: ProcessCustomMessage
 * @Description: 消息分流处理
 * @author: lee
 * @date: 2015年9月17日 下午5:51:10
 */
public class ProcessCustomMessage {
	String TAG = ProcessCustomMessage.class.getSimpleName();
	public final static String INTENT_ACTION_NEW_MEASSAGE = "com.oxygener.app.NEW_MESSAGE"; // 有新消息
    public final static String INTENT_EXTRA_MESSAGE = "message";
    public final static String INTENT_EXTRA_EXTRA = "extra";
       
	/**
     * 自定义消息处理
     * @param context
     * @param bundle
     */
    public void processCustomMessage(Context context, Bundle bundle) {
    	if (UserManager.getUserID()==0) {
			return;
		}
    	try {
			String title = bundle.getString(JPushInterface.EXTRA_TITLE);
			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        	JSONObject obj = new JSONObject(extras);
            int type = obj.optInt("type");
            String dealid = obj.optString("dealid");
            switch (type) {
			case 1:
				resultControl(context, title, message, dealid);
				break;
			default:
				break;
			}
            
		} catch (Exception e) {
			LogUtils.e(""+e.getMessage());
		}
    }
    /**
     * @Title: resultControl
     * @Description: 结果处理
     * @author: lee
     * @param context
     * @param title
     * @param message
     * @return: void
     */
    void resultControl(Context context,String title,String message,String dealid){
    	Intent in = OrderActivity.getIntent(context, OrderDetailFragment.class.getName());
    	OrderListResponse bean = new OrderListResponse();
    	bean.setOrderID(dealid);
    	in.putExtra("bean", bean);
    	in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	sendToNotification(context, title, message, in);
//    	context.startActivity(in);
//		sendToNotificationAutoCancel(context, title, message, in);
    }
    /**
     * @Title: sendToNotification
     * @Description: 发送通知
     * @author: lee
     * @param context
     * @param title
     * @param message
     * @param show
     * @return: void
     */
    private void sendToNotification(Context context,String title,String message,Intent show){
    	try {
    		Log.d(TAG, "show Notification");
    		if (TextUtils.isEmpty(title)) {
    			title =context.getResources().getString(R.string.app_name);
    		}
    		if (TextUtils.isEmpty(message)) {
    			message = "您有一条新消息";
    		}
    		int nid = new Random().nextInt();
    		
    		PendingIntent pi = PendingIntent.getActivity(context, nid, show,
    				PendingIntent.FLAG_CANCEL_CURRENT);
    		Notification notification = new Notification();
    		notification.icon = context.getApplicationInfo().icon;
    		
    		notification.tickerText = message;
    		notification.when = System.currentTimeMillis();
    		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
    		
    		notification.defaults |= Notification.DEFAULT_VIBRATE;
    		notification.defaults |= Notification.DEFAULT_SOUND;
    		notification.flags |= Notification.FLAG_AUTO_CANCEL;
    		notification.setLatestEventInfo(context, title, message, pi);
    		NotificationManager notificationManager = (NotificationManager) context
    				.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
    		notificationManager.notify(nid, notification);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    /**
     * @Title: sendToNotificationAutoCancel
     * @Description: 发送通知后立刻取消掉
     * @author: lee
     * @param context
     * @param title
     * @param message
     * @param show
     * @return: void
     */
    private void sendToNotificationAutoCancel(Context context,String title,String message,Intent show){
		try {
			Log.d(TAG, "show Notification");
			if (TextUtils.isEmpty(title)) {
				title =context.getResources().getString(R.string.app_name);
			}
			if (TextUtils.isEmpty(message)) {
				message = "您有一条新消息";
			}
			int nid = new Random().nextInt();

			PendingIntent pi = PendingIntent.getActivity(context, nid, show,
					PendingIntent.FLAG_CANCEL_CURRENT);
			Notification notification = new Notification();
			notification.icon = context.getApplicationInfo().icon;
			
			notification.tickerText = message;
			notification.when = System.currentTimeMillis();
			notification.flags |= Notification.FLAG_SHOW_LIGHTS;

			notification.defaults |= Notification.DEFAULT_VIBRATE;
			notification.defaults |= Notification.DEFAULT_SOUND;
			notification.flags |= Notification.FLAG_AUTO_CANCEL;
			notification.setLatestEventInfo(context, title, message, pi);
			NotificationManager notificationManager = (NotificationManager) context
					.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
			notificationManager.notify(nid, notification);
			notificationManager.cancel(nid);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
