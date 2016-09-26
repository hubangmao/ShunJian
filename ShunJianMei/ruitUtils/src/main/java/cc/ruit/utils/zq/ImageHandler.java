package cc.ruit.utils.zq;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cc.ruit.utils.R;

/**
 * 
 * @ClassName: ImageHandler
 * @Description: ViewPager的轮播器
 * @author: Johnny
 * @date: 2015年12月9日 下午1:11:45
 */
public class ImageHandler extends Handler {

	/**
	 * 请求更新显示的View。
	 */
	public static final int MSG_UPDATE_IMAGE = 1;
	/**
	 * 请求暂停轮播。
	 */
	public static final int MSG_KEEP_SILENT = 2;
	/**
	 * 请求恢复轮播。
	 */
	public static final int MSG_BREAK_SILENT = 3;
	/**
	 * 记录最新的页号，当用户手动滑动时需要记录新页号，否则会使轮播的页面出错。 例如当前如果在第一页，本来准备播放的是第二页，而这时候用户滑动到了末页，
	 * 则应该播放的是第一页，如果继续按照原来的第二页播放，则逻辑上有问题。
	 */
	public static final int MSG_PAGE_CHANGED = 4;

	private int currentItem = 0;
	public static long MSG_DELAY = 3000;// 轮播间隔时间
	private ViewPager viewPager;
	private TextView tabStrip;
	private Context context;
	private LinearLayout linearLayout;
	private int viewSize;//ViewPager中的view数量

	public ImageHandler(ViewPager viewPager, long delayTime,int viewSize) {
		this.viewPager = viewPager;
		MSG_DELAY = delayTime;
		this.viewSize = viewSize;
	}
	
	public ImageHandler(ViewPager viewPager, long delayTime,int viewSize,TextView tv) {
		this.viewPager = viewPager;
		MSG_DELAY = delayTime;
		this.viewSize = viewSize;
		tabStrip = tv;
		setPagerTabStrip(currentItem);
	}
	
	public ImageHandler(ViewPager viewPager, long delayTime,int viewSize,LinearLayout ll,Context context) {
		this.viewPager = viewPager;
		MSG_DELAY = delayTime;
		this.viewSize = viewSize;
		linearLayout = ll;
		this.context = context;
		setPagerDots();
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);

		if (hasMessages(MSG_UPDATE_IMAGE)) {
			removeMessages(MSG_UPDATE_IMAGE);
		}

		switch (msg.what) {
		//请求更新显示的View
		case MSG_UPDATE_IMAGE:
			currentItem++;
			//如果ViewPager已显示最后一张，则将currentItem设为0
			if (currentItem >= viewSize) {
				currentItem = 0;
			}
			viewPager.setCurrentItem(currentItem);
			//更新view数量提示
			setPagerTabStrip(currentItem);
			//更新点
			setPagerDots();
			//准备下次播放
			sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
			break;
		//请求暂停轮播	
		case MSG_KEEP_SILENT:
			//只要不发送消息就暂停了
			break;
		//请求恢复轮播	
		case MSG_BREAK_SILENT:
			sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
			break;
		//页号已改变	
		case MSG_PAGE_CHANGED:
			//记录当前的页号，避免播放的时候页面显示不正确。
			currentItem = msg.arg1;
			//更新view数量提示
			setPagerTabStrip(currentItem);
			//更新点
			setPagerDots();
			break;
		default:
			break;
		}
	}

	/**
	 * 
	 * @Title: setPagerTabStrip
	 * @Description: 设置ViewPager下方的数量标签
	 * @author: Johnny
	 * @return: void
	 */
	private void setPagerTabStrip(int position) {
		if (tabStrip == null) {
			return;
		}
		tabStrip.setText((position + 1) + "/" + viewSize);
	}

	/**
	 * 
	 * @Title: setPagerDots
	 * @Description: 设置ViewPager的点
	 * @author: Johnny
	 * @return: void
	 */
	private void setPagerDots(){
		if(linearLayout==null){
			return;
		}
		linearLayout.removeAllViews();
		for (int i = 0; i < viewSize; i++) {
			ImageView imageView = new ImageView(context);
			if (i == viewPager.getCurrentItem()) {
				imageView
						.setImageResource(R.drawable.dot_light);
			} else {
				imageView
						.setImageResource(R.drawable.dot_grey);
			}
			imageView.setPadding(5, 5, 5, 5);
			linearLayout.addView(imageView);
		}
	}
	/**
	 * 
	 * @ClassName: MyOnPageChangeListener
	 * @Description: 自定义的ViewPager监听器
	 * @author: Johnny
	 * @date: 2015年12月9日 下午1:47:38
	 */
	public static class MyOnPageChangeListener implements OnPageChangeListener {

		private ImageHandler handler;

		public MyOnPageChangeListener(ImageHandler handler) {
			this.handler = handler;
		}

		// 覆写该方法实现轮播效果的暂停和恢复
		@Override
		public void onPageScrollStateChanged(int arg0) {

			switch (arg0) {

			case ViewPager.SCROLL_STATE_DRAGGING:
				handler.sendEmptyMessage(ImageHandler.MSG_KEEP_SILENT);
				break;

			case ViewPager.SCROLL_STATE_IDLE:
				handler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE,
						ImageHandler.MSG_DELAY);
				break;

			default:
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		// 配合Adapter的currentItem字段进行设置
		@Override
		public void onPageSelected(int arg0) {
			handler.sendMessage(Message.obtain(handler,
					ImageHandler.MSG_PAGE_CHANGED, arg0, 0));
		}

	}
}
