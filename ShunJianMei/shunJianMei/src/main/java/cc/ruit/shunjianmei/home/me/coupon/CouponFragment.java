package cc.ruit.shunjianmei.home.me.coupon;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import cc.ruit.shunjianmei.R;
import cc.ruit.shunjianmei.base.BaseActivity;
import cc.ruit.shunjianmei.base.BaseAdapter;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.home.me.MyBalanceActivity;
import cc.ruit.shunjianmei.home.me.MyBalanceFragment;
import cc.ruit.shunjianmei.net.api.CouponListApi;
import cc.ruit.shunjianmei.net.request.CouponListRequest;
import cc.ruit.shunjianmei.net.response.CouponListResponse;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmei.util.FragmentManagerUtils;
import cc.ruit.shunjianmei.util.view.EmptyView;
import cc.ruit.shunjianmei.util.view.EmptyView.State;
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
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshListView;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshBase.Mode;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshBase.OnRefreshListener2;

/**
 * @ClassName: CouponFragment
 * @Description: 优惠劵列表
 * @author: Johnny
 * @date: 2015年10月9日 下午8:38:26
 */
public class CouponFragment extends BaseFragment implements OnClickListener {

	@ViewInject(R.id.pulltorefreshlistview)
	private PullToRefreshListView lv_refresh;

	private String pageSize = "10";
	private int pageIndex = 1;

	private List<CouponListResponse> msgList = new ArrayList<CouponListResponse>();
	private CouponListAdapter myAdapter;
	private EmptyView ev;// 空载页

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("CouponFragment"); // 统计页面
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("CouponFragment");
	}

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.coupon_layout, null);
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
		title .tv_title.setText("我的优惠劵");
	}

	private void initData() {
		myAdapter = new CouponListAdapter(activity, msgList);
		lv_refresh.getRefreshableView().setDividerHeight(
				BaseActivity.dip2px(activity, 10));
		lv_refresh.setAdapter(myAdapter);
		ev = new EmptyView(activity, new OnClickListener() {

			@Override
			public void onClick(View v) {
				LoadingDailog.show(activity, "数据加载中，请稍等");
				getData(pageSize, pageIndex);
			}
		});
		lv_refresh.setEmptyView(ev.getView());
		ev.setState(State.Loading);

		getData(pageSize, pageIndex);
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
				getData(pageSize,  pageIndex);

			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				getData(pageSize,  pageIndex);
			}
		});
	}

	/**
	 * 
	 * @Title: getData
	 * @Description: 请求接口
	 * @author: Johnny
	 * @return: void
	 */
	private void getData(String pageSize, final int pageIndex) {
		if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
			ToastUtils.showShort(activity.getResources().getString(
					R.string.no_networks_found));
			return;
		}

		int userId = UserManager.getUserID();
		LoadingDailog.show(activity, "数据加载中，请稍后");
		CouponListRequest request = new CouponListRequest(""+userId,
				pageSize, ""+pageIndex);
		CouponListApi.couponList(request, new RequestCallBack<String>() {

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
					List<CouponListResponse> data = CouponListResponse
							.getclazz2(result.getData());
					if (data != null) {
						// 设置网络请求的数据
						resultHanlder(data, pageIndex);
					}
				} else if (result.getCode() == 2100) {
					if (pageIndex==1) {
						ev.setNullState();
						ev.setMessage("您当前没有优惠券");
					}
				} else {
					ToastUtils.showShort("请求数据异常");
				}

			}

			@Override
			public void onFailure(HttpException error, String msg) {
				MobclickAgent.onEvent(activity, "couponfragment_failure");
				LoadingDailog.dismiss();
				lv_refresh.onRefreshComplete();
				ev.setErrState();
				ToastUtils.showShort(activity.getResources().getString(
						R.string.request_failure));

			}
		});

	}

	/**
	 * @Title: resultHanlder
	 * @Description: 结果处理
	 * @author: Johnny
	 * @param listcontrolSignUp
	 * @return: void
	 */
	void resultHanlder(List<CouponListResponse> info, int pageIndex) {
		if (info == null) {
			LogUtils.e("CouponListResponse err");
			return;
		}

		if (1==pageIndex) {
			msgList.clear();
		}
		if (1==pageIndex && info.size() <= 0) {
			return;
		}
		msgList.addAll(info);
		if (msgList.size() > 0) {
			ev.setDataState();;
		} else {
			ev.setNullState();
			ev.setMessage("您当前没有优惠券");
		}
		this.pageIndex++;
		myAdapter.notifyDataSetChanged();
	}

	/**
	 * 
	 * @Title: onClick
	 * @Description: 定义点击事件
	 * @param v
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	@OnClick({ R.id.rl_balance_me, R.id.rl_coupon_me, R.id.rl_pricelist_me,
			R.id.rl_favorite_me })
	public void onClick(View v) {
		Intent in;
		switch (v.getId()) {
		// 余额
		case R.id.rl_balance_me:
			in = MyBalanceActivity.getIntent(getActivity(),
					MyBalanceFragment.class.getName());
			startActivity(in);
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
	class CouponListAdapter extends BaseAdapter {

		private Context context;
		private List<CouponListResponse> list;

		public CouponListAdapter(Context context, List<CouponListResponse> list) {
			super(context, list);
			this.context = context;
			this.list = list;
		}

//		@Override
//		public void notifyDataSetChanged() {
//			super.notifyDataSetChanged();
//			if (list == null || list.size() <= 0) {
//				ev.setNullState();
//				ev.setMessage("您当前没有优惠券");
//			}
//		}

		@Override
		public int getCount() {
			return list == null ? 0 : list.size();
		}

		@Override
		public CouponListResponse getItem(int position) {
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
						R.layout.coupon_listview_item_layout, null);
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
			CouponListResponse obj = getItem(position);
			vh.tv_authority.setText(obj.getName());
			vh.tv_money.setText("¥" + obj.getAmount());
			vh.tv_starttime.setText(obj.getStartTime());
			vh.tv_endtime.setText(obj.getEndTime());
		}

	}

	class ViewHolder {

		@ViewInject(R.id.tv_authority)
		TextView tv_authority;// 优惠劵名称
		@ViewInject(R.id.tv_money)
		TextView tv_money;// 优惠金额
		@ViewInject(R.id.tv_starttime)
		TextView tv_starttime;// 生效时间
		@ViewInject(R.id.tv_endtime)
		TextView tv_endtime;// 过期时间

		void findView(View convertView) {
			ViewUtils.inject(this, convertView);
		}
	}

}
