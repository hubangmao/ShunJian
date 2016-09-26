package cc.ruit.shunjianmei.home.manager;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.net.api.StoreDetailApi;
import cc.ruit.shunjianmei.net.request.StoreDetailRequest;
import cc.ruit.shunjianmei.net.response.Images;
import cc.ruit.shunjianmei.net.response.StoreDetailResponse;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmei.util.FragmentManagerUtils;
import cc.ruit.shunjianmei.util.ImageLoaderUtils;
import cc.ruit.shunjianmei.util.view.EmptyView;
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
/**
 * @ClassName: StoreDetailFragment
 * @Description: 店面详情界面
 * @author: Johnny
 * @date: 2015年10月14日 下午4:32:59
 */
public class StoreDetailFragment extends BaseFragment implements OnClickListener{
	
	@ViewInject(R.id.viewpager)
	private ViewPager mViewPager;
	@ViewInject(R.id.ratingbar_stroedetail)
	private RatingBar ratingbar;//星形评分条
	
	@ViewInject(R.id.tv_opentime_storedetail)
	private TextView tv_opentime;//营业时间
	@ViewInject(R.id.tv_picturenum_storedetail)
	private TextView tv_picturenum;//图片数量
	@ViewInject(R.id.tv_name_storedetail)
	private TextView tv_name;//店铺名称
	@ViewInject(R.id.tv_distance_storedetail)
	private TextView tv_distance;//距离
	@ViewInject(R.id.tv_ordernum_storedetail)
	private TextView tv_ordernum;//接单量
	@ViewInject(R.id.tv_address_storedetail)
	private TextView tv_address;//地址
	@ViewInject(R.id.tv_parking_storedetail)
	private TextView tv_parking;//停车位
	@ViewInject(R.id.tv_phone_storedetail)
	private TextView tv_phone;//电话
	@ViewInject(R.id.tv_introduce_storedetail)
	private TextView tv_introduce;//简介
	
//	private EmptyView ev;// 空载页
	
	private int imageSize;
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("StoreDetail"); //统计页面
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
				boolean isBack = FragmentManagerUtils.back(getActivity(),
						R.id.content_frame);
				if (!isBack) {
					getActivity().finish();
				}
			}
		});
		title.iv_left.setImageResource(R.drawable.back);
		title.iv_left.setVisibility(View.VISIBLE);
//		title.iv_right.setImageResource(R.drawable.store_service_icon);
//		title.iv_right.setVisibility(View.VISIBLE);
		title.tv_title.setText(null);
	}
	
	/**
	 * @Title: initData
	 * @Description: 初始化数据
	 * @author: Johnny
	 * @return: void
	 */
	private void initData() {
		
		getData();
	}
	
	/**
	 * @Title: getData
	 * @Description: 获取数据
	 * @author: Johnny
	 * @return: void
	 */
	public void getData() {
		if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
			ToastUtils.showShort(activity.getResources().getString(
					R.string.no_networks_found));
			return;
		}
		
		LoadingDailog.show(activity, "数据加载中，请稍后");
		int UserId = UserManager.getUserID();
		String ID = getArguments().getString("ID");
		StoreDetailRequest request = new StoreDetailRequest(""+UserId,ID);
		StoreDetailApi.storeDetail(request, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				new LoadingViewUtil(view).hint();
				LoadingDailog.dismiss();
				
				BaseResponse result = BaseResponse
						.getBaseResponse(responseInfo.result);
				if (result==null) {
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
	void resultHanlder(List<StoreDetailResponse> info){
		if (info==null) {
			LogUtils.e("StoreListResponse err");
			return;
		}
		
		StoreDetailResponse obj = info.get(0);
		int photoNum = obj.getImages().size();
		tv_opentime.setText(obj.getBusinessHours());
		tv_picturenum.setText("1/"+photoNum);
		tv_name.setText(obj.getName());
		TitleUtil title = new TitleUtil(view);
		title.tv_title.setText(obj.getName());
//		tv_distance.setText(obj.getDistance()+"Km");
		tv_address.setText(obj.getAddress());
		tv_parking.setText("停车位" + obj.getCarNum() + "个");
		tv_phone.setText(obj.getTel());
		tv_introduce.setText(obj.getIntro());
		tv_ordernum.setText(obj.getOrderNum()+"次");
		
		setDataVisible(View.VISIBLE);
		imageSize=obj.getImages().size();
		initViewPager(obj.getImages());
		ratingbar.setRating(Float.parseFloat(obj.getScore()));
		creatNewThread(); 
	} 
	
	/**
	 * 
	 * @Title: setDataVisible
	 * @Description: 当数据请求回来后，显示所得数据
	 * @author: Johnny
	 * @return: void
	 */
	private void setDataVisible(int i) {
		
		tv_opentime.setVisibility(i);
		tv_picturenum.setVisibility(i);
		tv_name.setVisibility(i);
		tv_address.setVisibility(i);
		tv_parking.setVisibility(i);
		tv_phone.setVisibility(i);
		tv_introduce.setVisibility(i);
		tv_ordernum.setVisibility(i);
	}
	/**
	 * 
	 * @Title: onClick
	 * @Description: 定义点击事件
	 * @param v
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	@OnClick({ R.id.btn_login,R.id.rl_phone_storedetail })
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.btn_login:
			break;

		case R.id.rl_phone_storedetail:
			Util.openTelDall(activity, tv_phone.getText().toString());
			//showPhoneDialog();
			break;	
			
		default:
			break;
		}
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
			ImageLoaderUtils.getInstance(activity).loadImage(
					images.get(i).getPhoto(), imageView);
			
			pagerPhotos.add(imageView);
		}
		mViewPager.setAdapter(new MyViewPagerAdapter(pagerPhotos));
	}

	/**
	 * 
	 * @Title: showDialog
	 * @Description: 当用户点击联系电话时，弹出此对话框
	 * @author: Johnny
	 * @return: void
	 */
	private void showPhoneDialog() {
		final MessageDialog dialog = new MessageDialog(activity);
		dialog.setMessage("是否联系商户");
		dialog.getOkButton().setText("确定");
		dialog.getOkButton().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				callNumber();
				dialog.dismiss();
			}

		});
		dialog.getCancelButton().setText("取消");
		dialog.getCancelButton().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
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
		Uri uri = Uri.parse("tel:" + tv_phone.getText().toString());
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_DIAL);
		intent.setData(uri);
		startActivity(intent);
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
//			int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, activity.getResources().getDisplayMetrics());
//			imageView.setPadding(padding, padding, padding, padding);
//			int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, activity.getResources().getDisplayMetrics())/5;
//			ll.setGravity(Gravity.CENTER_VERTICAL);
//			ll.addView(imageView,width,width);
//		}
//	}
	
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
		tv_picturenum.setText(position + "/" + imageSize);
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
		
		MyViewPagerAdapter(List<ImageView> images){
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
