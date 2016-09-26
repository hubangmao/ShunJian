package cc.ruit.shunjianmei.home.manager.material;

import java.util.ArrayList;
import java.util.List;

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
import cc.ruit.shunjianmei.net.request.MaterialRecordRequest;
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
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshBase.Mode;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshBase.OnRefreshListener2;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshListView;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshBase.OnRefreshListener;
/**
 * 
 * @ClassName: MaterialHistoryFragment
 * @Description: TODO
 * @author: lizw
 * @date: 2015年11月5日 下午11:28:50
 */
public class MaterialHistoryFragment extends BaseFragment implements OnRefreshListener2<ListView>{
	@ViewInject(R.id.materialdoing_listview)
	private PullToRefreshListView lv_refresh;
	private DoingMaterialListAdapter mAdapter;
	EmptyView ev;// 空载页
	List<MaterialApplyResponse> msgList = new ArrayList<MaterialApplyResponse>();

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.materialdoing_fragment_layout, null);
		ViewUtils.inject(this, view);
		initData();
		return view;
	}

	private void initData() {
		mAdapter = new DoingMaterialListAdapter(activity, msgList);
		lv_refresh.setMode(Mode.BOTH);
		lv_refresh.setAdapter(mAdapter);
		lv_refresh.getRefreshableView().setDividerHeight(
				BaseActivity.dip2px(activity, 5));
		ev = new EmptyView(activity, new OnClickListener() {

			@Override
			public void onClick(View v) {
				LoadingDailog.show(activity, "数据加载中，请稍后");
				getData("0");
			}
		});
		lv_refresh.setOnRefreshListener(this);
		lv_refresh.setEmptyView(ev.getView());
		LoadingDailog.show(activity, "数据加载中，请稍后");
		getData("0");
	}

	/**
	 * @Title: getPoints
	 * @Description: 获取数据
	 * @author: lee
	 * @return: void
	 */
	public void getData(final String id) {
		if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
			ToastUtils.showShort(activity.getResources().getString(
					R.string.no_networks_found));
			LoadingDailog.dismiss();
			lv_refresh.onRefreshComplete();
			ev.setErrState();
			new LoadingViewUtil(view).hint();
			return;
		}
		MaterialRecordRequest request = new MaterialRecordRequest(""
				+ UserManager.getUserID(),id);
		MaterialApi.materialRecordDelivery(request,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						new LoadingViewUtil(view).hint();
						LoadingDailog.dismiss();
						lv_refresh.onRefreshComplete();
						BaseResponse result = BaseResponse
								.getBaseResponse(responseInfo.result);
						if (result == null) {
							return;
						}
						String[] split = result.getMsg().split("\\|");
						if ("1".equals(split[0])) {
							ToastUtils.showShort(split[1] + "");
						}
						if (result.getCode() == 1000) {
							List<MaterialApplyResponse> response = MaterialApplyResponse
									.getclazz2(result.getData());
							resultHanlder(response,id);
						}else if(result.getCode() == 2100){
							if ("0".equals(id)) {
								ev.setNullState();
							}
						}else {
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
	void resultHanlder(List<MaterialApplyResponse> info,String id) {
		if (info == null) {
			LogUtils.e("MessageListResponse err");
			return;
		}
		if ("0".equals(id)) {
			msgList.clear();
		}
		msgList.addAll(info);
		if (msgList.size() > 0) {
			ev.setDataState();
		}else if ("0".equals(id)) {
			ev.setNullState();
		}
		mAdapter.notifyDataSetChanged();

	}
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		getData("0");
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (msgList!=null&&msgList.size()>0) {
			getData(msgList.get(msgList.size()-1).getID());
		}else {
			getData("0");
		}
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
		private void setLable(int position, ViewHolder vh) {
			MaterialApplyResponse obj = getItem(position);
			vh.tv_time.setText(obj.getDate());
			vh.tv_address.setText(obj.getAddress());
			vh.tv_ok.setVisibility(View.GONE);
			List<MaterialApplyItemResponse> listitem = obj.getItem();
			addView(vh.listView, listitem);
		}

		private void addView(LinearLayout listView,
				List<MaterialApplyItemResponse> listitem) {
			LogUtils.e("listitem.size() = " + listitem.size());
//			if (listView.getChildCount() == listitem.size()) {
//				return;
//			}
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
