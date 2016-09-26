package cc.ruit.shunjianmei.home.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import cc.ruit.shunjianmei.R;
import cc.ruit.shunjianmei.base.BaseActivity;
import cc.ruit.shunjianmei.home.MainActivity;
import cc.ruit.shunjianmei.net.response.OrderListResponse;
import cc.ruit.shunjianmei.util.FragmentManagerUtils;
/**
 * 
 * @ClassName: OrderActivity
 * @Description: 订单界面
 * @author: 欧阳
 * @date: 2015年10月26日 下午2:08:19
 */
public class OrderActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.public_base_layout);
		Intent in = getIntent();
		Bundle bundle = in.getExtras();
		String className = in.getStringExtra("className");
		try {
			Fragment fragment = FragmentManagerUtils.getFragment(this, className);
			fragment.setArguments(bundle);
			FragmentManagerUtils.add(this, R.id.content_frame, fragment, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
	public static Intent getIntent(Context context,String className) {
		Intent in = new Intent(context, OrderActivity.class);
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
	public static Intent getIntent(Context context,String className,OrderListResponse bean) {
		Intent in = new Intent(context, OrderActivity.class);
		in.putExtra("className", className);
		in.putExtra("bean", bean);
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
	public static Intent getIntent(Context context,String className,String type) {
		Intent in = new Intent(context, OrderActivity.class);
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
				if (MainActivity.getInstance()==null) {
					startActivity(MainActivity.getIntent(this, OrderFragment.class.getName()));
				}
			}
		}
		return true;
	}
}
