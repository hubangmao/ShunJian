package cc.ruit.shunjianmei.home.hairstyle;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cc.ruit.shunjianmei.R;
import cc.ruit.shunjianmei.base.BaseAdapter;
import cc.ruit.shunjianmei.net.response.HairStyleListResponse;
import cc.ruit.shunjianmei.util.ImageLoaderUtils;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 
 * @ClassName: HairStyleAdapter
 * @Description: 发型管理适配器
 * @author: 欧阳
 * @date: 2015年10月16日 下午7:30:26
 */
public class HairStyleAdapter extends BaseAdapter {
	List<HairStyleListResponse> list;

	public HairStyleAdapter(Context context, List<HairStyleListResponse> list) {
		super(context, list);
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
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
		// int num = (width-20) / 2 - BaseFragment.dip2px(activity, 5);
		// LayoutParams p = new LayoutParams();
		//
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
		ImageLoaderUtils.getInstance(context).loadImage(obj.Photo, vh.iv_bg);
		vh.tv_style.setText(obj.Name);
		vh.tv_price.setText("¥" + obj.Price);
		vh.tv_person.setText(obj.UsedNum + "人做过");
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

}
