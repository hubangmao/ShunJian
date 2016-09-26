package cc.ruit.shunjianmei.home.store;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import cc.ruit.shunjianmei.R;
import cc.ruit.shunjianmei.base.BaseAdapter;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.net.api.StoreEvaluationListApi;
import cc.ruit.shunjianmei.net.request.StoreEvaluationListRequest;
import cc.ruit.shunjianmei.net.response.HairStyleResponse;
import cc.ruit.shunjianmei.net.response.StoreEvaluationListResponse;
import cc.ruit.shunjianmei.net.response.StoreEvaluationListResponse.Images;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmei.util.ForLargeImageActivity;
import cc.ruit.shunjianmei.util.FragmentManagerUtils;
import cc.ruit.shunjianmei.util.ImageLoaderUtils;
import cc.ruit.shunjianmei.util.RoundImageLoaderUtil;
import cc.ruit.shunjianmei.util.ScreenUtils;
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
import com.oruit.widget.title.TitleUtil;
import com.umeng.analytics.MobclickAgent;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshBase;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshBase.Mode;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshBase.OnRefreshListener2;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshListView;

/**
 * @ClassName: StoreEvaluateFragment
 * @Description: 美发店评价列表
 * @author: Johnny
 * @date: 2015年10月9日 下午8:38:26
 */
public class StoreEvaluateFragment extends BaseFragment implements
		OnClickListener {

	@ViewInject(R.id.pulltorefreshlistview)
	private PullToRefreshListView lv_refresh;// 下来刷新列表

	EmptyView ev;// 空载页
	private String id="0";//评价ID
	
	List<HairStyleResponse> dialogList;// 下拉菜单的数据
	List<StoreEvaluationListResponse> msgList = new ArrayList<StoreEvaluationListResponse>();// 下拉刷新列表的数据

	private String storeId; //美发店ID

	private StoreEvaluateAdapter pullAdapter;// 下拉刷新列表的Adapter

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("StoreEvaluateFragment"); // 统计页面
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("StoreEvaluateFragment");
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

//		UserInfo info = UserManager.getUserInfo(activity);
//		String nickName = info.getNickName();
		title.tv_title.setText("用户评论");
	}

	/**
	 * 
	 * @Title: initListView
	 * @Description:
	 * @author: Johnny
	 * @return: void
	 */
	private void initListView() {

		storeId = getArguments().getString("storeID");
		
		pullAdapter = new StoreEvaluateAdapter(activity, msgList);
		lv_refresh.setAdapter(pullAdapter);

		ev = new EmptyView(activity, new OnClickListener() {

			@Override
			public void onClick(View v) {
				id="0";
				getEvaluateData(storeId, id);
			}
		});
		lv_refresh.setEmptyView(ev.getView());
		ev.setState(State.Loading);
		getEvaluateData(storeId, id);
		
		lv_refresh.setMode(Mode.BOTH);
		lv_refresh.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				id="0";
				getEvaluateData(storeId, id);

			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {

				getEvaluateData(storeId, id);
			}
		});
	}

	/**
	 * 
	 * @Title: getEvaluateData
	 * @Description: 请求评价列表接口
	 * @author: Johnny
	 * @return: void
	 */
	private void getEvaluateData(String storeID, final String Id) {
		if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
			ToastUtils.showShort(activity.getResources().getString(
					R.string.no_networks_found));
			ev.setErrState();
			lv_refresh.onRefreshComplete();
			return;
		}
//		LoadingDailog.show(activity, "数据加载中，请稍后");
		int userId = UserManager.getUserID();
		if("0".equals(Id)){
			ev.setLoadingState();
		}
		StoreEvaluationListRequest request = new StoreEvaluationListRequest(""
				+ userId, storeID, Id);
		StoreEvaluationListApi.storeEvaluationList(request,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
//						LoadingDailog.dismiss();
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
							List<StoreEvaluationListResponse> data = StoreEvaluationListResponse
									.getclazz2(result.getData());
							if (data != null) {
								resultListHanlder(data,Id);
							} else {
								ev.setErrState();
								ToastUtils.showShort("请求数据异常");
							}
						}else if(result.getCode() == 2100){
							if ("0".equals(id)) {
								ev.setNullState();
							}
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						MobclickAgent.onEvent(activity, "login_failure");
//						LoadingDailog.dismiss();
						ev.setErrState();
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
	 * @return: void
	 */
	void resultListHanlder(List<StoreEvaluationListResponse> info,String id) {
		if (info == null || info.size() <= 0) {
			LogUtils.e("EvaluationListResponse err");
			return;
		}
		if("0".equals(id)){
			msgList.clear();
		}
		if (!"0".equals(id) && info.size() <= 0) {
			return;
		}
		msgList.addAll(info);
		if (msgList.size() > 0) {
			ev.setDataState();
		} else {
			ev.setNullState();
		}
		this.id = info.get(info.size()-1).getID();
		pullAdapter.notifyDataSetChanged();

	}

	/**
	 * 
	 * @ClassName: StoreEvaluateAdapter
	 * @Description: 下拉刷新列表的adapter
	 * @author: Johnny
	 * @date: 2015年10月14日 下午9:25:14
	 */
	class StoreEvaluateAdapter extends BaseAdapter {

		private Context context;
		private List<StoreEvaluationListResponse> list;

		public StoreEvaluateAdapter(Context context,
				List<StoreEvaluationListResponse> list) {
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
		public StoreEvaluationListResponse getItem(int position) {
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
			StoreEvaluationListResponse obj = getItem(position);
			RoundImageLoaderUtil.setErrImage(R.drawable.tx_man,
					R.drawable.tx_man, R.drawable.tx_man);
			RoundImageLoaderUtil.getInstance(activity, 500).loadImage(
					obj.getPhoto(), vh.iv_photo);
			vh.tv_date.setText(obj.getTime());
			if(!("").equals(obj.getContent())&&obj.getContent()!=null){
				vh.tv_evalution.setText(obj.getContent());
				vh.tv_evalution.setVisibility(View.VISIBLE);
			}
			
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
		TextView tv_date;// 评价时间
		@ViewInject(R.id.tv_evalution_evaluate)
		TextView tv_evalution;// 评价内容
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
	 * @Title: createRatingBar
	 * @Description:创建RatingBar
	 * @author: Johnny
	 * @param ll
	 * @param num
	 * @return: void
	 */
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
			final int index = i;
			
			ImageView iv = new ImageView(activity);
			//将制定的单位值转换为像素值
			int padding = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 5, activity.getResources()
							.getDisplayMetrics());
			iv.setPadding(padding, padding, padding, padding);
			ImageLoaderUtils.setErrImage(R.drawable.default_prc, R.drawable.default_prc, R.drawable.default_prc);
			ImageLoaderUtils.getInstance(activity).loadImage(
					images.get(i).Image, iv);
			int width = (ScreenUtils.getScreenWidth(activity) - padding*2) / 3;
			iv.setLayoutParams(new LayoutParams(width, width));
			iv.setScaleType(ScaleType.CENTER_CROP);
			
			iv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ArrayList<String> list = new ArrayList<String>();
					for (int j = 0; j < images.size(); j++) {
						list.add(images.get(j).Image);
					}
					startActivity(ForLargeImageActivity.getIntent(
							activity, index, list));
				}
			});
			
			ll.addView(iv);
			
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
