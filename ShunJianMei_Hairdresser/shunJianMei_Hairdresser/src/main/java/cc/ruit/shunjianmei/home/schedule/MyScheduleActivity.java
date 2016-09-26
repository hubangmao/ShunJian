package cc.ruit.shunjianmei.home.schedule;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.oruit.widget.title.TitleUtil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cc.ruit.shunjianmei.base.BaseActivity;
import cc.ruit.shunjianmei.base.BaseAdapter;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.home.order.OrderActivity;
import cc.ruit.shunjianmei.home.order.OrderDetailFragment;
import cc.ruit.shunjianmei.net.api.OrderApi;
import cc.ruit.shunjianmei.net.request.H_DeleteSelfWorkingRequest;
import cc.ruit.shunjianmei.net.request.H_GetDayWorkingRequest;
import cc.ruit.shunjianmei.net.response.H_DeleteSelfWorkingResponse;
import cc.ruit.shunjianmei.net.response.H_GetDayWorkingResponse;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmeihairdresser.R;
import cc.ruit.utils.sdk.ToastUtils;
import cc.ruit.utils.sdk.Util;
import cc.ruit.utils.sdk.http.NetWorkUtils;

public class MyScheduleActivity extends BaseActivity implements OnClickListener, OnItemClickListener {
	@ViewInject(R.id.lv_my_schedule)
	private ListView lvDetails;
	private List<H_GetDayWorkingResponse> responseList = new ArrayList<H_GetDayWorkingResponse>();
	private MyScheduleDetailsAdapter detailsAdapter;
	String Year, Month, Day;// 年，月，日
	private String OrderID;
	public static String ORDER_ID;
	private String Date;
	public static final int OVER_SCROLL_NEVER = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule_my);
		Date = ScheduleFragment.DATE;
		ViewUtils.inject(this);
		initTitle();
		initDate();
	}

	/**
	 * 初始化标题
	 */
	private void initTitle() {
		TitleUtil title = new TitleUtil(this);
		title.tv_title.setText(Date + "日程");
		title.iv_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		title.iv_left.setImageResource(R.drawable.arrow_left_white);
		title.iv_left.setVisibility(View.VISIBLE);
	}

	/**
	 * 初始化数据
	 */
	private void initDate() {
		// 获取年月日
		Year = Date.substring(0, 4);
		Month = Date.substring(5, 7);
		Day = Date.substring(8);
		lvDetails.setOnItemClickListener(this);
		getDateMsg();
	}

	@Override
	@OnClick(R.id.ll_schedule_my_add)
	public void onClick(View v) {
		Intent intent = new Intent(MyScheduleActivity.this, ScheduleCompileActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Date = ScheduleFragment.DATE;
		initDate();
	}

	/**
	 * 发送网络请求，获取数据
	 */
	private void getDateMsg() {

		if (!NetWorkUtils.checkNetworkAvailable1(this)) {
			ToastUtils.showShort(this.getResources().getString(R.string.no_networks_found));
			return;
		}
		H_GetDayWorkingRequest request = new H_GetDayWorkingRequest("" + UserManager.getUserID(), Year, Month, Day);
		OrderApi.GetDayWorkingRequest(request, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				BaseResponse result = BaseResponse.getBaseResponse(responseInfo.result);
				responseList = (List<H_GetDayWorkingResponse>) H_GetDayWorkingResponse.getclazz2(result.getData());
				if (responseList.size() > 0) {
					OrderID = responseList.get(0).getOrderID();
					Show();
				} else if (responseList.size() == 0) {
					detailsAdapter = new MyScheduleDetailsAdapter(getApplicationContext(), responseList);
					lvDetails.setAdapter(detailsAdapter);
					detailsAdapter.notifyDataSetChanged();
					return;
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				ToastUtils.showShort(R.string.request_failure);
			}
		});
	}

	/**
	 * 删除自编辑内容
	 */
	private void DeleteSelfWorking() {
		if (!NetWorkUtils.checkNetworkAvailable1(this)) {
			ToastUtils.showShort(this.getResources().getString(R.string.no_networks_found));
			return;
		}
		H_DeleteSelfWorkingRequest request = new H_DeleteSelfWorkingRequest("" + UserManager.getUserID(), OrderID);
		OrderApi.DeleteSelfWorking(request, new RequestCallBack<String>() {


			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				BaseResponse result = H_DeleteSelfWorkingResponse.getBaseResponse(responseInfo.result);
				if (result.getCode() == 1000) {
					ToastUtils.showShort("删除成功");
					getDateMsg();
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				ToastUtils.showShort("删除失败");
			}
		});
	}

	private void Show() {
		detailsAdapter = new MyScheduleDetailsAdapter(this, responseList);
		lvDetails.setAdapter(detailsAdapter);
		lvDetails.setOverScrollMode(OVER_SCROLL_NEVER);
		detailsAdapter.notifyDataSetChanged();
	}

	private class MyScheduleDetailsAdapter extends BaseAdapter {
		private Context context;
		List<H_GetDayWorkingResponse> resList;

		public MyScheduleDetailsAdapter(Context context, List<H_GetDayWorkingResponse> list) {
			super(context, list);
			this.context = context;
			resList = list;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh = null;
			if (convertView == null) {
				vh = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.item_my_schedule_list, null);
				vh.findView(convertView);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}

			setLabel(vh, position);
			return convertView;
		}

		private void setLabel(ViewHolder vh, final int position) {
			final H_GetDayWorkingResponse getWork = resList.get(position);
			vh.scheduleStartTime.setText(getWork.getStartHour() + ":00");
			vh.scheduleStartTime.setTextColor(Color.parseColor("#D03E3F"));
			vh.scheduleEndTime.setText(getWork.getEndHour() + ":00");
			vh.scheduleEndTime.setTextColor(Color.parseColor("#D03E3F"));
			vh.tvMobile.setText(getWork.getMobile());
			vh.username.setText(getWork.getName());
			vh.btnCall.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Util.openTelDall(MyScheduleActivity.this, getWork.getMobile());

				}
			});
			if ("0".equals(getWork.getType())) {
				vh.scheduleName1.setVisibility(View.VISIBLE);
				vh.btnDelete.setVisibility(View.GONE);
				vh.rl_my_schedule_details.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						ORDER_ID = getWork.getOrderID();
						startActivity(OrderActivity.getIntent(context, OrderDetailFragment.class.getName()));
					}
					
				});
				vh.tvDetails.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						ORDER_ID = getWork.getOrderID();
						startActivity(OrderActivity.getIntent(context, OrderDetailFragment.class.getName()));
					}
				});
			} else if ("1".equals(getWork.getType())) {
				vh.scheduleName.setVisibility(View.VISIBLE);
				vh.tvDetails.setText("编辑 >");
				vh.rl_my_schedule_details.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(MyScheduleActivity.this, ScheduleCompileActivity.class);
						intent.putExtra("OrderID", getWork.getOrderID());
						intent.putExtra("Name", getWork.getName());
						intent.putExtra("Mobile", getWork.getMobile());
						intent.putExtra("StartHour", getWork.getStartHour());
						intent.putExtra("EndHour", getWork.getEndHour());
						intent.putExtra("Type", getWork.getType());
						intent.putExtra("Content", getWork.getContent());
						startActivity(intent);
					}
				});
				vh.tvDetails.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(MyScheduleActivity.this, ScheduleCompileActivity.class);
						intent.putExtra("OrderID", getWork.getOrderID());
						intent.putExtra("Name", getWork.getName());
						intent.putExtra("Mobile", getWork.getMobile());
						intent.putExtra("StartHour", getWork.getStartHour());
						intent.putExtra("EndHour", getWork.getEndHour());
						intent.putExtra("Type", getWork.getType());
						intent.putExtra("Content", getWork.getContent());
						startActivity(intent);
					}

				});
				vh.btnDelete.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						AlertDialog.Builder builder = new AlertDialog.Builder(context);
						builder.setMessage("你确定要删除吗？");
						builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								OrderID = getWork.getOrderID();
								DeleteSelfWorking();
							}
						});
						builder.setNegativeButton("取消", null).show();

					}
				});
			}
		}

	}

	private static class ViewHolder {
		@ViewInject(R.id.tv_ll_my_schedule_startHour)
		TextView scheduleStartTime;
		@ViewInject(R.id.tv_ll_my_schedule_endHour)
		TextView scheduleEndTime;
		@ViewInject(R.id.tv_ll_my_schedule_name)
		TextView scheduleName;
		@ViewInject(R.id.tv_ll_my_schedule_name1)
		TextView scheduleName1;
		@ViewInject(R.id.tv_rl_details_username)
		TextView username;
		@ViewInject(R.id.ib_rl_call)
		ImageButton btnCall;
		@ViewInject(R.id.tv_rl_details)
		TextView tvDetails;
		@ViewInject(R.id.btn_rl_details_delete)
		Button btnDelete;
		@ViewInject(R.id.tv_my_schedule_mobile)
		TextView tvMobile;
		@ViewInject(R.id.rl_item_my_schedule)
		RelativeLayout rl_my_schedule_details;
		void findView(View convertView) {
			ViewUtils.inject(this, convertView);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

	}

}
