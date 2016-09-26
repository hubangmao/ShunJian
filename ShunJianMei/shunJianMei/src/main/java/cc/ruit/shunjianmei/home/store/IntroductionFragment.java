package cc.ruit.shunjianmei.home.store;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cc.ruit.shunjianmei.R;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.net.response.StoreDetailResponse;
import cc.ruit.shunjianmei.net.response.StoreDetailResponse.Comment;
import cc.ruit.shunjianmei.net.response.StoreDetailResponse.Comment.Item;
import cc.ruit.shunjianmei.util.ForLargeImageActivity;
import cc.ruit.shunjianmei.util.FragmentManagerUtils;
import cc.ruit.shunjianmei.util.ImageLoaderUtils;
import cc.ruit.shunjianmei.util.MyEventBus;
import cc.ruit.shunjianmei.util.RoundImageLoaderUtil;
import cc.ruit.shunjianmei.util.ScreenUtils;
import cc.ruit.utils.sdk.date.DateUtil;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

/**
 * @ClassName: IntroductionFragment
 * @Description: 店面详情简介
 * @author: Johnny
 * @date: 2015年10月14日 下午4:32:59
 */
public class IntroductionFragment extends BaseFragment {

	@ViewInject(R.id.ratingbar_evaluate_storedetail)
	private RatingBar ratingbar_evaluate;// 评论星形评分条
	@ViewInject(R.id.rl_evaluate_storedetail)
	private RelativeLayout rl_evaluate;// 用户评论
	@ViewInject(R.id.ll_images_evaluate_storedetail)
	private LinearLayout ll_images_evaluate;// 用户评论上传的照片

	private TextView tv_phone;// 电话
	@ViewInject(R.id.tv_introduce_storedetail)
	private TextView tv_introduce;// 简介
	@ViewInject(R.id.tv_comment_num_storedetail)
	private TextView tv_commentnum;// 评论数
	@ViewInject(R.id.iv_photo_evaluate_storedetail)
	private ImageView iv_photo;// 评论用户头像
	@ViewInject(R.id.tv_name_evaluate_storedetail)
	private TextView tv_name_evaluate;// 评论用户昵称
	@ViewInject(R.id.tv_date_evaluate_storedetail)
	private TextView tv_date_evaluate;// 评论时间
	@ViewInject(R.id.tv_comment_evaluate_storedetail)
	private TextView tv_comment;// 评论

	private StoreDetailResponse obj;

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("IntroductionFragment"); // 统计页面
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("IntroductionFragment");
	}

	IntroductionFragment(StoreDetailResponse obj) {
		this.obj = obj;
		EventBus.getDefault().register(this);
	}

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.storedetail_introduction_layout, null);
		ViewUtils.inject(this, view);
		initData();
		return view;
	}

	/**
	 * @Title: initData
	 * @Description: 初始化数据
	 * @author: Johnny
	 * @return: void
	 */
	private void initData() {
		if (obj == null) {
			return;
		} 
			Comment comment = obj.getComment();
			if (comment==null) {
				return;
			}
			tv_introduce.setText(obj.getIntro());
			tv_commentnum.setText("(" + obj.getCommentNum() + ")");
			tv_name_evaluate.setText(comment.Name);
			if (!TextUtils.isEmpty(comment.Time)) {
				try {
					Date date = DateUtil.parseStringToDate(comment.Time);
					SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd hh:mm");
					String format = sd.format(date);
					tv_date_evaluate.setText(format);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
			}
			//tv_date_evaluate.setText(comment.Time.substring(0, comment.Time.length()-3));
			
			if(!("").equals(comment.Content)&&comment.Content!=null){
				tv_comment.setText(comment.Content);
				tv_comment.setVisibility(View.VISIBLE);
			}
			RoundImageLoaderUtil.setErrImage(R.drawable.tx_man, R.drawable.tx_man, R.drawable.tx_man);
			RoundImageLoaderUtil.getInstance(activity, 500).loadImage(
					comment.Photo, iv_photo);
			iv_photo.setVisibility(TextUtils.isEmpty(comment.Photo)?View.INVISIBLE:View.VISIBLE);
			addImages(comment.Item, ll_images_evaluate);// 添加用户评论时上传的图片
			if (TextUtils.isEmpty(comment.Score)) {
				ratingbar_evaluate.setVisibility(View.INVISIBLE);
			}else {
				ratingbar_evaluate.setVisibility(View.VISIBLE);
				ratingbar_evaluate.setRating(Float.parseFloat(comment.Score));
			}
	}

	/**
	 * 
	 * @Title: addImages
	 * @Description:添加用户评论的图片
	 * @author: Johnny
	 * @return: void
	 */
	private void addImages(final List<Item> images, LinearLayout ll) {
		ll.removeAllViews();
		for (int i = 0;images!=null && i < images.size(); i++) {
			final int index = i;

			ImageView iv = new ImageView(activity);
			//将指定的单位值转换为像素值
			int padding = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 5, activity.getResources()
							.getDisplayMetrics());
			iv.setPadding(padding, padding, padding, padding);
			ImageLoaderUtils.setErrImage(R.drawable.default_prc, R.drawable.default_prc,
					R.drawable.default_prc);
			ImageLoaderUtils.getInstance(activity).loadImage(
					images.get(i).Photo, iv);
			int width = (ScreenUtils.getScreenWidth(activity) - padding*2) / 3;
			iv.setLayoutParams(new LayoutParams(width, width));
			iv.setScaleType(ScaleType.CENTER_CROP);

			iv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ArrayList<String> list = new ArrayList<String>();
					for (int j = 0; j < images.size(); j++) {
						list.add(images.get(j).Photo);
					}
					startActivity(ForLargeImageActivity.getIntent(activity,
							index, list));
				}
			});

			ll.addView(iv);

		}
	}

	/**
	 * 
	 * @Title: onClick
	 * @Description: 定义点击事件
	 * @param v
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@OnClick({ R.id.rl_evaluate_storedetail })
	public void onClick(View v) {
		switch (v.getId()) {
		// 评论列表
		case R.id.rl_evaluate_storedetail:
			Fragment fragment = FragmentManagerUtils.getFragment(activity,
					StoreEvaluateFragment.class.getName());
			Bundle bundle = new Bundle();
			bundle.putString("storeID", obj.getID());
			fragment.setArguments(bundle);
			FragmentManagerUtils.add(activity, R.id.content_frame, fragment,
					true);
			break;

		default:
			break;
		}
	}
	
	/**
	 * 
	 * @Title: onEventMainThread
	 * @Description: 接收EventBus消息
	 * @author: Johnny
	 * @param event
	 * @return: void
	 */
	public void onEventMainThread(MyEventBus event){
		if(event.getmMsg().equals("Introduction")){
			this.obj = event.getObj();
			initData();
		}
	}

	/**
	 * 
	 * @Title: createRatingBar
	 * @Description:创建RatingBar
	 * @author: Johnny
	 * @param ll
	 * @param num
	 * @return: void
	 */
	// private void createRatingBar(LinearLayout ll, int num) {
	// ll.removeAllViews();
	// for (int i = 0; i < 5; i++) {
	// ImageView imageView = new ImageView(activity);
	//
	// if (i < num) {
	// imageView.setImageResource(R.drawable.star_solid);
	// } else {
	// imageView.setImageResource(R.drawable.star_stroke);
	// }
	//
	// int padding = (int) TypedValue.applyDimension(
	// TypedValue.COMPLEX_UNIT_DIP, 5, activity.getResources()
	// .getDisplayMetrics());
	// imageView.setPadding(padding, padding, padding, padding);
	// int width = (int) TypedValue.applyDimension(
	// TypedValue.COMPLEX_UNIT_DIP, 120, activity.getResources()
	// .getDisplayMetrics()) / 5;
	// ll.setGravity(Gravity.CENTER_VERTICAL);
	// ll.addView(imageView, width, width);
	// }
	// }
	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}
