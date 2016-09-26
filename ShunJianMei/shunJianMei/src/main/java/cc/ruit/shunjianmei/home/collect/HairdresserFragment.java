package cc.ruit.shunjianmei.home.collect;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import cc.ruit.shunjianmei.R;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.home.hairdressert.HairDressertDetailsFragment;
import cc.ruit.shunjianmei.home.hairdressert.HairDressertFragment;
import cc.ruit.shunjianmei.home.hairdressert.HairdresserListAdapter;
import cc.ruit.shunjianmei.home.hairdressert.HairdresserListAdapter.Type;
import cc.ruit.shunjianmei.home.me.MyBalanceActivity;
import cc.ruit.shunjianmei.home.me.MyBalanceFragment;
import cc.ruit.shunjianmei.home.me.coupon.CouponActivity;
import cc.ruit.shunjianmei.home.me.coupon.CouponFragment;
import cc.ruit.shunjianmei.net.api.FavoriteHairdresserListApi;
import cc.ruit.shunjianmei.net.request.FavoriteHairdresserListRequest;
import cc.ruit.shunjianmei.net.response.HairdresserListResponse;
import cc.ruit.shunjianmei.reserve.ReserveActivity;
import cc.ruit.shunjianmei.reserve.ReserveFragment;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmei.util.FragmentManagerUtils;
import cc.ruit.shunjianmei.util.ImageLoaderUtils;
import cc.ruit.shunjianmei.util.MyEventBus;
import cc.ruit.shunjianmei.util.RoundImageLoaderUtil;
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
 * @ClassName: HairdresserFragment
 * @Description: 收藏发型师界面
 * @author: HBM
 * @date: 2015年12月9日 下午8:38:26
 */
public class HairdresserFragment extends BaseFragment implements OnClickListener {

	@ViewInject(R.id.pulltorefreshlistview)
	private PullToRefreshListView lv_refresh;

	private List<HairdresserListResponse> msgList = new ArrayList<HairdresserListResponse>();
	private HairdresserListAdapter myAdapter;

	private String pageSize = "10";// 加载页数目
	private int pageIndex = 1;// 加载页
	EmptyView ev;// 空载页

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("HairdresserFragment"); // 统计页面
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("HairdresserFragment");
	}

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.hairdresser_layout, null);
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
		if (!TextUtils.isEmpty(bus.getmMsg()) && bus.getmMsg().equals("请刷新美发师收藏列表")) {
			initData();
		}
	}

	/**
	 * 
	 * @Title: initData
	 * @Description: 初始化数据
	 * @author: Johnny
	 * @return: void
	 */
	private void initData() {
		myAdapter = new HairdresserListAdapter(activity, msgList, Type.collect);
		lv_refresh.setAdapter(myAdapter);
		ev = new EmptyView(activity, new OnClickListener() {

			@Override
			public void onClick(View v) {
				pageIndex = 1;
				getData("" + pageIndex, pageSize);
			}
		});
		lv_refresh.setEmptyView(ev.getView());
		ev.setState(State.Loading);

		lv_refresh.setMode(Mode.BOTH);
		lv_refresh.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

				pageIndex = 1;
				getData("" + pageIndex, pageSize);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

				getData("" + pageIndex, pageSize);
			}
		});

		getData("" + pageIndex, pageSize);
	}

	/**
	 * 
	 * @Title: getData
	 * @Description: 请求接口
	 * @author: Johnny
	 * @return: void
	 */
	private void getData(final String pageStart, String pageSize) {
		if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
			ToastUtils.showShort(activity.getResources().getString(R.string.no_networks_found));
			lv_refresh.onRefreshComplete();
			return;
		}

		int userId = UserManager.getUserID();
		FavoriteHairdresserListRequest request = new FavoriteHairdresserListRequest("" + userId, pageStart, pageSize);
		Log.i("Tag", "request=" + request);
		FavoriteHairdresserListApi.favoriteHairdresserList(request, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				LoadingDailog.dismiss();
				lv_refresh.onRefreshComplete();
				BaseResponse result = BaseResponse.getBaseResponse(responseInfo.result);
				if (result == null) {
					return;
				}
				String[] split = result.getMsg().split("\\|");

				if ("1".equals(split[0])) {
					ToastUtils.showShort(split[1] + "");
				}
				if (result.getCode() == 1000) {
					List<HairdresserListResponse> data = HairdresserListResponse.getclazz2(result.getData());
					Log.i("Tag", " FavoriteHairdresserList=" + result.getData());
					if (data != null) {
						// 设置网络请求的数据
						resultHanlder(data, pageStart);
					} else {
						ToastUtils.showShort("请求数据异常");
					}
				} else if (result.getCode() == 2100) {
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
				ToastUtils.showShort(activity.getResources().getString(R.string.request_failure));

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
	void resultHanlder(List<HairdresserListResponse> info, String pageStart) {
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
	@OnClick({ R.id.rl_balance_me, R.id.rl_coupon_me, R.id.rl_pricelist_me, R.id.rl_favorite_me })
	public void onClick(View v) {
		Intent in;
		switch (v.getId()) {
		// 余额
		case R.id.rl_balance_me:
			in = MyBalanceActivity.getIntent(getActivity(), MyBalanceFragment.class.getName());
			startActivity(in);
			break;
		// 我的优惠劵
		case R.id.rl_coupon_me:
			in = CouponActivity.getIntent(getActivity(), CouponFragment.class.getName());
			startActivity(in);
			break;

		default:
			break;
		}
	}

	/**
	 * @ClassName: HairStyleListAdapter
	 * @Description: 收藏美发师s列表
	 * @author: Johnny
	 * @date: 2015年9月11日 下午3:09:35
	 */
	class FavoriteHairdresserListAdapter extends BaseAdapter {

		private Context context;
		private List<HairdresserListResponse> list;

		public FavoriteHairdresserListAdapter(Context context, List<HairdresserListResponse> list) {
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
		public HairdresserListResponse getItem(int position) {
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
				convertView = LayoutInflater.from(context).inflate(R.layout.item_hair_dressert_fragment_layout, null);
				vh.findView(convertView);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			// 得到数据和绑定数据
			setLabel(position, vh);
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
		private void setLabel(final int position, ViewHolder vh) {
			HairdresserListResponse obj = getItem(position);
			vh.tv_name.setText(obj.getName());
			vh.tv_LevelName.setText(obj.getLevelName());
			vh.tv_Star.setText(obj.getStar());
			vh.tv_ordernum.setText("接单量 " + obj.getOrderNum() + "次");
			float start = Float.parseFloat(obj.getScore());
			vh.tv_evaluate.setRating(start);
			ImageLoaderUtils.setErrImage(R.drawable.default_prc, R.drawable.default_prc, R.drawable.default_prc);
			ImageLoaderUtils.getInstance(activity).loadImage(obj.getImage(), vh.iv_Image);
			RoundImageLoaderUtil.setErrImage(R.drawable.default_prc, R.drawable.default_prc, R.drawable.default_prc);
			RoundImageLoaderUtil.getInstance(activity, 360).loadImage(obj.getPhoto(), vh.iv_photo);
			vh.iv_Image.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Fragment fragment = FragmentManagerUtils.getFragment(activity,
							HairDressertDetailsFragment.class.getName());
					Bundle bundle = new Bundle();
					bundle.putString("ID", list.get(position).getID());
					bundle.putString("Name", list.get(position).getName());
					fragment.setArguments(bundle);
					FragmentManagerUtils.add(activity, R.id.content_frame, fragment, true);
				}
			});
			vh.ll_detail.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Fragment fragment = FragmentManagerUtils.getFragment(activity,
							HairDressertDetailsFragment.class.getName());
					Bundle bundle = new Bundle();
					bundle.putString("ID", list.get(position).getID());
					bundle.putString("Name", list.get(position).getName());
					fragment.setArguments(bundle);
					FragmentManagerUtils.add(activity, R.id.content_frame, fragment, true);
				}
			});
			vh.ll_time.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// Fragment fragment = FragmentManagerUtils.getFragment(
					// activity, ReserveFragment.class.getName());
					// Bundle bundle = new Bundle();
					// bundle.putString("ID", list.get(position).getID());
					// bundle.putString("From",
					// HairDressertFragment.class.getSimpleName());
					// fragment.setArguments(bundle);
					// FragmentManagerUtils.add(activity, R.id.content_frame,
					// fragment, true);
					Intent intent = ReserveActivity.getIntent(context, ReserveFragment.class.getName());
					intent.putExtra("ID", list.get(position).getID());
					intent.putExtra("From", HairDressertFragment.class.getSimpleName());
					startActivity(intent);
				}
			});
		}
	}

	 static class ViewHolder {
	 @ViewInject(R.id.tv_hairstylist_master_name)
	 TextView tv_name;// 发型师名称
	 @ViewInject(R.id.tv_hairstylist_master_LevelName)
	 TextView tv_LevelName;// 级别名称
	 @ViewInject(R.id.tv_hairstylist_master_Star)
	 TextView tv_Star;// 星座
	 @ViewInject(R.id.tv_order_receiving_num)
	 TextView tv_ordernum;// 接单量
	 @ViewInject(R.id.ratingbar_all_evaluate)
	 RatingBar tv_evaluate;// 评论
	 @ViewInject(R.id.iv_hairstylist_Image)
	 ImageView iv_Image;// 发型图片
	 @ViewInject(R.id.image_hairstylist_photo)
	 ImageView iv_photo;// 头像
	 @ViewInject(R.id.ll_hairdressert_detail)
	 LinearLayout ll_detail;// 详情
	 @ViewInject(R.id.ll_hairdressert_time)
	 LinearLayout ll_time;// 预约
	 @ViewInject(R.id.iv_hairdressert_detail)
	 ImageView iv_detail;
	 @ViewInject(R.id.iv_hairdressert_time)
	 ImageView iv_time;
	 @ViewInject(R.id.tv_hairdressert_detail)
	 TextView tv_detail;
	 @ViewInject(R.id.tv_hairdressert_time)
	 TextView tv_time;
	 void findView(View convertView) {
	 ViewUtils.inject(this, convertView);
	 }
	 }

	/**
	 * 
	 * @Title: createRatingBar
	 * @Description:创建RatingBar
	 * @author: Johnny
	 * @param ll
	 * @param num
	 * @return: void
	 */
	 private void createRatingBar(LinearLayout ll, int num) {
	 ll.removeAllViews();
	 for (int i = 0; i < 5; i++) {
	 ImageView imageView = new ImageView(activity);
	
	 if (i < num) {
	 imageView.setImageResource(R.drawable.star_solid);
	 } else {
	 imageView.setImageResource(R.drawable.star_stroke);
	 }
	
	 int padding = (int) TypedValue.applyDimension(
	 TypedValue.COMPLEX_UNIT_DIP, 5, activity.getResources()
	 .getDisplayMetrics());
	 imageView.setPadding(padding, padding, padding, padding);
	 int width = (int) TypedValue.applyDimension(
	 TypedValue.COMPLEX_UNIT_DIP, 120, activity.getResources()
	 .getDisplayMetrics()) / 5;
	 ll.setGravity(Gravity.CENTER_VERTICAL);
	 ll.addView(imageView, width, width);
	 }
	 }
}
