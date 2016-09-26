package cc.ruit.shunjianmei.reserve;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import cc.ruit.shunjianmei.R;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.constants.Constant;
import cc.ruit.shunjianmei.home.hairdressert.HairDressertDetailsFragment;
import cc.ruit.shunjianmei.home.order.OrderDetailFragment;
import cc.ruit.shunjianmei.home.order.OrderFragment;
import cc.ruit.shunjianmei.home.store.StoreActivity;
import cc.ruit.shunjianmei.home.store.StoreDetailFragment;
import cc.ruit.shunjianmei.home.store.StoreFragment;
import cc.ruit.shunjianmei.net.api.CanUseCouponsApi;
import cc.ruit.shunjianmei.net.api.CommitOrderApi;
import cc.ruit.shunjianmei.net.api.ReservationMaterialApi;
import cc.ruit.shunjianmei.net.api.ReservationServiceApi;
import cc.ruit.shunjianmei.net.api.ReservationStoreApi;
import cc.ruit.shunjianmei.net.request.CanUseCouponsRequest;
import cc.ruit.shunjianmei.net.request.CommitOrderRequest;
import cc.ruit.shunjianmei.net.request.ReservationMaterialAgainRequest;
import cc.ruit.shunjianmei.net.request.ReservationMaterialRequest;
import cc.ruit.shunjianmei.net.request.ReservationServiceAgainRequest;
import cc.ruit.shunjianmei.net.request.ReservationServiceRequest;
import cc.ruit.shunjianmei.net.request.ReservationStoreRequest;
import cc.ruit.shunjianmei.net.response.CanUseCouponsResponse;
import cc.ruit.shunjianmei.net.response.CommitOrderResponse;
import cc.ruit.shunjianmei.net.response.ReservationMaterialResponse;
import cc.ruit.shunjianmei.net.response.ReservationMaterialResponse.Item;
import cc.ruit.shunjianmei.net.response.ReservationServiceResponse;
import cc.ruit.shunjianmei.net.response.ReservationServiceResponse.HairStyle;
import cc.ruit.shunjianmei.net.response.ReservationStoreResponse;
import cc.ruit.shunjianmei.pay.OrderPayActivity;
import cc.ruit.shunjianmei.pay.OrderPayFragment;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmei.util.FragmentManagerUtils;
import cc.ruit.shunjianmei.util.RoundImageLoaderUtil;
import cc.ruit.shunjianmei.util.ScreenUtils;
import cc.ruit.shunjianmei.util.view.EmptyView;
import cc.ruit.shunjianmei.util.view.MyGridView;
import cc.ruit.shunjianmei.util.view.MyListView;
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
import com.oruit.widget.loadingdialog.LoadingViewUtil;
import com.oruit.widget.messagedialog.MessageDialog;
import com.oruit.widget.title.TitleUtil;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

/**
 * @ClassName: ReserveFragment
 * @Description: 预约界面
 * @author: Johnny
 * @date: 2015年10月14日 下午4:32:59
 */

@SuppressLint("InflateParams")
public class ReserveFragment extends BaseFragment implements OnClickListener {

	@ViewInject(R.id.sv_content)
	private ScrollView sv_content;//
	@ViewInject(R.id.ratingbar_reserve)
	private RatingBar ratingbar;// 星形评分条
	@ViewInject(R.id.rl_hairstyle_reserve)
	private RelativeLayout rl_hairstyle;// 发型类型的布局
	@ViewInject(R.id.ll1_reserve)
	private LinearLayout ll1_reserve;// 发型选项1的布局
	@ViewInject(R.id.ll2_reserve)
	private LinearLayout ll2_reserve;// 发型选项2的布局
	@ViewInject(R.id.rl_tf_reserve)
	private RelativeLayout rl_tf;// 烫发的布局
	@ViewInject(R.id.rl_rf_reserve)
	private RelativeLayout rl_rf;// 染发的布局
	@ViewInject(R.id.rl_hl_reserve)
	private RelativeLayout rl_hl;// 护理的布局

	@ViewInject(R.id.tv_pay_reserve)
	private TextView tv_pay;// 去支付按钮
	@ViewInject(R.id.tv_money_reserve)
	private TextView tv_money;// 应付金额
	@ViewInject(R.id.tv_discount_reserve)
	private TextView tv_discount;// 优惠金额
	@ViewInject(R.id.iv_dresserphoto_reserve)
	private ImageView iv_dresserphoto;// 发型师头像
	@ViewInject(R.id.tv_dressername_reserve)
	private TextView tv_dressername;// 发型师昵称
	@ViewInject(R.id.tv_levelname_reserve)
	private TextView tv_levelname;// 发型师等级
	@ViewInject(R.id.tv_star_reserve)
	private TextView tv_star;// 发型师星座
	@ViewInject(R.id.tv_ordernum_reserve)
	private TextView tv_ordernum;// 接单量
	@ViewInject(R.id.tv_hairstyle_reserve)
	private TextView tv_hairstyle;// 发型

	@ViewInject(R.id.tv_item1_reserve)
	private TextView tv_item1;// 服务选项1
	@ViewInject(R.id.tv_item1_price_reserve)
	private TextView tv_item1_price;// 服务选项1价格

	@ViewInject(R.id.tv_item2_reserve)
	private TextView tv_item2;// 服务选项2
	@ViewInject(R.id.tv_item2_price_reserve)
	private TextView tv_item2_price;// 服务选项2价格

	@ViewInject(R.id.tv_tf_reserve)
	private TextView tv_tf;// 烫发可选
	@ViewInject(R.id.tv_rf_reserve)
	private TextView tv_rf;// 染发可选
	@ViewInject(R.id.tv_hl_reserve)
	private TextView tv_hl;// 护理可选

	@ViewInject(R.id.iv1_reserve)
	private ImageView iv1;// 发型选项1的图标
	@ViewInject(R.id.iv2_reserve)
	private ImageView iv2;// 发型选项2的图标

	@ViewInject(R.id.radiogroup_reserve)
	private RadioGroup radioGroup;// 日期选择
	@ViewInject(R.id.horizontalScrollView_reserve)
	private HorizontalScrollView horizontalScrollView;

	@ViewInject(R.id.listview)
	private MyListView lv;// 商户列表
	@ViewInject(R.id.listview_dialog)
	private MyListView lv_dialog;// dialog列表

	@ViewInject(R.id.gd_reserve)
	private MyGridView gv_schedule;// 日程时段布局
	@ViewInject(R.id.fl_reserve_scrollview_layout)
	private FrameLayout fl_schedule;// 没有安排工作时间隐藏此布局
	@ViewInject(R.id.tv_fl_reserve_scrollview_layout_null)
	private TextView tv_schedule_null;// 没有安排工作时间显示的内容

	private MaterialAdapter materialAdapter;
	private List<Item> materialList = new ArrayList<Item>();

	private List<ReservationServiceResponse.HairStyle> hairStyleList;
	private List<ReservationServiceResponse.Schedule> scheduleList;
	private List<Item> tfList;// 烫发
	private List<Item> rfList;// 染发
	private List<Item> hlList;// 护理
	private View dialogView;// 预约物料的dialog视图
	private MessageDialog materialDialog;// 预约物料dialog

	private List<ReservationServiceResponse.Schedule.Item> schTimeList = new ArrayList<ReservationServiceResponse.Schedule.Item>();
	private List<ReservationStoreResponse> storeList = new ArrayList<ReservationStoreResponse>();
	private ReservationStoreAdapter storeAdapter;// 美发店Adapter
	private Map<String, String> items = new HashMap<String, String>();
	// private List<cc.ruit.shunjianmei.net.request.CommitOrderRequest.Item>
	// itemsParam = new
	// ArrayList<cc.ruit.shunjianmei.net.request.CommitOrderRequest.Item>();

	private EmptyView ev;// 空载页

	private String id;// 发型师ID
	private String hairStyleId = "0";// 发型ID
	private String hairStyleName = "";// 发型名字
	String againOrderID;// 再次预约时的订单ID
	private int scrollItemPosition;

	private ScheduleAdapter scheduleAdapter;

	ServiceBean service = new ServiceBean();// 数据存储modle,用于存储所有发型相关的需要组合的数据
	HairStyle hs = null;// 发型类型数据对象

	String from = "";// 跳转来源页面
	private boolean isClosed = false;// 标记页面是否退出的标识，避免在数据请求回来之前用户就以退出界面而导致页面崩溃
	protected boolean isMaterialLoading;// 物料信息正在加载的标示

	@Override
	public void onResume() {
		super.onResume();
		// LogUtils.i("ReserveFragment ------------------------------
		// onResume");
		MobclickAgent.onPageStart("ReserveFragment"); // 统计页面
	}

	@Override
	public void onPause() {
		super.onPause();
		// LogUtils.i("ReserveFragment ------------------------------ onPause");
		MobclickAgent.onPageEnd("ReserveFragment");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// LogUtils.i("ReserveFragment ------------------------------
		// onDestroy");
		EventBus.getDefault().unregister(this);
		isClosed = true;
	}

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.reserve_layout, null);
		ViewUtils.inject(this, view);
		initTitle();
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
				boolean isBack = FragmentManagerUtils.back(getActivity(), R.id.content_frame);
				if (!isBack) {
					getActivity().finish();
				}
			}
		});
		title.iv_left.setImageResource(R.drawable.back);
		title.iv_left.setVisibility(View.VISIBLE);
		title.iv_right.setImageResource(R.drawable.store_service_icon);
		title.iv_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Uri Storeuri = Uri.parse("tel:" + UserManager.getPhone());
				Intent Storeintent = new Intent();
				Storeintent.setAction(Intent.ACTION_DIAL);
				Storeintent.setData(Storeuri);
				startActivity(Storeintent);
			}
		});
		title.iv_right.setVisibility(View.INVISIBLE);
		title.tv_title.setText("预约");
	}

	/**
	 * @Title: initData
	 * @Description: 初始化数据
	 * @author: Johnny
	 * @return: void
	 */
	private void initData() {
		EventBus.getDefault().register(this);// 注册eventbus监听
		// 初始化其他页面传递数据
		if (id == null && getArguments() != null) {
			// 来源界面：HairStyleDetailsFragment、HairdresserFragment、HairDressertDetailsFragment、HairDressertFragmentHairDressertFragment
			hairStyleId = getArguments().getString("HairStyleId");
			hairStyleName = getArguments().getString("HairStyleName");
			id = getArguments().getString("ID");
			againOrderID = getArguments().getString("OrderID");
			from = getArguments().getString("From");
		}
		if (id == null && againOrderID == null) {
			ToastUtils.showLong("数据异常");
			return;
		}
		if (hairStyleId == null) {
			hairStyleId = "0";
		}
		// 日程列表处理
		scheduleAdapter = new ScheduleAdapter(activity, schTimeList);
		gv_schedule.setAdapter(scheduleAdapter);
		gv_schedule.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// hlList.get(hsAdapter.getSelectedIndex());
				if ("0".equals(schTimeList.get(position).State)) {
					scheduleAdapter.setSelectedIndex(position);
					scheduleAdapter.notifyDataSetChanged();
					String WHID = scheduleList.get(scrollItemPosition).Item.get(position).WHID;
					getReservationStoreData(WHID);
				}
			}
		});
		// 美发店处理
		storeAdapter = new ReservationStoreAdapter(activity, storeList);
		lv.setAdapter(storeAdapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				storeAdapter.setSelectedIndex(position);
				storeAdapter.notifyDataSetChanged();
			}
		});
		getReservationServiceData();
	}

	/**
	 * @Title: getReservationServiceData
	 * @Description: 获取预约服务数据
	 * @author: Johnny
	 * @return: void
	 */
	public void getReservationServiceData() {
		if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
			ToastUtils.showShort(activity.getResources().getString(R.string.no_networks_found));

			return;
		}
		LoadingDailog.show(activity, "数据加载中，请稍后");

		int UserId = UserManager.getUserID();
		RequestCallBack<String> callback = new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
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
					List<ReservationServiceResponse> response = ReservationServiceResponse.getclazz2(result.getData());
					resultReservationServiceHanlder(response);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				new LoadingViewUtil(view).hint();
				LoadingDailog.dismiss();
				ToastUtils.showShort(activity.getResources().getString(R.string.request_failure));
			}
		};
		if (OrderDetailFragment.class.getSimpleName().equals(from)
				|| OrderFragment.class.getSimpleName().equals(from)) {
			ReservationServiceAgainRequest request = new ReservationServiceAgainRequest("" + UserId, againOrderID);
			ReservationServiceApi.ReservationServiceAgain(request, callback);
		} else {
			ReservationServiceRequest request = new ReservationServiceRequest("" + UserId, id, hairStyleId);
			ReservationServiceApi.reservationService(request, callback);
		}
	}

	/**
	 * @Title: resultHanlder
	 * @Description: 结果处理
	 * @author: Johnny
	 * @param listcontrolSignUp
	 * @return: void
	 */
	void resultReservationServiceHanlder(List<ReservationServiceResponse> info) {
		if (info == null) {
			LogUtils.e("ReservationServiceResponse err");
			return;
		}
		ReservationServiceResponse obj = info.get(0);
		if (isClosed == true) {
			return;
		}
		setUserinfoData(obj);// 设置美发师个人信息部分数据
		setHairStyleData(obj);// 设置发型项目
		setScheduleData(obj);

	}

	/**
	 * @Title: setUserinfo
	 * @Description: 设置美发师个人信息部分数据
	 * @author: lee
	 * @param obj
	 * @return: void
	 */
	void setUserinfoData(ReservationServiceResponse obj) {
		id = obj.getID();
		tv_dressername.setText(obj.getName());// 美发师名字
		tv_levelname.setText(obj.getLevelName());// 美发师level
		tv_star.setText(obj.getStar());// 美发师星座
		ratingbar.setRating(Float.parseFloat(obj.getScore()));
		tv_ordernum.setText(obj.getOrderNum() + "次");// 订单次数
		RoundImageLoaderUtil.setErrImage(R.drawable.tx_man, R.drawable.tx_man, R.drawable.tx_man);
		RoundImageLoaderUtil.getInstance(activity, 500).loadImage(obj.getPhoto(), iv_dresserphoto);
	}

	/**
	 * @Title: setHairStyleData
	 * @Description: 发型数据
	 * @author: lee
	 * @param obj
	 * @return: void
	 */
	void setHairStyleData(ReservationServiceResponse obj) {
		hairStyleList = obj.getHairStyle();
		// 是否有附加服务
		showServiceTxt();
		if ("0".equals(hairStyleId)) {
			showHiarStyleDialog(hairStyleList);
		} else {
			for (int i = 0; hairStyleList != null && i < hairStyleList.size(); i++) {
				if (hairStyleName != null && hairStyleName.equals(hairStyleList.get(i).Name)) {
					hairStyleList.get(i).isChecked = true;
					setHairStyleType(hairStyleList);
				}
			}
		}
	}
	
	private String getTips()
	{
		String ret = "";
		if (hairStyleList.size() > 0)
		{
			if (!haveService(hairStyleList.get(0), "tf"))
			{
				ret = "未开放";
			}
			else
			{
				ret = "可选";
			}
		}
		return ret;
	}
	
	// add by jiazhaohui
	private void showServiceTxt()
	{
		String tips = getTips();
		tv_tf.setText(tips);
		tv_rf.setText(tips);
		tv_hl.setText(tips);
	}

	/**
	 * 
	 * @Title: addRadioButton
	 * @Description: 添加日期
	 * @author: Johnny
	 * @return: void
	 */
	private void setScheduleData(ReservationServiceResponse obj) {
		scheduleList = obj.getSchedule();
		if (scheduleList == null || scheduleList.size() <= 0) {
			fl_schedule.setVisibility(View.GONE);
			tv_schedule_null.setVisibility(View.VISIBLE);
			return;
		}
		for (int i = 0; i < scheduleList.size(); i++) {

			final RadioButton rb = new RadioButton(activity);
			android.view.ViewGroup.LayoutParams params = new LayoutParams(ScreenUtils.dip2px(activity, 80),
					LayoutParams.MATCH_PARENT);
			rb.setLayoutParams(params);
			rb.setButtonDrawable(android.R.color.transparent);
			rb.setGravity(Gravity.CENTER);
			rb.setBackground(activity.getResources().getDrawable(R.drawable.storedetail_radiobutton_bg));
			rb.setSingleLine(true);
			rb.setText(scheduleList.get(i).Date + "(" + scheduleList.get(i).WeekDay + ")");
			rb.setTextColor(activity.getResources().getColor(R.color.black33));
			rb.setOnCheckedChangeListener(new OnCheckedChangeListener() {// 次监听用于解决radiobutton无法改变字体颜色的bug

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					int black = activity.getResources().getColor(R.color.black);
					int red = activity.getResources().getColor(R.color.red_e5);
					rb.setTextColor(isChecked ? red : black);
				}
			});
			rb.setTextSize(13);

			rb.setOnClickListener(new MyOnClickListener(scheduleList.get(i).Item, i));
			radioGroup.addView(rb);
			rb.setChecked(i == 0 ? true : false);
		}

		List<ReservationServiceResponse.Schedule.Item> item = scheduleList.get(0).Item;
		schTimeList.clear();
		schTimeList.addAll(item);
		scheduleAdapter.notifyDataSetChanged();
	}

	/**
	 * @Title: getReservationMaterialData
	 * @Description: 获取预约物料数据
	 * @author: Johnny
	 * @return: void
	 */
	public void getReservationMaterialData(final int tag) {
		if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
			ToastUtils.showShort(activity.getResources().getString(R.string.no_networks_found));
			LoadingDailog.dismiss();
			return;
		}
		isMaterialLoading = true;
		RequestCallBack<String> callback = new RequestCallBack<String>() {

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
					List<ReservationMaterialResponse> response = ReservationMaterialResponse
							.getclazz2(result.getData());
					resultReservationMaterialHanlder(response, tag);
				} else {
					LoadingDailog.dismiss();
				}
				isMaterialLoading = false;

			}

			@Override
			public void onFailure(HttpException error, String msg) {
				isMaterialLoading = false;
				LoadingDailog.dismiss();
				ToastUtils.showShort(activity.getResources().getString(R.string.request_failure));
			}
		};
		int UserId = UserManager.getUserID();
		if (OrderDetailFragment.class.getSimpleName().equals(from)
				|| OrderFragment.class.getSimpleName().equals(from)) {
			// ReservationServiceAgainRequest request = new
			// ReservationServiceAgainRequest(""+ UserId, ID);
			// ReservationServiceApi.ReservationServiceAgain(request,callback);
			ReservationMaterialAgainRequest request = new ReservationMaterialAgainRequest("" + UserId, againOrderID);
			ReservationMaterialApi.ReservationMaterialAgain(request, callback);
		} else {
			ReservationMaterialRequest request = new ReservationMaterialRequest("" + UserId, hs.ID);
			ReservationMaterialApi.reservationMaterial(request, callback);
		}
	}

	/**
	 * @Title: resultHanlder
	 * @Description: 物料结果处理
	 * @author: Johnny
	 * @param listcontrolSignUp
	 * @return: void
	 */
	void resultReservationMaterialHanlder(List<ReservationMaterialResponse> info, int tag) {
		if (info == null) {
			LogUtils.e("ReservationMaterialResponse err");
			return;
		}

		for (int i = 0; i < info.size(); i++) {
			if (info.get(i).getCode().equals("tf")) {
				tfList = info.get(i).getItem();
			} else if (info.get(i).getCode().equals("rf")) {
				rfList = info.get(i).getItem();
			} else if (info.get(i).getCode().equals("hl")) {
				hlList = info.get(i).getItem();
			}
		}
		LoadingDailog.dismiss();
		if (tag == 0) {
			showMaterialDialog(tfList, 0);
		} else if (tag == 1) {
			showMaterialDialog(rfList, 1);
		} else if (tag == 2) {
			showMaterialDialog(hlList, 2);
		}

	}

	/**
	 * @Title: getReservationStoreData
	 * @Description: 获取预约美发店数据
	 * @author: Johnny
	 * @return: void
	 */
	public void getReservationStoreData(String WHID) {
		if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
			ToastUtils.showShort(activity.getResources().getString(R.string.no_networks_found));
			return;
		}
//		LoadingDailog.show(activity, "正在获取美发店~");
		int UserId = UserManager.getUserID();
		String times = service.getTotalTimes();
		ReservationStoreRequest request = new ReservationStoreRequest("" + UserId, id, WHID, times);
		ReservationStoreApi.reservationStore(request, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
//				LoadingDailog.dismiss();
				BaseResponse result = BaseResponse.getBaseResponse(responseInfo.result);
				if (result == null) {
					return;
				}
				String[] split = result.getMsg().split("\\|");
				if ("1".equals(split[0])) {
					ToastUtils.showShort(split[1] + "");
				}
				if (result.getCode() == 1000) {

					List<ReservationStoreResponse> response = ReservationStoreResponse.getclazz2(result.getData());
					resultReservationStoreHanlder(response);
				} else if (result.getCode() == 2100) {
					resultReservationStoreHanlder(null);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				new LoadingViewUtil(view).hint();
//				LoadingDailog.dismiss();
				ToastUtils.showShort(activity.getResources().getString(R.string.request_failure));
			}
		});
	}

	/**
	 * @Title: resultHanlder
	 * @Description: 美发店结果处理
	 * @author: Johnny
	 * @param listcontrolSignUp
	 * @return: void
	 */
	void resultReservationStoreHanlder(List<ReservationStoreResponse> info) {
		storeList.clear();
		if (info != null) {
			storeList.addAll(info);
		}
		storeAdapter.notifyDataSetChanged();
	}

	/**
	 * 
	 * @Title: getCouponData
	 * @Description: 请求优惠劵接口
	 * @author: Johnny
	 * @return: void
	 */
	private void getCouponData() {
		if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
			ToastUtils.showShort(activity.getResources().getString(R.string.no_networks_found));
			return;
		}

		int userId = UserManager.getUserID();
		String code = service.getCodes();
		String money = service.getTotalPrice();
		CanUseCouponsRequest request = new CanUseCouponsRequest("" + userId, money, code);
		CanUseCouponsApi.canUseCoupons(request, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				if (!isMaterialLoading) {
					LoadingDailog.dismiss();
				}
				BaseResponse result = BaseResponse.getBaseResponse(responseInfo.result);
				if (result == null) {
					return;
				}
				String[] split = result.getMsg().split("\\|");
				if ("1".equals(split[0])) {
					ToastUtils.showShort(split[1] + "");
				}
				if (result.getCode() == 1000) {
					List<CanUseCouponsResponse> response = CanUseCouponsResponse.getclazz2(result.getData());
					resultCouponHanlder(response);
				} else if (result.getCode() == 2100) {
					// ev.setNullState();
					resultCouponHanlder(null);
				} else {
					// ev.setErrState();
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				MobclickAgent.onEvent(activity, "mybalance_failure");
				LoadingDailog.dismiss();
				ToastUtils.showShort(activity.getResources().getString(R.string.request_failure));

			}
		});

	}

	/**
	 * @Title: resultCouponHanlder
	 * @Description: 结果处理
	 * @author: Johnny
	 * @param listcontrolSignUp
	 * @return: void
	 */
	void resultCouponHanlder(List<CanUseCouponsResponse> info) {
		try {
			float totle = 0;
			StringBuffer sb = new StringBuffer();
			for (int i = 0; info != null && i < info.size(); i++) {
				sb.append(info.get(i).getID()).append("|");
				totle += Float.parseFloat(info.get(i).getAmount());
			}
			tv_discount.setText("" + totle);
			tv_discount.setTag(sb.toString());
			tv_money.setText(Float.parseFloat(service.getTotalPrice()) - totle + "");
		} catch (Exception e) {
		}
	}

	private boolean isCommiting = false;

	/**
	 * @Title: commitOrderData
	 * @Description: 提交订单
	 * @author: Johnny
	 * @return: void
	 */
	public void commitOrderData() {
		if (isCommiting == true)
			return;
		if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
			ToastUtils.showShort(activity.getResources().getString(R.string.no_networks_found));
			return;
		}
		int UserId = UserManager.getUserID();
		// String hairStyleId = hs.ID;
		String times = service.getTotalTimes();
		String money = service.getTotalPrice();
		String mastMoney = tv_money.getText().toString();
		String couponID = (String) tv_discount.getTag();
		if (TextUtils.isEmpty(couponID)) {
			couponID = "";
		}
		String styleId = hs.ID;
		if (scheduleAdapter.getSelectedIndex() == -1
				|| scheduleAdapter.getItem(scheduleAdapter.getSelectedIndex()) == null) {
			ToastUtils.showLong("请选择预约时间");
			return;
		}
		String WHID = scheduleAdapter.getItem(scheduleAdapter.getSelectedIndex()).WHID;// 发型师工时ID
		if (storeAdapter.getCount() <= 0 || storeAdapter.getItem(storeAdapter.getSelectedIndex()) == null) {
			ToastUtils.showLong("请选择预约美发店");
			return;
		}
		String StoreID = storeAdapter.getItem(storeAdapter.getSelectedIndex()).getID();// 店铺ID
		List<CommitOrderRequest.Item> item = service.getMaterialId();
		LoadingDailog.show(activity, "正在生成订单，请稍后");
		CommitOrderRequest request = new CommitOrderRequest("" + UserId, id, hairStyleId, styleId, times, couponID,
				money, mastMoney, WHID, StoreID, "0", item);
		CommitOrderApi.commitOrder(request, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();
				isCommiting = true;
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				LoadingDailog.dismiss();
				isCommiting = false;
				BaseResponse result = BaseResponse.getBaseResponse(responseInfo.result);
				if (result == null) {
					return;
				}
				String[] split = result.getMsg().split("\\|");
				if ("1".equals(split[0])) {
					ToastUtils.showShort(split[1] + "");
				}
				if (result.getCode() == 1000) {

					List<CommitOrderResponse> response = CommitOrderResponse.getclazz2(result.getData());
					resultCommitOrderHanlder(response);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				isCommiting = false;
				LoadingDailog.dismiss();
				ToastUtils.showShort(activity.getResources().getString(R.string.request_failure));
			}
		});
	}

	/**
	 * @Title: resultHanlder
	 * @Description: 订单结果处理
	 * @author: Johnny
	 * @param listcontrolSignUp
	 * @return: void
	 */
	void resultCommitOrderHanlder(List<CommitOrderResponse> info) {
		String orderID = "0";// 订单ID
		if (info != null) {
			orderID = info.get(0).getOrderID();
		}

		Intent intent = OrderPayActivity.getIntent(activity, OrderPayFragment.class.getName());
		intent.putExtra("OrderID", orderID);
		startActivity(intent);
		activity.finish();
	}

	// 是否提供这项服务
	private boolean haveService(HairStyle ths, String code)
	{
		if (ths != null)
		{
			for (int i = 0; i < ths.Item.size(); i++)
			{
				if (ths.Item.get(i).Code.equals(code))
				{
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * 
	 * @Title: onClick
	 * @Description: 定义点击事件
	 * @param v
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	@OnClick({ R.id.rl_hairstyle_reserve, R.id.ll1_reserve, R.id.ll2_reserve, R.id.rl_tf_reserve, R.id.rl_rf_reserve,
			R.id.rl_hl_reserve, R.id.tv_pay_reserve })
	public void onClick(View v) {

		switch (v.getId()) {
		// 发型类型的布局
		case R.id.rl_hairstyle_reserve:
			showHiarStyleDialog(hairStyleList);
			break;

		// 发型选项1的布局
		case R.id.ll1_reserve:
			setIcon(v.getId());
			items.put("styleCode", "xjc");
			items.put("stylePrice", tv_item1.getText().toString().replaceAll("¥", ""));
			items.put("styleTimes", items.get("styleTimes1"));
			items.put("styleMaterialID", "0");
			getCouponData();
			break;

		// 发型选项2的布局
		case R.id.ll2_reserve:
			setIcon(v.getId());

			items.put("styleCode", "xc");
			items.put("stylePrice", tv_item2.getText().toString().replaceAll("¥", ""));
			items.put("styleTimes", items.get("styleTimes2"));
			items.put("styleMaterialID", "0");
			getCouponData();
			break;

		// 烫发的布局
		case R.id.rl_tf_reserve:
			if (hs == null) {
				showAlertDialog();
			} else if (tfList == null) {
				LoadingDailog.show(activity, "数据加载中，请稍后");
				getReservationMaterialData(0);
			} else {
				if (haveService(hs, Constant.SERVICE_TF_CODE))
				{
					showMaterialDialog(tfList, 0);
				}
			}
			break;

		// 染发的布局
		case R.id.rl_rf_reserve:
			if (hs == null) {
				showAlertDialog();
			} else if (rfList == null) {
				LoadingDailog.show(activity, "数据加载中，请稍后");
				getReservationMaterialData(1);
			} else {
				if (haveService(hs, Constant.SERVICE_RF_CODE))
				{
					showMaterialDialog(rfList, 1);
				}
			}
			break;

		// 护理的布局
		case R.id.rl_hl_reserve:
			if (hs == null) {
				showAlertDialog();
			} else if (hlList == null) {
				LoadingDailog.show(activity, "数据加载中，请稍后");
				getReservationMaterialData(2);
			} else {
				if (haveService(hs, Constant.SERVICE_HL_CODE))
				{
					showMaterialDialog(hlList, 2);
				}
			}
			break;
		// 去支付按钮
		case R.id.tv_pay_reserve:
			if (hs == null) {
				showAlertDialog();
			} else {
				commitOrderData();
			}
			break;

		default:
			break;
		}

	}

	/**
	 * 
	 * @Title: showAlertDialog
	 * @Description: 未选发型
	 * @author: Johnny
	 * @return: void
	 */
	private void showAlertDialog() {
		final MessageDialog alertDialog = new MessageDialog(activity);
		alertDialog.setMessage("请选择发型类型");
		alertDialog.getOkButton().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
			}
		});
		alertDialog.setCancelButtonGone(true);
		alertDialog.show();
	}

	/**
	 * 
	 * @Title: createListDialog
	 * @Description: 创建发型类型选项
	 * @author: Johnny
	 * @return: void
	 */
	private void showHiarStyleDialog(final List<HairStyle> dataList) {
		View view = LayoutInflater.from(activity).inflate(R.layout.reserve_dialog_layout, null);
		final MessageDialog messageDialog = new MessageDialog(activity, view);
		TextView tv_type = (TextView) view.findViewById(R.id.tv_type_reserve_dialog);// 发型类型
		tv_type.setText("发型类型");
		tv_type.setVisibility(View.GONE);
		TextView tv_tip = (TextView) view.findViewById(R.id.tv_reserve_dialog_tip);// 发型类型
		tv_tip.setVisibility(View.VISIBLE);
		MyListView lv_dialog = (MyListView) view.findViewById(R.id.listview_dialog);
		final HairStyleAdapter adapter = new HairStyleAdapter(activity, dataList);
		lv_dialog.setAdapter(adapter);
		lv_dialog.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				for (int i = 0; i < dataList.size(); i++) {
					if (dataList.get(i).isChecked == true) {
						dataList.get(i).isChecked = false;
						break;
					}
				}
				dataList.get(position).isChecked = true;
				adapter.notifyDataSetChanged();
			}
		});
		view.findViewById(R.id.tv_reserve_ok).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				messageDialog.dismiss();
				setHairStyleType(dataList);
			}
		});
		view.findViewById(R.id.tv_reserve_cancel).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				messageDialog.dismiss();
			}
		});
		messageDialog.show();
	}

	/**
	 * @Title: setHairStyleType
	 * @Description: 设置发型类型内容
	 * @author: lee
	 * @param dataList
	 * @return: void
	 */
	protected void setHairStyleType(List<HairStyle> dataList) {
		hs = null;
		service = new ServiceBean();
		for (int i = 0; dataList != null && i < dataList.size(); i++) {
			if (dataList.get(i).isChecked) {
				hs = dataList.get(i);
				break;
			}
		}
		if (hs == null) {// 未选择发型类型是的处理
			tv_hairstyle.setText("未选择");
			tv_hairstyle.setTextColor(activity.getResources().getColor(R.color.black33));
			cleanAllMaterial();
			return;
		}
		try {
			String text = hs.Name + "(用时" + service.getTotalTimes() + "分钟)";
			tv_hairstyle.setText(text);
			tv_hairstyle.setTextColor(activity.getResources().getColor(R.color.red_e5));
		} catch (Exception e) {
			LogUtils.e(e.getMessage());
		}

		setHairStyleMastItem(hs, R.id.ll1_reserve);// 设置发型类型为初始状态
		cleanAllMaterial();// 清空所有物理选择
		getReservationMaterialData(-1);// 提前请求物料数据，解决用户体验问题
		getCouponData();// 获取优惠券
	}

	/**
	 * @Title: setHairStyleMastItem
	 * @Description: 设置发型子项
	 * @author: lee
	 * @param hs
	 * @param selectViewId
	 * @return: void
	 */
	void setHairStyleMastItem(HairStyle hs, int selectViewId) {
		if (hs == null) {
			tv_item1.setVisibility(View.GONE);
			tv_item1_price.setVisibility(View.GONE);
			tv_item2_price.setVisibility(View.GONE);
			tv_item2.setVisibility(View.GONE);
			return;
		}
		tv_item1.setText(hs.Item.get(0).Name);
		tv_item1.setVisibility(View.VISIBLE);
		tv_item1_price.setText("¥" + hs.Item.get(0).Price);
		tv_item1_price.setVisibility(View.VISIBLE);
		iv1.setVisibility(View.VISIBLE);

		tv_item2.setText(hs.Item.get(1).Name);
		tv_item2.setVisibility(View.VISIBLE);
		tv_item2_price.setText("¥" + hs.Item.get(1).Price);
		tv_item2_price.setVisibility(View.VISIBLE);
		iv2.setVisibility(View.VISIBLE);
		setIcon(selectViewId);
	}

	/**
	 * 
	 * @Title: setIcon
	 * @Description: 设置点选icon
	 * @author: Johnny
	 * @return: void
	 */
	private void setIcon(int id) {
		iv1.setImageResource(
				id == R.id.ll1_reserve ? R.drawable.reserve_btn_selected : R.drawable.reserve_btn_unselected);
		iv2.setImageResource(
				id == R.id.ll2_reserve ? R.drawable.reserve_btn_selected : R.drawable.reserve_btn_unselected);
		service.priceType = id == R.id.ll1_reserve ? hs.Item.get(0).Price : hs.Item.get(1).Price;// 价格存储
		service.codeTypeName = id == R.id.ll1_reserve ? "xjc" : "xc";// 服务码存储
		service.timeType = id == R.id.ll1_reserve ? hs.Item.get(0).Times : hs.Item.get(1).Times;// 时长存储
		// StyleID = id == R.id.ll1_reserve?hs.ID:hs.Item.get(1).Times;
		refreshHairstyleType();
	}

	/**
	 * @Title: refreshHairstyleType
	 * @Description: 发型类型 时长的刷新
	 * @author: lee
	 * @return: void
	 */
	void refreshHairstyleType() {
		String text = hs.Name + "(用时" + service.getTotalTimes() + "分钟)";
		tv_hairstyle.setText(text);
		tv_hairstyle.setTextColor(activity.getResources().getColor(R.color.red_e5));
	}

	/**
	 * 
	 * @Title: showMaterialDialog
	 * @Description: 物料选择弹框
	 * @author: Johnny
	 * @param dataList
	 * @return: void
	 */
	@SuppressLint("InflateParams")
	private void showMaterialDialog(final List<Item> dataList, final int tag) {

		if (dialogView == null) {
			dialogView = LayoutInflater.from(activity).inflate(R.layout.reserve_dialog_layout, null);
			materialDialog = new MessageDialog(activity, dialogView);
		}
		TextView tv_type = (TextView) dialogView.findViewById(R.id.tv_type_reserve_dialog);// 发型类型
		final MyListView lv_dialog = (MyListView) dialogView.findViewById(R.id.listview_dialog);
		switch (tag) {
		case 0:
			tv_type.setText("烫发");
			break;
		case 1:
			tv_type.setText("染发");
			break;
		case 2:
			tv_type.setText("护理");
			break;
		}

		if (materialAdapter == null) {
			materialAdapter = new MaterialAdapter(activity, materialList, tag);
			lv_dialog.setAdapter(materialAdapter);
		}

		materialList.clear();
		materialList.addAll(dataList);
		materialAdapter.tag = tag;
		materialAdapter.notifyDataSetChanged();

		materialDialog.show();

		lv_dialog.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				for (int i = 0; i < dataList.size(); i++) {
					dataList.get(i).isChecked = false;
				}
				dataList.get(position).isChecked = true;
				materialList.clear();
				materialList.addAll(dataList);
				materialAdapter.notifyDataSetChanged();
			}
		});
		dialogView.findViewById(R.id.tv_reserve_ok).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				materialDialog.dismiss();
				setMaterialViewData(dataList, tag);
			}
		});
		dialogView.findViewById(R.id.tv_reserve_cancel).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				materialDialog.dismiss();
				for (int i = 0; i < dataList.size(); i++) {
					dataList.get(i).isChecked = false;
				}
				setMaterialViewData(dataList, tag);
			}
		});
	}

	/**
	 * @Title: setMaterialViewData
	 * @Description: 设置物料部分的数据内容
	 * @author: lee
	 * @param dataList
	 * @param tag
	 * @return: void
	 */
	protected void setMaterialViewData(List<Item> dataList, int tag) {
		ReservationMaterialResponse.Item item = null;
		for (int i = 0; dataList != null && i < dataList.size(); i++) {
			if (dataList.get(i).isChecked) {
				item = dataList.get(i);
				break;
			}
		}
		String tip = null;
		if (item == null) {
			tip = getTips();
			if (tag == 0) {
				tv_tf.setText(Html.fromHtml(tip));
				service.codeTf = "";
				service.timeTf = "0";
				service.materialidTf = "0";
				service.priceTf = "0";
			} else if (tag == 1) {
				tv_rf.setText(Html.fromHtml(tip));
				service.codeRf = "";
				service.timeRf = "0";
				service.materialidRf = "0";
				service.priceRf = "0";
			} else if (tag == 2) {
				tv_hl.setText(Html.fromHtml(tip));
				service.codeHl = "";
				service.timeHl = "0";
				service.materialidHl = "0";
				service.priceHl = "0";
			}
		} else {
			if (tag == 0) {
				// 烫发价格=烫发服务的价格+物料价格
				service.priceTf = ServiceBean.strPriceSum(hs.Item.get(2).Price, item.Price);
				service.codeTf = "tf";
				service.timeTf = hs.Item.get(2).Times;
				service.materialidTf = item.ID;
				tip = "<font color='#333333'>" + item.Name + "</font><font color='#e53123'>" + "   ¥" + service.priceTf
						+ "</font>";
				tv_tf.setText(Html.fromHtml(tip));
			} else if (tag == 1) {
				service.priceRf = ServiceBean.strPriceSum(hs.Item.get(3).Price, item.Price);
				service.codeRf = "rf";
				service.timeRf = hs.Item.get(3).Times;
				service.materialidRf = item.ID;
				tip = "<font color='#333333'>" + item.Name + "</font><font color='#e53123'>" + "   ¥" + service.priceRf
						+ "</font>";
				tv_rf.setText(Html.fromHtml(tip));
			} else if (tag == 2) {
				service.priceHl = ServiceBean.strPriceSum(hs.Item.get(4).Price, item.Price);
				service.codeHl = "hl";
				service.timeHl = hs.Item.get(4).Times;
				service.materialidHl = item.ID;
				tip = "<font color='#333333'>" + item.Name + "</font><font color='#e53123'>" + "   ¥" + service.priceHl
						+ "</font>";
				tv_hl.setText(Html.fromHtml(tip));
			}

		}
		getCouponData();// 刷新优惠券
		refreshHairstyleType();// 这时候该刷新时长了
	}

	/**
	 * @Title: cleanAllMaterial
	 * @Description: 清空所有物料信息
	 * @author: lee
	 * @return: void
	 */
	void cleanAllMaterial() {
		tv_tf.setText(getTips());
		tv_rf.setText(getTips());
		tv_hl.setText(getTips());
		service.priceTf = "0";
		service.priceRf = "0";
		service.priceHl = "0";
	}

	/**
	 * 
	 * @ClassName: MyOnClickListener
	 * @Description: 日程选择
	 * @author: Johnny
	 * @date: 2015年10月28日 下午5:26:09
	 */
	private class MyOnClickListener implements OnClickListener {
		private List<ReservationServiceResponse.Schedule.Item> item;
		private int position;

		MyOnClickListener(List<ReservationServiceResponse.Schedule.Item> data, int i) {
			item = data;
			position = i;
		}

		@Override
		public void onClick(View v) {
			if (item == null || item.size() <= 0) {
				return;
			}
			scrollItemPosition = position;
			schTimeList.clear();
			schTimeList.addAll(item);
			scheduleAdapter.setSelectedIndex(-1);
			scheduleAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 
	 * @ClassName: HairStyleAdapter
	 * @Description: dialog内发型ListView的adapter
	 * @author: Johnny
	 * @date: 2015年10月24日 上午11:03:43
	 */
	class HairStyleAdapter extends BaseAdapter {

		private Context context;
		private List<HairStyle> list;

		public HairStyleAdapter(Context context, List<HairStyle> list) {
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
		public HairStyle getItem(int position) {
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
				convertView = LayoutInflater.from(context).inflate(R.layout.reserve_listview_item_dialog, null);
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
		 * @Description: 绑定数据
		 * @author: Johnny
		 * @param position
		 * @param vh
		 * @return: void
		 */
		private void setLabel(int position, ViewHolder vh) {
			HairStyle obj = getItem(position);

			StringBuffer sb = new StringBuffer();
			sb.append(hairStyleList.get(position).Name);
			vh.tv_left.setText(sb.toString());
			vh.tv_left.setTextColor(activity.getResources().getColor(R.color.black33));
			vh.iv_select_right.setVisibility(View.VISIBLE);
			if (obj.isChecked) {
				vh.iv_select_right.setImageResource(R.drawable.reserve_btn_selected);
				vh.tv_left.setTextColor(activity.getResources().getColor(R.color.red_e5));
			} else {
				vh.iv_select_right.setImageResource(R.drawable.reserve_btn_unselected);
				vh.tv_left.setTextColor(activity.getResources().getColor(R.color.black33));
			}
			vh.tv_right.setVisibility(View.INVISIBLE);
			vh.iv_select.setVisibility(View.GONE);
		}
	}

	/**
	 * 
	 * @ClassName: HairStyleAdapter
	 * @Description: dialog内发型ListView的adapter
	 * @author: Johnny
	 * @date: 2015年10月24日 上午11:03:43
	 */
	class MaterialAdapter extends BaseAdapter {

		private Context context;
		private List<Item> list;
		private int tag;

		public MaterialAdapter(Context context, List<Item> list, int tag) {
			this.context = context;
			this.list = list;
			this.tag = tag;
		}

		@Override
		public int getCount() {
			return list == null ? 0 : list.size();
		}

		@Override
		public Item getItem(int position) {
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
				convertView = LayoutInflater.from(context).inflate(R.layout.reserve_listview_item_dialog, null);
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
		 * @Description: 绑定数据
		 * @author: Johnny
		 * @param position
		 * @param vh
		 * @return: void
		 */
		private void setLabel(int position, ViewHolder vh) {
			Item obj = getItem(position);

			vh.tv_left.setText(obj.Name);
			String price = "¥";
			switch (tag) {
			case 0:
				price += ServiceBean.strPriceSum(hs.Item.get(2).Price, obj.Price);
				break;
			case 1:
				price += ServiceBean.strPriceSum(hs.Item.get(3).Price, obj.Price);
				break;
			case 2:
				price += ServiceBean.strPriceSum(hs.Item.get(4).Price, obj.Price);
				break;
			}

			vh.tv_right.setText(price);
			vh.tv_right.setVisibility(View.VISIBLE);
			if (obj.isChecked) {
				// vh.btn.setImageResource(R.drawable.reserve_btn_selected);
				vh.tv_left.setTextColor(activity.getResources().getColor(R.color.red_e5));
				// vh.tv_right.setTextColor(activity.getResources().getColor(
				// R.color.red_e5));
				vh.iv_select.setImageResource(R.drawable.reserve_btn_selected);
			} else {
				vh.tv_left.setTextColor(activity.getResources().getColor(R.color.black33));
				// vh.tv_right.setTextColor(activity.getResources().getColor(
				// R.color.black33));
				vh.iv_select.setImageResource(R.drawable.reserve_btn_unselected);
			}
		}
	}

	/**
	 * 
	 * @ClassName: HairscheduleAdapter
	 * @Description: 日程的adapter
	 * @author: Johnny
	 * @date: 2015年10月24日 上午11:03:43
	 */
	class ScheduleAdapter extends BaseAdapter {

		private Context context;
		private List<ReservationServiceResponse.Schedule.Item> list;
		private int selectedIndex = -1;

		public ScheduleAdapter(Context context, List<ReservationServiceResponse.Schedule.Item> list) {
			this.context = context;
			this.list = list;
		}

		public int getSelectedIndex() {
			return selectedIndex;
		}

		public void setSelectedIndex(int selectedIndex) {
			this.selectedIndex = selectedIndex;
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
		public ReservationServiceResponse.Schedule.Item getItem(int position) {
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
				convertView = LayoutInflater.from(context).inflate(R.layout.reserve_gridview_item_layout, null);
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
		 * @Description: 绑定数据
		 * @author: Johnny
		 * @param position
		 * @param vh
		 * @return: void
		 */
		private void setLabel(int position, ViewHolder vh) {
			ReservationServiceResponse.Schedule.Item obj = getItem(position);
			// -1休息0可约1锁定2已预约
			if (obj.State.equals(Constant.SCHEDULE_STATE_YET_ORDER)) {
				vh.tv_gv.setText(obj.Hour + ":00" + "(约满)");
				vh.tv_gv.setTextColor(activity.getResources().getColor(R.color.black33));
				vh.tv_gv.setBackgroundColor(activity.getResources().getColor(R.color.gray_d8));
			} else if (obj.State.equals(Constant.SCHEDULE_STATE_LOCK)) {
				vh.tv_gv.setText(obj.Hour + ":00" + "(约满)");// 锁定状态显示约满
				vh.tv_gv.setTextColor(activity.getResources().getColor(R.color.black33));
				vh.tv_gv.setBackgroundColor(activity.getResources().getColor(R.color.gray_d8));
			} else if (obj.State.equals(Constant.SCHEDULE_STATE_CAN_ORDER)) {
				vh.tv_gv.setText(obj.Hour + ":00" + "(可约)");
				if (selectedIndex == position) {
					vh.tv_gv.setBackgroundColor(activity.getResources().getColor(R.color.red_e5));
					vh.tv_gv.setTextColor(activity.getResources().getColor(R.color.white));
				} else {
					vh.tv_gv.setBackgroundColor(activity.getResources().getColor(R.color.gray_f5));
					vh.tv_gv.setTextColor(activity.getResources().getColor(R.color.black33));
				}
			} else if (obj.State.equals(Constant.SCHEDULE_STATE_REST)) {
				vh.tv_gv.setText(obj.Hour + ":00" + "(休息)");
				vh.tv_gv.setTextColor(activity.getResources().getColor(R.color.black33));
				vh.tv_gv.setBackgroundColor(activity.getResources().getColor(R.color.gray_d8));
			}else if (obj.State.equals(Constant.SCHEDULE_STATE_OVERDUE)){
				vh.tv_gv.setText(obj.Hour + ":00" + "(过期)");
				vh.tv_gv.setTextColor(activity.getResources().getColor(R.color.black33));
				vh.tv_gv.setBackgroundColor(activity.getResources().getColor(R.color.gray_d8));
			}
		}
	}

	/**
	 * 
	 * @ClassName: ReservationStoreAdapter
	 * @Description: 商户的adapter
	 * @author: Johnny
	 * @date: 2015年10月24日 上午11:03:43
	 */
	class ReservationStoreAdapter extends BaseAdapter {

		private Context context;
		private List<ReservationStoreResponse> list;
		private int selectedIndex = 0;

		public ReservationStoreAdapter(Context context, List<ReservationStoreResponse> list) {
			this.context = context;
			this.list = list;
		}

		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
			sv_content.fullScroll(ScrollView.FOCUS_DOWN);
		}

		public int getSelectedIndex() {
			return selectedIndex;
		}

		public void setSelectedIndex(int selectedIndex) {
			this.selectedIndex = selectedIndex;
		}

		@Override
		public int getCount() {
			return list == null ? 0 : list.size();
		}

		@Override
		public ReservationStoreResponse getItem(int position) {
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
				convertView = LayoutInflater.from(context).inflate(R.layout.reserve_listview_item_layout, null);
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
		 * @Description: 绑定数据
		 * @author: Johnny
		 * @param position
		 * @param vh
		 * @return: void
		 */
		private void setLabel(final int position, ViewHolder vh) {
			ReservationStoreResponse obj = getItem(position);
			// vh.tv_gv.setText(obj.Hour + ":00");
			vh.tv_store.setText(obj.getName());
			vh.tv_address.setText(obj.getAddress());
			if (selectedIndex == position) {
				vh.btn_select.setImageResource(R.drawable.reserve_btn_selected);
				vh.iv_location.setImageResource(R.drawable.location_icon);
			} else {
				vh.btn_select.setImageResource(R.drawable.reserve_btn_unselected);
				vh.iv_location.setImageResource(R.drawable.localtionaddress);
			}
			vh.rl_select.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = ReserveActivity.getIntent(context,
							StoreDetailFragment.class.getName());
					intent.putExtra("ID", list.get(position).getID());
					startActivity(intent);
				}
			});
		}
	}

	static class ViewHolder {

		@ViewInject(R.id.tv_left_lv_reserve_dialog)
		TextView tv_left;
		@ViewInject(R.id.tv_right_lv_reserve_dialog)
		TextView tv_right;
		@ViewInject(R.id.tv_gv_reserve)
		TextView tv_gv;
		@ViewInject(R.id.tv_store_reserve)
		TextView tv_store;
		@ViewInject(R.id.tv_address_reserve)
		TextView tv_address;
		@ViewInject(R.id.btn_select_icon_reserve)
		ImageView btn_select;
		@ViewInject(R.id.iv_location_icon_reserve)
		ImageView iv_location;
		@ViewInject(R.id.btn_select_icon_lv_reserve_dialog)
		ImageView iv_select;
		@ViewInject(R.id.iv_select_right)
		ImageView iv_select_right;
		@ViewInject(R.id.rl_reserve_listview_item_layout)
		RelativeLayout rl_select;

		void findView(View convertView) {
			ViewUtils.inject(this, convertView);
		}
	}

	// ------------------------------------------------工具---------------------------------------------------------
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
			android.view.ViewGroup.LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.MATCH_PARENT);
			imageView.setLayoutParams(params);
			imageView.setScaleType(ScaleType.FIT_CENTER);
			// int padding = (int) TypedValue.applyDimension(
			// TypedValue.COMPLEX_UNIT_DIP, 5, activity.getResources()
			// .getDisplayMetrics());
			// imageView.setPadding(0, padding, padding, padding);
			// int width = (int) TypedValue.applyDimension(
			// TypedValue.COMPLEX_UNIT_DIP, 120, activity.getResources()
			// .getDisplayMetrics()) / 5;
			ll.setGravity(Gravity.CENTER_VERTICAL);
			ll.addView(imageView, ScreenUtils.dip2px(activity, 15), LayoutParams.MATCH_PARENT);
		}
	}

	/**
	 * @Title: onEventMainThread
	 * @Description: eventbus内容处理
	 * @author: lee
	 * @param bean
	 * @return: void
	 */
	public void onEventMainThread(EventOrderBean bean) {
		if (bean.isPay) {
			if (activity.isFinishing() || !activity.isDestroyed()) {
				activity.finish();
			}
		}
	}

}
