package cc.ruit.shunjianmei.home.store;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cc.ruit.shunjianmei.R;
import cc.ruit.shunjianmei.baidumap.LocationActivity;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.home.MainActivity;
import cc.ruit.shunjianmei.home.order.OrderFragment;
import cc.ruit.shunjianmei.home.order.OrderShareFragment;
import cc.ruit.shunjianmei.login.LoginActivity;
import cc.ruit.shunjianmei.login.LoginFragment;
import cc.ruit.shunjianmei.net.api.SaveFavoriteApi;
import cc.ruit.shunjianmei.net.api.SaveShareRecordApi;
import cc.ruit.shunjianmei.net.api.StartQuickPayApi;
import cc.ruit.shunjianmei.net.request.SaveFavoriteRequest;
import cc.ruit.shunjianmei.net.request.SaveShareRecordRequest;
import cc.ruit.shunjianmei.net.request.StartQuickPayRequest;
import cc.ruit.shunjianmei.net.response.SaveShareRecordResponse;
import cc.ruit.shunjianmei.net.response.StartQuickPayResponse;
import cc.ruit.shunjianmei.net.response.StoreDetailResponse.Images;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmei.util.FragmentManagerUtils;
import cc.ruit.shunjianmei.util.ImageLoaderUtils;
import cc.ruit.shunjianmei.util.MyEventBus;
import cc.ruit.shunjianmei.util.view.ShareDialog;
import cc.ruit.utils.sdk.ToastUtils;
import cc.ruit.utils.sdk.Util;
import cc.ruit.utils.sdk.file.FileConstant;
import cc.ruit.utils.sdk.http.NetWorkUtils;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

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
 * @ClassName: QuickpayResultFragment
 * @Description: 闪惠支付成功的结果界面
 * @author: jiazhaohui
 */
public class QuickpayResultFragment extends BaseFragment implements
		OnClickListener {
	
	@ViewInject(R.id.quickpay_result_orderid)
	private TextView quickpay_result_orderid;		// 订单号
	@ViewInject(R.id.quickpay_result_store_name)
	private TextView quickpay_result_store_name;		// 支付对象商家
	@ViewInject(R.id.quickpay_result_master)
	private TextView quickpay_result_master;		// 服务的美发师
	@ViewInject(R.id.quickpay_result_amount)
	private TextView quickpay_result_amount;		// 最后的实付款
	@ViewInject(R.id.quickpay_result_minus)
	private TextView quickpay_result_minus;		// 最后优惠金额
	
	private final int SHARE_TO_FRIEND_CIRCLE = 0;	// 分享到朋友圈
	private final int SHARE_TO_FRIEND = 1;			// 分享给好友

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("QuickpayResult"); // 统计页面
		
		
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("QuickpayResult");
	}

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.quickpay_result_layout, null);
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
//		title.iv_left.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// 返回Store
//				backStore();
//			}
//		});
//		title.iv_left.setImageResource(R.drawable.back);
//		title.iv_left.setVisibility(View.VISIBLE);
		title.iv_right.setVisibility(View.GONE);
		title.tv_title.setText("优惠支付成功");
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
		String orderID = getArguments().getString("OrderID");
		quickpay_result_orderid.setText(orderID);
		String storeName = getArguments().getString("StoreName");
		quickpay_result_store_name.setText(storeName);
		String masterName = getArguments().getString("MasterName");
		quickpay_result_master.setText(masterName);
		String payAmount = getArguments().getString("PayAmount");
		quickpay_result_amount.setText(payAmount);
		String reduceAmount = getArguments().getString("ReduceAmount");
		quickpay_result_minus.setText(reduceAmount);
	}
	
	
	/**
	 * 
	 * @Title: onClick
	 * @Description: 定义点击事件
	 * @param v
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	@OnClick({ R.id.btn_share_wechat_friendcircle, R.id.btn_share_wechat_friend,
			R.id.btn_share_cancel })
	public void onClick(View v) {
		FragmentManager fm = getChildFragmentManager();
		android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
		switch (v.getId())
		{
			// 点击了分享到朋友圈
			case R.id.btn_share_wechat_friendcircle:
				LoadingDailog.show(activity, "正在通知微信，请稍后~");
				sendShareNetRequest(SHARE_TO_FRIEND_CIRCLE);
				break;
			// 点击了分享给好友
			case R.id.btn_share_wechat_friend:
				LoadingDailog.show(activity, "正在通知微信，请稍后~");
				sendShareNetRequest(SHARE_TO_FRIEND);
				break;
			// 点击了“给钱都不要”
			case R.id.btn_share_cancel:
				getActivity().finish();
				break;
		}		
	}
	
	/**
	 * 
	 * @Title: SendNetRequest
	 * @Description: 保存分享记录
	 * @author: jiazhaohui
	 * @return: void
	 */
	private void sendShareNetRequest(int position) {
		if (!NetWorkUtils.isConnectInternet(getActivity())) {
			ToastUtils.showShort("网络未链接，请检查网络设置");
			LoadingDailog.dismiss();
			return;
		}
		StringBuffer sb = new StringBuffer();

		String codeID = getArguments().getString("OrderID");
		SaveShareRecordRequest request = new SaveShareRecordRequest(
				UserManager.getUserID() + "", codeID, sb.toString());
		final int pos = position;
		
		SaveShareRecordApi.SaveShareRecord(request,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						BaseResponse result = BaseResponse
								.getBaseResponse(responseInfo.result);
						String[] split = result.getMsg().split("\\|");
						if ("1".equals(split[0])) {
							ToastUtils.showShort(split[1] + "");
						}
						if (result.getCode() == 1000) {
							List<SaveShareRecordResponse> templist = SaveShareRecordResponse
									.getclazz2(result.getData());
							share(templist, pos);
						}else {
							LoadingDailog.dismiss();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						ToastUtils.showLong(R.string.no_networks_found);
						LoadingDailog.dismiss();
					}
				});

	}
	
	/**
	 * @Title: saveBitmap
	 * @Description: 把图片保存到sd卡
	 * @author: lee
	 * @param bitName
	 * @param mBitmap
	 * @return
	 * @return: String
	 */
	public String saveBitmap(String path, String bitName, Bitmap mBitmap) {
		File fileDir = new File(path);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		} else {
			fileDir = null;
		}
		File f = new File(path + bitName);
		try {
			f.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LogUtils.e("在保存图片时出错");
		}
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
			mBitmap.compress(Bitmap.CompressFormat.PNG, 60, fOut);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			if (fOut != null)
				fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			if (fOut != null)
				fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mBitmap.recycle();
		return f.getPath();
	}
	
	/**
	 * 
	 * @Title: share
	 * @Description: 分享地址
	 * @author: jiazhaohui
	 * @param templist
	 * @param url
	 * @param platform
	 * @return: void
	 */
	public void share(List<SaveShareRecordResponse> templist, int position) {
		final String title = "我刚在“顺间”做了一次美发，没有办卡和推销。超赞！";
		final String content = "在线预约顶级美发师，没有任何隐形消费，100%正品高端用料，首创7天不满意全额退款，赶紧来试一试吧！";
		final String url = templist.get(0).getURL();
		String image = "";
		try {
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.logo_zhi_128);
			String path = FileConstant.SD_PATH + FileConstant.UPLOAD_PHOTO_PATH;
			String name = "logo_zhi_128.png";
			saveBitmap(path, name, bitmap);
			image = path + name;
		} catch (Exception e) {
			LogUtils.e(e.getMessage(), e);
		}
		// }
		if (position == SHARE_TO_FRIEND) {
			new ShareDialog(activity).shareWechat(title, content, image, url,
					new ShareActionListener());
		} else if (position == SHARE_TO_FRIEND_CIRCLE) {
			new ShareDialog(activity).shareWechatMoments(title, content, image,
					url, new ShareActionListener());
		}
	}
	
	// 用户点击了回到商户
	private void backStore()
	{
		FragmentManagerUtils.back(getActivity(), R.id.content_frame);
		boolean isBack = FragmentManagerUtils.back(getActivity(),
				R.id.content_frame);
		if (!isBack) {
			getActivity().finish();
		}
	}
	
	class ShareActionListener implements PlatformActionListener {

		@Override
		public void onCancel(Platform arg0, int arg1) {
			// TODO Auto-generated method stub
			LoadingDailog.dismiss();
			LogUtils.i("onCancel" + arg0.getName());
			ToastUtils.showLong("分享已经取消", Gravity.CENTER);
		}

		@Override
		public void onComplete(Platform arg0, int arg1,
				HashMap<String, Object> arg2) {
			// TODO Auto-generated method stub
			LoadingDailog.dismiss();
			ToastUtils.showLong("分享成功", Gravity.CENTER);
			LogUtils.i("onCancel" + arg0.getName());
		}

		@Override
		public void onError(Platform arg0, int arg1, Throwable arg2) {
			// TODO Auto-generated method stub
			LoadingDailog.dismiss();
			ToastUtils.showLong("分享失败", Gravity.CENTER);
			LogUtils.i(arg0.getName() + "onCancel " + arg2.getMessage());
		}

	}
}
