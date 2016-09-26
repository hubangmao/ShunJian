package cc.ruit.shunjianmei.home.store;

import java.util.List;

import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import cc.ruit.shunjianmei.R;
import cc.ruit.shunjianmei.net.response.StoreListResponse;
import cc.ruit.shunjianmei.util.ImageLoaderUtils;
import cc.ruit.shunjianmei.util.MyEventBus;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import de.greenrobot.event.EventBus;

public class StoreListAdapter extends BaseAdapter {

	private FragmentActivity context;
	private List<StoreListResponse> list;

	public StoreListAdapter(FragmentActivity context, List<StoreListResponse> list) {
		this.context = context;
		this.list = list;
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
	public StoreListResponse getItem(int position) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.store_listview_item_layout, null);
			vh.findView(convertView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		// 得到数据和绑定次数
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
		StoreListResponse obj = getItem(position);
		vh.tv_name.setText(obj.getName());
		vh.tv_distance.setText(obj.getDistance() + "km");
		vh.tv_address.setText(obj.getAddress());
		vh.tv_ordernum.setText(obj.getOrderNum() + "次");
		vh.tv_parking.setText("停车位" + obj.getCarNum() + "个");
		// vh.tv_find_type.setText(obj.getIstype());
		if (obj.getIstype().equals("自由定价")) {
			vh.tv_find_type.setImageResource(R.drawable.master_free);
		} else {
			vh.tv_find_type.setImageResource(R.drawable.master_deep);
		}
		ImageLoaderUtils.setErrImage(R.drawable.default_prc, R.drawable.default_prc, R.drawable.default_prc);
		ImageLoaderUtils.getInstance(context).loadImage(obj.getPhoto(), vh.iv_photo);
		// add by jiazhaohui
		// 是否有闪惠标签
		if (obj.getQuickpay().equals("0")) {
			vh.iv_quickpay_icon.setVisibility(View.GONE);
		} else {
			vh.iv_quickpay_icon.setVisibility(View.VISIBLE);
		}

		vh.ratingbar.setRating(Float.parseFloat(obj.getScore()));
	}

	static class ViewHolder {
		@ViewInject(R.id.tv_name_store)
		TextView tv_name;// 店铺名称
		@ViewInject(R.id.tv_distance_store)
		TextView tv_distance;// 店铺距离
		@ViewInject(R.id.tv_address_store)
		TextView tv_address;// 店铺地址
		@ViewInject(R.id.tv_ordernum_store)
		TextView tv_ordernum;// 接单�?
		@ViewInject(R.id.tv_parking_store)
		TextView tv_parking;// 停车�?

		@ViewInject(R.id.ratingbar_stroe)
		RatingBar ratingbar;// 星级评分�?

		@ViewInject(R.id.iv_photo_store)
		ImageView iv_photo;// 店铺照片

		@ViewInject(R.id.iv_quickpay_icon)
		ImageView iv_quickpay_icon;
		@ViewInject(R.id.tv_find_type)
		ImageView tv_find_type;// 类型

		void findView(View convertView) {
			ViewUtils.inject(this, convertView);
		}
	}

}
