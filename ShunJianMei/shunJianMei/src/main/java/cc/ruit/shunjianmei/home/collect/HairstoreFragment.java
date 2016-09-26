package cc.ruit.shunjianmei.home.collect;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cc.ruit.shunjianmei.R;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.home.me.MyBalanceActivity;
import cc.ruit.shunjianmei.home.me.MyBalanceFragment;
import cc.ruit.shunjianmei.home.me.coupon.CouponActivity;
import cc.ruit.shunjianmei.home.me.coupon.CouponFragment;
import cc.ruit.shunjianmei.home.store.StoreDetailFragment;
import cc.ruit.shunjianmei.home.store.StoreListAdapter;
import cc.ruit.shunjianmei.net.api.FavoriteStoreListApi;
import cc.ruit.shunjianmei.net.request.FavoriteStoreListRequest;
import cc.ruit.shunjianmei.net.response.FavoriteStoreListResponse;
import cc.ruit.shunjianmei.net.response.StoreListResponse;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmei.util.FragmentManagerUtils;
import cc.ruit.shunjianmei.util.MyEventBus;
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
import com.umeng.analytics.MobclickAgent;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshBase;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshBase.Mode;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshBase.OnRefreshListener2;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshListView;

import de.greenrobot.event.EventBus;

/**
 * @ClassName: HairstoreFragment
 * @Description: 收藏店铺页
 * @author: HBM
 * @date: 2015年12月9日 下午8:38:26
 */
public class HairstoreFragment extends BaseFragment implements OnClickListener {

	@ViewInject(R.id.pulltorefreshlistview)
	private PullToRefreshListView lv_refresh;

	private String longitude = ""+UserManager.getlongitude();// 经度
	private String latitude = ""+UserManager.getLatitude();// 纬度
	private String pageSize = "10";// 加载页数目
	private int pageIndex = 1;// 加载页
	private List<StoreListResponse> msgList;
	private StoreListAdapter myAdapter;
	EmptyView ev;// 空载页

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("HairstoreFragment"); // 统计页面
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("HairstoreFragment");
	}

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.hairstore_layout, null);
		ViewUtils.inject(this, view);
		initData();
		initEventBus();
		return view;
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
		if (!TextUtils.isEmpty(bus.getmMsg()) && bus.getmMsg().equals("请刷新美发店收藏列表")) {
			initData();
		}
	}
	/**
	 * 
	 * @Title: initData
	 * @Description:
	 * @author: Johnny
	 * @return: void
	 */
	private void initData() {
		msgList = new ArrayList<StoreListResponse>();
		myAdapter = new StoreListAdapter(activity, msgList);
		lv_refresh.setAdapter(myAdapter);
		ev = new EmptyView(activity, new OnClickListener() {

			@Override
			public void onClick(View v) {
				pageIndex=1;
				getData(longitude, latitude, "" + pageIndex, pageSize);
			}
		});
		lv_refresh.setEmptyView(ev.getView());
		
		lv_refresh.setMode(Mode.BOTH);
		lv_refresh.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {

				pageIndex = 1;
				getData(longitude, latitude, "" + pageIndex, pageSize);
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {

				getData(longitude, latitude, "" + pageIndex, pageSize);
			}
		});

		lv_refresh.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Fragment fragment = FragmentManagerUtils.getFragment(activity, StoreDetailFragment.class.getName());
				Bundle bundle = new Bundle();
				String ID= msgList.get(position-1).getID();
				bundle.putString("ID", ID);
				fragment.setArguments(bundle);
				FragmentManagerUtils
						.add(activity, R.id.content_frame, fragment, true);
			}
		});
	
		ev.setState(State.Loading);		
		getData(longitude, latitude, "" + pageIndex, pageSize);
	}

	/**
	 * 
	 * @Title: getData
	 * @Description: 请求接口
	 * @author: Johnny
	 * @return: void
	 */
	private void getData(String longitude, String latitude,
			final String pageStart, String pageSize) {
		if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
			ToastUtils.showShort(activity.getResources().getString(
					R.string.no_networks_found));
			lv_refresh.onRefreshComplete();
			return;
		}

		int userId = UserManager.getUserID();
		FavoriteStoreListRequest request = new FavoriteStoreListRequest(""+userId
				, longitude, latitude, pageStart, pageSize);
		FavoriteStoreListApi.favoriteStoreList(request,
				new RequestCallBack<String>() {

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
							List<StoreListResponse> data = StoreListResponse
									.getclazz2(result.getData());
							if (data != null) {
								// 设置网络请求的数据
								resultHanlder(data, pageStart);
							} else {
								ToastUtils.showShort("请求数据异常");
							}
						}else if(result.getCode() == 2100){
							if ("1".equals(pageStart)) {
								ev.setNullState();
							}
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						MobclickAgent.onEvent(activity, "login_failure");
						LoadingDailog.dismiss();
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
	void resultHanlder(List<StoreListResponse> info, String pageStart) {
		if (info == null) {
			LogUtils.e("MineResponse err");
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
		myAdapter.notifyDataSetChanged();
		pageIndex++;
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
		// 我的优惠劵
		case R.id.rl_coupon_me:
			in = CouponActivity.getIntent(getActivity(),
					CouponFragment.class.getName());
			startActivity(in);
			break;

		default:
			break;
		}
	}

	/**
	 * @ClassName: StoreListAdapter
	 * @Description: 店面列表
	 * @author: Johnny
	 * @date: 2015年9月11日 下午3:09:35
	 */
//	class StoreListAdapter extends BaseAdapter {
//
//		private Context context;
//		private List<FavoriteStoreListResponse> list;
//
//		public StoreListAdapter(Context context,
//				List<FavoriteStoreListResponse> list) {
//			this.context = context;
//			this.list = list;
//		}
//
//		@Override
//		public void notifyDataSetChanged() {
//			super.notifyDataSetChanged();
//			if (list == null || list.size() <= 0) {
//				ev.setNullState();
//			}
//		}
//
//		@Override
//		public int getCount() {
//			return list == null ? 0 : list.size();
//		}
//
//		@Override
//		public FavoriteStoreListResponse getItem(int position) {
//			return list.get(position);
//		}
//
//		@Override
//		public long getItemId(int position) {
//			return position;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			ViewHolder vh = null;
//			if (convertView == null) {
//				vh = new ViewHolder();
//				convertView = LayoutInflater.from(context).inflate(
//						R.layout.store_listview_item_layout, null);
//				vh.findView(convertView);
//				convertView.setTag(vh);
//			} else {
//				vh = (ViewHolder) convertView.getTag();
//			}
//			// 得到数据和绑定数据
//			setLabel(position, vh);
//			return convertView;
//		}
//
//		/**
//		 * @Title: setLable
//		 * @Description: TODO
//		 * @author: Johnny
//		 * @param position
//		 * @param vh
//		 * @return: void
//		 */
//		private void setLabel(int position, ViewHolder vh) {
//			FavoriteStoreListResponse obj = getItem(position);
//			vh.tv_name.setText(obj.getName());
//			vh.tv_distance.setText(obj.getDistance());
//			vh.tv_address.setText(obj.getAddress());
//			vh.tv_ordernum.setText(obj.getOrderNum() + "次");
//			vh.tv_parking.setText(obj.getCarNum() + "个停车位");
//
//			// ImageLoaderUtils.getInstance(activity).loadImage(obj.getPhoto(),
//			// vh.iv_photo);
//
//			vh.ratingbar.setRating(Float.parseFloat(obj.getScore()));
//		}
//	}
//
//	static class ViewHolder {
//		@ViewInject(R.id.tv_name_store)
//		TextView tv_name;// 店铺名称
//		@ViewInject(R.id.tv_distance_store)
//		TextView tv_distance;// 店铺距离
//		@ViewInject(R.id.tv_address_store)
//		TextView tv_address;// 店铺地址
//		@ViewInject(R.id.tv_ordernum_store)
//		TextView tv_ordernum;// 接单量
//		@ViewInject(R.id.tv_parking_store)
//		TextView tv_parking;// 停车位
//
//		@ViewInject(R.id.ratingbar_stroe)
//		RatingBar ratingbar;// 星级评分�?
//
//		@ViewInject(R.id.iv_photo_store)
//		ImageView iv_photo;// 店铺照片
//
//		void findView(View convertView) {
//			ViewUtils.inject(this, convertView);
//		}
//	}
//
//	/**
//	 * 
//	 * @Title: createRatingBar
//	 * @Description:创建RatingBar
//	 * @author: Johnny
//	 * @param ll
//	 * @param num
//	 * @return: void
//	 */
//	private void createRatingBar(LinearLayout ll, int num) {
//		ll.removeAllViews();
//		for (int i = 0; i < 5; i++) {
//			ImageView imageView = new ImageView(activity);
//
//			if (i < num) {
//				imageView.setImageResource(R.drawable.star_solid);
//			} else {
//				imageView.setImageResource(R.drawable.star_stroke);
//			}
//
//			int padding = (int) TypedValue.applyDimension(
//					TypedValue.COMPLEX_UNIT_DIP, 5, activity.getResources()
//							.getDisplayMetrics());
//			imageView.setPadding(padding, padding, padding, padding);
//			int width = (int) TypedValue.applyDimension(
//					TypedValue.COMPLEX_UNIT_DIP, 120, activity.getResources()
//							.getDisplayMetrics()) / 5;
//			ll.setGravity(Gravity.CENTER_VERTICAL);
//			ll.addView(imageView, width, width);
//		}
//	}

}
