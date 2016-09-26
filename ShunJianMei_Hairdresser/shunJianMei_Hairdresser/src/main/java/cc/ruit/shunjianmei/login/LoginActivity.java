package cc.ruit.shunjianmei.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import cc.ruit.shunjianmei.base.BaseActivity;
import cc.ruit.shunjianmei.home.MainActivity;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmei.util.FragmentManagerUtils;
import cc.ruit.shunjianmeihairdresser.R;
import cc.ruit.utils.sdk.ToastUtils;
import cn.jpush.android.api.JPushInterface;

import com.umeng.analytics.MobclickAgent;
/**
 * @ClassName: LoginActivity
 * @Description: 登录相关
 * @author: lee
 * @date: 2015年8月31日 下午3:39:01
 */
public class LoginActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.public_base_layout);
		MobclickAgent.updateOnlineConfig(this);
		if (UserManager.getUserID() == 0) {
			LoginFragment fg = new LoginFragment();
			Bundle bundle = new Bundle();
			bundle.putString("from", WelcomeFragment.class.getSimpleName());
			fg.setArguments(bundle);
			FragmentManagerUtils.add(this, R.id.content_frame, fg, false);
		} else {
			Intent intent = MainActivity.getIntent(this);
			startActivity(intent);
			this.finish();
		}
	}

	private long mExitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			hideSoftInput();
			boolean isBack = FragmentManagerUtils.back(this, R.id.content_frame);
			if (!isBack && (System.currentTimeMillis() - mExitTime) > 2000) {
				ToastUtils.showLong("再按一次退出");
				mExitTime = System.currentTimeMillis();
			} else if (!isBack) {
				finish();
			}
		}
		return true;
	}

	/**
	 * @Title: getIntent
	 * @Description: 获取跳转到当前Activity的intent对象
	 * @author: lee
	 * @param context
	 * @return
	 * @return: Intent
	 */
	public static Intent getIntent(Context context) {
		Intent in = new Intent(context, LoginActivity.class);
		return in;
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		JPushInterface.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		JPushInterface.onPause(this);
	}

	/**
	 * @Title: hideSoftInput
	 * @Description: 隐藏软键盘
	 * @author: lee
	 * @return: void
	 */
	public void hideSoftInput() {
		View view = getWindow().peekDecorView();
		if (view != null) {
			InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}
}
