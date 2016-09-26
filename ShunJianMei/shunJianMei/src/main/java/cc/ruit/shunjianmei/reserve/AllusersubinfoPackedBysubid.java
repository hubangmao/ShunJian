package cc.ruit.shunjianmei.reserve;

import java.util.List;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import cc.ruit.shunjianmei.R;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.net.response.GetAllusersubinfoPackedBysubidResponse;

public class AllusersubinfoPackedBysubid extends BaseFragment {

	List<GetAllusersubinfoPackedBysubidResponse> data;
	@ViewInject(R.id.allusersubinfopakc_lsitview)
	private ListViewForScrollView Allusersubinfo_lv;

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.modify_allusersubinfopackedbysubid, null);
		ViewUtils.inject(this, view);
		init();
		return view;
	}

	public AllusersubinfoPackedBysubid(List<GetAllusersubinfoPackedBysubidResponse> data) {
		this.data = data;
		// EventBus.getDefault().register(this);
	}

	private void init() {
		AllusersubinfoPackedBysubidAdapter value = new AllusersubinfoPackedBysubidAdapter(getActivity(), data);
		Allusersubinfo_lv.setAdapter(value);
	}

	class AllusersubinfoPackedBysubidAdapter extends BaseAdapter {
		private Context context;
		private List<GetAllusersubinfoPackedBysubidResponse> finish_data;

		public AllusersubinfoPackedBysubidAdapter(Context context, List<GetAllusersubinfoPackedBysubidResponse> data) {
			this.context = context;
			this.finish_data = data;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return finish_data.size();
		}

		@Override
		public GetAllusersubinfoPackedBysubidResponse getItem(int position) {
			// TODO Auto-generated method stub
			return finish_data.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh = null;
			if (convertView == null) {
				vh = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.reserve_listview_item_free_dialog, null);
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
			GetAllusersubinfoPackedBysubidResponse obj = getItem(position);
			// vh.tv_gv.setText(obj.Hour + ":00");
			String Longhair = "<font color='#333333'>长发  :</font><font color='#f34c28'>" + " ￥" + obj.getLonghair()
					+ "</font>";
			vh.tv_longhair.setText(Html.fromHtml(Longhair));
			String Inhair = "<font color='#333333'>中发  :</font><font color='#f34c28'>" + " ￥" + obj.getInhair()
					+ "</font>";
			vh.tv_inhair.setText(Html.fromHtml(Inhair));
			String Shorthair = "<font color='#333333'>短发  :</font><font color='#f34c28'>" + " ￥" + obj.getShorthair()
					+ "</font>";
			vh.tv_shorthair.setText(Html.fromHtml(Shorthair));
			vh.tv_name.setText(obj.getName());
			vh.tv_time.setText(obj.getTimes() + "分钟");
			vh.btn_select.setVisibility(View.GONE);
			if (vh.tv_content.getLineCount() > 1) {
				vh.tv_content.setText(obj.getIntro() + ".........");
			} else {
				vh.tv_content.setText(obj.getIntro());
			}

			// vh.tv_longhair.setText("长发 :￥ " + obj.getLonghair());
			// vh.tv_inhair.setText("中发 :￥ " + obj.getInhair());
			// vh.tv_shorthair.setText("短发 :￥ " + obj.getShorthair());
			// vh.tv_dialog.setVisibility(View.GONE);

		}
	}

	static class ViewHolder {

		@ViewInject(R.id.tv_left_reserve_dialog_name)
		TextView tv_name;
		@ViewInject(R.id.tv_right_lv_reserve_dialog_time)
		TextView tv_time;
		@ViewInject(R.id.tv_show_package_details__dialog_content)
		TextView tv_content;
		@ViewInject(R.id.tv_long_hair_price)
		TextView tv_longhair;
		@ViewInject(R.id.tv_in_hair_price)
		TextView tv_inhair;
		@ViewInject(R.id.tv_short_hair_price)
		TextView tv_shorthair;
		@ViewInject(R.id.btn_select_icon_lv_reserve_dialogsss)
		TextView btn_select;

		// @ViewInject(R.id.btn_select_icon_lv_reserve_dialogsss)
		// TextView tv_dialog;

		void findView(View convertView) {
			ViewUtils.inject(this, convertView);
		}
	}

	/**
	 * @Title: setLable
	 * @Description: 绑定数据
	 * @author: Johnny
	 * @param position
	 * @param vh
	 * @return: void
	 */

}
