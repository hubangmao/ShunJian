package cc.ruit.shunjianmei.home.schedule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import cc.ruit.shunjianmei.base.BaseActivity;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.net.api.OrderApi;
import cc.ruit.shunjianmei.net.request.BusinessAreaStoreListRequest;
import cc.ruit.shunjianmei.net.response.BusinessAreaStoreListResponse;
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
import com.oruit.widget.title.TitleUtil;

public class BusinessAreaStoreListActivity extends BaseActivity implements
		OnClickListener {
	EmptyView ev;// 空载页
	@ViewInject(R.id.businesslist_listview)
	public ListView lv_refresh;
	private List<BusinessAreaStoreListResponse> chooseAddress = new ArrayList<BusinessAreaStoreListResponse>();
	private List<BusinessAreaStoreListResponse> responseAddress;
	HashMap<String, Boolean> states = new HashMap<String, Boolean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_businessareastorylit);
		ViewUtils.inject(this);
		initTitle();
		initData();
	}

	/**
	 * @Title: initTitle
	 * @Description: 标题初始化
	 * @author: lee
	 * @return: void
	 */
	private void initTitle() {
		TitleUtil title = new TitleUtil(this);
		title.iv_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (chooseAddress.size() <= 0) {
					finish();
					LogUtils.e("数据是空的");
					return;
				}
				Intent data = new Intent();
				data.putExtra("BUSINESSLISTCODE", (Serializable)chooseAddress);
				String BUSINESSLISTCODEADDRESSID = "";
				for (int i = 0; i < chooseAddress.size(); i++) {
					BUSINESSLISTCODEADDRESSID += chooseAddress.get(i).getID();
					if (i != chooseAddress.size() - 1) {
						BUSINESSLISTCODEADDRESSID += "|";
					}
				}
				LogUtils.e("数据是"+BUSINESSLISTCODEADDRESSID);
				data.putExtra("BUSINESSLISTCODEADDRESSID", BUSINESSLISTCODEADDRESSID);
				setResult(ScheduleFragment.BUSINESSLISTCODE, data);
				finish();
			}
		});
		title.iv_left.setImageResource(R.drawable.back);
		title.iv_left.setVisibility(View.VISIBLE);
		title.tv_title.setText("设置地址");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (chooseAddress.size() <= 0) {
				finish();
				return true;
			}
			Intent data = new Intent();
			data.putExtra("BUSINESSLISTCODE",(Serializable)chooseAddress);
			String BUSINESSLISTCODEADDRESSID = "";
			for (int i = 0; i < chooseAddress.size(); i++) {
				BUSINESSLISTCODEADDRESSID += chooseAddress.get(i).getID();
				if (i != chooseAddress.size() - 1) {
					BUSINESSLISTCODEADDRESSID += "|";
				}
			}
			data.putExtra("BUSINESSLISTCODEADDRESSID", BUSINESSLISTCODEADDRESSID);
			setResult(ScheduleFragment.BUSINESSLISTCODE, data);
			finish();
		}
		return true;
	}

	private void initData() {
		ev = new EmptyView(this, new OnClickListener() {

			@Override
			public void onClick(View v) {
				LoadingDailog.show(BusinessAreaStoreListActivity.this,
						"数据加载中，请稍后");
				getBusinessAreaStoreListMsg("0");
			}
		});
		getBusinessAreaStoreListMsg("0");
		lv_refresh.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		lv_refresh.setEmptyView(ev.getView());
	}

	private void getBusinessAreaStoreListMsg(String mag) {
		if (!NetWorkUtils.checkNetworkAvailable1(this)) {
			ToastUtils.showShort(this.getResources().getString(
					R.string.no_networks_found));
			LoadingDailog.dismiss();
			ev.setErrState();
			new LoadingViewUtil(this).hint();
			return;
		}
		BusinessAreaStoreListRequest request = new BusinessAreaStoreListRequest(
				"" + UserManager.getUserID(), mag);
		OrderApi.BusinessAreaStoreList(request, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				new LoadingViewUtil(BusinessAreaStoreListActivity.this).hint();
				LoadingDailog.dismiss();

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
					responseAddress = BusinessAreaStoreListResponse
							.getclazz2(result.getData());
					if (responseAddress == null) {
						LogUtils.e("MessageListResponse err");
						return;
					} else {
						final ViewitemHolder holder = new ViewitemHolder();
						lv_refresh.setAdapter(new BusinessListAdapter(getApplicationContext(), responseAddress));
					}
				} else {
					ev.setErrState();
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				new LoadingViewUtil(BusinessAreaStoreListActivity.this).hint();
				LoadingDailog.dismiss();
				ToastUtils.showShort(getResources().getString(
						R.string.request_failure));
				ev.setErrState();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		default:
			break;
		}
	}

	/**
	 * @ClassName: BusinessListAdapter
	 * @Description: 商圈地址
	 * @author: lee
	 * @date: 2015年9月11日 下午3:09:35
	 */
	class BusinessListAdapter extends BaseAdapter {

		private Context context;
		private List<BusinessAreaStoreListResponse> list;

		public BusinessListAdapter(Context context,
				List<BusinessAreaStoreListResponse> list) {
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
		public BusinessAreaStoreListResponse getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewitemHolder vh = null;
			if (convertView == null) {
				vh = new ViewitemHolder();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.businesslist_listview_item, null);
				vh.findView(convertView);
				convertView.setTag(vh);
			} else {
				vh = (ViewitemHolder) convertView.getTag();
			}
			// 得到数据和绑定数据
			setitemLable(position, vh);
			return convertView;
		}

		private void setitemLable(final int position, final ViewitemHolder vh) {
			final BusinessAreaStoreListResponse obj = getItem(position);
			vh.tv_name.setText(obj.getName());
			vh.address.setText(obj.getAddress());
			vh.linearlayout_item.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					for (String key : states.keySet()) {
						states.put(key, false);
					}
					states.put(String.valueOf(position), true);
					notifyDataSetChanged();
				}
			});
			boolean res = false;
			if (states.get(String.valueOf(position))==null||states.get(String.valueOf(position))==false) {
				res = false;
			}else{
				res = true;
			}
			vh.radioBtn.setChecked(res);
			if (res) {
				vh.image.setImageResource(R.drawable.address_red);
				chooseAddress.add(responseAddress.get(position));
			}else{
				vh.image.setImageResource(R.drawable.adrees_gray);
				chooseAddress.remove(responseAddress.get(position));
			}
	}
	}
	static class ViewitemHolder {

		@ViewInject(R.id.businesslist_linearlayout_item)
		LinearLayout linearlayout_item;
		@ViewInject(R.id.businesslist_textview_name)
		TextView tv_name;
		@ViewInject(R.id.businesslist_textview_address)
		TextView address;
		@ViewInject(R.id.businesslist_imageview_item)
		ImageView image;
		@ViewInject(R.id.businesslist_radiobtn_item)
		RadioButton radioBtn;

		void findView(View convertView) {
			ViewUtils.inject(this, convertView);
		}
	}
}
