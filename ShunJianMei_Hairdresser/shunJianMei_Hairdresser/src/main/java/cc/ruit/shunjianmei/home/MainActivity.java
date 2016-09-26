package cc.ruit.shunjianmei.home;

import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import cc.ruit.shunjianmei.base.BaseActivity;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.constants.Constant;
import cc.ruit.shunjianmei.db.MessageDbUtil;
import cc.ruit.shunjianmei.home.manager.ManagerFragment;
import cc.ruit.shunjianmei.home.me.MeFragment;
import cc.ruit.shunjianmei.home.order.OrderFragment;
import cc.ruit.shunjianmei.home.schedule.ScheduleFragment;
import cc.ruit.shunjianmei.net.api.OpenCityListApi;
import cc.ruit.shunjianmei.net.request.OpenCityListRequest;
import cc.ruit.shunjianmei.net.response.OpenCityListResponse;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmei.util.FragmentManagerUtils;
import cc.ruit.shunjianmeihairdresser.R;
import cc.ruit.utils.sdk.ToastUtils;
import cc.ruit.utils.sdk.http.NetWorkUtils;

/**
 * @ClassName: MainActivity
 * @Description: 主页框架 带侧滑
 * @author: lee
 * @date: 2015年8月30日 下午8:23:43
 */
@ContentView(R.layout.home_activity_layout)
public class MainActivity extends BaseActivity implements OnClickListener {
	// private Fragment mContent;//内容fragment
	@ViewInject(R.id.tv_red_point)
	TextView tv_point;

	private LocationClient mLocClient;
	private LocationClient mLocationClient = null;
	private BDLocationListener myListener = new MyLocationListenner();

	List<OpenCityListResponse> msgList;

	public boolean isFront;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		ViewUtils.inject(this);
		initData();
		initLocation();
	}

	private void initData() {
		MobclickAgent.updateOnlineConfig(this);
		Intent in = getIntent();
		String className = in.getStringExtra("classname");
		if (!TextUtils.isEmpty(className)) {
			tabControler(className);
		} else {
			className = ScheduleFragment.class.getName();
			tabControler(className);
		}
		setRedPoint((int) new MessageDbUtil().getUnReadCount(this));
		// 网络请求开通城市地址
		initCityList();
	}

	/**
	 * 
	 * @Title: initLocation
	 * @Description: 初始化百度定位
	 * @author: Johnny
	 * @return: void
	 */
	private void initLocation() {
		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener); // 注册监听函数
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(0);
		option.setIsNeedAddress(true);// 获取地址信息
		mLocClient.setLocOption(option);

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
		FragmentManagerUtils.detachByTag(this, ManagerFragment.class.getName(), OrderFragment.class.getName(),
				MeFragment.class.getName());// 先将除菜单fragment外的其他fragment
											// detach
		FragmentManagerUtils.addOrAttach(this, className, R.id.content_frame);// 添加要显示的fragment
		controlTab(className);
	}

	/**
	 * @Title: setRedPoint
	 * @Description: 控制消息红点
	 * @author: lee
	 * @param num
	 * @return: void
	 */
	public void setRedPoint(int num) {
		tv_point.setVisibility(num == 0 ? View.INVISIBLE : View.VISIBLE);
		tv_point.setText("" + num);
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
		// TODO Auto-generated method stub
		super.onStop();
		isFront = false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		instance = null;
	}

	@Override
	@OnClick({ R.id.ll_home_schedule, R.id.ll_home_manager, R.id.ll_home_order, R.id.ll_home_me })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_home_manager:
			MobclickAgent.onEvent(this, "tab_student");
			tabControler(ManagerFragment.class.getName());
			break;
		case R.id.ll_home_schedule:
			MobclickAgent.onEvent(this, "tab_work");
			tabControler(ScheduleFragment.class.getName());
			break;
		case R.id.ll_home_order:
			OrderFragment.requestMethod = Constant.ORDER_LIST_ALL_METHOD;
			MobclickAgent.onEvent(this, "tab_withdraw");
			tabControler(OrderFragment.class.getName());
			break;
		case R.id.ll_home_me:
			MobclickAgent.onEvent(this, "tab_message");
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
		if (ManagerFragment.class.getName().equals(name)) {
			checked = R.id.ll_home_manager;
		} else if (OrderFragment.class.getName().equals(name)) {
			checked = R.id.ll_home_order;
		} else if (MeFragment.class.getName().equals(name)) {
			checked = R.id.ll_home_me;
		} else if (ScheduleFragment.class.getName().equals(name)) {
			checked = R.id.ll_home_schedule;
		}
		if (checked == 0) {
			return;
		}

		((ImageView) findViewById(R.id.iv_home_schedule))
				.setImageResource(checked == R.id.ll_home_schedule ? R.drawable.edit_select : R.drawable.edit_normal);
		((ImageView) findViewById(R.id.iv_home_manager))
				.setImageResource(checked == R.id.ll_home_manager ? R.drawable.list_select : R.drawable.list_normal);
		((ImageView) findViewById(R.id.iv_home_order))
				.setImageResource(checked == R.id.ll_home_order ? R.drawable.order_select : R.drawable.order_normal);
		((ImageView) findViewById(R.id.iv_home_me))
				.setImageResource(checked == R.id.ll_home_me ? R.drawable.user_select : R.drawable.user_normal);
		int golden = getResources().getColor(R.color.red_f3);
		int black = getResources().getColor(R.color.black);
		((TextView) findViewById(R.id.tv_home_manager)).setTextColor(checked == R.id.ll_home_manager ? golden : black);
		((TextView) findViewById(R.id.tv_home_schedule))
				.setTextColor(checked == R.id.ll_home_schedule ? golden : black);
		((TextView) findViewById(R.id.tv_home_order)).setTextColor(checked == R.id.ll_home_order ? golden : black);
		((TextView) findViewById(R.id.tv_home_me)).setTextColor(checked == R.id.ll_home_me ? golden : black);
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

	/**
	 * 
	 * @Title: getCityList
	 * @Description: 得到城市列表
	 * @author: 欧阳
	 * @return: void
	 */
	private void initCityList() {
		if (!NetWorkUtils.isConnectInternet(this)) {
			ToastUtils.showShort("网络未链接，请检查网络设置");
			return;
		}
		// LoadingDailog.show(this, "数据加载中，请稍后", false);
		OpenCityListRequest request = new OpenCityListRequest("1");
		Log.i("Tag", "request=" + request.toJsonString(request));
		OpenCityListApi.OpenCityList(request, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				// LoadingDailog.dismiss();
				BaseResponse response = BaseResponse.getBaseResponse(responseInfo.result);
				if (response == null) {
					return;
				}

				String[] split = response.getMsg().split("\\|");
				if ("1".equals(split[0])) {
					ToastUtils.showShort(split[1] + "");
				}
				if (response.getCode() == 1000) {
					msgList = OpenCityListResponse.getclazz2(response.getData());
					mLocClient.start();
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				// LoadingDailog.dismiss();
				ToastUtils.showShort("请求数据失败");
			}
		});
	}

	/*------------------------------百度地图定位 begin---------------------------------*/
	/**
	 * 
	 * @ClassName: MyLocationListenner
	 * @Description: 定位SDK监听函数
	 * @author: Johnny
	 * @date: 2015年9月29日 下午3:14:57
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			String city = location.getCity();
			Float latitude = (float) location.getLatitude();
			Float longitude = (float) location.getLongitude();

			UserManager.setLatitude(latitude);
			UserManager.setlongitude(longitude);
			// UserManager.setLatitude((float) 39.8414970000);
			// UserManager.setlongitude((float) 116.2909490000);
			if (msgList == null || msgList.size() <= 0) {
				return;
			}
			for (int i = 0; msgList != null && i < msgList.size(); i++) {
				if (msgList.get(i).getName().equals(city)) {
					UserManager.setCityID(msgList.get(i).getID());
				} else {
					UserManager.setCityID("1");
				}
			}
			if ("0".equals(UserManager.getCityID())) {
				ToastUtils.showShort("当前城市没有开通");
			}

		}
	}

	/*------------------------------百度地图定位 end---------------------------------*/

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
