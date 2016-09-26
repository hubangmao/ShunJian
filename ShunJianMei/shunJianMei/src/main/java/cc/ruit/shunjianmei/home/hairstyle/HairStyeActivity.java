/**  
 * Copyright © 2015公司名字. All rights reserved.
 *
 * @Title: HairdresserActivity.java
 * @Prject: ShunJianMei
 * @Package: cc.ruit.shunjianmei.home.hairdressert
 * @Description: 发型师所有界面的fragment切换
 * @author: 欧阳  
 * @date: 2015年10月21日 上午10:03:49
 * @version: V1.0  
 */
package cc.ruit.shunjianmei.home.hairstyle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import cc.ruit.shunjianmei.R;
import cc.ruit.shunjianmei.base.BaseActivity;
import cc.ruit.shunjianmei.util.FragmentManagerUtils;

/**
 * @ClassName: HairdresserActivity
 * @Description: 发型界面
 * @author: 欧阳
 * @date: 2015年10月21日 上午10:03:49
 */
public class HairStyeActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_frame);
		Intent in = getIntent();
		Bundle bundle = in.getExtras();
		String className = in.getStringExtra("className");
		try {
			Fragment fragment = FragmentManagerUtils.getFragment(this,
					className);
			fragment.setArguments(bundle);
			FragmentManagerUtils.add(this, R.id.content_frame, fragment, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Title: getIntent
	 * @Description: 获取跳转到当前Activity的intent对象
	 * @author: lee
	 * @param context
	 * @return
	 * @return: Intent
	 */
	public static Intent getIntent(Context context, String className) {
		Intent in = new Intent(context, HairStyeActivity.class);
		in.putExtra("className", className);
		return in;
	}

	/**
	 * @Title: getIntent
	 * @Description: 获取跳转到当前Activity的intent对象
	 * @author: lee
	 * @param context
	 * @return
	 * @return: Intent
	 */
	public static Intent getIntent(Context context, String className,
			String type) {
		Intent in = new Intent(context, HairStyeActivity.class);
		in.putExtra("className", className);
		in.putExtra("type", type);
		return in;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			boolean isBack = FragmentManagerUtils
					.back(this, R.id.content_frame);
			if (!isBack) {
				finish();
			}
		}
		return true;
	}
}
