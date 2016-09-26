package cc.ruit.shunjianmei.home.order;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cc.ruit.shunjianmei.R;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.home.MainActivity;
import cc.ruit.shunjianmei.net.api.SaveShareRecordApi;
import cc.ruit.shunjianmei.net.request.SaveShareRecordRequest;
import cc.ruit.shunjianmei.net.response.SaveShareRecordResponse;
import cc.ruit.shunjianmei.net.response.UploadImageResponce;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmei.util.FragmentManagerUtils;
import cc.ruit.shunjianmei.util.ImageLoaderUtils;
import cc.ruit.shunjianmei.util.ScreenUtils;
import cc.ruit.shunjianmei.util.view.MyGridView;
import cc.ruit.shunjianmei.util.view.ShareDialog;
import cc.ruit.utils.sdk.ToastUtils;
import cc.ruit.utils.sdk.file.FileConstant;
import cc.ruit.utils.sdk.file.FileHelper;
import cc.ruit.utils.sdk.http.NetWorkUtils;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.oruit.widget.loadingdialog.LoadingDailog;
import com.oruit.widget.title.TitleUtil;

/**
 * 
 * @ClassName: OrderSharaFragment
 * @Description: 分享订单
 * @author: 欧阳
 * @date: 2015年10月27日 下午1:37:42
 */
public class OrderShareFragment extends BaseFragment {
	@ViewInject(R.id.mgv_sharaphoto)
	MyGridView sharaphoto;// 将分享的图片展示
	@ViewInject(R.id.tv_order_share_photo)
	TextView tv_tip;// 选择照片提示语
	GridViewAdapter adapter;// 评价
	List<UploadImageResponce> mlist;
	String EvalID;// 评价ID
	int Position;
	@ViewInject(R.id.tv_order_share_comment)
	TextView tvComment;
	@ViewInject(R.id.iv_order_share_red)
	ImageView ivRed;

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.order_shara_fragment, null);
		ShareSDK.initSDK(activity);
		ViewUtils.inject(this, view);
		initTitle();
		initGridView();
		initData();
		return view;
	}

	/**
	 * 
	 * @Title: initData
	 * @Description: 将上一个界面传来的数据一一添加在集合里面
	 * @author: 欧阳
	 * @return: void
	 */
	private void initData() {
		EvalID = OrderDetailFragment.EvalID;
		if (OrderEvaluationFragment.Evaluation == "first") {
			if (this.getArguments() == null) {
				return;
			}
			if (this.getArguments().getSerializable("PhotoID") != null) {
				List<UploadImageResponce> PhotoID = (List<UploadImageResponce>) this
						.getArguments().getSerializable("PhotoID");
				mlist.addAll(PhotoID);
				adapter.notifyDataSetChanged();
			}
			if (this.getArguments().getStringArrayList("StoreID") != null) {
				List<UploadImageResponce> PhotoID = (List<UploadImageResponce>) this
						.getArguments().getSerializable("StoreID");
				mlist.addAll(PhotoID);
				adapter.notifyDataSetChanged();
			}
			if (mlist == null || mlist.size() <= 0) {
				tv_tip.setVisibility(View.GONE);
			}
			OrderEvaluationFragment.Evaluation = "";
		}else{
			tv_tip.setVisibility(View.INVISIBLE);
			tvComment.setVisibility(View.INVISIBLE);
			ivRed.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 
	 * @Title: initTitle
	 * @Description: 初始化标题
	 * @author: 欧阳
	 * @return: void
	 */
	private void initTitle() {
		TitleUtil titleUtil = new TitleUtil(view);
		titleUtil.tv_title.setText("分享获得优惠劵");
		titleUtil.iv_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean isBack = FragmentManagerUtils.back(getActivity(),
						R.id.content_frame);
				if (!isBack) {
					getActivity().finish();
				}
			}
		});
		titleUtil.iv_left.setImageResource(R.drawable.arrow_left_white);
		titleUtil.iv_left.setVisibility(View.VISIBLE);
	}

	/**
	 * 
	 * @Title: initGridView
	 * @Description: 初始化
	 * @author: 欧阳
	 * @return: void
	 */
	private void initGridView() {
		mlist = new ArrayList<UploadImageResponce>();
		adapter = new GridViewAdapter(activity, mlist);
		sharaphoto.setAdapter(adapter);
		sharaphoto.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				for (int i = 0; mlist != null && i < mlist.size(); i++) {
					mlist.get(i).clear = false;
				}
				mlist.get(position).clear = true;
				adapter.notifyDataSetChanged();
			}
		});
	}

	@OnClick({ R.id.btn_shara_weixinfriend, R.id.btn_friend_quan,
			R.id.btn_shara_cancel })
	public void methodClick(View v) {
		switch (v.getId()) {
		case R.id.btn_shara_weixinfriend:// 分享给微信好友
			// TODO 先进行网络请求获取URL
			LoadingDailog.show(activity, "正在通知微信，请稍后~");
			Position = 1;
			SendNetRequest();
			break;
		case R.id.btn_friend_quan:// 分享给朋友圈
			LoadingDailog.show(activity, "正在通知微信，请稍后~");
			Position = 2;
			SendNetRequest();
			break;
		case R.id.btn_shara_cancel:// 取消分享
			Intent intent = MainActivity.getIntent(activity,
					OrderFragment.class.getName());
			startActivity(intent);
			activity.finish();
			break;
		default:
			break;
		}
	}

	/**
	 * 
	 * @Title: SendNetRequest
	 * @Description: 保存分享记录
	 * @author: 欧阳
	 * @return: void
	 */
	private void SendNetRequest() {
		if (!NetWorkUtils.isConnectInternet(getActivity())) {
			ToastUtils.showShort("网络未链接，请检查网络设置");
			LoadingDailog.dismiss();
			return;
		}
		StringBuffer sb = new StringBuffer();
		if (mlist.size() > 0) {
			for (int i = 0; i < mlist.size(); i++) {
				if (mlist.get(i).clear == true) {
					if (i == mlist.size() - 1) {
						sb.append(mlist.get(i).ID);
					} else {
						sb.append(mlist.get(i).ID);
					}
				}
			}
		} else {
			sb.append("");
		}

		SaveShareRecordRequest request = new SaveShareRecordRequest(
				UserManager.getUserID() + "", EvalID, sb.toString());
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
							shara(templist, Position);
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
	 * 
	 * @Title: shara
	 * @Description: 分享地址
	 * @author: 欧阳
	 * @param templist
	 * @param url
	 * @param platform
	 * @return: void
	 */
	public void shara(List<SaveShareRecordResponse> templist, int position) {
		final String title = "我刚在“顺间”做了一次美发，没有办卡和推销。超赞！";
		final String content = "在线预约顶级美发师，没有任何隐形消费，100%正品高端用料，首创7天不满意全额退款，赶紧来试一试吧！";
		final String url = templist.get(0).getURL();
		String image = "";
		// for (int i = 0; mlist != null && i < mlist.size(); i++) {
		// if (mlist.get(i).clear == true) {
		// image = mlist.get(i).getLoacpath();
		// }
		// }
		// if (TextUtils.isEmpty(image)) {
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
		if (Position == 1) {
			new ShareDialog(activity).shareWechat(title, content, image, url,
					new ShareActionListener());
		} else if (Position == 2) {
			new ShareDialog(activity).shareWechatMoments(title, content, image,
					url, new ShareActionListener());
		}
		// }

		// ShareParams oks = new ShareParams();
		// oks.setTitle("顺间优惠券");
		// oks.setText("我在顺间美发，送你一张优惠券，快来体验吧~");
		// if (mlist.size() > 0 ) {
		// for (int i = 0; i < mlist.size(); i++) {
		// if (mlist.get(i).clear==true) {
		// oks.setImageUrl(mlist.get(i).Image);
		// }
		// }
		// }else {
		// oks.setImageData(BitmapFactory.decodeResource(getResources(),
		// R.drawable.icon));
		// }
		// oks.setUrl(url);
		// oks.setShareType(Platform.SHARE_WEBPAGE);// 分享到网页的方式打开连接
		// Platform weixin = ShareSDK.getPlatform(activity, platform);
		// weixin.setPlatformActionListener(this);
		// weixin.share(oks);
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
	 * @ClassName: ScheduleGridViewAdapter
	 * @Description: 事件安排
	 * @author: 欧阳
	 * @date: 2015年10月26日 下午12:56:44
	 */
	class GridViewAdapter extends BaseAdapter {

		private Context context;
		private List<UploadImageResponce> mlist;
		List<String> PicIDList = new ArrayList<String>();
		int width;

		public GridViewAdapter(Context context, List<UploadImageResponce> mlist) {
			this.context = context;
			this.mlist = mlist;
			int padding = ScreenUtils.dip2px(activity, 5);
			width = (ScreenUtils.getScreenWidth(activity) - padding * 4) / 3;
		}

		@Override
		public int getCount() {
			return mlist == null ? 0 : mlist.size();
		}

		@Override
		public UploadImageResponce getItem(int position) {
			return mlist.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewitemHolder vh = null;
			if (convertView == null) {
				vh = new ViewitemHolder();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.item_order_shara_fragment, null);
				vh.findView(convertView);
				convertView.setTag(vh);
			} else {
				vh = (ViewitemHolder) convertView.getTag();
			}
			setLabel(vh, position);
			// LayoutParams lp = (LayoutParams)
			// vh.rl_item_shara.getLayoutParams();
			// int StoreScreen = (ScreenUtils.getScreenWidth(activity) - 40) /
			// 3;
			// lp.width = StoreScreen;
			// lp.height = StoreScreen;
			// 将制定的单位值转换为像素值
			convertView.setLayoutParams(new AbsListView.LayoutParams(width,
					width));
			return convertView;
		}

		/**
		 * 
		 * @Title: setLabel
		 * @Description: 将数据绑定
		 * @author: 欧阳
		 * @param vh
		 * @param position
		 * @return: void
		 */
		public void setLabel(final ViewitemHolder vh, final int position) {
			ImageLoaderUtils.setErrImage(R.drawable.default_prc, R.drawable.default_prc, R.drawable.default_prc);
			ImageLoaderUtils.getInstance(context).loadImage(
					mlist.get(position).Image, vh.shara_photo);
			if (mlist.get(position).clear == true) {
				vh.shara_photo_select.setImageResource(R.drawable.select_red);
			} else {
				vh.shara_photo_select.setImageResource(R.drawable.select_white);
			}
		}
	}

	static class ViewitemHolder {

		@ViewInject(R.id.image_shara_photo2)
		ImageView shara_photo;// 分享图片
		@ViewInject(R.id.image_shara_photo_del2)
		ImageView shara_photo_select;// 选择按钮
		@ViewInject(R.id.rl_item_shara)
		RelativeLayout rl_item_shara;
		@ViewInject(R.id.ll_item_shara)
		LinearLayout ll_item_shara;

		void findView(View convertView) {
			ViewUtils.inject(this, convertView);
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
	@Override
	public void onStop() {
		super.onStop();
		LoadingDailog.dismiss();
	}
}
