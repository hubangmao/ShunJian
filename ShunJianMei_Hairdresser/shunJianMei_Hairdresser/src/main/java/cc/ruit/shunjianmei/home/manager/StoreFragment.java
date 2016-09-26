package cc.ruit.shunjianmei.home.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import cc.ruit.shunjianmei.base.BaseActivity;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.constants.Constant;
import cc.ruit.shunjianmei.net.api.StoreListApi;
import cc.ruit.shunjianmei.net.request.StoreListRequest;
import cc.ruit.shunjianmei.net.response.StoreListResponse;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmei.util.FragmentManagerUtils;
import cc.ruit.shunjianmei.util.ImageLoaderUtils;
import cc.ruit.shunjianmei.util.view.EmptyView;
import cc.ruit.shunjianmei.util.view.EmptyView.State;
import cc.ruit.shunjianmei.util.view.FilterItemBean;
import cc.ruit.shunjianmei.util.view.FilterPopupwindow;
import cc.ruit.shunjianmei.util.view.FilterPopupwindow.OnItemSelecedListener;
import cc.ruit.shunjianmeihairdresser.R;
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
import com.oruit.widget.messagedialog.MessageDialog;
import com.oruit.widget.title.TitleUtil;
import com.umeng.analytics.MobclickAgent;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshBase;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshBase.Mode;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshBase.OnRefreshListener2;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshListView;

/**
 * @ClassName: StoreFragment
 * @Description: 美发店列表
 * @author: Johnny
 * @date: 2015年10月14日 下午4:32:59
 */
public class StoreFragment extends BaseFragment implements OnClickListener, OnItemSelecedListener {

	@ViewInject(R.id.pulltorefreshlistview)
	private PullToRefreshListView lv_refresh;// 列表
	@ViewInject(R.id.fl_container)
	private FrameLayout fl;

	@ViewInject(R.id.tv_star)
	private TextView tv_star;// 环境星级
	@ViewInject(R.id.tv_order_num)
	private TextView tv_order;// 接单量
	@ViewInject(R.id.tv_distance)
	private TextView tv_distance;// 距离
	@ViewInject(R.id.tv_parking_space)
	private TextView tv_parking;// 停车位

	FilterPopupwindow popupWindow;// 下拉菜单

	private StoreListAdapter mAdapter;
	List<StoreListResponse> msgList = new ArrayList<StoreListResponse>();

	private String pageSize = "10";// 加载页数
	private int pageIndex = 1;// 加载页
	private String longitude = "" + UserManager.getlongitude();// 经度
	private String latitude = "" + UserManager.getLatitude();// 纬度
	private String cityID = "" + UserManager.getCityID();// 城市ID

	private int star = 0;
	private int order = 0;
	private int distance = 0;
	private int parking = 0;

	private EmptyView ev;// 空载页

	private List<FilterItemBean> starList = new ArrayList<FilterItemBean>();// 环境星级选项
	private List<FilterItemBean> orderList = new ArrayList<FilterItemBean>();// 接单量选项
	private List<FilterItemBean> distanceList = new ArrayList<FilterItemBean>();// 距离选项
	private List<FilterItemBean> parkingList = new ArrayList<FilterItemBean>();// 停车位选项
	private Map<String, String> filterTexts = new HashMap<String, String>();// 储存筛选项文本
	private Map<String, String> filterIds = new HashMap<String, String>();// 储存筛选项Id

	private static final int STAR_TAG = 0;// 环境星级标识
	private static final int ORDER_TAG = 1;// 接单量标识
	private static final int DISTANCE_TAG = 2;// 距离标识
	private static final int PARKING_TAG = 3;// 停车位标识
	String type = "1";// 筛选类型

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("StoreManagement"); // 统计页面
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("StoreManagement");
	}

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.store_manager_layout, null);
		ViewUtils.inject(this, view);
		initTitle();
		initData();
		initPullToRefreshListView();
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
				boolean isBack = FragmentManagerUtils.back(getActivity(), R.id.content_frame);
				if (!isBack) {
					getActivity().finish();
				}
			}
		});
		title.iv_left.setImageResource(R.drawable.back);
		title.iv_left.setVisibility(View.VISIBLE);
		// title.iv_right.setImageResource(R.drawable.store_service_icon);
		//
		// int padding = (int)
		// TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 11,
		// getResources().getDisplayMetrics());
		// title.iv_right.setPadding(padding, padding, padding, padding);
		// title.iv_right.setVisibility(View.VISIBLE);
		//
		// title.iv_right.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Util.openTelDall(activity, Constant.serverPhone);
		//
		// }
		// });

		title.tv_title.setText("美发店");
	}

	/**
	 * @Title: initData
	 * @Description: 初始化数据
	 * @author: Johnny
	 * @return: void
	 */
	private void initData() {

		starList.add(new FilterItemBean("1", "从高到低"));
		starList.add(new FilterItemBean("0", "从低到高"));

		orderList.add(new FilterItemBean("1", "从多到少"));
		orderList.add(new FilterItemBean("0", "从少到多"));

		distanceList.add(new FilterItemBean(null, "从近到远"));// 此处不需要向接口传参，所以写为定

		parkingList.add(new FilterItemBean("1", "从多到少"));
		parkingList.add(new FilterItemBean("0", "从少到多"));

		filterTexts.put("环境星级", "从高到低");
		filterTexts.put("接单量", "从多到少");
		filterTexts.put("距离", "从近到远");
		filterTexts.put("停车位", "从多到少");

		filterIds.put("环境星级", "1");
		filterIds.put("接单量", "1");
		filterIds.put("停车位", "1");

		mAdapter = new StoreListAdapter(activity, msgList);
		lv_refresh.setAdapter(mAdapter);
		lv_refresh.getRefreshableView().setDividerHeight(BaseActivity.dip2px(activity, 5));
		ev = new EmptyView(activity, new OnClickListener() {

			@Override
			public void onClick(View v) {
				// LoadingDailog.show(activity, "数据加载中，请稍候");
				ev.setState(State.Loading);
				pageIndex = 1;
				getData(filterIds.get("环境星级"), filterIds.get("接单量"), filterIds.get("停车位"), type, "" + pageIndex);
			}
		});
		lv_refresh.setEmptyView(ev.getView());
		ev.setState(State.Loading);

		getData(filterIds.get("环境星级"), filterIds.get("接单量"), filterIds.get("停车位"), type, "" + pageIndex);
	}

	private void initPullToRefreshListView() {

		lv_refresh.setMode(Mode.BOTH);
		lv_refresh.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				pageIndex = 1;
				getData(filterIds.get("环境星级"), filterIds.get("接单量"), filterIds.get("停车位"), type, "" + pageIndex);

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

				getData(filterIds.get("环境星级"), filterIds.get("接单量"), filterIds.get("停车位"), type, "" + pageIndex);
			}
		});

		lv_refresh.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Fragment fragment = FragmentManagerUtils.getFragment(activity, StoreDetailFragment.class.getName());
				Bundle bundle = new Bundle();
				String ID = msgList.get(position - 1).getID();
				bundle.putString("ID", ID);
				fragment.setArguments(bundle);
				FragmentManagerUtils.add(activity, R.id.content_frame, fragment, true);
			}
		});
	}

	/**
	 * @Title: getPoints
	 * @Description: 获取数据
	 * @author: Johnny
	 * @return: void
	 */
	public void getData(String score, String order, String parking, String type, final String pageStart) {
		if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
			ToastUtils.showShort(activity.getResources().getString(R.string.no_networks_found));
			LoadingDailog.dismiss();
			lv_refresh.onRefreshComplete();
			ev.setErrState();
			// new LoadingViewUtil(view).hint();
			return;
		}
		StoreListRequest request = new StoreListRequest("" + UserManager.getUserID(), cityID, score, order, longitude,
				latitude, parking, type, "" + pageStart, "" + pageSize);
		StoreListApi.storeList(request, new RequestCallBack<String>() {

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
					List<StoreListResponse> response = StoreListResponse.getclazz2(result.getData());
					resultHanlder(response, "" + pageStart);
				} else if (result.getCode() == 2100) {
					if ("1".equals(pageStart)) {
						ev.setNullState();
					}
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
	 * @author: Johnny
	 * @param listcontrolSignUp
	 * @return: void
	 */
	void resultHanlder(List<StoreListResponse> info, String pageStart) {
		if (info == null) {
			LogUtils.e("StoreListResponse err");
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

	}

	/**
	 * 
	 * @Title: onClick
	 * @Description: 定义点击事件
	 * @param v
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	@OnClick({ R.id.ll_star, R.id.ll_order, R.id.ll_distance, R.id.ll_parking })
	public void onClick(View v) {
		// ----------初始化筛选框-------------
		if (popupWindow == null) {
			popupWindow = new FilterPopupwindow(activity, fl);
			popupWindow.setOnItemClickListener(this);
		}
		switch (v.getId()) {
		// 环境星级
		case R.id.ll_star:
			if (popupWindow.isCurrentShowing(STAR_TAG)) {
				popupWindow.dismiss();
			} else {
				popupWindow.setSelectedPosition(star);
				popupWindow.show(starList, STAR_TAG);
			}

			break;
		// 接单
		case R.id.ll_order:
			if (popupWindow.isCurrentShowing(ORDER_TAG)) {
				popupWindow.dismiss();
			} else {
				popupWindow.setSelectedPosition(order);
				popupWindow.show(orderList, ORDER_TAG);
			}
			break;
		// 距离
		case R.id.ll_distance:
			if (popupWindow.isCurrentShowing(DISTANCE_TAG)) {
				popupWindow.dismiss();
			} else {
				popupWindow.setSelectedPosition(distance);
				popupWindow.show(distanceList, DISTANCE_TAG);
			}
			break;
		// 停车
		case R.id.ll_parking:
			if (popupWindow.isCurrentShowing(PARKING_TAG)) {
				popupWindow.dismiss();
			} else {
				popupWindow.setSelectedPosition(parking);
				popupWindow.show(parkingList, PARKING_TAG);
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 
	 * @Title: showDialog
	 * @Description: 当用户点击联系电话时，弹出此对话框
	 * @author: Johnny
	 * @return: void
	 */
	private void showPhoneDialog(String message, int flag) {
		final MessageDialog dialog = new MessageDialog(activity);
		dialog.setMessage(message);
		dialog.getOkButton().setText("确定");
		dialog.getOkButton().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				callNumber();
				dialog.dismiss();
			}

		});
		if (flag == 1) {
			dialog.getCancelButton().setText("取消");
			dialog.getCancelButton().setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
		} else {
			dialog.setCancelButtonGone(true);
		}
		dialog.show();
	}

	/**
	 * 
	 * @Title: callNumber
	 * @Description: 调用系统拨号功能
	 * @author: Johnny
	 * @return: void
	 */
	private void callNumber() {

		try {
			Uri uri = Uri.parse("tel:" + "134-3955-3792");
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_DIAL);
			intent.setData(uri);
			startActivity(intent);
		} catch (Exception e) {
			showPhoneDialog("无法拨打电话", 2);
		}
	}

	/**
	 * @ClassName: StoreListAdapter
	 * @Description: 店面列表
	 * @author: Johnny
	 * @date: 2015�?9�?11�? 下午3:09:35
	 */
	class StoreListAdapter extends BaseAdapter {

		private Context context;
		private List<StoreListResponse> list;

		public StoreListAdapter(Context context, List<StoreListResponse> list) {
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
		public StoreListResponse getItem(int position) {
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
				convertView = LayoutInflater.from(context).inflate(R.layout.store_listview_item_layout, null);
				vh.findView(convertView);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			// 得到数据和绑定次数
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
		private void setLabel(int position, ViewHolder vh) {
			StoreListResponse obj = getItem(position);
			vh.tv_name.setText(obj.getName());
			vh.tv_distance.setText(obj.getDistance() + "km");
			vh.tv_distance.setVisibility(View.GONE);
			vh.tv_address.setText(obj.getAddress());
			vh.tv_ordernum.setText(obj.getOrderNum() + "次");
			vh.tv_parking.setText("停车位" + obj.getCarNum() + "个");
			if (obj.getIstype().equals("0")) {
				vh.img_type.setBackgroundResource(R.drawable.master_deep);
			} else {
				vh.img_type.setBackgroundResource(R.drawable.master_free);
			}
			ImageLoaderUtils.setErrImage(R.drawable.default_prc, R.drawable.default_prc, R.drawable.default_prc);
			ImageLoaderUtils.getInstance(activity).loadImage(obj.getPhoto(), vh.iv_photo);

			vh.ratingbar.setRating(Float.parseFloat(obj.getScore()));
		}
	}

	static class ViewHolder {
		@ViewInject(R.id.tv_name_store)
		TextView tv_name;// 店铺名称
		@ViewInject(R.id.tv_distance_store)
		TextView tv_distance;// 店铺距离
		@ViewInject(R.id.tv_address_store)
		TextView tv_address;// 店铺地址
		@ViewInject(R.id.tv_ordernum_store)
		TextView tv_ordernum;// 接单�?
		@ViewInject(R.id.tv_parking_store)
		TextView tv_parking;// 停车�?

		@ViewInject(R.id.ratingbar_stroe)
		RatingBar ratingbar;// 星级评分�?

		@ViewInject(R.id.iv_photo_store)
		ImageView iv_photo;// 店铺照片
		@ViewInject(R.id.img_parking_store)
		ImageView img_type;// 店铺照片

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
	// private void createRatingBar(LinearLayout ll, int num) {
	// ll.removeAllViews();
	// for (int i = 0; i < 5; i++) {
	// ImageView imageView = new ImageView(activity);
	//
	// if (i < num) {
	// imageView.setImageResource(R.drawable.star_solid);
	// } else {
	// imageView.setImageResource(R.drawable.star_stroke);
	// }
	//
	// int padding = (int) TypedValue.applyDimension(
	// TypedValue.COMPLEX_UNIT_DIP, 2, activity.getResources()
	// .getDisplayMetrics());
	// imageView.setPadding(padding, padding, padding, padding);
	// int width = (int) TypedValue.applyDimension(
	// TypedValue.COMPLEX_UNIT_DIP, 120, activity.getResources()
	// .getDisplayMetrics()) / 5;
	// ll.setGravity(Gravity.CENTER_VERTICAL);
	// ll.addView(imageView, width, width);
	// }
	// }

	/**
	 * 
	 * @Title: onItemClick
	 * @Description: FilterPopupwindow点�?�后的事�?
	 * @param parent
	 * @param view
	 * @param position
	 * @param id
	 * @param tag
	 * @see cc.ruit.shunjianmei.util.view.FilterPopupwindow.OnItemSelecedListener#onItemClick(android.widget.AdapterView,
	 *      android.view.View, int, long, java.lang.String)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id, int tag) {

		pageIndex = 1;
		// popupWindow.setSelectedPosition(position);
		ev.setState(State.Loading);

		switch (tag) {
		// 环境星级选项
		case 0:
			String score = starList.get(position).getId();
			String starText = starList.get(position).getName();
			filterTexts.put("环境星级", starText);
			filterIds.put("环境星级", score);
			// popupWindow.setSelectedPosition(position);
			// setTabText(tag);
			// pageIndex = 1;
			star = position;
			type = "1";
			getData(filterIds.get("环境星级"), filterIds.get("接单量"), filterIds.get("停车位"), type, "" + pageIndex);
			break;
		// 接单量选项
		case 1:
			String order = orderList.get(position).getId();
			String orderText = orderList.get(position).getName();
			filterTexts.put("接单量", orderText);
			filterIds.put("接单量", order);
			// popupWindow.setSelectedPosition(position);
			// pageIndex = 1;
			// setTabText(tag);
			this.order = position;
			type = "2";
			getData(filterIds.get("环境星级"), filterIds.get("接单量"), filterIds.get("停车位"), type, "" + pageIndex);
			break;
		// 距离选项
		case 2:
			String distance = distanceList.get(position).getId();
			String distanceText = distanceList.get(position).getName();
			filterTexts.put("距离", distanceText);
			filterIds.put("距离", distance);
			// popupWindow.setSelectedPosition(position);
			// pageIndex = 1;
			// setTabText(tag);
			this.distance = position;
			type = "3";
			getData(filterIds.get("环境星级"), filterIds.get("接单量"), filterIds.get("停车位"), type, "" + pageIndex);
			break;
		// 停车位
		case 3:
			String parking = parkingList.get(position).getId();
			String parkingText = parkingList.get(position).getName();
			filterTexts.put("停车位", parkingText);
			filterIds.put("停车位", parking);
			// popupWindow.setSelectedPosition(position);
			// pageIndex = 1;
			// setTabText(tag);
			this.parking = position;
			type = "4";
			getData(filterIds.get("环境星级"), filterIds.get("接单量"), filterIds.get("停车位"), type, "" + pageIndex);
			break;

		default:
			break;
		}
	}

	/**
	 * 
	 * @Title: setTabText
	 * @Description: 设置选项栏的文本颜色
	 * @author: Johnny
	 * @return: void
	 */
	private void setTabText(int tag) {
		if (filterTexts.get("环境星级") != null && tag == 0) {
			tv_star.setText(filterTexts.get("环境星级"));
			tv_star.setTextColor(activity.getResources().getColor(R.color.red));
		}
		if (filterTexts.get("接单量") != null && tag == 1) {
			tv_order.setText(filterTexts.get("接单量"));
			tv_order.setTextColor(activity.getResources().getColor(R.color.red));
		}
		if (filterTexts.get("距离") != null && tag == 2) {
			tv_distance.setText(filterTexts.get("距离"));
			tv_distance.setTextColor(activity.getResources().getColor(R.color.red));
		}
		if (filterTexts.get("停车位") != null && tag == 3) {
			tv_parking.setText(filterTexts.get("停车位"));
			tv_parking.setTextColor(activity.getResources().getColor(R.color.red));
		}

	}
}
