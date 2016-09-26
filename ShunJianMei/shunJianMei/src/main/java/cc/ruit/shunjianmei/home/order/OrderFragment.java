package cc.ruit.shunjianmei.home.order;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cc.ruit.shunjianmei.R;
import cc.ruit.shunjianmei.base.BaseActivity;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.net.api.OrderListApi;
import cc.ruit.shunjianmei.net.api.UpdateOrderStateApi;
import cc.ruit.shunjianmei.net.request.OrderListRequest;
import cc.ruit.shunjianmei.net.request.UpdateOrderStateRequest;
import cc.ruit.shunjianmei.net.response.OrderListResponse;
import cc.ruit.shunjianmei.pay.OrderPayActivity;
import cc.ruit.shunjianmei.pay.OrderPayFragment;
import cc.ruit.shunjianmei.reserve.EventOrderBean;
import cc.ruit.shunjianmei.reserve.ReserveActivity;
import cc.ruit.shunjianmei.reserve.ReserveFragment;
import cc.ruit.shunjianmei.reserve.ReserveFreeFragment;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmei.util.MyEventBus;
import cc.ruit.shunjianmei.util.RoundImageLoaderUtil;
import cc.ruit.shunjianmei.util.view.EmptyView;
import cc.ruit.shunjianmei.util.view.EmptyView.State;
import cc.ruit.shunjianmei.util.view.FilterItemBean;
import cc.ruit.shunjianmei.util.view.FilterPopupwindow;
import cc.ruit.shunjianmei.util.view.FilterPopupwindow.OnItemSelecedListener;
import cc.ruit.utils.sdk.ToastUtils;
import cc.ruit.utils.sdk.Util;
import cc.ruit.utils.sdk.http.NetWorkUtils;

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
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshBase;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshBase.Mode;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshBase.OnRefreshListener2;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshListView;

import de.greenrobot.event.EventBus;

/**
 * @ClassName: OrderFragment
 * @Description: 订单
 * @author: lee
 * @date: 2015年10月12日 下午4:03:42
 */
public class OrderFragment extends BaseFragment
		implements OnClickListener, OnItemClickListener, OnRefreshListener2<ListView> {
	@ViewInject(R.id.pulltorefreshlistview)
	PullToRefreshListView lv_refresh;// 列表
	@ViewInject(R.id.tv_state)
	TextView tv_state;// 状态
	@ViewInject(R.id.iv_state)
	ImageView iv_state;// 状态
	@ViewInject(R.id.ll_state)
	LinearLayout ll_state;// 状态
	@ViewInject(R.id.tv_time)
	TextView tv_time;// 时间
	@ViewInject(R.id.iv_time)
	ImageView iv_time;// 时间
	@ViewInject(R.id.ll_time)
	LinearLayout ll_time;// 时间
	@ViewInject(R.id.fl_content)
	FrameLayout fl_content;// 内容部分
	@ViewInject(R.id.ll_filter)
	LinearLayout ll_filter;// 筛选菜单布局
	EmptyView ev;// 空载页
	private final int STATE_TAG = 0;
	private final int TIME_TAG = 1;
	private OrderListAdapter mAdapter;
	List<OrderListResponse> orderList = new ArrayList<OrderListResponse>();
	int pageStart = 1;// 开始页下标
	String state = "0";// 状态
	String sort = "1";// 时间顺序
	FilterPopupwindow fp;// 筛选弹框
	private String Type = "";
	private String Userid = "";

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("OrderFragment"); // 统计页面
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("OrderFragment");
	}

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.order_layout, null);
		ViewUtils.inject(this, view);
		initTitle();
		initData();
		initEventBus();
		return view;
	}

	/**
	 * 
	 * @Title: initTitle
	 * @Description: 初始化标题
	 * @author: 欧阳
	 * @return: void
	 */
	private void initTitle() {
		TitleUtil title = new TitleUtil(view);
		title.tv_title.setText("我的订单");
	}

	/**
	 * 
	 * @Title: initData
	 * @Description: 初始化数据
	 * @author: 欧阳
	 * @return: void
	 */
	private void initData() {
		mAdapter = new OrderListAdapter(activity, orderList);
		lv_refresh.setAdapter(mAdapter);
		lv_refresh.setMode(Mode.BOTH);
		lv_refresh.getRefreshableView().setDividerHeight(BaseActivity.dip2px(activity, 5));
		ev = new EmptyView(activity, new OnClickListener() {

			@Override
			public void onClick(View v) {
				LoadingDailog.show(activity, "数据加载中，请稍后");
				pageStart = 1;
				getAllData(pageStart, state, sort);
			}
		});
		lv_refresh.setOnItemClickListener(this);
		lv_refresh.setEmptyView(ev.getView());
		lv_refresh.setOnRefreshListener(this);
		ev.setState(State.Loading);
		pageStart = 1;
		getAllData(pageStart, state, sort);
	}

	/**
	 * @Title: initFilterPopupWindow
	 * @Description: 初始化筛选弹框
	 * @author: lee
	 * @return: void
	 */
	void initFilterPopupWindow() {
		if (fp != null) {
			return;
		}
		fp = new FilterPopupwindow(activity, fl_content);
		fp.setOnItemClickListener(new OnItemSelecedListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id, int tag) {
				if (tag == 0) {
					if (state.equals(stateList.get(position).getId())) {
						return;
					}
					state = stateList.get(position).getId();
					tv_state.setText(stateList.get(position).getName());
					tv_state.setTag(position);
					LoadingDailog.show(activity, "正在加载...");
					getAllData(1, state, sort);
				} else if (tag == 1) {
					if (sort.equals(timeList.get(position).getId())) {
						return;
					}
					sort = timeList.get(position).getId();
					tv_time.setText(timeList.get(position).getName());
					tv_time.setTag(position);
					LoadingDailog.show(activity, "正在加载...");
					getAllData(1, state, sort);
				}
			}
		});
	}

	@Override
	@OnClick({ R.id.ll_state, R.id.ll_time })
	public void onClick(View v) {
		initFilterPopupWindow();
		switch (v.getId()) {
		case R.id.ll_state:
			int state = 0;
			if (tv_state.getTag() != null) {
				state = (Integer) tv_state.getTag();
			}
			if (fp.isCurrentShowing(STATE_TAG)) {
				fp.dismiss();
			} else {
				showStatePopupwindow(state);
			}

			break;
		case R.id.ll_time:
			int time = 0;
			if (tv_time.getTag() != null) {
				time = (Integer) tv_time.getTag();
			}
			if (fp.isCurrentShowing(time)) {
				fp.dismiss();
			} else {
				showTimePopupwindow(time);
			}

			break;
		default:
			break;
		}
	}

	List<FilterItemBean> stateList;

	private void showStatePopupwindow(int position) {
		if (stateList == null) {
			stateList = new ArrayList<FilterItemBean>();
			stateList.add(new FilterItemBean("0", "全部"));
			stateList.add(new FilterItemBean("1", "待支付"));
			stateList.add(new FilterItemBean("3", "已预约"));
			stateList.add(new FilterItemBean("4", "已签到"));
			stateList.add(new FilterItemBean("5", "服务中"));
			stateList.add(new FilterItemBean("6", "评论中"));
			stateList.add(new FilterItemBean("7", "评论完成"));
			stateList.add(new FilterItemBean("8", "订单完结"));
			stateList.add(new FilterItemBean("9", "取消"));
			stateList.add(new FilterItemBean("10", "异常处理"));
		}
		fp.show(stateList, STATE_TAG);
		fp.setSelectedPosition(position);
	}

	List<FilterItemBean> timeList;

	private void showTimePopupwindow(int position) {
		if (timeList == null) {
			timeList = new ArrayList<FilterItemBean>();
			timeList.add(new FilterItemBean("1", "从最近开始"));
			timeList.add(new FilterItemBean("2", "从最远开始"));
		}
		fp.show(timeList, TIME_TAG);
		fp.setSelectedPosition(position);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		startActivity(
				OrderActivity.getIntent(activity, OrderDetailFragment.class.getName(), orderList.get(position - 1)));

	}

	/**
	 * @Title: getPoints
	 * @Description: 获取数据
	 * @author: lee
	 * @return: void
	 */
	public void getAllData(final int pageStart, String state, String sort) {
		if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
			ToastUtils.showShort(activity.getResources().getString(R.string.no_networks_found));
			LoadingDailog.dismiss();
			lv_refresh.onRefreshComplete();
			ev.setErrState();
			new LoadingViewUtil(view).hint();
			return;
		}
		OrderListRequest request = new OrderListRequest(UserManager.getUserID() + "", state, sort, "20",
				"" + pageStart);
		OrderListApi.OrderList(request, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				new LoadingViewUtil(view).hint();
				LoadingDailog.dismiss();

				BaseResponse result = BaseResponse.getBaseResponse(responseInfo.result);
				if (result == null) {
					return;
				}
				lv_refresh.onRefreshComplete();
				String[] split = result.getMsg().split("\\|");
				if ("1".equals(split[0])) {
					ToastUtils.showShort(split[1] + "");
				}
				if (result.getCode() == 1000) {

					List<OrderListResponse> response = OrderListResponse.getclazz2(result.getData());
					resultHanlder(response, pageStart);
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
				lv_refresh.onRefreshComplete();
				ToastUtils.showShort(activity.getResources().getString(R.string.request_failure));
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
	void resultHanlder(List<OrderListResponse> info, int pageStart) {
		if (info == null) {
			LogUtils.e("MessageListResponse err");
			return;
		}

		if (1 == pageStart) {
			orderList.clear();
		}
		if (1 != pageStart && info.size() <= 0) {
			return;
		}
		pageStart++;
		orderList.addAll(info);
		if (orderList.size() <= 0) {
			ev.setNullState();
		}
		mAdapter.notifyDataSetChanged();

	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		pageStart = 1;
		getAllData(pageStart, state, sort);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (orderList != null && orderList.size() > 0) {
			getAllData(pageStart, state, sort);
		}
	}

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
		if (!TextUtils.isEmpty(bus.getmMsg()) && bus.getmMsg().equals("请刷新订单状态")) {
			initData();
		}

	}

	public void onEventMainThread(EventOrderBean bean) {
		if (bean.isPay()) {
			initData();
		}
	}

	/**
	 * 
	 * @ClassName: OrderListAdapter
	 * @Description: 订单列表
	 * @author: 欧阳
	 * @date: 2015年10月29日 下午9:23:24
	 */
	class OrderListAdapter extends BaseAdapter {
		AlertDialog dialog;
		private Context context;
		private List<OrderListResponse> list;

		public OrderListAdapter(Context context, List<OrderListResponse> list) {
			this.context = context;
			this.list = list;
		}

		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
			if (list == null || list.size() <= 0) {
				ev.setNullState();
			}
		}

		@Override
		public int getCount() {
			return list == null ? 0 : list.size();
		}

		@Override
		public OrderListResponse getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh = null;
			if (convertView == null) {
				vh = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.item_order_list_layout, null);
				vh.findView(convertView);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			// 得到数据和绑定数据
			setLable(position, vh);
			return convertView;
		}

		/**
		 * @Title: setLable
		 * @Description: TODO
		 * @author: lee
		 * @param position
		 * @param vh
		 * @return: void
		 */
		private void setLable(final int position, ViewHolder vh) {
			final OrderListResponse obj = getItem(position);
			// vh.tv_ordernum.setText(obj.getOrderCode());
			vh.tv_projectname.setText(obj.getStyleName());

			vh.tv_username.setText(obj.getName());
			vh.tv_LevelName.setText(obj.getLevelName());
			vh.tv_time.setText(obj.getAppointmentTime());
			vh.tv_address.setText(obj.getAddress());
			vh.tv_mobile.setText(obj.getStoreTel());
			vh.tv_money.setText("¥" + obj.getAmount());
			vh.tv_storeName.setText(obj.getStoreName());
			vh.iv_call.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Util.openTelDall(activity, list.get(position).getMobile());
				}
			});
			vh.tv_mobile.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Util.openTelDall(activity, list.get(position).getStoreTel());
				}
			});
			// 判断
			int State = 0;
			try {
				State = Integer.parseInt(obj.getState());
			} catch (Exception e) {
			}
			if (State == 3) {
				vh.tv_state.setText("预约成功");
			} else {
				vh.tv_state.setText(obj.getStateName());
			}
			if (State == 1) {
				vh.tv_cancel.setVisibility(View.VISIBLE);
				vh.tv_pay.setVisibility(View.VISIBLE);
				vh.tv_pay.setText("支付");
				vh.tv_pay_info.setVisibility(View.GONE);
				vh.tv_money.setVisibility(View.GONE);
				vh.tv_cancel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						alertDialog(list.get(position).getOrderID(), "9", position);
					}
				});
				vh.tv_pay.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = OrderPayActivity.getIntent(activity, OrderPayFragment.class.getName());
						intent.putExtra("OrderID", obj.getOrderID());
						startActivity(intent);
					}
				});
			} else if (State == 9) {
				if (list.get(position).getIshairpacked().equals("0")) {
					Type = list.get(position).getIshairpacked();
					Userid = list.get(position).getUserid();
					Log.i("Tag", "Type1=" + list.get(position).getIshairpacked());

				} else {
					Type = list.get(position).getIshairpacked();
					Userid = list.get(position).getUserid();
					Log.i("Tag", "Type2=" + list.get(position).getIshairpacked());
				}
				vh.tv_cancel.setVisibility(View.GONE);
				vh.tv_pay.setVisibility(View.VISIBLE);
				vh.tv_pay.setText("再次预约");
				vh.tv_pay_info.setVisibility(View.VISIBLE);
				vh.tv_money.setVisibility(View.VISIBLE);
				vh.tv_pay.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						openReserveFragment(obj.getOrderID());
					}
				});
			} else if (State == 6) {
				vh.tv_cancel.setVisibility(View.GONE);
				vh.tv_pay.setVisibility(View.VISIBLE);
				vh.tv_pay.setText("评论");
				vh.tv_pay_info.setVisibility(View.VISIBLE);
				vh.tv_money.setVisibility(View.VISIBLE);
				vh.tv_pay.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						openEvaluationFragment(obj.getOrderID());
					}
				});

			} else {
				vh.tv_cancel.setVisibility(View.GONE);
				vh.tv_pay.setVisibility(View.GONE);
				vh.tv_pay_info.setVisibility(View.VISIBLE);
				vh.tv_money.setVisibility(View.VISIBLE);
			}
			RoundImageLoaderUtil.setErrImage(R.drawable.tx_man, R.drawable.tx_man, R.drawable.tx_man);
			RoundImageLoaderUtil.getInstance(activity, 100).loadImage(obj.getPhoto(), vh.iv_userphoto);
		}

		/**
		 * 
		 * @Title: openEvaluationFragment
		 * @Description: 打开评论界面
		 * @author: 欧阳
		 * @return: void
		 */
		public void openEvaluationFragment(String OrderID) {
			Intent intent = ReserveActivity.getIntent(activity, OrderEvaluationFragment.class.getName());
			Bundle bundle = new Bundle();
			bundle.putString("ID", OrderID);
			bundle.putString("From", OrderFragment.class.getSimpleName());
			intent.putExtras(bundle);
			startActivity(intent);
			// Fragment fragment = FragmentManagerUtils.getFragment(activity,
			// OrderEvaluationFragment.class.getName());
			// Bundle bundle = new Bundle();
			// bundle.putString("ID", OrderID);
			// fragment.setArguments(bundle);
			// FragmentManagerUtils.add(activity, R.id.content_frame, fragment,
			// true);
		}

		/**
		 * 
		 * @Title: openReserveFragment
		 * @Description: 打开预约界面
		 * @author: 欧阳
		 * @return: void
		 */
		public void openReserveFragment(String OrderID) {

			if (Type.equals("0")) {
				Intent intent = ReserveActivity.getIntent(activity, ReserveFragment.class.getName());
				Bundle bundle = new Bundle();
				bundle.putString("OrderID", OrderID);
				bundle.putString("From", OrderFragment.class.getSimpleName());
				intent.putExtras(bundle);
				startActivity(intent);
			} else {
				Intent intent = ReserveActivity.getIntent(activity, ReserveFreeFragment.class.getName());
				Bundle bundle = new Bundle();
				bundle.putString("OrderID", OrderID);
				bundle.putString("From", OrderFragment.class.getSimpleName());
				bundle.putString("Userid", Userid);
				intent.putExtras(bundle);
				startActivity(intent);
			}

			// Fragment fragment = FragmentManagerUtils.getFragment(activity,
			// ReserveFragment.class.getName());
			// Bundle bundle = new Bundle();
			// bundle.putString("ID", ID);
			// bundle.putString("From", OrderFragment.class.getSimpleName());
			// fragment.setArguments(bundle);
			// FragmentManagerUtils.add(activity, R.id.content_frame,
			// new ReserveFragment(), true);
		}

		/**
		 * 
		 * @Title: alertDialog
		 * @Description: 询问对话框
		 * @author: 欧阳
		 * @return: void
		 */
		protected void alertDialog(final String orderID, final String state, final int position) {
			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			builder.setMessage("是否取消该订单？");
			builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// 网络请求更新UI
					sendNetDetailRequest(orderID, state, position);

				}
			});
			builder.setNegativeButton("否", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			// 最重要的代码
			dialog = builder.show();

		}

		/**
		 * 
		 * @Title: sendNetDetailRequest
		 * @Description: 发送网络更新订单状态请求
		 * @author: 欧阳
		 * @return: void
		 */
		protected void sendNetDetailRequest(String orderId, String state, final int position) {
			if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
				ToastUtils.showShort(activity.getResources().getString(R.string.no_networks_found));
				return;
			}
			UpdateOrderStateRequest request = new UpdateOrderStateRequest(UserManager.getUserID() + "", orderId, state);
			UpdateOrderStateApi.UpdateOrderState(request, new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					BaseResponse result = BaseResponse.getBaseResponse(responseInfo.result);
					if (result == null) {
						return;
					}
					String[] split = result.getMsg().split("\\|");
					if ("1".equals(split[0])) {
						ToastUtils.showShort(split[1] + "");
					}
					if (result.getCode() == 1000) {
						ToastUtils.showShort("修改成功");
						list.get(position).setState("9");
						list.get(position).setStateName("已取消");
						notifyDataSetChanged();
					}
				}

				@Override
				public void onFailure(HttpException error, String msg) {
					ToastUtils.showShort("网络请求失败");
				}
			});

		}
	}

	static class ViewHolder {
		// @ViewInject(R.id.tv_ordernum)
		// TextView tv_ordernum;
		@ViewInject(R.id.tv_projectname)
		TextView tv_projectname;
		@ViewInject(R.id.tv_state)
		TextView tv_state;

		@ViewInject(R.id.tv_username)
		TextView tv_username;
		@ViewInject(R.id.tv_LevelName)
		TextView tv_LevelName;
		@ViewInject(R.id.tv_time)
		TextView tv_time;

		@ViewInject(R.id.tv_storeName)
		TextView tv_storeName;
		@ViewInject(R.id.tv_address)
		TextView tv_address;
		@ViewInject(R.id.tv_mobile)
		TextView tv_mobile;
		@ViewInject(R.id.tv_pay_info)
		TextView tv_pay_info;
		@ViewInject(R.id.tv_money)
		TextView tv_money;
		@ViewInject(R.id.tv_cancel)
		TextView tv_cancel;
		@ViewInject(R.id.tv_pay)
		TextView tv_pay;

		@ViewInject(R.id.iv_userphoto)
		ImageView iv_userphoto;
		@ViewInject(R.id.iv_call)
		ImageView iv_call;

		void findView(View convertView) {
			ViewUtils.inject(this, convertView);
		}
	}

	public int getScreenWidth() {
		DisplayMetrics metric = new DisplayMetrics();
		WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels;
		return width;
	}
}
