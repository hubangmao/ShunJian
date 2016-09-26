package cc.ruit.shunjianmei.util.view;

import android.app.Activity;
import android.app.Dialog;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import cc.ruit.shunjianmei.R;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.lidroid.xutils.util.LogUtils;
/**
 * @ClassName: ShareDialog
 * @Description: 分享Dialog；
 * @author: lee
 * @date: 2015年9月9日 上午11:44:26
 */
public class ShareDialog implements OnClickListener{
	private OnItemClickListener onItemClickListener;
	Dialog dialog ;
	private Activity activity;
	
	public ShareDialog(Activity activity) {
		super();
		this.activity = activity;
	}
	/**
	 * @Title: show
	 * @Description: show分享选择框
	 * @author: lee
	 * @param activity 弹出以来的activity
	 * @param onItemClickListener 内容点击回调
	 * @return: void
	 */
//	public void show(OnItemClickListener onItemClickListener) {
//		this.onItemClickListener = onItemClickListener;
//		View view = LayoutInflater.from(activity).inflate(R.layout.share_dialog_layout,
//				null);
////		LinearLayout ll_wechat = (LinearLayout) view.findViewById(R.id.ll_share_wechat);
////		LinearLayout ll_moments = (LinearLayout) view.findViewById(R.id.ll_share_wechatmoments);
////		LinearLayout ll_qq = (LinearLayout) view.findViewById(R.id.ll_share_qq);
////		LinearLayout ll_qzone = (LinearLayout) view.findViewById(R.id.ll_share_qzone);
////		LinearLayout ll_sina = (LinearLayout) view.findViewById(R.id.ll_share_sina);
////		view.findViewById(R.id.ll_share_wechat).setOnClickListener(this);
////		view.findViewById(R.id.ll_share_wechatmoments).setOnClickListener(this);
////		view.findViewById(R.id.ll_share_qq).setOnClickListener(this);
////		view.findViewById(R.id.ll_share_qzone).setOnClickListener(this);
////		view.findViewById(R.id.ll_share_sina).setOnClickListener(this);
////		view.findViewById(R.id.btn_cancel).setOnClickListener(this);
//		dialog = new Dialog(activity,
//				R.style.transparentFrameWindowStyle);
//		dialog.setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT,
//				LayoutParams.WRAP_CONTENT));
//		Window window = dialog.getWindow();
//		// 设置显示动画
//		window.setWindowAnimations(R.style.main_menu_animstyle);
//		WindowManager.LayoutParams wl = window.getAttributes();
//		wl.x = 0;
//		wl.y = activity.getWindowManager().getDefaultDisplay().getHeight();
//		// 以下这两句是为了保证按钮可以水平满屏
//		wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
//		wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//
//		// 设置显示位置
//		dialog.onWindowAttributesChanged(wl);
//		// 设置点击外围解散
//		dialog.setCanceledOnTouchOutside(true);
//		dialog.show();
//	}
	/**
	 * @ClassName: OnItemClickListener
	 * @Description: shareDialog子项点击监听
	 * @author: lee
	 * @date: 2015年9月9日 上午11:43:49
	 */
	public interface OnItemClickListener{
		public void onItemClickListener(int postion,View view);
	}
	@Override
	public void onClick(View v) {
		if (dialog!=null&&dialog.isShowing()) {
			dialog.dismiss();
		}
		int postion = 0;
//		switch (v.getId()) {
//		case R.id.ll_share_wechat:
//			postion = 0;
//			break;
//		case R.id.ll_share_wechatmoments:
//			postion = 1;
//			break;
//		case R.id.ll_share_qq:
//			postion = 2;
//			break;
//		case R.id.ll_share_qzone:
//			postion = 3;
//			break;
//		case R.id.ll_share_sina:
//			postion = 4;
//			break;
//		case R.id.btn_cancel:
//			return;
//		default:
//			break;
//		}
		if (onItemClickListener!=null) {
			onItemClickListener.onItemClickListener(postion, v);
		}
	}
	/**
	 * @Title: showSina
	 * @Description: 新浪分享
	 * @author: lee
	 * @param text
	 * @param hostImagePath
	 * @param paListener
	 * @return: void
	 */
	public void showSina(String text,String imageUrl,PlatformActionListener paListener){
		ShareSDK.initSDK(activity);
		ShareParams sp = new ShareParams();
		if (TextUtils.isEmpty(text)&&TextUtils.isEmpty(imageUrl)) {
			LogUtils.e("showSina text and hostImagePath is null");
			return;
		}
		sp.setText(text);
		sp.setImageUrl(imageUrl);
		Platform platform = ShareSDK.getPlatform(SinaWeibo.NAME);
		platform.setPlatformActionListener(paListener); // 设置分享事件回调
		// 执行图文分享
		platform.share(sp);
	}
	/**
	 * @Title: showWechat
	 * @Description: 微信
	 * @author: lee
	 * @param title
	 * @param content
	 * @param hostImagePath
	 * @param url
	 * @param paListener
	 * @return: void
	 */
	public void shareWechat(String title,String content,String hostImagePath,String url,PlatformActionListener paListener){
		ShareSDK.initSDK(activity);
		ShareParams sp = new ShareParams();
		sp.setShareType(Platform.SHARE_WEBPAGE);
		sp.setText(content);
		sp.setTitle(title);
		sp.setImagePath(hostImagePath);
		sp.setUrl(url);
		Platform platform = ShareSDK.getPlatform (Wechat.NAME);
		platform.setPlatformActionListener (paListener); // 设置分享事件回调
		// 执行图文分享
		platform.SSOSetting(true);
		platform.share(sp);
	}
	/**
	 * @Title: showWechat
	 * @Description: 微信朋友圈
	 * @author: lee
	 * @param title
	 * @param content
	 * @param hostImagePath
	 * @param url
	 * @param paListener
	 * @return: void
	 */
	public void shareWechatMoments(String title,String content,String hostImagePath,String url,PlatformActionListener paListener){
		ShareSDK.initSDK(activity);
		ShareParams sp = new ShareParams();
		sp.setShareType(Platform.SHARE_WEBPAGE);
		sp.setText(content);
		sp.setTitle(title);
		sp.setImagePath(hostImagePath);
		sp.setUrl(url);
		Platform platform = ShareSDK.getPlatform (WechatMoments.NAME);
		platform.setPlatformActionListener (paListener); // 设置分享事件回调
		// 执行图文分享
		platform.SSOSetting(true);
		platform.share(sp);
	}
	/**
	 * @Title: shareQzone
	 * @Description: 空间分享
	 * @author: lee
	 * @param title
	 * @param content
	 * @param imageUrl
	 * @param url
	 * @param paListener
	 * @return: void
	 */
	public void shareQzone(String title,String content,String comment,String imageUrl,String url,PlatformActionListener paListener){
		ShareSDK.initSDK(activity);
		ShareParams sp = new ShareParams();
		sp.setShareType(Platform.SHARE_WEBPAGE);
		sp.setTitle(title);
		sp.setTitleUrl(url);
		sp.setText(content);
		sp.setComment(comment);
		sp.setImageUrl(imageUrl);
		sp.setSite(activity.getResources().getString(R.string.app_name));
		sp.setSiteUrl(url);
		sp.setUrl(url);
		Platform platform = ShareSDK.getPlatform (QZone.NAME);
		platform.setPlatformActionListener (paListener); // 设置分享事件回调
		// 执行图文分享
		platform.SSOSetting(true);
		platform.share(sp);
	}
	public void shareQQ(String title,String content,String imageUrl,String url,PlatformActionListener paListener){
		ShareSDK.initSDK(activity);
		ShareParams sp = new ShareParams();
		sp.setShareType(Platform.SHARE_WEBPAGE);
		sp.setTitle(title);
		sp.setTitleUrl(url);
		sp.setText(content);
		sp.setImageUrl(imageUrl);
		sp.setUrl(url);
		Platform platform = ShareSDK.getPlatform (QQ.NAME);
		platform.setPlatformActionListener (paListener); // 设置分享事件回调
		// 执行图文分享
		platform.SSOSetting(true);
		platform.share(sp);
	}
}
