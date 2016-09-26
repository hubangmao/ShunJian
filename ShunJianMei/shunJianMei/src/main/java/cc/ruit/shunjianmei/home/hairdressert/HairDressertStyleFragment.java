package cc.ruit.shunjianmei.home.hairdressert;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cc.ruit.shunjianmei.R;
import cc.ruit.shunjianmei.base.BaseAdapter;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.home.hairstyle.HairStyleDetailsFragment;
import cc.ruit.shunjianmei.net.response.HairStyleListResponse;
import cc.ruit.shunjianmei.net.response.HairdresserDetailResponse;
import cc.ruit.shunjianmei.util.FragmentManagerUtils;
import cc.ruit.shunjianmei.util.ImageLoaderUtils;
import cc.ruit.shunjianmei.util.MyEventBus;
import cc.ruit.shunjianmei.util.view.MyGridView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.oruit.widget.loadingdialog.LoadingDailog;

import de.greenrobot.event.EventBus;

/**
 * 
 * @ClassName: HairDressertStyleFragment
 * @Description: 发型师中的发型
 * @author: HBM
 * @date: 2015年10月24日 下午8:35:44
 */
public class HairDressertStyleFragment extends BaseFragment {
	@ViewInject(R.id.gv_hairdressert)
	MyGridView hairdressert;// 布局
	private HairStyleAdapter mAdapter;// 发型列表
	List<HairStyleListResponse> list;
	List<HairdresserDetailResponse> templist;

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.hair_dressert_style_fragment, null);
		ViewUtils.inject(this, view);
		initGrivdView();
		initData();
		return view;
	}

	HairDressertStyleFragment(List<HairdresserDetailResponse> templist) {
		this.templist = templist;
		EventBus.getDefault().register(this);
	}

	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	/**
	 * 
	 * @Title: onEventMainThread
	 * @Description: 接收EventBus消息
	 * @author: Johnny
	 * @param event
	 * @return: void
	 */
	public void onEventMainThread(MyEventBus event) {
		if (event.getmMsg().equals("HairdresserDetail")) {
			this.templist = event.getTemplist();
			initData();
		}
	}

	/**
	 * 
	 * @Title: initGrivdView
	 * @Description: 初始化GrivdView
	 * @author: 欧阳
	 * @return: void
	 */
	private void initGrivdView() {
		list = new ArrayList<HairStyleListResponse>();
		mAdapter = new HairStyleAdapter(activity, list);
		hairdressert.setAdapter(mAdapter);
		hairdressert.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Fragment fragment = FragmentManagerUtils.getFragment(activity,
						HairStyleDetailsFragment.class.getName());
				Bundle bundle = new Bundle();
				bundle.putString("ID", list.get(position).ID);
				bundle.putString("Name", list.get(position).Name);
				fragment.setArguments(bundle);
				FragmentManagerUtils.add(getActivity(), R.id.content_frame,
						fragment, true);

			}
		});
	}

	HairdresserDetailResponse Response;

	/**
	 * 
	 * @Title: initData
	 * @Description: 初始化数据
	 * @author: 欧阳
	 * @return: void
	 */
	private void initData() {
		if (templist == null || templist.size() < 0) {
			return;
		}
		List<HairStyleListResponse> Response = templist.get(0).getHairStyle();
		list.clear();
		list.addAll(Response);
		mAdapter.notifyDataSetChanged();
		LoadingDailog.dismiss();

	}

	/**
	 * 
	 * @ClassName: HairStyleAdapter
	 * @Description: 发型管理适配器
	 * @author: 欧阳
	 * @date: 2015年10月16日 下午7:30:26
	 */
	class HairStyleAdapter extends BaseAdapter {
		List<HairStyleListResponse> list;
		int width = 0;

		public HairStyleAdapter(Context context,
				List<HairStyleListResponse> list) {
			super(context, list);
			this.list = list;
			width = getScreenWidth();
		}

		@Override
		public HairStyleListResponse getItem(int position) {
			return list.get(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;

			// 适配器的优化
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.item_hair_style_gv_layout, null);
				holder = new ViewHolder();
				holder.findView(convertView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			// 得到数据和绑定数据
			setLabel(holder, position);
			// int num = (width - 30) / 2 - BaseFragment.dip2px(context, 10);
			// LayoutParams p = new LayoutParams(num, num * 427 / 325);
			// convertView.setLayoutParams(p);
			return convertView;

		}

		/**
		 * 
		 * @Title: setLabel
		 * @Description: 绑定数据在控件上
		 * @author: 欧阳
		 * @param vh
		 * @param position
		 * @return: void
		 */
		public void setLabel(ViewHolder vh, final int position) {
			final HairStyleListResponse obj = getItem(position);
			ImageLoaderUtils.setErrImage(R.drawable.default_prc,
					R.drawable.default_prc, R.drawable.default_prc);
			ImageLoaderUtils.getInstance(context)
					.loadImage(obj.Photo, vh.iv_bg);
			vh.tv_style.setText(obj.Name);
			vh.tv_price.setText("¥" + obj.Price);
			vh.tv_person.setText(obj.UsedNum + "人做过");
		}
	}

	static class ViewHolder {
		@ViewInject(R.id.iv_hair_style_gv_item_bg)
		ImageView iv_bg;
		@ViewInject(R.id.tv_hair_style_gv_item_style)
		TextView tv_style;
		@ViewInject(R.id.tv_hair_style_gv_item_price)
		TextView tv_price;
		@ViewInject(R.id.tv_hair_style_gv_item_person)
		TextView tv_person;
		@ViewInject(R.id.ll_content)
		LinearLayout ll_content;

		void findView(View convertView) {
			ViewUtils.inject(this, convertView);
		}
	}

	public int getScreenWidth() {
		DisplayMetrics metric = new DisplayMetrics();
		WindowManager wm = (WindowManager) activity
				.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels;
		return width;
	}
}
