package cc.ruit.shunjianmei.home.hairdressert;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import cc.ruit.shunjianmei.R;
import cc.ruit.shunjianmei.constants.Constant;
import cc.ruit.shunjianmei.net.response.HairdresserListResponse;
import cc.ruit.shunjianmei.reserve.ReserveActivity;
import cc.ruit.shunjianmei.reserve.ReserveFragment;
import cc.ruit.shunjianmei.reserve.ReserveFreeFragment;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmei.util.FragmentManagerUtils;
import cc.ruit.shunjianmei.util.ImageLoaderUtils;
import cc.ruit.shunjianmei.util.MyEventBus;
import cc.ruit.shunjianmei.util.RoundImageLoaderUtil;
import cc.ruit.utils.sdk.ToastUtils;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import de.greenrobot.event.EventBus;

/**
 * 
 * @ClassName: HairdresserListAdapter
 * @Description: 发型师适配器
 * @author: HBM
 * @date: 2015年10月20日 下午9:36:13
 */
public class HairdresserListAdapter extends BaseAdapter {

	private FragmentActivity context;
	private List<HairdresserListResponse> list;
	private Type type;

	public HairdresserListAdapter(FragmentActivity context, List<HairdresserListResponse> list, Type type) {
		this.context = context;
		this.list = list;
		this.type = type;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		if (list == null || list.size() <= 0) {
			EventBus.getDefault().post(new MyEventBus("emptyViewSetNullState"));
			// ev.setNullState();
		}
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public HairdresserListResponse getItem(int position) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_hair_dressert_fragment_layout, null);
			vh.findView(convertView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		// 得到数据和绑定数据
		setLabel(position, vh);
		return convertView;
	}

	/**
	 * 
	 * @Title: setLabel
	 * @Description: 绑定控件
	 * @author: 欧阳
	 * @param position
	 * @param vh
	 * @return: void
	 */
	private void setLabel(final int position, final ViewHolder vh) {
		HairdresserListResponse obj = getItem(position);
		vh.tv_name.setText(obj.getName());
		vh.tv_LevelName.setText(obj.getLevelName());
		vh.tv_Star.setText(obj.getName());
		vh.tv_ordernum.setText("接单" + obj.getOrderNum() + "次");
		String price = TextUtils.isEmpty(obj.getPrice()) ? "0.00" : obj.getPrice();
		String tip = "<font color='#ababab'>设计剪发 </font><font color='#e53123'>" + "¥" + price + "</font>";
		vh.tv_price.setText(Html.fromHtml(tip));
		vh.tv_price.setVisibility(type == Type.collect ? View.GONE : View.VISIBLE);
		float start = Float.parseFloat(obj.getScore());
		vh.tv_evaluate.setRating(start);
		// vh.tv_find_type.setText(obj.getIstypeString());
		if (obj.getIstypeString().equals("自由定价")) {
			vh.tv_find_type.setImageResource(R.drawable.master_free);
		} else {
			vh.tv_find_type.setImageResource(R.drawable.master_deep);
		}
		ImageLoaderUtils.setErrImage(R.drawable.default_prc, R.drawable.default_prc, R.drawable.default_prc);
		ImageLoaderUtils.getInstance(context).loadImage(obj.getImage(), vh.iv_Image);
		// add by jiazhaohui
		String flag = obj.getFlagpng();
		if (flag == null || flag == "") {
			vh.iv_flag_Image.setVisibility(View.GONE);
		} else {
			vh.iv_flag_Image.setVisibility(View.VISIBLE);
			String flagstr = Constant.IMAGESURL + flag;
			ImageLoaderUtils.getInstance(context).loadImage(flagstr, vh.iv_flag_Image);
		}

		RoundImageLoaderUtil.setErrImage(R.drawable.tx_man, R.drawable.tx_man, R.drawable.tx_man);
		RoundImageLoaderUtil.getInstance(context, 360).loadImage(obj.getPhoto(), vh.iv_photo);
		vh.iv_Image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Fragment fragment = FragmentManagerUtils.getFragment(context,
						HairDressertDetailsFragment.class.getName());
				Bundle bundle = new Bundle();
				bundle.putString("ID", list.get(position).getID());
				bundle.putString("Name", list.get(position).getName());
				bundle.putString("Type", list.get(position).getIstypeString());
				fragment.setArguments(bundle);
				FragmentManagerUtils.add(context, R.id.content_frame, fragment, true);
			}
		});
		vh.ll_detail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Fragment fragment = FragmentManagerUtils.getFragment(context,
						HairDressertDetailsFragment.class.getName());
				Bundle bundle = new Bundle();
				bundle.putString("ID", list.get(position).getID());
				bundle.putString("Name", list.get(position).getName());
				bundle.putString("Type", list.get(position).getIstypeString());
				fragment.setArguments(bundle);
				FragmentManagerUtils.add(context, R.id.content_frame, fragment, true);
			}
		});
		vh.ll_time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (UserManager.getUserID() <= 0) {
					EventBus.getDefault().post(new MyEventBus("showUnLoginDialog"));
					// showUnLoginDialog();
					return;
				}
				// Fragment fragment = FragmentManagerUtils.getFragment(
				// activity, ReserveFragment.class.getName());
				// Bundle bundle = new Bundle();
				// bundle.putString("ID", list.get(position).getID());
				// bundle.putString("From",
				// HairDressertFragment.class.getSimpleName());
				// fragment.setArguments(bundle);
				// FragmentManagerUtils.add(activity, R.id.content_frame,
				// fragment, true);
				if (list.get(position).getIstypeString().equals("自由定价")) {
					if (list.get(position).getHasPacked().equals("1")) {
						Intent intent = ReserveActivity.getIntent(context, ReserveFreeFragment.class.getName());
						intent.putExtra("ID", list.get(position).getID());
						intent.putExtra("From", HairDressertFragment.class.getSimpleName());
						context.startActivity(intent);
					} else {
						ToastUtils.showShort("无此套餐");
					}
				} else {
					Intent intent = ReserveActivity.getIntent(context, ReserveFragment.class.getName());
					intent.putExtra("ID", list.get(position).getID());
					intent.putExtra("From", HairDressertFragment.class.getSimpleName());
					context.startActivity(intent);
				}

			}
		});
	}

	static class ViewHolder {
		@ViewInject(R.id.tv_hairstylist_master_name)
		TextView tv_name;// 发型师名称
		@ViewInject(R.id.tv_hairstylist_master_LevelName)
		TextView tv_LevelName;// 级别名称
		@ViewInject(R.id.tv_hairstylist_master_Star)
		TextView tv_Star;// 星座
		@ViewInject(R.id.tv_order_receiving_num)
		TextView tv_ordernum;// 接单量
		@ViewInject(R.id.ratingbar_all_evaluate)
		RatingBar tv_evaluate;// 评论
		@ViewInject(R.id.iv_hairstylist_Image)
		ImageView iv_Image;// 发型图片
		// add by jiazhaohui
		@ViewInject(R.id.iv_flag_Image)
		ImageView iv_flag_Image;// 发型图
		@ViewInject(R.id.image_hairstylist_photo)
		ImageView iv_photo;// 头像
		@ViewInject(R.id.ll_hairdressert_detail)
		LinearLayout ll_detail;// 详情
		@ViewInject(R.id.ll_hairdressert_time)
		LinearLayout ll_time;// 预约
		@ViewInject(R.id.iv_hairdressert_detail)
		ImageView iv_detail;
		@ViewInject(R.id.iv_hairdressert_time)
		ImageView iv_time;
		@ViewInject(R.id.tv_hairdressert_detail)
		TextView tv_detail;
		@ViewInject(R.id.tv_hairdressert_time)
		TextView tv_time;
		@ViewInject(R.id.tv_order_price)
		TextView tv_price;// 价格
		@ViewInject(R.id.image_find_type)
		ImageView tv_find_type;// 类型

		void findView(View convertView) {
			ViewUtils.inject(this, convertView);
		}
	}

	public enum Type {
		normal, collect
	}
}
