package cc.ruit.utils.sdk;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * GridView,ListView 高度计算 用于GridView，,ListView内容显示不全
 * 
 * @author lilw
 * @CrateTime 2012-12-3
 */
public class SetViewHeight {

	/**
	 * 重新计算GridView高度 比边其他滚动条使listView显示不全 让GridView显示全部内容 注： 本方法调用完毕后最好
	 * 手动调用一次adapter.notifyDataSetChanged();
	 * @author lilw
	 * @CrateTime 2013-4-2
	 * @param gridView 要调整的GridView
	 */
	@SuppressLint("NewApi")
	public static void setGridViewHeightBasedOnChildren(GridView gridView,String viewName) {
//		LogUtils.i("viewName :"+viewName);
		ListAdapter listAdapter = gridView.getAdapter();
		int num = gridView.getNumColumns();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		int count = 1;
		count = (int) Math.ceil((double)listAdapter.getCount() / (double)num);
//		LogUtils.i("count :"+count);
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, gridView);
			listItem.measure(0, 0);
			int itemHeight = listItem.getMeasuredHeight();
			if (totalHeight < itemHeight) {
				totalHeight = itemHeight;
			}
		}
//		LogUtils.i("totalHeight :"+totalHeight);
		ViewGroup.LayoutParams params = gridView.getLayoutParams();
		int spacing = 0;
		
		if(Build.VERSION.SDK_INT>=16 ){
			spacing = gridView.getVerticalSpacing()*(count==1?1:(count));
		}
		
		params.height =(totalHeight * count)+spacing+gridView.getPaddingTop()+gridView.getPaddingBottom();
//		LogUtils.i("params.height :"+params.height );
		gridView.setLayoutParams(params);
	}
	@SuppressLint("NewApi")
	public static void setGridViewHeightBasedOnChildren(GridView gridView) {
		setGridViewHeightBasedOnChildren(gridView, "");
	}
	
	
	
	
	/**
	 * 
	 * 重新计算listView高度 比边其他滚动条使listView显示不全 让listView显示全部内容 注： 调用完毕后最好
	 * 手动调用一次adapter.notifyDataSetChanged();
	 * 
	 * @author lilw
	 * @CrateTime 2012-12-11
	 * @param listView
	 *            需要显示全部内容的listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}
		int totalHeight = 0;
		// int minHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			// listView.get;
			View listItem = listAdapter.getView(i, null, listView);
			if(listItem==null) continue;
			try {
				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			// if (i==listAdapter.getCount()-1&&listAdapter.getCount()>1) {
			// totalHeight += listItem.getMeasuredHeight();
			// }
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ listView.getDividerHeight() * (listAdapter.getCount() + 5);
		listView.setLayoutParams(params);
	}
	/**
	 * @Title: getListViewHeightBasedOnChildren
	 * @Description: 获取item高度
	 * @author: lee
	 * @param listView
	 * @return: int
	 */
	public static int getListViewHeightBasedOnChildren(ListView listView){
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return 0;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			// listView.get;
			View listItem = listAdapter.getView(i, null, listView);
			if(listItem==null) continue;
			try {
				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		int height = totalHeight+listView.getDividerHeight() * (listAdapter.getCount());
		return height;
	}
}
