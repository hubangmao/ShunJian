package cc.ruit.utils.sdk.ouys;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 
 * @ClassName: GridViewAdapter
 * @Description: GridView添加一个空白item 监听空白的item
 * @author: 欧阳
 * @date: 2015年12月9日 上午9:24:41
 */
public class GridViewAdapter extends BaseAdapter {
	List<Object> list;
	int width = 0;
	Context context;

	public GridViewAdapter(Context context, List<Object> list) {
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		return list.size() + 1;
	}

	@Override
	public int getItemViewType(int position) {
		if (position == list.size()) {
			return 1;
		} else {
			return 2;
		}

	}

	@Override
	public int getViewTypeCount() {
		return 3;// 当前类型加1;
	}

	@Override
	public Object getItem(int position) {
		if (position >= list.size()) {
			return null;
		} else {
			return list.get(position);
		}

	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 根据item的类型来加载不同的布局 ，点击事件也是根据item的类型点击事件
		if (getItemViewType(position) == 1) {

		} else if (getItemViewType(position) == 2) {

		}
		// 当第二种类型的布局才去绑定数据
		if (getItemViewType(position) == 2) {
			// 得到数据和绑定数据
			
		}
		return convertView;
	}

}
