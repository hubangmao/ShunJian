package cc.ruit.shunjianmei.home.schedule;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.oruit.widget.loadingdialog.LoadingDailog;
import com.oruit.widget.title.TitleUtil;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cc.ruit.shunjianmei.base.BaseActivity;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.net.api.OrderApi;
import cc.ruit.shunjianmei.net.request.H_GetMasterDefaultSettingRequest;
import cc.ruit.shunjianmei.net.request.H_QuickSettingRequest;
import cc.ruit.shunjianmei.net.request.H_SetMasterDefaultSettingRequest;
import cc.ruit.shunjianmei.net.response.BusinessAreaStoreListResponse;
import cc.ruit.shunjianmei.net.response.H_GetMasterDefaultSettingResponse;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmei.util.MyEventBus;
import cc.ruit.shunjianmeihairdresser.R;
import cc.ruit.utils.sdk.ToastUtils;
import cc.ruit.utils.sdk.http.NetWorkUtils;
import de.greenrobot.event.EventBus;

public class ScheduleSettingActivity extends BaseActivity implements OnClickListener {

	@ViewInject(R.id.textView01)
	TextView tvWeek1;
	@ViewInject(R.id.textView02)
	TextView tvWeek2;
	@ViewInject(R.id.textView03)
	TextView tvWeek3;
	@ViewInject(R.id.textView04)
	TextView tvWeek4;
	@ViewInject(R.id.textView05)
	TextView tvWeek5;
	@ViewInject(R.id.textView06)
	TextView tvWeek6;
	@ViewInject(R.id.textView07)
	TextView tvWeek7;
	@ViewInject(R.id.imageButton1)
	TextView tvHour10;
	@ViewInject(R.id.ImageButton01)
	TextView tvHour11;
	@ViewInject(R.id.ImageButton02)
	TextView tvHour12;
	@ViewInject(R.id.ImageButton03)
	TextView tvHour13;
	@ViewInject(R.id.ImageButton04)
	TextView tvHour14;
	@ViewInject(R.id.ImageButton05)
	TextView tvHour15;
	@ViewInject(R.id.ImageButton06)
	TextView tvHour16;
	@ViewInject(R.id.ImageButton07)
	TextView tvHour17;
	@ViewInject(R.id.ImageButton08)
	TextView tvHour18;
	@ViewInject(R.id.ImageButton09)
	TextView tvHour19;
	@ViewInject(R.id.ImageButton10)
	TextView tvHour20;
	@ViewInject(R.id.ImageButton11)
	TextView tvHour21;
	@ViewInject(R.id.tv_rl_ll_shedule_setting_store)
	TextView tvShopName;
	@ViewInject(R.id.tv_rl_ll_shedule_setting_store_address)
	TextView tvShopAddress;
	@ViewInject(R.id.schedule_setting_businesslist_imageview_item)
	ImageView tvImage;
	@ViewInject(R.id.rl_ll_shedule_setting_store_address)
	private LinearLayout linearlayout_address;// 店铺名称及地址布局
	private List<H_GetMasterDefaultSettingResponse> responseList = new ArrayList<H_GetMasterDefaultSettingResponse>();// 响应的结果
	public static final int BUSINESSLISTCODE = 1000;
	private ArrayList<BusinessAreaStoreListResponse> businessImp;

	private StringBuffer DayWorkHour = new StringBuffer();// 每个工作日的工作时间点
	private StringBuffer WorkDays = new StringBuffer();// 工作日信息
	private String WorkShopID = "";// 工作的美发店ID
	private String Address;// 工作的美发店地址
	private String WorkShop;// 工作的美发店名

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule_setting_layout);
		ViewUtils.inject(this);
		initTitle();
		initData();
		initEventBus();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {

		getDataMsg();
	}

	/**
	 * 获取数据
	 */
	private void getDataMsg() {
		if (!NetWorkUtils.checkNetworkAvailable1(this)) {
			ToastUtils.showShort(this.getResources().getString(R.string.no_networks_found));
			return;
		}
		H_GetMasterDefaultSettingRequest request = new H_GetMasterDefaultSettingRequest("" + UserManager.getUserID());
		OrderApi.GetMasterDefaultSettingRequest(request, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				BaseResponse result = BaseResponse.getBaseResponse(responseInfo.result);
				if (result == null) {
					return;
				}
				if (result.getCode() == 1000) {
					responseList = H_GetMasterDefaultSettingResponse.getclazz2(result.getData());
					if (responseList.get(0).getWorkDay() != null && responseList.get(0).getDayWorkHour() != null) {
						ShowData();
					}

				}
				if (result.getCode() == 1200) {
					ToastUtils.showShort(R.string.request_failure);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				ToastUtils.showShort(R.string.request_failure);
			}
		});
	}

	boolean isWork = false;

	@Override
	@OnClick({ R.id.imageButton1, R.id.ImageButton01, R.id.ImageButton02, R.id.ImageButton03, R.id.ImageButton04,
			R.id.ImageButton05, R.id.ImageButton06, R.id.ImageButton07, R.id.ImageButton08, R.id.ImageButton09,
			R.id.ImageButton10, R.id.ImageButton11, R.id.textView01, R.id.textView02, R.id.textView03, R.id.textView04,
			R.id.textView05, R.id.textView06, R.id.textView07, R.id.schedule_setting_button_save,
			R.id.rl_ll_shedule_setting_store_address })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_ll_shedule_setting_store_address:// 跳转地址界面
			startActivityForResult(new Intent(this, BusinessAreaStoreListActivity.class), BUSINESSLISTCODE);
			break;
		case R.id.schedule_setting_button_save:// 保存
			getWorkWeek();
			getWorkHour();
			checkSaveParames();
			if (WorkDays.length() > 0) {
				WorkDays.delete(0, WorkDays.length());
			}
			if (DayWorkHour.length() > 0) {
				DayWorkHour.delete(0, DayWorkHour.length());
			}
			break;
		case R.id.textView01:
			if (tvWeek1.getText().equals("周一(工作)")) {
				isWork = true;
			} else {
				isWork = false;
			}
			if (isWork) {
				tvWeek1.setText("周一(休息)");
				tvWeek1.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_gray);
				isWork = false;

			} else {
				tvWeek1.setText("周一(工作)");
				tvWeek1.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_blue);
				isWork = true;
			}
			break;
		case R.id.textView02:
			if (tvWeek2.getText().equals("周二(工作)")) {
				isWork = true;
			} else {
				isWork = false;
			}
			if (isWork) {
				tvWeek2.setText("周二(休息)");
				tvWeek2.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_gray);
				isWork = false;
			} else {
				tvWeek2.setText("周二(工作)");
				tvWeek2.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_blue);
				isWork = true;
			}
			break;
		case R.id.textView03:
			if (tvWeek3.getText().equals("周三(工作)")) {
				isWork = true;
			} else {
				isWork = false;
			}
			if (isWork) {
				tvWeek3.setText("周三(休息)");
				tvWeek3.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_gray);
				isWork = false;
			} else {
				tvWeek3.setText("周三(工作)");
				tvWeek3.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_blue);
				isWork = true;
			}
			break;
		case R.id.textView04:
			if (tvWeek4.getText().equals("周四(工作)")) {
				isWork = true;
			} else {
				isWork = false;
			}
			if (isWork) {
				tvWeek4.setText("周四(休息)");
				tvWeek4.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_gray);
				isWork = false;
			} else {
				tvWeek4.setText("周四(工作)");
				tvWeek4.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_blue);
				isWork = true;
			}
			break;
		case R.id.textView05:
			if (tvWeek5.getText().equals("周五(工作)")) {
				isWork = true;
			} else {
				isWork = false;
			}
			if (isWork) {
				tvWeek5.setText("周五(休息)");
				tvWeek5.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_gray);
				isWork = false;
			} else {
				tvWeek5.setText("周五(工作)");
				tvWeek5.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_blue);
				isWork = true;
			}
			break;
		case R.id.textView06:
			if (tvWeek6.getText().equals("周六(工作)")) {
				isWork = true;
			} else {
				isWork = false;
			}
			if (isWork) {
				tvWeek6.setText("周六(休息)");
				tvWeek6.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_gray);
				isWork = false;
			} else {
				tvWeek6.setText("周六(工作)");
				tvWeek6.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_blue);
				isWork = true;
			}
			break;
		case R.id.textView07:
			if (tvWeek7.getText().equals("周日(工作)")) {
				isWork = true;
			} else {
				isWork = false;
			}
			if (isWork) {
				tvWeek7.setText("周日(休息)");
				tvWeek7.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_gray);
				isWork = false;
			} else {
				tvWeek7.setText("周日(工作)");
				tvWeek7.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_blue);
				isWork = true;
			}
			break;
		case R.id.imageButton1:
			if (tvHour10.getText().equals("10:00(工作)")) {
				isWork = true;
			} else {
				isWork = false;
			}
			if (isWork) {
				tvHour10.setText("10:00(休息)");
				tvHour10.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_gray);
				isWork = false;
			} else {
				tvHour10.setText("10:00(工作)");
				tvHour10.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_blue);
				isWork = true;
			}
			break;
		case R.id.ImageButton01:
			if (tvHour11.getText().equals("11:00(工作)")) {
				isWork = true;
			} else {
				isWork = false;
			}
			if (isWork) {
				tvHour11.setText("11:00(休息)");
				tvHour11.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_gray);
				isWork = false;
			} else {
				tvHour11.setText("11:00(工作)");
				tvHour11.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_blue);
				isWork = true;
			}
			break;
		case R.id.ImageButton02:
			if (tvHour12.getText().equals("12:00(工作)")) {
				isWork = true;
			} else {
				isWork = false;
			}
			if (isWork) {
				tvHour12.setText("12:00(休息)");
				tvHour12.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_gray);
				isWork = false;
			} else {
				tvHour12.setText("12:00(工作)");
				tvHour12.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_blue);
				isWork = true;
			}
			break;
		case R.id.ImageButton03:
			if (tvHour13.getText().equals("13:00(工作)")) {
				isWork = true;
			} else {
				isWork = false;
			}
			if (isWork) {
				tvHour13.setText("13:00(休息)");
				tvHour13.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_gray);
				isWork = false;
			} else {
				tvHour13.setText("13:00(工作)");
				tvHour13.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_blue);
				isWork = true;
			}
			break;
		case R.id.ImageButton04:
			if (tvHour14.getText().equals("14:00(工作)")) {
				isWork = true;
			} else {
				isWork = false;
			}
			if (isWork) {
				tvHour14.setText("14:00(休息)");
				tvHour14.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_gray);
				isWork = false;
			} else {
				tvHour14.setText("14:00(工作)");
				tvHour14.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_blue);
				isWork = true;
			}
			break;
		case R.id.ImageButton05:
			if (tvHour15.getText().equals("15:00(工作)")) {
				isWork = true;
			} else {
				isWork = false;
			}
			if (isWork) {
				tvHour15.setText("15:00(休息)");
				tvHour15.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_gray);
				isWork = false;
			} else {
				tvHour15.setText("15:00(工作)");
				tvHour15.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_blue);
				isWork = true;
			}
			break;
		case R.id.ImageButton06:
			if (tvHour16.getText().equals("16:00(工作)")) {
				isWork = true;
			} else {
				isWork = false;
			}
			if (isWork) {
				tvHour16.setText("16:00(休息)");
				tvHour16.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_gray);
				isWork = false;
			} else {
				tvHour16.setText("16:00(工作)");
				tvHour16.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_blue);
				isWork = true;
			}
			break;
		case R.id.ImageButton07:
			if (tvHour17.getText().equals("17:00(工作)")) {
				isWork = true;
			} else {
				isWork = false;
			}
			if (isWork) {
				tvHour17.setText("17:00(休息)");
				tvHour17.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_gray);
				isWork = false;
			} else {
				tvHour17.setText("17:00(工作)");
				tvHour17.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_blue);
				isWork = true;
			}
			break;
		case R.id.ImageButton08:
			if (tvHour18.getText().equals("18:00(工作)")) {
				isWork = true;
			} else {
				isWork = false;
			}
			if (isWork) {
				tvHour18.setText("18:00(休息)");
				tvHour18.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_gray);
				isWork = false;
			} else {
				tvHour18.setText("18:00(工作)");
				tvHour18.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_blue);
				isWork = true;
			}
			break;
		case R.id.ImageButton09:
			if (tvHour19.getText().equals("19:00(工作)")) {
				isWork = true;
			} else {
				isWork = false;
			}
			if (isWork) {
				tvHour19.setText("19:00(休息)");
				tvHour19.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_gray);
				isWork = false;
			} else {
				tvHour19.setText("19:00(工作)");
				tvHour19.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_blue);
				isWork = true;
			}
			break;
		case R.id.ImageButton10:
			if (tvHour20.getText().equals("20:00(工作)")) {
				isWork = true;
			} else {
				isWork = false;
			}
			if (isWork) {
				tvHour20.setText("20:00(休息)");
				tvHour20.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_gray);
				isWork = false;
			} else {
				tvHour20.setText("20:00(工作)");
				tvHour20.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_blue);
				isWork = true;
			}
			break;
		case R.id.ImageButton11:
			if (tvHour21.getText().equals("21:00(工作)")) {
				isWork = true;
			} else {
				isWork = false;
			}
			if (isWork) {
				tvHour21.setText("21:00(休息)");
				tvHour21.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_gray);
				isWork = false;
			} else {
				tvHour21.setText("21:00(工作)");
				tvHour21.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_blue);
				isWork = true;
			}
			break;

		}
	}

	String workdays;
	String dayWorkHour;

	/**
	 * 保存数据，接口
	 */
	private void SaveMasterDefaultSetting() {
		if (!NetWorkUtils.checkNetworkAvailable1(this)) {
			ToastUtils.showShort(this.getResources().getString(R.string.no_networks_found));
			return;
		}
		workdays = WorkDays.toString().substring(0, WorkDays.length() - 1);
		dayWorkHour = DayWorkHour.toString().substring(0, DayWorkHour.length() - 1);
		WorkShop = tvShopName.getText().toString();
		Address = tvShopAddress.getText().toString();
		if (responseList.get(0).getWorkShopID() != null && !responseList.get(0).getWorkShopID().equals("")
				&& tagID != "tagID") {
			WorkShopID = responseList.get(0).getWorkShopID();
		}
		LoadingDailog.show(this, "正在保存中，请稍候。。。");
		H_SetMasterDefaultSettingRequest request = new H_SetMasterDefaultSettingRequest("" + UserManager.getUserID(),
				workdays, dayWorkHour, WorkShopID, WorkShop, Address);
		OrderApi.SetMasterDefaultSetting(request, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				BaseResponse result = BaseResponse.getBaseResponse(responseInfo.result);
				if (result.getCode() == 1000) {
					QuickSetting();
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {

			}
		});

	}

	private void QuickSetting() {
		if (!NetWorkUtils.checkNetworkAvailable1(this)) {
			ToastUtils.showShort(this.getResources().getString(R.string.no_networks_found));
			return;
		}
		H_QuickSettingRequest request = new H_QuickSettingRequest("" + UserManager.getUserID());
		OrderApi.SaveQuickSetting(request, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				BaseResponse result = BaseResponse.getBaseResponse(responseInfo.result);
				if (result.getCode() == 1000) {
					LoadingDailog.dismiss();
					ToastUtils.showShort("保存成功");
					tagID = "";
				}

			}

			@Override
			public void onFailure(HttpException error, String msg) {
				LoadingDailog.dismiss();
				ToastUtils.showShort("保存失败");

			}
		});
	}

	private void checkSaveParames() {
		if (WorkDays == null || WorkDays.length() <= 0) {
			ToastUtils.showShort("请选择工作日");
		} else if (DayWorkHour == null || DayWorkHour.length() <= 0) {
			ToastUtils.showShort("请选择工作时长");
		} else if (tvShopName.getText().toString().equals("工作区域")
				|| tvShopAddress.getText().toString().equals("请点击设置工作地点")) {
			ToastUtils.showShort("请设置工作地址");
		} else {
			SaveMasterDefaultSetting();
		}
	}

	private String getWorkHour() {
		if (tvHour10.getText().equals("10:00(工作)")) {
			DayWorkHour.append("10|");
		}
		if (tvHour11.getText().equals("11:00(工作)")) {
			DayWorkHour.append("11|");
		}
		if (tvHour12.getText().equals("12:00(工作)")) {
			DayWorkHour.append("12|");
		}
		if (tvHour13.getText().equals("13:00(工作)")) {
			DayWorkHour.append("13|");
		}
		if (tvHour14.getText().equals("14:00(工作)")) {
			DayWorkHour.append("14|");
		}
		if (tvHour15.getText().equals("15:00(工作)")) {
			DayWorkHour.append("15|");
		}
		if (tvHour16.getText().equals("16:00(工作)")) {
			DayWorkHour.append("16|");
		}
		if (tvHour17.getText().equals("17:00(工作)")) {
			DayWorkHour.append("17|");
		}
		if (tvHour18.getText().equals("18:00(工作)")) {
			DayWorkHour.append("18|");
		}
		if (tvHour19.getText().equals("19:00(工作)")) {
			DayWorkHour.append("19|");
		}
		if (tvHour20.getText().equals("20:00(工作)")) {
			DayWorkHour.append("20|");
		}
		if (tvHour21.getText().equals("21:00(工作)")) {
			DayWorkHour.append("21|");
		}
		return dayWorkHour;

	}

	private String getWorkWeek() {
		if (tvWeek1.getText().equals("周一(工作)")) {
			WorkDays.append("1|");
		}
		if (tvWeek2.getText().equals("周二(工作)")) {
			WorkDays.append("2|");
		}
		if (tvWeek3.getText().equals("周三(工作)")) {
			WorkDays.append("3|");
		}
		if (tvWeek4.getText().equals("周四(工作)")) {
			WorkDays.append("4|");
		}
		if (tvWeek5.getText().equals("周五(工作)")) {
			WorkDays.append("5|");
		}
		if (tvWeek6.getText().equals("周六(工作)")) {
			WorkDays.append("6|");
		}
		if (tvWeek7.getText().equals("周日(工作)")) {
			WorkDays.append("7|");
		}
		return workdays;

	}

	/**
	 * 将数据显示
	 */
	private void ShowData() {
		// 商店名
		String workShop = responseList.get(0).getWorkShop();
		tvShopName.setText(workShop);
		// 美发店地址
		String address = responseList.get(0).getAddress();
		tvShopAddress.setText(address);
		tvShopAddress.setTextColor(Color.GRAY);
		tvImage.setImageResource(R.drawable.address_red);
		// 星期
		String[] workDay = responseList.get(0).getWorkDay().split("\\|");
		String week = "";
		for (int i = 0; i < workDay.length; i++) {
			week += workDay[i];
		}
		if (!week.contains("1")) {
			tvWeek1.setText("周一(休息)");
			tvWeek1.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_gray);
		}
		if (!week.contains("2")) {
			tvWeek2.setText("周二(休息)");
			tvWeek2.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_gray);
		}
		if (!week.contains("3")) {
			tvWeek3.setText("周三(休息)");
			tvWeek3.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_gray);
		}
		if (!week.contains("4")) {
			tvWeek4.setText("周四(休息)");
			tvWeek4.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_gray);
		}
		if (!week.contains("5")) {
			tvWeek5.setText("周五(休息)");
			tvWeek5.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_gray);
		}
		if (!week.contains("6")) {
			tvWeek6.setText("周六(休息)");
			tvWeek6.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_gray);
		}
		if (!week.contains("7")) {
			tvWeek7.setText("周日(休息)");
			tvWeek7.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_gray);
		}

		// 小时
		String[] workTime = responseList.get(0).getDayWorkHour().split("\\|");
		String hour = "";
		for (int i = 0; i < workTime.length; i++) {
			hour += workTime[i] + ",";
		}
		if (!hour.contains("10")) {
			tvHour10.setText("10:00(休息)");
			tvHour10.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_gray);
		}
		if (!hour.contains("11")) {
			tvHour11.setText("11:00(休息)");
			tvHour11.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_gray);
		}
		if (!hour.contains("12")) {
			tvHour12.setText("12:00(休息)");
			tvHour12.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_gray);
		}
		if (!hour.contains("13")) {
			tvHour13.setText("13:00(休息)");
			tvHour13.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_gray);
		}
		if (!hour.contains("14")) {
			tvHour14.setText("14:00(休息)");
			tvHour14.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_gray);
		}
		if (!hour.contains("15")) {
			tvHour15.setText("15:00(休息)");
			tvHour15.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_gray);
		}
		if (!hour.contains("16")) {
			tvHour16.setText("16:00(休息)");
			tvHour16.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_gray);
		}
		if (!hour.contains("17")) {
			tvHour17.setText("17:00(休息)");
			tvHour17.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_gray);
		}
		if (!hour.contains("18")) {
			tvHour18.setText("18:00(休息)");
			tvHour18.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_gray);
		}
		if (!hour.contains("19")) {
			tvHour19.setText("19:00(休息)");
			tvHour19.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_gray);
		}
		if (!hour.contains("20")) {
			tvHour20.setText("20:00(休息)");
			tvHour20.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_gray);
		}
		if (!hour.contains("21")) {
			tvHour21.setText("21:00(休息)");
			tvHour21.setBackgroundResource(R.drawable.schedule_gridview_item_checkbox_gray);
		}

	}

	private String tagID = "";

	@SuppressWarnings("unchecked")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		LogUtils.e("resultCode = " + resultCode);
		if (resultCode == BUSINESSLISTCODE) {
			businessImp = (ArrayList<BusinessAreaStoreListResponse>) data.getSerializableExtra("BUSINESSLISTCODE");
			tvShopName.setText(businessImp.get(0).getName());
			tvShopAddress.setText(businessImp.get(0).getAddress());
			WorkShopID = businessImp.get(0).getID();
			tagID = "tagID";
			tvImage.setImageResource(R.drawable.address_red);
		}
	}

	/**
	 * 初始化标题
	 */
	private void initTitle() {
		TitleUtil title = new TitleUtil(this);
		title.tv_title.setText("一键设置");
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
	 * 
	 * @Title: initEventBus
	 * @Description: 注册EventBus
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

	/**
	 * 
	 * @Title: onEventMainThread
	 * @Description: 这个方法不能进行耗时操作
	 * @param bus
	 * @return: void
	 */
	public void onEventMainThread(MyEventBus bus) {
		if (!TextUtils.isEmpty(bus.getmMsg()) && bus.getmMsg().equals("刷新订单状态")) {
			initData();
		}
	}
}
