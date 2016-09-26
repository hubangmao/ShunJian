package cc.ruit.shunjianmei.home.hairstyle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cc.ruit.shunjianmei.R;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.net.api.FavoriteHairStyleListApi;
import cc.ruit.shunjianmei.net.api.HairStyleApi;
import cc.ruit.shunjianmei.net.api.HairStyleListApi;
import cc.ruit.shunjianmei.net.request.FavoriteHairStyleListRequest;
import cc.ruit.shunjianmei.net.request.HairStyleListRequest;
import cc.ruit.shunjianmei.net.request.HairStyleRequest;
import cc.ruit.shunjianmei.net.response.HairStyleListResponse;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmei.util.FragmentManagerUtils;
import cc.ruit.shunjianmei.util.view.BusinessAreaBean;
import cc.ruit.shunjianmei.util.view.BusinessAreaPopupwindow;
import cc.ruit.shunjianmei.util.view.BusinessAreaPopupwindow.OnItemSelecedListener;
import cc.ruit.shunjianmei.util.view.EmptyView;
import cc.ruit.shunjianmei.util.view.EmptyView.State;
import cc.ruit.utils.sdk.ToastUtils;
import cc.ruit.utils.sdk.http.NetWorkUtils;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.oruit.widget.loadingdialog.LoadingDailog;
import com.oruit.widget.loadingdialog.LoadingViewUtil;
import com.oruit.widget.title.TitleUtil;
import com.umeng.analytics.MobclickAgent;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshBase;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshBase.Mode;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshBase.OnLastItemVisibleListener;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshBase.OnRefreshListener2;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshGridView;

/**
 * 
 * @ClassName: HairManagementFragment
 * @Description: 发型管理
 * @author: 欧阳
 * @date: 2015年10月16日 下午7:16:32
 */
public class HairStyleFragment extends BaseFragment implements
		OnItemSelecedListener {
	@ViewInject(R.id.gv_hair)
	PullToRefreshGridView gv_hair;// 返回按钮
	@ViewInject(R.id.ll_filter)
	LinearLayout ll_filter;// 发型选择
	@ViewInject(R.id.ll_hair_style)
	LinearLayout ll_style;// 发型选择
	@ViewInject(R.id.ll_hair_price)
	LinearLayout ll_price;// 发型选择
	@ViewInject(R.id.tv_hair)
	TextView tv_hair;// 发型
	@ViewInject(R.id.iv_hair)
	ImageView iv_hair;// 图标上下
	@ViewInject(R.id.tv_price)
	TextView tv_price;// 价格
	@ViewInject(R.id.iv_price)
	ImageView iv_price;// 价格上下
	@ViewInject(R.id.fl_gv_hair)
	FrameLayout fl_gv_hair;
	private HairStyleAdapter mAdapter;// 发型列表
	List<HairStyleListResponse> list;
	private EmptyView ev;// 空载页
	BusinessAreaPopupwindow popupWindow;// 下拉菜单
	public final static int LOOKHAIR = 1;
	private String pageSize = "10";// 加载页数目
	private int pageIndex = 1;// 加载页
	private static final int STYLE_TAG = 0;// 发型选项标识
	private static final int PRICE_TAG = 1;// 价格选项标识
	private List<BusinessAreaBean> styleList = new ArrayList<BusinessAreaBean>();// 商圈选项
	private List<BusinessAreaBean> pricesort = new ArrayList<BusinessAreaBean>();// 智能排序选项
	private Map<String, String> filterTexts = new HashMap<String, String>();// 储存筛选项文本
	private Map<String, String> filterIds = new HashMap<String, String>();// 储存筛选项Id
	protected boolean isLast = false;//是否为最后一页
	protected boolean isLoading = false;//是否正在请求接口中
	
	PageType type;

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.hair_style_manager_layout, null);
		ViewUtils.inject(this, view);
		initPageType();
		initTitle(view, "发型");
		initData();
		initPullToRefreshGridView();
		
		return view;
	}
	/**
	 * @Title: initPageType
	 * @Description: 页面类型
	 * @author: lee
	 * @return: void
	 */
	private void initPageType() {
		if(getArguments()!=null&&getArguments().getString("from")!=null){
			type = PageType.coollect;
		}else {
			type = PageType.normal;
		}
	}
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("HairManagement"); // 统计页面
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("HairManagement");
	}

	/**
	 * 
	 * @Title: initData
	 * @Description: 初始化数据
	 * @author: 欧阳
	 * @return: void
	 */
	private void initTitle(View view, String title) {
		TitleUtil titleUtil = new TitleUtil(view);
		// 设置标题栏中间的文字
		titleUtil.tv_title.setText(title);
		// 设置标题栏左边的图片
		titleUtil.iv_left.setVisibility(View.VISIBLE);
		titleUtil.iv_left.setImageResource(R.drawable.back);
		titleUtil.iv_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				activity.finish();
			}
		});
		titleUtil.rl_container.setVisibility(type==PageType.coollect?View.GONE:View.VISIBLE);
		ll_filter.setVisibility(type==PageType.coollect?View.GONE:View.VISIBLE);
	}
	
	void getData(){
		if (type == PageType.coollect) {
			getData(""+pageIndex, pageSize);
		}else {
			getData(filterIds.get("发型"), filterIds.get("价格排序"), "" + pageIndex);
			getHairStyle();
		}
	}
	/**
	 * 
	 * @Title: getHairStyle
	 * @Description: 得到发型
	 * @author: 欧阳
	 * @return: void
	 */
	private void getHairStyle() {
		
		if (!NetWorkUtils.isConnectInternet(activity)) {
			ToastUtils.showShort("网络未链接，请检查网络设置");
			return;
		} else {
			HairStyleRequest request = new HairStyleRequest(
					UserManager.getUserID() + "");
			HairStyleApi.hairStyle(request, new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					BaseResponse response = BaseResponse
							.getBaseResponse(responseInfo.result);
					if (response == null) {
						return;
					}

					String[] split = response.getMsg().split("\\|");
					if ("1".equals(split[0])) {
						ToastUtils.showShort(split[1] + "");
					}
					if (response.getCode() == 1000) {
						List<BusinessAreaBean> templist = BusinessAreaBean
								.getclazz2(response.getData());
						if (templist.size() > 0 && templist != null) {
							styleList.clear();
							styleList.addAll(templist);
							styleList.add(0, new BusinessAreaBean("0", "全部"));
						}
					}
				}

				@Override
				public void onFailure(HttpException error, String msg) {
					ToastUtils.showShort("请求数据失败");
				}
			});
		}
	}

	/**
	 * @Title: initData
	 * @Description: 初始化数据
	 * @author: Johnny
	 * @return: void
	 */
	private void initData() {
		pricesort.add(new BusinessAreaBean("0", "价格从低到高"));
		pricesort.add(new BusinessAreaBean("1", "价格从高到低"));

		filterTexts.put("发型", "发型");
		filterTexts.put("价格排序", "价格排序");

		filterIds.put("发型", "0");
		filterIds.put("价格排序", "0");
		getData();
	}

	/**
	 * 
	 * @Title: initGridView
	 * @Description: 初始化GridView
	 * @author: 欧阳
	 * @return: void
	 */
	private void initPullToRefreshGridView() {
		list = new ArrayList<HairStyleListResponse>();
		mAdapter = new HairStyleAdapter(activity, list);
		gv_hair.setAdapter(mAdapter);
		ev = new EmptyView(activity, new OnClickListener() {

			@Override
			public void onClick(View v) {
				LoadingDailog.show(activity, "数据加载中，请稍后");
				pageIndex = 1;
				getData();
			}
		});
		gv_hair.setEmptyView(ev.getView());
		ev.setState(State.Loading);
		
		gv_hair.setMode(Mode.BOTH);
		gv_hair.setOnRefreshListener(new OnRefreshListener2<GridView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<GridView> refreshView) {
				pageIndex = 1;
				getData();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<GridView> refreshView) {
				getData();
			}
		});
		gv_hair.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				if (isLast) {
					return;
				}
				isLast = true;
				getData();
			}
		});
		gv_hair.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 打开发型详情列表
				 Fragment fragment = FragmentManagerUtils.getFragment(
				 activity, HairStyleDetailsFragment.class.getName());
				 Bundle bundle = new Bundle();
				 bundle.putString("ID", list.get(position).ID);
				 bundle.putString("Name",list.get(position).Name);
				 fragment.setArguments(bundle);
				 FragmentManagerUtils.add(getActivity(), R.id.content_frame,
				 fragment, true);

			}
		});

	}
	/**
	 * 
	 * @Title: getData
	 * @Description: 请求收藏接口
	 * @author: Johnny
	 * @return: void
	 */
	private void getData(final String pageIndex, String pageSize) {
		if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
			ToastUtils.showShort(activity.getResources().getString(
					R.string.no_networks_found));
			return;
		}

		int userId = UserManager.getUserID();
		FavoriteHairStyleListRequest request = new FavoriteHairStyleListRequest(
				"" + userId, pageIndex, pageSize);
		FavoriteHairStyleListApi.favoriteHairStyleList(request,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						LoadingDailog.dismiss();
						gv_hair.onRefreshComplete();
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
							List<HairStyleListResponse> data = HairStyleListResponse
									.getclazz2(result.getData());
							if (data != null) {
								// 设置网络请求的数据
								resultHanlder(data, pageIndex);
							} else {
								ToastUtils.showShort("请求数据异常");
							}
						} else if (result.getCode() == 2100) {
							if("1".equals(pageIndex)){
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
	 * 
	 * @Title: getData
	 * @Description: 网络请求数据
	 * @author: 欧阳
	 * @param string
	 * @param string2
	 * @param string3
	 * @return: void
	 */
	protected void getData(String ID, String Sort, final String PageIndex) {
		if(isLoading) return;
		if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
			ToastUtils.showShort(activity.getResources().getString(
					R.string.no_networks_found));
			LoadingDailog.dismiss();
			gv_hair.onRefreshComplete();
			ev.setErrState();
			new LoadingViewUtil(view).hint();
			return;
		} 
		HairStyleListRequest request = new HairStyleListRequest(
				UserManager.getUserID() + "", ID, Sort, UserManager.getCityID()
						+ "", pageSize + "", PageIndex);
		HairStyleListApi.HairStyleList(request, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();
				isLoading = true;
			}
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				isLoading = false;
				LoadingDailog.dismiss();
				BaseResponse result = BaseResponse
						.getBaseResponse(responseInfo.result);
				if (result == null) {
					return;
				}
				gv_hair.onRefreshComplete();
				String[] split = result.getMsg().split("\\|");
				if ("1".equals(split[0])) {
					ToastUtils.showShort(split[1] + "");
				}
				if (result.getCode() == 1000) {
					List<HairStyleListResponse> templist = HairStyleListResponse
							.getclazz2(result.getData());
					resultHanlder(templist, PageIndex);
				} else if (result.getCode() == 2100) {
					if ("1".equals(PageIndex)) {
						ev.setNullState();
					}
				} else {
					ev.setErrState();
				}

			}

			@Override
			public void onFailure(HttpException error, String msg) {
				LoadingDailog.dismiss();
				ev.setErrState();
				isLast = false;
				isLoading = false;
				gv_hair.onRefreshComplete();
				ToastUtils.showShort(activity.getResources().getString(
						R.string.request_failure));
			}
		});

	}

	protected void resultHanlder(List<HairStyleListResponse> templist,
			String pageIndex) {
		if (templist == null) {
			ev.setNullState();
			return;
		}
		if (!"1".equals(pageIndex) && templist.size() <= 0) {
			return;
		}else if (templist.size()==0) {
			ev.setNullState();
			return;
		}
		if ("1".equals(pageIndex)) {
			list.clear();	
		}
		isLast = false;
		list.addAll(templist);
		this.pageIndex++;
		mAdapter.notifyDataSetChanged();
//		if (!"1".equals(pageIndex)) {//向上滑动一项
//			if (list.size()>templist.size()) {
//				gv_hair.getRefreshableView().setSelection(list.size()-templist.size());
//			}
//		}
	}

	/**
	 * 
	 * @Title: resultHanlder
	 * @Description: 数据处理
	 * @author: 欧阳
	 * @param templist
	 * @param pageIndex
	 * @return: void
	 */
	protected void resultHanlder(List<HairStyleListResponse> templist, int pageIndex) {
		
	}

	@OnClick({ R.id.ll_hair_price, R.id.ll_hair_style })
	public void methodClick(View v) {
		// ----------初始化筛选框-------------
		if (popupWindow == null) {
			popupWindow = new BusinessAreaPopupwindow(activity, fl_gv_hair);
			popupWindow.setOnItemClickListener(this);
		}
		switch (v.getId()) {
		case R.id.ll_hair_price:
			if (popupWindow.isCurrentShowing(PRICE_TAG)) {
				popupWindow.dismiss();
			} else {
				if (Integer.parseInt(Priceid) != 0) {
					popupWindow.setSelectedPosition(Integer.parseInt(Priceid));
				} else {
					popupWindow.setSelectedPosition(0);
				}
				popupWindow.show(pricesort, PRICE_TAG);
			}
			break;
		case R.id.ll_hair_style:
			if (popupWindow.isCurrentShowing(STYLE_TAG)) {
				popupWindow.dismiss();
			} else {
				if (Integer.parseInt(StyleID) != 0) {
					popupWindow.setSelectedPosition(Integer.parseInt(StyleID));
				} else {
					popupWindow.setSelectedPosition(0);
				}
				popupWindow.show(styleList, STYLE_TAG);
			}
			break;
		default:
			break;
		}
	}

	String StyleID = "0";
	String Priceid = "0";

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id, int tag) {
		switch (tag) {
		// 发型
		case 0:
			StyleID = styleList.get(position).getID();
			String Style = styleList.get(position).getName();
			filterTexts.put("发型", Style);
			filterIds.put("发型", StyleID);
			popupWindow.setSelectedPosition(position);
//			setTabText(tag);
			pageIndex = 1;
			gv_hair.setEmptyView(ev.getView());
			ev.setState(State.Loading);
			getData(StyleID, filterIds.get("价格排序"), "" + pageIndex);

			initTitle(this.view, Style);
						
			break;
		// 价格
		case 1:
			Priceid = pricesort.get(position).getID();
			String price = pricesort.get(position).getName();
			filterTexts.put("价格排序", price);
			filterIds.put("价格排序", Priceid);
			popupWindow.setSelectedPosition(position);
//			setTabText(tag);
			pageIndex = 1;
			gv_hair.setEmptyView(ev.getView());
			ev.setState(State.Loading);
			getData(filterIds.get("发型"), Priceid, "" + pageIndex);
			break;
		}
	}

	/**
	 * 
	 * @Title: setTabText
	 * @Description: 设置文本属性
	 * @author: 欧阳
	 * @param tag
	 * @return: void
	 */
	private void setTabText(int tag) {
		if (filterTexts.get("发型") != null && tag == 0) {
			tv_hair.setText(filterTexts.get("发型"));
			tv_hair.setTextColor(activity.getResources().getColor(R.color.red_e5));
			iv_hair.setImageResource(R.drawable.arrow_up_gray);
		} else {
			tv_hair.setTextColor(activity.getResources().getColor(
					R.color.gray_ab));
			iv_hair.setImageResource(R.drawable.arrow_down_gray);
		}
		if (filterTexts.get("价格排序") != null && tag == 1) {
			tv_price.setText(filterTexts.get("价格排序"));
			tv_price.setTextColor(activity.getResources().getColor(R.color.red_e5));
			iv_price.setImageResource(R.drawable.arrow_up_gray);
		} else {
			tv_price.setTextColor(activity.getResources().getColor(
					R.color.gray_ab));
			iv_price.setImageResource(R.drawable.arrow_down_gray);
		}

	}

	
	public int getScreenWidth() {
		DisplayMetrics metric = new DisplayMetrics();
		WindowManager wm = (WindowManager) activity
				.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels;
		return width;
	}
	public enum PageType {
		normal,coollect
	}
}
