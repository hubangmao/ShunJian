package cc.ruit.shunjianmei.util;

import java.io.File;

import android.content.Context;
import android.widget.ImageView;
import cc.ruit.shunjianmei.R;
import cc.ruit.utils.sdk.file.FileConstant;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

/**
 * 图片加载工具类
 * 
 * @author Msz
 * 
 */
public class ImageLoaderUtils {

	private static ImageLoaderUtils imageUtil;

	public static ImageLoader imageLoader;
	public static DisplayImageOptions options;
	private static ImageLoaderConfiguration config;
	
	public static int defaltImageResId=R.drawable.icon;
	public static int errImage=R.drawable.icon;
	public static int failImage=R.drawable.icon;

	private ImageLoaderUtils(Context ctx) {
		initImageLoader(ctx);
	};

	public static ImageLoaderUtils getInstance(Context ctx) {
//		if (imageUtil == null) {
			imageUtil = new ImageLoaderUtils(ctx);
//		}
		return imageUtil;
	}

	public static ImageLoader getImageLoader() {
		return imageLoader;
	}
	//R.drawable.default_prc
	public static ImageLoaderUtils setErrImage(int defaltImageResId,int errImage,int failImage){
		ImageLoaderUtils.defaltImageResId=defaltImageResId;
		ImageLoaderUtils.errImage=errImage;
		ImageLoaderUtils.failImage=failImage;
		
		return imageUtil;
	}
	/**
	 * 图片下载初始化
	 * 
	 * @author Msz
	 * @param context
	 */
	private void initImageLoader(Context context) {
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.showImageOnLoading(defaltImageResId)
				// 加载开始默认的图片
				.showImageForEmptyUri(errImage)
				// 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(failImage)
				// 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true)
				// 设置下载的图片是否缓存在内存中
				.cacheOnDisk(true)
				.considerExifParams(true)
				// 设置下载的图片是否缓存在SD卡中				
				.build();
		// File cacheDir = StorageUtils.getCacheDirectory(context);
		File cacheDir = StorageUtils.getOwnCacheDirectory(context,
				FileConstant.IMG_PATH);
		config = new ImageLoaderConfiguration.Builder(context)
				.threadPoolSize(3)
				.threadPriority(Thread.NORM_PRIORITY - 1)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// .discCacheExtraOptions(240, 400, CompressFormat.PNG, 40,
				// null)
				.diskCacheExtraOptions(1024, 1024, null)
				.memoryCacheExtraOptions(1024, 1024).diskCacheFileCount(100)
				.diskCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
				.defaultDisplayImageOptions(options).build();
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(config);
	}

	/**
	 * 
	 * @param uri
	 * @param imageView
	 * @param options
	 */
	public void loadImage(String uri, ImageView imageView) {
//		uri = StaticField.SITE + uri;
		imageLoader.displayImage(uri, imageView, options);
	}

	/**
	 * 
	 * @param uri
	 * @param imageView
	 * @param options
	 * @param listener
	 */
	public void loadImage(String uri, ImageView imageView,
			ImageLoadingListener listener) {
//		uri = StaticField.SITE + uri;
		imageLoader.displayImage(uri, imageView, options, listener);
	}

	public void displayImage(String uri, ImageView imageView,
			DisplayImageOptions options) {
//		uri = StaticField.SITE + uri;
		imageLoader.displayImage(uri, imageView, options);
	}
}