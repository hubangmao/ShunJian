package cc.ruit.utils.zq;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class BasePagerAdapter extends PagerAdapter {

	private List<View> listViews;
	
	public BasePagerAdapter(List<View> listViews,List<String> titleList) {
		this.listViews = listViews;
	}

	@Override
	public int getCount() {
		return listViews.size();// 返回视图的数量
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(listViews.get(position));// 删除视图
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {// 这个方法用来实例化视图
		container.addView(listViews.get(position), 0);// 添加视图
		return listViews.get(position);
	}

}
