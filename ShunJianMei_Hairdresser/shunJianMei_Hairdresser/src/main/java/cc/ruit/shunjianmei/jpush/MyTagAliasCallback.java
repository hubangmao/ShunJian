package cc.ruit.shunjianmei.jpush;

import java.util.Set;

import android.util.Log;
import cn.jpush.android.api.TagAliasCallback;

public class MyTagAliasCallback implements TagAliasCallback{

	@Override
	public void gotResult(int code, String alias, Set<String> tags) {
		String TAG = "JPush";
		switch (code) {
		case 0:
			String logs = "Set tag and alias success, alias = " + alias + "; tags = " + tags;
			Log.i(TAG, logs);
			break;
		
		default:
			logs = "Failed with errorCode = " + code + " alias = " + alias + "; tags = " + tags;
			Log.e(TAG, logs);
		}
	}
	
}
