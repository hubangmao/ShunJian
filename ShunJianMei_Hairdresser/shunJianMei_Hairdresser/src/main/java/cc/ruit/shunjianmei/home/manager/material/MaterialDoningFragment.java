package cc.ruit.shunjianmei.home.manager.material;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cc.ruit.shunjianmei.base.BaseActivity;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.net.api.MaterialApi;
import cc.ruit.shunjianmei.net.request.MaterialApplyRequest;
import cc.ruit.shunjianmei.net.request.UpdateRecordStateRequest;
import cc.ruit.shunjianmei.net.response.MaterialApplyItemResponse;
import cc.ruit.shunjianmei.net.response.MaterialApplyResponse;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmei.util.view.EmptyView;
import cc.ruit.shunjianmeihairdresser.R;
import cc.ruit.utils.sdk.ToastUtils;
import cc.ruit.utils.sdk.http.NetWorkUtils;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.oruit.widget.loadingdialog.LoadingDailog;
import com.oruit.widget.loadingdialog.LoadingViewUtil;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshBase;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshBase.OnRefreshListener;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshListView;

public class MaterialDoningFragment extends BaseFragment {
	@ViewInject(R.id.materialdoing_listview)
	private PullToRefreshListView lv_refresh;
	private DoingMaterialListAdapter mAdapter;
	EmptyView ev;// 空载页
	List<MaterialApplyResponse> msgList = new ArrayList<MaterialApplyResponse>();

	@SuppressLint("InflateParams")
	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.materialdoing_fragment_layout, null);
		ViewUtils.inject(this, view);
		initData();
		return view;
	}

	private void initData() {
		mAdapter = new DoingMaterialListAdapter(activity, msgList);
		lv_refresh.setAdapter(mAdapter);
		lv_refresh.getRefreshableView().setDividerHeight(
				BaseActivity.dip2px(activity, 5));
		ev = new EmptyView(activity, new OnClickListener() {

			@Override
			public void onClick(View v) {
				LoadingDailog.show(activity, "数据加载中，请稍后");
				getData();
			}
		});
		lv_refresh.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				getData();
			}
		});
		lv_refresh.setEmptyView(ev.getView());
		LoadingDailog.show(activity, "数据加载中，请稍候。。。");
		getData();
	}

	/**
	 * @Title: getPoints
	 * @Description: 获取数据
	 * @author: lee
	 * @return: void
	 */
	public void getData() {

		if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
			ToastUtils.showShort(activity.getResources().getString(
					R.string.no_networks_found));
			LoadingDailog.dismiss();
			lv_refresh.onRefreshComplete();
			ev.setErrState();
			new LoadingViewUtil(view).hint();
			return;
		}
		MaterialApplyRequest request = new MaterialApplyRequest(""
				+ UserManager.getUserID());
		MaterialApi.materialApplyDelivery(request,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						new LoadingViewUtil(view).hint();
						LoadingDailog.dismiss();
						BaseResponse result = BaseResponse
								.getBaseResponse(responseInfo.result);
						if (result == null) {
							return;
						}
						lv_refresh.onRefreshComplete();
						String[] split = result.getMsg().split("\\|");
						if ("1".equals(split[0])) {
							ToastUtils.showShort(split[1] + "");
						}
						if (result.getCode() == 1000) {
							List<MaterialApplyResponse> response = MaterialApplyResponse
									.getclazz2(result.getData());
							resultHanlder(response);
						} else {
							ev.setErrState();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						new LoadingViewUtil(view).hint();
						LoadingDailog.dismiss();
						ev.setErrState();
						lv_refresh.onRefreshComplete();
						ToastUtils.showShort(activity.getResources().getString(
								R.string.request_failure));
					}
				});
	}

	/**
	 * @Title: resultHanlder
	 * @Description: 结果处理
	 * @author: lee
	 * @param listcontrolSignUp
	 * @return: void
	 */
	void resultHanlder(List<MaterialApplyResponse> info) {
		if (info == null) {
			LogUtils.e("MessageListResponse err");
			return;
		}

		msgList.clear();
		msgList.addAll(info);
		if (msgList.size() > 0) {
			ev.setVisible(false);
		} else {
			ev.setNullState();
		}
		mAdapter.notifyDataSetChanged();

	}

	/**
	 * @ClassName: WithdrawHistoryAdapter
	 * @Description: 提现历史
	 * @author: lee
	 * @date: 2015年9月11日 下午3:09:35
	 */
	class DoingMaterialListAdapter extends BaseAdapter {

		private Context context;
		private List<MaterialApplyResponse> list;

		public DoingMaterialListAdapter(Context context,
				List<MaterialApplyResponse> list) {
			this.context = context;
			this.list = list;
		}

		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
			if (list == null || list.size() <= 0) {
				ev.setNullState();
			}
		}

		@Override
		public int getCount() {
			return list == null ? 0 : list.size();
		}

		@Override
		public MaterialApplyResponse getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh = null;
			if (convertView == null) {
				vh = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.materialdoing_listview_item, null);
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
		 * @author: lee
		 * @param position
		 * @param vh
		 * @return: void
		 */
		private void setLable(final int position, ViewHolder vh) {
			MaterialApplyResponse obj = getItem(position);
			vh.tv_time.setText(obj.getDate());
			vh.tv_address.setText(obj.getAddress());
			vh.tv_ok.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					updateRecordState(list.get(position).getID(), position);
				}
			});
			List<MaterialApplyItemResponse> listitem = obj.getItem();
			addView(vh.listView, listitem);
		}

		private void addView(LinearLayout listView,
				List<MaterialApplyItemResponse> listitem) {
			// TODO
			// if (listView.getChildCount() == listitem.size()) {
			// return;
			// }
			listView.removeAllViews();
			for (int i = 0; i < listitem.size(); i++) {
				ViewitemHolder vhitem = new ViewitemHolder();
				View convertView = LayoutInflater.from(context).inflate(
						R.layout.materialdoing_listview_listview_item, null);
				vhitem.findView(convertView);
				MaterialApplyItemResponse obj = listitem.get(i);
				vhitem.tv_name.setText((i + 1) + "");
				vhitem.tv_faction.setText(obj.getName());
				vhitem.tv_num.setText(obj.getNum() + "支");
				listView.addView(convertView);
			}
		}

		protected void updateRecordState(String id, final int position) {
			LoadingDailog.show(activity, "数据加载中，请稍候。。。");
			if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
				ToastUtils.showShort(activity.getResources().getString(
						R.string.no_networks_found));
				LoadingDailog.dismiss();
				return;
			}
			UpdateRecordStateRequest request = new UpdateRecordStateRequest(""
					+ UserManager.getUserID(), id);
			MaterialApi.updateRecordStateDelivery(request,
					new RequestCallBack<String>() {

						@Override
						public void onSuccess(ResponseInfo<String> responseInfo) {
							new LoadingViewUtil(view).hint();
							LoadingDailog.dismiss();
							BaseResponse result = BaseResponse
									.getBaseResponse(responseInfo.result);
							if (result == null) {
								return;
							}
							lv_refresh.onRefreshComplete();
							String[] split = result.getMsg().split("\\|");
							if ("1".equals(split[0])) {
								ToastUtils.showShort(split[1] + "");
							}
							if (result.getCode() == 1000) {
								msgList.remove(position);
								mAdapter.notifyDataSetChanged();
							} else {
								ev.setErrState();
							}
						}

						@Override
						public void onFailure(HttpException error, String msg) {
							new LoadingViewUtil(view).hint();
							LoadingDailog.dismiss();
							ev.setErrState();
							lv_refresh.onRefreshComplete();
							ToastUtils.showShort(activity.getResources()
									.getString(R.string.request_failure));
						}
					});

		}

	}

	static class ViewHolder {

		@ViewInject(R.id.materialdoing_listitem_textview_time)
		TextView tv_time;
		@ViewInject(R.id.materialdoing_listitem_textview_address)
		TextView tv_address;
		@ViewInject(R.id.materialdoing_listitem_textview_ok)
		TextView tv_ok;
		@ViewInject(R.id.materialdoing_listitem_listview)
		LinearLayout listView;

		void findView(View convertView) {
			ViewUtils.inject(this, convertView);
		}
	}

	static class ViewitemHolder {

		@ViewInject(R.id.materialdoing_listitem_textview_name)
		TextView tv_name;
		@ViewInject(R.id.materialdoing_listitem_textview_faction)
		TextView tv_faction;
		@ViewInject(R.id.materialdoing_listitem_textview_num)
		TextView tv_num;

		void findView(View convertView) {
			ViewUtils.inject(this, convertView);
		}
	}
}
