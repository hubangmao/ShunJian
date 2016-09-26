package cc.ruit.shunjianmei.home.schedule;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.oruit.widget.loadingdialog.LoadingDailog;
import com.oruit.widget.loadingdialog.LoadingViewUtil;
import com.oruit.widget.title.TitleUtil;
import com.umeng.analytics.MobclickAgent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import cc.ruit.shunjianmei.base.BaseAdapter;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.constants.Constant;
import cc.ruit.shunjianmei.home.order.OrderActivity;
import cc.ruit.shunjianmei.home.order.OrderDetailFragment;
import cc.ruit.shunjianmei.net.api.OrderApi;
import cc.ruit.shunjianmei.net.request.HairdresserScheduleRequest;
import cc.ruit.shunjianmei.net.request.SaveHairdresserScheduleRequest;
import cc.ruit.shunjianmei.net.request.ScheduleOrderListRequest;
import cc.ruit.shunjianmei.net.response.BusinessAreaStoreListResponse;
import cc.ruit.shunjianmei.net.response.HairdresserScheduleResponse;
import cc.ruit.shunjianmei.net.response.OrderListResponse;
import cc.ruit.shunjianmei.net.response.SaveHairdresserScheduleResponse;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmei.util.MyEventBus;
import cc.ruit.shunjianmei.util.RoundImageLoaderUtil;
import cc.ruit.shunjianmei.util.ScreenUtils;
import cc.ruit.shunjianmei.util.view.EmptyView;
import cc.ruit.shunjianmeihairdresser.R;
import cc.ruit.utils.sdk.ToastUtils;
import cc.ruit.utils.sdk.Util;
import cc.ruit.utils.sdk.date.DateUtil;
import cc.ruit.utils.sdk.http.NetWorkUtils;
import de.greenrobot.event.EventBus;

/**
 * @ClassName: ScheduleFragment
 * @Description: 日程
 * @author: lee
 * @date: 2015年10月12日 下午3:54:33
 */
public class ScheduleFragment extends BaseFragment implements OnClickListener {
	@ViewInject(R.id.schedule_gridlayout)
	private GridView gv_list;// 时间
	@ViewInject(R.id.schedule_radiogroup)
	private RadioGroup radiogroup;
	@ViewInject(R.id.schedule_radiobutton_allright)
	private TextView allwork;
	@ViewInject(R.id.schedule_radiobutton_allfalse)
	private TextView allunwork;
	@ViewInject(R.id.schedule_horizontalScrollView_top)
	private HorizontalScrollView horizontalScrollView;
	@ViewInject(R.id.schedule_imageView_left)
	private ImageView leftImageView;
	@ViewInject(R.id.schedule_imageView_right)
	private ImageView rightImageView;
	@ViewInject(R.id.fl_content)
	private LinearLayout fl_content;
	@ViewInject(R.id.schedule_linearlayout_address)
	private LinearLayout linearlayout_address;
	@ViewInject(R.id.tv_schedule_new_time)
	private TextView newTime;
	@ViewInject(R.id.tv_schedule_manager_work_details)
	private TextView workDetails;
	public static String DATE = "";
	private String tag = "";
	EmptyView ev;// 空载页
	private View subViewChild;

	private int subViewWidth, subViewMoveX;

	public static final int BUSINESSLISTCODE = 1000;
	private ArrayList<BusinessAreaStoreListResponse> businessImp;
	private String addressIds = "";
	private String dateMsg = "";// 当前时间页
	ScheduleGridViewAdapter timeAdapter;// 时间adapter
	List<HairdresserScheduleResponse> list = new ArrayList<HairdresserScheduleResponse>();// 时间的集合

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("ScheduleFragment"); // 统计页面
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("ScheduleFragment");
	}

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.schedule_manager_layout, null);
		ViewUtils.inject(this, view);
		initTitle();
		initDateOfMMdd();
		initData();
		initEventBus();
		return view;
	}

	/**
	 * @Title: initTitle
	 * @Description: 标题初始化
	 * @author: lee
	 * @return: void
	 */
	private void initTitle() {
		TitleUtil title = new TitleUtil(view);
		title.tv_title.setText("日程管理");
	}

	/**
	 * @Title: initDateOfMMdd
	 * @Description: 初始化日期
	 * @author: lee
	 * @return: void
	 */
	private void initDateOfMMdd() {
		horizontalScrollView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				subViewChild = (View) horizontalScrollView.getChildAt(horizontalScrollView.getChildCount() - 1);
				subViewWidth = subViewChild.getRight();
				subViewMoveX = horizontalScrollView.getScrollX();
				if (subViewWidth - subViewMoveX - horizontalScrollView.getWidth() <= 45) {
					// 最右边
					leftImageView.setVisibility(View.VISIBLE);
					rightImageView.setVisibility(View.GONE);
				} else if (subViewMoveX <= 10) {
					leftImageView.setVisibility(View.GONE);
					rightImageView.setVisibility(View.VISIBLE);
				} else {
					leftImageView.setVisibility(View.VISIBLE);
					rightImageView.setVisibility(View.VISIBLE);
				}
				return false;
			}
		});
		addRadioButton();
		timeAdapter = new ScheduleGridViewAdapter(activity, list);
		gv_list.setAdapter(timeAdapter);
		gv_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if ("-1".equals(list.get(position).getState())) {
					list.get(position).setState("0");
				} else if ("0".equals(list.get(position).getState())) {
					list.get(position).setState("-1");
				}
				timeAdapter.notifyDataSetChanged();
				setAllCanWorkBtn();
			}
		});
	}

	/**
	 * 
	 * @Title: addRadioButton
	 * @Description: 添加日期
	 * @author: 欧阳
	 * @param total
	 * @return: void
	 */
	protected void addRadioButton() {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < 15; i++) {
			list.add(DateUtil.getNextDay(i, "MM.dd(E)"));
		}
		for (int i = 0; i < list.size(); i++) {
			RadioButton rb = new RadioButton(activity);
			android.view.ViewGroup.LayoutParams params = new LayoutParams(ScreenUtils.dip2px(activity, 80),
					LayoutParams.MATCH_PARENT);
			rb.setLayoutParams(params);
			rb.setButtonDrawable(android.R.color.transparent);
			rb.setGravity(Gravity.CENTER);
			rb.setBackgroundResource(R.drawable.schedule_fragment_radiobutton_bg);
			rb.setSingleLine(true);
			rb.setTextColor(activity.getResources().getColor(R.color.black));
			rb.setTextSize(13);
			rb.setText(list.get(i));
			radiogroup.addView(rb);
			rb.setChecked(i == 0 ? true : false);
			final int index = i;
			rb.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					getData(DateUtil.getNextDay(index, "yyyy-MM-dd"));
					getDataTime(DateUtil.getNextDay(index, "MM.dd(E)"));

				}
			});
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		if (tag == "scheduleSettingActivity") {
			getData(DATE);
			tag = "";
		} else if (tag == "MyScheduleActivity") {
			getData(DATE);
			tag = "";
		}
	}

	/**
	 * @Title: initData
	 * @Description: 初始化数据
	 * @author: lee
	 * @return: void
	 */
	private void initData() {
		ev = new EmptyView(activity, new OnClickListener() {

			@Override
			public void onClick(View v) {
				LoadingDailog.show(activity, "数据加载中，请稍后");
				getData(DateUtil.getNextDay(0, "yyyy-MM-dd"));
				// getBusinessAreaStoreListMsg("0");
			}
		});
		leftImageView.setVisibility(View.GONE);
		rightImageView.setVisibility(View.VISIBLE);
		// businessname.setText("请点击设置，选择地址");
		// address.setVisibility(View.INVISIBLE);
		getData(DateUtil.getNextDay(0, "yyyy-MM-dd"));
		newTime.setText(DateUtil.getNextDay(0, "MM.dd(E)" + "日程管理："));
		workDetails.setText(DateUtil.getNextDay(0, "MM.dd(E)") + "日程管理");
	}

	/**
	 * @Title: getData
	 * @Description: 获取数据
	 * @author: lee
	 * @param dateIndex
	 *            1代表1天以后
	 * @return: void
	 */
	void getData(String time) {
		if (!LoadingDailog.isShowing()) {
			LoadingDailog.show(activity, "数据加载中，请稍候。。。");
		}
		DATE = time;
		dateMsg = time;
		getDateMsg(time);
		getOrderListByDate(time);
	}

	void getDataTime(String time) {
		newTime.setText(time + "日程管理：");
		workDetails.setText(time + "日程管理");
	}

	/**
	 * 
	 * @Title: initEventBus
	 * @Description: 注册EventBus
	 * @author: 欧阳
	 * @return: void
	 */
	private void initEventBus() {
		EventBus.getDefault().register(this);
	}

	/**
	 * 
	 * @Title: onDestroy
	 * @Description: 注销EventBus
	 * @see cc.ruit.shunjianmei.base.BaseFragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	/**
	 * 
	 * @Title: onEventMainThread
	 * @Description: 这个方法不能进行耗时操作
	 * @author: 欧阳
	 * @param bus
	 * @return: void
	 */
	public void onEventMainThread(MyEventBus bus) {
		if (!TextUtils.isEmpty(bus.getmMsg()) && bus.getmMsg().equals("刷新订单状态")) {
			initData();
		}
	}

	/**
	 * @Title: getOrderListByDate
	 * @Description: 获取指定日期订单
	 * @author: lee
	 * @param time
	 * @return: void
	 */
	boolean isCanUser = false;

	private void getOrderListByDate(String time) {
		if (!isCanUser) {
			return;
		}
		// TODO Auto-generated method stub
		if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
			ToastUtils.showShort(activity.getResources().getString(R.string.no_networks_found));
			LoadingDailog.dismiss();
			fl_content.removeAllViews();
			fl_content.addView(ev.getView());
			ev.setErrState();
			new LoadingViewUtil(view).hint();
			return;
		}
		ScheduleOrderListRequest request = new ScheduleOrderListRequest("" + UserManager.getUserID(), time);
		OrderApi.ScheduleOrderList(request, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				new LoadingViewUtil(view).hint();
				LoadingDailog.dismiss();

				BaseResponse result = BaseResponse.getBaseResponse(responseInfo.result);
				if (result == null) {
					return;
				}
				fl_content.removeAllViews();
				String[] split = result.getMsg().split("\\|");
				if ("1".equals(split[0])) {
					ToastUtils.showShort(split[1] + "");
				}
				if (result.getCode() == 1000) {

					List<OrderListResponse> response = OrderListResponse.getclazz2(result.getData());
					// resultHanlder(response,"1");
					addOrderItem(fl_content, response);
				} else if (result.getCode() == 2100) {
					ev.setNullState();
				} else {
					ev.setErrState();
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				new LoadingViewUtil(view).hint();
				LoadingDailog.dismiss();
				ev.setErrState();
				fl_content.removeAllViews();
				fl_content.addView(ev.getView());
				ToastUtils.showShort(activity.getResources().getString(R.string.request_failure));
			}
		});
	}

	@Override
	@OnClick({ R.id.schedule_radiobutton_allright, R.id.schedule_button_save, R.id.schedule_button_setting,
			R.id.schedule_radiobutton_allfalse, R.id.ll_schedule_manager_layout, R.id.fl_content })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.schedule_radiobutton_allright:// 当日工作
			setAllCanWork(true);
			break;
		case R.id.schedule_radiobutton_allfalse:// 当日休息
			setAllCanWork(false);
			break;
		case R.id.schedule_button_save:// 保存
			saveDateTimeToNet();
			break;
		case R.id.schedule_button_setting:
			startActivityForResult(new Intent(activity, BusinessAreaStoreListActivity.class), BUSINESSLISTCODE);
			// activity.startActivityForResult(ManagerActivity.getIntent(activity,BusinessAreaStoreListActivity.class.getName()
			// ), BUSINESSLISTCODE);
			break;
		case R.id.ll_schedule_manager_layout:// 跳转一键设置
			Intent in = new Intent(activity, ScheduleSettingActivity.class);
			startActivity(in);
			tag = "scheduleSettingActivity";
			break;
		case R.id.fl_content:// 日程管理中心
			Intent intent = new Intent(activity, MyScheduleActivity.class);
			startActivity(intent);
			tag = "MyScheduleActivity";
			break;
		default:
			break;
		}
	}

	/**
	 * @Title: setAllCanWorkBtn
	 * @Description: 设置当日工作和当日休息按钮状态
	 * @author: lee
	 * @param i
	 * @return: void
	 */
	protected void setAllCanWorkBtn() {
		int workCount = 0;
		int unworkCount = 0;
		for (int j = 0; j < list.size(); j++) {
			if ("-1".equals(list.get(j).getState())) {
				unworkCount++;
			} else if ("0".equals(list.get(j).getState())) {
				workCount++;
			}
		}
		Drawable img_on, img_off;
		img_off = activity.getResources().getDrawable(R.drawable.radiobutton_false);
		img_on = activity.getResources().getDrawable(R.drawable.point_red);
		// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
		img_off.setBounds(0, 0, img_off.getMinimumWidth(), img_off.getMinimumHeight());
		img_on.setBounds(0, 0, img_on.getMinimumWidth(), img_on.getMinimumHeight());
		allwork.setCompoundDrawables(workCount == list.size() ? img_on : img_off, null, null, null); // 设置当日工作
		allunwork.setCompoundDrawables(unworkCount == list.size() ? img_on : img_off, null, null, null); // 设置当日休息
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		LogUtils.e("resultCode = " + resultCode);
		if (resultCode == BUSINESSLISTCODE) {
			businessImp = (ArrayList<BusinessAreaStoreListResponse>) data.getSerializableExtra("BUSINESSLISTCODE");
			// addressIds = data.getStringExtra("BUSINESSLISTCODEADDRESSID");
			// LogUtils.e("addressIds = " + addressIds);
			addAddressItemView(businessImp);
		}
	}

	/**
	 * @Title: addAddressItemView
	 * @Description: 添加地址
	 * @author: lee
	 * @param businessImp
	 * @return: void
	 */
	void addAddressItemView(List<BusinessAreaStoreListResponse> businessImp) {
		linearlayout_address.removeAllViews();
		addressIds = "";
		for (int i = 0; businessImp != null && i < businessImp.size(); i++) {
			addView(linearlayout_address, businessImp.get(i));
			if (!TextUtils.isEmpty(businessImp.get(i).getAddress())) {
				addressIds += businessImp.get(i).getID() + "|";
			}
		}
	}

	/**
	 * @Title: addView
	 * @Description: 添加一个item
	 * @author: lee
	 * @param listView
	 * @param listitem
	 * @return: void
	 */
	private void addView(LinearLayout listView, BusinessAreaStoreListResponse listitem) {
		ListViewitemHolder vhitem = new ListViewitemHolder();
		View convertView = LayoutInflater.from(activity).inflate(R.layout.schedule_manager_view_item, null);
		vhitem.findView(convertView);
		vhitem.businessname.setText(listitem.getName());
		vhitem.address.setText(listitem.getAddress());
		vhitem.address.setVisibility(TextUtils.isEmpty(listitem.getAddress()) ? View.GONE : View.VISIBLE);
		listView.addView(convertView);
	}

	/**
	 * @Title: checkSaveParames
	 * @Description: 校验保存参数
	 * @author: lee
	 * @return
	 * @return: boolean
	 */
	boolean checkSaveParames() {
		int workCount = 0;
		int unworkCount = 0;
		for (int j = 0; j < list.size(); j++) {
			if ("-1".equals(list.get(j).getState())) {
				unworkCount++;
			} else if ("0".equals(list.get(j).getState())) {
				workCount++;
			}
		}

		if (unworkCount != list.size() && TextUtils.isEmpty(addressIds)) {
			ToastUtils.showShort("请先选择地址！");
			return false;
		}
		if (list == null || list.size() <= 0) {
			ToastUtils.showLong("请选择休息或可约的时段");
			return false;
		}
		return true;
	}

	/**
	 * @Title: saveDateTimeToNet
	 * @Description: 保存当前日程
	 * @author: lee
	 * @return: void
	 */
	private void saveDateTimeToNet() {
		if (list == null || list.size() <= 0) {
			ToastUtils.showLong("请选择休息或可约的时段");
			return;
		}
		List<SaveHairdresserScheduleResponse> templist = new ArrayList<SaveHairdresserScheduleResponse>();
		if (!checkSaveParames()) {
			return;
		}
		templist.clear();
		for (int i = 0; i < list.size(); i++) {
			HairdresserScheduleResponse bean = list.get(i);
			if (!bean.getState().equals("-2")) {
				templist.add(new SaveHairdresserScheduleResponse(bean.getHour(), bean.getState()));
			}
		}

		LoadingDailog.show(activity, "正在保存中，请稍候。。。");
		if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
			ToastUtils.showShort(activity.getResources().getString(R.string.no_networks_found));
			LoadingDailog.dismiss();
			ev.setErrState();
			new LoadingViewUtil(view).hint();
			return;
		}
		SaveHairdresserScheduleRequest request = new SaveHairdresserScheduleRequest("" + UserManager.getUserID(),
				dateMsg, templist, addressIds);
		OrderApi.saveHairdresserSchedule(request, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				new LoadingViewUtil(view).hint();
				// LoadingDailog.dismiss();

				BaseResponse result = BaseResponse.getBaseResponse(responseInfo.result);
				if (result == null) {
					return;
				}
				String[] split = result.getMsg().split("\\|");
				if ("1".equals(split[0])) {
					ToastUtils.showShort(split[1] + "");
				}
				if (result.getCode() == 1000) {
					ToastUtils.showShort("同步成功！");
					getData(dateMsg);
				} else {
					LoadingDailog.dismiss();
					ev.setErrState();
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				new LoadingViewUtil(view).hint();
				LoadingDailog.dismiss();
				ToastUtils.showShort(activity.getResources().getString(R.string.request_failure));
				ev.setErrState();
			}
		});
	}

	/**
	 * @Title: setAllCanWork
	 * @Description: 全部可约的设置
	 * @author: lee
	 * @param canWork
	 * @return: void
	 */
	private void setAllCanWork(boolean canWork) { // TODO
		// -1休息0可约1锁定2已预约
		if (canWork) {// 全部可约
			for (int i = 0; i < list.size(); i++) {
				if ("-1".equals(list.get(i).getState())) {
					list.get(i).setState("0");
				}
			}
			timeAdapter.notifyDataSetChanged();
		} else {// 全部休息
			for (int i = 0; i < list.size(); i++) {
				if ("0".equals(list.get(i).getState())) {
					list.get(i).setState("-1");
				}
			}
			timeAdapter.notifyDataSetChanged();
		}
		setAllCanWorkBtn();
	}

	/**
	 * @Title: getDateMsg
	 * @Description: 获取当日每时段是否可用
	 * @author: lee
	 * @param i
	 * @return: void
	 */
	private void getDateMsg(String time) {

		if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
			ToastUtils.showShort(activity.getResources().getString(R.string.no_networks_found));
			LoadingDailog.dismiss();
			ev.setErrState();
			new LoadingViewUtil(view).hint();
			return;
		}
		HairdresserScheduleRequest request = new HairdresserScheduleRequest("" + UserManager.getUserID(), time);
		OrderApi.hairdresserSchedule(request, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				new LoadingViewUtil(view).hint();
				LoadingDailog.dismiss();

				BaseResponse result = BaseResponse.getBaseResponse(responseInfo.result);
				if (result == null) {
					return;
				}
				String[] split = result.getMsg().split("\\|");
				if ("1".equals(split[0])) {
					ToastUtils.showShort(split[1] + "");
				}
				if (result.getCode() == 1000) {
					List<HairdresserScheduleResponse> response = HairdresserScheduleResponse
							.getclazz2(result.getData());
					resultHanlder(response);
				} else {
					ev.setErrState();
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				new LoadingViewUtil(view).hint();
				LoadingDailog.dismiss();
				ToastUtils.showShort(activity.getResources().getString(R.string.request_failure));
				ev.setErrState();
			}
		});
	}

	/**
	 * @Title: resultHanlder
	 * @Description: 结果处理
	 * @author: lee
	 * @param listcontrolSignUp
	 * @return: void
	 */
	void resultHanlder(List<HairdresserScheduleResponse> info) {
		if (info == null) {
			LogUtils.e("MessageListResponse err");
			return;
		}
		list.clear();
		list.addAll(info);
		// TODO sdfdf
		timeAdapter.notifyDataSetChanged();
		// 设置地址
		if (list.size() > 0 && list.get(0).getItem() != null && list.get(0).getItem().size() > 0) {
			addAddressItemView(list.get(0).getItem());
		} else {
			List<BusinessAreaStoreListResponse> tempList = new ArrayList<BusinessAreaStoreListResponse>();
			tempList.add(new BusinessAreaStoreListResponse("0", "请点击设置按钮添加地址", ""));
			addAddressItemView(tempList);
		}
		setAllCanWorkBtn();
	}

	/**
	 * 将短时间格式字符串转换为时间 yyyy-MM-dd
	 * 
	 * @return 短时间格式 yyyy-MM-dd
	 */
	public static Date strToDate(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}

	/**
	 * @ClassName: ScheduleGridViewAdapter
	 * @Description: 日程时段
	 * @author: lee
	 * @date: 2015年11月7日 上午11:59:11
	 */
	class ScheduleGridViewAdapter extends BaseAdapter {

		private Context context;
		private List<HairdresserScheduleResponse> list;

		public ScheduleGridViewAdapter(Context context, List<HairdresserScheduleResponse> list) {
			super(context, list);
			this.context = context;
			this.list = list;
		}

		@Override
		public int getCount() {
			return list == null ? 0 : list.size();
		}

		@Override
		public HairdresserScheduleResponse getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewitemHolder vh = null;
			if (convertView == null) {
				vh = new ViewitemHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.schedulefragment_gridview_item, null);
				vh.findView(convertView);
				convertView.setTag(vh);
			} else {
				vh = (ViewitemHolder) convertView.getTag();
			}
			// 得到数据和绑定数据
			setLabel(position, vh);
			return convertView;
		}

		private void setLabel(int position, ViewitemHolder vh) {
			HairdresserScheduleResponse bean = list.get(position);
			// -1休息0可约1锁定2已预约-2过期
//			-1休息0可约1锁定2已预约-2过期
			if (Constant.SCHEDULE_STATE_REST.equals(bean.getState())) {//休息
				vh.datetime.setText(list.get(position).getHour()+":00(休息)");
				vh.datetime.setTextColor(Color.BLACK);
				vh.datetime.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_gray);
			}else if(Constant.SCHEDULE_STATE_CAN_ORDER.equals(bean.getState())){
				vh.datetime.setText(list.get(position).getHour()+":00(可约)");
				vh.datetime.setTextColor(Color.WHITE);
				vh.datetime.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_blue);
			}else if(Constant.SCHEDULE_STATE_LOCK.equals(bean.getState())){//锁定状态显示已约
				vh.datetime.setText(list.get(position).getHour()+":00(已约)");
				vh.datetime.setTextColor(Color.WHITE);
				vh.datetime.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_red);
			}else if(Constant.SCHEDULE_STATE_YET_ORDER.equals(bean.getState())){
				vh.datetime.setText(list.get(position).getHour()+":00(已约)");
				vh.datetime.setTextColor(Color.WHITE);
				vh.datetime.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_red);
			}else if(Constant.SCHEDULE_STATE_OVERDUE.equals(bean.getState())){
				vh.datetime.setText(list.get(position).getHour()+":00(过期)");
				vh.datetime.setTextColor(Color.BLACK);
				vh.datetime.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_gray);
			}
		}
	}

	static class ViewitemHolder {

		@ViewInject(R.id.schedule_gridview_item_time)
		TextView datetime;
		// @ViewInject(R.id.schedule_gridview_item_checkbox)
		// CheckBox checkBox; // 选中可约 未选中休息

		void findView(View convertView) {
			ViewUtils.inject(this, convertView);
			// checkBox.setChecked(true);
			// datetime.setTextColor(Color.WHITE);
		}
	}

	static class ListViewitemHolder {

		@ViewInject(R.id.schedule_textview_businessname)
		TextView businessname;
		@ViewInject(R.id.schedule_textview_address)
		TextView address;

		void findView(View convertView) {
			ViewUtils.inject(this, convertView);
		}
	}

	/**
	 * @Title: addOrderItem
	 * @Description: 添加订单列表
	 * @author: lee
	 * @param listView
	 * @param listitem
	 * @return: void
	 */
	private void addOrderItem(LinearLayout listView, List<OrderListResponse> listitem) {
		listView.removeAllViews();
		for (int i = 0; i < listitem.size(); i++) {
			OrderViewHolder vhitem = new OrderViewHolder();
			View convertView = LayoutInflater.from(activity).inflate(R.layout.item_order_list_layout, null);
			vhitem.findView(convertView);
			final OrderListResponse obj = listitem.get(i);
			vhitem.tv_ordernum.setText(obj.getOrderCode());
			vhitem.tv_projectname.setText(obj.getStyleName());
			vhitem.tv_state.setText(obj.getStateName());
			vhitem.tv_username.setText(obj.getName());
			vhitem.tv_titel.setText("");
			vhitem.tv_time.setText(obj.getAppointmentTime());
			vhitem.tv_address.setText(obj.getAddress());
			vhitem.tv_mobile.setText(obj.getMobile());
			vhitem.tv_money.setText(obj.getAmount());
			vhitem.iv_call.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Util.openTelDall(activity, obj.getMobile());
					//// Uri uri = Uri.parse("tel:" + obj.getMobile());
					// Intent intent = new Intent();
					// intent.setAction(Intent.ACTION_DIAL);
					// intent.setData(uri);
					// startActivity(intent);
				}
			});
			RoundImageLoaderUtil.setErrImage(R.drawable.tx_man, R.drawable.tx_man, R.drawable.tx_man);
			RoundImageLoaderUtil.getInstance(activity, 10).loadImage(obj.getPhoto(), vhitem.iv_userphoto);
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					startActivity(OrderActivity.getIntent(activity, OrderDetailFragment.class.getName(), obj));//
				}
			});
			listView.addView(convertView);
			// 设置一个10dp的间隙
			LayoutParams params = (LayoutParams) convertView.getLayoutParams();
			params.bottomMargin = ScreenUtils.dip2px(activity, 10);
		}
	}

	static class OrderViewHolder {
		@ViewInject(R.id.tv_ordernum)
		TextView tv_ordernum;
		@ViewInject(R.id.tv_projectname)
		TextView tv_projectname;
		@ViewInject(R.id.tv_state)
		TextView tv_state;

		@ViewInject(R.id.tv_username)
		TextView tv_username;
		@ViewInject(R.id.tv_title)
		TextView tv_titel;
		@ViewInject(R.id.tv_time)
		TextView tv_time;

		@ViewInject(R.id.tv_address)
		TextView tv_address;
		@ViewInject(R.id.tv_mobile)
		TextView tv_mobile;
		@ViewInject(R.id.tv_money)
		TextView tv_money;
		@ViewInject(R.id.tv_cancel)
		TextView tv_cancel;

		@ViewInject(R.id.iv_userphoto)
		ImageView iv_userphoto;
		@ViewInject(R.id.iv_call)
		ImageView iv_call;

		void findView(View convertView) {
			ViewUtils.inject(this, convertView);
		}
	}
}
