package cc.ruit.shunjianmei.home.hairdressert;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cc.ruit.shunjianmei.R;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.home.hairstyle.HairStyleDetailsFragment;
import cc.ruit.shunjianmei.login.LoginActivity;
import cc.ruit.shunjianmei.login.LoginFragment;
import cc.ruit.shunjianmei.net.api.GetAllusersubinfoPackedBysubidApi;
import cc.ruit.shunjianmei.net.api.HairdresserDetailApi;
import cc.ruit.shunjianmei.net.api.HairdresserScheduleApi;
import cc.ruit.shunjianmei.net.api.SaveFavoriteApi;
import cc.ruit.shunjianmei.net.request.GetAllusersubinfoPackedBysubidRequest;
import cc.ruit.shunjianmei.net.request.HairdresserDetailRequest;
import cc.ruit.shunjianmei.net.request.HairdresserScheduleRequest;
import cc.ruit.shunjianmei.net.request.SaveFavoriteRequest;
import cc.ruit.shunjianmei.net.response.GetAllusersubinfoPackedBysubidResponse;
import cc.ruit.shunjianmei.net.response.HairdresserDetailResponse;
import cc.ruit.shunjianmei.net.response.HairdresserScheduleResponse;
import cc.ruit.shunjianmei.reserve.AllusersubinfoPackedBysubid;
import cc.ruit.shunjianmei.reserve.ReserveActivity;
import cc.ruit.shunjianmei.reserve.ReserveFragment;
import cc.ruit.shunjianmei.reserve.ReserveFreeFragment;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmei.util.FragmentManagerUtils;
import cc.ruit.shunjianmei.util.ImageLoaderUtils;
import cc.ruit.shunjianmei.util.MyEventBus;
import cc.ruit.shunjianmei.util.RoundImageLoaderUtil;
import cc.ruit.utils.sdk.ToastUtils;
import cc.ruit.utils.sdk.http.NetWorkUtils;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.oruit.widget.loadingdialog.LoadingDailog;
import com.oruit.widget.messagedialog.MessageDialog;
import com.oruit.widget.title.TitleUtil;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshBase.Mode;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshScrollView;

import de.greenrobot.event.EventBus;

/**
 * 
 * @ClassName: HairDressertDetailsFragment
 * @Description: 发型师详情
 * @author: HBM
 * @date: 2015年10月21日 下午2:47:08
 */
public class HairDressertDetailsFragment extends BaseFragment implements OnPageChangeListener {
	private String tag;// 标签
	private String Type;// 类型
	private List<GetAllusersubinfoPackedBysubidResponse> list_GetAllusersubinfoPackedBysubidResponse;
	@ViewInject(R.id.rl_hairdessert_schedule_package)
	RelativeLayout rl_hairdessert_schedule_package;
	// *****
	@ViewInject(R.id.ratingbar_all_masterevaluate)
	RatingBar masterevaluate;// 发型师评论
	@ViewInject(R.id.iv_masterphoto_dessertdetails)
	ImageView masterphoto;// 发型师头像
	@ViewInject(R.id.tv_mastername_dessertdetails)
	TextView mastername;// 发型师姓名
	@ViewInject(R.id.tv_masterLevelName_dessertdetails)
	TextView masterLevelName;// 发型师水平等级
	@ViewInject(R.id.tv_masterStar_dessertdetails)
	TextView masterStar;// 发型师星座
	@ViewInject(R.id.tv_OrderNum_dessertdetails)
	TextView OrderNum;// 订单量
	@ViewInject(R.id.tv_photonum_masterdetails)
	TextView photonum;// 相片数量
	@ViewInject(R.id.vp_masterphoto_dessertdetails)
	ViewPager vp_masterphoto;// 图片展示
	@ViewInject(R.id.tv_hairdessert_Schedule_package)
	TextView hairpackage;// 套餐
	@ViewInject(R.id.tv_line_Schedule_package)
	TextView linepackage;// 打开套餐红线
	@ViewInject(R.id.tv_hairdessert_intros)
	TextView intro;// 打开简介fragment
	@ViewInject(R.id.tv_hairdessert_Schedules)
	TextView Schedule;// 日程
	@ViewInject(R.id.tv_hairdessert_hairstyle)
	TextView hairstyle;// 发型
	@ViewInject(R.id.tv_line_intro)
	TextView lineintro;// 打开简介红线
	@ViewInject(R.id.tv_line_Schedule)
	TextView lineSchedule;// 日程红线
	@ViewInject(R.id.tv_line_hairstyle)
	TextView linehairstyle;// 发型红线
	@ViewInject(R.id.tv_price)
	TextView tv_price;// 发型红线
	@ViewInject(R.id.vp_masterphoto_dessertdetails)
	ViewPager mViewpager;// 图片轮播
	@ViewInject(R.id.PullToRefreshScrollView)
	PullToRefreshScrollView Scrollview;
	@ViewInject(R.id.rl_hairdessert_btn)
	RelativeLayout hairdessert_btn;// 预约按钮
	String name = "";// 发型师名字
	String ID;// 发型师ID
	FragmentManager fm;
	TitleUtil title;
	List<HairdresserDetailResponse> templist;// 请求数据
	private ArrayList<ImageView> pagerPhotos = new ArrayList<ImageView>();// 存放ViewPager中的图片

	// 理发师日程信息
	List<HairdresserScheduleResponse> scheduleTemplist;

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.hair_dressert_details_fragment_layout, null);
		ViewUtils.inject(this, view);
		// initFragment();
		initData();
		initScheduleData(); // 请求理发师日程信息
		initTitle();

		return view;
	}

	/**
	 * 
	 * @Title: initHairdresserDetailData
	 * @Description: 请求数据
	 * @author: 欧阳
	 * @return: void
	 */
	private void initHairdresserDetailData() {
		if (!NetWorkUtils.isConnectInternet(getActivity())) {
			ToastUtils.showShort(activity.getResources().getString(R.string.no_networks_found));
			LoadingDailog.dismiss();
			return;
		}
		HairdresserDetailRequest request = new HairdresserDetailRequest(UserManager.getUserID() + "", ID);
		HairdresserDetailApi.HairdresserDetail(request, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				BaseResponse response = BaseResponse.getBaseResponse(responseInfo.result);
				if (response == null) {
					LoadingDailog.dismiss();
					return;
				}
				String[] split = response.getMsg().split("\\|");
				if ("1".equals(split[0])) {
					ToastUtils.showShort(split[1] + "");
				}
				if (response.getCode() == 1000) {
					templist = HairdresserDetailResponse.getclazz2(response.getData());
					resultHanlder(templist);
					tag = templist.get(0).getHasPacked();
					initHavePackage();
					resultHanlder(templist);
				}
				LoadingDailog.dismiss();
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				ToastUtils.showShort(activity.getResources().getString(R.string.request_failure));
				LoadingDailog.dismiss();
			}
		});

	}

	/**
	 * 
	 * @Title: resultHanlder
	 * @Description: 处理数据
	 * @author: 欧阳
	 * @param templist2
	 * @return: void
	 */
	protected void resultHanlder(List<HairdresserDetailResponse> templist2) {
		if (templist != null && templist.size() > 0) {
			bundweidgt(templist);
			title.tv_title.setText(name);
			try {
				if (Integer.parseInt(templist.get(0).getIsFavorite()) == 0) {
					title.iv_right.setImageResource(R.drawable.star_collect_solid);
				} else {
					title.iv_right.setImageResource(R.drawable.star_collect_stroke);
				}
			} catch (Exception e) {
			}
			MyEventBus myEvent = new MyEventBus("HairdresserDetail");
			myEvent.setTemplist(templist2);
			EventBus.getDefault().post(myEvent);
			if (templist.get(0).getImages().size() > 0 && templist.get(0).getImages() != null) {
				initViewPager();
				creatNewThread();
			}
			view.requestFocus();
		}
	}

	// /**
	// *
	// * @Title: initFraglayout
	// * @Description: 展示发型详情简介界面
	// * @author: 欧阳
	// * @return: void
	// */
	// private void initFraglayout() {
	// HairDressertDetailsIntroFragment fragment=new
	// HairDressertDetailsIntroFragment();
	// Bundle bundle = new Bundle();
	// bundle.putSerializable("HairdresserDetailResponse", templist.get(0));
	// fragment.setArguments(bundle);
	// fm.beginTransaction().replace(R.id.fl_hairdessert_content,
	// fragment).commit();
	// }
	// /**
	// *
	// * @Title: initFraglayout
	// * @Description: 发型师中的发型
	// * @author: 欧阳
	// * @return: void
	// */
	// private void initHairStyleFraglayout() {
	// HairDressertStyleFragment fragment=new HairDressertStyleFragment();
	// Bundle bundle = new Bundle();
	// bundle.putSerializable("HairStyle", templist.get(0));
	// fragment.setArguments(bundle);
	// fm.beginTransaction().replace(R.id.fl_hairdessert_content,
	// fragment).commit();
	// }
	// /**
	// *
	// * @Title: initFraglayout
	// * @Description: 发型师日程列表
	// * @author: 欧阳
	// * @return: void
	// */
	// private void initHairdresserScheduleFraglayout() {
	// //FragmentManagerUtils.addOrAttach(activity,HairdresserScheduleFragment.class.getName(),
	// R.id.fl_hairdessert_content);
	// HairdresserScheduleFragment fragment=new HairdresserScheduleFragment();
	// Bundle bundle = new Bundle();
	// bundle.putSerializable("ID", templist.get(0).getUserID());
	// fragment.setArguments(bundle);
	// fm.beginTransaction().replace(R.id.fl_hairdessert_content,
	// fragment).commit();
	// }
	/**
	 * 
	 * @Title: bundweidgt
	 * @Description: 绑定控件
	 * @author: 欧阳
	 * @param templist
	 * @return: void
	 */
	protected void bundweidgt(List<HairdresserDetailResponse> templist) {
		if (!TextUtils.isEmpty(templist.get(0).getScore())) {
			Float Score = Float.valueOf(templist.get(0).getScore());
			masterevaluate.setRating(Score);
		} else {
			masterevaluate.setRating(0);
		}
		RoundImageLoaderUtil.setErrImage(R.drawable.tx_man, R.drawable.tx_man, R.drawable.tx_man);
		RoundImageLoaderUtil.getInstance(activity, 360).loadImage(templist.get(0).getPhoto(), masterphoto);
		mastername.setText(templist.get(0).getName());
		masterLevelName.setText(templist.get(0).getLevelName());
		masterStar.setText(templist.get(0).getStar());
		OrderNum.setText("接单" + templist.get(0).getOrderNum() + "次");
		if (Type.equals("自由定价")) {
			tv_price.setVisibility(View.GONE);
		} else {
			tv_price.setVisibility(View.VISIBLE);
			String tip = "<font color='#ababab'>设计剪发</font><font color='#e53123'>" + " ¥" + templist.get(0).getPrice()
					+ "</font>";
			tv_price.setText(Html.fromHtml(tip));
		}

	}

	/**
	 * 
	 * @Title: addImageView
	 * @Description: 为ViewPager添加背景图
	 * @author: Johnny
	 * @return: void
	 */
	private void initViewPager() {
		ImageLoaderUtils.setErrImage(R.drawable.default_prc, R.drawable.default_prc, R.drawable.default_prc);
		for (int i = 0; i < templist.get(0).getImages().size(); i++) {
			ImageView imageView = new ImageView(activity);
			ImageLoaderUtils.getInstance(activity).loadImage(templist.get(0).getImages().get(i).Photo, imageView);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			pagerPhotos.add(imageView);
		}
		addViewPagerText(1);
		mViewpager.setAdapter(new MyViewPagerAdapter());
	}

	/**
	 * 
	 * @Title: addViewPagerDots
	 * @Description: 添加ViewPager图片下方的文本
	 * @author: sue
	 * @return: void
	 */
	private void addViewPagerText(int position) {
		photonum.setText(position + "/" + templist.get(0).getImages().size());
	}

	/**
	 * 
	 * @Title: creatNewThread
	 * @Description: 开启一个子线程，定时滑动ViewPager的图片
	 * @author: sue
	 * @return: void
	 */
	public void creatNewThread() {

		new Thread() {

			public void run() {
				while (true) {
					try {
						sleep(3000);
						handler.sendEmptyMessage(123);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	// ViewPager当前页的位置
	private int itemPosition = 1;
	// 用于设置Viewpager显示哪一页
	Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {

			if (msg.what == 123) {
				int num = itemPosition + 1;
				mViewpager.setCurrentItem(itemPosition++);
				if (itemPosition >= templist.get(0).getImages().size()) {
					itemPosition = 0;
				}
				addViewPagerText(num);
			}
		};
	};

	/**
	 * 
	 * @Title: initData
	 * @Description: 得到上一个数据
	 * @author: 欧阳
	 * @return: void
	 */
	private void initData() {
		fm = getChildFragmentManager();
		mViewpager.setOnPageChangeListener(this);
		Scrollview.setMode(Mode.DISABLED);
		if (this.getArguments() != null) {
			name = this.getArguments().getString("Name").toString();
			ID = this.getArguments().getString("ID").toString();
			Type = this.getArguments().getString("Type");
			int red = getResources().getColor(R.color.red_e5);
			if (Type.equals("自由定价")) {
				rl_hairdessert_schedule_package.setVisibility(View.VISIBLE);
				hairpackage.setTextColor(red);

			} else {
				rl_hairdessert_schedule_package.setVisibility(View.GONE);
				// lineintro.setBackgroundResource(R.color.red_e5);
				lineintro.setVisibility(View.VISIBLE);
				intro.setTextColor(red);
			}
		} else {
			return;
		}
		LoadingDailog.show(activity, "正在加载...");
		initHairdresserDetailData();
	}

	/**
	 * 
	 * @Title: initData
	 * @Description: 网络请求数据
	 * @author: 欧阳
	 * @return: void
	 */
	private void initScheduleData() {
		if (!NetWorkUtils.isConnectInternet(activity)) {
			ToastUtils.showShort(activity.getResources().getString(R.string.no_networks_found));
			return;
		}
		HairdresserScheduleRequest request = new HairdresserScheduleRequest(UserManager.getUserID() + "", ID);
		HairdresserScheduleApi.HairdresserSchedule(request, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				LoadingDailog.dismiss();
				BaseResponse response = BaseResponse.getBaseResponse(responseInfo.result);
				if (response == null) {
					return;
				}

				String[] split = response.getMsg().split("\\|");
				if ("1".equals(split[0])) {
					ToastUtils.showShort(split[1] + "");
				}
				if (response.getCode() == 1000) {
					scheduleTemplist = HairdresserScheduleResponse.getclazz2(response.getData());
				}

			}

			@Override
			public void onFailure(HttpException error, String msg) {
				LoadingDailog.dismiss();
				ToastUtils.showShort(activity.getResources().getString(R.string.request_failure));

			}
		});

	}

	/**
	 * 
	 * @Title: initTitle
	 * @Description: 初始化标题
	 * @author: 欧阳
	 * @return: void
	 */
	private void initTitle() {
		title = new TitleUtil(view);
		// title.rl_container.setAlpha(0.8f);
		title.iv_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean isBack = FragmentManagerUtils.back(getActivity(), R.id.content_frame);
				if (!isBack) {
					getActivity().finish();
				}
				LoadingDailog.dismiss();
			}
		});
		title.iv_left.setImageResource(R.drawable.arrow_back_white);
		title.iv_left.setVisibility(View.VISIBLE);
		title.iv_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (UserManager.getUserID() <= 0) {
					showUnLoginDialog();
					return;
				}
				LoadingDailog.show(activity, "正在收藏...");
				if (Integer.parseInt(templist.get(0).getIsFavorite()) == 0) {
					SaveFoodDiariesFavorite(ID, "1");
				} else if (Integer.parseInt(templist.get(0).getIsFavorite()) == 1) {
					SaveFoodDiariesFavorite(ID, "0");
				}
			}
		});
		title.iv_right.setVisibility(View.VISIBLE);
		title.tv_title.setText(name);
	}

	/**
	 * 
	 * @Title: initFragment
	 * @Description: 初始化fragment
	 * @author: 欧阳
	 * @return: void
	 */
	private void initFragment() {
		android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
		if (Type.equals("自由定价")) {
			ft.replace(R.id.fl_hairdessert_content,
					new AllusersubinfoPackedBysubid(list_GetAllusersubinfoPackedBysubidResponse));
		} else {
			ft.replace(R.id.fl_hairdessert_content, new HairDressertDetailsIntroFragment(templist));
		}

		//
		// ft.replace(R.id.fl_hairdessert_content, new
		// HairDressertDetailsIntroFragment(templist));
		ft.addToBackStack(null);
		ft.commitAllowingStateLoss();

	}

	protected void SaveFoodDiariesFavorite(String iD, final String State) {
		SaveFavoriteRequest request = new SaveFavoriteRequest(UserManager.getUserID() + "", "2", ID, State);
		SaveFavoriteApi.SaveFavorite(request, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				BaseResponse response = BaseResponse.getBaseResponse(responseInfo.result);
				if (response.getCode() == 1000) {
					if (Integer.parseInt(State) == 1) {
						ToastUtils.showShort("收藏成功");
						title.iv_right.setImageResource(R.drawable.star_collect_stroke);
						templist.get(0).setIsFavorite("1");
						EventBus.getDefault().post(new MyEventBus("请刷新美发师收藏列表"));
						LoadingDailog.dismiss();
					} else {
						ToastUtils.showShort("取消收藏");
						title.iv_right.setImageResource(R.drawable.star_collect_solid);
						templist.get(0).setIsFavorite("0");
						EventBus.getDefault().post(new MyEventBus("请刷新美发师收藏列表"));
						LoadingDailog.dismiss();
					}
				}

			}

			@Override
			public void onFailure(HttpException error, String msg) {
				ToastUtils.showShort("收藏关注失败");
			}
		});

	}

	/**
	 * 
	 * @Title: methodClick
	 * @Description: 点击事件切换fragment
	 * @author: 欧阳
	 * @param v
	 * @return: void
	 */
	@OnClick({ R.id.tv_hairdessert_intros, R.id.tv_hairdessert_Schedule_package, R.id.tv_hairdessert_Schedules,
			R.id.tv_hairdessert_hairstyle, R.id.btn_intro_appointment })
	public void methodClick(View v) {
		if (templist == null || templist.size() < 0) {
			return;
		}
		FragmentManager fm = getChildFragmentManager();
		android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
		switch (v.getId()) {
		case R.id.tv_hairdessert_Schedule_package:// 套餐
			controlTab(R.id.tv_line_Schedule_package, R.id.tv_hairdessert_Schedule_package);
			ft.replace(R.id.fl_hairdessert_content, new

			AllusersubinfoPackedBysubid(list_GetAllusersubinfoPackedBysubidResponse));
			ft.addToBackStack(null);
			ft.commitAllowingStateLoss();
			break;
		case R.id.tv_hairdessert_intros:// 简介
			hairdessert_btn.setVisibility(View.VISIBLE);
			controlTab(R.id.tv_line_intro, R.id.tv_hairdessert_intros);
			ft.replace(R.id.fl_hairdessert_content, new HairDressertDetailsIntroFragment(templist));
			ft.addToBackStack(null);
			ft.commitAllowingStateLoss();
			break;
		case R.id.tv_hairdessert_Schedules:// 日程
			hairdessert_btn.setVisibility(View.VISIBLE);
			controlTab(R.id.tv_line_Schedule, R.id.tv_hairdessert_Schedules);
			ft.replace(R.id.fl_hairdessert_content,
					new HairdresserScheduleFragment(templist.get(0).getUserID(), scheduleTemplist));
			ft.addToBackStack(null);
			ft.commitAllowingStateLoss();
			// initHairdresserScheduleFraglayout();
			break;
		case R.id.tv_hairdessert_hairstyle:// 发型
			controlTab(R.id.tv_line_hairstyle, R.id.tv_hairdessert_hairstyle);
			ft.replace(R.id.fl_hairdessert_content, new HairDressertStyleFragment(templist));
			ft.addToBackStack(null);
			ft.commitAllowingStateLoss();
			// initHairdresserScheduleFraglayout();
			break;
		case R.id.btn_intro_appointment:// 预约
			if (UserManager.getUserID() <= 0) {
				showUnLoginDialog();
				break;
			}
			if (Type.equals("自由定价")) {
				if (tag.equals("0")) {
					ToastUtils.showShort("暂无套餐");
				} else {
					Intent intent = ReserveActivity.getIntent(activity, ReserveFreeFragment.class.getName());
					Bundle bundle = new Bundle();
					bundle.putString("ID", ID);
					bundle.putString("From", HairStyleDetailsFragment.class.getSimpleName());
					intent.putExtras(bundle);
					startActivity(intent);
				}
			} else {
				Intent intent = ReserveActivity.getIntent(activity, ReserveFragment.class.getName());
				Bundle bundle = new Bundle();
				bundle.putString("ID", ID);
				bundle.putString("From", HairStyleDetailsFragment.class.getSimpleName());
				intent.putExtras(bundle);
				startActivity(intent);
			}

			break;
		default:
			break;
		}
	}

	/**
	 * 
	 * @Title: controlTab
	 * @Description: 控件按钮设置
	 * @author: 欧阳
	 * @param vh
	 * @param checked
	 * @return: void
	 */
	public void controlTab(int checked_iv, int checked_tv) {
		int red = getResources().getColor(R.color.red_e5);
		int gray = getResources().getColor(R.color.gray_ab);
		int white = getResources().getColor(R.color.gray_e5);
		intro.setTextColor(checked_tv == R.id.tv_hairdessert_intros ? red : gray);
		Schedule.setTextColor(checked_tv == R.id.tv_hairdessert_Schedules ? red : gray);
		hairpackage.setTextColor(checked_tv == R.id.tv_hairdessert_Schedule_package ? red : gray);
		hairstyle.setTextColor(checked_tv == R.id.tv_hairdessert_hairstyle ? red : gray);

		linepackage.setVisibility(checked_iv == R.id.tv_line_Schedule_package ? View.VISIBLE : View.INVISIBLE);
		lineintro.setVisibility(checked_iv == R.id.tv_line_intro ? View.VISIBLE : View.INVISIBLE);
		// lineintro.setBackgroundColor(checked_iv == R.id.tv_line_intro ? red
		// : white);
		lineSchedule.setVisibility(checked_iv == R.id.tv_line_Schedule ? View.VISIBLE : View.INVISIBLE);

		// .setBackgroundColor(checked_iv == R.id.tv_line_Schedule ? red
		// : white);
		linehairstyle.setVisibility(checked_iv == R.id.tv_line_hairstyle ? View.VISIBLE : View.INVISIBLE);
		// linehairstyle
		// .setBackgroundColor(checked_iv == R.id.tv_line_hairstyle ? red
		// : white);
	}

	/**
	 * @Title: showDialog
	 * @Description: 提示登录
	 * @author: lee
	 * @param checkVersionResponse
	 * @return: void
	 */
	void showUnLoginDialog() {
		final MessageDialog messageDialog = new MessageDialog(activity);
		messageDialog.setMessage("请先登录");
		messageDialog.getOkButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 启动登录
				Bundle bundle = new Bundle();
				bundle.putString("from", HairDressertDetailsFragment.class.getSimpleName());
				Intent in = LoginActivity.getIntent(activity, LoginFragment.class.getName());
				in.putExtras(bundle);
				startActivity(in);
				messageDialog.dismiss();
			}
		});
		messageDialog.getCancelButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				messageDialog.dismiss();
			}
		});
		messageDialog.show();
	}

	/**
	 * 
	 * @ClassName: MyViewPagerAdapter
	 * @Description: ViewPager的Adapter
	 * @author: 欧阳
	 * @date: 2015年10月23日 下午4:09:49
	 */
	class MyViewPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return pagerPhotos == null ? 0 : pagerPhotos.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(pagerPhotos.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(pagerPhotos.get(position));
			return pagerPhotos.get(position);
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		photonum.setText(arg0 + 1 + "/" + templist.get(0).getImages().size());

	}

	/**
	 * 套餐
	 */
	private void initHavePackage() {
		GetAllusersubinfoPackedBysubidRequest mGetAllusersubinfoPackedBysubidRequest = new GetAllusersubinfoPackedBysubidRequest(
				ID);
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
							list_GetAllusersubinfoPackedBysubidResponse = GetAllusersubinfoPackedBysubidResponse
									.getclazz2(result.getData());
							initFragment();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						// TODO Auto-generated method stub

					}
				});

	}
}
