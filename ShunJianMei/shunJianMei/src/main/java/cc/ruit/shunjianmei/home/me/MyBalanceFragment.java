package cc.ruit.shunjianmei.home.me;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cc.ruit.shunjianmei.R;
import cc.ruit.shunjianmei.base.BaseActivity;
import cc.ruit.shunjianmei.base.BaseAdapter;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.net.api.MyBalanceApi;
import cc.ruit.shunjianmei.net.api.TradeRecordApi;
import cc.ruit.shunjianmei.net.request.MyBalanceRequest;
import cc.ruit.shunjianmei.net.request.TradeRecordRequest;
import cc.ruit.shunjianmei.net.response.MyBalanceResponse;
import cc.ruit.shunjianmei.net.response.TradeRecordResponse;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmei.util.FragmentManagerUtils;
import cc.ruit.shunjianmei.util.MyEventBus;
import cc.ruit.shunjianmei.util.view.EmptyView;
import cc.ruit.shunjianmei.util.view.EmptyView.State;
import cc.ruit.shunjianmei.util.view.FilterItemBean;
import cc.ruit.shunjianmei.util.view.FilterPopupwindow;
import cc.ruit.shunjianmei.util.view.FilterPopupwindow.OnItemSelecedListener;
import cc.ruit.utils.sdk.ToastUtils;
import cc.ruit.utils.sdk.http.NetWorkUtils;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.oruit.widget.loadingdialog.LoadingDailog;
import com.oruit.widget.title.TitleUtil;
import com.umeng.analytics.MobclickAgent;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshBase;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshBase.Mode;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshBase.OnRefreshListener2;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshListView;

import de.greenrobot.event.EventBus;

/**
 * @ClassName: MyBalanceFragment
 * @Description: 我的余额界面
 * @author: Johnny
 * @date: 2015年10月9日 下午8:38:26
 */
public class MyBalanceFragment extends BaseFragment implements OnClickListener,
		OnItemSelecedListener {

	@ViewInject(R.id.rl_state_mybalance)
	private RelativeLayout rl_state;// 按状态选择的布局
	@ViewInject(R.id.rl_date_mybalance)
	private RelativeLayout rl_date;// 按时间选择的布局
	@ViewInject(R.id.fl_container)
	private FrameLayout mFrameLayout;// ListView的父容器

	@ViewInject(R.id.tv_moneynum_mybalance)
	private TextView tv_moneynum;// 余额
	@ViewInject(R.id.tv_canwithdraw_mybalance)
	private TextView tv_canwithdraw;// 可提取余额
	@ViewInject(R.id.tv_charge_mybalance)
	private TextView tv_charge;// 充值按钮
	@ViewInject(R.id.tv_withdraw_mybalance)
	private TextView tv_withdraw;// 提现按钮
	@ViewInject(R.id.tv_state_mybalance)
	private TextView tv_state;// 按状态选择的文本
	@ViewInject(R.id.iv_state_mybalance)
	private TextView iv_state;// 按状态选择的下拉图标
	@ViewInject(R.id.line_state_mybalance)
	private View line_state;// 按状态选择项的横线
	@ViewInject(R.id.tv_date_mybalance)
	private TextView tv_date;// 按时间选择的文本
	@ViewInject(R.id.iv_date_mybalance)
	private TextView iv_date;// 按时间选择的下拉图标
	@ViewInject(R.id.line_date_mybalance)
	private View line_date;// 按时间选择项的横线
	@ViewInject(R.id.cover)
	private View cover;// 下拉菜单出现时的背景

	@ViewInject(R.id.pulltorefreshlistview)
	private PullToRefreshListView lv_refresh;// 列表

	private EmptyView ev;// 空载页
	private FilterPopupwindow popupWindow;// 下拉菜单

	private static final int STATE_TAG = 0;// 按状态筛选标识
	private static final int DATE_TAG = 1;// 按时间筛选标识

//	private String myBalance;// 我的余额
//	private String canWithDraw;// 可提取金额

	private List<FilterItemBean> stateList = new ArrayList<FilterItemBean>();// 按状态选择
	private List<FilterItemBean> dateList = new ArrayList<FilterItemBean>();// 按时间选择
	private Map<String, String> filterTexts = new HashMap<String, String>();// 储存筛选项文本
	private Map<String, String> filterIds = new HashMap<String, String>();// 储存筛选项Id

	List<TradeRecordResponse> msgList = new ArrayList<TradeRecordResponse>();

	private int pageIndex = 1;
	private String pageSize = "10";

	private List<String> chooseItem = new ArrayList<String>();// 筛选的选择项

	private MyBalanceAdapter mAdapter;

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("MyBalanceFragment"); // 统计页面
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MyBalanceFragment");
	}

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.mybalance_layout, null);
		ViewUtils.inject(this, view);
		initTitle();
		initPullToRefreshListView();
		initData();
		return view;
	}

	/**
	 * @Title: initTitle
	 * @Description: 标题初始化
	 * @author: Johnny
	 * @return: void
	 */
	private void initTitle() {
		TitleUtil title = new TitleUtil(view);
		title.iv_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean isBack = FragmentManagerUtils.back(getActivity(),
						R.id.content_frame);
				if (!isBack) {
					getActivity().finish();
				}
			}
		});
		title.iv_left.setImageResource(R.drawable.arrow_left_white);
		title.iv_left.setVisibility(View.VISIBLE);
		title.tv_title.setText("我的余额");
	}

	/**
	 * 
	 * @Title: initData
	 * @Description:
	 * @author: Johnny
	 * @return: void
	 */
	private void initData() {
		stateList.add(new FilterItemBean("0", "全部"));
		stateList.add(new FilterItemBean("1", "支付"));
		stateList.add(new FilterItemBean("2", "赔偿"));
		stateList.add(new FilterItemBean("3", "退款"));
		stateList.add(new FilterItemBean("4", "提现"));

		dateList.add(new FilterItemBean("0", "从最近开始"));
		dateList.add(new FilterItemBean("1", "从最远开始"));
		
		filterTexts.put("按状态选择", "状态");
		filterTexts.put("按照时间", "时间");

		filterIds.put("按状态", "0");
		filterIds.put("按时间", "0");

//		chooseItem.add("0");// 按状态筛选
//		chooseItem.add("0");// 按时间筛选

		EventBus.getDefault().register(this);
		
		mAdapter = new MyBalanceAdapter(activity, msgList);
		lv_refresh.setAdapter(mAdapter);
		lv_refresh.getRefreshableView().setDividerHeight(
				BaseActivity.dip2px(activity, 5));
		ev = new EmptyView(activity, new OnClickListener() {

			@Override
			public void onClick(View v) {
				LoadingDailog.show(activity, "数据加载中，请稍后");
				getTradeRecordData("" + pageIndex, filterIds.get("按状态"),
						filterIds.get("按时间"));
				getMyBalanceData();
			}
		});
		lv_refresh.setEmptyView(ev.getView());
		ev.setState(State.Loading);
		getTradeRecordData("" + pageIndex, filterIds.get("按状态"),
				filterIds.get("按时间"));
//		getTradeRecordData("" + pageIndex, chooseItem.get(0), chooseItem.get(0));
		getMyBalanceData();
	}

	/**
	 * 
	 * @Title: initPullToRefreshListView
	 * @Description: 初始化下拉刷新菜单
	 * @author: Johnny
	 * @return: void
	 */
	private void initPullToRefreshListView() {

		lv_refresh.setMode(Mode.BOTH);
		lv_refresh.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {

				pageIndex = 1;
				getTradeRecordData("" + pageIndex, filterIds.get("按状态"),
						filterIds.get("按时间"));
//				getTradeRecordData("" + pageIndex, chooseItem.get(0),
//						chooseItem.get(0));
				getMyBalanceData();

			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {

				getTradeRecordData("" + pageIndex, filterIds.get("按状态"),
						filterIds.get("按时间"));
//				getTradeRecordData("" + pageIndex, chooseItem.get(0),
//						chooseItem.get(0));
				getMyBalanceData();
			}
		});
	}

	/**
	 * 
	 * @Title: getTradeRecordData
	 * @Description: 请求接口
	 * @author: Johnny
	 * @return: void
	 */
	private void getTradeRecordData(final String pageStart, String type,
			String sort) {
		if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
			ToastUtils.showShort(activity.getResources().getString(
					R.string.no_networks_found));
			lv_refresh.onRefreshComplete();
			return;
		}
		int userId = UserManager.getUserID();
		TradeRecordRequest request = new TradeRecordRequest("" + userId,
				pageSize, pageStart, type, sort);
		TradeRecordApi.tradeRecord(request, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				LoadingDailog.dismiss();
				lv_refresh.onRefreshComplete();
				BaseResponse result = BaseResponse
						.getBaseResponse(responseInfo.result);
				if (result == null) {
					return;
				}
				String[] split = result.getMsg().split("\\|");
				if ("1".equals(split[0])) {
					ToastUtils.showShort(split[1] + "");
				}
				if (result.getCode() == 1000) {
					List<TradeRecordResponse> response = TradeRecordResponse
							.getclazz2(result.getData());
					resultTradeRecordHanlder(response, pageStart);
				} else if (result.getCode() == 2100) {
					if (pageIndex==1) {
						ev.setNullState();
					}
				} else {
					ev.setErrState();
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				MobclickAgent.onEvent(activity, "mybalance_failure");
				LoadingDailog.dismiss();
				lv_refresh.onRefreshComplete();
				ToastUtils.showShort(activity.getResources().getString(
						R.string.request_failure));

			}
		});

	}

	/**
	 * @Title: resultTradeRecordHanlder
	 * @Description: 结果处理
	 * @author: lee
	 * @param listcontrolSignUp
	 * @return: void
	 */
	void resultTradeRecordHanlder(List<TradeRecordResponse> info,
			String pageStart) {
		if (info == null) {
			LogUtils.e("MyBalanceResponse err");
			return;
		}

		if ("1".equals(pageStart)) {
			msgList.clear();
		}
		if (!"1".equals(pageStart) && info.size() <= 0) {
			return;
		}
		msgList.addAll(info);
		if (msgList.size() > 0) {
			ev.setVisible(false);
		} else {
			ev.setNullState();
		}
		mAdapter.notifyDataSetChanged();
		pageIndex++;
		
		if ("1".equals(pageStart)) {
			//重新请求数据时将列表返回顶部,需要在notifyDataSetChanged()方法后调用
			lv_refresh.getRefreshableView().setSelection(0);
		}
		

	}

	/**
	 * 
	 * @Title: getMyBalanceData
	 * @Description: 请求接口
	 * @author: Johnny
	 * @return: void
	 */
	private void getMyBalanceData() {
		if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
			ToastUtils.showShort(activity.getResources().getString(
					R.string.no_networks_found));
			LoadingDailog.dismiss();
			return;
		}
		
		LoadingDailog.show(activity, "数据加载中，请稍后");
		int userId = UserManager.getUserID();
		MyBalanceRequest request = new MyBalanceRequest("" + userId);
		MyBalanceApi.myBalance(request, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				LoadingDailog.dismiss();
				lv_refresh.onRefreshComplete();
				BaseResponse result = BaseResponse
						.getBaseResponse(responseInfo.result);
				if (result == null) {
					return;
				}
				String[] split = result.getMsg().split("\\|");
				if ("1".equals(split[0])) {
					ToastUtils.showShort(split[1] + "");
				}
				if (result.getCode() == 1000) {
					 List<MyBalanceResponse> response = MyBalanceResponse.getclazz2(result.getData());
//					MyBalanceResponse response = MyBalanceResponse
//							.getclazz(result.getData());
					resultMyBalanceHanlder(response);
				} else {
					tv_moneynum.setText("0.0");
					tv_canwithdraw.setText("¥" + 0.00);
//					myBalance = "0.0";
//					canWithDraw = "0.0";
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				MobclickAgent.onEvent(activity, "mybalance_failure");
				lv_refresh.onRefreshComplete();
				ToastUtils.showShort(activity.getResources().getString(
						R.string.request_failure));

			}
		});

	}

	/**
	 * @Title: resultMyBalanceHanlder
	 * @Description: 结果处理
	 * @author: lee
	 * @param listcontrolSignUp
	 * @return: void
	 */
	void resultMyBalanceHanlder(List<MyBalanceResponse> response) {
		if (response == null) {
			LogUtils.e("MyBalanceResponse err");
			return;
		}
		tv_moneynum.setText(response.get(0).getTotal());
		tv_canwithdraw.setText("¥"+response.get(0).getAmount());

	}

	/**
	 * 
	 * @Title: onClick
	 * @Description: 定义点击事件
	 * @param v
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	@OnClick({ R.id.rl_state_mybalance, R.id.rl_date_mybalance,
			R.id.tv_withdraw_mybalance })
	public void onClick(View v) {
		// ----------初始化筛选框-------------
		if (popupWindow == null) {
			popupWindow = new FilterPopupwindow(activity, mFrameLayout);
			popupWindow.setOnItemClickListener(this);
		}
		switch (v.getId()) {
		// 按状态选择的布局
		case R.id.rl_state_mybalance:
			if (popupWindow.isCurrentShowing(STATE_TAG)) {
				popupWindow.dismiss();
			} else {
				popupWindow.show(stateList, STATE_TAG);
			}
			break;

		// 按时间选择的布局
		case R.id.rl_date_mybalance:
			if (popupWindow.isCurrentShowing(DATE_TAG)) {
				popupWindow.dismiss();
			} else {
				popupWindow.show(dateList, DATE_TAG);
			}
			break;
		// 提现按钮
		case R.id.tv_withdraw_mybalance:
			Fragment fragment = new WithdrawFragment();
//			Bundle bundle = new Bundle();
//			bundle.putString("MyBalance", myBalance);
//			bundle.putString("CanWithDraw", canWithDraw);
//			fragment.setArguments(bundle);
			FragmentManagerUtils.add(activity, R.id.content_frame, fragment,
					true);

			break;

		default:

			break;
		}
	}

	/**
	 * 
	 * @ClassName: MyBalanceAdapter
	 * @Description: TODO
	 * @author: Johnny
	 * @date: 2015年10月14日 下午9:25:14
	 */
	class MyBalanceAdapter extends BaseAdapter {

		private Context context;
		private List<TradeRecordResponse> list;

		public MyBalanceAdapter(Context context, List<TradeRecordResponse> list) {
			super(context, list);
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
		public TradeRecordResponse getItem(int position) {
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
				convertView = LayoutInflater.from(context).inflate(
						R.layout.mybalance_listview_item_layout, null);
				vh.findView(convertView);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			// 得到数据和绑定数据
			setLablel(position, vh);
			return convertView;
		}

		/**
		 * @Title: setLable
		 * @Description: TODO
		 * @author: Johnny
		 * @param position
		 * @param vh
		 * @return: void
		 */
		private void setLablel(int position, ViewHolder vh) {
			TradeRecordResponse obj = getItem(position);
			vh.tv_ordercode.setText(obj.getOrderCode());
			vh.tv_paytime.setText(obj.getPayTime());
			vh.tv_amount.setText("¥" + obj.getAmount());
			vh.tv_remark.setText(obj.getRemark());
			vh.tv_reservetime.setText(obj.getAppointmentTime());
		}

	}

	class ViewHolder {

		@ViewInject(R.id.tv_ordercode)
		TextView tv_ordercode;// 订单编号
		@ViewInject(R.id.tv_paytime)
		TextView tv_paytime;// 订单编号
		@ViewInject(R.id.tv_amount)
		TextView tv_amount;// 金额
		@ViewInject(R.id.tv_remark)
		TextView tv_remark;// 交易说明
		@ViewInject(R.id.tv_reservetime)
		TextView tv_reservetime;// 预约时间

		void findView(View convertView) {
			ViewUtils.inject(this, convertView);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id, int tag) {

		popupWindow.setSelectedPosition(position);
		pageIndex = 1;
		ev.setState(State.Loading);
		switch (tag) {
		// 按状态筛选
		case 0:
			String stateId = stateList.get(position).getId();
			String stateText = stateList.get(position).getName();
			filterTexts.put("按状态", stateText);
			filterIds.put("按状态", stateId);
//			popupWindow.setSelectedPosition(position);
			setTabText(tag);
//			pageIndex = 1;
//			ev.setState(State.Loading);
//			getTradeRecordData("" + pageIndex, filterIds.get("按状态"),
//					filterIds.get("按时间"));
			break;
		// 时间筛选
		case 1:
			String dateId = dateList.get(position).getId();
			String dateText = dateList.get(position).getName();
			filterTexts.put("按时间", dateText);
			filterIds.put("按时间", dateId);
//			popupWindow.setSelectedPosition(position);
//			pageIndex = 1;
//			ev.setState(State.Loading);
			setTabText(tag);
//			getTradeRecordData("" + pageIndex, filterIds.get("按状态"),
//					filterIds.get("按时间"));
			break;
		}
		getTradeRecordData("" + pageIndex, filterIds.get("按状态"),
				filterIds.get("按时间"));
	}

	/**
	 * 
	 * @Title: setTabText
	 * @Description: 设置选项栏的文本颜色
	 * @author: Johnny
	 * @return: void
	 */
	private void setTabText(int tag) {
		if (filterTexts.get("按状态") != null && tag == 0) {
			tv_state.setText(filterTexts.get("按状态"));
		}
		if (filterTexts.get("按时间") != null && tag == 1) {
			tv_date.setText(filterTexts.get("按时间"));
		}
	}
	
	/**
	 * 
	 * @Title: onEventMainThread
	 * @Description: 接收EventBus消息
	 * @author: Johnny
	 * @param event
	 * @return: void
	 */
	public void onEventMainThread(MyEventBus event){
		if(event.getmMsg().equals("MyBalance")){
			getMyBalanceData();
		}
	}
	
	/**
	 * 
	 * @Title: onDestroy
	 * @Description: 注销EventBus
	 * @see cc.ruit.shunjianmei.base.BaseFragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}
