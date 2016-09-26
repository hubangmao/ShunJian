package cc.ruit.shunjianmei.home.store;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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
import cc.ruit.shunjianmei.baidumap.LocationActivity;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.login.LoginActivity;
import cc.ruit.shunjianmei.login.LoginFragment;
import cc.ruit.shunjianmei.net.api.SaveFavoriteApi;
import cc.ruit.shunjianmei.net.api.StoreDetailApi;
import cc.ruit.shunjianmei.net.request.SaveFavoriteRequest;
import cc.ruit.shunjianmei.net.request.StoreDetailRequest;
import cc.ruit.shunjianmei.net.response.StoreDetailResponse;
import cc.ruit.shunjianmei.net.response.StoreDetailResponse.Images;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmei.util.FragmentManagerUtils;
import cc.ruit.shunjianmei.util.ImageLoaderUtils;
import cc.ruit.shunjianmei.util.MyEventBus;
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

import de.greenrobot.event.EventBus;

/**
 * @ClassName: StoreDetailFragment
 * @Description: 店面详情界面
 * @author: Johnny
 * @date: 2015年10月14日 下午4:32:59
 */
public class StoreDetailFragment extends BaseFragment implements OnClickListener {

	@ViewInject(R.id.viewpager)
	private ViewPager mViewPager;
	@ViewInject(R.id.ratingbar_stroedetail)
	private RatingBar ratingbar;// 星形评分条

	@ViewInject(R.id.tv_opentime_storedetail)
	private TextView tv_opentime;// 营业时间
	@ViewInject(R.id.tv_picturenum_storedetail)
	private TextView tv_picturenum;// 图片数量
	@ViewInject(R.id.tv_name_storedetail)
	private TextView tv_name;// 店铺名称
	@ViewInject(R.id.tv_distance_storedetail)
	private TextView tv_distance;// 距离
	@ViewInject(R.id.tv1_storedetail)
	private TextView tv_ordernum;// 接单量
	@ViewInject(R.id.tv_address_storedetail)
	private TextView tv_address;// 地址
	@ViewInject(R.id.tv_parking_storedetail)
	private TextView tv_parking;// 停车位
	@ViewInject(R.id.tv_phone_storedetail)
	private TextView tv_phone;// 电话

	// add by jiazhaohui
	@ViewInject(R.id.tv_quick_pay_tips)
	private TextView tv_quickpay_tips;// 闪惠提示部分
	@ViewInject(R.id.rl_quick_pay_lineview)
	private View tv_quickpay_line; // 分割线
	@ViewInject(R.id.rl_quick_pay)
	private RelativeLayout rl_quickpay;

	@ViewInject(R.id.tv_style_storedetial)
	private TextView tv_style;// 商户介绍
	@ViewInject(R.id.tv_dresser_storedetial)
	private TextView tv_dresser;// 美发师
	@ViewInject(R.id.cursor1)
	private View cursor1;
	@ViewInject(R.id.cursor2)
	private View cursor2;

	private int imageSize;
	private boolean isFavorite;// 是否收藏的标记
	private String id;// 店铺ID

	private static String tag = "";

	private StoreDetailResponse obj;

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("StoreDetail"); // 统计页面

		// if (!tag.equals(""))
		// {
		// getData();
		// tag = "";
		// }
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("StoreDetail");
	}

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.storedetail_layout, null);
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
		title.tv_title.setText(null);

		title.iv_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (UserManager.getUserID() <= 0) {
					showUnLoginDialog();
				} else if (isFavorite == true) {
					saveStoreFavorite(id, "0");
				} else {
					saveStoreFavorite(id, "1");
				}
			}

		});
	}

	/**
	 * @Title: initData
	 * @Description: 初始化数据
	 * @author: Johnny
	 * @return: void
	 */
	private void initData() {

		// data1.setText(setDateMsg(0));
		// data2.setText(setDateMsg(1));
		// data3.setText(setDateMsg(2));
		// data4.setText(setDateMsg(3));
		// data5.setText(setDateMsg(4));
		// data6.setText(setDateMsg(5));
		// data7.setText(setDateMsg(6));

		// data1.setChecked(true);

		getData();
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
				bundle.putString("from", StoreDetailFragment.class.getSimpleName());
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
	 * @Title: getData
	 * @Description: 获取数据
	 * @author: Johnny
	 * @return: void
	 */
	public void getData() {
		if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
			ToastUtils.showShort(activity.getResources().getString(R.string.no_networks_found));
			return;
		}

		LoadingDailog.show(activity, "数据加载中，请稍后");
		int UserId = UserManager.getUserID();
		float latitude = UserManager.getLatitude();
		float longitude = UserManager.getlongitude();
		String ID = getArguments().getString("ID");
		StoreDetailRequest request = new StoreDetailRequest("" + UserId, ID, "" + latitude, "" + longitude);
		StoreDetailApi.storeDetail(request, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				new LoadingViewUtil(view).hint();
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

					List<StoreDetailResponse> response = StoreDetailResponse.getclazz2(result.getData());
					resultHanlder(response);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				new LoadingViewUtil(view).hint();
				LoadingDailog.dismiss();
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
	void resultHanlder(List<StoreDetailResponse> info) {
		if (info == null) {
			LogUtils.e("StoreDetailResponse err");
			return;
		}

		obj = info.get(0);
		id = obj.getID();
		int photoNum = obj.getImages().size();
		tv_opentime.setText(obj.getBusinessHours());
		tv_picturenum.setText("1/" + photoNum);
		tv_name.setText(obj.getName());
		TitleUtil title = new TitleUtil(view);
		title.tv_title.setText(obj.getName());
		tv_distance.setText(obj.getDistance() + "Km");
		tv_address.setText(obj.getAddress());
		tv_parking.setText("停车位" + obj.getCarNum() + "个");
		tv_phone.setText(obj.getTel());
		tv_ordernum.setText(obj.getOrderNum() + "次");

		if ("0".equals(obj.getIsFavorite())) {
			title.iv_right.setImageResource(R.drawable.star_collect_solid);
			title.iv_right.setVisibility(View.VISIBLE);
			isFavorite = false;
		} else {
			title.iv_right.setImageResource(R.drawable.star_collect_stroke);
			title.iv_right.setVisibility(View.VISIBLE);
			isFavorite = true;
		}

		// add by jiazhaohui
		// 显示闪惠部分
		if ("0".equals(obj.getQuickpay())) {
			rl_quickpay.setVisibility(View.GONE);
			tv_quickpay_line.setVisibility(View.GONE);
		} else {
			rl_quickpay.setVisibility(View.VISIBLE);
			tv_quickpay_line.setVisibility(View.VISIBLE);
			tv_quickpay_tips.setText(obj.getPerferiential());
		}

		ratingbar.setRating(Float.parseFloat(obj.getScore()));

		imageSize = obj.getImages().size();
		initViewPager(obj.getImages());

		creatNewThread();

		MyEventBus myEvent = new MyEventBus("Introduction");
		myEvent.setObj(obj);
		EventBus.getDefault().post(myEvent);

		if (obj != null) {
			FragmentManager fm = getChildFragmentManager();
			android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
			ft.replace(R.id.fl_child_content, new DresserFragment(obj));
			ft.addToBackStack(null);
			ft.commitAllowingStateLoss();
		}
	}

	/**
	 * 
	 * @Title: SaveFoodDiariesFavorite
	 * @Description: 收藏店铺接口
	 * @author: Johnny
	 * @param iD
	 * @param State
	 * @return: void
	 */
	private void saveStoreFavorite(String id, final String State) {
		if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
			ToastUtils.showShort(activity.getResources().getString(R.string.no_networks_found));
			return;
		}

		LoadingDailog.show(activity, "数据加载中，请稍后");
		SaveFavoriteRequest request = new SaveFavoriteRequest(UserManager.getUserID() + "", "3", id, State);
		SaveFavoriteApi.SaveFavorite(request, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				BaseResponse response = BaseResponse.getBaseResponse(responseInfo.result);
				TitleUtil title = new TitleUtil(view);
				if (response.getCode() == 1000) {
					if (Integer.parseInt(State) == 1) {
						ToastUtils.showShort("收藏成功");
						title.iv_right.setImageResource(R.drawable.star_collect_stroke);
						isFavorite = true;
						EventBus.getDefault().post(new MyEventBus("请刷新美发店收藏列表"));
						LoadingDailog.dismiss();
					} else {
						ToastUtils.showShort("取消收藏");
						title.iv_right.setImageResource(R.drawable.star_collect_solid);
						isFavorite = false;
						EventBus.getDefault().post(new MyEventBus("请刷新美发店收藏列表"));
						LoadingDailog.dismiss();
					}
				}

			}

			@Override
			public void onFailure(HttpException error, String msg) {
				ToastUtils.showShort("收藏关注失败");
				LoadingDailog.dismiss();
			}
		});

	}

	/**
	 * 
	 * @Title: onClick
	 * @Description: 定义点击事件
	 * @param v
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	@OnClick({ R.id.tv_style_storedetial, R.id.tv_dresser_storedetial, R.id.rl_phone_storedetail, R.id.rl_storedetail,
			R.id.rl_quick_pay })
	public void onClick(View v) {
		if (obj == null) {
			return;
		}
		FragmentManager fm = getChildFragmentManager();
		android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
		switch (v.getId()) {
		// 美发师
		case R.id.tv_style_storedetial:
			setTabColor(v.getId());
			ft.replace(R.id.fl_child_content, new DresserFragment(obj));
			ft.addToBackStack(null);
			ft.commitAllowingStateLoss();
			break;
		// 商户列表
		case R.id.tv_dresser_storedetial:
			setTabColor(v.getId());
			ft.replace(R.id.fl_child_content, new IntroductionFragment(obj));
			ft.addToBackStack(null);
			ft.commitAllowingStateLoss();
			break;
		// 联系电话
		case R.id.rl_phone_storedetail:
			Util.openTelDall(activity, obj.getTel());
			// showPhoneDialog("是否联系商户",1);
			break;
		// 优惠快捷支付
		case R.id.rl_quick_pay:
			startQuickpay();
			break;
		// 地图
		case R.id.rl_storedetail:
			Intent intent = LocationActivity.getIntent(activity, LocationActivity.class.getSimpleName());
			intent.putExtra("latitude", obj.getLatitude());
			intent.putExtra("longitude", obj.getLongitude());
			intent.putExtra("title", obj.getName() != null ? obj.getName() : "美发店");
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	/**
	 * 
	 * @Title: startQuickpay
	 * @Description: 点击了闪惠跳转到闪惠界面
	 * @author: jiazhaohui
	 * @return: void
	 */
	private void startQuickpay() {
		// 如果没有登录，则提示登录
		if (UserManager.getUserID() <= 0) {
			showUnLoginDialog();
		} else {
			// 跳转闪惠界面
			Fragment fragment = FragmentManagerUtils.getFragment(activity, QuickpayFragment.class.getName());
			String ID = getArguments().getString("ID");
			Bundle bundle = new Bundle();
			bundle.putString("ID", ID);
			String Name = obj.getName();
			bundle.putString("ID", ID);
			bundle.putString("Name", Name);
			fragment.setArguments(bundle);
			FragmentManagerUtils.add(activity, R.id.content_frame, fragment, true);
			tag = "EnterQuickpay";
		}
	}

	/**
	 * 
	 * @Title: setTabColor
	 * @Description: 设置选项栏的颜色
	 * @author: Johnny
	 * @return: void
	 */
	private void setTabColor(int id) {
		tv_style.setTextColor(
				activity.getResources().getColor(id == R.id.tv_style_storedetial ? R.color.red_e5 : R.color.black33));
		tv_dresser.setTextColor(
				activity.getResources().getColor(id == R.id.tv_dresser_storedetial ? R.color.red_e5 : R.color.black33));
		cursor1.setBackgroundColor(
				activity.getResources().getColor(id == R.id.tv_style_storedetial ? R.color.red_e5 : R.color.grey));
		cursor2.setBackgroundColor(
				activity.getResources().getColor(id == R.id.tv_dresser_storedetial ? R.color.red_e5 : R.color.grey));
		cursor1.setVisibility(id == R.id.tv_style_storedetial ? View.VISIBLE : View.INVISIBLE);
		cursor2.setVisibility(id == R.id.tv_dresser_storedetial ? View.VISIBLE : View.INVISIBLE);

	}

	/**
	 * 
	 * @Title: addImageView
	 * @Description: 为ViewPager添加背景图
	 * @author: Johnny
	 * @return: void
	 */
	private void initViewPager(List<Images> images) {
		List<ImageView> pagerPhotos = new ArrayList<ImageView>();
		ImageLoaderUtils.setErrImage(R.drawable.default_prc, R.drawable.default_prc, R.drawable.default_prc);
		for (int i = 0; i < images.size(); i++) {
			ImageView imageView = new ImageView(activity);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			ImageLoaderUtils.getInstance(activity).loadImage(images.get(i).Photo, imageView);

			pagerPhotos.add(imageView);
		}
		mViewPager.setAdapter(new MyViewPagerAdapter(pagerPhotos));
	}

	/**
	 * 
	 * @Title: creatNewThread
	 * @Description: 开启一个子线程，定时滑动ViewPager的图片
	 * @author: Johnny
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
				mViewPager.setCurrentItem(itemPosition++);
				addViewPagerText(itemPosition);
				if (itemPosition >= imageSize) {
					itemPosition = 0;

				}
			}
		};
	};

	/**
	 * 
	 * @Title: addViewPagerText
	 * @Description: 添加ViewPager图片下方的文本
	 * @author: Johnny
	 * @return: void
	 */
	private void addViewPagerText(int position) {
		tv_picturenum.setText(position + "/" + obj.getImages().size());
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
			Uri uri = Uri.parse("tel:" + obj.getTel());
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_DIAL);
			intent.setData(uri);
			startActivity(intent);
		} catch (Exception e) {
			showPhoneDialog("无法拨打电话", 2);
		}
	}

	/**
	 * 
	 * @ClassName: MyViewPagerAdapter
	 * @Description: ViewPager的Adapter
	 * @author: Johnny
	 * @date: 2015年9月11日 下午1:33:03
	 */
	class MyViewPagerAdapter extends PagerAdapter {

		private List<ImageView> images;

		MyViewPagerAdapter(List<ImageView> images) {
			this.images = images;
		}

		@Override
		public int getCount() {
			return images == null ? 0 : images.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(images.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(images.get(position));
			return images.get(position);
		}
	}

}
