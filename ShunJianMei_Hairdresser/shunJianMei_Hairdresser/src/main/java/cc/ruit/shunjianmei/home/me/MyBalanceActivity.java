package cc.ruit.shunjianmei.home.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import cc.ruit.shunjianmei.base.BaseActivity;
import cc.ruit.shunjianmei.util.FragmentManagerUtils;
import cc.ruit.shunjianmeihairdresser.R;
/**
 * @ClassName: ManagerActivity
 * @Description: 我的余额
 * @author: Johnny
 * @date: 2015年9月3日 下午4:45:12
 */
public class MyBalanceActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.public_base_layout);
		Intent in = getIntent();
		Bundle bundle = in.getExtras();
		String className = in.getStringExtra("className");
		try {
			Fragment fragment = FragmentManagerUtils.getFragment(this, className);
//			fragment.setArguments(bundle);
			FragmentManagerUtils.add(this, R.id.content_frame, fragment, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	/**
	 * @Title: getIntent
	 * @Description: 获取跳转到当前Activity的intent对象
	 * @author: Johnny
	 * @param context
	 * @return
	 * @return: Intent
	 */
	public static Intent getIntent(Context context,String className) {
		Intent in = new Intent(context, MyBalanceActivity.class);
		in.putExtra("className", className);
		return in;
	}
	/**
	 * @Title: getIntent
	 * @Description: 获取跳转到当前Activity的intent对象
	 * @author: Johnny
	 * @param context
	 * @return
	 * @return: Intent
	 */
	public static Intent getIntent(Context context,String className,String type) {
		Intent in = new Intent(context, MyBalanceActivity.class);
		in.putExtra("className", className);
		in.putExtra("type", type);
		return in;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_DOWN) {
			boolean isBack = FragmentManagerUtils.back(this, R.id.content_frame);
			if (!isBack) {
				finish();
			}
		}
		return true;
	}
}
