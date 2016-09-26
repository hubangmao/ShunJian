package cc.ruit.utils.zq;

import android.view.View;

/**
 * 生成GridView的网格线的工具
 */
public class GridDividerCreater {

	/**
	 * 
	 * @Title: create
	 * @Description: 生成GridView的网格线，水平线和垂直线初始状态需要为不可见
	 * @author: Johnny
	 * @param position
	 *            GridView的item位置
	 * @param columnNums
	 *            GridView的列数
	 * @param size
	 *            GridView的item个数
	 * @param verticalLine
	 *            垂直线
	 * @param horizontalLine
	 *            水平线
	 * @return: void
	 */
	public static void create(int position, int columnNums, int size,
			View verticalLine, View horizontalLine) {
		int start = position + 1;
		int end = size - columnNums;

		if (start % columnNums == 0 && start <= end) {
			verticalLine.setVisibility(View.INVISIBLE);
			horizontalLine.setVisibility(View.VISIBLE);
		}
		if (start % columnNums > 0 && start <= end) {
			verticalLine.setVisibility(View.VISIBLE);
			horizontalLine.setVisibility(View.VISIBLE);
		}
		if (start % columnNums == 0 && start > end) {
			verticalLine.setVisibility(View.INVISIBLE);
			horizontalLine.setVisibility(View.INVISIBLE);
		}
		if (start % columnNums > 0 && start > end) {
			verticalLine.setVisibility(View.VISIBLE);
			horizontalLine.setVisibility(View.INVISIBLE);
		}
	}
}
