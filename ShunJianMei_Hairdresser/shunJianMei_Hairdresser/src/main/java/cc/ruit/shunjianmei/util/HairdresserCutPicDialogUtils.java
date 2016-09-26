package cc.ruit.shunjianmei.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

import com.lidroid.xutils.util.LogUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import cc.ruit.shunjianmei.home.me.MyInformationFragment;

/**
 * @ClassName: CutPicDialogUtils
 * @Description: 图片选择裁剪封装
 * @date: 2015年11月12日 下午7:05:25
 */
public class HairdresserCutPicDialogUtils {

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
	public HairdresserCutPicDialogUtils(Activity activity, Fragment fragment) {
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
		openPhotoZoom();
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
		if (requestCode == PHOTOZOOM) {// 读取相册缩放图片
			if (data == null)
				return;
			if (MyInformationFragment.postion == "hairdressPhoto") {
				startUserPhotoZoom(data.getData());

			}else if(MyInformationFragment.postion == "picID1"){
				startFirstPhotoZoom(data.getData());
			}else{

				startPhotoZoom(data.getData());
			}
		} else if (requestCode == PHOTORESOULT) {// 处理结果
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
	 * 对第一张形象照片的裁剪
	 * 
	 * @param uri
	 */
	private void startFirstPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 16);
		intent.putExtra("aspectY", 7);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 275);
		intent.putExtra("outputY", 120);
		// 将数据保留在Bitmap中返回
		intent.putExtra("return-data", true);
		if (fragment != null) {
			fragment.startActivityForResult(intent, PHOTORESOULT);
		} else {
			activity.startActivityForResult(intent, PHOTORESOULT);
		}
	}



	/**
	 * 对头像的裁剪
	 * 
	 * @param uri
	 */

	private void startUserPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", ScreenUtils.dip2px(activity, 128));
		intent.putExtra("outputY", ScreenUtils.dip2px(activity, 128));
		intent.putExtra("return-data", true);
		if (fragment != null) {
			fragment.startActivityForResult(intent, PHOTORESOULT);
		} else {
			activity.startActivityForResult(intent, PHOTORESOULT);
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
		intent.putExtra("aspectX", 4);
		intent.putExtra("aspectY", 3);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 400);
		intent.putExtra("outputY", 300);
		// 保留比例
		intent.putExtra("scale", true);
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
	public void openPhotoZoom() {
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

	// /**
	// * @Title: openCamera
	// * @Description: 调用系统的拍照功能
	// * @author: lee
	// * @return: void
	// */
	// private void openCamera() {
	// // 调用系统的拍照功能
	// ImageName = getFileName();
	// Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	// intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new
	// File(CACHE_IMAGE_PATH, ImageName)));
	// if (fragment != null) {
	// fragment.startActivityForResult(intent, PHOTOHRAPH);
	// } else {
	// activity.startActivityForResult(intent, PHOTOHRAPH);
	// }
	// }

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
