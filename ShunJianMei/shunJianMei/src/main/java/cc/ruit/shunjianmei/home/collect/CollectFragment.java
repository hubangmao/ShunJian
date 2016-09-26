package cc.ruit.shunjianmei.home.collect;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cc.ruit.shunjianmei.R;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.home.FindFragment;
import cc.ruit.shunjianmei.home.hairdressert.HairDressertFragment;
import cc.ruit.shunjianmei.home.me.MeFragment;
import cc.ruit.shunjianmei.home.order.OrderFragment;
import cc.ruit.shunjianmei.util.FragmentManagerUtils;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.oruit.widget.title.TitleUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * @ClassName: CollectFragment
 * @Description: 我的收藏界面
 * @author: HBM
 * @date: 2015年12月9日 下午8:38:26
 */
public class CollectFragment extends BaseFragment implements OnClickListener {

	@ViewInject(R.id.fl_container)
	private FrameLayout fl_container;// 内容布局

	@ViewInject(R.id.tv_style_collect)
	private TextView tv_style;// 发型
	@ViewInject(R.id.tv_dresser_collect)
	private TextView tv_dresser;// 发型师
	@ViewInject(R.id.tv_store_collect)
	private TextView tv_store;// 理发店
	@ViewInject(R.id.cursor1)
	private View cursor1;
	@ViewInject(R.id.cursor2)
	private View cursor2;
	@ViewInject(R.id.cursor3)
	private View cursor3;

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("CollectFragment"); // 统计页面
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("CollectFragment");
	}

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.collect_layout, null);
		ViewUtils.inject(this, view);
		initTitle();
		initData();
		return view;
	}

	/**
	 * @Title: initTitle
	 * @Description: 标题初始化
	 * @author: Johnny
	 * @return: void
	 */
	private void initTitle() {
		TitleUtil title = new TitleUtil(view);
		title.iv_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean isBack = FragmentManagerUtils.back(getActivity(), R.id.content_frame);
				if (!isBack) {
					getActivity().finish();
				}
			}
		});
		title.iv_left.setImageResource(R.drawable.arrow_left_white);
		title.iv_left.setVisibility(View.VISIBLE);
		title.tv_title.setText("我的收藏");
	}

	/**
	 * 
	 * @Title: initData
	 * @Description:
	 * @author: Johnny
	 * @return: void
	 */
	private void initData() {
		tabControler(HairstyleFragment.class.getName());
	}

	/**
	 * @Title: tabControler
	 * @Description: tab切换
	 * @author: lee
	 * @param className
	 * @return: void
	 */
	public void tabControler(String className) {
		FragmentManagerUtils.detachByTag(activity, HairdresserFragment.class.getName(),
				HairstyleFragment.class.getName(), HairstoreFragment.class.getName(), MeFragment.class.getName());
		FragmentManagerUtils.addOrAttach(activity, className, R.id.fl_container);
		// 添加要显示的fragment
		setCursorColor(className);
	}

	/**
	 * 
	 * @Title: setCursorColor
	 * @Description: 改变头标的颜色
	 * @author: Johnny
	 * @param i
	 * @return: void
	 */
	private void setCursorColor(String className) {
		cursor1.setBackgroundColor(activity.getResources()
				.getColor(HairstyleFragment.class.getName().equals(className) ? R.color.red_e5 : R.color.main_grey_bg));
		cursor2.setBackgroundColor(activity.getResources().getColor(
				HairdresserFragment.class.getName().equals(className) ? R.color.red_e5 : R.color.main_grey_bg));
		cursor3.setBackgroundColor(activity.getResources()
				.getColor(HairstoreFragment.class.getName().equals(className) ? R.color.red_e5 : R.color.main_grey_bg));
	}

	/**
	 * 
	 * @Title: onClick
	 * @Description: 定义点击事件
	 * @param v
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	@OnClick({ R.id.tv_style_collect, R.id.tv_dresser_collect, R.id.tv_store_collect })
	public void onClick(View v) {
		switch (v.getId()) {
		// 发型
		case R.id.tv_style_collect:
			tabControler(HairstyleFragment.class.getName());
			break;

		// 发型师
		case R.id.tv_dresser_collect:
			tabControler(HairdresserFragment.class.getName());
			break;
		// 美发店
		case R.id.tv_store_collect:
			tabControler(HairstoreFragment.class.getName());
			break;

		default:

			break;
		}
	}

}
