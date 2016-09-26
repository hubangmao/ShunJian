package cc.ruit.shunjianmei.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import cc.ruit.shunjianmei.R;

import com.lidroid.xutils.util.LogUtils;
/**
 * @ClassName: CutPicDialogUtils
 * @Description: 头像选择裁剪封装
 * @author: lee
 * @date: 2015年11月12日 下午7:05:25
 */
public class CutPicDialogUtils {

	public final static String CACHE = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.chache/cache";
	public static final int REQUEST_PHOTO = new Random().nextInt();
	public static final int NONE = 0;
	public static final int PHOTOHRAPH = 1;// 拍照
	public static final int PHOTOZOOM = 2; // 缩放
	public static final int PHOTORESOULT = 3;// 结果
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
	public CutPicDialogUtils(Activity activity, Fragment fragment) {
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
	 * @Title: finish
	 * @Description: 裁剪结束的后调用
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
			startPhotoZoom(Uri.fromFile(picture));
		} else if (requestCode == PHOTOZOOM) {// 读取相册缩放图片
			if (data == null)
				return;
			startPhotoZoom(data.getData());
		} else if (requestCode == PHOTORESOULT) {// 处理结果
			if (data == null)
				return;
			Bundle extras = data.getExtras();
			if (extras != null) {
				Bitmap photo = extras.getParcelable("data");
				// this.onCuiPicOverLinstener.onCuiPicOver(photo);
				String path = saveBitmap(getFileName(), photo);
				finish(Activity.RESULT_OK, path);
			}

		}
	}

	/**
	 * @Title: startPhotoZoom
	 * @Description: 裁剪
	 * @author: lee
	 * @param uri
	 * @return: void
	 */
	private void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 128);
		intent.putExtra("outputY", 128);
		intent.putExtra("return-data", true);
		if (fragment != null) {
			fragment.startActivityForResult(intent, PHOTORESOULT);
		} else {
			activity.startActivityForResult(intent, PHOTORESOULT);
		}
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
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
		if (fragment != null) {
			fragment.startActivityForResult(intent, PHOTOZOOM);
		} else {
			activity.startActivityForResult(intent, PHOTOZOOM);
		}
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
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(CACHE_IMAGE_PATH, ImageName)));
		if (fragment != null) {
			fragment.startActivityForResult(intent, PHOTOHRAPH);
		} else {
			activity.startActivityForResult(intent, PHOTOHRAPH);
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
			mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
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
}
