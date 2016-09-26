package cc.ruit.utils.sdk.file;

import android.net.Uri;

/**
 * 文件相关常量
 * 
 * @author tianjm
 * 
 * 文件操作需要用到的一些常量
 *   需要在Application中进行初始化
 */
public class FileConstant {

	/**
	 * 首次登陆验证目录
	 */
	public static String RES_FIRST_DATA = "";

	/**
	 * app文件夹
	 */
	public static String APP_PATH = "";

	/**
	 * zip下载路径(大文件存储路径)
	 */
	public static String DOWNLOAD_PATH = "";

	/**
	 * 图片缓存路径(sd卡)
	 */
	public static String IMG_PATH = "";

	/**
	 * 图片缓存路径(data目录)
	 */
	public static String IMG_APP_PATH = "";

	/**
	 * 活动海报图片缓存路径(sd卡)
	 */
	public static String IMG_PATH_CAMPAIGN = "";

	/**
	 * 活动海报图片缓存路径(data目录)
	 */
	public static String IMG_APP_CAMPAIGN = "";

	/**
	 * 
	 */
	public static boolean sdCardIsExist;

	/**
	 * SD卡路径
	 */
	public static String SD_PATH;

	/**
	 * 客户端是否是首次启动
	 */
	public static boolean isFirstStartUp = true;

	/**
	 * 当前接入点uri
	 */
	public static final Uri PREFERRED_APN_URI = Uri
			.parse("content://telephony/carriers/preferapn");

	/**
	 * 录音缓存路径(sd卡)
	 */
	public static String AUDIO_PATH = "";

	/**
	 * 图片上传缓存路径(sd卡)
	 */
	public static String UPLOAD_PHOTO_PATH = "";
	
	/**
	 * 视频上传缓存路径(sd卡)
	 */
	public static String UPLOAD_VIDEO_PATH = "";
	
	/**
	 * 拍照
	 */
	public static final int PHOTOCAPTURE = 1;

	/**
	 * 相册
	 */
	public static final int PHOTOALBUM = 2;

	/**
	 * 缩放 结果
	 */
	public static final int PHOTORESOULT = 3;

	/**
	 * 视频录制
	 */
	public static final int VIDEORECORD = 4;
	
	/**
	 * 视频播放
	 */
	public static final int VIDEOSHOW = 5;
	
	/**
	 * @param first_tag_path
	 * @param app_path
	 * @param dwonload_path
	 * @param img_path
	 * @param img_app_path
	 * @param audio_path
	 * @param campaign_path
	 * @param campaign_app_path
	 */
	public FileConstant(String first_tag_path, String app_path,
			String dwonload_path, String img_path, String img_app_path,
			String audio_path, String loc_photo_path, String campaign_path,
			String campaign_app_path) {
		RES_FIRST_DATA = first_tag_path;
		APP_PATH = app_path;
		DOWNLOAD_PATH = dwonload_path;
		IMG_PATH = img_path;
		IMG_APP_PATH = img_app_path;
		AUDIO_PATH = audio_path;
		UPLOAD_PHOTO_PATH = loc_photo_path;
		IMG_PATH_CAMPAIGN = campaign_path;
		IMG_APP_CAMPAIGN = campaign_app_path;
	}

	/**
	 * @param first_tag_path
	 * @param app_path
	 * @param dwonload_path
	 * @param img_path
	 * @param img_app_path
	 * @param audio_path
	 * @param loc_photo_path
	 * @param loc_video_path
	 */
	public FileConstant(String first_tag_path, String app_path,
			String dwonload_path, String img_path, String img_app_path,
			String audio_path, String loc_photo_path,String loc_video_path) {
		RES_FIRST_DATA = first_tag_path;
		APP_PATH = app_path;
		DOWNLOAD_PATH = dwonload_path;
		IMG_PATH = img_path;
		IMG_APP_PATH = img_app_path;
		AUDIO_PATH = audio_path;
		UPLOAD_PHOTO_PATH = loc_photo_path;
		UPLOAD_VIDEO_PATH = loc_video_path;
	}
	
	/**
	 * @param sdCardIsExist
	 * @param sd_path
	 */
	public void setSD_data(boolean sdCardIsExist, String sd_path) {
		this.sdCardIsExist = sdCardIsExist;
		SD_PATH = sd_path;
	}
}
