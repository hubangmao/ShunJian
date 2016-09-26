package cc.ruit.utils.ui;


import android.app.Application;
import cc.ruit.utils.sdk.ToastUtils;
import cc.ruit.utils.sdk.emoji.EmojiManager;

public class MyApplication extends Application {
	/**
	 * APP对象
	 */
	private static MyApplication instance;
	public static MyApplication getInstance() {
		return instance;
	}
	@Override
	public void onCreate() {  
		// TODO Auto-generated method stub
		super.onCreate();
		instance = this;
		ToastUtils.getInstace(this);
		EmojiManager.getInstace().initEmoji(this);
	}
}
