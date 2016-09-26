package cc.ruit.shunjianmei.home.hairdressert;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cc.ruit.shunjianmei.R;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.constants.Constant;
import cc.ruit.shunjianmei.home.store.StoreDetailFragment;
import cc.ruit.shunjianmei.net.api.HairdresserScheduleApi;
import cc.ruit.shunjianmei.net.request.HairdresserScheduleRequest;
import cc.ruit.shunjianmei.net.response.HairdresserScheduleResponse;
import cc.ruit.shunjianmei.net.response.HairdresserScheduleResponse.Item;
import cc.ruit.shunjianmei.net.response.HairdresserScheduleResponse.Item.StoreItem;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmei.util.FragmentManagerUtils;
import cc.ruit.shunjianmei.util.ScreenUtils;
import cc.ruit.shunjianmei.util.view.MyGridView;
import cc.ruit.utils.sdk.ToastUtils;
import cc.ruit.utils.sdk.http.NetWorkUtils;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.oruit.widget.loadingdialog.LoadingDailog;

/**
 * 
 * @ClassName: HairdresserScheduleFragment
 * @Description: 发型师日程列表
 * @author: HBM
 * @date: 2015年10月24日 下午4:38:29
 */
public class HairdresserScheduleFragment extends BaseFragment {
	@ViewInject(R.id.schedule_gridlayout)
	MyGridView schedule_gridlayout;// 展示日期
	@ViewInject(R.id.schedule_listlayout)
	ListView schedule_listlayout;// 展示地址
	@ViewInject(R.id.schedule_radiogroup)
	private RadioGroup radioGroup;
	@ViewInject(R.id.schedule_horizontalScrollView_top)
	private HorizontalScrollView horizontalScrollView;
	@ViewInject(R.id.schedule_imageView_left)
	private ImageView leftImageView;// 向左箭头
	@ViewInject(R.id.schedule_imageView_right)
	private ImageView rightImageView;// 向右箭头
	StoreScheduleAdapter listadapter;// 地址的展示
	ScheduleGridViewAdapter Gridadapter;// 时间的展示
	private View subViewChild;
	private int subViewWidth, subViewMoveX;
	List<cc.ruit.shunjianmei.net.response.HairdresserScheduleResponse.Item.StoreItem> listitem;// listview地址数据
	List<cc.ruit.shunjianmei.net.response.HairdresserScheduleResponse.Item> Gridlist;// gridview时间数据
	String ID;// 发型师ID
	List<HairdresserScheduleResponse> templist;

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.hair_dresser_schedule_fragment, null);
		ViewUtils.inject(this, view);
		initHorizontalScrollView();
		initListview();
		initGridView();
		if (templist != null && templist.size() > 0) {
			addRadioButton();
		}
		return view;
	}

	/**
	 * 
	 * @Title:HairdresserScheduleFragment
	 * @Description:发型师详情
	 * @param ID
	 */
	HairdresserScheduleFragment(String ID, List<HairdresserScheduleResponse> list) {
		this.ID = ID;
		this.templist = list;
	}

	/**
	 * 
	 * @Title: initHorizontalScrollView
	 * @Description: 初始化水平滚动条
	 * @author: 欧阳
	 * @return: void
	 */
	private void initHorizontalScrollView() {
		horizontalScrollView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				subViewChild = (View) horizontalScrollView
						.getChildAt(horizontalScrollView.getChildCount() - 1);
				subViewWidth = subViewChild.getRight();
				subViewMoveX = horizontalScrollView.getScrollX();
				if (subViewWidth - subViewMoveX
						- horizontalScrollView.getWidth() <= 45) {
					// 最右边
					leftImageView.setVisibility(View.VISIBLE);
					rightImageView.setVisibility(View.GONE);
				} else if (subViewMoveX <= 10) {
					leftImageView.setVisibility(View.GONE);
					rightImageView.setVisibility(View.VISIBLE);
				} else {
					leftImageView.setVisibility(View.VISIBLE);
					rightImageView.setVisibility(View.VISIBLE);
				}
				return false;
			}
		});

	}

	/**
	 * 
	 * @Title: initListview
	 * @Description: 初始化listview
	 * @return: void
	 */
	private void initListview() {
		listitem = new ArrayList<HairdresserScheduleResponse.Item.StoreItem>();
		listadapter = new StoreScheduleAdapter(activity, listitem);
		schedule_listlayout.setAdapter(listadapter);
		schedule_listlayout.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Fragment fragment = FragmentManagerUtils.getFragment(activity,
						StoreDetailFragment.class.getName());
				Bundle bundle = new Bundle();
				String ID = listitem.get(position).ID;
				bundle.putString("ID", ID);
				fragment.setArguments(bundle);
				FragmentManagerUtils.add(activity, R.id.content_frame,
						fragment, true);
			}
		});

	}

	/**
	 * 
	 * @Title: initGridView
	 * @Description: 初始化gridview
	 * @author: 欧阳
	 * @return: void
	 */
	private void initGridView() {
		Gridlist = new ArrayList<HairdresserScheduleResponse.Item>();
		Gridadapter = new ScheduleGridViewAdapter(activity, Gridlist);
		schedule_gridlayout.setAdapter(Gridadapter);
		schedule_gridlayout.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Gridadapter.setSeclection(position);
				Gridadapter.notifyDataSetChanged();
				Item info = Gridlist.get(position);
				List<StoreItem> item = info.Item;
				listitem.clear();
				listitem.addAll(item);
				listadapter.notifyDataSetChanged();

			}
		});
	}

	/**
	 * 
	 * @Title: addRadioButton
	 * @Description: 根据返回数据的条数动态的显示按钮个数
	 * @author: 欧阳
	 * @param total
	 * @return: void
	 */
	protected void addRadioButton() {
		for (int i = 0; i < templist.size(); i++) {
			final RadioButton rb = new RadioButton(activity);
			android.view.ViewGroup.LayoutParams params = new LayoutParams(
					ScreenUtils.dip2px(activity, 80), LayoutParams.MATCH_PARENT);
			rb.setLayoutParams(params);
			rb.setButtonDrawable(android.R.color.transparent);
			rb.setGravity(Gravity.CENTER);
			rb.setBackground(activity.getResources().getDrawable(
					R.drawable.storedetail_radiobutton_bg));
			rb.setSingleLine(true);
			rb.setText(templist.get(i).getDate() + "("
					+ templist.get(i).getWeekDay() + ")");
			// TODO
			rb.setTextColor(activity.getResources().getColor(
					R.drawable.storedetail_textview_bg));
			rb.setTextSize(13);
			rb.setOnCheckedChangeListener(new OnCheckedChangeListener() {// 次监听用于解决radiobutton无法改变字体颜色的bug

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					int black = activity.getResources().getColor(R.color.black);
					int red = activity.getResources().getColor(R.color.red_e5);
					rb.setTextColor(isChecked ? red : black);
				}
			});
			rb.setOnClickListener(new MyOnClickListener(templist.get(i)
					.getItem()));
			radioGroup.addView(rb);
			rb.setChecked(i == 0 ? true : false);
		}
		if (templist.get(0).getItem() != null
				&& templist.get(0).getItem().size() > 0) {
			Gridlist.clear();
			Gridlist.addAll(templist.get(0).getItem());
			Gridadapter.notifyDataSetChanged();
			List<StoreItem> StoreItem = templist.get(0).getItem().get(0).Item;
			listitem.clear();
			listitem.addAll(StoreItem);
			listadapter.notifyDataSetChanged();
		}
		LoadingDailog.dismiss();
	}

	private class MyOnClickListener implements OnClickListener {
		private List<cc.ruit.shunjianmei.net.response.HairdresserScheduleResponse.Item> item;

		MyOnClickListener(
				List<cc.ruit.shunjianmei.net.response.HairdresserScheduleResponse.Item> data) {
			item = data;

		}

		@Override
		public void onClick(View v) {
			if (item == null || item.size() <= 0) {
				return;
			}
			Gridlist.clear();
			Gridlist.addAll(item);
			Gridadapter.notifyDataSetChanged();
			List<StoreItem> StoreItem = item.get(0).Item;
			listitem.clear();
			listitem.addAll(StoreItem);
			listadapter.notifyDataSetChanged();

		}
	}

	/**
	 * 
	 * @ClassName: StoreScheduleAdapter
	 * @Description: 店铺信息
	 * @author: 欧阳
	 * @date: 2015年10月26日 下午12:57:06
	 */
	class StoreScheduleAdapter extends BaseAdapter {

		private Context context;
		private List<cc.ruit.shunjianmei.net.response.HairdresserScheduleResponse.Item.StoreItem> list;

		public StoreScheduleAdapter(
				Context context,
				List<cc.ruit.shunjianmei.net.response.HairdresserScheduleResponse.Item.StoreItem> list) {
			this.context = context;
			this.list = list;
		}

		@Override
		public int getCount() {
			return list == null ? 0 : list.size();
		}

		@Override
		public cc.ruit.shunjianmei.net.response.HairdresserScheduleResponse.Item.StoreItem getItem(
				int position) {
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
				convertView = LayoutInflater.from(context).inflate(
						R.layout.item_hair_dresser_schedule_fragment, null);
				vh.findView(convertView);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			// 得到数据和绑定数据
			setLabel(position, vh);
			return convertView;
		}

		private void setLabel(int position, ViewHolder vh) {
			cc.ruit.shunjianmei.net.response.HairdresserScheduleResponse.Item.StoreItem StoreItem = getItem(position);
			vh.localtionname.setText(StoreItem.Name);
		}
	}

	static class ViewHolder {
		@ViewInject(R.id.tv_localtionname)
		TextView localtionname;// 美发师名称
		@ViewInject(R.id.rl_StoreSchedule)
		RelativeLayout StoreSchedule;// 点击事件

		void findView(View convertView) {
			ViewUtils.inject(this, convertView);
		}
	}

	/**
	 * 
	 * @ClassName: ScheduleGridViewAdapter
	 * @Description: 事件安排
	 * @author: 欧阳
	 * @date: 2015年10月26日 下午12:56:44
	 */
	class ScheduleGridViewAdapter extends BaseAdapter {

		private Context context;
		private List<cc.ruit.shunjianmei.net.response.HairdresserScheduleResponse.Item> mlist;
		private int clickTemp = -1;

		public ScheduleGridViewAdapter(
				Context context,
				List<cc.ruit.shunjianmei.net.response.HairdresserScheduleResponse.Item> mlist) {
			this.context = context;
			this.mlist = mlist;
		}

		// 标识选择的Item
		public void setSeclection(int position) {
			clickTemp = position;
		}

		@Override
		public int getCount() {
			return mlist == null ? 0 : mlist.size();
		}

		@Override
		public cc.ruit.shunjianmei.net.response.HairdresserScheduleResponse.Item getItem(
				int position) {
			return mlist.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewitemHolder vh = null;
			if (convertView == null) {
				vh = new ViewitemHolder();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.schedulefragment_gridview_item, null);
				vh.findView(convertView);
				convertView.setTag(vh);
			} else {
				vh = (ViewitemHolder) convertView.getTag();
			}
			if (clickTemp == position) {
				vh.datetime.setBackgroundColor(getResources().getColor(
						R.color.gray_ab));
			} else {
				vh.datetime.setBackgroundColor(getResources().getColor(
						R.color.gray_e5));
			}
			// 得到数据和绑定数据
			if (mlist.get(position).State.equals(Constant.SCHEDULE_STATE_CAN_ORDER)) {
				vh.datetime.setText(mlist.get(position).Hour + ":00(" + "可约"
						+ ")");
				vh.datetime.setBackgroundColor(getResources().getColor(
						R.color.gray_f5));
			} else if (mlist.get(position).State.equals(Constant.SCHEDULE_STATE_REST)) {
				vh.datetime.setText(mlist.get(position).Hour + ":00(" + "休息"
						+ ")");
				vh.datetime.setBackgroundColor(getResources().getColor(
						R.color.gray_d8));
			} else if (mlist.get(position).State.equals(Constant.SCHEDULE_STATE_LOCK)) {//锁定状态显示约满
				vh.datetime.setText(mlist.get(position).Hour + ":00(" + "约满"
						+ ")");
				vh.datetime.setBackgroundColor(getResources().getColor(
						R.color.gray_d8));
			} else if (mlist.get(position).State.equals(Constant.SCHEDULE_STATE_YET_ORDER) ) {
				vh.datetime.setText(mlist.get(position).Hour + ":00(" + "约满"
						+ ")");
				vh.datetime.setBackgroundColor(getResources().getColor(
						R.color.gray_d8));
			}else if (mlist.get(position).State.equals(Constant.SCHEDULE_STATE_OVERDUE)) {
				vh.datetime.setText(mlist.get(position).Hour + ":00(" + "过期"
						+ ")");
				vh.datetime.setBackgroundColor(getResources().getColor(
						R.color.gray_d8));
			}
			return convertView;
		}
	}

	static class ViewitemHolder {

		@ViewInject(R.id.schedule_gridview_item_time)
		TextView datetime;

		void findView(View convertView) {
			ViewUtils.inject(this, convertView);
			datetime.setTextColor(Color.GRAY);
		}
	}

}
