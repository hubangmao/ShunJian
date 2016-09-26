package cc.ruit.shunjianmei.util.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cc.ruit.shunjianmei.R;
import cc.ruit.shunjianmei.net.response.OpenCityListResponse;
import cc.ruit.shunjianmei.util.ScreenUtils;
import cc.ruit.utils.sdk.SetViewHeight;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @ClassName: FilterPopupwindow
 * @Description: 筛选弹框
 * @author: lee
 * @date: 2015年10月17日 下午5:15:02
 */
public class CityPopupwindow implements OnItemClickListener {
	private Context context;
	private View view;
	// private PopupWindow pw;
	private ListView lv;
	private FilterListAdapter adapter;
	List<OpenCityListResponse> list = new ArrayList<OpenCityListResponse>();
	private FrameLayout parent;
	private OnItemSelecedListener listener;
	private boolean isCancel = true;
	private int tag;// 弹出标识
	private int selectedPosition = -1;// 默认选项

	public CityPopupwindow(Context context, FrameLayout parent) {
		super();
		this.context = context;
		this.parent = parent;
		init();
	}

//	public static FilterPopupwindow getInstance(Context context,
//			FrameLayout parent) {
//		if (instance == null) {
//			instance = new FilterPopupwindow(context, parent);
//		}
//		return instance;
//	}

	/**
	 * @Title: init
	 * @Description: 初始化
	 * @author: lee
	 * @return: void
	 */
	void init() {
		view = LayoutInflater.from(context).inflate(
				R.layout.filter_popupwindow_layout, null);
		lv = (ListView) view.findViewById(R.id.mlv_content);
		adapter = new FilterListAdapter(context, list);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);
		view.findViewById(R.id.null_space).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// pw.dismiss();
						dismiss();
					}
				});
		parent.addView(view);
		view.setVisibility(View.GONE);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (isCancel) {
			dismiss();
		}
		if (listener != null) {
			listener.onItemClick(parent, view, position, id, tag);
		}
	}

	/**
	 * @Title: setOnItemClickListener
	 * @Description: 设置点击监听
	 * @author: lee
	 * @param listener
	 * @return: void
	 */
	public void setOnItemClickListener(OnItemSelecedListener listener) {
		this.listener = listener;
	}

	/**
	 * @Title: setAutoCancel
	 * @Description: 设置自动关闭 默认为自动关闭
	 * @author: lee
	 * @param isCancel
	 * @return: void
	 */
	public void setAutoCancel(boolean isCancel) {
		this.isCancel = isCancel;
	}

	/**
	 * @Title: setSelectedPosition
	 * @Description: 设置默认选中项
	 * @author: lee
	 * @param position
	 * @return: void
	 */
	public void setSelectedPosition(int position) {
		selectedPosition = position;
		adapter.notifyDataSetChanged();
	}

	/**
	 * 
	 * @Title: isShowingTag
	 * @Description: 返回下拉菜单开启时的tag
	 * @author: Johnny
	 * @return
	 * @return: String
	 */
	public int isShowingTag() {
		if (view.getVisibility() == View.VISIBLE) {
			return tag;
		}
		return -1;
	}

	/**
	 * 
	 * @Title: isCurrentShowing
	 * @Description: 判断下来菜单是否开启，
	 * @author: Johnny
	 * @param tag
	 * @return
	 * @return: boolean true为开启,false为未开启
	 */
	public boolean isCurrentShowing(int tag) {
		if (view.getVisibility() == View.VISIBLE && this.tag != -1
				&& this.tag == tag) {
			return true;
		}
		return false;
	}

	/**
	 * @Title: show
	 * @Description: 显示弹框
	 * @author: lee
	 * @param anchor
	 * @param list
	 * @return: void
	 */
	public void show(List<OpenCityListResponse> list, int tag) {
		if (this.list != null) {
			this.list.clear();
			this.list.addAll(list);
		}
		view.setVisibility(View.VISIBLE);
		adapter.notifyDataSetChanged();
		int height = SetViewHeight.getListViewHeightBasedOnChildren(lv);
		if (height>(int) (ScreenUtils.getDeviceHeight(context)*0.35)) {
			android.view.ViewGroup.LayoutParams params = lv.getLayoutParams();
			params.height = (int) (ScreenUtils.getDeviceHeight(context)*0.35);
		}else {
			android.view.ViewGroup.LayoutParams params = lv.getLayoutParams();
			params.height = height;
		}
		this.tag = tag;
	}

	/**
	 * @Title: dismiss
	 * @Description: 关闭弹框
	 * @author: lee
	 * @return: void
	 */
	public void dismiss() {
		// if (pw!=null&&pw.isShowing()) {
		// pw.dismiss();
		// }
		if (view != null) {
			view.setVisibility(View.GONE);
		}
	}

	/**
	 * @ClassName: WithdrawHistoryAdapter
	 * @Description: 提现历史
	 * @author: lee
	 * @date: 2015年9月11日 下午3:09:35
	 */
	class FilterListAdapter extends BaseAdapter {

		private Context context;
		private List<OpenCityListResponse> list;
		private int red;// 红色字体颜色值
		private int black;// 黑色字体颜色值

		public FilterListAdapter(Context context, List<OpenCityListResponse> list) {
			this.context = context;
			this.list = list;
			red = context.getResources().getColor(R.color.red_f3);
			black = context.getResources().getColor(R.color.black);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list == null ? 0 : list.size();
		}

		@Override
		public OpenCityListResponse getItem(int position) {
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
						R.layout.item_filter_popupwindow_layout, null);
				vh.findView(convertView);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			// 得到数据和绑定数据
			setLable(position, vh);
			return convertView;
		}

		/**
		 * @Title: setLable
		 * @Description: TODO
		 * @author: lee
		 * @param position
		 * @param vh
		 * @return: void
		 */
		private void setLable(int position, ViewHolder vh) {
			OpenCityListResponse obj = getItem(position);
			vh.tv_name.setText(obj.getName());
			vh.iv_select.setVisibility(View.GONE);
			vh.tv_name.setTextColor(position == selectedPosition ? red : black);
		}

	}

	static class ViewHolder {
		@ViewInject(R.id.tv_name)
		TextView tv_name;
		@ViewInject(R.id.iv_select)
		ImageView iv_select;//选择
		void findView(View convertView) {
			ViewUtils.inject(this, convertView);
		}
	}

	public interface OnItemSelecedListener {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id, int tag);
	}
}
