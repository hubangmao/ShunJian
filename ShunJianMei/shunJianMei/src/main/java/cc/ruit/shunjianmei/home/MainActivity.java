package cc.ruit.shunjianmei.home;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import cc.ruit.shunjianmei.R;
import cc.ruit.shunjianmei.base.BaseActivity;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.home.hairdressert.HairDressertFragment;
import cc.ruit.shunjianmei.home.me.MeFragment;
import cc.ruit.shunjianmei.home.order.OrderFragment;
import cc.ruit.shunjianmei.login.LoginActivity;
import cc.ruit.shunjianmei.login.LoginFragment;
import cc.ruit.shunjianmei.net.api.SettingApi;
import cc.ruit.shunjianmei.net.request.CheckVersionRequest;
import cc.ruit.shunjianmei.net.response.CheckVersionResponse;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmei.util.FragmentManagerUtils;
import cc.ruit.utils.sdk.ToastUtils;
import cc.ruit.utils.sdk.http.NetWorkUtils;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.oruit.widget.messagedialog.MessageDialog;
import com.umeng.analytics.MobclickAgent;

/**
 * @ClassName: MainActivity
 * @Description: 主页框架 带侧滑
 * @author: lee
 * @date: 2015年8月30日 下午8:23:43
 */
public class MainActivity extends BaseActivity {
	@ViewInject(R.id.iv_home_find)
	ImageView imagefind;// 发现
	@ViewInject(R.id.iv_home_order)
	ImageView imageorder;// 订单
	@ViewInject(R.id.iv_home_time)
	ImageView imagetime;// 预约
	@ViewInject(R.id.iv_home_me)
	ImageView imageme;// 我的
	public boolean isFront;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity_layout);
		instance = this;
		ViewUtils.inject(this);
		initData();

	}

	/**
	 * 
	 * @Title: initData
	 * @Description:添加第一个fragment
	 * @author: 欧阳
	 * @return: void
	 */
	private void initData() {
		MobclickAgent.updateOnlineConfig(this);
		Intent in = getIntent();
		String className = in.getStringExtra("classname");
		if (!TextUtils.isEmpty(className)) {
			tabControler(className);
		} else {
			tabControler(FindFragment.class.getName());
		}
		getData(false);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		String className = intent.getStringExtra("classname");
		if (!TextUtils.isEmpty(className)) {
			tabControler(className);
		}
	}

	/**
	 * @Title: tabControler
	 * @Description: tab切换
	 * @author: lee
	 * @param className
	 * @return: void
	 */
	public void tabControler(String className) {
		FragmentManagerUtils.detachByTag(this, FindFragment.class.getName(), OrderFragment.class.getName(),
				HairDressertFragment.class.getName(), MeFragment.class.getName());
		FragmentManagerUtils.addOrAttach(this, className, R.id.content_frame);
		// 添加要显示的fragment
		controlTab(className);
	}

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

	@OnClick({ R.id.ll_home_find, R.id.ll_home_order, R.id.ll_home_time, R.id.ll_home_me })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_home_find:// 发现界面
			MobclickAgent.onEvent(this, "tab_work");
			tabControler(FindFragment.class.getName());
			break;
		case R.id.ll_home_order:// 订单界面
			if (UserManager.getUserID() <= 0) {
				showUnLoginDialog();
				break;
			}
			MobclickAgent.onEvent(this, "tab_student");
			tabControler(OrderFragment.class.getName());
			break;
		case R.id.ll_home_time:// 预约界面
			if (UserManager.getUserID() <= 0) {
				showUnLoginDialog();
				break;
			}
			MobclickAgent.onEvent(this, "tab_home");
			tabControler(HairDressertFragment.class.getName());

			break;
		case R.id.ll_home_me:// 我的界面
			if (UserManager.getUserID() <= 0) {
				showUnLoginDialog();
				break;
			}
			MobclickAgent.onEvent(this, "tab_withdraw");
			tabControler(MeFragment.class.getName());
			break;
		default:
			break;
		}
	}

	/**
	 * @Title: controlTab
	 * @Description: tab切换是的图标管理
	 * @author: lee
	 * @param checked
	 * @return: void
	 */
	public void controlTab(String name) {
		int checked = 0;
		if (FindFragment.class.getName().equals(name)) {
			checked = R.id.ll_home_find;
		} else if (MeFragment.class.getName().equals(name)) {
			checked = R.id.ll_home_me;
		} else if (OrderFragment.class.getName().equals(name)) {
			checked = R.id.ll_home_order;
		} else if (HairDressertFragment.class.getName().equals(name)) {
			checked = R.id.ll_home_time;
		}
		if (checked == 0) {
			return;
		}
		imagefind.setImageResource(checked == R.id.ll_home_find ? R.drawable.eye_red : R.drawable.eye_gray);
		imageorder.setImageResource(checked == R.id.ll_home_order ? R.drawable.order_red : R.drawable.order_gray);
		imagetime.setImageResource(checked == R.id.ll_home_time ? R.drawable.time_red : R.drawable.time_gray);
		imageme.setImageResource(checked == R.id.ll_home_me ? R.drawable.user_red : R.drawable.user_gray);
		int red = getResources().getColor(R.color.red_f3);
		int gray = getResources().getColor(R.color.gray_ab);
		((TextView) findViewById(R.id.tv_home_find)).setTextColor(checked == R.id.ll_home_find ? red : gray);
		((TextView) findViewById(R.id.tv_home_order)).setTextColor(checked == R.id.ll_home_order ? red : gray);
		((TextView) findViewById(R.id.tv_home_time)).setTextColor(checked == R.id.ll_home_time ? red : gray);
		((TextView) findViewById(R.id.tv_home_me)).setTextColor(checked == R.id.ll_home_me ? red : gray);

	}

	/*-----------------检测更新逻辑部分--begin-------------------*/
	/**
	 * @Title: getData
	 * @Description: 版本更新
	 * @author: lee
	 * @return: void
	 */
	public void getData(final boolean isUser) {
		if (!NetWorkUtils.checkNetworkAvailable1(this)) {
			ToastUtils.showShort(this.getResources().getString(R.string.no_networks_found));
			return;
		}
		CheckVersionRequest request = new CheckVersionRequest(getVersionName(this), "2");
		SettingApi.CheckVersion(request, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				BaseResponse result = BaseResponse.getBaseResponse(responseInfo.result);
				if (result == null) {
					return;
				}
				String[] split = result.getMsg().split("\\|");
				if ("1".equals(split[0]) && isUser) {
					ToastUtils.showShort(split[1] + "");
				}
				if (result.getCode() == 1000) {
					CheckVersionResponse checkVersionResponse = CheckVersionResponse.getclazz(result.getData());
					// 当返回版本大于本地版本号提示下载
					if (checkVersionResponse != null && Double.parseDouble(checkVersionResponse.Code) > Double
							.parseDouble(getVersionName(MainActivity.this))) {
						showDialog(checkVersionResponse);
					}
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				// ToastUtils.showLong("网络可能不太好，请您再试试吧");
			}
		});
	}

	/**
	 * @Title: showDialog
	 * @Description: 提示下载
	 * @author: lee
	 * @param checkVersionResponse
	 * @return: void
	 */
	void showDialog(final CheckVersionResponse checkVersionResponse) {
		final MessageDialog messageDialog = new MessageDialog(this);
		messageDialog.setTitle("是否下载" + checkVersionResponse.Name);
		messageDialog.setMessage(checkVersionResponse.Intro);
		messageDialog.getOkButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 启动浏览器
				startBrowser(checkVersionResponse.URL);
			}
		});
		messageDialog.getCancelButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				messageDialog.dismiss();
			}
		});
		messageDialog.show();
	}

	/**
	 * 
	 * @Title: getVersionName
	 * @Description: 获取本地VersionName
	 * @param context
	 * @return
	 * @return: String
	 */
	public String getVersionName(Context context) {
		String versionName = "";
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			if (versionName == null || versionName.length() <= 0) {
				return "";
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName;
	}

	/**
	 * 
	 * @Title: startBrowser
	 * @Description: 启动浏览器
	 * @param url
	 * @return: void
	 */
	public void startBrowser(String url) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		Uri content_url = Uri.parse(url);
		intent.setData(content_url);
		startActivity(intent);
	}
	/*----------------版本检测更新------end-----------------------------*/

	/**
	 * @Title: showDialog
	 * @Description: 提示登录
	 * @author: lee
	 * @param checkVersionResponse
	 * @return: void
	 */
	void showUnLoginDialog() {
		final MessageDialog messageDialog = new MessageDialog(this);
		messageDialog.setMessage("请先登录");
		messageDialog.getOkButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 启动登录
				Bundle bundle = new Bundle();
				bundle.putString("from", MainActivity.class.getSimpleName());
				Intent in = LoginActivity.getIntent(MainActivity.this, LoginFragment.class.getName());
				in.putExtras(bundle);
				startActivity(in);
				messageDialog.dismiss();
			}
		});
		messageDialog.getCancelButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				messageDialog.dismiss();
			}
		});
		messageDialog.show();
	}

	private static MainActivity instance;// mainactivity实体

	/**
	 * @Title: getInstance
	 * @Description: 获取MainActivity实体对象
	 * @author: lee
	 * @return
	 * @return: MainActivity
	 */
	public static MainActivity getInstance() {
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
		Intent in = new Intent(context, MainActivity.class);
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
		Intent in = new Intent(context, MainActivity.class);
		in.putExtra("classname", firstCalssName);
		return in;
	}

	private long mExitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				ToastUtils.showLong("再按一次退出");
				mExitTime = System.currentTimeMillis();
			} else {
				finish();
			}
		}
		return true;
	}

}
