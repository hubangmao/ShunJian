package cc.ruit.shunjianmei.home.order;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import cc.ruit.shunjianmei.R;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.net.api.FileUploadApi;
import cc.ruit.shunjianmei.net.api.InitEvaluationApi;
import cc.ruit.shunjianmei.net.api.SaveEvaluationApi;
import cc.ruit.shunjianmei.net.request.InitEvaluationRequest;
import cc.ruit.shunjianmei.net.request.SaveEvaluationRequest;
import cc.ruit.shunjianmei.net.request.UploadImageRequest;
import cc.ruit.shunjianmei.net.response.InitEvaluationResponse;
import cc.ruit.shunjianmei.net.response.InitEvaluationResponse.Store;
import cc.ruit.shunjianmei.net.response.InitEvaluationResponse.User;
import cc.ruit.shunjianmei.net.response.SaveEvaluationResponse;
import cc.ruit.shunjianmei.net.response.UploadImageResponce;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmei.util.FragmentManagerUtils;
import cc.ruit.shunjianmei.util.ImageLoaderUtils;
import cc.ruit.shunjianmei.util.MyEventBus;
import cc.ruit.shunjianmei.util.RoundImageLoaderUtil;
import cc.ruit.shunjianmei.util.ScreenUtils;
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
import com.oruit.widget.title.TitleUtil;

import de.greenrobot.event.EventBus;

/**
 * 
 * @ClassName: OrderEvaluationFragment
 * @Description: 评论界面
 * @author: 欧阳
 * @date: 2015年10月27日 上午9:00:29
 */
public class OrderEvaluationFragment extends BaseFragment implements OnRatingBarChangeListener {

	private Bitmap bm_finish;
	private String imgUrlimgUrl = null;
	private String path;
	@ViewInject(R.id.image_orderEvaluation_photo)
	ImageView hairstylist_photo;// 发型师头像
	@ViewInject(R.id.tv_orderEvaluation_master_name)
	TextView hairstylist_Name;// 发型师名称
	@ViewInject(R.id.tv_orderEvaluation_level)
	TextView levelName;
	@ViewInject(R.id.ratingbar_masterevaluate)
	RatingBar hairstylist_evaluate;// 发型师评论
	@ViewInject(R.id.tv_orderevaluation_quantity)
	TextView order_quantity;// 接单量
	@ViewInject(R.id.tv_masterevaluation_star)
	TextView star;// 发型师星座
	@ViewInject(R.id.ed_hairdessert_evaluation)
	EditText hairdessert_evaluation;// 发型师评论的内容
	@ViewInject(R.id.ratingbar_communication_evaluate)
	RatingBar communicationEvaluate;// 沟通评论
	@ViewInject(R.id.ratingbar_profession_evaluate)
	RatingBar professionEvaluate;// 专业评论
	@ViewInject(R.id.ratingbar_ontime_evaluate)
	RatingBar ontimeEvaluate;// 守时评论

	@ViewInject(R.id.image_orderevalution_StorePhoto)
	ImageView StorePhoto;// 店铺图片
	@ViewInject(R.id.tv_orderevalution_StoreName)
	TextView StoreName;// 店铺名称
	@ViewInject(R.id.tv_orderevalutions_StoreAddress)
	TextView StoreAddress;// 店铺地址
	@ViewInject(R.id.ratingbar_orderevalutions_StoreScore)
	RatingBar StoreScore;// 店铺评分
	@ViewInject(R.id.tv_orderevalutions_OrderNum)
	TextView OrderNum;// 店铺接单量
	@ViewInject(R.id.tv_orderevalutions_CarNum)
	TextView CarNum;// 停车位
	@ViewInject(R.id.ed_Store_evaluation)
	EditText StoreEvaluation;// 店铺评论
	@ViewInject(R.id.LinearLayout_image_photo_uoload)
	LinearLayout photo_uoload;// 发型图片
	@ViewInject(R.id.ll_image_Storephoto)
	LinearLayout image_Storephoto;// 店铺的图片
	@ViewInject(R.id.ratingbar_environment_evaluate)
	RatingBar environmentEvaluate;// 环境评论
	@ViewInject(R.id.ratingbar_traffic_evaluate)
	RatingBar trafficEvaluate;// 交通评论
	@ViewInject(R.id.btn_report)
	Button report;
	AlertDialog dialog;// 对话框
	List<SaveEvaluationRequest.Item> listItem = new ArrayList<SaveEvaluationRequest.Item>();// 发型评论分数
	List<SaveEvaluationRequest.Item> StoreList = new ArrayList<SaveEvaluationRequest.Item>();// 店铺评论
	List<UploadImageResponce> PhotoID = new ArrayList<UploadImageResponce>();
	List<UploadImageResponce> StoreID = new ArrayList<UploadImageResponce>();
	public static String Evaluation = "";

	int imageindex = 1;
	int Storeindex = 1;
	public static final int IMAGE_SELECT = 3;
	public static final int STOREIMAGE_SELECT = 2;
	int position;

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.order_evaluation_fragment, null);
		ViewUtils.inject(this, view);
		LoadingDailog.show(activity, "正在加载...");
		initTitle();
		initData();
		initRatingbar();
		return view;
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
		// 设置标题栏中间的文字
		titleUtil.tv_title.setText("评论");
		// 设置标题栏左边的图片
		titleUtil.iv_left.setVisibility(View.VISIBLE);
		titleUtil.iv_left.setImageResource(R.drawable.back);
		titleUtil.iv_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				alertDialog();
			}
		});
	}

	/**
	 * 
	 * @Title: alertDialog
	 * @Description: 询问对话框
	 * @author: 欧阳
	 * @return: void
	 */
	protected void alertDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setMessage("评论还没完成，您确定要离开？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				boolean isBack = FragmentManagerUtils.back(getActivity(), R.id.content_frame);
				if (!isBack) {
					getActivity().finish();
				}

			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		// 最重要的代码
		dialog = builder.show();

	}

	String OrderID;// 订单ID

	/**
	 * 
	 * @Title: initData
	 * @Description: 得到上一个界面的值
	 * @author: 欧阳
	 * @return: void
	 */
	private void initData() {
		if (this.getArguments() != null) {
			OrderID = this.getArguments().get("ID").toString();
			getData();
		} else {
			LoadingDailog.dismiss();

		}
	}

	/**
	 * 
	 * @Title: getData
	 * @Description: 网络请求数据
	 * @author: 欧阳
	 * @return: void
	 */
	private void getData() {
		if (!NetWorkUtils.isConnectInternet(getActivity())) {
			ToastUtils.showShort("网络未链接，请检查网络设置");
			LoadingDailog.dismiss();
			return;
		} else {
			InitEvaluationRequest request = new InitEvaluationRequest(UserManager.getUserID() + "", OrderID);
			InitEvaluationApi.InitEvaluation(request, new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					BaseResponse result = BaseResponse.getBaseResponse(responseInfo.result);
					String[] split = result.getMsg().split("\\|");
					if ("1".equals(split[0])) {
						ToastUtils.showShort(split[1] + "");
					}
					if (result.getCode() == 1000) {
						List<InitEvaluationResponse> mlist = InitEvaluationResponse.getclazz2(result.getData());
						if (mlist.size() > 0 && mlist != null) {
							Bungewidget(mlist);
						}
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
	}

	/**
	 * 
	 * @Title: Bungewidget
	 * @Description: 绑定控件
	 * @author: 欧阳
	 * @param mlist
	 * @return: void
	 */
	protected void Bungewidget(List<InitEvaluationResponse> mlist) {
		User user = mlist.get(0).User.get(0);
		RoundImageLoaderUtil.setErrImage(R.drawable.tx_man, R.drawable.tx_man, R.drawable.tx_man);
		RoundImageLoaderUtil.getInstance(activity, 360).loadImage(user.Photo, hairstylist_photo);
		hairstylist_Name.setText(user.Name);
		levelName.setText(user.LevelName);
		if (!TextUtils.isEmpty(user.Score)) {
			Float masterScore = Float.valueOf(user.Score);
			hairstylist_evaluate.setRating(masterScore);
		} else {
			hairstylist_evaluate.setRating(0);
		}
		order_quantity.setText("接单" + user.OrderNum + "次");
		Store Store = mlist.get(0).Store.get(0);
		ImageLoaderUtils.setErrImage(R.drawable.default_prc, R.drawable.default_prc, R.drawable.default_prc);
		ImageLoaderUtils.getInstance(activity).loadImage(Store.Photo, StorePhoto);
		StoreName.setText(Store.Name);
		StoreAddress.setText(Store.Address);
		if (!TextUtils.isEmpty(Store.Score)) {
			Float StoreScore_info = Float.valueOf(Store.Score);
			StoreScore.setRating(StoreScore_info);
		} else {
			StoreScore.setRating(0);
		}
		OrderNum.setText("接单" + Store.OrderNum + "次");
		CarNum.setText("车位:" + Store.CarNum + "个");
	}

	private void initRatingbar() {
		communicationEvaluate.setOnRatingBarChangeListener(this);
		professionEvaluate.setOnRatingBarChangeListener(this);
		ontimeEvaluate.setOnRatingBarChangeListener(this);
		environmentEvaluate.setOnRatingBarChangeListener(this);
		trafficEvaluate.setOnRatingBarChangeListener(this);

	}

	String hairdessertEvaluation;// 商品评论
	String Store_Evaluation;// 店铺评论

	/**
	 * 
	 * @Title: methodClick
	 * @Description: 选择照片的按钮处理
	 * @author: 欧阳
	 * @param v
	 * @return: void
	 */
	@OnClick({ R.id.iv_hairdessert_evaluation, R.id.iv_Store_evaluation, R.id.btn_report })
	public void methodClick(View v) {
		hairdessertEvaluation = hairdessert_evaluation.getText().toString();
		Store_Evaluation = StoreEvaluation.getText().toString();
		switch (v.getId()) {
		case R.id.iv_hairdessert_evaluation:
			if (PhotoID.size() < 3) {
				position = IMAGE_SELECT;
				showDialog_tupian();
			} else {
				ToastUtils.showShort("亲 最多只能上传三张哟");
			}
			break;
		case R.id.iv_Store_evaluation:
			if (StoreID.size() < 2) {
				position = STOREIMAGE_SELECT;
				showDialog_tupian();
			} else {
				ToastUtils.showShort("亲 最多只能上传两种张哟");
			}
			break;
		case R.id.btn_report:// 发表
			// TODO 点击发表调用接口
			Evaluation = "first";
			if (TextUtils.isEmpty(hairdessertEvaluation)) {
				hairdessertEvaluation = "";
			}
			if (TextUtils.isEmpty(Store_Evaluation)) {
				Store_Evaluation = "";
			}
			SendNetRequest();
			break;
		}
	}

	private String photo_name;
	String imgUrl;

	/**
	 * 
	 * @Title: showDialog_tupian
	 * @Description: 保存图片
	 * @author: 欧阳
	 * @return: void
	 */
	private void showDialog_tupian() {
		photo_name = System.currentTimeMillis() + ".jpg";

		View view = activity.getLayoutInflater().inflate(R.layout.photo_choose_dialog, null);
		Button btn_tulu = (Button) view.findViewById(R.id.tuku);
		Button btn_paizhao = (Button) view.findViewById(R.id.paizhao);
		Button btn_quxiao = (Button) view.findViewById(R.id.quxiao);

		final Dialog dialog = new Dialog(activity, R.style.transparentFrameWindowStyle);
		dialog.setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		Window window = dialog.getWindow();
		// 设置显示动画
		window.setWindowAnimations(R.style.main_menu_animstyle);
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = 0;
		wl.y = activity.getWindowManager().getDefaultDisplay().getHeight();
		// 以下这两句是为了保证按钮可以水平满屏
		wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
		wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

		// 设置显示位置
		dialog.onWindowAttributesChanged(wl);
		// 设置点击外围解散
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
		btn_quxiao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		// 从相册里面选取
		btn_tulu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (FileConstant.sdCardIsExist) { //
					if (FileConstant.sdCardIsExist) {
						Intent intent = new Intent(Intent.ACTION_PICK, null);
						intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
						startActivityForResult(intent, FileConstant.PHOTOALBUM);
					} else {
						ToastUtils.showShort("无法使用图片功能,请先插入sd卡");
					}
					dialog.dismiss();
				}
			}
		});
		// 拍照
		btn_paizhao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (FileConstant.sdCardIsExist) {
					Uri uri = Uri.fromFile(new File(FileConstant.SD_PATH + FileConstant.IMG_PATH_CAMPAIGN, photo_name));
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
					startActivityForResult(intent, FileConstant.PHOTOCAPTURE);
				} else {
					ToastUtils.showShort("无法使用图片功能,请先插入sd卡");
				}
				dialog.dismiss();
			}
		});
	}

	// /**
	// *
	// * @Title: onActivityResult
	// * @Description: 选取图片的结果返回
	// * @param requestCode
	// * @param resultCode
	// * @param data
	// * @see android.support.v4.app.Fragment#onActivityResult(int, int,
	// * android.content.Intent)
	// */
	// @Override
	// public void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// super.onActivityResult(requestCode, resultCode, data);
	// switch (requestCode) {
	// case FileConstant.PHOTOALBUM:
	// if (data == null) {
	// return;
	// }
	// openSystemZoomImage(
	// data.getData(),
	// Uri.fromFile(new File(FileConstant.SD_PATH
	// + FileConstant.UPLOAD_PHOTO_PATH, photo_name)));
	// break;
	// case FileConstant.PHOTOCAPTURE:
	// try {
	// // 设置文件保存路径这里放在根目录下
	// File picture = new File(FileConstant.SD_PATH
	// + FileConstant.UPLOAD_PHOTO_PATH, photo_name);
	// File pic = new File(FileConstant.SD_PATH
	// + FileConstant.IMG_PATH_CAMPAIGN, photo_name);
	// openSystemZoomImage(Uri.fromFile(pic), Uri.fromFile(picture));
	// } catch (Exception e) {
	// ToastUtils.showLong("您的相机发生异常，请重新拍照");
	// }
	// break;
	// case FileConstant.PHOTORESOULT:
	// if (data == null) {
	// return;
	// }
	// imgUrl = FileConstant.SD_PATH + FileConstant.UPLOAD_PHOTO_PATH
	// + photo_name;
	// // *******网络请求图片*********//
	// UpLoadPhoto();
	// break;
	// default:
	// break;
	// }
	// }
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
		ContentResolver resolver = OrderEvaluationFragment.this.getActivity().getContentResolver();
		switch (requestCode) {
		case FileConstant.PHOTOALBUM:
			if (data == null) {
				return;
			}
			imgUrl = FileUtil.uri2filePath(getActivity(), data.getData());
			Bitmap bmp = BitmapOption(bm_finish);
			// bm_finish = BitmapFactory.decodeFile(imgUrl);
			if (bmp != null) {
				// Bitmap mmbit = compressBitmap(bm_finish, 5120);
				// if (mmbit != null) {
				Uri uri = Uri.parse(MediaStore.Images.Media
						.insertImage(OrderEvaluationFragment.this.getActivity().getContentResolver(), bmp, null, null));
				imgUrl = FileUtil.uri2filePath(getActivity(), uri);
				// }
			} else {

			}

			// saveBitmap2file(mmbit, "qqq.jpg");
			// imgUrlimgUrl = FileUtil.uri2filePath(getActivity(), uri);
			//
			// } else {
			// Log.i("Tag", "hhhh12");
			// }
			// // imgUrl = null;
			// // saveBitmap2file(mmbit, imgUrl);
			// } else {
			// Log.i("Tag", "hhhh2");
			// }

			if (!".jpg".equals(FileUtil.getFileExtensionByFilePath(imgUrl))
					&& !".png".equals(FileUtil.getFileExtensionByFilePath(imgUrl))) {
				ToastUtils.showShort("请选择.jpg或.png图片");
			} else {

				LoadingDailog.show(getActivity(), "正在上传图片", false);
				// LoadingDailog.show(getActivity(), "1000");
				UpLoadPhoto();
			}
			break;
		case FileConstant.PHOTOCAPTURE:
			File file = new File(FileConstant.SD_PATH + FileConstant.IMG_PATH_CAMPAIGN, photo_name);
			if (!file.exists()) {
				return;
			}
			try {
				imgUrl = Uri.fromFile(new File(FileConstant.SD_PATH + FileConstant.IMG_PATH_CAMPAIGN, photo_name))
						.toString();
				imgUrl = imgUrl.substring(7).toString();
				Bitmap bm = BitmapOption(bm_finish);
				// bm_finish = BitmapFactory.decodeFile(imgUrl);
				if (bm != null) {
					// Bitmap mmbit = compressBitmap(bm_finish, 5120);
					// if (mmbit != null) {
					Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(
							OrderEvaluationFragment.this.getActivity().getContentResolver(), bm, null, null));
					imgUrl = FileUtil.uri2filePath(getActivity(), uri);
					// }
				} else {

				}
				if (!".jpg".equals(FileUtil.getFileExtensionByFilePath(imgUrl))
						&& !".png".equals(FileUtil.getFileExtensionByFilePath(imgUrl))) {
					ToastUtils.showShort("请选择.jpg或.png图片");
				} else {
					LoadingDailog.show(activity, "正在上传图片", false);
					UpLoadPhoto();

				}
			} catch (Exception e) {
				ToastUtils.showLong("您的相机发生异常，请重新拍照");
			}
			break;
		default:
			break;
		}
	}
	// /**
	// *
	// * @Title: openSystemZoomImage
	// * @Description: 系统裁剪图片
	// * @author: 欧阳
	// * @param uri
	// * @param output_uri
	// * @return: void
	// */
	// public void openSystemZoomImage(Uri uri, Uri output_uri) {
	// Intent intent = new Intent("com.android.camera.action.CROP");
	// intent.setDataAndType(uri, "image/*");
	// // 发送裁剪信号
	// intent.putExtra("crop", "true");
	// // aspectX aspectY 是宽高的比例
	// intent.putExtra("aspectX", 1);
	// intent.putExtra("aspectY", 1);
	// // outputX outputY 是裁剪图片宽高
	// intent.putExtra("outputX", 200);// 还要判断一下是从发型图片还是店铺图片
	// intent.putExtra("outputY", 200);
	// // 定义输出路径
	// intent.putExtra("outputFormat", "JPEG");
	// intent.putExtra("noFaceDetection", true);
	// intent.putExtra("return-data", true);
	// intent.putExtra(MediaStore.EXTRA_OUTPUT, output_uri);
	// startActivityForResult(intent, FileConstant.PHOTORESOULT);
	// }

	private Bitmap BitmapOption(Bitmap bitmap) {
		Options opts = new Options();
		opts.inJustDecodeBounds = true;
		bitmap = BitmapFactory.decodeFile(imgUrl, opts);
		opts.inPreferredConfig = Config.ARGB_8888;
		opts.inSampleSize = calculateInSampleSize(opts, 480, 800);
		opts.inJustDecodeBounds = false;
		opts.inPurgeable = true;
		opts.inInputShareable = true;
		bitmap = BitmapFactory.decodeFile(imgUrl, opts);
		return bitmap;
	}
	//计算图片的缩放值
	public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;

	    if (height > reqHeight || width > reqWidth) {
	             final int heightRatio = Math.round((float) height/ (float) reqHeight);
	             final int widthRatio = Math.round((float) width / (float) reqWidth);
	             inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	    }
	        return inSampleSize;
	}
	private Object readStream(InputStream openInputStream) {
		// TODO Auto-generated method stub
		return null;
	}

	List<UploadImageResponce> Responce;

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
			return;
		} else {
			UploadImageRequest sr = new UploadImageRequest(UserManager.getUserID() + "");

			// try {
			// saveBitmap2file(compressBitmap(getBitmap(imgUrl), 5120), path);
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// LoadingDailog.show(getActivity(), "正在上传图片", true);
			new FileUploadApi().upload(imgUrl, sr.toJsonString(sr), new RequestCallBack<String>() {
				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					LoadingDailog.dismiss();
					BaseResponse result = BaseResponse.getBaseResponse(responseInfo.result);
					String[] split = result.getMsg().split("\\|");
					if ("1".equals(split[0])) {
						ToastUtils.showShort(split[1] + "");
					}
					if (result.getCode() == 1000) {
						List<UploadImageResponce> responce = UploadImageResponce.getclazz(result.getData());
						if (responce != null && responce.size() > 0) {
							UploadImageResponce obj = responce.get(0);
							obj.setLoacpath(imgUrl);
							if (position == IMAGE_SELECT) {
								PhotoID.add(obj);
								addView(PhotoID, photo_uoload, position);
							} else if (position == STOREIMAGE_SELECT) {
								StoreID.add(obj);
								addView(StoreID, image_Storephoto, position);
							}

						}

					}
				}

				@Override
				public void onFailure(HttpException error, String msg) {
					ToastUtils.showLong(R.string.no_networks_found);
				}
			});
		}
	}

	/**
	 * 
	 * @Title: addView
	 * @Description: 添加发型图片
	 * @author: 欧阳
	 * @param images
	 * @return: void
	 */
	private void addView(final List<UploadImageResponce> images, final LinearLayout ll, int flag) {
		ll.removeAllViews();
		for (int i = 0; i < images.size(); i++) {
			final View imageview = LayoutInflater.from(activity).inflate(R.layout.add_image_layout, null);
			// 将制定的单位值转换为像素值
			int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5,
					activity.getResources().getDisplayMetrics());

			int width = (ScreenUtils.getScreenWidth(activity) - padding * 4) / 3;
			// imageview.setPadding(padding, padding, padding, padding);
			imageview.setLayoutParams(new LayoutParams(width, width));

			ImageView photo = (ImageView) imageview.findViewById(R.id.image_shop_photo);
			ImageView del = (ImageView) imageview.findViewById(R.id.image_shop_photo_del);

			ImageLoaderUtils.setErrImage(R.drawable.default_prc, R.drawable.default_prc, R.drawable.default_prc);
			ImageLoaderUtils.getInstance(activity).loadImage(images.get(i).Image, photo);

			del.setOnClickListener(new MyOnClickListener(i, flag));
			ll.addView(imageview);
		}

	}

	StringBuffer ImageIDsb;// 图片字符串评论
	StringBuffer StroreIDsb;// 图片字符串评论

	/**
	 * 
	 * @ClassName: MyOnClickListener
	 * @Description: 自定义的点击监听器
	 * @author: Johnny
	 * @date: 2015年11月14日 下午11:10:01
	 */
	class MyOnClickListener implements OnClickListener {

		private int index;
		private int flag;
		String loacpath;

		MyOnClickListener(int i, int flag) {
			index = i;
			this.flag = flag;
			this.loacpath = loacpath;

		}

		@Override
		public void onClick(View v) {
			if (flag == IMAGE_SELECT) {
				PhotoID.remove(index);
				addView(PhotoID, photo_uoload, flag);
			} else if (flag == STOREIMAGE_SELECT) {
				StoreID.remove(index);
				addView(StoreID, image_Storephoto, flag);
			}
		}

	}

	/**
	 * 
	 * @Title: SendNetRequest
	 * @Description: 提交评论内容
	 * @author: 欧阳
	 * @return: void
	 */
	private void SendNetRequest() {
		if (listItem.size() == 3 && StoreList.size() == 2) {
			SaveEvaluationRequest(hairdessertEvaluation);
		} else {
			if (communicationEvaluate.getRating() == 0.0) {
				ToastUtils.showShort("请对发型师的沟通打分");
				return;
			} else {
				SaveEvaluationRequest.Item item = new SaveEvaluationRequest.Item();
				item.setCode("js");
				item.setScore(communicationEvaluate.getRating() + "");
				listItem.add(item);
			}
			if (professionEvaluate.getRating() == 0.0) {
				ToastUtils.showShort("请对发型师的专业打分");
				return;
			} else {
				SaveEvaluationRequest.Item item = new SaveEvaluationRequest.Item();
				item.setCode("zy");
				item.setScore(professionEvaluate.getRating() + "");
				listItem.add(item);
			}
			if (ontimeEvaluate.getRating() == 0.0) {
				ToastUtils.showShort("请对发型师的守时打分");
				return;
			} else {
				SaveEvaluationRequest.Item item = new SaveEvaluationRequest.Item();
				item.setCode("sh");
				item.setScore(ontimeEvaluate.getRating() + "");
				listItem.add(item);
			}

			if (environmentEvaluate.getRating() == 0.0) {
				ToastUtils.showShort("请对美发店的环境打分");
				return;
			} else {
				SaveEvaluationRequest.Item item = new SaveEvaluationRequest.Item();
				item.setCode("hj");
				item.setScore(environmentEvaluate.getRating() + "");
				StoreList.add(item);
			}
			if (trafficEvaluate.getRating() == 0.0) {
				ToastUtils.showShort("请对美发店的交通打分");
				return;
			} else {
				SaveEvaluationRequest.Item item = new SaveEvaluationRequest.Item();
				item.setCode("jt");
				item.setScore(trafficEvaluate.getRating() + "");
				StoreList.add(item);
			}
			ImageIDsb = new StringBuffer();
			if (PhotoID.size() > 0 && PhotoID != null) {
				for (int i = 0; i < PhotoID.size(); i++) {
					if (i == PhotoID.size() - 1) {
						ImageIDsb.append(PhotoID.get(i).ID);
					} else {
						ImageIDsb.append(PhotoID.get(i).ID).append("|");
					}
				}
			} else {
				ImageIDsb.append("");
			}
			StroreIDsb = new StringBuffer();
			if (StoreID.size() > 0 && StoreID != null) {
				for (int i = 0; i < StoreID.size(); i++) {
					if (i == StoreID.size() - 1) {
						StroreIDsb.append(StoreID.get(i).ID);
					} else {
						StroreIDsb.append(StoreID.get(i).ID).append("|");
					}
				}
			} else {
				StroreIDsb.append("");
			}
			LoadingDailog.show(activity, "正在发布评论....");
			SaveEvaluationRequest(hairdessertEvaluation);
		}
	}

	List<SaveEvaluationResponse> templList;// 评论ID

	/**
	 * 
	 * @Title: SaveEvaluationRequest
	 * @Description: 保存评论接口
	 * @author: 欧阳
	 * @return: void
	 */
	private void SaveEvaluationRequest(String hairdessertEvaluation) {
		if (!NetWorkUtils.isConnectInternet(getActivity())) {
			ToastUtils.showShort("网络未链接，请检查网络设置");
			LoadingDailog.dismiss();
			return;
		}
		SaveEvaluationRequest request = new SaveEvaluationRequest(UserManager.getUserID() + "", OrderID, "1",
				hairdessertEvaluation, listItem, ImageIDsb.toString());
		SaveEvaluationApi.SaveEvaluation(request, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				BaseResponse response = BaseResponse.getBaseResponse(responseInfo.result);
				if (response == null) {
					return;
				}
				String[] split = response.getMsg().split("\\|");
				if ("1".equals(split[0])) {
					ToastUtils.showShort(split[1] + "");
				}
				if (response.getCode() == 1000) {
					templList = SaveEvaluationResponse.getclazz2(response.getData());
					OrderDetailFragment.EvalID = templList.get(0).getEvalID();
					SaveStroeEvaluation();
				}
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
	 * @Title: SaveStroeEvaluation
	 * @Description: 保存店铺信息
	 * @author: 欧阳
	 * @return: void
	 */
	protected void SaveStroeEvaluation() {
		if (!NetWorkUtils.isConnectInternet(getActivity())) {
			ToastUtils.showShort("网络未链接，请检查网络设置");
			LoadingDailog.dismiss();
			return;
		}
		SaveEvaluationRequest request = new SaveEvaluationRequest(UserManager.getUserID() + "", OrderID, "2",
				Store_Evaluation, StoreList, StroreIDsb.toString());
		SaveEvaluationApi.SaveEvaluation(request, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				BaseResponse response = BaseResponse.getBaseResponse(responseInfo.result);
				if (response == null) {
					return;
				}
				String[] split = response.getMsg().split("\\|");
				if ("1".equals(split[0])) {
					ToastUtils.showShort(split[1] + "");
				}
				if (response.getCode() == 1000) {
					SharatoFragment(templList.get(0).getEvalID());
					LoadingDailog.dismiss();
				}
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
	 * @Title: SharatoFragment
	 * @Description: 将图片传过去
	 * @author: 欧阳
	 * @param evalID
	 * @return: void
	 */
	protected void SharatoFragment(String evalID) {
		Fragment fragment = FragmentManagerUtils.getFragment(activity, OrderShareFragment.class.getName());
		Bundle bundle = new Bundle();
		if (PhotoID.size() > 0) {
			bundle.putSerializable("PhotoID", (Serializable) PhotoID);
		}
		if (StoreID.size() > 0) {
			bundle.putSerializable("StoreID", (Serializable) StoreID);
		}
		bundle.putString("EvalID", evalID);
		fragment.setArguments(bundle);
		EventBus.getDefault().post(new MyEventBus("请刷新订单状态"));
		FragmentManagerUtils.add(activity, R.id.content_frame, fragment, false);
	}

	public int getScreenWidth() {
		DisplayMetrics metric = new DisplayMetrics();
		WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels;
		return width;
	}

	@Override
	public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
		switch (ratingBar.getId()) {
		case R.id.ratingbar_communication_evaluate:// 交流评论
			communicationEvaluate.setRating(rating);
			break;
		case R.id.ratingbar_profession_evaluate:// 专业评论
			professionEvaluate.setRating(rating);
			break;
		case R.id.ratingbar_ontime_evaluate:// 准时评论
			ontimeEvaluate.setRating(rating);
			break;
		case R.id.ratingbar_environment_evaluate:// 环境评论
			environmentEvaluate.setRating(rating);
			break;
		case R.id.ratingbar_traffic_evaluate:// 交通评论
			trafficEvaluate.setRating(rating);
			break;
		default:
			break;
		}
	}

	/**
	 * 质量压缩
	 * 
	 * @author ping 2015-1-5 下午1:29:58
	 * @param image
	 * @param maxkb
	 * @return
	 */
	public static Bitmap compressBitmap(Bitmap image, int maxkb) {

		// L.showlog(压缩图片);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		// Log.i(test,原始大小 + baos.toByteArray().length);
		while (baos.toByteArray().length / 1024 > 5120) { // 循环判断如果压缩后图片是否大于(maxkb)50kb,大于继续压缩
			// Log.i(test,压缩一次!);
			baos.reset();// 重置baos即清空baos
			options -= 10;// 每次都减少10
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
		}
		// Log.i(test,压缩后大小 + baos.toByteArray().length);
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	static boolean saveBitmap2file(Bitmap bmp, String filename) {
		CompressFormat format = Bitmap.CompressFormat.JPEG;
		int quality = 100;
		OutputStream stream = null;
		try {
			stream = new FileOutputStream("/sdcard/" + filename);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bmp.compress(format, quality, stream);
	}

	public static Bitmap getBitmap(String path) throws IOException {

		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// conn.setConnectTimeout(5000);
		// conn.setRequestMethod("GET");
		// if (conn.getResponseCode() == 200) {
		InputStream inputStream = conn.getInputStream();
		Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
		return bitmap;
		// }
		// return null;
	}

	/**
	 * 处理图片
	 * 
	 * @param bm
	 *            所要转换的bitmap
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
