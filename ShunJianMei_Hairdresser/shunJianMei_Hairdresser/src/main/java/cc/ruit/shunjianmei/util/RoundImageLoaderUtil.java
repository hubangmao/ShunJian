package cc.ruit.shunjianmei.util;

import java.io.File;

import android.content.Context;
import android.widget.ImageView;
import cc.ruit.shunjianmeihairdresser.R;
import cc.ruit.utils.sdk.file.FileConstant;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class RoundImageLoaderUtil {
	private static RoundImageLoaderUtil imageUtil;

	public static ImageLoader imageLoader;
	public static DisplayImageOptions options;
	private static ImageLoaderConfiguration config;
	int cornerRadiusPixels;
	
	private static int defaltImageResId=R.drawable.icon;
	private static int errImage=R.drawable.icon;
	private static int failImage=R.drawable.icon;

	private RoundImageLoaderUtil(Context ctx, int cornerRadiusPixels) {
		this.cornerRadiusPixels = cornerRadiusPixels;
		initImageLoader(ctx);
	};

	public static RoundImageLoaderUtil getInstance(Context ctx,
			int cornerRadiusPixels) {
//		if (imageUtil == null) {
			imageUtil = new RoundImageLoaderUtil(ctx, cornerRadiusPixels);
//		}
		return imageUtil;
	}
	public static ImageLoader getImageLoader() {
		return imageLoader;
	}
	public static RoundImageLoaderUtil setErrImage(int defaltImageResId,int errImage,int failImage){
		RoundImageLoaderUtil.defaltImageResId=defaltImageResId;
		RoundImageLoaderUtil.errImage=errImage;
		RoundImageLoaderUtil.failImage=failImage;
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
				.displayer(new RoundedBitmapDisplayer(cornerRadiusPixels,0))
				.build();
		// File cacheDir = StorageUtils.getCacheDirectory(context);
		File cacheDir = StorageUtils.getOwnCacheDirectory(context,
				FileConstant.IMG_PATH);
		config = new ImageLoaderConfiguration.Builder(context)
				.threadPoolSize(3)
				.threadPriority(Thread.NORM_PRIORITY - 1)//设置当前线程的优先级
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// .discCacheExtraOptions(240, 400, CompressFormat.PNG, 40,
				// null)
				.diskCacheExtraOptions(480, 800, null)// 本地缓存的详细信息
				.memoryCacheExtraOptions(480, 800).diskCacheFileCount(100)//内存缓存文件的最大长宽
				.diskCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
				.defaultDisplayImageOptions(options).build();
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(config);
	}

	public void changeOptions() {
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.icon) // 加载开始默认的图片
				.showImageForEmptyUri(R.drawable.icon) // url爲空會显示该图片，自己放在drawable里面的
				.showImageOnFail(R.drawable.icon) // 加载图片出现问题，会显示该图片
				.cacheInMemory(true) // 缓存用
				.cacheOnDisk(true) // 缓存用
				.displayer(new RoundedBitmapDisplayer(10)) // 图片圆角显示，值为整数
				.build();
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

}