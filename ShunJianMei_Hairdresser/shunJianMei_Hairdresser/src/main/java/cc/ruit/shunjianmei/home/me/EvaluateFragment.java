package cc.ruit.shunjianmei.home.me;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cc.ruit.shunjianmei.base.BaseAdapter;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.net.api.EvaluationListApi;
import cc.ruit.shunjianmei.net.api.HairStyleApi;
import cc.ruit.shunjianmei.net.request.EvaluationListRequest;
import cc.ruit.shunjianmei.net.request.HairStyleRequest;
import cc.ruit.shunjianmei.net.response.EvaluationListResponse;
import cc.ruit.shunjianmei.net.response.HairStyleResponse;
import cc.ruit.shunjianmei.net.response.Images;
import cc.ruit.shunjianmei.usermanager.UserInfo;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmei.util.ForLargeImageActivity;
import cc.ruit.shunjianmei.util.FragmentManagerUtils;
import cc.ruit.shunjianmei.util.ImageLoaderUtils;
import cc.ruit.shunjianmei.util.RoundImageLoaderUtil;
import cc.ruit.shunjianmei.util.ScreenUtils;
import cc.ruit.shunjianmei.util.view.EmptyView;
import cc.ruit.shunjianmei.util.view.EmptyView.State;
import cc.ruit.shunjianmei.util.view.FilterItemBean;
import cc.ruit.shunjianmei.util.view.FilterPopupwindow;
import cc.ruit.shunjianmei.util.view.FilterPopupwindow.OnItemSelecedListener;
import cc.ruit.shunjianmeihairdresser.R;
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
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshBase.OnRefreshListener;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshBase.OnRefreshListener2;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshListView;

/**
 * @ClassName: EvaluateFragment
 * @Description: 我的评论界面
 * @author: Johnny
 * @date: 2015年10月9日 下午8:38:26
 */
public class EvaluateFragment extends BaseFragment implements OnClickListener,
		OnItemSelecedListener {

	@ViewInject(R.id.rl_filter_evaluate)
	private RelativeLayout rl_state;// 按状态选择的布局
	@ViewInject(R.id.fl_container_evaluate)
	private FrameLayout mFrameLayout;// ListView的父容器

	@ViewInject(R.id.tv_filter_evaluate)
	private TextView tv_filter;// 筛选项

	private List<FilterItemBean> filterList = new ArrayList<FilterItemBean>();// 筛选项
	private String filterText;// 筛选项文本

	@ViewInject(R.id.pulltorefreshlistview)
	private PullToRefreshListView lv_refresh;// 下来刷新列表

	EmptyView ev;// 空载页

	private FilterPopupwindow popupWindow;// 下来菜单
	List<HairStyleResponse> dialogList;// 下拉菜单的数据
	List<EvaluationListResponse> msgList = new ArrayList<EvaluationListResponse>();// 下拉刷新列表的数据

	private String styleId = "0";// 发型ID
	private String id = "0";// 评论ID，用户分页
	private int itemId = 0;

	private EvaluateAdapter pullAdapter;// 下拉刷新列表的Adapter

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("EvaluateFragment"); // 统计页面
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("EvaluateFragment");
	}

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.evaluate_layout, null);
		ViewUtils.inject(this, view);
		initTitle();
		initListView();
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

		UserInfo info = UserManager.getUserInfo(activity);
		String nickName = info.getNickName();
		title.tv_title.setText(nickName + "的评论");

	}

	/**
	 * 
	 * @Title: initListView
	 * @Description:
	 * @author: Johnny
	 * @return: void
	 */
	private void initListView() {

		pullAdapter = new EvaluateAdapter(activity, msgList);
		lv_refresh.setAdapter(pullAdapter);

		ev = new EmptyView(activity, new OnClickListener() {

			@Override
			public void onClick(View v) {
				id = "0";
				getEvaluateData(styleId, id);
			}
		});
		lv_refresh.setEmptyView(ev.getView());
		ev.setState(State.Loading);
		getHairStyleData();
		getEvaluateData(styleId, id);

		lv_refresh.setMode(Mode.BOTH);
		lv_refresh.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				id = "0";
				getEvaluateData(styleId, id);
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {

				getEvaluateData(styleId, id);
			}
		});
	}

	/**
	 * 
	 * @Title: getHairStyleData
	 * @Description: 请求发型接口
	 * @author: Johnny
	 * @return: void
	 */
	private void getHairStyleData() {
		if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
			ToastUtils.showShort(activity.getResources().getString(
					R.string.no_networks_found));
			return;
		}

		int userId = UserManager.getUserID();
		LoadingDailog.show(activity, "数据加载中，请稍后");
		HairStyleRequest request = new HairStyleRequest("" + userId);
		HairStyleApi.hairStyle(request, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				LoadingDailog.dismiss();
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
					List<HairStyleResponse> response = HairStyleResponse
							.getclazz2(result.getData());
					hairStyleResultHanlder(response, result.getData());
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				MobclickAgent.onEvent(activity, "evaluate_failure");
				LoadingDailog.dismiss();
				ToastUtils.showShort(activity.getResources().getString(
						R.string.request_failure));

			}
		});

	}

	/**
	 * @Title: resultHanlder
	 * @Description: 发型分类的 结果处理
	 * @author: Johnny
	 * @param listcontrolSignUp
	 * @return: void
	 */
	void hairStyleResultHanlder(List<HairStyleResponse> info, String jason) {
		if (info == null) {
			LogUtils.e("HairStyleResponse err");
			return;
		}
		//由于后台返回数据没有全部这一项，因此将全部选项添加至下拉菜单第一行
		FilterItemBean firstBean = new FilterItemBean();
		firstBean.setId("0");
		firstBean.setName("全部");
		filterList.add(firstBean);
		
		for (int i = 0; i < info.size(); i++) {
			FilterItemBean bean = new FilterItemBean();
			bean.setId(info.get(i).getID());
			bean.setName(info.get(i).getName());
			filterList.add(bean);
		}

//		UserManager.setHairStyle(jason);
	}

	/**
	 * 
	 * @Title: getEvaluateData
	 * @Description: 请求评论列表接口
	 * @author: Johnny
	 * @return: void
	 */
	private void getEvaluateData(String styleId, final String ID) {
		if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
			ToastUtils.showShort(activity.getResources().getString(
					R.string.no_networks_found));
			return;
		}

		ev.setLoadingState();
		int userId = UserManager.getUserID();
		EvaluationListRequest request = new EvaluationListRequest("" + userId,
				"" + userId, styleId, ID);
		EvaluationListApi.evaluationList(request,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
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
							List<EvaluationListResponse> data = EvaluationListResponse
									.getclazz2(result.getData());
							if (data != null) {
								resultListHanlder(data);
							} else {
								ToastUtils.showShort("请求数据异常");
							}
						} else if (result.getCode() == 2100) {
							if ("0".equals(ID)) {
								ev.setNullState();
							}
						} else {
							ev.setErrState();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						MobclickAgent.onEvent(activity, "login_failure");
						lv_refresh.onRefreshComplete();
						ToastUtils.showShort(activity.getResources().getString(
								R.string.request_failure));

					}
				});

	}

	/**
	 * @Title: resultListHanlder
	 * @Description: 下拉刷新列表的结果处理
	 * @author: lee
	 * @param listcontrolSignUp
	 * @return: void
	 */
	void resultListHanlder(List<EvaluationListResponse> info) {
		if (info == null || info.size() <= 0) {
			LogUtils.e("EvaluationListResponse err");
			return;
		}
		msgList.clear();
		msgList.addAll(info);
		if (info.size() > 0) {
			ev.setVisible(false);
		} else {
			ev.setNullState();
		}
		pullAdapter.notifyDataSetChanged();
		id = msgList.get(msgList.size() - 1).getID();
	}

	/**
	 * 
	 * @Title: onClick
	 * @Description: 定义点击事件
	 * @param v
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	@OnClick({ R.id.rl_filter_evaluate })
	public void onClick(View v) {
		if (popupWindow == null) {
			popupWindow = new FilterPopupwindow(activity, mFrameLayout);
			popupWindow.setOnItemClickListener(this);
		}
		switch (v.getId()) {
		// 按状态选择的布局
		case R.id.rl_filter_evaluate:
			if (popupWindow.isCurrentShowing(1)) {
				popupWindow.dismiss();
			} else {
				popupWindow.setSelectedPosition(itemId);
				popupWindow.show(filterList, 1);
			}

			break;

		default:

			break;
		}
	}

	/**
	 * 
	 * @ClassName: EvaluateAdapter
	 * @Description: 下拉刷新列表的adapter
	 * @author: Johnny
	 * @date: 2015年10月14日 下午9:25:14
	 */
	class EvaluateAdapter extends BaseAdapter {

		private Context context;
		private List<EvaluationListResponse> list;

		public EvaluateAdapter(Context context,
				List<EvaluationListResponse> list) {
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
		public EvaluationListResponse getItem(int position) {
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
						R.layout.evaluate_listview_item_layout, null);
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
		 * @author: Johnny
		 * @param position
		 * @param vh
		 * @return: void
		 */
		private void setLable(int position, ViewHolder vh) {
			EvaluationListResponse obj = getItem(position);
			RoundImageLoaderUtil.setErrImage(R.drawable.tx_man,
					R.drawable.tx_man, R.drawable.tx_man);
			RoundImageLoaderUtil.getInstance(activity, 500).loadImage(
					obj.getPhoto(), vh.iv_photo);
			vh.tv_date.setText(obj.getTime());
			vh.tv_evalution.setText(obj.getContent());
			vh.tv_name.setText(obj.getName());

			vh.ratingbar.setRating(Float.parseFloat(obj.getScore()));
			addImages(obj.getImages(), vh.imagesContainer);
		}

	}

	class ViewHolder {
		@ViewInject(R.id.iv_photo_evaluate)
		ImageView iv_photo;// 美发师头像
		@ViewInject(R.id.tv_name_evaluate)
		TextView tv_name;// 美发师姓名
		@ViewInject(R.id.tv_date_evaluate)
		TextView tv_date;// 评论时间
		@ViewInject(R.id.tv_evalution_evaluate)
		TextView tv_evalution;// 评论内容
		@ViewInject(R.id.ratingbar_evaluate)
		RatingBar ratingbar;// 星级评分条
		@ViewInject(R.id.ll_images_evaluate)
		LinearLayout imagesContainer;// 显示照片

		void findView(View convertView) {
			ViewUtils.inject(this, convertView);
		}
	}

	/**
	 * 
	 * @Title: addImages
	 * @Description:添加用户的评论图片
	 * @author: Johnny
	 * @return: void
	 */
	private void addImages(final List<Images> images, LinearLayout ll) {
		ll.removeAllViews();
		for (int i = 0; i < images.size(); i++) {
			ImageView iv = new ImageView(activity);
			iv.setScaleType(ScaleType.CENTER_CROP);
			ImageLoaderUtils.setErrImage(R.drawable.default_prc, R.drawable.default_prc, R.drawable.default_prc);
			ImageLoaderUtils.getInstance(activity).loadImage(
					images.get(i).getImage(), iv);
			int padding = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 5, activity.getResources()
							.getDisplayMetrics());
			iv.setPadding(padding, padding, padding, padding);
			int width = (ScreenUtils.getScreenWidth(activity)-padding*2) / 3;
			ll.addView(iv, new LayoutParams(width, width));
			final int index = i;
			iv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					ArrayList<String> list = new ArrayList<String>();
					for (int j = 0; j < images.size(); j++) {
						list.add(images.get(j).getImage());
					}
					startActivity(ForLargeImageActivity.getIntent(activity,
							index, list));
				}
			});
		}
	}

	/**
	 * 
	 * @Title: onItemClick
	 * @Description: 筛选菜单的点击事件
	 * @param parent
	 * @param view
	 * @param position
	 * @param id
	 * @param tag
	 * @see cc.ruit.shunjianmei.util.view.FilterPopupwindow.OnItemSelecedListener#onItemClick(android.widget.AdapterView,
	 *      android.view.View, int, long, int)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id, int tag) {

		styleId = filterList.get(position).getId();
		filterText = filterList.get(position).getName();
//		setTabText(filterText);
		itemId = position;
		this.id = "0";
		getEvaluateData(styleId, this.id);
	}

	/**
	 * 
	 * @Title: setTabText
	 * @Description: 设置选项栏的文本颜色
	 * @author: Johnny
	 * @return: void
	 */
	private void setTabText(String text) {
		tv_filter.setText(text);
		tv_filter.setTextColor(activity.getResources().getColor(R.color.red));

	}

}
