package cc.ruit.utils.sdk.ouys;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cc.ruit.utils.R;

import com.lidroid.xutils.util.LogUtils;

/**
 * 
 * @ClassName: PicDialogUtils
 * @Description:选择图片上传不需要裁剪
 * @author: 欧阳
 * @date: 2015年12月9日 上午10:29:21
 */
public class PicDialogUtils {
	public final static String CACHE = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/.chache/cache";
	public static final int REQUEST_PHOTO = new Random().nextInt();
	public static final int NONE = 0;
	public static final int PHOTOHRAPH = 1;// 拍照
	public static final int PHOTOZOOM = 2; // 缩放
	public static final String IMAGE_UNSPECIFIED = "image/*";
	public static final String EXTRA_PHOTO_PATH = "PHOTO_PATH";
	public static final String EXTRA_PHOTO = "PHOTO";
	public static String CACHE_IMAGE_PATH = CACHE + "/image";
	private String ImageName;
	Activity activity;
	Fragment fragment;

	/**
	 * @Title:CutPicDialogUtils
	 * @Description:如果是在fragment中使用，传入fragment的对象，如果不是在fragment中 fragment传null
	 * @param activity
	 * @param fragment
	 */
	public PicDialogUtils(Activity activity, Fragment fragment) {
		super();
		this.activity = activity;
		this.fragment = fragment;
	}

	/**
	 * @Title: shwoChooseDialog
	 * @Description: 显示选择框
	 * @author: lee
	 * @return: void
	 */
	public void shwoChooseDialog() {
		View view = activity.getLayoutInflater().inflate(
				R.layout.photo_choose_dialog, null);
		Button btn_tulu = (Button) view.findViewById(R.id.tuku);
		Button btn_paizhao = (Button) view.findViewById(R.id.paizhao);
		Button btn_quxiao = (Button) view.findViewById(R.id.quxiao);

		final Dialog dialog = new Dialog(activity,
				R.style.transparentFrameWindowStyle);
		dialog.setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
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
				openPhotoZoom();
				dialog.dismiss();
			}
		});
		// 拍照
		btn_paizhao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openCamera();
				dialog.dismiss();
			}
		});
	}

	/**
	 * @Title: openPhotoZoom
	 * @Description: 调用系统的相册
	 * @author: lee
	 * @return: void
	 */
	private void openPhotoZoom() {
		// 调用系统的相册
		ImageName = getFileName();
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				IMAGE_UNSPECIFIED);
		if (fragment != null) {
			fragment.startActivityForResult(intent, PHOTOZOOM);
		} else {
			activity.startActivityForResult(intent, PHOTOZOOM);
		}
	}

	/**
	 * @Title: getFileName
	 * @Description: 获取文件名，唯一值
	 * @author: lee
	 * @return
	 * @return: String
	 */
	private static String getFileName() {
		String uuid = UUID.randomUUID().toString();
		return "/" + uuid + ".png";
	}

	/**
	 * @Title: openCamera
	 * @Description: 调用系统的拍照功能
	 * @author: lee
	 * @return: void
	 */
	private void openCamera() {
		// 调用系统的拍照功能
		ImageName = getFileName();
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(CACHE_IMAGE_PATH, ImageName)));
		if (fragment != null) {
			fragment.startActivityForResult(intent, PHOTOHRAPH);
		} else {
			activity.startActivityForResult(intent, PHOTOHRAPH);
		}
	}

	/**
	 * @Title: onActivityResult
	 * @Description: 调用startActivityResult，返回之后的回调函数，
	 *               在fragment或activity页面的onActivityResult方法中调用次方法
	 * @author: lee
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 * @return: void
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == NONE)
			return;
		if (requestCode == PHOTOHRAPH) {// 拍照
			// 设置文件保存路径这里放在跟目录下
			File picture = new File(CACHE_IMAGE_PATH + ImageName);
			if (!picture.exists()) {
				return;
			} else {
				finish(Activity.RESULT_OK, Uri.fromFile(picture).toString());
				// Bitmap bitmap=null;
				// try {
				// bitmap = MediaStore.Images.Media.getBitmap(
				// activity.getContentResolver(),
				// Uri.fromFile(new File(CACHE_IMAGE_PATH, ImageName)));
				// String path = saveBitmap(getFileName(), bitmap);
				//
				// } catch (Exception e) {
				// e.printStackTrace();
				// }
			}
		} else if (requestCode == PHOTOZOOM) {// 读取相册缩放图片
			if (data == null)
				return;
			Bundle extras = data.getExtras();
			if (extras != null) {
				Bitmap photo = extras.getParcelable("data");
				String path = saveBitmap(getFileName(), photo);
				finish(Activity.RESULT_OK, path);
			}
		}
	}

	/**
	 * @Title: finish
	 * @Description: 不裁剪
	 * @author: lee
	 * @param result
	 * @param photo
	 * @return: void
	 */
	private void finish(int result, String photo) {
		// Intent intent = new Intent();
		// intent.putExtra(EXTRA_PHOTO_PATH, CACHE_IMAGE_PATH + ImageName);
		// intent.putExtra(EXTRA_PHOTO, photo);
		if (this.listener != null) {
			listener.onChoosPicResult(result, photo);
		}
	}

	private OnChoosPicResultListener listener;

	/**
	 * @ClassName: OnChoosPicResultListener
	 * @Description: 结果处理监听
	 * @author: lee
	 * @date: 2015年11月12日 下午7:12:44
	 */
	public interface OnChoosPicResultListener {
		public void onChoosPicResult(int result, String photo);
	}

	/**
	 * @Title: setOnChoosPicResultListener
	 * @Description: 设置监听
	 * @author: lee
	 * @param listener
	 * @return: void
	 */
	public void setOnChoosPicResultListener(OnChoosPicResultListener listener) {
		this.listener = listener;
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
	private String saveBitmap(String bitName, Bitmap mBitmap) {
		File fileDir = new File(CACHE_IMAGE_PATH);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		} else {
			fileDir = null;
		}
		File f = new File(CACHE_IMAGE_PATH + bitName);
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
	 * @Title: addView
	 * @Description: 动态添加图片管理
	 * @author: 欧阳
	 * @param images
	 * @param photo_uoload
	 * @return: void
	 */
	private void addView(final List<Object> images, final LinearLayout ll,
			int flag) {
		ll.removeAllViews();
		for (int i = 0; i < images.size(); i++) {
			final View imageview = LayoutInflater.from(activity).inflate(
					R.layout.add_image_layout, null);
			// 将制定的单位值转换为像素值
			int padding = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 5, activity.getResources()
							.getDisplayMetrics());

			int width = (getScreenWidth(activity) - padding * 4) / 3;
			// imageview.setPadding(padding, padding, padding, padding);
			imageview.setLayoutParams(new LayoutParams(width, width));

			ImageView photo = (ImageView) imageview
					.findViewById(R.id.image_shop_photo);
			ImageView del = (ImageView) imageview
					.findViewById(R.id.image_shop_photo_del);
			//加载图片控件
			// ImageLoaderUtils.setErrImage(R.drawable.default_prc,
			// R.drawable.default_prc, R.drawable.default_prc);
			// ImageLoaderUtils.getInstance(activity).loadImage(
			// images.get(i).Image, photo);

			del.setOnClickListener(new MyOnClickListener(i, flag));
			ll.addView(imageview);
		}
	}

	/**
	 * @Title: getScreenWidth
	 * @Description: TODO
	 * @author: Johnny
	 * @param context
	 * @return
	 * @return: int
	 */
	public static int getScreenWidth(Context context) {
		DisplayMetrics metric = new DisplayMetrics();
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels;
		return width;
	}

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
//			if (flag == IMAGE_SELECT) {
//				PhotoID.remove(index);
//				addView(PhotoID, photo_uoload, flag);
//			} else if (flag == STOREIMAGE_SELECT) {
//				StoreID.remove(index);
//				addView(StoreID, image_Storephoto, flag);
//			}
		}

	}
}
