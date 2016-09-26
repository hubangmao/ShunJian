package cc.ruit.shunjianmei.home.manager.material;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import cc.ruit.shunjianmei.base.BaseActivity;
import cc.ruit.shunjianmei.util.FragmentManagerUtils;
import cc.ruit.shunjianmeihairdresser.R;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.oruit.widget.title.TitleUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * @ClassName: MaterialManagementActivity
 * @Description: 物料管理页面
 * @author: lee
 * @date: 2015年9月3日 下午4:45:12
 */
@ContentView(R.layout.materialmanagement_activity_layout)
public class MaterialManagementActivity extends BaseActivity implements
		OnClickListener {
	@ViewInject(R.id.materialmanager_radiobutton_get)
	private Button getMaterial;
	@ViewInject(R.id.materialmanager_radiobutton_doing)
	private Button doingMaterial;
	@ViewInject(R.id.materialmanager_radiobutton_history)
	private Button historyMaterial;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		instance = this;
		initTitle();
		initData();
	}

	/**
	 * @Title: initData
	 * @Description: 数据初始化
	 * @author: leezw
	 * @return: void
	 */
	private void initData() {
		String className = MaterialGetFragment.class.getName();
		tabControler(className);
		getMaterial
				.setBackgroundResource(R.drawable.materialmanager_left_btn01);
		doingMaterial
				.setBackgroundResource(R.drawable.materialmanager_middle_btn);
		historyMaterial
				.setBackgroundResource(R.drawable.materialmanager_right_btn);
	}

	/**
	 * @Title: tabControler
	 * @Description: tab切换
	 * @author: lee
	 * @param className
	 * @return: void
	 */
	public void tabControler(String className) {
		FragmentManagerUtils.detachByTag(this,
				MaterialDoningFragment.class.getName(),
				MaterialHistoryFragment.class.getName());// 先将除菜单fragment外的其他fragment
															// detach
		FragmentManagerUtils.addOrAttach(this, className,
				R.id.materialactivity_content_frame);// 添加要显示的fragment
	}

	/**
	 * @Title: initTitle
	 * @Description: 标题初始化
	 * @author: lee
	 * @return: void
	 */
	private void initTitle() {
		TitleUtil title = new TitleUtil(this);
		title.iv_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		title.iv_left.setImageResource(R.drawable.back);
		title.iv_left.setVisibility(View.VISIBLE);
		title.tv_title.setText("物料管理");
	}

	@Override
	@OnClick({ R.id.materialmanager_radiobutton_get,
			R.id.materialmanager_radiobutton_doing,
			R.id.materialmanager_radiobutton_history })
	public void onClick(View v) {
		Log.e("alshdfjkhasldkjhf", v.getId() + "");
		switch (v.getId()) {
		case R.id.materialmanager_radiobutton_get:
			getMaterial
					.setBackgroundResource(R.drawable.materialmanager_left_btn01);
			doingMaterial
					.setBackgroundResource(R.drawable.materialmanager_middle_btn);
			historyMaterial
					.setBackgroundResource(R.drawable.materialmanager_right_btn);
			tabControler(MaterialGetFragment.class.getName());
			break;
		case R.id.materialmanager_radiobutton_doing:
			getMaterial
					.setBackgroundResource(R.drawable.materialmanager_left_btn);
			doingMaterial
					.setBackgroundResource(R.drawable.materialmanager_middle_btn01);
			historyMaterial
					.setBackgroundResource(R.drawable.materialmanager_right_btn);
			tabControler(MaterialDoningFragment.class.getName());
			break;
		case R.id.materialmanager_radiobutton_history:
			getMaterial
					.setBackgroundResource(R.drawable.materialmanager_left_btn);
			doingMaterial
					.setBackgroundResource(R.drawable.materialmanager_middle_btn);
			historyMaterial
					.setBackgroundResource(R.drawable.materialmanager_right_btn01);
			tabControler(MaterialHistoryFragment.class.getName());
			break;
		default:
			break;
		}
	}

	public boolean isFront;

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		isFront = false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		instance = null;
	}

	private static MaterialManagementActivity instance;// mainactivity实体

	/**
	 * @Title: getInstance
	 * @Description: 获取MainActivity实体对象
	 * @author: lee
	 * @return
	 * @return: MainActivity
	 */
	public static MaterialManagementActivity getInstance() {
		return instance;
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
		Intent in = new Intent(context, MaterialManagementActivity.class);
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
	public static Intent getIntent(Context context, String firstCalssName) {
		Intent in = new Intent(context, MaterialManagementActivity.class);
		in.putExtra("classname", firstCalssName);
		return in;
	}
}
