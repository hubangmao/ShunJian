package cc.ruit.shunjianmei.home.store;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import cc.ruit.shunjianmei.R;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.home.MainActivity;
import cc.ruit.shunjianmei.home.hairdressert.HairDressertDetailsFragment;
import cc.ruit.shunjianmei.login.LoginActivity;
import cc.ruit.shunjianmei.login.LoginFragment;
import cc.ruit.shunjianmei.net.response.StoreDetailResponse;
import cc.ruit.shunjianmei.net.response.StoreDetailResponse.Schedule;
import cc.ruit.shunjianmei.reserve.ReserveActivity;
import cc.ruit.shunjianmei.reserve.ReserveFragment;
import cc.ruit.shunjianmei.reserve.ReserveFreeFragment;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmei.util.FragmentManagerUtils;
import cc.ruit.shunjianmei.util.RoundImageLoaderUtil;
import cc.ruit.shunjianmei.util.ScreenUtils;
import cc.ruit.shunjianmei.util.view.EmptyView;
import cc.ruit.shunjianmei.util.view.MyListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.oruit.widget.messagedialog.MessageDialog;
import com.umeng.analytics.MobclickAgent;

/**
 * @ClassName: DresserFragment
 * @Description: 店面详情美发师列表
 * @author: Johnny
 * @date: 2015年10月14日 下午4:32:59
 */
public class DresserFragment extends BaseFragment {

	@ViewInject(R.id.radiogroup_storedetail)
	private RadioGroup radioGroup;
	@ViewInject(R.id.scrollview_storedetail)
	private HorizontalScrollView horizontalScrollView;
	@ViewInject(R.id.iv_left_storedetail)
	private ImageView leftImageView;// 向左箭头
	@ViewInject(R.id.iv_right_storedetail)
	private ImageView rightImageView;// 向右箭头

	@ViewInject(R.id.lv_dresser_storedetail)
	private MyListView lv_dresser;// 美发师列表

	private EmptyView ev;// 空载页

	private View subViewChild;
	private int subViewWidth, subViewMoveX;

	private List<StoreDetailResponse.Schedule.Item> msgList = new ArrayList<StoreDetailResponse.Schedule.Item>();
	private DresserListAdapter myAdapter;
	private StoreDetailResponse obj;

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("DresserFragment"); // 统计页面
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("DresserFragment");
	}

	DresserFragment(StoreDetailResponse obj) {
		this.obj = obj;
	}

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.storedetail_hairdresser_layout, null);
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

		myAdapter = new DresserListAdapter(activity, msgList);
		lv_dresser.setAdapter(myAdapter);

		// horizontalScrollView.setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// subViewChild = (View) horizontalScrollView
		// .getChildAt(horizontalScrollView.getChildCount() - 1);
		// subViewWidth = subViewChild.getRight();
		// subViewMoveX = horizontalScrollView.getScrollX();
		// if (subViewWidth - subViewMoveX
		// - horizontalScrollView.getWidth() <= 45) {
		// // 最右边
		// leftImageView.setVisibility(View.VISIBLE);
		// rightImageView.setVisibility(View.GONE);
		// } else if (subViewMoveX <= 10) {
		// leftImageView.setVisibility(View.GONE);
		// rightImageView.setVisibility(View.VISIBLE);
		// } else {
		// leftImageView.setVisibility(View.VISIBLE);
		// rightImageView.setVisibility(View.VISIBLE);
		// }
		// return false;
		// }
		// });

		addRadioButton(obj.getSchedule());
	}

	// /**
	// *
	// * @Title: addImages
	// * @Description:
	// * @author: Johnny
	// * @return: void
	// */
	// private void addImages(List<Item> images, LinearLayout ll) {
	// ll.removeAllViews();
	// for (int i = 0; i < images.size(); i++) {
	// ImageView iv = new ImageView(activity);
	// iv.setScaleType(ScaleType.CENTER_CROP);
	// ImageLoaderUtils.getInstance(activity).loadImage(
	// images.get(i).Photo, iv);
	// int padding = (int) TypedValue.applyDimension(
	// TypedValue.COMPLEX_UNIT_DIP, 10, activity.getResources()
	// .getDisplayMetrics());
	// iv.setPadding(padding, padding, padding, padding);
	// int width = ScreenUtils.getScreenWidth(activity) / 3;
	// ll.addView(iv, new LayoutParams(width, width));
	// }
	// }

	/**
	 * 
	 * @Title: addRadioButton
	 * @Description:添加水平滚动条
	 * @author: Johnny
	 * @return: void
	 */
	private void addRadioButton(List<Schedule> schedule) {

		if (schedule == null || schedule.size() <= 0) {
			return;
		}

		for (int i = 0; i < schedule.size(); i++) {

			final RadioButton rb = new RadioButton(activity);
			android.view.ViewGroup.LayoutParams params = new LayoutParams(ScreenUtils.dip2px(activity, 80),
					LayoutParams.MATCH_PARENT);
			rb.setLayoutParams(params);
			rb.setButtonDrawable(android.R.color.transparent);
			rb.setGravity(Gravity.CENTER);
			rb.setBackground(activity.getResources().getDrawable(R.drawable.storedetail_radiobutton_bg));
			rb.setSingleLine(true);
			rb.setText(schedule.get(i).Date + "(" + schedule.get(i).WeekDay + ")");
			rb.setTextColor(activity.getResources().getColor(R.color.black33));
			rb.setTextSize(13);

			rb.setOnCheckedChangeListener(new OnCheckedChangeListener() {// 次监听用于解决radiobutton无法改变字体颜色的bug

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					int black = activity.getResources().getColor(R.color.black);
					int red = activity.getResources().getColor(R.color.red_e5);
					rb.setTextColor(isChecked ? red : black);
				}
			});
			rb.setOnClickListener(new MyOnClickListener(schedule.get(i).Item));
			radioGroup.addView(rb);
			rb.setChecked(i == 0 ? true : false);
		}

		List<cc.ruit.shunjianmei.net.response.StoreDetailResponse.Schedule.Item> item = schedule.get(0).Item;
		msgList.clear();
		msgList.addAll(item);
		myAdapter.notifyDataSetChanged();
	}

	/**
	 * 
	 * @Title: onClick
	 * @Description: 定义点击事件
	 * @param v
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@OnClick({ R.id.ll_star, R.id.ll_order, R.id.ll_distance, R.id.ll_parking })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_star:
			break;

		default:
			break;
		}
	}

	/**
	 * 
	 * @ClassName: MyOnClickListener
	 * @Description: 自定义的
	 * @author: Johnny
	 * @date: 2015年11月8日 上午2:08:13
	 */
	private class MyOnClickListener implements OnClickListener {
		private List<StoreDetailResponse.Schedule.Item> item;

		MyOnClickListener(List<StoreDetailResponse.Schedule.Item> data) {
			item = data;
		}

		@Override
		public void onClick(View v) {
			if (item == null || item.size() <= 0) {
				return;
			}

			msgList.clear();
			msgList.addAll(item);
			myAdapter.notifyDataSetChanged();
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

	/**
	 * 
	 * @ClassName: DresserListAdapter
	 * @Description: 美发店详情评论列表Adapter
	 * @author: Johnny
	 * @date: 2015年10月24日 上午11:03:43
	 */
	class DresserListAdapter extends BaseAdapter {

		private Context context;
		private List<StoreDetailResponse.Schedule.Item> list;

		public DresserListAdapter(Context context, List<StoreDetailResponse.Schedule.Item> list) {
			this.context = context;
			this.list = list;
		}

		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
			if (list == null || list.size() <= 0) {
				ev.setNullState();
			}
		}

		@Override
		public int getCount() {
			return list == null ? 0 : list.size();
		}

		@Override
		public cc.ruit.shunjianmei.net.response.StoreDetailResponse.Schedule.Item getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh = null;
			if (convertView == null) {
				vh = new ViewHolder();
				convertView = LayoutInflater.from(context)
						.inflate(R.layout.storedetail_hairdresser_listview_item_layout, null);
				vh.findView(convertView);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			// 得到数据和绑定数�?
			setLabel(position, vh);
			return convertView;
		}

		/**
		 * @Title: setLable
		 * @Description: TODO
		 * @author: Johnny
		 * @param position
		 * @param vh
		 * @return: void
		 */
		private void setLabel(int position, ViewHolder vh) {
			final cc.ruit.shunjianmei.net.response.StoreDetailResponse.Schedule.Item item = getItem(position);
			vh.tv_name.setText(item.Name);
			vh.tv_levelname.setText(item.LevelName);
			vh.tv_star.setText(item.Star);
			vh.tv_ordernum.setText(item.OrderNum + "次");
			RoundImageLoaderUtil.setErrImage(R.drawable.tx_man, R.drawable.tx_man, R.drawable.tx_man);
			RoundImageLoaderUtil.getInstance(activity, 500).loadImage(item.Photo, vh.iv_photo);

			vh.ratingbar.setRating(Float.parseFloat(item.Score));
			// 详情
			vh.ll_detail.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Fragment fragment = FragmentManagerUtils.getFragment(activity,
							HairDressertDetailsFragment.class.getName());
					Bundle bundle = new Bundle();
					bundle.putString("ID", item.ID);
					bundle.putString("Name", item.Name);
					if (obj.getIstype().equals("0")) {
						bundle.putString("Type", "资深合作");
					} else {
						bundle.putString("Type", "自由定价");
					}
					fragment.setArguments(bundle);
					FragmentManagerUtils.add(activity, R.id.content_frame, fragment, true);
				}
			});

			// 预约
			vh.ll_reserve.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (UserManager.getUserID() <= 0) {
						showUnLoginDialog();
						return;
					}
					if (obj.getIstype().equals("1")) {
						Intent intent = ReserveActivity.getIntent(context, ReserveFreeFragment.class.getName());
						intent.putExtra("ID", item.ID);
						intent.putExtra("From", DresserFragment.class.getSimpleName());
						startActivity(intent);
					} else {
						Intent intent = ReserveActivity.getIntent(context, ReserveFragment.class.getName());
						intent.putExtra("ID", item.ID);
						intent.putExtra("From", DresserFragment.class.getSimpleName());
						startActivity(intent);
					}

				}
			});
		}
	}

	static class ViewHolder {
		@ViewInject(R.id.tv_name_storedetail)
		TextView tv_name;// 美发师名称
		@ViewInject(R.id.tv_levelname_storedetail)
		TextView tv_levelname;// 美发师头衔
		@ViewInject(R.id.tv_star_storedetail)
		TextView tv_star;// 美发师星座
		@ViewInject(R.id.tv_ordernum_storedetail)
		TextView tv_ordernum;// 接单量

		@ViewInject(R.id.ratingbar_lv_stroedetail)
		RatingBar ratingbar;// 星级评分条

		@ViewInject(R.id.iv_photo_storedetail)
		ImageView iv_photo;// 美发师照片

		@ViewInject(R.id.ll_hairdressert_detail_storedetail)
		LinearLayout ll_detail;// 美发师详情
		@ViewInject(R.id.ll_hairdressert_reserve_storedetail)
		LinearLayout ll_reserve;// 美发师预约

		void findView(View convertView) {
			ViewUtils.inject(this, convertView);
		}
	}

	/**
	 * @Title: showDialog
	 * @Description: 提示登录
	 * @author: lee
	 * @param checkVersionResponse
	 * @return: void
	 */
	void showUnLoginDialog() {
		final MessageDialog messageDialog = new MessageDialog(activity);
		messageDialog.setMessage("请先登录");
		messageDialog.getOkButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 启动登录
				Bundle bundle = new Bundle();
				bundle.putString("from", MainActivity.class.getSimpleName());
				Intent in = LoginActivity.getIntent(activity, LoginFragment.class.getName());
				in.putExtras(bundle);
				startActivity(in);
				messageDialog.dismiss();
			}
		});
		messageDialog.getCancelButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				messageDialog.dismiss();
			}
		});
		messageDialog.show();
	}
}
