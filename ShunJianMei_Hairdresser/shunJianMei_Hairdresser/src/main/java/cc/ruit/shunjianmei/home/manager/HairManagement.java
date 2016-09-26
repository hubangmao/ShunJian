package cc.ruit.shunjianmei.home.manager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cc.ruit.shunjianmei.base.BaseAdapter;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.net.api.H_HairStyleStateApi;
import cc.ruit.shunjianmei.net.api.HairStyleManagerApi;
import cc.ruit.shunjianmei.net.request.H_HairStyleStateRequest;
import cc.ruit.shunjianmei.net.request.HairStyleListRequest;
import cc.ruit.shunjianmei.net.response.HairListResponse;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmei.util.FragmentManagerUtils;
import cc.ruit.shunjianmei.util.ImageLoaderUtils;
import cc.ruit.shunjianmei.util.MyEventBus;
import cc.ruit.shunjianmeihairdresser.R;
import cc.ruit.utils.sdk.ToastUtils;
import cc.ruit.utils.sdk.http.NetWorkUtils;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.oruit.widget.loadingdialog.LoadingDailog;
import com.oruit.widget.loadingdialog.LoadingViewUtil;
import com.oruit.widget.title.TitleUtil;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

/**
 * 
 * @ClassName: HairManagementFragment
 * @Description: 发型管理
 * @author: 欧阳
 * @date: 2015年10月16日 下午7:16:32
 */
public class HairManagement extends BaseFragment {
	@ViewInject(R.id.gv_hair)
	GridView gv_hair;// 返回按钮
	private HairStyleAdapter mAdapter;
	List<HairListResponse> list;
	public final static int LOOKHAIR = 1;

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.hair_style_manager_layout, null);
		ViewUtils.inject(this, view);
		initTitle(view);// 初始化标题
		initEventBus();// 初始化EventBus
		initGridView();// 初始化GridView
		LoadingDailog.show(activity, "正在加载...");
		initData();// 请求数据
		return view;
	}

	/**
	 * 
	 * @Title: initEventBus
	 * @Description: 注册EventBus
	 * @author: 欧阳
	 * @return: void
	 */
	private void initEventBus() {
		EventBus.getDefault().register(this);
	}

	/**
	 * 
	 * @Title: onDestroy
	 * @Description: 注销EventBus
	 * @see cc.ruit.shunjianmei.base.BaseFragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("HairManagement"); // 统计页面
	}

	/**
	 * 
	 * @Title: onEventMainThread
	 * @Description: 这个方法不能进行耗时操作
	 * @author: 欧阳
	 * @param bus
	 * @return: void
	 */
	public void onEventMainThread(MyEventBus bus) {
		if (!TextUtils.isEmpty(bus.getmMsg())) {
			initData();
		}
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("HairManagement");
	}

	/**
	 * 
	 * @Title: initData
	 * @Description: 初始化数据
	 * @author: 欧阳
	 * @return: void
	 */
	private void initTitle(View view) {
		TitleUtil titleUtil = new TitleUtil(view);
		// 设置标题栏中间的文字
		titleUtil.tv_title.setText("发型管理");
		// 设置标题栏左边的图片
		titleUtil.iv_left.setVisibility(View.VISIBLE);
		titleUtil.iv_left.setImageResource(R.drawable.back);
		titleUtil.iv_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				activity.finish();
			}
		});
	}

	/**
	 * 
	 * @Title: initGridView
	 * @Description: 初始化发型项目GridView
	 * @author: 欧阳
	 * @return: void
	 */
	private void initGridView() {
		list = new ArrayList<HairListResponse>();
		mAdapter = new HairStyleAdapter(activity, list, activity);
		gv_hair.setAdapter(mAdapter);
		initData();
		gv_hair.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (mAdapter.getItemViewType(position) == 1) {
					Fragment fragment = FragmentManagerUtils.getFragment(activity, HairStyleDetailment.class.getName());
					FragmentManagerUtils.add(getActivity(), R.id.content_frame, fragment, true);
				}

			}
		});

	}

	/**
	 * 
	 * @Title: initData
	 * @Description: 初始化数据
	 * @author: 欧阳
	 * @return: void
	 */
	private void initData() {
		if (!NetWorkUtils.isConnectInternet(activity)) {
			ToastUtils.showShort("网络未链接，请检查网络设置");
			LoadingDailog.dismiss();
			new LoadingViewUtil(view).hint();
			return;
		}
		HairStyleListRequest request = new HairStyleListRequest(UserManager.getUserID() + "");
		HairStyleManagerApi.HairStyleList(request, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				BaseResponse response = BaseResponse.getBaseResponse(responseInfo.result);
				LoadingDailog.dismiss();
				if (response == null) {
					return;
				}

				String[] split = response.getMsg().split("\\|");
				if ("1".equals(split[0])) {
					ToastUtils.showShort(split[1] + "");
				}
				if (response.getCode() == 1000) {
					List<HairListResponse> temlist = HairListResponse.getclazz2(response.getData());
					if (temlist.size() > 0 & temlist != null) {
						list.clear();
						list.addAll(temlist);
					}
					// 通知adapter改变
					mAdapter.notifyDataSetChanged();
					LoadingDailog.dismiss();
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				ToastUtils.showShort("请求数据失败");
				LoadingDailog.dismiss();
			}
		});

	}

	/**
	 * 
	 * @ClassName: HairStyleAdapter
	 * @Description: 发型管理适配器
	 * @author: 欧阳
	 * @date: 2015年10月16日 下午7:30:26
	 */
	class HairStyleAdapter extends BaseAdapter {
		List<HairListResponse> list;
		FragmentActivity activity;
		int width = 0;
		View lastView;

		public HairStyleAdapter(Context context, List<HairListResponse> list, FragmentActivity activity) {
			super(context, list);
			this.list = list;
			this.activity = activity;
			width = getScreenWidth();
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
			return 3;
		}

		@Override
		public HairListResponse getItem(int position) {
			if (position >= list.size()) {
				return null;
			} else {
				return list.get(position);
			}

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;

			// 适配器的优化
			if (convertView == null) {
				if (getItemViewType(position) == 1) {
					convertView = LayoutInflater.from(activity).inflate(R.layout.gridview_last_item, null);
				} else {
					convertView = LayoutInflater.from(activity).inflate(R.layout.item_hair_style_gv_layout, null);
					holder = new ViewHolder();
					holder.findView(convertView);
					convertView.setTag(holder);
				}

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			// 当第二种类型的布局才去绑定数据
			if (getItemViewType(position) == 2) {
				// 得到数据和绑定数据
				setLabel(holder, position);
			}
			int num = (width - 30) / 2 - BaseFragment.dip2px(activity, 10);
			LayoutParams p = new LayoutParams(num, num * 5 / 3);
			convertView.setLayoutParams(p);
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
			final HairListResponse obj = getItem(position);
			int state = 0;
			try {
				state = Integer.parseInt(obj.getState());
			} catch (Exception e) {
			}
			// ---------提示文字颜色不一样，特殊处理一下-----------
			switch (state) {
			case 1:
				vh.tv_del.setVisibility(View.INVISIBLE);
				vh.tv_editor.setVisibility(View.INVISIBLE);
				vh.tv_state.setText("审核中");
				break;
			case 2:
				vh.tv_del.setVisibility(View.INVISIBLE);
				vh.tv_editor.setVisibility(View.VISIBLE);
				vh.tv_state.setText("审核失败");
				break;
			case 3:
				vh.tv_editor.setVisibility(View.VISIBLE);
				vh.tv_del.setVisibility(View.VISIBLE);
				vh.tv_state.setText("待发布");
				vh.tv_del.setText("发布");
				break;
			case 4:
				vh.tv_editor.setVisibility(View.VISIBLE);
				vh.tv_del.setVisibility(View.VISIBLE);
				vh.tv_state.setText("已发布");
				vh.tv_del.setText("撤销");
				break;
			default:
				break;
			}
			ImageLoaderUtils.setErrImage(R.drawable.default_prc, R.drawable.default_prc, R.drawable.default_prc);
			ImageLoaderUtils.getInstance(activity).loadImage(obj.getPhoto(), vh.iv_bg);
			vh.tv_style.setText(obj.getName());
			vh.tv_price.setText("¥" + obj.getPrice());
			if (obj.getUsedNum().equals("0.0")) {
				vh.tv_person.setVisibility(View.GONE);
			}else{
				vh.tv_person.setText(obj.getUsedNum() + "人做过");
			}
			vh.tv_editor.setOnClickListener(new OnClickListener() {// 编辑

				@Override
				public void onClick(View v) {
					HairStyleDetailment fg = new HairStyleDetailment();
					Bundle bundle = new Bundle();
					bundle.putString("HairStyleDetail", obj.getID() + "");
					fg.setArguments(bundle);
					FragmentManagerUtils.add(getActivity(), R.id.content_frame, fg, false);
				}
			});
			vh.tv_del.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (Integer.parseInt(getItem(position).getState()) == 3) {// 待发布
						UpdateHairStyleState(getItem(position).getID(), "3", position);

					} else if (Integer.parseInt(getItem(position).getState()) == 4) {// 撤销
						UpdateHairStyleState(getItem(position).getID(), "2", position);
					}

				}
			});
		}

		/**
		 * 
		 * @Title: UpdateHairStyleState
		 * @Description: 更新发型状态
		 * @author: 欧阳
		 * @param id
		 * @param string
		 * @return: void
		 */
		protected void UpdateHairStyleState(String id, final String state, final int position) {
			H_HairStyleStateRequest request = new H_HairStyleStateRequest(UserManager.getUserID() + "", id, state);
			H_HairStyleStateApi.H_HairStyleState(request, new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					BaseResponse response = BaseResponse.getBaseResponse(responseInfo.result);
					if (response == null) {
						return;
					}

					String[] split = response.getMsg().split("\\|");
					if ("1".equals(split[0])) {
						ToastUtils.showShort(split[1] + "");
					}
					if (response.getCode() == 1000) {
						if (Integer.parseInt(state) == 3) {
							getItem(position).setState("4");
							notifyDataSetChanged();
						} else {
							getItem(position).setState("3");
							notifyDataSetChanged();
						}
						ToastUtils.showShort("操作成功");
					}
				}

				@Override
				public void onFailure(HttpException error, String msg) {
					ToastUtils.showShort("更新状态失败");
				}
			});
		}
	}

	static class ViewHolder {
		@ViewInject(R.id.tv_hair_style_gv_item_state)
		TextView tv_state;
		@ViewInject(R.id.iv_hair_style_gv_item_bg)
		ImageView iv_bg;
		@ViewInject(R.id.tv_hair_style_gv_item_del)
		TextView tv_del;
		@ViewInject(R.id.tv_hair_style_gv_item_editor)
		TextView tv_editor;
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

	public int getScreenWidth() {
		DisplayMetrics metric = new DisplayMetrics();
		WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels;
		return width;
	}

}
