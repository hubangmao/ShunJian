package cc.ruit.shunjianmei.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import cc.ruit.shunjianmei.R;
import cc.ruit.shunjianmei.base.BaseActivity;
import cc.ruit.shunjianmei.util.view.MyImageView;
import cc.ruit.shunjianmei.util.view.PicGallery;
import cc.ruit.utils.sdk.file.FileConstant;

/**
 * 图片浏览
 * 
 * @author Administrator
 *
 */
public class ForLargeImageActivity extends BaseActivity implements
		OnClickListener {

	private PicGallery gallery;
	private GalleryAdapter2 mAdapter;
	private TextView tv_index;
	private int index;
	private List<String> imageList;

	private ProgressDialog mSaveDialog = null;
	private Button mBtnSave;
	private ImageButton ib_delPhoto;

	private Bitmap mBitmap;
	private String mFileName;
	private String mSaveMessage;
//	private String type;
//	private boolean isNativeUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_show_pic_main);
		Intent intent = getIntent();
		index = intent.getIntExtra("index", 0);
//		isNativeUser = intent.getBooleanExtra("isNativUser", false);
//		type = intent.getStringExtra("type");
		imageList = intent.getStringArrayListExtra("infoList");
		gallery = (PicGallery) findViewById(R.id.pic_gallery);
		tv_index = (TextView) findViewById(R.id.tv_index);
		ib_delPhoto = (ImageButton) findViewById(R.id.btn_del);
		ib_delPhoto.setOnClickListener(this);
//		if (isNativeUser) {
//			ib_delPhoto.setVisibility(View.VISIBLE);
//		} else {
//			ib_delPhoto.setVisibility(View.INVISIBLE);
//		}
		gallery.setVerticalFadingEdgeEnabled(false);// 取消竖直渐变边框
		gallery.setHorizontalFadingEdgeEnabled(false);// 取消水平渐变边框
		gallery.setDetector(new GestureDetector(this, new MySimpleGesture()));
		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				index = arg2;
				tv_index.setText(arg2 + 1 + " / " + imageList.size()); // 设置显示图片数量及当前图片索引
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		mAdapter = new GalleryAdapter2(this, imageList);
		gallery.setAdapter(mAdapter);

		if (index > 0) {
			gallery.setSelection(index);
		}

		findViewById(R.id.btn_download).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						mSaveDialog = ProgressDialog.show(
								ForLargeImageActivity.this, "保存图片",
								"图片正在保存中，请稍等...", true);
						new Thread(connectNet).start();
					}
				});
	}

	private class MySimpleGesture extends SimpleOnGestureListener {
		// 按两下的第二下Touch down时触发
		public boolean onDoubleTap(MotionEvent e) {

			View view = gallery.getSelectedView();
			if (view instanceof MyImageView) {
				MyImageView imageView = (MyImageView) view;
				if (imageView.getScale() > imageView.getMiniZoom()) {
					imageView.zoomTo(imageView.getMiniZoom());
				} else {
					imageView.zoomTo(imageView.getMaxZoom());
				}

			} else {

			}
			return true;
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			// Logger.LOG("onSingleTapConfirmed",
			// "onSingleTapConfirmed excute");
			// mTweetShow = !mTweetShow;
			// tweetLayout.setVisibility(mTweetShow ? View.VISIBLE
			// : View.INVISIBLE);
			return true;
		}
	}

	/**
	 * Get image from newwork
	 * 
	 * @param path
	 *            The path of image
	 * @return byte[]
	 * @throws Exception
	 */
	public byte[] getImage(String path) throws Exception {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5 * 1000);
		conn.setRequestMethod("POST");
		InputStream inStream = conn.getInputStream();
		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			return readStream(inStream);
		}
		return null;
	}

	/**
	 * Get image from newwork
	 * 
	 * @param path
	 *            The path of image
	 * @return InputStream
	 * @throws Exception
	 */
	public InputStream getImageStream(String path) throws Exception {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5 * 1000);
		conn.setRequestMethod("GET");
		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			return conn.getInputStream();
		}
		return null;
	}

	/**
	 * Get data from stream
	 * 
	 * @param inStream
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] readStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		inStream.close();
		return outStream.toByteArray();
	}

	/**
	 * 保存文件
	 * 
	 * @param bm
	 * @param fileName
	 * @throws IOException
	 */
	public void saveFile(Bitmap bm, String fileName) throws IOException {
		File dirFile = new File(FileConstant.IMG_PATH);
		if (!dirFile.exists()) {
			dirFile.mkdir();
		}
		File myCaptureFile = new File(FileConstant.IMG_PATH + "/"
				+ System.currentTimeMillis() + ".jpg");
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(myCaptureFile));
		bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
		bos.flush();
		bos.close();
	}

	private Runnable saveFileRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				saveFile(mBitmap, mFileName);
				mSaveMessage = "图片保存成功！";
			} catch (IOException e) {
				mSaveMessage = "图片保存失败！";
				e.printStackTrace();
			}
			messageHandler.sendMessage(messageHandler.obtainMessage());
		}

	};

	private Handler messageHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mSaveDialog.dismiss();
			Toast.makeText(ForLargeImageActivity.this, mSaveMessage,
					Toast.LENGTH_SHORT).show();
		}
	};

	/*
	 * 连接网络 由于在4.0中不允许在主线程中访问网络，所以需要在子线程中访问
	 */
	private Runnable connectNet = new Runnable() {
		@Override
		public void run() {
			try {
				String filePath = imageList.get(index);
				mFileName = imageList.get(index);

				// 以下是取得图片的两种方法
				// ////////////// 方法1：取得的是byte数组, 从byte数组生成bitmap
				byte[] data = getImage(filePath);
				if (data != null) {
					mBitmap = BitmapFactory.decodeByteArray(data, 0,
							data.length);// bitmap
				} else {
					Toast.makeText(ForLargeImageActivity.this, "Image error!",
							1).show();
				}
				// //////////////////////////////////////////////////////

				// ******** 方法2：取得的是InputStream，直接从InputStream生成bitmap
				// ***********/
				// mBitmap =
				// BitmapFactory.decodeStream(getImageStream(filePath));
				// ********************************************************************/
				new Thread(saveFileRunnable).start();
				// 发送消息，通知handler在主线程中更新UI
			} catch (Exception e) {
				Toast.makeText(ForLargeImageActivity.this, "无法链接网络！", 1).show();
				e.printStackTrace();
			}

		}

	};

	/**
	 * 获取Intent
	 * 
	 * @param context
	 * @param index
	 * @param isNativeUser
	 * @param list
	 * @return
	 */
	public static Intent getIntent(Context context, int index, ArrayList<String> list) {
		Intent intent = new Intent(context, ForLargeImageActivity.class);
		intent.putExtra("index", index);
//		intent.putExtra("isNativUser", isNativeUser);
//		intent.putExtra("type", type);
		intent.putStringArrayListExtra("infoList", list);
		return intent;
	}

	@Override
	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.btn_del:
//			delPhoto(imageList.get(index));
//			break;
//
//		default:
//			break;
//		}
	}

//	private void delPhoto(String path) {
//		if (!NetworkUtils.isNetworkAvaliable(this)) {
//			MyToast.showToast(this, R.string.network_not_available_error);
//			return;
//		}
//		showProgressDialog();
//		DataService.getInstance().delPicture(path, type,
//				new RequestCallBack<String>() {
//
//					@Override
//					public void onFailure(HttpException arg0, String arg1) {
//						Toast.makeText(ForLargeImageActivity.this,
//								R.string.error_no_service, Toast.LENGTH_LONG)
//								.show();
//						dismissProgressDialog();
//					}
//
//					private void handleResult(ResponseInfoVo responseInfo) {
//						if (responseInfo == null) {
//							MyToast.showToast(ForLargeImageActivity.this,
//									"返回内容解析报错");
//							return;
//						}
//						int code = responseInfo.getCode();
//						String message = responseInfo.getMsg();
//						if (code == 200) {
//							MyToast.showToast(ForLargeImageActivity.this,
//									message);
//							if (imageList != null && imageList.size() > 1) {
//								imageList.remove(index);
//								tv_index.setText(index + 1 + " / " + imageList.size());
//								mAdapter.notifyDataSetChanged();
//							} else {
//								finish();
//							}
//							sendRefreshBroadcase();
//						} else {
//							Toast.makeText(ForLargeImageActivity.this, message,
//									Toast.LENGTH_LONG).show();
//						}
//
//					}
//
//					@Override
//					public void onSuccess(ResponseInfo<String> responseInfo) {
//						dismissProgressDialog();
//						handleResult(ResponseInfoVo.parseJson(Unicode
//								.decodeUnicode(responseInfo.result)));
//					}
//				});
//	}

//	private void sendRefreshBroadcase() {
//		Intent intent = new Intent(UserActivity.REFRESH_ACTION_WHEN_DEL);
//		ForLargeImageActivity.this.sendBroadcast(intent);
//	}
}
