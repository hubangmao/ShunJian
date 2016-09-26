package cc.ruit.shunjianmei.reserve;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import cc.ruit.shunjianmei.R;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.constants.Constant;
import cc.ruit.shunjianmei.home.order.OrderDetailFragment;
import cc.ruit.shunjianmei.home.order.OrderFragment;
import cc.ruit.shunjianmei.home.store.StoreDetailFragment;
import cc.ruit.shunjianmei.net.api.CanUseCouponsApi;
import cc.ruit.shunjianmei.net.api.CommitOrderApi;
import cc.ruit.shunjianmei.net.api.GetAllusersubinfoPackedBysubidApi;
import cc.ruit.shunjianmei.net.api.ReservationServiceApi;
import cc.ruit.shunjianmei.net.api.ReservationStoreApi;
import cc.ruit.shunjianmei.net.request.CanUseCouponsRequest;
import cc.ruit.shunjianmei.net.request.CommitOrderRequest;
import cc.ruit.shunjianmei.net.request.GetAllusersubinfoPackedBysubidRequest;
import cc.ruit.shunjianmei.net.request.ReservationServiceAgainRequest;
import cc.ruit.shunjianmei.net.request.ReservationServiceRequest;
import cc.ruit.shunjianmei.net.request.ReservationStoreRequest;
import cc.ruit.shunjianmei.net.response.CanUseCouponsResponse;
import cc.ruit.shunjianmei.net.response.CommitOrderResponse;
import cc.ruit.shunjianmei.net.response.GetAllusersubinfoPackedBysubidResponse;
import cc.ruit.shunjianmei.net.response.ReservationMaterialResponse.Item;
import cc.ruit.shunjianmei.net.response.ReservationServiceResponse;
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
import de.greenrobot.event.EventBus;

/**
 * @ClassName: ReserveFragment
 * @Description: 预约界面
 * @author: Johnny
 * @date: 2015年10月14日 下午4:32:59
 */

@SuppressLint("InflateParams")
public class ReserveFreeFragment extends BaseFragment implements OnClickListener {
	/**
	 * 单选按钮
	 */
	private int posi_finishok = -1;
	private String posi_finish;
	private GetAllusersubinfoPackedBysubidResponse finish_data;
	private String UserId;
	private String finishmoney = "0";
	private int clickRadioGroup = 0;
	private int tag = 0;
	private List<GetAllusersubinfoPackedBysubidResponse> alllist;
	private GetAllusersubinfoPackedBysubidResponse finishData;
	@ViewInject(R.id.iv_dresserphoto_reserve)
	private ImageView img;
	private int posi;
	@ViewInject(R.id.radiogroup)
	private RadioGroup rg_hair_money;
	@ViewInject(R.id.rb_hair_long)
	private RadioButton rb_hair_long;
	@ViewInject(R.id.rb_hair_in)
	private RadioButton rb_hair_in;
	@ViewInject(R.id.rb_hair_short)
	private RadioButton rb_hair_short;
	private Boolean yn = false;
	/**
	 * 支付
	 */
	// private String finish_money = "0";
	private String mastMoney;
	GetAllusersubinfoPackedBysubidResponse finishdata;
	private String finish_Amount = "0";
	private String couponID = "";
	private String mUserid = "0";
	// *****
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
	private FrameLayout fl_schedule;// 没有安排工作时间隐藏此布局DDDAA
	@ViewInject(R.id.tv_fl_reserve_scrollview_layout_null)
	private TextView tv_schedule_null;// 没有安排工作时间显示的内容

	// private MaterialAdapter materialAdapter;
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
	// HairStyle hs = null;// 发型类型数据对象

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
		view = inflater.inflate(R.layout.reserve_free_layout, null);
		ViewUtils.inject(this, view);

		initTitle();
		initData();
		initChack();

		return view;
	}

	/**
	 * @Title: initTitle
	 * @Description: 标题初始化
	 * @author: Johnny
	 * @return: void
	 */
	private void initTitle() {
		getCouponData();// 刷新优惠券
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
		initPackage();
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
			mUserid = getArguments().getString("Userid");
			if (mUserid == null) {
				mUserid = "0";
			}
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
		setScheduleData(obj);
		initPackage();
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
		// LoadingDailog.show(activity, "正在获取美发店~");
		int UserId = UserManager.getUserID();
		String times = service.getTotalTimes();
		ReservationStoreRequest request = new ReservationStoreRequest("" + UserId, id, WHID, times);
		ReservationStoreApi.reservationStore(request, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				// LoadingDailog.dismiss();
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
				// LoadingDailog.dismiss();
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
	 * @author: jny
	 * @return: void
	 */
	private void getCouponData() {
		if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
			ToastUtils.showShort(activity.getResources().getString(R.string.no_networks_found));
			return;
		}

		int userId = UserManager.getUserID();
		String code = "tc";
		String money = "12";

		CanUseCouponsRequest request = new CanUseCouponsRequest("" + userId, money, code);
		request.toJsonString(request);
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
					if (result != null) {
						List<CanUseCouponsResponse> response = CanUseCouponsResponse.getclazz2(result.getData());
						if (response != null && response.size() != 0) {
							couponID = response.get(0).getID();
							finish_Amount = response.get(0).getAmount();
							tv_discount.setText("￥" + finish_Amount);

						} else {

						}
					} else {

					}

				} else if (result.getCode() == 2100) {
				} else {
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

	private boolean isCommiting = false;

	/**
	 * @Title: commitOrderData
	 * @Description: 提交订单
	 * @author: Johnny
	 * @return: void
	 */

	public void commitOrderData() {
		rb_hair_long.setChecked(false);
		rb_hair_in.setChecked(false);
		rb_hair_short.setChecked(false);
		if (isCommiting == true) {
			return;
		}
		if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
			ToastUtils.showShort(activity.getResources().getString(R.string.no_networks_found));
			return;
		}
		if (!mUserid.equals("0")) {
			UserId = mUserid;
		} else {
			UserId = UserManager.getUserID() + "";
		}

		String times = finishdata.getTimes();
		String money = finishmoney;
		Double amoney = Double.valueOf(money).doubleValue() - Double.valueOf(finish_Amount).doubleValue();
		mastMoney = amoney + "";
		String styleId = finishdata.getId();
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

		CommitOrderRequest request = new CommitOrderRequest(UserId + "", id, hairStyleId, styleId, times, couponID,
				money, mastMoney, WHID, StoreID, "1", item);
		// Log.i("Tag", "Tag=" + request.toJsonString(request));
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

	/**
	 * 
	 * @Title: onClick
	 * @Description: 定义点击事件
	 * @param v
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	@OnClick({ R.id.rl_hairstyle_reserve, R.id.ll1_reserve, R.id.ll2_reserve, R.id.tv_pay_reserve })
	public void onClick(View v) {

		switch (v.getId()) {
		// 发型类型的布局
		case R.id.rl_hairstyle_reserve:
			initPackage();
			break;

		// 去支付按钮
		case R.id.tv_pay_reserve:

			if (tv_money.getText().toString().length() == 0) {
				initPackage();
			} else {
				if (!finishmoney.equals("0")) {
					commitOrderData();
				} else {
					Toast.makeText(ReserveFreeFragment.this.activity, "请选择发型长度", 1000).show();
				}

			}

			break;

		default:
			break;
		}

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
		private List<GetAllusersubinfoPackedBysubidResponse> list;
		HashMap<String, Boolean> states = new HashMap<String, Boolean>();// 用于记录每个RadioButton的状态，并保证只可选一个

		public HairStyleAdapter(Context context, List<GetAllusersubinfoPackedBysubidResponse> list) {
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
			return list.size();
			// return 5;
			// return list == null ? 0 : list.size();
		}

		@Override
		public GetAllusersubinfoPackedBysubidResponse getItem(int position) {
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
				convertView = LayoutInflater.from(context).inflate(R.layout.reserve_listview_item_free_dialog, null);
				vh.findView(convertView);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			// 得到数据和绑定数据
			setLabelDialog(position, vh);
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
		private void setLabelDialog(final int position, final ViewHolder vh) {
			final GetAllusersubinfoPackedBysubidResponse obj = getItem(position);
			// i = position;
			String Longhair = "<font color='#333333'>长发  :</font><font color='#f34c28'>" + " ￥" + obj.getLonghair()
					+ "</font>";
			vh.tv_long_hair_price.setText(Html.fromHtml(Longhair));
			String Inhair = "<font color='#333333'>中发  :</font><font color='#f34c28'>" + " ￥" + obj.getInhair()
					+ "</font>";
			vh.tv_in_hair_price.setText(Html.fromHtml(Inhair));
			String Shorthair = "<font color='#333333'>短发  :</font><font color='#f34c28'>" + " ￥" + obj.getShorthair()
					+ "</font>";
			vh.tv_short_hair_price.setText(Html.fromHtml(Shorthair));

			vh.tv_left_reserve_dialog_name.setText(obj.getName());
			vh.tv_right_lv_reserve_dialog_time.setText(obj.getTimes() + "分钟");
			if (vh.tv_show_package_details__dialog_content.getLineCount() > 1) {
				vh.tv_show_package_details__dialog_content.setText(obj.getIntro() + ".........");
			} else {
				vh.tv_show_package_details__dialog_content.setText(obj.getIntro());
			}
			vh.rl_reserve.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					for(String key : states.keySet()){
						states.put(key, false);
					}
					states.put(String.valueOf(position), true);
					notifyDataSetChanged();
				}
			});
			boolean res = false;
			if (states.get(String.valueOf(position)) == null||states.get(String.valueOf(position))==false) {
				res = false;
			}else{
				res = true;
			}
			vh.imgselect.setClickable(res);
			if (res) {
				vh.imgselect.setBackgroundResource(R.drawable.reserve_btn_selected);
				posi_finishok = position;
				posi_finish = String.valueOf(position);
				finish_data = obj;
				finishdata = alllist.get(position);
				HairStyleAdapter.this.notifyDataSetChanged();
			}else{
				vh.imgselect.setBackgroundResource(R.drawable.reserve_btn_unselected);
			}
			
				
//			vh.imgselect.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					vh.imgselect.setBackgroundResource(R.drawable.reserve_btn_selected);
//					posi_finishok = position;
//					posi_finish = String.valueOf(position);
//					finish_data = obj;
//					finishdata = alllist.get(position);
//					HairStyleAdapter.this.notifyDataSetChanged();
//				}
//
//			});
//			if (position == posi_finishok) {
//				vh.imgselect.setBackgroundResource(R.drawable.reserve_btn_selected);
//			} else {
//				vh.imgselect.setBackgroundResource(R.drawable.reserve_btn_unselected);
//			}
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
			} else if (obj.State.equals(Constant.SCHEDULE_STATE_OVERDUE)) {
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
		private void setLabel(final int position, final ViewHolder vh) {
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
					Intent intent = ReserveActivity.getIntent(context, StoreDetailFragment.class.getName());
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
		/**
		 * 修改
		 */
		@ViewInject(R.id.tv_left_reserve_dialog_name)
		TextView tv_left_reserve_dialog_name;
		@ViewInject(R.id.tv_right_lv_reserve_dialog_time)
		TextView tv_right_lv_reserve_dialog_time;
		@ViewInject(R.id.tv_show_package_details__dialog_content)
		TextView tv_show_package_details__dialog_content;
		@ViewInject(R.id.tv_long_hair_price)
		TextView tv_long_hair_price;
		@ViewInject(R.id.tv_in_hair_price)
		TextView tv_in_hair_price;
		@ViewInject(R.id.tv_short_hair_price)
		TextView tv_short_hair_price;
		@ViewInject(R.id.btn_select_icon_lv_reserve_dialogsss)
		TextView imgselect;
		@ViewInject(R.id.tv_reserve_cancel)
		TextView tv_reserve_cancel;
		@ViewInject(R.id.tv_reserve_ok)
		TextView tv_reserve_ok;
		@ViewInject(R.id.rl_reserve_listview_item_free_dialog)
		RelativeLayout rl_reserve;
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

	/**
	 * @Title: onEventMainThread
	 * @Description: 请求套餐类型
	 * @author: jny
	 * @param null
	 * @return: void
	 */
	private void initPackage() {
		GetAllusersubinfoPackedBysubidRequest mGetAllusersubinfoPackedBysubidRequest = new GetAllusersubinfoPackedBysubidRequest(
				id);
		GetAllusersubinfoPackedBysubidApi.canGetAllusersubinfoPackedBysubid(mGetAllusersubinfoPackedBysubidRequest,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// TODO Auto-generated method stub

						BaseResponse result = BaseResponse.getBaseResponse(responseInfo.result);
						if (result == null) {
							return;
						}
						String[] split = result.getMsg().split("\\|");
						if ("1".equals(split[0])) {
							ToastUtils.showShort(split[1] + "");
						}
						if (result.getCode() == 1000) {
							alllist = GetAllusersubinfoPackedBysubidResponse.getclazz2(result.getData());
							if (alllist.size() != 0) {
								showHiarStyleDialog1();
							}

						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						// TODO Auto-generated method stub

					}
				});

	}

	/**
	 * @Title: onEventMainThread
	 * @Description: 套餐类型展示
	 * @author: jny
	 * @param null
	 * @return: void
	 */
	private void showHiarStyleDialog1() {

		View view = LayoutInflater.from(activity).inflate(R.layout.reserve_dialog_free_layout, null);
		final MessageDialog messageDialog = new MessageDialog(activity, view);
		view.findViewById(R.id.tv_type_reserve_dialog);// 发型类型
		view.findViewById(R.id.tv_type_reserve_dialog).setVisibility(View.GONE);
		ListView lv_dialog = (ListView) view.findViewById(R.id.listview_free_dialog);

		HairStyleAdapter adapter = new HairStyleAdapter(activity, alllist);
		lv_dialog.setAdapter(adapter);
		rb_hair_long.setClickable(true);
		rb_hair_in.setClickable(true);
		rb_hair_short.setClickable(true);
		rb_hair_long.setEnabled(true);
		rb_hair_in.setEnabled(true);
		rb_hair_short.setEnabled(true);

		view.findViewById(R.id.tv_reserve_ok).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (posi_finishok >= 0) {
					Log.i("TAG", "posi_finishok-------->"+posi_finish);
					if (clickRadioGroup > 0) {
						Log.i("TAG", "clickRadioGroup-------->"+clickRadioGroup);
						switch (tag) {
						case 1:
							finishmoney = alllist.get(posi_finishok).getLonghair();
							break;
						case 2:
							finishmoney = alllist.get(posi_finishok).getInhair();
							break;
						case 3:
							finishmoney = alllist.get(posi_finishok).getShorthair();
							break;
						default:
							break;
						}
						// tv_money.setText("￥" + finishmoney);
						if (finish_Amount.equals("0")) {
							tv_money.setText("￥" + finishmoney);
						} else {
							Double finish_moneys = Double.parseDouble(finishmoney) - Double.parseDouble(finish_Amount);
							tv_money.setText("￥" + finish_moneys);
						}
					}
					tv_hairstyle.setText(alllist.get(posi_finishok).getName());
					messageDialog.dismiss();
				} else {
					ToastUtils.showLong("请选择套餐");
				}

			}
		});
		view.findViewById(R.id.tv_reserve_cancel).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				posi_finishok = -1;
				finish_data = null;
				tv_money.setText("￥" + "0");
				tv_hairstyle.setText("请选择套餐");
				rb_hair_long.setChecked(false);
				rb_hair_in.setChecked(false);
				rb_hair_short.setChecked(false);
				messageDialog.dismiss();

			}
		});
		messageDialog.show();

	}

	/**
	 * @Title: initChack
	 * @Description:创建initChack
	 * @author: jny
	 * @param null
	 * @return: void
	 */
	private void initChack() {
		rb_hair_long.setChecked(false);
		rb_hair_in.setChecked(false);
		rb_hair_short.setChecked(false);
		rg_hair_money.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				clickRadioGroup++;
				Drawable drawable = getResources().getDrawable(R.drawable.reserve_btn_selected);
				/// 这一步必须要做,否则不会显示.
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				Drawable drawable1 = getResources().getDrawable(R.drawable.reserve_btn_unselected);
				/// 这一步必须要做,否则不会显示.
				drawable1.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				switch (checkedId) {
				case R.id.rb_hair_long:

					rb_hair_short.setCompoundDrawables(drawable1, null, null, null);
					rb_hair_long.setCompoundDrawables(drawable, null, null, null);
					rb_hair_in.setCompoundDrawables(drawable1, null, null, null);
					if (finish_data == null) {
						rb_hair_long.setChecked(false);
						rb_hair_long.setEnabled(false);
						rb_hair_long.setClickable(false);
						Toast.makeText(ReserveFreeFragment.this.getActivity(), "请先选择套餐类型", 1000).show();
					} else {
						tag = 1;
						rb_hair_long.setChecked(true);
						finishmoney = finish_data.getLonghair();

						// finishmoney = alllist.get(posi).getLonghair();
						if (finish_Amount.equals("0")) {
							tv_money.setText("￥" + finishmoney);
						} else {
							Double finish_moneys = Double.parseDouble(finishmoney) - Double.parseDouble(finish_Amount);
							tv_money.setText("￥" + finish_moneys);
						}
					}
					break;
				case R.id.rb_hair_in:

					rb_hair_short.setCompoundDrawables(drawable1, null, null, null);
					rb_hair_long.setCompoundDrawables(drawable1, null, null, null);
					rb_hair_in.setCompoundDrawables(drawable, null, null, null);
					Log.i("Tag", "OK");
					if (finish_data == null) {
						rb_hair_in.setChecked(false);
						rb_hair_in.setEnabled(false);
						rb_hair_in.setClickable(false);
						Toast.makeText(ReserveFreeFragment.this.getActivity(), "请先选择套餐类型", 1000).show();
					} else {
						tag = 2;
						rb_hair_in.setChecked(true);
						finishmoney = finish_data.getInhair();
						if (finish_Amount.equals("0")) {
							tv_money.setText("￥" + finishmoney);
						} else {
							Double finish_moneys = Double.parseDouble(finishmoney) - Double.parseDouble(finish_Amount);
							tv_money.setText("￥" + finish_moneys);
						}

					}

					break;
				case R.id.rb_hair_short:

					rb_hair_short.setCompoundDrawables(drawable, null, null, null);
					rb_hair_long.setCompoundDrawables(drawable1, null, null, null);
					rb_hair_in.setCompoundDrawables(drawable1, null, null, null);
					if (finish_data == null) {
						rb_hair_short.setChecked(false);
						rb_hair_short.setEnabled(false);
						rb_hair_short.setClickable(false);
						// rb_hair_short.setBackgroundResource(R.drawable.reserve_btn_unselected);

						Toast.makeText(ReserveFreeFragment.this.getActivity(), "请先选择套餐类型", 1000).show();
					} else {
						tag = 3;
						rb_hair_short.setChecked(true);
						finishmoney = finish_data.getShorthair();
						if (finish_Amount.equals("0")) {
							tv_money.setText("￥" + finishmoney);
						} else {
							Double finish_moneys = Double.parseDouble(finishmoney) - Double.parseDouble(finish_Amount);
							tv_money.setText("￥" + finish_moneys);
						}

					}

					break;

				}

			}

		});

	}

}
