package cc.ruit.shunjianmei.home.manager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.NumberPicker.Formatter;
import android.widget.NumberPicker.OnScrollListener;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import cc.ruit.shunjianmei.base.BaseAdapter;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.net.api.FileUploadApi;
import cc.ruit.shunjianmei.net.api.H_SaveHairStyleApi;
import cc.ruit.shunjianmei.net.api.HairStyleDetailApi;
import cc.ruit.shunjianmei.net.request.H_SaveHairStyleRequest;
import cc.ruit.shunjianmei.net.request.HairStyleDetailRequest;
import cc.ruit.shunjianmei.net.request.ServiceItem;
import cc.ruit.shunjianmei.net.request.UploadImageRequest;
import cc.ruit.shunjianmei.net.response.HairStyleDetailResponse;
import cc.ruit.shunjianmei.net.response.UploadImageResponce;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmei.util.FragmentManagerUtils;
import cc.ruit.shunjianmei.util.ImageLoaderUtils;
import cc.ruit.shunjianmei.util.MyEventBus;
import cc.ruit.shunjianmei.util.ScreenUtils;
import cc.ruit.shunjianmeihairdresser.R;
import cc.ruit.utils.sdk.ToastUtils;
import cc.ruit.utils.sdk.file.FileConstant;
import cc.ruit.utils.sdk.file.FileUtil;
import cc.ruit.utils.sdk.http.NetWorkUtils;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.oruit.widget.loadingdialog.LoadingDailog;
import com.oruit.widget.loadingdialog.LoadingViewUtil;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

/**
 * 
 * @ClassName: HairStyleDetailment
 * @Description: 发型编辑
 * @author: 欧阳
 * @date: 2015年10月16日 下午9:06:00
 */
public class HairStyleDetailment extends BaseFragment implements OnValueChangeListener {

	private String imgUrlimgUrl;
	@ViewInject(R.id.iv_hair_style_jianjie)
	private ImageView iv_jianjie;
	@ViewInject(R.id.iv_hair_style_details)
	private ImageView iv_details;
	@ViewInject(R.id.hair_style_intro)
	private EditText intor;// 简介
	@ViewInject(R.id.lv_hair_style_service)
	private ListView lv; // 服务项目
	@ViewInject(R.id.gv_hair_style_detail)
	private GridView gv; // 发型
	@ViewInject(R.id.bt_jianjie)
	private ImageView btJianJie;// 用于简介
	@ViewInject(R.id.bt_detailes)
	private ImageView btDetailes; // 用于详情
	@ViewInject(R.id.hair_style_detail_save)
	Button btnSave;
	@ViewInject(R.id.tv_OrderDetails_title)
	TextView tv_title;
	@ViewInject(R.id.rl_style_detail_save)
	RelativeLayout rl_save;
	private List<HairStyleDetailResponse> list = new ArrayList<HairStyleDetailResponse>();
	private List<ServiceTimes> services;// 服务集合
	private List<HairStyle> styles;// 发型集合
	private ServiceTimesAdapter stAdapter;// 服务项目适配器
	private HairStyleAdapter hsAdapter;// 发型适配器
	AlertDialog dialog;
	int Servicesposition;// 当前的服务位置
	int StylePosition = -1;// 当前发型的服务位置
	int Action;// 当前的行为
	List<ServiceItem> Item;
	int screenWidth;
	public String postion = " ";

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.hair_style_detail_layout, null);
		ViewUtils.inject(this, view);
		initImageViewHeight();// 初始化图片的高度
		initData();// 请求数据
		LoadingDailog.show(activity, "正在加载...");
		initListView();// 将服务展示控件初始化
		initGridview();// 将发型展示控件初始化
		getData();// 接口请求数据
		return view;
	}

	/**
	 * 
	 * @Title: initImageViewHeight
	 * @Description: 给图片设置高度
	 * @author: 欧阳
	 * @return: void
	 */
	private void initImageViewHeight() {
		screenWidth = ScreenUtils.getScreenWidth(activity);

		LayoutParams lp=(LayoutParams) iv_jianjie.getLayoutParams();
		LayoutParams lp1=(LayoutParams) iv_details.getLayoutParams();
		lp.height=screenWidth/2;
//		lp1.height=screenWidth/17*22;

		tv_title.setText("发型");
	}

	/**
	 * 
	 * @Title: initData
	 * @Description: 判断他的行为
	 * @author: 欧阳
	 * @return: void
	 */
	private void initData() {
		if (this.getArguments() != null) {
			if (this.getArguments().getInt("Action") == 1) {
				Action = this.getArguments().getInt("Action");
				rl_save.setVisibility(View.GONE);
				intor.setFocusable(false);
				intor.setFocusableInTouchMode(false);
			}
		}
		// else {
		// return;
		// }

	}

	/**
	 * 
	 * @Title: initGridview
	 * @Description: 初始化发型选项
	 * @author: 欧阳
	 * @return: void
	 */
	private void initGridview() {
		styles = new ArrayList<HairStyle>();
		hsAdapter = new HairStyleAdapter(activity, styles);
		gv.setAdapter(hsAdapter);
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (Action != 1) {
					hsAdapter.setSelectedPosition(position);
					if (hsAdapter.getCurrentPosition() != position) {
						styles.get(hsAdapter.getCurrentPosition()).setChecked("0");
					}
					StylePosition = position;
					hsAdapter.notifyDataSetChanged();
				}
			}
		});

	}

	/**
	 * 
	 * @Title: initListView
	 * @Description: 初始化服务项目
	 * @author: 欧阳
	 * @return: void
	 */
	private void initListView() {
		services = new ArrayList<ServiceTimes>();
		stAdapter = new ServiceTimesAdapter(activity, services);
		lv.setAdapter(stAdapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// 选择时间
				if (Action != 1) {
					timeSelect(services.get(position).getTimes());
					Servicesposition = position;
				}
			}
		});
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
	 * @Title: onClick
	 * @Description: 用于简介和用于详情的监听
	 * @author: gaoj
	 * @param v
	 * @return: void
	 */
	@OnClick({ R.id.bt_jianjie, R.id.bt_detailes, R.id.hair_style_detail_save, R.id.iv_OrderDetails_back })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_jianjie:// 选取发型
			// TODO 相片选取
			if (Action != 1) {
				selectPhoto();
				postion = "jianjie";
			} else {
				btJianJie.setEnabled(false);
			}
			break;
		case R.id.bt_detailes:// 发型图详情
			if (Action != 1) {
				postion = "detailes";
				selectPhoto();
			} else {
				btDetailes.setEnabled(false);
			}
			break;
		case R.id.hair_style_detail_save:// 保存
			// TODO 调用接口上传
			SaveHairDetail();
			break;
		case R.id.iv_OrderDetails_back:
			FragmentManagerUtils.back(activity, 0);
			break;
		default:
			break;
		}

	}

	/**
	 * 
	 * @Title: SaveHairDetail
	 * @Description: 保存发型
	 * @author: 欧阳
	 * @return: void
	 */
	private void SaveHairDetail() {
		if (!NetWorkUtils.isConnectInternet(getActivity())) {
			ToastUtils.showShort("网络未链接，请检查网络设置");
			return;
		} else {
			String Remark = intor.getText().toString();
			if (Remark.length() < 10) {
				ToastUtils.showShort("描述输入的字数不够哟");
				return;
			} else if (Remark.length() > 200) {
				ToastUtils.showShort("描述输入的字数太长了");
				return;
			} else if (StylePosition == -1) {
				ToastUtils.showShort("请选择发型");
				return;
			} else if (UserManager.getjianjiephotoID() == 0) {
				ToastUtils.showShort("请选择用于简介的图片");
				return;
			} else if (UserManager.getdetailphotoID() == 0) {
				ToastUtils.showShort("请选择用于详情的图片");
				return;
			}
			saveHairData();
			H_SaveHairStyleRequest request = new H_SaveHairStyleRequest(UserManager.getUserID() + "", ID,
					UserManager.getjianjiephotoID() + "", UserManager.getdetailphotoID() + "",
					styles.get(StylePosition).getID(), Remark, Item);
			H_SaveHairStyleApi.HairStyleList(request, new RequestCallBack<String>() {

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
						ToastUtils.showShort("保存成功");
						// 发送消息
						EventBus.getDefault().post(new MyEventBus("刷新数据"));
						FragmentManagerUtils.back(activity, 0);

					}
				}

				@Override
				public void onFailure(HttpException error, String msg) {
					ToastUtils.showShort("数据请求失败");
				}
			});
		}
	}

	/**
	 * 
	 * @Title: saveHairData
	 * @Description: 保存发型数据
	 * @author: 欧阳
	 * @return: void
	 */
	private void saveHairData() {
		Item = new ArrayList<ServiceItem>();
		for (int i = 0; i < services.size(); i++) {
			ServiceItem Serviceitem = new ServiceItem();
			Serviceitem.setCode(services.get(i).getCode());
			Serviceitem.setTimes(services.get(i).getTimes());
			Item.add(Serviceitem);
		}
	}

	private String photo_name;
	String imgUrl;
	private boolean headPhotoUploadingSuccess = false;

	/**
	 * 
	 * @Title: selectPhoto
	 * @Description: 选取照片
	 * @author: 欧阳
	 * @return: void
	 */
	private void selectPhoto() {
		photo_name = System.currentTimeMillis() + ".jpg";
		if (FileConstant.sdCardIsExist) {
			if (FileConstant.sdCardIsExist) {
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent, FileConstant.PHOTOALBUM);
			} else {
				ToastUtils.showShort("无法使用图片功能,请先插入sd卡");
			}
		}
	}

	/**
	 * 
	 * @Title: onActivityResult
	 * @Description: 选取图片的结果返回
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 * @see android.support.v4.app.Fragment#onActivityResult(int, int,
	 *      android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case FileConstant.PHOTOALBUM:
			if (data == null) {
				ToastUtils.showLong("图片获取失败");
				return;
			}
			String path = data.getData().toString();
			imgUrl = FileUtil.uri2filePath(activity, data.getData());
			if (!".jpg".equals(FileUtil.getFileExtensionByFilePath(imgUrl))
					&& !".png".equals(FileUtil.getFileExtensionByFilePath(imgUrl))) {
				ToastUtils.showShort("请选择.jpg或.png图片");
			} else {
				Bitmap bm = BitmapFactory.decodeFile(imgUrl);
				Bitmap bmm = zoomImg(bm, 960, 640);
				Uri uri = Uri.parse(MediaStore.Images.Media
						.insertImage(HairStyleDetailment.this.getActivity().getContentResolver(), bmm, null, null));
				LoadingDailog.show(activity, "正在上传图片", false);
				imgUrlimgUrl = FileUtil.uri2filePath(getActivity(), uri);
				UpLoadPhoto();

			}

			break;
		case FileConstant.PHOTORESOULT:
			if (data == null) {
				return;
			}
			break;
		default:
			break;
		}

	}

	/**
	 * 
	 * @Title: UpLoadPhoto
	 * @Description: 上传图片
	 * @author: 欧阳
	 * @return: void
	 */
	private void UpLoadPhoto() {
		if (!NetWorkUtils.isConnectInternet(getActivity())) {
			ToastUtils.showShort("网络未链接，请检查网络设置");
			LoadingDailog.dismiss();
			return;
		}
		UploadImageRequest sr = new UploadImageRequest(UserManager.getUserID() + "");
		new FileUploadApi().upload(imgUrlimgUrl, sr.toJsonString(sr), new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				BaseResponse result = BaseResponse.getBaseResponse(responseInfo.result);
				String[] split = result.getMsg().split("\\|");
				if ("1".equals(split[0])) {
					ToastUtils.showShort(split[1] + "");
					LoadingDailog.dismiss();
				}
				if (result.getCode() == 1000) {
					List<UploadImageResponce> Responce = UploadImageResponce.getclazz(result.getData());
					if (postion == "jianjie") {
						UserManager.setjianjiephotoID(Integer.parseInt(Responce.get(0).ID));
					} else if (postion == "detailes") {
						UserManager.setdetailphotoID(Integer.parseInt(Responce.get(0).ID));
					}
					setHeadPhoto(Responce.get(0).Image);
					LoadingDailog.dismiss();
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				ToastUtils.showLong(R.string.request_failure);
				LoadingDailog.dismiss();
			}

		});

	}

	/**
	 * 
	 * @Title: setHeadPhoto
	 * @Description: 设置头像
	 * @author: 欧阳
	 * @return: void
	 */
	protected void setHeadPhoto(String image) {
		headPhotoUploadingSuccess = true;
		ImageLoaderUtils.setErrImage(R.drawable.default_prc, R.drawable.default_prc, R.drawable.default_prc);
		if (postion == "jianjie") {
			btJianJie.setVisibility(View.INVISIBLE);
			ImageLoaderUtils.getInstance(activity).loadImage(image, iv_jianjie);
		} else if (postion == "detailes") {
			btDetailes.setVisibility(View.INVISIBLE);
			ImageLoaderUtils.getInstance(activity).loadImage(image, iv_details);
		}
	}

	/**
	 * 
	 * @Title: openSystemZoomImage
	 * @Description: 打开系统的裁剪工具
	 * @author: 欧阳
	 * @param uri
	 * @param output_uri
	 * @param width
	 * @param height
	 * @return: void
	 */
	public void openSystemZoomImage(Uri uri, Uri output_uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 发送裁剪信号
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 2);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", screenWidth);
		intent.putExtra("outputY", screenWidth / 2);
		// 定义输出路径
		intent.putExtra("outputFormat", "JPEG");
		intent.putExtra("noFaceDetection", false);
		intent.putExtra("return-data", true);
		intent.putExtra("scaleUpIfNeeded", true);// 如果小于要求输出大小，就放大
		intent.putExtra("scale", true);// 缩放
		intent.putExtra(MediaStore.EXTRA_OUTPUT, output_uri);
		startActivityForResult(intent, FileConstant.PHOTORESOULT);
	}

	/**
	 * @Title: initTitle
	 * @Description: 标题初始化
	 * @author: lee
	 * @return: void
	 */
	String ID;

	/**
	 * @Title: getPoints
	 * @Description: 获取数据
	 * @author: gaoj
	 * @return: void
	 */
	public void getData() {
		if (this.getArguments() != null) {
			ID = getArguments().getString("HairStyleDetail").toString();

		} else {
			ID = "0";
		}
		HairStyleDetailRequest request = new HairStyleDetailRequest("" + UserManager.getUserID(), ID);
		HairStyleDetailApi.HairStyleDetail(request, new RequestCallBack<String>() {

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
					List<HairStyleDetailResponse> response = HairStyleDetailResponse.getclazz2(result.getData());
					if (response.size() > 0 && response != null) {
						setData(response.get(0));
						services.addAll(response.get(0).getServiceTimes());
						styles.addAll(response.get(0).getHairStyle());
					}

				}
				hsAdapter.notifyDataSetChanged();
				stAdapter.notifyDataSetChanged();
				LoadingDailog.dismiss();
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
	 * @Title: setData
	 * @Description: 设置网络请求的数据
	 * @author: Johnny
	 * @return: void
	 */
	private void setData(HairStyleDetailResponse data) {
		ImageLoaderUtils.setErrImage(R.drawable.default_prc, R.drawable.default_prc, R.drawable.default_prc);
		int PicID1 = 0;
		int PicID2 = 0;
		try {
			PicID1 = Integer.parseInt(data.getPicID1());
			PicID2 = Integer.parseInt(data.getPicID2());
		} catch (Exception e) {

		}
		UserManager.setjianjiephotoID(PicID1);
		if (!TextUtils.isEmpty(data.getPhoto())) {
			ImageLoaderUtils.setErrImage(R.drawable.default_prc, R.drawable.default_prc, R.drawable.default_prc);
			ImageLoaderUtils.getInstance(activity).loadImage(data.getPhoto(), iv_jianjie);
			ImageLoaderUtils.getInstance(activity).loadImage(data.getImage(), iv_details);
		}
		UserManager.setdetailphotoID(PicID2);
		intor.setText(data.getIntro());
		for (int i = 0; i < data.getHairStyle().size(); i++) {
			if (Integer.parseInt(data.getHairStyle().get(i).getChecked()) == 1) {
				StylePosition = i;
			}
		}

	}

	NumberPicker hour;// 选小时
	NumberPicker minute;// 选分钟
	TextView hm;// 显示时间
	int currentHour = -1;// 当前时间
	int currentMinute = -1;// 当前分钟

	/**
	 * 
	 * @Title: timeSelect
	 * @Description: 时间选择
	 * @author: 欧阳
	 * @return: void
	 */
	public void timeSelect(final String times) {
		LayoutInflater inflater = LayoutInflater.from(activity);
		View TimeView = inflater.inflate(R.layout.dailog_time_select, null);
		Button cancel = (Button) TimeView.findViewById(R.id.btn_time_select_cancel);
		Button sumbit = (Button) TimeView.findViewById(R.id.btn_time_select_sumbit);
		hm = (TextView) TimeView.findViewById(R.id.hair_style_time_hourmine);
		hour = (NumberPicker) TimeView.findViewById(R.id.num_time_select_hourpicker);
		minute = (NumberPicker) TimeView.findViewById(R.id.num_time_select_minuteicker);
		AlertDialog.Builder Timebuilder = new AlertDialog.Builder(activity);
		Timebuilder.setView(TimeView);
		Timebuilder.setCancelable(true);
		hour.getChildAt(0).setFocusable(false);
		minute.getChildAt(0).setFocusable(false);
		int time = Integer.parseInt(times);
		hm.setText(time / 60 + "小时" + (time - time / 60 * 60) + "分钟");
		initNumberPicker(hour, minute, times);
		hour.setOnValueChangedListener(this);
		minute.setOnValueChangedListener(this);
		cancel.setOnClickListener(new OnClickListener() {// 取消

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		sumbit.setOnClickListener(new OnClickListener() {// 确认

			@Override
			public void onClick(View v) {
				services.get(Servicesposition).setTimes((currentHour * 60 + currentMinute) + "");
				stAdapter.notifyDataSetChanged();
				dialog.dismiss();
			}
		});
		dialog = Timebuilder.show();
	}

	/**
	 * 
	 * @Title: onValueChange
	 * @Description: 时间改变
	 * @author: 欧阳
	 * @param picker
	 * @param oldVal
	 * @param newVal
	 * @return: void
	 */
	@Override
	public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
		if (picker == hour) {
			Log.i("aa", newVal + "");
			currentHour = newVal;
			hm.setText(currentHour + "小时");
		} else if (picker == minute) {
			currentMinute = newVal;
			hm.setText(currentHour + "小时" + currentMinute + "分钟");
		}
	}

	/**
	 * 
	 * @Title: initNumberPicker
	 * @Description: 初始化数字选择器
	 * @author: 欧阳
	 * @param hour
	 * @param minute
	 * @return: void
	 */
	private void initNumberPicker(NumberPicker hour, NumberPicker minute, String times) {
		int time = Integer.parseInt(times);
		hour.setMaxValue(24);
		hour.setMinValue(0);
		hour.setValue(time / 60);
		currentHour = time / 60;
		hour.setWrapSelectorWheel(false);
		hour.setFocusable(false);
		minute.setMaxValue(60);
		minute.setMinValue(0);
		minute.setWrapSelectorWheel(false);
		minute.setFocusable(false);
		minute.setValue(time - time / 60 * 60);
		currentMinute = time - time / 60 * 60;
		minute.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChange(NumberPicker view, int scrollState) {
			}
		});
		hour.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChange(NumberPicker view, int scrollState) {
			}
		});
		hour.setFormatter(new Formatter() {

			@Override
			public String format(int value) {
				return "" + value;
			}
		});
		minute.setFormatter(new Formatter() {

			@Override
			public String format(int value) {
				return "" + value;
			}
		});
	}
	/**
	 * 
	 * @ClassName: HairStyleAdapter
	 * @Description: 发型适配器
	 * @author: 欧阳
	 * @date: 2015年10月16日 下午10:43:29
	 */
	class HairStyleAdapter extends BaseAdapter {

		private Context context;
		private List<HairStyle> hairs;
		private int selectedPosition = -1;
		private int currentPosition;

		public HairStyleAdapter(Context context, List<HairStyle> hairs) {
			super(context, hairs);
			this.context = context;
			this.hairs = hairs;
		}

		@Override
		public int getCount() {
			return hairs != null ? hairs.size() : 0;
		}

		@Override
		public HairStyle getItem(int position) {
			return hairs.get(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = LayoutInflater.from(activity).inflate(R.layout.item_hair_style_detail_gv, null);
			TextView tv = (TextView) convertView;
			HairStyle hs = getItem(position);
			if (Integer.parseInt(getItem(position).getChecked()) == 1) {
				Drawable img;
				Resources res = getResources();
				img = res.getDrawable(R.drawable.ture2x);
				img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
				tv.setCompoundDrawables(img, null, null, null);
				currentPosition = position;
			}

			if (selectedPosition == position) {
				Drawable img;
				Resources res = getResources();
				img = res.getDrawable(R.drawable.ture2x);
				img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
				tv.setCompoundDrawables(img, null, null, null);
			} else {
				Drawable img;
				Resources res = getResources();
				img = res.getDrawable(R.drawable.quan2x);
				img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
			}

			((TextView) convertView).setText(hs.getName());
			return convertView;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		public void setSelectedPosition(int selectedPosition) {
			this.selectedPosition = selectedPosition;
		}

		public int getCurrentPosition() {
			return currentPosition;
		}

		public void setCurrentPosition(int currentPosition) {
			this.currentPosition = currentPosition;
		}

	}

	/**
	 * @ClassName: ServiceTimesAdapter
	 * @Description: 服务项目
	 * @author: gaoj
	 * @date: 2015年10月16日 下午2:10:29
	 */
	class ServiceTimesAdapter extends BaseAdapter {
		private Context context;
		private List<ServiceTimes> services;

		public ServiceTimesAdapter(Context context, List<ServiceTimes> services) {
			super(context, services);
			this.context = context;
			this.services = services;
		}

		@Override
		public int getCount() {
			return services != null ? services.size() : 0;
		}

		@Override
		public ServiceTimes getItem(int position) {
			return services.get(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ServiceViewHolder sv = null;
			if (convertView == null) {
				sv = new ServiceViewHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.item_hair__style_code_item_layout, null);
				sv.findView(convertView);
				convertView.setTag(sv);
			} else {
				sv = (ServiceViewHolder) convertView.getTag();
			}
			// 得到数据和绑定数据
			setLable(position, sv);
			return convertView;
		}

		/**
		 * @Title: setLable
		 * @Description:绑定控件
		 * @author: gaoj
		 * @param position
		 * @param vh
		 * @return: void
		 */
		private void setLable(int position, ServiceViewHolder sv) {
			ServiceTimes obj = getItem(position);
			sv.tv_service.setText(obj.getName());
			int time = Integer.parseInt(obj.getTimes());
			if (time / 60 <= 1) {
				sv.tv_service_time.setText(time + "分钟");
			} else {
				sv.tv_service_time.setText(time / 60 + "小时" + (time - time / 60 * 60) + "分钟");
			}

		}
	}

	static class ServiceViewHolder {
		@ViewInject(R.id.hair_style_code)
		TextView tv_service;
		@ViewInject(R.id.hair_style_code_time)
		TextView tv_service_time;

		@ViewInject(R.id.ll_content)
		LinearLayout ll_content;

		void findView(View convertView) {
			ViewUtils.inject(this, convertView);
		}
	}

	public int getScreenWidth() {
		DisplayMetrics metric = new DisplayMetrics();
		WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels;
		return width;
	}

	/**
	 * 处理图片
	 * 
	 * @param bm
	 *            所要转换的bitmap
	 * @param newWidth新的宽
	 * @param newHeight新的高
	 * @return 指定宽高的bitmap
	 */
	public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片 www.2cto.com
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
		return newbm;
	}

}
